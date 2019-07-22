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
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.batch.SyncTableBatchService;
import com.yinker.etl.trans.dao.TransInfoDao;
import com.yinker.etl.trans.model.SelectJson;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.SyncTableInfo;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.model.base.TransInfoPK;
import net.sf.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseService;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 * <b>类描述:</b>TransInfo表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:58
 * </pre>
 */
public class TransInfoService extends BaseService<TransInfoPK, TransInfo, TransInfoDao> implements org.zwork.framework.base.BaseService<TransInfoPK, TransInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransInfoService.class);

    @Resource(name = "transInfoDao")
    private TransInfoDao transInfoDao;

    @Resource(name = "pmDataSourceService")
    private PmDataSourceService pmDataSourceService;

    /* 跑批任务执行类 */
    @Resource(name = "quartzScheduleService")
    private QuartzScheduleService<QuartzSchedule> quartzScheduleService;

    /* 表结构同步服务类 */
    @Resource(name = "syncTableBatchService")
    private SyncTableBatchService syncTableBatchService;

    @Resource(name = "syncTableInfoService")
    private SyncTableInfoService syncTableInfoService;

    @Resource(name = "etlKeyGeneratorContainerTrans")
    private KeyGeneratorContainer keyGeneratorContainer;

    /**
     * 获取配置表的id和transName字段
     *
     * @return
     */
    public List<Map<String, Object>> selectIdAndTransNameMap () {
        return transInfoDao.selectIdAndTransNameMap();
    }

    /**
     * 通过数据源编号获取链接
     *
     * @param dataBaseId 数据源编号
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnectByDatasourceId (String dataBaseId) throws Exception {
        Connection con = null;
        PmDataSourcePK dsPK = new PmDataSourcePK();
        dsPK.setId(dataBaseId);
        PmDataSource dataSource = pmDataSourceService.selectByPK(dsPK);
        Properties properties = FileUtil.getPropertiesByFile(PmDataSourceAction.class.getClassLoader().getResource("/").getPath() + "etl_config.properties");
        String AESKey = (String) properties.get("AESKey");
        switch(dataSource.getDatabaseType()) {
            case PMConstants.DATA_BASE_TYPE_ORACLE: // 获取ORACLE数据源
                con = DataSourceUtil.getOracleConnection(dataSource.getHostName(), dataSource.getPort() + "", dataSource.getUserName(),
                        AESUtil.decrypt(AESKey, dataSource.getPassword()).trim(), dataSource.getDatabaseName());
                break;
            case PMConstants.DATA_BASE_TYPE_MYSQL:  // 获取Mysql数据源
                con = DataSourceUtil.getMysqlConnection(dataSource.getHostName(), dataSource.getPort() + "", dataSource.getUserName(),
                        AESUtil.decrypt(AESKey, dataSource.getPassword()).trim(), dataSource.getDatabaseName());
                break;
        }
        return con;
    }

    /**
     * 通过数据源获取所有表
     *
     * @param con
     * @return
     */
    public List<String> getTablesCode (Connection con) {
        LOGGER.info("查询该数据源下的所有表信息");
        List<String> tables = new ArrayList<String>();
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(null, null, null, new String[]{"TABLE"});
            LOGGER.info("查询结束，开始压入集合");
            while(rs.next()) {
                /*LOGGER.debug("表名：" + rs.getString(3));
                LOGGER.debug("表所属用户名：" + rs.getString(2));
                LOGGER.debug("------------------------------");*/
                tables.add(rs.getString(3));
            }
            LOGGER.info("共压入【{}】条数据", tables.size());
            LOGGER.info("关闭数据源资源");
            con.close();
            LOGGER.info("数据源已关闭");
        } catch(Exception e) {
            LOGGER.error("程序运行异常");
            e.printStackTrace();
            try {
                LOGGER.info("准备在异常中关闭数据源");
                con.close();
                LOGGER.info("数据源已关闭");
            } catch(SQLException e1) {
                LOGGER.error("关闭数据源异常");
                e1.printStackTrace();
            }
        }
        return tables;
    }

    /**
     * 查找所有表中包含的关键词
     *
     * @param tables 所有的表名称
     * @param key    需要查找的关键词
     * @return
     */
    public List<SelectJson> getHashTables (List<String> tables, String key) {
        LOGGER.info("开始从{}张表筛选关键词为【{}】", tables.size(), key);
        List<SelectJson> jsons = new ArrayList<>();
        SelectJson sj;
        for(String table : tables) {
            if (table.indexOf(key) >= 0) {
                sj = new SelectJson();
                sj.setId(table);
                sj.setName(table);
                jsons.add(sj);
            }
        }
        LOGGER.info("共筛选出{}张表", jsons.size());
        return jsons;
    }

    /**
     * 新增抽取转换配置
     *
     * @param entity
     */
    public void insertTransInfo (TransInfo entity) {
        entity = getTransInfoByCheck(entity);
        insert(entity);
    }

    /**
     * 修改抽取转换配置
     *
     * @param entity
     */
    public void updateTransInfo (TransInfo entity) {
        entity = getTransInfoByCheck(entity);
        updateByPK(entity);
    }

    /**
     * 处理TransTableStructureInfo 使其可以进行数据库操作处理
     *
     * @param entity
     * @return
     */
    private TransInfo getTransInfoByCheck (TransInfo entity) {

        if (entity.getSrcTbCode().indexOf(",") != -1) {
            entity.setSrcTbCode(entity.getSrcTbCode().substring(1, entity.getSrcTbCode().length()).trim());
        }
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
     * 根据勾选内容获取对象集合
     *
     * @param selectedIds
     * @return
     */
    public List<TransInfo> selectByIds (String selectedIds) {
        if (StringUtils.isNotEmpty(selectedIds)) {
            List<TransInfo> transInfos = new ArrayList<TransInfo>();
            TransInfoPK pk = new TransInfoPK();
            TransInfo info = new TransInfo();
            String[] idArray = selectedIds.split("\\|");
            for(String id : idArray) {
                pk.setId(id);
                info = this.selectByPK(pk);
                PmDataSourcePK pmDataSourcePK = new PmDataSourcePK();
                pmDataSourcePK.setId(info.getSrcDbId()); //查询源数据库信息
                info.setPmDataSourceSrcInfo(pmDataSourceService.selectByPK(pmDataSourcePK));
                pmDataSourcePK.setId(info.getTargetDbId()); //查询目标数据库信息
                info.setPmDataSourceTargetInfo(pmDataSourceService.selectByPK(pmDataSourcePK));

                transInfos.add(info);
            }
            return transInfos;
        }
        return null;
    }

    /**
     * 加密：先转JSON+AES
     *
     * @param selectTransInfo
     * @return
     * @throws Exception
     */
    public String encrypt (List<TransInfo> selectTransInfo) throws Exception {
        //转换成json数据
        String json = JSON.toJSONString(selectTransInfo);
        LOGGER.info("JSON->{}", json);
        byte[] base64Json = Base64.encodeBase64(json.getBytes("UTF-8"));
        LOGGER.info("Base64加密后的JSON->{}", base64Json);
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
    public List<TransInfo> decode (String code) throws Exception {
        List<TransInfo> selectTransInfo = null;
        if (StringUtils.isNotEmpty(code)) {
            LOGGER.info("密文 ->{}", code);
            String aesJm = AESUtil.decrypt(getPassword(), code);
            LOGGER.info("AES解密  ->{}", aesJm);
            byte[] jiemi = Base64.decodeBase64(aesJm);
            String content = new String(jiemi, "UTF-8");
            LOGGER.info("BEASE64解密 ->{}", content);
            selectTransInfo = JSONArray.toList(JSONArray.fromObject(content), TransInfo.class);
        }
        return selectTransInfo;
    }

    /**
     * 在数据库中插入数据，有则update，没有insert
     * 默认导入转换状态：关闭
     *
     * @param infos
     * @return
     */
    public Map<String, String> insertOrUpdate (List<TransInfo> infos, String userId) throws Exception {
        Map<String, String> map = new HashMap<>();
        ;
        Map<String, String> srcMap;
        Map<String, String> TargetMap;
        TransInfoPK pk = new TransInfoPK();
        TransInfo transInfo;

        Map<String, PmDataSource> dataSourceMap = pmDataSourceService.getDataSourceMap();
        // 判断数据源是否存在
        for(TransInfo info : infos) {
            if (!dataSourceMap.containsKey(info.getPmDataSourceSrcInfo().getCode())) {
                map.put("result", "error");
                map.put("msg", "系统不存在名称为【" + info.getPmDataSourceSrcInfo().getCode() + "】的数据源");
                return map;
            }
            if (!dataSourceMap.containsKey(info.getPmDataSourceTargetInfo().getCode())) {
                map.put("result", "error");
                map.put("msg", "系统不存在名称为【" + info.getPmDataSourceTargetInfo().getCode() + "】的数据源，请检查数据源后重新操作！");
                return map;
            }
        }

        int insertCount = 0;
        int updateCount = 0;
        for(TransInfo transInfoi : infos) {
            pk.setId(transInfoi.getId());
            transInfo = this.selectByPK(pk);
            transInfoi.setSrcDbId(dataSourceMap.get(transInfoi.getPmDataSourceSrcInfo().getCode()).getId());
            transInfoi.setTargetDbId(dataSourceMap.get(transInfoi.getPmDataSourceTargetInfo().getCode()).getId());
            transInfoi.setIsEnable(TransConstants.IS_ENABLE_TRANS_OFF); //导入默认状态为：关闭
            transInfoi.setOwner(userId);
            if (transInfo == null) {
                this.insert(transInfoi);
                this.insertQuartz(transInfoi);
                insertCount++;
            } else {
                this.updateByPK(transInfoi);
                this.updateQuartz(transInfoi);
                updateCount++;
            }
        }
        if (insertCount > 0 || updateCount > 0) {
            map.put("result", "success");
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
//     * @param pmDataSourceInfo 需要判断的数据源
//     * @return 更新后的ID信息
     */
    public Map<String, String> syncDataBase (PmDataSource pmDataSourceInfo) {
        Map<String, String> returnMap = new HashMap<>();
        // 判断table 的 数据源是否存在
        PmDataSourceQuery dsQuery = new PmDataSourceQuery();
        dsQuery.setCode(pmDataSourceInfo.getCode());
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
     * 取得密钥
     */
    private String getPassword () {
        Properties properties = null;
        String TRANS_INFO_KEY = "";
        try {
            properties = FileUtil.getPropertiesByFile(TransInfoService.class.getClassLoader().getResource("/").getPath() + "etl_config.properties");
            TRANS_INFO_KEY = (String) properties.get("TRANS_INFO_KEY");
        } catch(IOException e) {
            e.printStackTrace();
        }
        return TRANS_INFO_KEY;
    }

    /**
     * 创建ETL转换自定创建跑批任务
     *
     * @param entity
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    public void insertQuartz (TransInfo entity) throws ClassNotFoundException, SchedulerException {
        // 转换类型为 增量抽取
        if ("Y".equals(entity.getIsDiyQuartz())) {
            // 创建自定义跑批任务
            LOGGER.debug("开始创建自定义跑批任务");
            QuartzSchedule quartz = new QuartzSchedule();
            quartz.setGroup(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS);
            quartz.setCron(entity.getCron());
            String transName = "";
            if ("1".equals(entity.getTransType())) {
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_1);
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_1 + entity.getId();
            } else if ("2".equals(entity.getTransType())) {
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_2);
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_2 + entity.getId();
            } else if ("3".equals(entity.getTransType())) {
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_3);
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_3 + entity.getId();
            } else if ("4".equals(entity.getTransType())) {
                quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_4);
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_4 + entity.getId();
            }
            quartz.setName(transName);
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
    public void updateQuartz (TransInfo entity) throws Exception {
        // 转换类型为 增量抽取
        if ("Y".equals(entity.getIsDiyQuartz())) {
            String transName = "";
            if ("1".equals(entity.getTransType())) {
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_1 + entity.getId();
            } else if ("2".equals(entity.getTransType())) {
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_2 + entity.getId();
            } else if ("3".equals(entity.getTransType())) {
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_3 + entity.getId();
            } else if ("4".equals(entity.getTransType())) {
                transName = QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_4 + entity.getId();
            }
            LOGGER.debug("删除定时任务", QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + transName);
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + transName);
            if ("Y".equals(entity.getIsDiyQuartz())) {
                QuartzSchedule quartz = new QuartzSchedule();
                quartz.setName(transName);
                quartz.setGroup(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS);
                quartz.setCron(entity.getCron());
                quartz.setDescription("自定义调度任务：" + entity.getTransName());
                if ("1".equals(entity.getTransType())) {
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_1);
                } else if ("2".equals(entity.getTransType())) {
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_2);
                } else if ("3".equals(entity.getTransType())) {
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_3);
                } else if ("4".equals(entity.getTransType())) {
                    quartz.setExecutor(QuartzConstants.TRANS_DIY_ETL_CLASS_4);
                }
                LOGGER.debug("新增任务");
                quartzScheduleService.insert(quartz);
            }
        }
    }

    /**
     * 删除任务
     */
    public void deleteQuartz (TransInfo entity) throws SchedulerException {
        if ("1".equals(entity.getTransType())) {
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_1 + entity.getId());
        } else if ("2".equals(entity.getTransType())) {
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_2 + entity.getId());
        } else if ("3".equals(entity.getTransType())) {
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_3 + entity.getId());
        } else if ("4".equals(entity.getTransType())) {
            quartzScheduleService.delete(QuartzConstants.TRIGGER_GROUP_DIY_ETLTRANS + "." + QuartzConstants.TRIGGER_NAME_DIY_ETLTRANS_4 + entity.getId());
        }
    }

    /**
     * 目标数据表自动创建同步
     *
     * @param queryEntity
     * @return Map
     * @throws Exception
     */
    public Map<String, String> createTargetTb (TransInfoQuery queryEntity) throws Exception {
        //作为返回记录
        Map<String, String> dataMap = new HashMap<String, String>(2);

        try {
            PmDataSourceQuery pm = new PmDataSourceQuery();
            pm.setId(queryEntity.getSrcDbId());
            PmDataSource dataSource = pmDataSourceService.selectByEntity(pm).get(0);
            queryEntity.setPmDataSourceSrcInfo(dataSource); //查询源数据源Code
            queryEntity.setSrcDbCode(dataSource.getCode());
            pm.setId(queryEntity.getTargetDbId());
            dataSource = pmDataSourceService.selectByEntity(pm).get(0);
            queryEntity.setPmDataSourceTargetInfo(dataSource);//查询目标数据源Code
            queryEntity.setTargetDbCode(dataSource.getCode());

            SyncTableInfo syncTableInfo = new SyncTableInfo();
            String dpTable = queryEntity.getSrcTbCode();
            if (!queryEntity.getSrcTbCode().equals(queryEntity.getTargetTbCode())) {
                //判断表名前缀是否为dp_
                String tablePre = queryEntity.getSrcTbCode().substring(0, 3);
                if (!"dp_".equals(tablePre)) {
                    dpTable = "dp_" + queryEntity.getSrcTbCode();
                }
                //若转换类型为时间拉链抽取，则添加_his后缀
                if ("4".equals(queryEntity.getTransType())) {
                    dpTable += "_his";
                }
            }

            syncTableInfo.setTableName(dpTable);
            queryEntity.setTargetTbCode(dpTable);
            syncTableInfo.setTableSchema(queryEntity.getPmDataSourceTargetInfo().getDatabaseName());

            //切换至目标数据源
            DataSourceContextHolder.setDataSourceKey(queryEntity.getPmDataSourceTargetInfo().getCode());
            //查询数据源下的表，判断dp表是否存在
            List<String> tablesList = syncTableInfoService.selectTablesList(syncTableInfo);
            if (tablesList != null && tablesList.size() > 0) {
                LOGGER.info("{}表已存在，无需创建", dpTable);
                dataMap.put("exist", "表已经存在无需创建。");
            } else {
                LOGGER.info("在目标数据源{}下创建数据表{}", queryEntity.getTargetDbCode(), dpTable);
                if ("4".equals(queryEntity.getTransType())) {
                    syncTableInfo.setIdName(dpTable + "_id");
                    syncTableInfoService.createDpTable(syncTableInfo);
                } else {
                    syncTableInfoService.createDpTable(syncTableInfo);
                }
            }
            //恢复默认数据源
            DataSourceContextHolder.clearDataSourceKey();
            //进入表结构同步
            LOGGER.info("进行目标数据表{}与源数据表{}结构同步", dpTable, queryEntity.getTargetTbCode());
            syncTableBatchService.syncTableInfo(queryEntity, queryEntity.getPmDataSourceSrcInfo().getDatabaseName(), queryEntity.getPmDataSourceTargetInfo().getDatabaseName(), "transInfo", "", "");
            //操作成功记录
            dataMap.put("flag", "0");
            dataMap.put("exist", "表已经存在无需创建,同步表结构完成。");
        } catch(Exception e) {
            e.printStackTrace();
            String errorMessage = "创建失败！" + e.getMessage();
            errorMessage = errorMessage.replaceAll("\r|\n|\'|\"", "");
            LOGGER.info("{}", errorMessage);
            //将错误信息返回前台
            dataMap.put("flag", "1");
            dataMap.put("infos", errorMessage);
        }
        return dataMap;
    }

    public Pagination getTransErrorLogList (TransInfoQuery entityQuery) {
        return modelDao.getTransErrorList(entityQuery);
    }

    public void toCreateData (int count) throws IOException {

        long beginTime = System.currentTimeMillis();
        DataSourceContextHolder.setDataSourceKey("etl");
/*
        int[] test = new int[]{1, 2};
*/
        int[] test = new int[]{10, 50, 100};
        for(int j = 0; j < test.length; j++) {
            count = test[j];
            count = count * 100;
            for(int k = 1; k <= 100; k++) {
                for(int i = 1; i <= count; i++) {
                    StringBuffer sb = new StringBuffer();
                    FileInputStream fis = new FileInputStream("F:\\etlTest\\hr_user_info.sql");
                    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    sb.append("INSERT INTO dp_hr_user_info_" + test[j] + "w_" + k);
                    String line = null;
                    while((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    try {
                        modelDao.toCreateData(sb.toString());
                    } catch(Exception ex) {
                        try {
                            Thread.sleep(1000 * 10);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        ex.printStackTrace();
                    }
                }
            }
        }

        LOGGER.info("耗时" + (System.currentTimeMillis() - beginTime) + "毫秒");
        DataSourceContextHolder.clearDataSourceKey();
    }

    public void createTransInfo () {
        long beginTime = System.currentTimeMillis();
        DataSourceContextHolder.setDataSourceKey("etl");

        // int[] sourceCount = new int[]{500,1000,5000};
        int[] targetCount = new int[]{1, 10, 50, 100};
        int count = 100;

        for(int i = 0; i < targetCount.length; i++) {
            //   for(int j = 0; j < sourceCount.length; j++) {
            for(int k = 1; k <= count; k++) {
                String id = keyGeneratorContainer.getNextKey("transInfoId");
                TransInfo info = new TransInfo();
                info.setId(id);
                info.setIsDiyQuartz("N");
                info.setIsEnable("N");
                info.setTransName("测试转换：" + "dp_hr_user_info_" + targetCount[i] + "w_" + k);
                info.setSrcDbId("00000000000000000048");
                info.setSrcDbCode("drap_dev");
                info.setSrcTbCode("hr_user_info_500");
                info.setSrcDescript("源表描述：drap_dev——hr_user_info_500");
                info.setTargetDbId("00000000000000000019");
                info.setTargetDbCode("etl");
                info.setTargetTbCode("dp_hr_user_info_" + targetCount[i] + "w_" + k);
                info.setTargetDescript("目标描述：etl——dp_hr_user_info_" + targetCount[i] + "w_" + k);
                info.setTransType("1");
                info.setOwner("admin");
                info.setLastUpdateDate(new Date());
                info.setCreateTime(new Date());

                this.insert(info);
            }
            //}
        }
        LOGGER.info("耗时" + (System.currentTimeMillis() - beginTime) + "毫秒");
        DataSourceContextHolder.clearDataSourceKey();
    }

    public List<String> selectIdByTransName (TransInfoQuery transInfoQuery) {
        return transInfoDao.selectIdByTransName(transInfoQuery);
    }

    /**
     * 根据List<PmDataSource> 查询出db的id数组
     *
     * @param dataSourceInfos
     * @return
     */
    public String[] getDSArray (List<PmDataSource> dataSourceInfos) {
        String[] sbArray = new String[dataSourceInfos.size()];
        for(int i = 0; i < dataSourceInfos.size(); i++) {
            sbArray[i] = dataSourceInfos.get(i).getId();
        }
        return sbArray;
    }

    /**
     * 查询已有转换数量
     *
     * @return
     */
    public Integer selectTransCount () {
        return transInfoDao.selectTransCount();
    }

    /**
     * 数据源信息变更时，同步更新表中db相关字段信息
     *
     * @return
     */
    public void updateDbCode (String id, String code) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("code", code);
        transInfoDao.updateDbCode(map);
    }

    /**
     * 此方法用于查询是否包含增量数据，目的是否需要执行更新操作
     *
     * @param srcDb    源数据源
     * @param targetDb 目标数据源
     * @param maxSQL   最大更新字段SQL
     * @param excutSQL 源数据源查询SQL
     * @return
     */
    public Boolean isHasUpdateData (String srcDb, String targetDb, String maxSQL, String excutSQL, String maxType) {
        Boolean flag = false;
        try {
            LOGGER.info("开始查询目标数据源最大更新字段的值");
            LOGGER.info("切换目标数据源" + targetDb);
            DataSourceContextHolder.setDataSourceKey(targetDb);
            List<HashMap<String, Object>> resultList = modelDao.superManagerSelect(maxSQL);
            String maxValue = "";
            if (resultList != null && resultList.size() > 0) {
                Map<String, Object> resultMap = resultList.get(0);
                if (resultMap.containsKey("max_value")) {
                    if ("DATETIME".equals(maxType)) {
                        maxValue = (String) resultMap.get("max_value");
                    }
                    if ("INT".equals(maxType)) {
                        maxValue = resultMap.get("max_value") + "";
                    }
                    LOGGER.info("最大更新字段的值为:" + maxValue);
                    excutSQL = excutSQL + "\'" + maxValue + "\'";
                    LOGGER.info("开始查询源数据源的增量数据SQL：" + excutSQL);
                    DataSourceContextHolder.setDataSourceKey(srcDb);
                    LOGGER.info("切换到源数据源:" + srcDb);
                    resultList = modelDao.superManagerSelect(excutSQL);
                    if (resultList != null && resultList.size() > 0) {
                        resultMap = resultList.get(0);
                        if (resultMap.containsKey("value_count")) {
                            Long resultCount = (Long) resultMap.get("value_count");
                            LOGGER.info("共查询出增量数据[" + resultCount + "]条");
                            if (resultCount > 0) {
                                flag = true;
                            }
                        }
                    }
                }
            } else {
                LOGGER.info("没有查询到数据!");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            flag = true;
        }
        DataSourceContextHolder.clearDataSourceKey();
        return flag;
    }

    public List<Map<String, Object>> getTransDayReport (String email) {
        return transInfoDao.getTransDayReport(email);
    }

    public List<Map<String, Object>> getErrorRecordDayReport (String email) {
        return transInfoDao.getErrorRecordDayReport(email);
    }

    public List<Map<String, Object>> getErrorTSDayReport (String email) {
        return transInfoDao.getErrorTSDayReport(email);
    }

    public List<Map<String, Object>> getDayReportUsers () {
        return transInfoDao.getDayReportUsers();
    }

    public List<Map<String, Object>> getExceptionReportUsers () {
        return transInfoDao.getExceptionReportUsers();
    }
}
