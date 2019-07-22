package com.yinker.etl.trans.batch;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.SyncTableInfoQuery;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.SyncTableInfo;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.service.SyncTableInfoService;
import com.yinker.etl.trans.service.TransInfoService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时调度任务 - 同步表结构
 */
public class SyncTableBatchService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTableBatchService.class);

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "syncTableInfoService")
    private SyncTableInfoService syncTableInfoService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;

    /**
     * 定时任务方法
     */
    @Override
    protected void executeInternal (JobExecutionContext context) throws JobExecutionException {
        DataSourceContextHolder.clearDataSourceKey();
        LOGGER.info("表结构同步start....");
        try {
            // 查询所有状态为开启的转换
            LOGGER.info("查询所有状态为开启的转换");
            List<TransInfo> transInfos = getNeedToSyncTableTrans();
            LOGGER.info("查询结束，共查出【{}】条", transInfos.size());
            LOGGER.info("开始批量同步表结构");
            Long beginTime = System.currentTimeMillis();
            for(TransInfo transInfo : transInfos) {
                syncTableInfo(transInfo, transInfo.getPmDataSourceSrcInfo().getDatabaseName(), transInfo.getPmDataSourceTargetInfo().getDatabaseName(), "transInfo", transInfo.getId(), transInfo.getTransName());
            }
            LOGGER.info("同步表结构结束，共耗时【{}】毫秒", (System.currentTimeMillis() - beginTime));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取需要转换的转换
     *
     * @return
     */
    public List<TransInfo> getNeedToSyncTableTrans () {
        TransInfoQuery transInfoQuery = new TransInfoQuery();
        transInfoQuery.setIsEnable(TransConstants.IS_ENABLE_TRANS_ON);
        transInfoQuery.setIsSyncTable(TransConstants.IS_SYNC_TABLE_ON);
        List<TransInfo> srcList = transInfoService.selectByEntity(transInfoQuery);
        return srcList;
    }

    /**
     * 同步表结构信息
     *
     * @param transInfo
     */
    public void syncTableInfo (TransInfo transInfo, String srcDatabaseName, String targetDatabaseName, String from, String transId, String transName) {

        // 查询源表表结构
        LOGGER.info("查询源表信息：数据源[" + transInfo.getSrcDbCode() + "]数据表[" + transInfo.getSrcTbCode() + "]");
        DataSourceContextHolder.setDataSourceKey(transInfo.getSrcDbCode());
        SyncTableInfoQuery srcQuery = new SyncTableInfoQuery();
        srcQuery.setTableSchema(srcDatabaseName);
        srcQuery.setTableName(transInfo.getSrcTbCode());
        List<SyncTableInfo> srcTbList = syncTableInfoService.selectByEntity(srcQuery);
        DataSourceContextHolder.clearDataSourceKey();
        LOGGER.info("源表信息查询结束");

        // 查询目标表结构
        LOGGER.info("开始查询目标表信息：数据源[" + transInfo.getTargetDbCode() + "]数据表[" + transInfo.getTargetTbCode() + "]");
        DataSourceContextHolder.setDataSourceKey(transInfo.getTargetDbCode());
        SyncTableInfoQuery destQuery = new SyncTableInfoQuery();
        destQuery.setTableSchema(targetDatabaseName);
        destQuery.setTableName(transInfo.getTargetTbCode());
        List<SyncTableInfo> destTbList = syncTableInfoService.selectByEntity(destQuery);
        DataSourceContextHolder.clearDataSourceKey();
        LOGGER.info("目标表信息查询结束");
        LOGGER.info("开始表结构同步操作");
        syncTable(srcTbList, destTbList, transInfo.getTargetTbCode(), transInfo.getTargetDbCode(), from, transId, transName);

    }

    /**
     * 对比所有数据表差别 - 并执行sql
     *
     * @param srcList
     * @param removeList
     * @return
     */
    public void syncTable (List<SyncTableInfo> srcList, List<SyncTableInfo> removeList, String targetTbCode, String targetDbCode, String from, String transId, String transName) {
        Map<String, Object> sendMap = new HashMap<>();
        List<Map<String, Object>> sendList = new ArrayList<>();
        Map<String, String> columnKeysMap = new HashMap<>();

        sendMap.put("exeKey", "N"); //是否执行修改主键操作
        sendMap.put("syncInfoAuto", new SyncTableInfo());
        if (null != removeList && removeList.size() > 0) {
            Map<String, SyncTableInfo> map = new HashMap<>();
            for(SyncTableInfo sync : removeList) {
                map.put(sync.getColumnName().toLowerCase(), sync);
                if ("PRI".equals(sync.getColumnKey())) {
                    sendMap.put("exeKey", "Y"); //目标表有主键才能执行drop key操作
                }
                if ("auto_increment".equals(sync.getExtra())) {
                    //是否执行修改主键自增长操作,（只有一个）,目标表在删除主键前务必要删除自增属性
                    sendMap.put("syncInfoAuto", sync);
                }
            }

            for(SyncTableInfo sync : srcList) {
                String columnName = sync.getColumnName().toLowerCase();
                try {
                    if ("PRI".equals(sync.getColumnKey())) {
                        columnKeysMap.put(sync.getKeyOrdinalPosition(), sync.getColumnName());
                    }
                    sync.setTableName(targetTbCode);
                    if (!map.containsKey(columnName)) {
                        Map<String, Object> parmMap = new HashMap<>();
                        parmMap.put("syncInfo", sync);
                        parmMap.put("type", TransConstants.SYNC_TABLE_INFO_ADD);
                        sendList.add(parmMap);
                    } else {
                        if (!"trans_time".equals(sync.getColumnName())
                                && (!map.get(columnName).getColumnType().equals(sync.getColumnType())
                                || !map.get(columnName).getIsNullable().equals(sync.getIsNullable())
                                || !map.get(columnName).getColumnKey().equals(sync.getColumnKey())
                                || !map.get(columnName).getColumnComment().equals(sync.getColumnComment()))) {
                            Map<String, Object> parmMap = new HashMap<>();
                            parmMap.put("syncInfo", sync);
                            parmMap.put("type", TransConstants.SYNC_TABLE_INFO_UPDATE);
                            sendList.add(parmMap);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    LOGGER.error("表结构同步异常" + e.getMessage());
                }
            }
        }
        if (sendList.size() > 0) {
            LOGGER.info("将表结构同步信息发送到MQ");
            if (!targetTbCode.endsWith("_his") && !"transTableStructure".equals(from)) { //目前自定义表结构会自动生成主键
                sendMap.put("columnKeys", columnKeysMap);
            } else {
                sendMap.put("exeKey", "N"); //历史表以及自定义表结构 不执行修改drop key操作
            }

            sendMap.put("tbCode", targetTbCode);
            sendMap.put("dbCode", targetDbCode);
            sendMap.put("data", sendList);
            sendMap.put("transId", transId);
            sendMap.put("transName", transName);

            String obj = JSON.toJSON(sendMap).toString();
            LOGGER.info("同步表结构发送报文为：" + obj);
            etlSendService.send(rabbitmqName + "_synctableKey", obj);
            LOGGER.info("消息发送结束");
        }
    }
}
