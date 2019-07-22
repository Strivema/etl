package com.yinker.etl.pm.action;

import com.yinker.etl.pm.model.PmMongoTransferBatchLogQuery;
import com.yinker.etl.pm.model.base.PmMongoTransferBatchLog;
import com.yinker.etl.pm.service.PmMongoTransferBatchLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.service.PmDataDictItemService;

/**
 * <pre>
 * <b>类描述:</b>pm_mongo_transfer_batch_log表对应的Action类。
 * </pre>
 */
public class PmMongoTransferBatchLogAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmMongoTransferBatchLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmMongoTransferBatchLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmMongoTransferBatchLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmMongoTransferBatchLogService entityService;

    /* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    /**
     * 执行初始化方法
     */
    public void init () {
        entity = new PmMongoTransferBatchLog();
        queryEntity = new PmMongoTransferBatchLogQuery();
    }

    public PmMongoTransferBatchLogAction () {
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
            queryEntity.setSortColumns("create_time desc");
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        }
        return "list-result";
    }

    public PmMongoTransferBatchLog getEntity () {
        return entity;
    }

    public void setEntity (PmMongoTransferBatchLog entity) {
        this.entity = entity;
    }

    public PmMongoTransferBatchLogQuery getQueryEntity () {
        return queryEntity;
    }

    public void setQueryEntity (PmMongoTransferBatchLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public PmMongoTransferBatchLogService getEntityService () {
        return entityService;
    }

    public void setEntityService (PmMongoTransferBatchLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService (
            PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }

    public void setKeyGeneratorContainer (
            KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }


}
