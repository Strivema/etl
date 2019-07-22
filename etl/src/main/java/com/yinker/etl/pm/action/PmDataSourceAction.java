package com.yinker.etl.pm.action;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.pm.model.PmDataSourceQuery;
import com.yinker.etl.pm.model.PmDatasourceGroupQuery;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.model.base.PmDataSourcePK;
import com.yinker.etl.pm.model.base.PmDatasourceGroup;
import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.pm.service.PmDatasourceGroupService;
import com.yinker.etl.pm.util.AESUtil;
import com.yinker.etl.pm.util.DataSourceUtil;
import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import com.yinker.etl.trans.util.KettleDataSorceInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.session.User;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * <pre>
 * <b>类描述:</b>PmDataSource表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:42
 * </pre>
 */
public class PmDataSourceAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmDataSourceAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmDataSource entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmDataSourceQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmDataSourceService entityService;

    /* 抽取配置服务类 */
    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    @Resource(name = "pmDatasourceGroupService")
    private PmDatasourceGroupService pmDatasourceGroupService;

    /* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    /* 数据源类型 */
    protected List<PmDataDictItem> databaseTypeInfo;

    /* 数据源类型 */
    protected List<PmDataDictItem> databaseCategoryInfo;

    @Value("%{AESKey}")
    private String AESKey;

    /**
     * 执行初始化方法
     */
    public void init () {
        entity = new PmDataSource();
        queryEntity = new PmDataSourceQuery();

        databaseTypeInfo = pmDataDictItemService.selectByDictCode("etl.databaseType");
        databaseCategoryInfo = pmDataDictItemService.selectByDictCode("etl.databaseCategory");
    }

    public PmDataSourceAction () {
    }

    /**
     * 新增及新增保存页面
     *
     * @return
     * @throws Exception
     */
    public String create () throws Exception {
        if ("toCreate".equals(operate)) {// 转向新增页面
            entity.setDatabaseType("2"); //默认mysql
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存
            // 设置Id
            String id = keyGeneratorContainer.getNextKey("pmDataSourceId");
            entity.setId(id);
            Date now = new Date();
            entity.setCreateTime(now);
            entity.setLastUpdateTime(now);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOwner(user.getName());
            LOGGER.debug("新增数据，新增前对象信息：{}", entity);

            try {

                String psw = AESUtil.encrypt(AESKey, entity.getPassword()); //对数据库密码进行AES加密
                entity.setPassword(psw);
                LOGGER.debug("对数据库密码进行AES加密并存入数据库：{}", psw);
                entityService.insert(entity);

            } catch(IOException e) {
                e.printStackTrace();
            }

            // 初始化数据源操作
            entityService.initDataSource();
            SimpleTrans.setDatabaseMetaList(KettleDataSorceInit.initDataSource());

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
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);

            Date now = new Date();
            entity.setLastUpdateTime(now);

            try {

                String psw = AESUtil.encrypt(AESKey, entity.getPassword()); //对数据库密码进行AES加密
                LOGGER.debug("对数据库密码进行AES加密并更新数据库：{}", psw);
                entity.setPassword(psw);
                //数据源信息修改时同步etl抽取配置，自定义结构抽取表信息
                transInfoService.updateDbCode(entity.getId(), entity.getCode());
                transTableStructureInfoService.updateDbCode(entity.getId(), entity.getCode());

                entityService.updateByPK(entity);
            } catch(IOException e) {
                e.printStackTrace();
            }
            // 初始化数据源操作
            entityService.initDataSource();
            SimpleTrans.setDatabaseMetaList(KettleDataSorceInit.initDataSource());

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
        String selectedErrorIds = request.getParameter("selectedIds");
        if (StringUtils.isNotEmpty(selectedErrorIds)) {
            String[] idArray = selectedErrorIds.split("\\|");
            for(int i = 0; i < idArray.length; i++) {
                TransInfoQuery ti = new TransInfoQuery();
                ti.setSrcDbId(idArray[i]);
                List<TransInfo> tiSrcList = transInfoService.selectByEntity(ti);

                ti.setSrcDbId(null);
                ti.setTargetDbId(idArray[i]);
                List<TransInfo> tiTarList = transInfoService.selectByEntity(ti);

                PmDatasourceGroupQuery pmDatasourceGroupQuery = new PmDatasourceGroupQuery();
                pmDatasourceGroupQuery.setDatasourceId(idArray[i]);
                List<PmDatasourceGroup> pgList = pmDatasourceGroupService.selectByEntity(pmDatasourceGroupQuery);

                if ((tiSrcList == null || tiSrcList.size() == 0) && (tiTarList == null || tiTarList.size() == 0) && (pgList == null || pgList.size() == 0)) {
                    PmDataSourcePK pk = new PmDataSourcePK();
                    pk.setId(idArray[i]);
                    LOGGER.debug("删除数据:{}", pk.pkString());

                    entityService.deleteByPK(pk);
                } else if ((tiSrcList != null && tiSrcList.size() > 0) || (tiTarList != null && tiTarList.size() > 0)) {
                    setWarnMessage("有转换正在使用此数据源，不可删除！");
                } else if (pgList != null && pgList.size() > 0) {
                    setWarnMessage("有权限组正在使用此数据源，不可删除！");
                }
            }
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
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
        PmDataSource pmDataSource = entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            entity = pmDataSource;
            entity.setPassword("******");//VIEW查看时遮挡密码密文
        }
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    public String toList () throws Exception {
        return "list";
    }

    /**
     * 查询列表页面
     *
     * @return
     * @throws Exception
     */
    public String list () throws Exception {
        if ("query".equals(operate)) {// 执行数据库查询操作
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
            return "list-result";
        } else { //转向查询页面，但不执行数据库查询操作
            return "list-result";
        }
    }

    /**
     * 测试数据库连接功能
     *
     * @throws Exception
     */
    public void connectionTest () throws IOException {
        //首先验证数据库名称是否重复
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        Map<String, String> dataMap = new HashMap<String, String>(2);

        PmDataSourceQuery pmDataSourceQuery = new PmDataSourceQuery();
        pmDataSourceQuery.setCode(queryEntity.getCode());
        List<PmDataSource> entityList = entityService.selectByEntity(pmDataSourceQuery);
        if (entityList.size() >= 1 && "add".equals(operate)) {
            dataMap.put("flag", "3");
            dataMap.put("infos", "数据源名称(code)重复！");
            toJosnLink(dataMap);
            return;
        }
        if (entityList.size() >= 1 && "edit".equals(operate)) {
            pmDataSourceQuery.setCode(null);
            pmDataSourceQuery.setId(queryEntity.getId());
            List<PmDataSource> entityListSecond = entityService.selectByEntity(pmDataSourceQuery);
            if (entityListSecond != null && entityListSecond.size() > 0 && !entityListSecond.get(0).getCode().equals(queryEntity.getCode())) {
                dataMap.put("flag", "3");
                dataMap.put("infos", "数据源名称(code)重复！");
                toJosnLink(dataMap);
                return;
            }
        }
        /*PmDataSourceQuery pmDataSourceQuery2 = new PmDataSourceQuery();
        pmDataSourceQuery2.setHostName(queryEntity.getHostName());
        pmDataSourceQuery2.setDatabaseType(queryEntity.getDatabaseType());
        pmDataSourceQuery2.setCode(queryEntity.getCode());
        List<PmDataSource> entityList2 = entityService.selectByEntity(pmDataSourceQuery2);
        if (("add".equals(operate) && entityList2.size() > 0) || ("edit".equals(operate) && (entityList2.size() >= 2 || (entityList2.size() == 1 && !entityList2.get(0).getId().equals(queryEntity.getId()))))) {
            dataMap.put("flag", "3");
            dataMap.put("infos", "数据源已存在，请勿重复添加！");
            toJosnLink(dataMap);
            return;
        }*/

        Connection con = null;
        JSONObject t = new JSONObject(true);
        try {
            if ("1".equals(queryEntity.getDatabaseType())) {
                // oracle 链接方式
                con = DataSourceUtil.getOracleConnection(queryEntity.getHostName(), queryEntity.getPort().toString(), queryEntity.getUserName(), queryEntity.getPassword(), queryEntity.getDatabaseName());
                dataMap.put("flag", "1");
                dataMap.put("infos", "测试连接成功！");
                toJosnLink(dataMap);
            } else if ("2".equals(queryEntity.getDatabaseType())) {
                // mysql 链接方式
                con = DataSourceUtil.getMysqlConnection(queryEntity.getHostName(), queryEntity.getPort().toString(), queryEntity.getUserName(), queryEntity.getPassword(), queryEntity.getDatabaseName());
                dataMap.put("flag", "1");
                dataMap.put("infos", "测试连接成功！");
                toJosnLink(dataMap);
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            dataMap.put("flag", "0");
            String errorMessage = "测试连接失败！" + e.getMessage();
            errorMessage = errorMessage.replaceAll("\r|\n|\'|\"", "");
            dataMap.put("infos", errorMessage);
            toJosnLink(dataMap);
        } finally {
            // 关闭资源
            try {
                if (con != null) {
                    con.close();
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
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

    public PmDataSource getEntity () {
        return entity;
    }

    public void setEntity (PmDataSource entity) {
        this.entity = entity;
    }

    public PmDataSourceQuery getQueryEntity () {
        return queryEntity;
    }

    public void setQueryEntity (PmDataSourceQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService (PmDataSourceService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService (PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }

    public void setKeyGeneratorContainer (KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List<PmDataDictItem> getDatabaseTypeInfo () {
        return databaseTypeInfo;
    }

    public void setDatabaseTypeInfo (List<PmDataDictItem> databaseTypeInfo) {
        this.databaseTypeInfo = databaseTypeInfo;
    }

    public List<PmDataDictItem> getDatabaseCategoryInfo () {
        return databaseCategoryInfo;
    }

    public void setDatabaseCategoryInfo (List<PmDataDictItem> databaseCategoryInfo) {
        this.databaseCategoryInfo = databaseCategoryInfo;
    }


}
