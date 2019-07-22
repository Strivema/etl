package com.yinker.etl.trans.batch;

import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.service.TransInfoService;
import net.sf.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ETL抽取转换 - 自定义时间抽取执行类
 * Created by 崔博文 on 2017/11/13.18:47
 */
public class DiyCronTransBatchService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiyCronTransBatchService.class);

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;

    @Override
    protected void executeInternal (JobExecutionContext context) throws JobExecutionException {
        DataSourceContextHolder.clearDataSourceKey();
        LOGGER.info("ETL抽取转换 - 自定义时间抽取执行类跑批转换开始。。。");
        JobKey jobKey = context.getJobDetail().getKey();
        String transName = jobKey.getName();
        LOGGER.info("转换名称为【{}】", transName);

        TransInfoQuery transQuery = new TransInfoQuery();
        transQuery.setId(transName.split("_")[transName.split("_").length - 1]);
        transQuery.setTransType(TransConstants.TRANS_TYPE_1);
        transQuery.setIsEnable(TransConstants.IS_ENABLE_TRANS_ON);
        transQuery.setIsDiyQuartz(TransConstants.IS_ENABLE_TRANS_ON);
        List<TransInfo> transList = transInfoService.selectByEntity(transQuery);

        LOGGER.info("一共找到{}条符合条件的数据", transList.size());
        String batchNo = "ZHPC" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        LOGGER.info("本次转换的批次编号为：【{}】", batchNo);
        LOGGER.info("开始发送到MQ");
        for(TransInfo transInfo : transList) {
            transInfo.setBatchNo(batchNo);
            String msg = JSONObject.fromObject(transInfo).toString();
            etlSendService.send(rabbitmqName + "_incrementKey", msg);
        }
        LOGGER.info("发送到MQ结束");
    }
}
