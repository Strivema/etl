package com.yinker.etl.pm.action;

import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.service.TransInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseStruts2Action;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;

import com.yinker.etl.pm.model.PmKettleTransLogQuery;
import com.yinker.etl.pm.model.base.PmKettleTransLog;
import com.yinker.etl.pm.model.base.PmKettleTransLogPK;
import com.yinker.etl.pm.service.PmKettleTransLogService;

import javax.annotation.Resource;
import java.util.*;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleTransLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:47
 * </pre>
 */
public class PmKettleTransLogAction extends BaseStruts2Action {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmKettleTransLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmKettleTransLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmKettleTransLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmKettleTransLogService entityService;

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 数据字典-状态 */
    private List<PmDataDictItem> transStatus;

    /* 数据字典-转换名称列表 */
    private List pmTransNameInfos;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;
	
	/* 等于normalQuery时查询日志*/
	private String from;

    /* 等于mongodb时查询mongo详情*/
    private String query;
	
	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new PmKettleTransLog();
		queryEntity = new PmKettleTransLogQuery();

        transStatus = pmDataDictItemService.selectByDictCode("etl.transStatus");

        pmTransNameInfos = transInfoService.selectIdAndTransNameMap();
	}
    public PmKettleTransLogAction() {
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
			String id = keyGeneratorContainer.getNextKey("pmKettleTransLogId");
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
                PmKettleTransLogPK pk = new PmKettleTransLogPK();
				pk.setId(idArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }
			
		}
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((PmKettleTransLogPK)entity);
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
    
    public String toList(){
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
            queryEntity.setSortColumns("LOGDATE desc");
            if(queryEntity.getTransname() != null && !"".equals(queryEntity.getTransname()) && !"normalQuery".equals(from)){
                TransInfoQuery transInfoQuery = new TransInfoQuery();
                transInfoQuery.setTransName(queryEntity.getTransname());
                List<String> idList = transInfoService.selectIdByTransName(transInfoQuery);
                page = entityService.myPageQuery(queryEntity, idList);
            }else {
                page = entityService.pageQuery(queryEntity);
            }
        } else if ("mongodb".equals(operate) && !"normalQuery".equals(from)) {
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("STATUS", queryEntity.getStatus());
            conditions.put("LOGDATESTART", queryEntity.getStartdate());
            conditions.put("LOGDATEEND", queryEntity.getEnddate());
            if (queryEntity.getTransname() != null && !"".equals(queryEntity.getTransname())){
                TransInfoQuery transInfoQuery = new TransInfoQuery();
                transInfoQuery.setTransName(queryEntity.getTransname());
                List<String> idList = transInfoService.selectIdByTransName(transInfoQuery);
                conditions.put("TRANSNAME", idList);
            }
            page = entityService.findByCondition(conditions, queryEntity.getPageNumber(), queryEntity.getPageSize());
        } else if ("mongodb".equals(operate) && "normalQuery".equals(from)){
            Map<String, Object> conditions = new HashMap<String, Object>();
            List<String> idList = new ArrayList<String>();
            idList.add(queryEntity.getTransname());
            conditions.put("TRANSNAME", idList);
            page = entityService.findByCondition(conditions, queryEntity.getPageNumber(), queryEntity.getPageSize());
        }
        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        if("normalQuery".equals(from)){
        	return "normalQuery-main";
        }
        return "list-result";
    }

    public String getTransErrorList(){
        return "transErrorList";
    }

    public String transErrorList(){
        queryEntity.setExtendSQL("Y");
        page = entityService.pageQuery(queryEntity);
        return "transErrorList-result";
    }

    public String errorview() throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
        PmKettleTransLog pmKettleTransLog =  entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            entity = pmKettleTransLog;
        }
        LOGGER.debug("查询结果:{}", entity);
        return "errorview";
    }

    public String getStepAndErrorLog(){
        return "normalQuery";
    }
	
	public PmKettleTransLog getEntity() {
        return entity;
    }

    public void setEntity(PmKettleTransLog entity) {
        this.entity = entity;
    }

    public PmKettleTransLogQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(PmKettleTransLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(PmKettleTransLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

    public List getPmTransNameInfos() {
        return pmTransNameInfos;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<PmDataDictItem> getTransStatus() {
        return transStatus;
    }
}
