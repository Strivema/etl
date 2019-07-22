package com.yinker.etl.trans.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.pm.model.PmErrorTableStructureLogQuery;
import com.yinker.etl.pm.model.base.PmErrorTableStructureLog;
import com.yinker.etl.pm.service.PmErrorTableStructureLogService;
import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.dao.SyncTableInfoDao;
import com.yinker.etl.trans.model.SyncTableInfoQuery;
import com.yinker.etl.trans.model.base.SyncTableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.pm.PMManager;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步表结构
 *
 * @author Lenovo
 */
public class SyncTableInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTableInfoService.class);

    @Resource(name = "syncTableInfoDao")
    private SyncTableInfoDao syncTableInfoDao;

    @Resource(name = "pmErrorTableStructureLogService")
    private PmErrorTableStructureLogService pmErrorTableStructureLogService;

    @Resource(name = "etlKeyGeneratorContainerPm")
    private KeyGeneratorContainer keyGeneratorContainer;

    public List<SyncTableInfo> selectByEntity (SyncTableInfoQuery entity) {
        return syncTableInfoDao.selectByEntity(entity);
    }

    /**
     * 表结构同步去掉auto_increament属性
     *
     * @param map
     */
    private void exeAutoMove (Map map) {
        syncTableInfoDao.exeAutoMove(map);
    }

    /**
     * 表结构同步去除目标表中的主键
     *
     * @param tableName
     */
    public void dropKeys (String tableName) {
        syncTableInfoDao.dropKeys(tableName);
    }

    public void addKeys (Map<String, String> addKeyMap) {
        syncTableInfoDao.addKeys(addKeyMap);
    }

    public int executeAlterSqlBanKey (SyncTableInfo entity) {
        return syncTableInfoDao.executeAlterSqlBanKey(entity);
    }

    public int executeModifySqlBanKey (SyncTableInfo entity) {
        return syncTableInfoDao.executeModifySqlBanKey(entity);
    }

    public int createDpTable (SyncTableInfo entity) {
        return syncTableInfoDao.createDpTable(entity);
    }


    public List<String> selectTablesList (SyncTableInfo entity) {
        return syncTableInfoDao.selectTablesList(entity);
    }

    /**
     * 在此数据源中此表是否存在
     *
     * @param dataBaseCode 数据库Code
     * @param tableName    表名称
     * @return
     */
    public boolean existsTableInDatabase (String dataBaseCode, String tableName) {
        SyncTableInfo syncTableInfo = new SyncTableInfo();
        syncTableInfo.setTableName(tableName);
        syncTableInfo.setTableSchema(dataBaseCode);
        List<String> tablesList = selectTablesList(syncTableInfo);
        return tablesList != null && tablesList.size() > 0;
    }

    /**
     * 表结构同步接收端方法
     *
     * @param json
     */
    public void syncTableTrans (JSONObject json) {
        LOGGER.info("表结构同步接收数据...");
        LOGGER.info("接收到同步报文为：" + json.toString());
        // 接收到报文序列化
        Map<String, Object> sendMap = JSONObject.parseObject(json.toString());
        // 目标表数据源
        String dbCode = sendMap.get("dbCode").toString();
        SyncTableInfo sync;
        SyncTableInfo syncAuto;
        try {
            DataSourceContextHolder.setDataSourceKey(dbCode);
            List<Map<String, Object>> msgList = (List<Map<String, Object>>) sendMap.get("data");
            Map<String, String> columnKeysMap = (Map<String, String>) sendMap.get("columnKeys");
            String tableName = sendMap.get("tbCode").toString();

            //删除主键的auto_increment属性
            syncAuto = JSON.parseObject(sendMap.get("syncInfoAuto").toString(), SyncTableInfo.class);
            // 不修改目标表的auto_increment属性
            try {
                if (syncAuto != null && StringUtils.isNotEmpty(syncAuto.getColumnName())) {
                    Map map = new HashMap<String, String>();
                    map.put("exeType", "drop");
                    map.put("tableName", tableName);
                    map.put("columnName", syncAuto.getColumnName());
                    map.put("columnType", syncAuto.getColumnType());
                    map.put("isNullable", syncAuto.getIsNullable());
                    map.put("columnComment", syncAuto.getColumnComment());
                    this.exeAutoMove(map);
                }
                //删除主键
                if ("Y".equals(sendMap.get("exeKey").toString())) {
                    this.dropKeys(tableName);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                LOGGER.error("删除主键异常" + ex.getMessage());
            }

            for(Map<String, Object> syncMap : msgList) {
                sync = JSON.parseObject(syncMap.get("syncInfo").toString(), SyncTableInfo.class);
                try {
                    if (TransConstants.SYNC_TABLE_INFO_ADD.equals(syncMap.get("type"))) {
                        this.executeAlterSqlBanKey(sync);
                    } else if (TransConstants.SYNC_TABLE_INFO_UPDATE.equals(syncMap.get("type"))) {
                        this.executeModifySqlBanKey(sync);
                    }
                } catch(Exception e) {
                    try {
                        DataSourceContextHolder.clearDataSourceKey();
                        PmErrorTableStructureLogQuery logQuery = new PmErrorTableStructureLogQuery();
                        logQuery.setErrTableName(sync.getTableName());
                        logQuery.setErrColumnName(sync.getColumnName());
                        logQuery.setErrStatus("0");
                        logQuery.setErrDesc(e.getMessage());
                        List<PmErrorTableStructureLog> etlLog = pmErrorTableStructureLogService.selectByEntity(logQuery);
                        if (etlLog.isEmpty()) {
                            PmErrorTableStructureLog log = new PmErrorTableStructureLog();
                            log.setId(keyGeneratorContainer.getNextKey("pmErrorTableStructureLogId"));
                            log.setErrTransId(String.valueOf(sendMap.get("transId")));
                            log.setErrTransName(String.valueOf(sendMap.get("transName")));
                            log.setErrTableName(sync.getTableName());
                            log.setErrColumnName(sync.getColumnName());
                            log.setErrStatus("0");
                            log.setErrDesc(e.getMessage());
                            log.setRemark("");
                            log.setErrCreateTime(PMManager.getCurrentWorkDateTime());
                            pmErrorTableStructureLogService.insert(log);
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        LOGGER.error("记录异常信息报错" + ex.getMessage());
                    } finally {
                        DataSourceContextHolder.setDataSourceKey(dbCode);
                    }
                }
            }
            try {
                //生成主键
                if (columnKeysMap != null && columnKeysMap.size() > 0) {
                    Map<String, String> addKeyMap = new HashMap<>();
                    addKeyMap.put("tableName", tableName);
                    String keys = "(";
                    for(int i = 1; i <= columnKeysMap.size(); i++) {
                        keys += columnKeysMap.get(i + "") + ",";
                    }
                    addKeyMap.put("keys", keys.substring(0, keys.length() - 1) + ")");
                    this.addKeys(addKeyMap);
                }
                //生成主键的auto_increment属性（不必要）
                if (syncAuto != null && StringUtils.isNotEmpty(syncAuto.getColumnName())) {
                    //不修改目标表的auto_increment属性
                    Map map = new HashMap<String, String>();
                    map.put("exeType", "add");
                    map.put("tableName", tableName);
                    map.put("columnName", syncAuto.getColumnName());
                    map.put("columnType", syncAuto.getColumnType());
                    map.put("isNullable", syncAuto.getIsNullable());
                    map.put("columnComment", syncAuto.getColumnComment());
                    this.exeAutoMove(map);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                LOGGER.error("创建主键异常" + ex.getMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
            LOGGER.error("表结构同步出现异常：" + e.getMessage());
        } finally {
            DataSourceContextHolder.clearDataSourceKey();
        }
    }


}