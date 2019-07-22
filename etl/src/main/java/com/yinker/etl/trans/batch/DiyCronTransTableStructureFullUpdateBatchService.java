package com.yinker.etl.trans.batch;

import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransTableStructureInfoQuery;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;

/**
 *  定时调度任务 - 自定义结构抽取配置（全量更新抽取）自定义抽取时间
 */
public class DiyCronTransTableStructureFullUpdateBatchService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiyCronTransTableStructureFullUpdateBatchService.class);

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    /* 简易转换服务类 */
    @Resource(name = "simpleTrans")
    private SimpleTrans simpleTrans;

    @Override
    protected void executeInternal (JobExecutionContext context) throws JobExecutionException {
        DataSourceContextHolder.clearDataSourceKey();
        LOGGER.info("定时调度任务 - 自定义结构抽取配置（全量更新抽取）自定义抽取时间开始。。。");
        JobKey jobKey = context.getJobDetail().getKey();
        String transName = jobKey.getName();
        LOGGER.info("转换名称为【{}】", transName);

        TransTableStructureInfoQuery transQuery = new TransTableStructureInfoQuery();
        transQuery.setId(transName.split("_")[transName.split("_").length - 1]);
        transQuery.setTransType(TransConstants.TRANS_TYPE_2);
        transQuery.setIsEnable(TransConstants.IS_ENABLE_TRANS_ON);
        transQuery.setIsDiyQuartz(TransConstants.IS_ENABLE_TRANS_ON);
        List<TransTableStructureInfo> transList = transTableStructureInfoService.selectByEntity(transQuery);
        try{
            for (TransTableStructureInfo tableStructureInfo : transList) {
                simpleTrans.fullUpdateTrans(tableStructureInfo.getId(), tableStructureInfo.getTransName(), tableStructureInfo.getSrcDbCode(), tableStructureInfo.getTargetDbCode(), tableStructureInfo.getTargetTbCode(),
                        tableStructureInfo.getTargetTbCode(), "源表描述：自定义结构抽取（" + tableStructureInfo.getId() + "）", "目标表描述：自定义结构抽取（" + tableStructureInfo.getId() + "）",
                        "", tableStructureInfo.getSqlstr(),tableStructureInfo.getCompareParameter().split("\\|"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
