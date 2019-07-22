package com.yinker.etl.trans.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.pm.model.PmDataSourceQuery;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.pm.service.PmGroupService;
import com.yinker.etl.pm.util.FileUtil;
import com.yinker.etl.qrtz.model.base.QuartzSchedule;
import com.yinker.etl.qrtz.service.QuartzScheduleService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.SelectJson;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.model.base.TransInfoPK;
import com.yinker.etl.trans.service.TransConfigChangeLogService;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
import org.apache.commons.codec.binary.Base64;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.model.base.PmParam;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import org.zwork.srdp.pm.service.PmParamService;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.session.User;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 * <b>类描述:</b>TransInfo表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:57
 * </pre>
 */
public class TransInfoAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransInfoAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected TransInfo entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected TransInfoQuery queryEntity;

    /* 定义服务层操作类 */
    protected TransInfoService entityService;

    /* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    /* 数据字典-转换类型 */
    private List<PmDataDictItem> transTypes;
    /* 数据字典-启用状态 */
    private List<PmDataDictItem> transIsEnables;
    /* 数据字典-转换执行状态 */
    private List<PmDataDictItem> transInfoType;

    /* 数据字典-数据源列表 */
    private List<PmDataSource> dataSourceInfos;

    /* 数据字典-数据源列表 */
    private List<PmDataSource> srcDataSourceInfos;

    /* 读写数据源 - 目标数据源 */
    private List<PmDataSource> targetDataSourceInfos;

    /* 数据源管理服务类 */
    @Resource(name = "pmDataSourceService")
    private PmDataSourceService pmDataSourceService;

    /* 数据导入导出日志服务类 */
    @Resource(name = "transConfigChangeLogService")
    private TransConfigChangeLogService transConfigChangeLogService;

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    @Resource(name = "pmParamService")
    private PmParamService pmParamService;

    @Resource(name = "pmGroupService")
    private PmGroupService pmGroupService;

    @Resource(name = "quartzScheduleService")
    private QuartzScheduleService<QuartzSchedule> quartzScheduleService;

    // 检索内容
    private String key;

    // 数据源编号
    private String dataBaseId;

    //数据导出文件名称
    private String fileName;

    //数据导出流
    private InputStream outputDataStream;

    //数据导入文件
    private File file;

    private int count;

    //跑批批次号
    private String batchNo;

    //过滤条件 BASE64加密后(添加、修改时使用)
    private String encodeSQL;

    /**
     * 执行初始化方法
     */
    public void init () {
        entity = new TransInfo();
        queryEntity = new TransInfoQuery();

        transTypes = pmDataDictItemService.selectByDictCode("trans.transType");
        transIsEnables = pmDataDictItemService.selectByDictCode("trans.info_is_enable");
        transInfoType = pmDataDictItemService.selectByDictCode("trans.transInfoType");
        dataSourceInfos = pmDataSourceService.selectAll();

/*
        PmDataSourceQuery targetDSQuery = new PmDataSourceQuery();
        targetDSQuery.setDatabaseCategory("2");
        targetDataSourceInfos = pmDataSourceService.selectByEntity(targetDSQuery);*/
    }

    public TransInfoAction () {
    }

    /**
     * 新增及新增保存页面
     *
     * @return
     * @throws Exception
     */
    public String create () throws Exception {
        setDataSourceValue();
        if ("toCreate".equals(operate)) {// 转向新增页面

            //判断已有转换数是否超过允许的最大值
            PmParam param = pmParamService.selectByCode(TransConstants.ETL_TRANS_MAX_COUNT);
            if(param != null){
                String transMaxCount = param.getValue();
                Integer existsTransCount = entityService.selectTransCount();
                if(existsTransCount != null && existsTransCount >= Integer.valueOf(transMaxCount)){
                    setWarnMessage("当前转换数已达阈值：" + transMaxCount + "， 无法创建转换，请联系管理员！");
                    return PROMPT;
                }
            }

            entity.setType("7");
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存

            // 校验转换名称是否重复
            TransInfoQuery transInfoQuery = new TransInfoQuery();
            transInfoQuery.setTransName(entity.getTransName());
            List<TransInfo> infos = entityService.selectByEntity(transInfoQuery);
            if (infos == null || infos.size() > 0) {
                setWarnMessage("此转换名称已存在，请重新输入！");
                return PROMPT;
            }

            // 若是自定义时间，或者是清空全量抽取，都默认为自定义抽取时间
            if (TransConstants.TRANS_INFO_TYPE_THREAD_99.equals(entity.getType()) || TransConstants.TRANS_TYPE_3.equals(entity.getTransType())) {
                entity.setIsDiyQuartz("Y");
                entity.setType(TransConstants.TRANS_INFO_TYPE_THREAD_99);
            } else {
                entity.setIsDiyQuartz("N");
            }

            // 校验参数
            if ("Y".equals(entity.getIsDiyQuartz())) {
                if (StringUtils.isEmpty(entity.getCron())) {
                    setWarnMessage("请填写Cron表达式！");
                    return PROMPT;
                }
            }
            String whereStr = new String(Base64.decodeBase64(encodeSQL),"UTF-8").toLowerCase().trim();//获得解密后的sqlentity.setWhereStr(whereStr);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOwner(user.getId());
            entity.setCreateTime(new Date());
            entity.setLastUpdateDate(new Date());
            entity.setId(keyGeneratorContainer.getNextKey("transInfoId"));
            if(entity.getWhereStr() != null && !"".equals(entity.getWhereStr())){
                entity.setWhereStr(entity.getWhereStr().trim());
            }

            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
            entityService.insertTransInfo(entity);

            entityService.insertQuartz(entity);


            setInfoMessage("操作成功!");
            LOGGER.debug("新增数据成功，转向修改页面");
            return EDIT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }

    /**
     * 修改及修改保存页面
     *
     * @return
     * @throws Exception
     */
    public String edit () throws Exception {
        setDataSourceValue();
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存

            // 若是自定义时间，或者是清空全量抽取，都默认为自定义抽取时间
            if (TransConstants.TRANS_INFO_TYPE_THREAD_99.equals(entity.getType())||TransConstants.TRANS_TYPE_3.equals(entity.getTransType())) {
                entity.setIsDiyQuartz("Y");
                entity.setType(TransConstants.TRANS_INFO_TYPE_THREAD_99);

            } else {
                entity.setIsDiyQuartz("N");
            }

            // 校验参数
            if ("Y".equals(entity.getIsDiyQuartz())) {
                if (StringUtils.isEmpty(entity.getCron())) {
                    setWarnMessage("请填写Cron表达式！");
                    return PROMPT;
                }
            }

            // 校验转换名称是否重复
            TransInfoQuery transInfoQuery = new TransInfoQuery();
            transInfoQuery.setTransName(entity.getTransName());
            List<TransInfo> infos = entityService.selectByEntity(transInfoQuery);
            if (infos == null || infos.size() > 0) {
                for(TransInfo info : infos) {
                    if (!info.getId().equals(entity.getId())) {
                        setWarnMessage("此转换名称已存在，请重新输入！");
                        return PROMPT;
                    }
                }
            }

            entity.setLastUpdateDate(new Date());
            String whereStr = new String(Base64.decodeBase64(encodeSQL),"UTF-8").toLowerCase().trim();//获得解密后的sql
            entity.setWhereStr(whereStr);
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);
            entityService.updateTransInfo(entity);
            LOGGER.debug("更新Quartz定时任务");
            if(!"Y".equals(entity.getIsDiyQuartz())){
        entityService.deleteQuartz(entity);
    }else {
        entityService.updateQuartz(entity);
    }
    setInfoMessage("操作成功!");
            LOGGER.debug("更新数据成功!");
            return PROMPT;
} else {
        throw new Exception("无效operate参数值[" + operate + "]");
        }
        }

    /**
     * 根据主键删除数据记录
     *
     * @return
     * @throws Exception
     */
    public String delete () throws Exception {
        String selectedIds = request.getParameter("selectedIds");
        if (StringUtils.isNotEmpty(selectedIds)) {
            String[] idArray = selectedIds.split("\\|");
            StringBuffer sb = new StringBuffer();
            int noDeleteCount = 0;
            for(int i = 0; i < idArray.length; i++) {
                TransInfoPK pk = new TransInfoPK();
                pk.setId(idArray[i]);
                TransInfo info = entityService.selectByPK(pk);
                if ("Y".equals(info.getIsEnable())) {
                    sb.append("[" + info.getTransName() + "]");
                    noDeleteCount++;
                    continue;
                }
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
                LOGGER.debug("删除定时任务");
                entityService.deleteQuartz(info);
            }
            LOGGER.info("本次共选择【{}】条，成功删除【{}】条", idArray.length, idArray.length - noDeleteCount);
            if (noDeleteCount > 0) {
                LOGGER.info("转换名为：{}的状态为开启，故未成功删除", sb.toString());
            }
            setInfoMessage("本次共选择" + idArray.length + "条，成功删除" + (idArray.length - noDeleteCount) + "条!" + (noDeleteCount > 0 ? "<br>转换名为：" + sb.toString() + "的状态为开启，固未成功删除" : ""));
        }
        return PROMPT;
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     *
     * @return
     * @throws Exception
     */
    public String view () throws Exception {
        setDataSourceValue();

        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
        TransInfo transInfo = entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            entity = transInfo;
        }
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    public String toList () {
        setDataSourceValue();
        return LIST;
    }

    /**
     * 查询列表页面
     *
     * @return
     * @throws Exception
     */
    public String list () throws Exception {
        setDataSourceValue();
        LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
        queryEntity.setSortColumns(" last_update_date desc");
        String[] srcArray = entityService.getDSArray(srcDataSourceInfos);
        String[] targetArray = entityService.getDSArray(targetDataSourceInfos);
        if (srcArray == null || srcArray.length == 0 || targetArray == null || targetArray.length == 0) {
            queryEntity.setId("9999999999");
        }
        queryEntity.setSrcDBArray(srcArray);
        queryEntity.setTargetDBArray(targetArray);
        page = entityService.pageQuery(queryEntity);

        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        return LIST_RESULT;
    }


    /**
     * 通过数据源检索所有表
     */
    public void getDatabeseTablesJson () throws Exception {
        List<SelectJson> keysTables = new ArrayList<>();
        if (StringUtils.isNotEmpty(dataBaseId)) {
            // 获取数据源
            Connection con = entityService.getConnectByDatasourceId(dataBaseId);
            // 获取所有表
            List<String> tables = entityService.getTablesCode(con);
            // 获取所有表中包含关键词的信息
            keysTables = entityService.getHashTables(tables, key);
        }
        String json = JSON.toJSONString(keysTables);
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 对转换进行批量关闭/打开操作。
     */
    public String turn () {
        String selectedIds = request.getParameter("selectedIds");
        String type = request.getParameter("type");
        if (StringUtils.isNotEmpty(selectedIds)) {
            String[] idArray = selectedIds.split("\\|");
            for(int i = 0; i < idArray.length; i++) {
                TransInfoPK pk = new TransInfoPK();
                pk.setId(idArray[i]);
                entity = entityService.selectByPK(pk);

                entity.setLastUpdateDate(new Date());
                entity.setIsEnable(type);
                entityService.updateByPK(entity);
            }
        }
        return PROMPT;
    }


    /**
     * 数据导出
     *
     * @return String
     * @throws Exception
     */
    public String outputData () throws Exception {
        String selectedIds = request.getParameter("selectedIds");
        LOGGER.info("根据传入的ID来查出导出对象！");
        List<TransInfo> selectTransInfo = entityService.selectByIds(selectedIds);
        LOGGER.info("将数据进行加密");
        String messageCode = entityService.encrypt(selectTransInfo);
//        String messageCode = JSON.toJSONString(selectTransInfo);
        LOGGER.info("数据存入文件");
        String filePath = entityService.writeToFile(messageCode);
        InputStream inputStream = new FileInputStream(new File(filePath));
        this.outputDataStream = inputStream;
        if (StringUtils.isNotEmpty(selectedIds)) {
            String tmpName = "ETL抽取配置";
            for(int i = 0; i < selectTransInfo.size(); i++) {
                if (i < 5) {
                    tmpName += "[" + selectTransInfo.get(i).getSrcTbCode() + "]";
                } else {
                    break;
                }
            }
            tmpName += "[共" + selectTransInfo.size() + "条]" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".cbw";
            this.fileName = new String(tmpName.getBytes("gb2312"), "ISO8859-1");

            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            String log = "用户【" + user.getName() + "】对表【trans_info】执行数据导出操作，本次共导出【" + selectTransInfo.size() + "】条数据";
            transConfigChangeLogService.wirteChangeLog(user, "trans_info", "1", log, keyGeneratorContainer.getNextKey("transConfigChangeLogId")); //第三个参数  '1'表示 导出
            LOGGER.info("{}", log);

        }
        return "outputData";
    }

    public String toImportData () {
        return "toImportData";
    }

    /**
     * 数据导入
     *
     * @return String
     * @throws Exception
     */
    public String importData () throws Exception {
        try {
            LOGGER.info("获取文件内容:{}", file);
            String fileContent = FileUtil.readFileByLines(file);
            List<TransInfo> infos = entityService.decode(fileContent);
            String msg = "";
            String returnCode = "success";
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            if (infos != null && infos.size() > 0 && infos.get(0) != null) {
                Map<String, String> resultMap = entityService.insertOrUpdate(infos,user.getId());
                if ("success".equals(resultMap.get("result"))) {
                    msg = "导入成功！";

                    String log = "用户【" + user.getName() + "】对表【trans_info】执行数据导入操作，本次执行共增加数据【" + resultMap.get("insert") + "】条，更新数据【" + resultMap.get("update") + "】条";
                    transConfigChangeLogService.wirteChangeLog(user, "trans_info", "2", log, keyGeneratorContainer.getNextKey("transConfigChangeLogId")); //第三个参数 '2'表示 导入
                    LOGGER.info("{}", log);
                } else {
                    msg = resultMap.get("msg");
                    returnCode = "error";
                }
            } else {
                msg = "数据异常！";
            }
            LOGGER.info("{}", msg);
            String retStr = "{\"returnCode\":\"" + returnCode + "\",\"remark\":\"" + msg + "\"}";
            response.setHeader("content-type", "text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(retStr);
            out.flush();
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
            String retStr = "{\"returnCode\":\"" + "error" + "\",\"remark\":\"" + "   上传失败原因：" + e.toString() + "\"}";
            response.setHeader("content-type", "text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(retStr);
            out.flush();
            out.close();
            return null;
        }
        return null;
    }


    /**
     * 目标数据表自动创建同步
     *
     * @throws Exception
     */
    public void createTargetTb () throws Exception {
        Map<String, String> dataMap = entityService.createTargetTb(queryEntity);
        toJosnLink(dataMap);
    }

    /**
     * 将Map转化为JSON格式
     *
     * @throws Exception
     */
    public void toJosnLink (Map object) {
        JSONObject t = new JSONObject(true);
        t.put("link", object);
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(t.toString());
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入查看日志页面
     *
     * @return String
     */
    public String listLogs () {
        TransInfoPK pk = new TransInfoPK();
        pk.setId(entity.getId());
        entity = entityService.selectByPK(pk);
        return "normalQuery";
    }

    public String toTransError () {
        return "transError";
    }

    public String transError () {
        LOGGER.info("查询报错的表抽取的转换信息");
        if ("query".equals(operate)) {
            page = entityService.getTransErrorLogList(queryEntity);
        }
        return "transError-result";
    }

    public String createTestData () {
        return "createTestData";
    }

    public void toCreateData () throws IOException {
        entityService.toCreateData(count);
    }

    public void createTransInfo () {
        entityService.createTransInfo();
    }

    /**
     * 校验过滤条件
     *
     * @return
     */
    public void checkWhereStr () throws Exception{
        Map<String, String> dataMap = new HashMap<String, String>(2);

        PmDataSourceQuery pmDataSourceQuery = new PmDataSourceQuery();
        pmDataSourceQuery.setId(queryEntity.getSrcDbId());
        PmDataSource pmDataSource = pmDataSourceService.selectByEntity(pmDataSourceQuery).get(0);
        String srcTb = queryEntity.getSrcTbCode();
        String whereStr = new String(Base64.decodeBase64(queryEntity.getWhereStr()),"UTF-8").toLowerCase();//获得解密后的sql

        whereStr = "where " + whereStr;
        if (whereStr.indexOf("limit") < 0) { //简单处理一般情况下不带LIMIT的SQL
            whereStr += " limit 1";
        }
        String sql = "select count(1) from " + srcTb + " " + whereStr;
        try {
            Connection connection = transTableStructureInfoService.getConnection(pmDataSource);
            transTableStructureInfoService.checkSQL(connection, sql);

            if (connection != null) {
                connection.close();
            }
        } catch(Exception e) {
            dataMap.put("flag", "err");
            dataMap.put("infos", "过滤条件有误，请检查：:" + e.getMessage());
            LOGGER.info("SQL测试失败，错误原因：{}", e.getMessage());
            toJosnLink(dataMap);
            return;
        }
        dataMap.put("flag", "rig"); //测试通过
        toJosnLink(dataMap);
    }

    /**
     * 校验CRON表达式
     *
     * @return
     */
    public void checkCron () {
        Map<String, String> dataMap = new HashMap<String, String>(2);

        try{
            Trigger trigger = null;
            QuartzSchedule qs = new QuartzSchedule();
            qs.setName("test"); //必要字段，这里暂设为test
            qs.setCron(queryEntity.getCron());
            trigger = quartzScheduleService.getCronTrigger(qs);
        } catch (Exception pe) {
            pe.printStackTrace();
            dataMap.put("flag","err");
            dataMap.put("infos","定时设置：CRON表达式错误，请检查！");
            toJosnLink(dataMap);
            return;
        }

        dataMap.put("flag", "rig"); //测试通过
        toJosnLink(dataMap);
    }

    private void setDataSourceValue () {
        User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
        srcDataSourceInfos = pmGroupService.getDataSourceByUserId(user.getId(), TransConstants.DATA_SOURCE_SRC_TABLE);
        targetDataSourceInfos = pmGroupService.getDataSourceByUserId(user.getId(), TransConstants.DATA_SOURCE_DEST_TABLE);
    }


    public TransInfo getEntity () {
        return entity;
    }

    public void setEntity (TransInfo entity) {
        this.entity = entity;
    }

    public TransInfoQuery getQueryEntity () {
        return queryEntity;
    }

    public void setQueryEntity (TransInfoQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService (TransInfoService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService (PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }

    public void setKeyGeneratorContainer (KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List<PmDataDictItem> getTransTypes () {
        return transTypes;
    }

    public void setTransTypes (List<PmDataDictItem> transTypes) {
        this.transTypes = transTypes;
    }

    public List<PmDataSource> getDataSourceInfos () {
        return dataSourceInfos;
    }

    public void setDataSourceInfos (List<PmDataSource> dataSourceInfos) {
        this.dataSourceInfos = dataSourceInfos;
    }

    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    public String getDataBaseId () {
        return dataBaseId;
    }

    public void setDataBaseId (String dataBaseId) {
        this.dataBaseId = dataBaseId;
    }

    public List<PmDataDictItem> getTransIsEnables () {
        return transIsEnables;
    }

    public List<PmDataDictItem> getTransInfoType () {
        return transInfoType;
    }

    public List<PmDataSource> getTargetDataSourceInfos () {
        return targetDataSourceInfos;
    }

    public String getFileName () {
        return fileName;
    }

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public InputStream getOutputDataStream () {
        return outputDataStream;
    }

    public void setOutputDataStream (InputStream outputDataStream) {
        this.outputDataStream = outputDataStream;
    }

    public File getFile () {
        return file;
    }

    public void setFile (File file) {
        this.file = file;
    }

    public void setCount (int count) {
        this.count = count;
    }

    public List<PmDataSource> getSrcDataSourceInfos () {
        return srcDataSourceInfos;
    }

    public void setSrcDataSourceInfos (List<PmDataSource> srcDataSourceInfos) {
        this.srcDataSourceInfos = srcDataSourceInfos;
    }

    public String getEncodeSQL() {
        return encodeSQL;
    }

    public void setEncodeSQL(String encodeSQL) {
        this.encodeSQL = encodeSQL;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNo() {
        return batchNo;
    }
}
