package com.yinker.etl.trans.batch;

import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.model.base.TransStatusLog;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransStatusLogService;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import org.pentaho.di.core.exception.KettleException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.zwork.framework.thirdparty.org.springframework.datasource.util.DataSourceKeyHolder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *  定时调度任务 - ETL抽取配置（全量更新抽取）
 */
public class DpFullUpdateBatchService extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DpFullUpdateBatchService.class);

    /* 转换配置服务类 */
    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    /* 简易转换服务类 */
    @Resource(name = "simpleTrans")
    private SimpleTrans simpleTrans;

    @Resource(name = "transStatusLogService")
    private TransStatusLogService transStatusLogService;

    /**
     * 定时任务方法
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        LOGGER.info("定时调度任务 - ETL抽取配置（全量更新抽取）开始.....");
        JobKey jobKey = context.getJobDetail().getKey();
        String transName = jobKey.getName();
        LOGGER.info("转换名称为【{}】", transName);
        DataSourceKeyHolder.clearDataSourceKey();
        LOGGER.info("查询需要抽取的转换");
        List<TransInfo> transList = getNeedToSyncTransInfoList(transName);
        LOGGER.info("共查出需要转换的条数为【{}】条", transList.size());
        String batchNo = "ZHPC" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + transName.split("_")[4];

        String fullTransType = TransConstants.FULL_TRANS_TYPE_2;
        for (TransInfo etlIfTransInfo : transList) {
            etlIfTransInfo.setBatchNo(batchNo);
            etlIfTransInfo.setJobName(transName);

            //判断此条转换是否正在执行
            List<TransStatusLog> etlTransStatusLogs = transStatusLogService.isTransRunning(etlIfTransInfo.getId().toString());
            if (etlTransStatusLogs != null && etlTransStatusLogs.size() > 0) {
                //插入正在执行的记录
                String log = "此条转换有其他线程(批次:" + etlTransStatusLogs.get(0).getBatchNo() + ",服务器IP：" + etlTransStatusLogs.get(0).getOsName() + ",线程：" + etlTransStatusLogs.get(0).getTransThread() + ")正在执行，本次请求将不进行转换！";
                transStatusLogService.insertRunningTrance(etlIfTransInfo.getId().toString(), etlIfTransInfo.getTransName().toString(), etlIfTransInfo.getBatchNo().toString(), etlIfTransInfo.getJobName().toString(), etlIfTransInfo.getType().toString(), etlIfTransInfo.getTargetTbCode().toString(), fullTransType, log);
            } else {
                // 插入开始记录日志
                String insertLogID = transStatusLogService.insertBeginTrance(etlIfTransInfo.getId().toString(), etlIfTransInfo.getTransName().toString(), etlIfTransInfo.getBatchNo().toString(), etlIfTransInfo.getJobName().toString(), etlIfTransInfo.getType().toString(), etlIfTransInfo.getTargetTbCode().toString(), fullTransType);

                try {
                    LOGGER.info("接口表[" + etlIfTransInfo.getSrcTbCode() + "]抽取开始start...");
                    Long beginTime = System.currentTimeMillis();
                    simpleTrans.fullUpdateTrans(etlIfTransInfo.getId(), etlIfTransInfo.getTransName(), etlIfTransInfo.getSrcDbCode(), etlIfTransInfo.getTargetDbCode(), etlIfTransInfo.getSrcTbCode(),
                            etlIfTransInfo.getTargetTbCode(), etlIfTransInfo.getSrcDescript(), etlIfTransInfo.getTargetDescript(), etlIfTransInfo.getWhereStr(),null,null);
                    // 插入结束日志
                    transStatusLogService.updateEndTrance(insertLogID, (int) (System.currentTimeMillis() - beginTime));
                } catch(KettleException e) {
                    transStatusLogService.updateExceptionTrance(insertLogID, e.toString());
                    e.printStackTrace();
                } catch(Exception e) {
                    transStatusLogService.updateExceptionTrance(insertLogID, e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询需要同步的TransInfo集合
     * @param jobName
     * @return
     */
    private List<TransInfo> getNeedToSyncTransInfoList(String jobName){
        String[] jobNames = jobName.split("_");
        TransInfoQuery query = new TransInfoQuery();
        query.setTransType(TransConstants.TRANS_TYPE_2);
        query.setIsEnable(TransConstants.IS_ENABLE_TRANS_ON);
        // 固定N分钟转换时间
        if(jobNames.length == 5){
            query.setIsDiyQuartz(TransConstants.IS_ENABLE_TRANS_OFF);
            query.setType(jobNames[jobNames.length - 1]);
        }
        // 自定义抽取时间转换
        if(jobNames.length == 6){
            query.setIsDiyQuartz(TransConstants.IS_ENABLE_TRANS_ON);
            query.setType("99");
            query.setId(jobNames[jobNames.length - 1]);
        }
        List<TransInfo> transList = transInfoService.selectByEntity(query);

        // 自定义抽取时间转换，若任务无相应转换，则删除对应任务
        if(jobNames.length == 6 && transList == null){
            try {
                TransInfo info = new TransInfo();
                info.setTransType(query.getTransType());
                info.setId(query.getId());
                transInfoService.deleteQuartz(info);
            }catch (Exception e){
                e.printStackTrace();
                LOGGER.debug("删除任务调度时遇到异常:{}",e.getMessage());
            }
        }

        return transList;
    }

}
