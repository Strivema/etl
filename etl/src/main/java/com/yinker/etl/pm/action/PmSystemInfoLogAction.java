package com.yinker.etl.pm.action;

import com.yinker.etl.pm.model.PmSystemInfoLogQuery;
import com.yinker.etl.pm.model.base.PmSystemInfoLog;
import com.yinker.etl.pm.service.PmSystemInfoLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.framework.base.support.BaseStruts2Action;
import org.zwork.srdp.pm.service.PmDataDictItemService;

/** 
 * <pre>
 * <b>类描述:</b>PmSystemInfoLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:39:08
 * </pre>
 */
public class PmSystemInfoLogAction extends BaseStruts2Action {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmSystemInfoLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmSystemInfoLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmSystemInfoLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmSystemInfoLogService entityService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;
	
	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new PmSystemInfoLog();
		queryEntity = new PmSystemInfoLogQuery();
	}
    public PmSystemInfoLogAction() {
    }

    /**
     * 新增及新增保存页面
     * 
     * @return
     * @throws Exception
     */

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

    /**
     * 根据主键查询数据库，成功后转向查看页面
     * 
     * @return
     * @throws Exception
     */
    public String view() throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
		PmSystemInfoLog pmSystemInfoLog =  entityService.selectByPK(entity);
        if (entity == null) {
           throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
			entity = pmSystemInfoLog;
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
	
	public PmSystemInfoLog getEntity() {
        return entity;
    }

    public void setEntity(PmSystemInfoLog entity) {
        this.entity = entity;
    }

    public PmSystemInfoLogQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(PmSystemInfoLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(PmSystemInfoLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }
}
