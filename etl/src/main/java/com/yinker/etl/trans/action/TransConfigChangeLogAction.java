package com.yinker.etl.trans.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;

import com.yinker.etl.trans.model.TransConfigChangeLogQuery;
import com.yinker.etl.trans.model.base.TransConfigChangeLog;
import com.yinker.etl.trans.model.base.TransConfigChangeLogPK;
import com.yinker.etl.trans.service.TransConfigChangeLogService;

/** 
 * <pre>
 * <b>类描述:</b>TransConfigChangeLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:59
 * </pre>
 */
public class TransConfigChangeLogAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransConfigChangeLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected TransConfigChangeLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected TransConfigChangeLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected TransConfigChangeLogService entityService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;
	
	/* 操作类型 */
    protected List<PmDataDictItem> operateTypeList;
	
	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new TransConfigChangeLog();
		queryEntity = new TransConfigChangeLogQuery();
		
		operateTypeList = pmDataDictItemService.selectByDictCode("trans.operateType");
	}
    public TransConfigChangeLogAction() {
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
			String id = keyGeneratorContainer.getNextKey("transConfigChangeLogId");
            entity.setId(id);
            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
            entityService.insert(entity);
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
    public String edit() throws Exception {
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);
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
                TransConfigChangeLogPK pk = new TransConfigChangeLogPK();
				pk.setId(idArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }
			
		}
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((TransConfigChangeLogPK)entity);
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
		TransConfigChangeLog transConfigChangeLog =  entityService.selectByPK(entity);
        if (entity == null) {
           throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
			entity = transConfigChangeLog;
		}
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    
    public String toList() throws Exception {
    	return LIST;
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
            queryEntity.setSortColumns(" create_time desc");
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
	
	public TransConfigChangeLog getEntity() {
        return entity;
    }

    public void setEntity(TransConfigChangeLog entity) {
        this.entity = entity;
    }

    public TransConfigChangeLogQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(TransConfigChangeLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(TransConfigChangeLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }
	public List<PmDataDictItem> getOperateTypeList() {
		return operateTypeList;
	}
	
	
}
