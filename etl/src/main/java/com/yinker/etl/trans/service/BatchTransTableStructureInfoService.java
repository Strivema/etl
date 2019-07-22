package com.yinker.etl.trans.service;

import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.base.TransStatusLog;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 崔博文 on 2018/1/24.18:51
 */
public class BatchTransTableStructureInfoService extends TransTableStructureInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTransTableStructureInfoService.class);

    @Resource(name = "simpleTrans")
    private SimpleTrans simpleTrans;

    @Resource(name = "transStatusLogService")
    private TransStatusLogService transStatusLogService;

    public void incrementExcute (TransTableStructureInfo tableStructureInfo, String jobName , String batchNo) {

        //判断此条转换是否正在执行
        List<TransStatusLog> etlTransStatusLogs = transStatusLogService.isTransRunning(tableStructureInfo.getId());
        if (etlTransStatusLogs != null && etlTransStatusLogs.size() > 0) {
            //插入正在执行的记录
            String log = "此条转换有其他线程(批次:" + etlTransStatusLogs.get(0).getBatchNo() + ",服务器IP：" + etlTransStatusLogs.get(0).getOsName() + ",线程：" + etlTransStatusLogs.get(0).getTransThread() + ")正在执行，本次请求将不进行转换！";
            LOGGER.info(log);
            transStatusLogService.insertRunningTrance(tableStructureInfo.getId(), tableStructureInfo.getTransName(), batchNo, jobName, tableStructureInfo.getType(), tableStructureInfo.getTargetTbCode(), TransConstants.FULL_TRANS_TYPE_7, log);
        } else {
            // 插入开始记录日志
            String insertLogID = transStatusLogService.insertBeginTrance(tableStructureInfo.getId(), tableStructureInfo.getTransName(), batchNo, jobName,  tableStructureInfo.getType(), tableStructureInfo.getTargetTbCode(), TransConstants.FULL_TRANS_TYPE_7);

            try {
                LOGGER.info("开始执行转换[" + tableStructureInfo.getTransName() + "]抽取开始start...");
                Long beginTime = System.currentTimeMillis();

                // 获取增量SQL
                String maxTimeSQL = getMaxSQL(tableStructureInfo.getPmDataSourceSrcInfo(),tableStructureInfo.getSqlstr(),tableStructureInfo.getIncrementColumn(),tableStructureInfo.getTargetTbCode());
                if(StringUtils.isEmpty(maxTimeSQL)) {
                    throw new Exception("处理增量字段SQL异常！");
                }

                // 获取执行SQL
                String excuteSQL = "SELECT * FROM ("+tableStructureInfo.getSqlstr()+") AS EXCSQ WHERE EXCSQ."+tableStructureInfo.getIncrementColumn()+" >?";

                String[] matchKey = tableStructureInfo.getCompareParameter().split("\\|");
                String[] keyCondition = new String[0];

                // 如果对比条件较多的时候，创建多条对比条件。
                if (matchKey != null && matchKey.length > 0) {
                    keyCondition = new String[matchKey.length];
                    for(int i = 0; i < matchKey.length; i++) {
                        keyCondition[i] = "=";
                    }
                }

                simpleTrans.incrementTrans(tableStructureInfo.getId(),tableStructureInfo.getTransName(),tableStructureInfo.getSrcDbCode(),tableStructureInfo.getTargetDbCode(),tableStructureInfo.getTargetTbCode(),tableStructureInfo.getTargetTbCode(),
                        "自定义结构抽取["+ tableStructureInfo.getTransName()+"]抽取源数据","自定义结构抽取["+ tableStructureInfo.getTransName()+"]插入目标数据",maxTimeSQL,excuteSQL,matchKey,keyCondition, TransConstants.FIELD_DATA_BASE_NOID);

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
