package com.yinker.etl.pm.action;

import com.yinker.etl.pm.model.PmMongoTransferTableNameInfoQuery;
import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfo;
import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfoPK;
import com.yinker.etl.pm.service.PmMongoTransferTableNameInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.PMManager;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.session.User;

import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>pm_mongo_transfer_batch_log表对应的Action类。
 * </pre>
 */
public class PmMongoTransferTableNameInfoAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmMongoTransferTableNameInfoAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmMongoTransferTableNameInfo entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmMongoTransferTableNameInfoQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmMongoTransferTableNameInfoService entityService;

    /* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    /* 是否删除 */
    private List<PmDataDictItem> isDeleteList;
    /* 状态 */
    private List<PmDataDictItem> stateList;

    /* 数据字典-日志名称列表 */
    private List etlLogList;

    /**
     * 执行初始化方法
     */
    public void init () {
        entity = new PmMongoTransferTableNameInfo();
        queryEntity = new PmMongoTransferTableNameInfoQuery();
    }

    public PmMongoTransferTableNameInfoAction () {
    }

    /**
     * 新增及新增保存页面
     *
     * @return
     * @throws Exception
     */
    public String create () throws Exception {
        //获取ETL数据库的LOG表
        etlLogList = entityService.selectEtlLogList();
        if ("toCreate".equals(operate)) {// 转向新增页面
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存

            queryEntity.setTableName(entity.getTableName());
            List<PmMongoTransferTableNameInfo> list = entityService.selectByEntity(queryEntity);
            if(list != null && list.size() > 0){
                setWarnMessage("跑批表已经存在，请勿重复添加！");
                return EDIT;
            }

            // 设置Id
            String id = keyGeneratorContainer.getNextKey("pmMongoTransferBatchLogId");
            entity.setId(id);
            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOperator(user.getName());
            entity.setCreateTime(PMManager.getCurrentWorkDateTime());
            entity.setLastUpdateTime(PMManager.getCurrentWorkDateTime());

            if("N".equals(entity.getIsDiyRetentionTime())){
                entity.setRetentionTime(null);
                entity.setRetentionColumnName("");
            }
            if("Y".equals(entity.getIsDiyRetentionTime())){
                entity.setIsDelete("Y");
            }

            entityService.insert(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("新增数据成功");
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
            String[] errorIdArray = selectedErrorIds.split("\\|");
            for(int i = 0; i < errorIdArray.length; i++) {
                PmMongoTransferTableNameInfoPK pk = new PmMongoTransferTableNameInfoPK();
                pk.setId(errorIdArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }

        }
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK(entity);
        return PROMPT;
    }

    /**
     * 修改及修改保存页面
     *
     * @return
     * @throws Exception
     */
    public String edit () throws Exception {
        //获取ETL数据库的LOG表
        etlLogList = entityService.selectEtlLogList();
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新user对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中user信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);
            entity.setLastUpdateTime(PMManager.getCurrentWorkDateTime());

            if("N".equals(entity.getIsDiyRetentionTime())){
                entity.setRetentionTime(null);
                entity.setRetentionColumnName("");
            }
            if("Y".equals(entity.getIsDiyRetentionTime())){
                entity.setIsDelete("Y");
            }

            entityService.updateByPK(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("更新数据成功!");
            return PROMPT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
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
        entity = entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
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
        if ("query".equals(operate)) {
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        }
        return "list-result";
    }

    public String changeState () {
        String selectedIds = request.getParameter("selectedIds");
        String state = request.getParameter("state");
        if (StringUtils.isNotEmpty(selectedIds)) {
            String[] idArray = selectedIds.split("\\|");
            for(int i = 0; i < idArray.length; i++) {
                PmMongoTransferTableNameInfoPK pk = new PmMongoTransferTableNameInfoPK();
                pk.setId(idArray[i]);
                entity = entityService.selectByPK(pk);

                entity.setLastUpdateTime(PMManager.getCurrentWorkDateTime());
                entity.setState(state);
                entityService.updateByPK(entity);
            }
        }
        return PROMPT;
    }

    public PmMongoTransferTableNameInfo getEntity () {
        return entity;
    }

    public void setEntity (PmMongoTransferTableNameInfo entity) {
        this.entity = entity;
    }

    public PmMongoTransferTableNameInfoQuery getQueryEntity () {
        return queryEntity;
    }

    public void setQueryEntity (PmMongoTransferTableNameInfoQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public PmMongoTransferTableNameInfoService getEntityService () {
        return entityService;
    }

    public void setEntityService (
            PmMongoTransferTableNameInfoService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService (
            PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }


    public List<PmDataDictItem> getIsDeleteList () {
        return isDeleteList;
    }

    public void setIsDeleteList (List<PmDataDictItem> isDeleteList) {
        this.isDeleteList = isDeleteList;
    }

    public List<PmDataDictItem> getStateList () {
        return stateList;
    }

    public void setStateList (List<PmDataDictItem> stateList) {
        this.stateList = stateList;
    }

    public void setKeyGeneratorContainer (
            KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List getEtlLogList() {
        return etlLogList;
    }

    public void setEtlLogList(List etlLogList) {
        this.etlLogList = etlLogList;
    }
}
