package com.yinker.etl.trans.batch;

import com.yinker.etl.pm.service.PmSystemInfoLogService;
import com.yinker.etl.qrtz.QuartzConstants;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.service.TransInfoService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.zwork.framework.thirdparty.org.springframework.datasource.util.DataSourceKeyHolder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时调度任务 - ETL抽取配置（增量抽取）
 */
public class TableTransBatchService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableTransBatchService.class);

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Resource(name = "pmSystemInfoLogService")
    private PmSystemInfoLogService pmSystemInfoLogService;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;

    @Override
    protected void executeInternal (JobExecutionContext context) {

        LOGGER.info("增量数据抽取start...");
        JobKey jobKey = context.getJobDetail().getKey();
        String transName = jobKey.getName();
        LOGGER.info("转换名称为【{}】", transName);

        String fullTransType = TransConstants.FULL_TRANS_TYPE_1;

        try {

            String systemSn = pmSystemInfoLogService.getSystemSN();
            if (StringUtils.isNotEmpty(jobKey.getGroup()) && (TransConstants.TABLE_TRANS_GROUP_NAME.equals(jobKey.getGroup()) || QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS.equals(jobKey.getGroup()))) {
                DataSourceKeyHolder.clearDataSourceKey();

                LOGGER.info("查询需要抽取的转换");
                List<TransInfo> transList = getNeedToSyncTransInfoList(transName);
                LOGGER.info("共查出需要转换的条数为【{}】条", transList.size());
                String batchNo = "ZHPC" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + transName.split("_")[3];
                LOGGER.info("本次转换的批次编号为：【{}】" , batchNo);
                LOGGER.info("开始发送到MQ");

                for(TransInfo transInfo : transList) {
                    transInfo.setBatchNo(batchNo);
                    transInfo.setJobName(transName);
                    transInfo.setTransType(fullTransType);
                    transInfo.setSystemSn(systemSn);
                    String msg = JSONObject.fromObject(transInfo).toString();
                    LOGGER.info("发送MQ -> 转换名【"+transInfo.getTransName()+"】,报文：" + msg);
                    etlSendService.send(rabbitmqName + "_incrementKey", msg);
                }
                LOGGER.info("发送到MQ结束");
            } else {
                throw new Exception("无效group参数值[" + jobKey.getGroup() + "]");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
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
        query.setTransType(TransConstants.TRANS_TYPE_1);
        query.setIsEnable(TransConstants.IS_ENABLE_TRANS_ON);
        // 固定N分钟转换时间
        if(jobNames.length == 4){
            query.setIsDiyQuartz(TransConstants.IS_ENABLE_TRANS_OFF);
            query.setType(jobNames[jobNames.length - 1]);
        }
        // 自定义抽取时间转换
        if(jobNames.length == 5){
            query.setIsDiyQuartz(TransConstants.IS_ENABLE_TRANS_ON);
            query.setType("99");
            query.setId(jobNames[jobNames.length - 1]);
        }
        List<TransInfo> transList = transInfoService.selectByEntity(query);

        // 自定义抽取时间转换，若任务无相应转换，则删除对应任务
        if(jobNames.length == 5 && transList == null){
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

        if(TransConstants.TRANS_TYPE_1.equals(query.getTransType())){
            for(int i = 0; i < transList.size(); i++) {
                TransInfo transInfo = transList.get(i);
                String maxTimeSQL = "SELECT case when max(last_update_time) is null then '1970-01-01' else date_sub(max(last_update_time), interval 60 second) end as max_value from " + transInfo.getTargetTbCode();
                String excuteSQL = "select COUNT(1) as value_count from " + transInfo.getSrcTbCode() + " where ";
                if (StringUtils.isNotEmpty(transInfo.getWhereStr())) {
                    excuteSQL = excuteSQL  + transInfo.getWhereStr() + " and ";
                }
                excuteSQL = excuteSQL + " last_update_time >";
                if(!transInfoService.isHasUpdateData(transInfo.getSrcDbCode(),transInfo.getTargetDbCode(),maxTimeSQL,excuteSQL,"DATETIME")){
                    transList.remove(transInfo);
                    i--;
                }
            }
        }

        return transList;
    }

}
