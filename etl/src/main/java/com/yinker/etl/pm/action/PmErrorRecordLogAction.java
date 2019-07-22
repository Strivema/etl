package com.yinker.etl.pm.action;

import com.yinker.etl.pm.model.PmErrorRecordLogQuery;
import com.yinker.etl.pm.model.base.PmErrorRecordLog;
import com.yinker.etl.pm.model.base.PmErrorRecordLogPK;
import com.yinker.etl.pm.service.PmErrorRecordLogService;

import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.TransTableStructureInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.service.PmDataDictItemService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorRecordLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:49
 * </pre>
 */
public class PmErrorRecordLogAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmErrorRecordLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmErrorRecordLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmErrorRecordLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmErrorRecordLogService entityService;

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;
	
	/* 等于normalQuery时查询日志*/
	private String from;
    private String selectedErrorIds;
    private String remark;
    private String errId;

	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new PmErrorRecordLog();
		queryEntity = new PmErrorRecordLogQuery();
	}
    public PmErrorRecordLogAction() {
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
    /*public String delete() throws Exception {
		String selectedErrorIds = request.getParameter("selectedErrorIds");
		if (StringUtils.isNotEmpty(selectedErrorIds)) {
			String[] errorIdArray = selectedErrorIds.split("\\|");
			for (int i = 0; i < errorIdArray.length; i++) {
                PmErrorRecordLogPK pk = new PmErrorRecordLogPK();
				pk.setErrorId(errorIdArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }
			
		}
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((PmErrorRecordLogPK)entity);
        return PROMPT;
    }*/

    /**
     * 处理方法
     *
     * @return
     * @throws Exception
     */
    public String addRemark() throws Exception {
        if ("toAddRemark".equals(operate)) {
            if (StringUtils.isNotEmpty(selectedErrorIds)) {
                String[] errorIdArray = selectedErrorIds.split("\\|");
                int flag1 = 0; //已处理
                int flag2 = 0; //未处理
                for (int i = 0; i < errorIdArray.length; i++) {
                    PmErrorRecordLogPK logPK = new PmErrorRecordLogPK();
                    PmErrorRecordLog log = new PmErrorRecordLog();
                    logPK.setErrorId(Integer.valueOf(errorIdArray[i]));
                    log = entityService.selectByPK(logPK);
                    if("9".equals(log.getErrStatus())) {
                        setRemark(log.getRemark());
                        flag1++;
                    }else {
                        flag2++;
                    }
                    if(flag1 > 0 && flag2 > 0){
                        setErrorMessage("请勿同时选择已处理和未处理日志");
                        return PROMPT;
                    }
                    if(flag1 > 1){
                        setErrorMessage("请勿选择多个已处理日志");
                        return PROMPT;
                    }
                }
                return "toAddRemark";
            }
            return PROMPT;
        }else if("toAddRemark-mongodb".equals(operate)){
            if (StringUtils.isNotEmpty(selectedErrorIds)) {
                String[] errorIdArray = selectedErrorIds.split("\\|");
                int flag1 = 0; //已处理
                int flag2 = 0; //未处理
                for (int i = 0; i < errorIdArray.length; i++) {
                    Map<String, Object> conditions = new HashMap<String, Object>();
                    PmErrorRecordLog log = entityService.selectMongoByErrorId(errorIdArray[i]);
                    if("9".equals(log.getErrStatus())) {
                        setRemark(log.getRemark());
                        flag1++;
                    }else {
                        flag2++;
                    }
                    if(flag1 > 0 && flag2 > 0){
                        setErrorMessage("请勿同时选择已处理和未处理日志");
                        return PROMPT;
                    }
                    if(flag1 > 1){
                        setErrorMessage("请勿选择多个已处理日志");
                        return PROMPT;
                    }
                }
                return "toAddRemark";
            }
            return PROMPT;
        } else{
            if("query".equals(from) || "normalQuery".equals(from)){
                if (StringUtils.isNotEmpty(selectedErrorIds)) {
                    String[] errorIdArray = selectedErrorIds.split("\\|");
                    for (int i = 0; i < errorIdArray.length; i++) {
                        PmErrorRecordLogPK logPK = new PmErrorRecordLogPK();
                        PmErrorRecordLog log = new PmErrorRecordLog();
                        logPK.setErrorId(Integer.valueOf(errorIdArray[i]));
                        log = entityService.selectByPK(logPK);
                        if(!"9".equals(log.getErrStatus())){
                            log.setErrStatus("9");
                            log.setRemark(remark);
                            entityService.updateByPK(log); //根据err_table_name和err_desc更新
                        }
                    }
                }
            }
            if("mongodb".equals(from)){
                if (StringUtils.isNotEmpty(selectedErrorIds)) {
                    String[] errorIdArray = selectedErrorIds.split("\\|");
                    for (int i = 0; i < errorIdArray.length; i++) {
                        PmErrorRecordLog log = new PmErrorRecordLog();
                        Map<String, Object> conditions = new HashMap<String, Object>();
                        conditions.put("errorId", Long.valueOf(errorIdArray[i]));
                        log = entityService.selectById(conditions);
                        if(!"9".equals(log.getErrStatus())){
                            log.setErrStatus("9");
                            log.setRemark(remark);
                            entityService.updateById(log); //根据err_table_name和err_desc更新
                        }
                    }
                }
            }
            return PROMPT;
        }
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     * 
     * @return
     * @throws Exception
     */
    public String view() throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
        if("query".equals(operate)){
            queryEntity.setSortColumns("err_status asc,err_create_time desc");
            page = entityService.pageQueryView(queryEntity);
        }else if("mongodb".equals(operate)){
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("errTableName", queryEntity.getErrTableName());
            conditions.put("errDesc", queryEntity.getErrDesc());
            page = entityService.mongoPageQueryView(conditions, queryEntity.getPageNumber(), queryEntity.getPageSize());
        }

        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());

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
        if ("normalQuery".equals(from)) {
            if(errId != null && !"".equals(errId)){
                TransInfoQuery transInfoQuery = new TransInfoQuery();
                transInfoQuery.setId(errId);
                List<TransInfo> transInfoList = transInfoService.selectByEntity(transInfoQuery);
                if (transInfoList != null && transInfoList.size() > 0) {
                    queryEntity.setErrTransName(transInfoList.get(0).getTransName());
                }
            }
        } else if ("tableStructure-normalQuery".equals(from)) {
            TransTableStructureInfoQuery transTableStructureInfoQuery = new TransTableStructureInfoQuery();
            transTableStructureInfoQuery.setId(queryEntity.getErrTransName());
            List<TransTableStructureInfo> transTableStructureInfoList = transTableStructureInfoService.selectByEntity(transTableStructureInfoQuery);
            if (transTableStructureInfoList != null && transTableStructureInfoList.size() > 0) {
                queryEntity.setErrTransName(transTableStructureInfoList.get(0).getTransName());
            }
        }
        if ("query".equals(operate)) {// 执行数据库查询操作
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            queryEntity.setSortColumns("err_status asc,err_create_time desc");

            page = entityService.pageQuery(queryEntity);

        } else if ("mongodb".equals(operate)) {
            Map<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("errTransName", queryEntity.getErrTransName());
            conditions.put("errTableName", queryEntity.getErrTableName());
            conditions.put("errStatus", queryEntity.getErrStatus());
            conditions.put("errCreateTimeStart", queryEntity.getErrCreateTimeStart());
            conditions.put("errCreateTimeEnd", queryEntity.getErrCreateTimeEnd());
            conditions.put("transId", queryEntity.getTransId());
            page = entityService.findByCondition(conditions, queryEntity.getPageNumber(), queryEntity.getPageSize());
        }

        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());

        if ("normalQuery".equals(from) || "tableStructure-normalQuery".equals(from)) {
            return "normalQuery-main";
        }
        return "list-result";
    }

    public PmErrorRecordLog getEntity() {
        return entity;
    }

    public void setEntity(PmErrorRecordLog entity) {
        this.entity = entity;
    }

    public PmErrorRecordLogQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(PmErrorRecordLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(PmErrorRecordLogService entityService) {
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

    public String getSelectedErrorIds() {
        return selectedErrorIds;
    }

    public void setSelectedErrorIds(String selectedErrorIds) {
        this.selectedErrorIds = selectedErrorIds;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getErrId() {
        return errId;
    }

    public void setErrId(String errId) {
        this.errId = errId;
    }
}
