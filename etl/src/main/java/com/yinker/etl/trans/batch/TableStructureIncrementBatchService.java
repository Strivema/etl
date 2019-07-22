package com.yinker.etl.trans.batch;

import com.yinker.etl.qrtz.QuartzConstants;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransTableStructureInfoQuery;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 定时调度任务 - 自定义结构抽取（增量抽取）
 */
public class TableStructureIncrementBatchService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableStructureIncrementBatchService.class);

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    @Resource(name="transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;

    @Override
    protected void executeInternal (JobExecutionContext context) {
        LOGGER.info("定时调度任务 ：自定义结构抽取 - 增量抽取跑批任务");
        JobKey jobKey = context.getJobDetail().getKey();
        String transName = jobKey.getName();
        LOGGER.info("转换名称为【{}】", transName);

        String fullTransType = TransConstants.FULL_TRANS_TYPE_7;

        try {
            if (StringUtils.isNotEmpty(jobKey.getGroup()) && (TransConstants.TABLE_TRANS_GROUP_NAME.equals(jobKey.getGroup()) || QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS.equals(jobKey.getGroup()))) {
                DataSourceKeyHolder.clearDataSourceKey();

                LOGGER.info("查询需要抽取的转换");
                List<TransTableStructureInfo> transList = getNeedToSyncList(transName);
                LOGGER.info("共查出需要转换的条数为【{}】条", transList.size());
                String batchNo = "TTZHPC" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + transName.split("_")[3];
                LOGGER.info("本次转换的批次编号为：【{}】" , batchNo);
                LOGGER.info("开始发送到MQ");

                for(TransTableStructureInfo transInfo : transList) {
                    TransTableStructureInfo info = new TransTableStructureInfo();
                    info.setId(transInfo.getId());
                    info.setBatchNo(batchNo);
                    info.setJobName(transName);
                    info.setTransType(fullTransType);
                    String msg = JSONObject.fromObject(info).toString();
                    LOGGER.info("发送MQ -> 转换名【"+transInfo.getTransName()+"】,报文：" + msg);
                    etlSendService.send(rabbitmqName + "_tableStructureIncrementKey", msg);
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
     * 查询需要同步的集合
     * @param jobName
     * @return
     */
    private List<TransTableStructureInfo> getNeedToSyncList(String jobName) throws Exception {
        String[] jobNames = jobName.split("_");
        TransTableStructureInfoQuery query = new TransTableStructureInfoQuery();
        query.setTransType(TransConstants.TRANS_TYPE_1);
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
        List<TransTableStructureInfo> transList = transTableStructureInfoService.selectByEntity(query);

        // 自定义抽取时间转换，若任务无相应转换，则删除对应任务
        if(jobNames.length == 6 && transList == null){
            try {
                TransTableStructureInfo info = new TransTableStructureInfo();
                info.setTransType(query.getTransType());
                info.setId(query.getId());
                transTableStructureInfoService.deleteQuartz(info);
            }catch (Exception e){
                e.printStackTrace();
                LOGGER.debug("删除任务调度时遇到异常:{}",e.getMessage());
            }
        }

        if (TransConstants.TRANS_TYPE_1.equals(query.getTransType())) {
            for(int i = 0; i < transList.size(); i++) {
                TransTableStructureInfo transInfo = transList.get(i);
                // 获取增量SQL
                String maxTimeSQL = "";
                String maxType ="";
                // 获取字段类型
                String columnType = transTableStructureInfoService.getColumnType(transInfo.getPmDataSourceSrcInfo(), transInfo.getSqlstr(), transInfo.getIncrementColumn());
                LOGGER.info("增量字段[" + transInfo.getIncrementColumn() + "]的数据库类型为[" + columnType + "]");

                if (Arrays.binarySearch(TransConstants.COLUMN_TYPE_INT, columnType) >= 0) {
                    // 增量字段类型为Int类型的处理SQL
                    maxTimeSQL = "select CASE WHEN MAX(" + transInfo.getIncrementColumn() + ") is NULL THEN 0 ELSE MAX(" + transInfo.getIncrementColumn() + ") END AS max_value from " + transInfo.getTargetTbCode();
                    maxType = "INT";
                }
                if (Arrays.binarySearch(TransConstants.COLUMN_TYPE_DATE, columnType) >= 0) {
                    // 增量字段为Date类型的处理SQL
                    maxTimeSQL = "SELECT case when max(" + transInfo.getIncrementColumn() + ") is null then '1970-01-01' else date_sub(max(" + transInfo.getIncrementColumn() + "), interval 60 second) end as max_value from " + transInfo.getTargetTbCode();
                    maxType = "DATETIME";
                }
                LOGGER.info(" ----- 获取增量字段SQL：" + maxTimeSQL);

                // 获取执行SQL
                String excuteSQL = "SELECT  COUNT(1)  as value_count FROM (" + transInfo.getSqlstr() + ") AS EXCSQ WHERE EXCSQ." + transInfo.getIncrementColumn() + " >";
                if (!transInfoService.isHasUpdateData(transInfo.getSrcDbCode(), transInfo.getTargetDbCode(), maxTimeSQL, excuteSQL,maxType)) {
                    transList.remove(transInfo);
                    i--;
                }
            }
        }

        return transList;
    }

}
