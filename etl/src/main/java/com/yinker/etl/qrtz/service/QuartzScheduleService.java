package com.yinker.etl.qrtz.service;

import com.yinker.etl.qrtz.QuartzConstants;
import com.yinker.etl.qrtz.dao.QuartzScheduleDao;
import com.yinker.etl.qrtz.model.base.QuartzSchedule;
import net.sf.json.JSONArray;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.StringMatcher.StringOperatorName;
import org.zwork.common.utils.StringUtils;

import java.util.*;
import java.util.Map.Entry;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzScheduleService<T extends QuartzSchedule>  {

	private Scheduler scheduler;
	private QuartzScheduleDao quartzScheduleDao;

	/**
	 * 添加定时任务
	 * 
	 * @param entity
	 * @return String
	 * @throws SchedulerException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String insert(T entity) throws SchedulerException, ClassNotFoundException {
		if (null != entity && StringUtils.isNotEmpty(entity.getName()) && StringUtils.isNotEmpty(entity.getGroup())) {
			Trigger trigger = null;

			// 定时设置 cron模式
			trigger = getCronTrigger(entity);

			try {
				Class jobClass = Class.forName(entity.getExecutor());
				JobDetail job = newJob(jobClass).withIdentity(entity.getName(), entity.getGroup())
						.withDescription(entity.getDescription()).build();
				JobDataMap map = job.getJobDataMap();
				Iterator<?> it = entity.getMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<?, ?> ent = (Entry<?, ?>) it.next();
					map.put((String) ent.getKey(), ent.getValue());
				}
				scheduler.scheduleJob(job, trigger);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new ClassNotFoundException(e.getMessage());
			} catch (SchedulerException e) {
				e.printStackTrace();
				throw  new SchedulerException(e.getMessage());
			}catch (Exception ex ){
				ex.printStackTrace();
			}
		}
		return entity.getId();
	}

	/**
	 * 删除定时任务
	 * 
	 * @param value
	 * @return String
	 * @throws SchedulerException
	 */
	public String delete(String value) throws SchedulerException {
		JobKey key = null;
		try {
			String[] jobKey = value.split("\\.");
			if (jobKey.length > 0) {
				key = JobKey.jobKey(jobKey[1], jobKey[0]);
				scheduler.deleteJob(key);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return key.toString();
	}

	/**
	 * 更新定时任务
	 * 
	 * @param entity
	 * @return String
	 * @throws SchedulerException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String update(T entity) throws Exception {
		if (null != entity && null != entity.getName()
				&& !"".equals(entity.getName()) && null != entity.getGroup()
				&& !"".equals(entity.getGroup())) {
			Trigger trigger = null;

			trigger = getCronTrigger(entity);

			try {
				Class jobClass = Class.forName(entity.getExecutor());
				JobDetail job = newJob(jobClass)
						.withIdentity(entity.getName(), entity.getGroup())
						.withDescription(entity.getDescription()).build();
				JobDataMap map = job.getJobDataMap();
				Iterator<?> it = entity.getMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<?, ?> ent = (Entry<?, ?>) it.next();
					map.put((String) ent.getKey(), ent.getValue());
				}
				if (null != delete(entity.getId())) {
					scheduler.scheduleJob(job, trigger);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new ClassNotFoundException(e.getMessage());
			} catch (SchedulerException e) {
				e.printStackTrace();
				throw  new SchedulerException(e.getMessage());
			}
		}
		return entity.getId();
	}

	/**
	 *	组装cron的Trigger
	 * @param entity
	 * @return
	 */

	public Trigger getCronTrigger(T entity) {
		Trigger trigger;
		String cronExpression = entity.getCron();
		boolean isValid = CronExpression.isValidExpression(cronExpression);
		if (!isValid) {
			throw new IllegalArgumentException("cronExpression cannot be null");
		}
		trigger = newTrigger()
				.withIdentity(entity.getName(), entity.getGroup())
				.withSchedule(cronSchedule(cronExpression))
				.withDescription(entity.getDescription()).build();
		return trigger;
	}

	/**
	 * 查询单个定时任务
	 * 
	 * @param entity
	 * @return Map
	 * @throws SchedulerException
	 */
	public Object select(T entity) throws SchedulerException {
		String[] keyArray = entity.getId().split("\\.");
		JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(keyArray[1],keyArray[0]));
		String params = JSONArray.fromObject(jobDetail.getJobDataMap()).toString();
		Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(keyArray[1], keyArray[0]));
		Map<String, Object> map = new HashMap<String, Object>();
		if (trigger instanceof SimpleTrigger) {
			map.put("triggerType", QuartzConstants.SIMPLE_TRIGGER);
		} else {
			map.put("triggerType", QuartzConstants.CRON_TRIGGER);
		}
		for (Object key : jobDetail.getJobDataMap().keySet()) {
			if (QuartzConstants.KETTLE_REPO.equals(key.toString())) {
				map.put(QuartzConstants.KETTLE_REPO, jobDetail.getJobDataMap().get(key).toString());
			}
		}
		map.put("params", params);
		map.put("jobDetail", jobDetail);
		map.put("trigger", trigger);
		return map;
	}

	/**
	 * 查询所有定时任务
	 * 
	 * @param entity
	 * @return Map
	 * @throws SchedulerException
	 */
	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	public Object selectByWhere(T entity, Boolean diyBool) throws SchedulerException {
		List<String> groups = scheduler.getJobGroupNames();
		if (diyBool == false){
			groups.remove("DIY_ETLTRANS");
			groups.remove("TABLE_TRANS");
		}
		List<HashMap<String, Object>> jobList = new ArrayList<HashMap<String, Object>>();
		for (String group : groups) {
			if (null != entity.getGroup() && !group.contains(entity.getGroup())) {
				continue;
			}
			Set<JobKey> jobKeys = scheduler.getJobKeys(new GroupMatcher(group,StringOperatorName.EQUALS) {});
			for (JobKey jobKey : jobKeys) {
				if (null != entity.getName() && !jobKey.toString().contains(entity.getName())) {
					continue;
				}
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				HashMap<String, Object> jobInfoMap = new HashMap<String, Object>();
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				jobInfoMap.put("triggers", triggers);
				jobInfoMap.put("jobDetail", jobDetail);
				jobInfoMap.put("params", JSONArray.fromObject(jobDetail.getJobDataMap()).toString());
				jobList.add(jobInfoMap);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobList", jobList);
		map.put("scheduler", scheduler);
		return map;
	}

	/**
	 * 判断是否重名
	 * 
	 * @param entity
	 * @return boolean
	 * @throws SchedulerException
	 */
	public boolean exists(T entity) {
		if (null != quartzScheduleDao.exists(entity)) {
			return true;
		}
		return false;
	}

	/**
	 * 执行单个定时任务
	 * 
	 * @param strVal
	 * @return String
	 * @throws SchedulerException
	 */
	public String execute(String strVal) throws SchedulerException {
		JobKey key = null;
		try {
			String[] jobKey = strVal.split("\\.");
			if (jobKey.length > 0) {
				key = JobKey.jobKey(jobKey[1], jobKey[0]);
				Trigger trigger = newTrigger()
						.withIdentity(jobKey[1] + UUID.randomUUID().toString(), jobKey[0]).withPriority(100).forJob(key)
						.build();
				scheduler.scheduleJob(trigger);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return key.toString();
	}

	/**
	 * 暂停单个定时任务
	 * 
	 * @param strVal
	 * @return String
	 * @throws SchedulerException
	 */
	public String pause(String strVal) throws SchedulerException {
		JobKey key = null;
		try {
			String[] jobKey = strVal.split("\\.");
			if (jobKey.length > 0) {
				key = JobKey.jobKey(jobKey[1], jobKey[0]);
				scheduler.pauseJob(key);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return key.toString();
	}

	/**
	 * 恢复单个定时任务
	 * 
	 * @param strVal
	 * @return String
	 * @throws SchedulerException
	 */
	public String resume(String strVal) throws SchedulerException {
		JobKey key = null;
		try {
			String[] jobKey = strVal.split("\\.");
			if (jobKey.length > 0) {
				key = JobKey.jobKey(jobKey[1], jobKey[0]);
				scheduler.resumeJob(key);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return key.toString();
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setQuartzScheduleDao(QuartzScheduleDao quartzScheduleDao) {
		this.quartzScheduleDao = quartzScheduleDao;
	}

}