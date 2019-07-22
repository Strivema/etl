package com.yinker.etl.trans.exec;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

/**
 * 執行所有配置的定時任務
 * 
 * @author Lenovo
 */
public class DetailQuartzJobService extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DetailQuartzJobService.class);
	private String targetObject;
	private String targetMethod;

	@Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			LOGGER.info("execute [" + targetObject + "] at once>>>>>>");
			ApplicationContext applicationContext = null;
			try {
				applicationContext = getApplicationContext(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Object otargetObject = applicationContext.getBean(targetObject);
			Method m = null;
			try {
				m = otargetObject.getClass().getMethod(targetMethod, new Class[] {});
				m.invoke(otargetObject, new Object[] {});
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

	private ApplicationContext getApplicationContext(JobExecutionContext context) {
		ApplicationContext appCtx = null;
		try {
			SchedulerContext ctx = context.getScheduler().getContext();
			appCtx = (ApplicationContext)ctx.get("applicationContextKey");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return appCtx;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
}
