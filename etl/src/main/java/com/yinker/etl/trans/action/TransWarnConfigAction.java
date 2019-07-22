package com.yinker.etl.trans.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseStruts2Action;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import com.yinker.etl.trans.model.TransWarnConfigQuery;
import com.yinker.etl.trans.model.base.TransWarnConfig;
import com.yinker.etl.trans.model.base.TransWarnConfigPK;
import com.yinker.etl.trans.service.TransWarnConfigService;

import java.util.Date;
import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>TransWarnConfig表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 20:20:31
 * </pre>
 */
public class TransWarnConfigAction extends BaseStruts2Action {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransWarnConfigAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected TransWarnConfig entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected TransWarnConfigQuery queryEntity;

    /* 定义服务层操作类 */
    protected TransWarnConfigService entityService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;
	
	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new TransWarnConfig();
		queryEntity = new TransWarnConfigQuery();
	}
    public TransWarnConfigAction() {
    }

    /**
     * 新增及新增保存页面
     * 
     * @return
     * @throws Exception
     */
    public String create() throws Exception {
        if ("toCreate".equals(operate)) {// 转向新增页面
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存
			// 设置Id
			String id = keyGeneratorContainer.getNextKey("transWarnConfigId");
            entity.setId(id);

            Date data = new Date();
            entity.setCreateTime(data);
            entity.setLastUpdateTime(data);

            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
            entityService.insert(entity);
            setInfoMessage("操作成功!");
            return PROMPT;
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
    public String edit() throws Exception {
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据

            LOGGER.debug("准备更新user对象,主键信息：{}", entity.pkString());

            queryEntity.setTransId(entity.getTransId());
            List<TransWarnConfig> entityList = entityService.selectByEntity(queryEntity);
            if (entityList.size() == 0) {
                setWarnMessage("本条记录未曾配置，请先添加配置!");
                return CREATE;
            }else{
                entity = entityList.get(0);
                LOGGER.debug("更新前数据库中user信息[{}]" + entity);

                return EDIT;
            }

        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);

            entity.setLastUpdateTime(new Date());
            entityService.updateByPK(entity);
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
    public String delete() throws Exception {
		String selectedIds = request.getParameter("selectedIds");
		if (StringUtils.isNotEmpty(selectedIds)) {
			String[] idArray = selectedIds.split("\\|");
			for (int i = 0; i < idArray.length; i++) {
                TransWarnConfigPK pk = new TransWarnConfigPK();
				pk.setId(idArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }
			
		}
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((TransWarnConfigPK)entity);
        return PROMPT;
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     * 
     * @return
     * @throws Exception
     */
    public String view() throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
		TransWarnConfig transWarnConfig =  entityService.selectByPK(entity);
        if (entity == null) {
           throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
			entity = transWarnConfig;
		}
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    /**
     * 查询列表页面
     * 
     * @return
     * @throws Exception
     */
    public String list() throws Exception {
        if ("query".equals(operate)) {// 执行数据库查询操作
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
            return LIST;
        } else { //转向查询页面，但不执行数据库查询操作
            return LIST;
        }
    }
	
	public TransWarnConfig getEntity() {
        return entity;
    }

    public void setEntity(TransWarnConfig entity) {
        this.entity = entity;
    }

    public TransWarnConfigQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(TransWarnConfigQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(TransWarnConfigService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }
}
