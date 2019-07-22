package com.yinker.etl.trans.service.trans;

import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.util.KettleDataSorceInit;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Kettle资源库，适用于调用kettle资源库调用
 *
 * @author Lenovo
 */
public class RepositoryMetaTrans {

    private static final Map<String, Object> threadMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryMetaTrans.class);


    /**
     * 执行资源库转换
     *
     * @param transDir  资源目录
     * @param transName 转换名称
     */
    public boolean executeTrans (String transDir, String transName) {

        try {
            KettleDatabaseRepository rep = KettleDataSorceInit.initDatabaseMeta();
            // 根据变量查找到模型所在的目录对象,此步骤很重要
            RepositoryDirectoryInterface directory = rep.findDirectory(transDir);

            // 创建ktr元对象,参数1为资源库中转换/job的名称
            TransMeta transformationMeta = rep.loadTransformation(transName, directory, null, true, null);
            Trans trans = new Trans(transformationMeta);
            trans.execute(null);
            trans.waitUntilFinished();
        } catch(KettleException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 执行作业
     *
     * @param jobDir  资源目录
     * @param jobName 转换名称
     * @throws KettleException
     */
    public boolean executeJob (String jobDir, String jobName, String status) throws KettleException {
        LOGGER.info("开始执行作业：" + jobDir + jobName);
        KettleDatabaseRepository rep = KettleDataSorceInit.initDatabaseMeta();
        // 根据变量查找到模型所在的目录对象,此步骤很重要
        RepositoryDirectoryInterface directory = rep.findDirectory(jobDir);

        // 执行指定作业  
        Job job;
        if (TransConstants.JOB_EXECUTE_STATUS_START.equals(status)) {
            // 创建ktr元对象,参数1为资源库中转换/job的名称
            JobMeta jobMeta = rep.loadJob(jobName, directory, null, null);
            // 执行指定作业
            job = new Job(rep, jobMeta);
            job.start();
            threadMap.put(jobName, job);
        } else if (TransConstants.JOB_EXECUTE_STATUS_STOP.equals(status)) {
            if (threadMap.get(jobName) != null) {
                job = (Job) threadMap.get(jobName);
                job.stopAll();
                threadMap.remove(jobName);
            } else {
                return false;
            }
        }

        LOGGER.info("作业执行结束");
        return true;
    }

    /**
     * 启动作业 - 只支持异常终止断线重连操作
     *
     * @return
     * @throws KettleException
     */
    public void startJob (String jobDir, String jobName) throws KettleException {
        KettleDatabaseRepository rep = KettleDataSorceInit.initDatabaseMeta();
        // 根据变量查找到模型所在的目录对象,此步骤很重要
        RepositoryDirectoryInterface directory = rep.findDirectory(jobDir);

        // 创建ktr元对象,参数1为资源库中转换/job的名称
        JobMeta jobMeta = ((Repository) rep).loadJob(jobName, directory, null, null);
        // 执行指定作业  
        Job job = new Job(rep, jobMeta);
        job.start();
        threadMap.put(jobName, job);
    }

    /**
     * 停止作业 - 只支持异常终止断线重连操作
     *
     * @return
     * @throws KettleException
     */
    public void stopJob (String jobName) throws KettleException {

        Job job = (Job) threadMap.get(jobName);
        job.stopAll();
        threadMap.remove(jobName);
    }

    /**
     * 检测作业状态
     *
     * @return
     */
    public String getJobStatus (String jobName, String status) {
        if (TransConstants.JOB_EXECUTE_STATUS_START.equals(status)) {
            if (threadMap.get(jobName) == null) {
                return TransConstants.JOB_BREAK_STATUS_2;
            }
        } else if (TransConstants.JOB_EXECUTE_STATUS_STOP.equals(status)) {
            if (threadMap.get(jobName) != null) {
                return TransConstants.JOB_BREAK_STATUS_1;
            }
        }

        return TransConstants.JOB_BREAK_STATUS_0;
    }

}
