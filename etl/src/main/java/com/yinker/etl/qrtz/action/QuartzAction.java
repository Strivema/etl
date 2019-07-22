package com.yinker.etl.qrtz.action;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.qrtz.model.base.QuartzSchedule;
import com.yinker.etl.qrtz.service.QuartzScheduleService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.Pagination;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>任务调度管理Action类
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:42
 * </pre>
 */
public class QuartzAction extends BaseFlowAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzAction.class);
	/* 任务调度服务类 */
	private QuartzScheduleService<QuartzSchedule> quartzScheduleService;
	/* 数据字典服务类 */
	protected PmDataDictItemService pmDataDictItemService;

	//任务调度实体类
	private QuartzSchedule entity;
	//调度者
	private Scheduler scheduler;
	//任务调度Map，用于前台显示
	private Map<?, ?> quartzMap;
	//任务调度详情，用于前台显示
	private JobDetail jobDetail;
	//执行者（类路径），用于前台显示
	private String className;
	//数据字典：任务调度群组名称
    private List<PmDataDictItem> qrtzGroup;
    
	/**
     *  执行初始化方法
     */
    public void init(){
    	entity = new QuartzSchedule();
    	quartzMap = new HashMap<String, Object>();
    	
    	qrtzGroup = pmDataDictItemService.selectByDictCode("qrtz.group");
    }
    
    public QuartzAction(){
    }
    
    /**
     * 新增定时任务
     * 
     * @return
     * @throws Exception
     */
    public String create() throws Exception {
        if ("toCreate".equals(operate)) {// 转向新增页面
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存
        	try{
                LOGGER.debug("新增数据，新增前对象信息：{}", entity);
				if (!quartzScheduleService.exists(entity)) {
					quartzScheduleService.insert(entity);
					setInfoMessage("操作成功!");
					LOGGER.debug("新增数据成功");
				}else {
					setWarnMessage("操作失败!");
					LOGGER.debug("新增数据失败");
				}
        	}catch(Exception ex){
        		ex.printStackTrace();
        		throw new Exception("操作失败，请联系管理员！");
        	}
            return PROMPT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }
    
    public String toList(){
    	return LIST;
    }
    
    /**
     * 查询调度任务列表
     * @return
     */
    @SuppressWarnings("unchecked")
	public String list(){
    	try {
			Map<?, ?> cronMap;
    		if("DIY_ETLTRANS".equals(entity.getGroup())||"TABLE_TRANS".equals(entity.getGroup())){
				cronMap = (Map<?, ?>) quartzScheduleService.selectByWhere(entity, true);
			}else {
				cronMap = (Map<?, ?>) quartzScheduleService.selectByWhere(entity, false);
			}
    		scheduler = (Scheduler) cronMap.get("scheduler");
    		page = new Pagination();
    		List<HashMap<String, Object>> mapList = (List<HashMap<String, Object>>)cronMap.get("jobList");
    		
			page.setPageNumber(entity.getPageNumber());
			page.setPageSize(entity.getPageSize());
			page.setCount(mapList.size());
			
			List<HashMap<String, Object>> subList;
			int pageStart = (page.getPageNumber() - 1) * page.getPageSize();
			if(pageStart < page.getCount()){
				int pageEnd = pageStart + page.getPageSize();
				if(pageEnd < page.getCount()){
					subList = mapList.subList(pageStart, pageEnd);
				}else{
					subList = mapList.subList(pageStart, page.getCount());
				}
			}else{
				subList = mapList;
			}
			page.setData(subList);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    	
    	return "list-result";
    }
    
    /**
     * 更新调度任务
     * 
     * @return
     * @throws SchedulerException
     */
    public String edit() throws Exception {
    	if ("toEdit".equals(operate)) {// 转向修改页面
    		quartzMap = (Map<?, ?>) quartzScheduleService.select(entity);
    		jobDetail = (JobDetail)quartzMap.get("jobDetail");
    		setClassName(jobDetail.getJobClass().getName());
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
        	if (!quartzScheduleService.exists(entity)) {
        		quartzScheduleService.update(entity);
				setInfoMessage("操作成功!");
				LOGGER.debug("更新数据成功!");
    		}else {
				setWarnMessage("操作失败!");
				LOGGER.debug("更新数据失败");
			}
            return PROMPT;
        }
    	
		return EDIT;
	}
    
    /**
     * 查看调度任务详情
     * @return
     */
    public String view() {
		try {
			quartzMap = (Map<?, ?>) quartzScheduleService.select(entity);
			jobDetail = (JobDetail)quartzMap.get("jobDetail");
			setClassName(jobDetail.getJobClass().getName());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
        return VIEW;
	}
    
    /**
     * 删除任务
     * 
     * @return
     * @throws SchedulerException
     */
    public String delete() {
    	
    	try {
    		String selectedIds = request.getParameter("ids");
    		if (StringUtils.isNotEmpty(selectedIds)) {
    			String[] idArray = selectedIds.split("\\|");
    			for (int i = 0; i < idArray.length; i++) {
    				quartzScheduleService.delete(idArray[i]);
                }
    			setWarnMessage("删除成功！");
    		}
    	} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return PROMPT;
	}
    
    /**
     * 执行调度任务
     * @return 
     */
    @Override
    public String execute(){
    	
    	try {
			String jobKeys = request.getParameter("id");
			if (null != jobKeys) {
				quartzScheduleService.execute(jobKeys);
				
				setWarnMessage("启动成功！");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    	
    	return PROMPT;
    }
    
    /**
     * 暂停调度任务
     * 
     * @return
     * @throws IOException
     */
    public String pause() throws IOException {
		try {
			String jobKeys = request.getParameter("id");
			if (null != jobKeys) {
				quartzScheduleService.pause(jobKeys);
				
				setWarnMessage("任务已暂停！");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return PROMPT;
	}
    
    /**
     * 恢复任务调度
     * 
     * @return
     */
    public String resume() {
		try {
			String jobKeys = request.getParameter("id");
			if (null != jobKeys) {
				quartzScheduleService.resume(jobKeys);
				
				setWarnMessage("任务已恢复！");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return PROMPT;
	}

	/**
	 * 执行者字段，CRON表达式校验
	 *
	 * @throws Exception
	 */
	public void check(){
    	Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("flag","1");
    	try{
			Trigger trigger = null;
			trigger = quartzScheduleService.getCronTrigger(entity);
			Class jobClass = Class.forName(entity.getExecutor());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			dataMap.put("flag","2");
			dataMap.put("infos","执行者（Executor）错误，请检查！");
			toJosnLink(dataMap);
			return;
		} catch (Exception pe) {
			pe.printStackTrace();
			dataMap.put("flag","3");
			dataMap.put("infos","定时设置：CRON表达式错误，请检查！");
			toJosnLink(dataMap);
			return;
		}
		toJosnLink(dataMap);
	}

	/**
	 * 将Map转化为JSON格式
	 *
	 * @throws Exception
	 */
	public void toJosnLink(Map object){
		JSONObject t =  new JSONObject(true);
		t.put("link", object);
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(t.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public QuartzSchedule getEntity() {
		return entity;
	}

	public void setEntity(QuartzSchedule entity) {
		this.entity = entity;
	}

	public void setQuartzScheduleService(
			QuartzScheduleService<QuartzSchedule> quartzScheduleService) {
		this.quartzScheduleService = quartzScheduleService;
	}

	public List<PmDataDictItem> getQrtzGroup() {
		return qrtzGroup;
	}

	public void setQrtzGroup(List<PmDataDictItem> qrtzGroup) {
		this.qrtzGroup = qrtzGroup;
	}

	public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
		this.pmDataDictItemService = pmDataDictItemService;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public Map<?, ?> getQuartzMap() {
		return quartzMap;
	}

	public void setQuartzMap(Map<?, ?> quartzMap) {
		this.quartzMap = quartzMap;
	}

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
