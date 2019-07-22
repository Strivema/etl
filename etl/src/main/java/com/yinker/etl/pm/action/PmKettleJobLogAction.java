package com.yinker.etl.pm.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import com.yinker.etl.pm.model.PmKettleJobLogQuery;
import com.yinker.etl.pm.model.base.PmKettleJobLog;
import com.yinker.etl.pm.model.base.PmKettleJobLogPK;
import com.yinker.etl.pm.service.PmKettleJobLogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:51
 * </pre>
 */
public class PmKettleJobLogAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmKettleJobLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmKettleJobLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmKettleJobLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmKettleJobLogService entityService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;

    private List<PmDataDictItem> transStatus;

    private String query;

	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new PmKettleJobLog();
		queryEntity = new PmKettleJobLogQuery();

        transStatus = pmDataDictItemService.selectByDictCode("etl.transStatus");
	}
    public PmKettleJobLogAction() {
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
		String selectedIdJobs = request.getParameter("selectedIdJobs");
		if (StringUtils.isNotEmpty(selectedIdJobs)) {
			String[] idJobArray = selectedIdJobs.split("\\|");
			for (int i = 0; i < idJobArray.length; i++) {
                PmKettleJobLogPK pk = new PmKettleJobLogPK();
				pk.setIdJob(idJobArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }
			
		}
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((PmKettleJobLogPK)entity);
        return PROMPT;
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     * 
     * @return
     * @throws Exception
     */
    public String view() throws Exception {

        if("mysql".equals(query)) {
            // 从mysql数据库中查询数据
            LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
            entity = entityService.selectByPK(entity);
        } else if("mongodb".equals(query)) {
            // 从mongodb数据库中查询数据
            LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("CHANNEL_ID", entity.getChannelId());
            entity = entityService.selectById(conditions);
        }
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        }
        LOGGER.debug("查询结果:{}", entity);

        return VIEW;
    }

    public String toList() throws Exception{
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

            page = entityService.pageQuery(queryEntity);
        }else if ("mongodb".equals(operate)) {
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("JOBNAME", queryEntity.getJobname());
            conditions.put("STATUS", queryEntity.getStatus());
            conditions.put("LOGDATESTART", queryEntity.getLogdateStart());
            conditions.put("LOGDATEEND", queryEntity.getLogdateEnd());
            page = entityService.findByCondition(conditions, queryEntity.getPageNumber(), queryEntity.getPageSize());
        }
        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        return "list-result";
    }
	
	public PmKettleJobLog getEntity() {
        return entity;
    }

    public void setEntity(PmKettleJobLog entity) {
        this.entity = entity;
    }

    public PmKettleJobLogQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(PmKettleJobLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(PmKettleJobLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List<PmDataDictItem> getTransStatus() {
        return transStatus;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
