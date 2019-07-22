package com.yinker.etl.trans.exec;

import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.service.trans.RepositoryMetaTrans;
import org.pentaho.di.core.exception.KettleException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 *  <b>Quartz定时器调用:</b>
 *  <br>
 *  用于调用Kettle的Job
 *  <br>
 *  Quartz的任务命名方式需要注意：需要全路径命名，并且路径分隔符用单斜线（/）分隔
 *  <br>
 *  eg: /CRMS/JOB/XXXXXX
 *  @author bowen Cui
 *  @since 2017年12月28日20:47:35
 */
public class KettleExecService extends QuartzJobBean {

    @Resource(name = "repositoryMetaTrans")
    private RepositoryMetaTrans repositoryMetaTrans;

    @Override
    protected void executeInternal (JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();

        String jobkeyName = jobKey.getName();
        String[] spliName = jobkeyName.split("\\/");
        StringBuffer dir = new StringBuffer();
        for(int i = 1; i < spliName.length - 1; i++) {
            dir.append("/" + spliName[i]);
        }
        execute(spliName[spliName.length - 1], dir.toString());
    }

    /**
     * 执行Kettle定时任务
     */
    private void execute (String name, String dir) {
        try {
            repositoryMetaTrans.executeJob(dir, name, TransConstants.JOB_EXECUTE_STATUS_START);
        } catch(KettleException e) {
            e.printStackTrace();
        }
    }

}
