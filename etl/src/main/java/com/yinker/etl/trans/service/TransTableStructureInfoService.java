package com.yinker.etl.trans.service;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.action.PmDataSourceAction;
import com.yinker.etl.pm.model.PmDataSourceQuery;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.model.base.PmDataSourcePK;
import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.pm.util.AESUtil;
import com.yinker.etl.pm.util.DataSourceUtil;
import com.yinker.etl.pm.util.FileUtil;
import com.yinker.etl.qrtz.QuartzConstants;
import com.yinker.etl.qrtz.model.base.QuartzSchedule;
import com.yinker.etl.qrtz.service.QuartzScheduleService;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.batch.SyncTableBatchService;
import com.yinker.etl.trans.dao.TransTableStructureInfoDao;
import com.yinker.etl.trans.model.SelectJson;
import com.yinker.etl.trans.model.SyncTableInfoQuery;
import com.yinker.etl.trans.model.base.SyncTableInfo;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.model.base.TransTableStructureInfoPK;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import net.sf.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseService;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 * <b>类描述:</b>TransTableStructureInfo表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-21 15:58:43
 * </pre>
 */
public class TransTableStructureInfoService extends BaseService<TransTableStructureInfoPK, TransTableStructureInfo, TransTableStructureInfoDao> implements
        org.zwork.framework.base.BaseService<TransTableStructureInfoPK, TransTableStructureInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransTableStructureInfoService.class);

    @Resource(name = "pmDataSourceService")
    private PmDataSourceService pmDataSourceService;

    @Resource(name = "simpleTrans")
    private SimpleTrans simpleTrans;

    /* 跑批任务执行类 */
    @Resource(name = "quartzScheduleService")
    private QuartzScheduleService<QuartzSchedule> quartzScheduleService;

    /* 表结构同步服务类 */
    @Resource(name = "syncTableBatchService")
    private SyncTableBatchService syncTableBatchService;

    @Resource(name = "syncTableInfoService")
    private SyncTableInfoService syncTableInfoService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    /**
     * 新增抽取转换配置
     *
     * @param entity
     */
    public void insertTransInfo (TransTableStructureInfo entity) {
        entity = getTableStructureInfoByCheck(entity);
        insert(entity);
    }

    /**
     * 修改抽取转换配置
     *
     * @param entity
     */
    public void updateTransInfo (TransTableStructureInfo entity) {
        entity = getTableStructureInfoByCheck(entity);
        updateByPK(entity);
    }

    /**
     * 处理TransTableStructureInfo 使其可以进行数据库操作处理
     *
     * @param entity
     * @return
     */
    private TransTableStructureInfo getTableStructureInfoByCheck (TransTableStructureInfo entity) {

        if (entity.getTargetTbCode().indexOf(",") != -1) {
            entity.setTargetTbCode(entity.getTargetTbCode().substring(1, entity.getTargetTbCode().length()).trim());
        }

        PmDataSourcePK pmDataSourcePK = new PmDataSourcePK();
        pmDataSourcePK.setId(entity.getSrcDbId()); //查询源数据库信息
        entity.setSrcDbCode(pmDataSourceService.selectByPK(pmDataSourcePK).getCode());
        pmDataSourcePK.setId(entity.getTargetDbId()); //查询目标数据库信息
        entity.setTargetDbCode(pmDataSourceService.selectByPK(pmDataSourcePK).getCode());
        return entity;
    }

    /**
     * 创建ETL转换自定创建跑批任务
     *
     * @param entity
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    public void insertQuartz (TransTableStructureInfo entity) throws ClassNotFoundException, SchedulerException {
        // 转换类型为 全量更新抽取
        if ("Y".equals(entity.getIsDiyQuartz())) {
            // 创建自定义跑批任务
            LOGGER.debug("开始创建自定义跑批任务");
            QuartzSchedule quartz = new QuartzSchedule();
            quartz.setGroup(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS);
            quartz.setCron(entity.getCron());
            if("2".equals(entity.getTransType())){
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_TABLESTRUCT_CLASS_1);
                quartz.setName(QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_1 + entity.getId());
            }else if ("3".equals(entity.getTransType())){
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_TABLESTRUCT_CLASS_2);
                quartz.setName(QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_2 + entity.getId());
            }else if ("1".equals(entity.getTransType())){
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_TABLESTRUCT_CLASS_3);
                quartz.setName(QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_3 + entity.getId());
            }
            quartz.setDescription("自定义调度任务：" + entity.getTransName());
            quartzScheduleService.insert(quartz);
            LOGGER.debug("跑批任务创建完成");
        }
    }

    /**
     * 更新ETL转换的时候判断是否需要更新跑批任务
     *
     * @param entity
     * @throws Exception
     */
    public void updateQuartz (TransTableStructureInfo entity) throws Exception {
        // 转换类型为 增量抽取
        if ("Y".equals(entity.getIsDiyQuartz())) {
            String transName = "";
            if("2".equals(entity.getTransType())){
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_1 + entity.getId();
            }else if ("3".equals(entity.getTransType())){
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_2 + entity.getId();
            }else if ("1".equals(entity.getTransType())){
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_3 + entity.getId();
            }
            LOGGER.debug("删除定时任务", QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + transName);
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + transName);
            if ("Y".equals(entity.getIsDiyQuartz())) {
                QuartzSchedule quartz = new QuartzSchedule();
                quartz.setName(transName);
                quartz.setGroup(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS);
                quartz.setCron(entity.getCron());
                quartz.setDescription("自定义调度任务：" + entity.getTransName());
                if("2".equals(entity.getTransType())){
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_TABLESTRUCT_CLASS_1);
                }else if ("3".equals(entity.getTransType())){
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_TABLESTRUCT_CLASS_2);
                }else if ("1".equals(entity.getTransType())){
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_TABLESTRUCT_CLASS_3);
                }
                LOGGER.debug("新增任务");
                quartzScheduleService.insert(quartz);
            }
        }
    }

    /**
     * 删除任务
     */
    public void deleteQuartz (TransTableStructureInfo entity) throws SchedulerException {
        if("2".equals(entity.getTransType())){
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_1 + entity.getId());
        }else if ("3".equals(entity.getTransType())){
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_2 + entity.getId());
        }else if ("1".equals(entity.getTransType())){
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_3 + entity.getId());
        }
    }

    /**
     * 通过 目标数据源、源数据源、执行SQL 同步表（存在则同步，不存在则创建）
     */
    public Map<String, String> createTargetTableBySQL (String srcDbId, String targetDbId, String sql, String tableName) {
        Map<String, String> resultMap = new HashMap<>();
        PmDataSourcePK pmDataSourcePK = new PmDataSourcePK();
        pmDataSourcePK.setId(srcDbId); //查询源数据库信息
        PmDataSource srcDataSource = pmDataSourceService.selectByPK(pmDataSourcePK);
        pmDataSourcePK.setId(targetDbId); //查询目标数据库信息
        PmDataSource targetDataSource = pmDataSourceService.selectByPK(pmDataSourcePK);
        // 校验SQL准确性
        ResultSet resultSet;
        Connection connection;
        try {
            connection = getConnection(srcDataSource);
            resultSet = checkSQLLimit(connection, sql);
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "Error");
            resultMap.put("message", "SQL校验失败：" + e.getMessage());
            return resultMap;
        }
        // 检验表是否存在，不存在则创建
        DataSourceContextHolder.setDataSourceKey(targetDataSource.getCode());
        Boolean isExists = syncTableInfoService.existsTableInDatabase(targetDataSource.getDatabaseName(), tableName);
        if (!isExists) {
            SyncTableInfo syncTableInfo = new SyncTableInfo();
            syncTableInfo.setIdName(tableName + "_id");
            syncTableInfo.setTableName(tableName);
            syncTableInfoService.createDpTable(syncTableInfo);
        }
        DataSourceContextHolder.clearDataSourceKey();

        // 查询源表结构
        List<SyncTableInfo> srcTableInfos;
        try {
            srcTableInfos = getTableInfo(resultSet, tableName);
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
            resultMap.put("result", "Error");
            resultMap.put("message", "创建表结构异常：" + e.getMessage());
            return resultMap;
        }

        // 查询目标表结构
        DataSourceContextHolder.setDataSourceKey(targetDataSource.getCode());
        SyncTableInfoQuery destQuery = new SyncTableInfoQuery();
        destQuery.setTableSchema(targetDataSource.getCode());
        destQuery.setTableName(tableName);
        List<SyncTableInfo> targetTableInfo = syncTableInfoService.selectByEntity(destQuery);
        DataSourceContextHolder.clearDataSourceKey();

        syncTableBatchService.syncTable(srcTableInfos, targetTableInfo, tableName, targetDataSource.getCode(), "transTableStructure","","");

        resultMap.put("result", "Success");
        resultMap.put("message", "操作成功！");
        return resultMap;
    }

    /**
     * 校验sql
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public ResultSet checkSQL (Connection conn, String sql) throws Exception {
        // 查询信息
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
    }

    /**
     * 只取表结构
     * @param conn
     * @param sql
     * @return
     * @throws Exception
     */
    public ResultSet checkSQLLimit(Connection conn, String sql) throws Exception {
        if(sql.toUpperCase().indexOf("LIMIT")==-1){
            sql += " LIMIT 0";
        }
        return checkSQL(conn,sql);
    }

    /**
     * 根据SQL获取查询字段名
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public List<String> getUCColumns(Connection conn, String sql) throws Exception{
        List<String> columnList = new ArrayList<String>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData() ;
        int colcount = rsmd.getColumnCount();
        for (int i = 1; i <= colcount; i++) {
            columnList.add(rsmd.getColumnLabel(i));
        }
        return columnList;
    }

    /**
     * 根据SQL获取查询增量比较字段名
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public List<SelectJson> getICColumns(Connection conn, String sql) throws Exception{
        List<SelectJson> columnList = new ArrayList<SelectJson>();

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData() ;
        int colcount = rsmd.getColumnCount();
        for (int i = 1; i <= colcount; i++) {
            if("INT".equals(rsmd.getColumnTypeName(i))
                    || "INTEGER".equals(rsmd.getColumnTypeName(i))
                    || "SMALLINT".equals(rsmd.getColumnTypeName(i))
                    || "TINYINT".equals(rsmd.getColumnTypeName(i))
                    || "BIGINT".equals(rsmd.getColumnTypeName(i))
                    || "MEDIUMINT".equals(rsmd.getColumnTypeName(i))
                    || "DATETIME".equals(rsmd.getColumnTypeName(i))
                    || "TIMESTAMP".equals(rsmd.getColumnTypeName(i))){

                SelectJson sj = new SelectJson();
                String columnName = rsmd.getColumnLabel(i);
                sj.setId(columnName);
                sj.setName(columnName);
                columnList.add(sj);
            }
        }
        return columnList;
    }


    /**
     * 获取数据库链接资源
     *
     * @param pmDataSource
     * @return
     */
    public Connection getConnection (PmDataSource pmDataSource) throws Exception {
        Connection conn = null;
        if (PMConstants.DATA_BASE_TYPE_MYSQL.equals(pmDataSource.getDatabaseType())) {
            conn = DataSourceUtil.getMysqlConnection(pmDataSource.getHostName(), String.valueOf(pmDataSource.getPort()), pmDataSource.getUserName(), AESUtil.decrypt(getAESKey(), pmDataSource.getPassword()).trim(), pmDataSource.getDatabaseName());
        }
        if (PMConstants.DATA_BASE_TYPE_ORACLE.equals(pmDataSource.getDatabaseType())) {
            conn = DataSourceUtil.getOracleConnection(pmDataSource.getHostName(), String.valueOf(pmDataSource.getPort()), pmDataSource.getUserName(), AESUtil.decrypt(getAESKey(), pmDataSource.getPassword()).trim(), pmDataSource.getDatabaseName());
        }
        return conn;
    }

    /**
     * 通过传入参数获得字段类型
     * @param dataSource    数据源
     * @param sql           SQL
     * @param column        字段
     * @return
     */
    public String getColumnType(PmDataSource dataSource,String sql,String column) throws Exception {

        if(dataSource != null) {
            Connection conn = getConnection(dataSource);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData() ;
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                if(column.equals(rsmd.getColumnLabel(i))){
                    return rsmd.getColumnTypeName(i);
                }
            }
        }
        return "";
    }

    /**
     * 获取目标表SQL查询结构
     *
     * @param resultSet SQL查询结果集
     * @param tableName 目标表名称
     * @throws SQLException
     */
    public List<SyncTableInfo> getTableInfo (ResultSet resultSet, String tableName) throws SQLException {
        List<SyncTableInfo> syncTableInfos = new ArrayList<>();
        // 遍历结果集
        ResultSetMetaData data = resultSet.getMetaData();
        for(int i = 1; i <= data.getColumnCount(); i++) {
            SyncTableInfo tableInfo = new SyncTableInfo();
            tableInfo.setColumnName(data.getColumnLabel(i));
            tableInfo.setTableName(tableName);
            tableInfo.setIsNullable("YES");
            // 拼接字段类型
            if ("DATETIME".equals(data.getColumnTypeName(i)) || "DATE".equals(data.getColumnTypeName(i))
                    || "TIMESTAMP".equals(data.getColumnTypeName(i)) || "LONGBLOB".equals(data.getColumnTypeName(i))) {
                tableInfo.setColumnType(data.getColumnTypeName(i));
            } else if ("NULL".equals(data.getColumnTypeName(i))) {
                tableInfo.setColumnType("VARCHAR(256)");
            } else {
                tableInfo.setColumnType(data.getColumnTypeName(i) + "(" + data.getColumnDisplaySize(i) + ")");
            }
            syncTableInfos.add(tableInfo);
        }
        return syncTableInfos;
    }

    /**
     * 获取AES key
     *
     * @return
     */
    private String getAESKey () {
        Properties properties = null;
        try {
            properties = FileUtil.getPropertiesByFile(PmDataSourceAction.class.getClassLoader().getResource("/").getPath() + "etl_config.properties");
        } catch(IOException e) {
            e.printStackTrace();
        }
        return (String) properties.get("AESKey");
    }

    /**
     * 在数据库中插入数据，有则update，没有insert
     * 导入数据默认状态：关闭
     *
     * @param infos
     * @return
     */
    public Map<String, String> insertOrUpdate (List<TransTableStructureInfo> infos,String userId) throws Exception{
        Map<String, String> map = new HashMap<>();;
        Map<String, String> srcMap;
        Map<String, String> TargetMap;
        TransTableStructureInfoPK pk = new TransTableStructureInfoPK();

        Map<String,PmDataSource> dataSourceMap = pmDataSourceService.getDataSourceMap();
        // 判断数据源是否存在
        for(TransTableStructureInfo info : infos) {
            if(!dataSourceMap.containsKey(info.getPmDataSourceSrcInfo().getCode())){
                map.put("result","error");
                map.put("msg","系统不存在名称为【"+info.getPmDataSourceSrcInfo().getCode()+"】的数据源");
                return map;
            }
            if(!dataSourceMap.containsKey(info.getPmDataSourceTargetInfo().getCode())){
                map.put("result","error");
                map.put("msg","系统不存在名称为【"+info.getPmDataSourceTargetInfo().getCode()+"】的数据源，请检查数据源后重新操作！");
                return map;
            }
        }
        TransTableStructureInfo transInfo;
        int insertCount = 0;
        int updateCount = 0;
        for(TransTableStructureInfo transTableStructureInfoi : infos) {
            pk.setId(transTableStructureInfoi.getId());
            transInfo = this.selectByPK(pk);
            transTableStructureInfoi.setSrcDbId(dataSourceMap.get(transTableStructureInfoi.getPmDataSourceSrcInfo().getCode()).getId());
            transTableStructureInfoi.setTargetDbId(dataSourceMap.get(transTableStructureInfoi.getPmDataSourceTargetInfo().getCode()).getId());
            transTableStructureInfoi.setIsEnable(TransConstants.IS_ENABLE_TRANS_OFF); //导入默认状态为：关闭
            transTableStructureInfoi.setOwner(userId);
            if (transInfo == null) {
                this.insert(transTableStructureInfoi);
                this.insertQuartz(transTableStructureInfoi);
                insertCount++;
            }else {
                this.updateByPK(transTableStructureInfoi);
                this.updateQuartz(transTableStructureInfoi);
                updateCount++;
            }
        }
        if (insertCount > 0 || updateCount > 0) {
            map.put("result","success");
            map.put("insert", insertCount + "");
            map.put("update", updateCount + "");
        }
        return map;
    }

    /**
     * 判断其数据源是否存在
     * 存在,不执行更新操作
     * 不存在则创建
     *
     * @param pmDataSourceInfo 需要判断的数据源
     * @return 更新后的ID信息
     */
    public Map<String, String> syncDataBase (PmDataSource pmDataSourceInfo) {
        Map<String, String> returnMap = new HashMap<>();
        // 判断table 的 数据源是否存在
        PmDataSourceQuery dsQuery = new PmDataSourceQuery();
        dsQuery.setDatabaseType(pmDataSourceInfo.getDatabaseType());
        dsQuery.setCode(pmDataSourceInfo.getCode());
        dsQuery.setHostName(pmDataSourceInfo.getHostName());
        List<PmDataSource> dataSourceInfos = pmDataSourceService.selectByEntity(dsQuery);
        // 如果数据源不存在，则创建一个数据源
        if (dataSourceInfos == null || dataSourceInfos.size() == 0) {
            pmDataSourceInfo.setId("DS" + new SimpleDateFormat("yyyyMMddHHmmss").format(pmDataSourceInfo.getCreateTime()) + pmDataSourceInfo.getId().substring(pmDataSourceInfo.getId().length() - 4, pmDataSourceInfo.getId().length()));
            pmDataSourceService.insert(pmDataSourceInfo);
        } else {
            //如果数据源存在,不执行更新操作
            pmDataSourceInfo.setId(dataSourceInfos.get(0).getId());
        }

        returnMap.put("dataSourceId", pmDataSourceInfo.getId());

        return returnMap;
    }

    /**
     * 根据勾选内容获取对象集合
     *
     * @param selectedIds
     * @return
     */
    public List<TransTableStructureInfo> selectByIds (String selectedIds) {
        if (StringUtils.isNotEmpty(selectedIds)) {
            List<TransTableStructureInfo> transTableStructureInfos = new ArrayList<TransTableStructureInfo>();
            TransTableStructureInfoPK pk = new TransTableStructureInfoPK();
            TransTableStructureInfo info = new TransTableStructureInfo();
            String[] idArray = selectedIds.split("\\|");
            for(String id : idArray) {
                pk.setId(id);
                info = this.selectByPK(pk);
                PmDataSourcePK pmDataSourcePK = new PmDataSourcePK();
                pmDataSourcePK.setId(info.getSrcDbId()); //查询源数据库信息
                info.setPmDataSourceSrcInfo(pmDataSourceService.selectByPK(pmDataSourcePK));
                pmDataSourcePK.setId(info.getTargetDbId()); //查询目标数据库信息
                info.setPmDataSourceTargetInfo(pmDataSourceService.selectByPK(pmDataSourcePK));

                transTableStructureInfos.add(info);
            }
            return transTableStructureInfos;
        }
        return null;
    }

    /**
     * 密文写入文件
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String writeToFile (String content) throws Exception {
        return FileUtil.writeToFile(content, ServletActionContext.getServletContext().getRealPath("/") + "/filetemp/");
    }

    /**
     * 加密：先转JSON+AES
     *
     * @param selectTransInfo
     * @return
     * @throws Exception
     */
    public String encrypt (List<TransTableStructureInfo> selectTransInfo) throws Exception {
        //转换成json数据
        String json = JSON.toJSONString(selectTransInfo);
        LOGGER.info("JSON->{}", json);
        byte[] base64Json = org.apache.commons.codec.binary.Base64.encodeBase64(json.getBytes("UTF-8"));
        LOGGER.debug("Base64加密后的JSON->{}", base64Json);
        return AESUtil.encrypt(getPassword(), new String(base64Json));
    }

    /**
     * 解密：先解密+json转List
     *
     * @param code
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public List<TransTableStructureInfo> decode (String code) throws Exception {
        List<TransTableStructureInfo> selectTransInfo = null;
        if (StringUtils.isNotEmpty(code)) {
            LOGGER.debug("密文 ->{}", code);
            String aesJm = AESUtil.decrypt(getPassword(), code);
            LOGGER.debug("AES解密  ->{}", aesJm);
            byte[] jiemi = Base64.decodeBase64(aesJm);
            String content = new String(jiemi, "UTF-8");
            LOGGER.info("BEASE64解密 ->{}", content);
            selectTransInfo = JSONArray.toList(JSONArray.fromObject(content), TransTableStructureInfo.class);
        }
        return selectTransInfo;
    }

    /**
     * 取得密钥
     */
    private String getPassword () {
        Properties properties = null;
        String TRANS_TABLE_STRUCTURE_INFO_KEY = "";
        try {
            properties = FileUtil.getPropertiesByFile(TransTableStructureInfoService.class.getClassLoader().getResource("/").getPath() + "etl_config.properties");
            TRANS_TABLE_STRUCTURE_INFO_KEY = (String) properties.get("TRANS_TABLE_STRUCTURE_INFO_KEY");
        } catch(IOException e) {
            e.printStackTrace();
        }
        return TRANS_TABLE_STRUCTURE_INFO_KEY;
    }


    /**
     * 查询已有转换数量
     * @return
     */
    public Integer selectTransCount() {
        return this.modelDao.selectTransCount();
    }

    /**
     * 数据源信息变更时，同步更新表中db相关字段信息
     * @return
     */
    public void updateDbCode(String id, String code) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("code", code);
        this.modelDao.updateDbCode(map);
    }

    /**
     * 得到增量字段最大SQL的获取方法
     *
     * @param dataSource   数据源
     * @param sql          源数据查询SQL
     * @param cloumn       增量字段
     * @param targetTbCode 目标数据表名
     * @return
     * @throws Exception
     */
    public String getMaxSQL (PmDataSource dataSource, String sql, String cloumn, String targetTbCode) throws Exception {
        String returnSQL = "";

        // 获取字段类型
        String columnType = getColumnType(dataSource, sql, cloumn);
        LOGGER.info("增量字段[" + cloumn + "]的数据库类型为[" + columnType + "]");

        if (Arrays.binarySearch(TransConstants.COLUMN_TYPE_INT, columnType) >= 0) {
            // 增量字段类型为Int类型的处理SQL
            returnSQL = "select CASE WHEN MAX(" + cloumn + ") is NULL THEN 0 ELSE MAX(" + cloumn + ") END AS max_value from " + targetTbCode;
        }
        if (Arrays.binarySearch(TransConstants.COLUMN_TYPE_DATE, columnType) >= 0) {
            // 增量字段为Date类型的处理SQL
            returnSQL = "SELECT case when max(" + cloumn + ") is null then '1970-01-01' else date_sub(max(" + cloumn + "), interval 60 second) end as max_value from " + targetTbCode;
        }
        LOGGER.info(" ----- 获取增量字段SQL：" + returnSQL);
        return returnSQL;
    }
}
