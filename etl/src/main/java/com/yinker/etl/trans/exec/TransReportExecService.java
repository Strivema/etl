package com.yinker.etl.trans.exec;

import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransStatusLogQuery;
import com.yinker.etl.trans.model.TransTimebatchLogQuery;
import com.yinker.etl.trans.model.base.TransStatusLog;
import com.yinker.etl.trans.model.base.TransTimebatchLog;
import com.yinker.etl.trans.service.TransStatusLogService;
import com.yinker.etl.trans.service.TransTimebatchLogService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.pm.model.base.PmParam;
import org.zwork.srdp.pm.service.PmParamService;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * 用于job的定时调用功能
 */
public class TransReportExecService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransReportExecService.class);

    @Resource(name = "transStatusLogService")
    private TransStatusLogService transStatusLogService;

    @Resource(name = "transTimebatchLogService")
    private TransTimebatchLogService transTimebatchLogService;

    /* 序列键容器 */
    @Resource(name = "etlKeyGeneratorContainerTrans")
    private KeyGeneratorContainer keyGeneratorContainer;

//	@Resource(name = "mailToService")
//	private MailToService mailToService;

    /* 数据字典服务类 */
    @Resource(name = "pmParamService")
    protected PmParamService pmParamService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    private Integer maxTranceTime;

    @Override
    protected void executeInternal (JobExecutionContext context) throws JobExecutionException {

        Long beginTime = System.currentTimeMillis();
        LOGGER.info("--------------  开始执行转换报表跑批任务   --------------");
        //查询最后一条内容
        TransTimebatchLog lastTimeBathchLog = transTimebatchLogService.selectLastOneColumn();
        LOGGER.info("查询跑批记录最后一条记录");

        //根据最后一条的批次号查询本次需要遍历的集合
        List<TransStatusLog> etlTransStatusLogs;
        if (lastTimeBathchLog == null) {

            LOGGER.info("跑批记录没有记录，查询所有的转换记录");
            //如果没有最后一条则认为从来没有跑批过，将之前所有的数据统一执行
            etlTransStatusLogs = transStatusLogService.selectAllByBathch(new TransStatusLogQuery());

        } else {

            LOGGER.info("查询到记录，开始执行大于此次跑批批号{}的记录", lastTimeBathchLog.getBatchNo());
            //根据最后一条的开始时间来查询大于这个开始时间的所有跑批任务
            TransStatusLogQuery stautsQery = new TransStatusLogQuery();
            stautsQery.setBatchNo(lastTimeBathchLog.getBatchNo());
            etlTransStatusLogs = transStatusLogService.selectAllByBathch(stautsQery);
            LOGGER.info("共查询到大于批号{}的数据有{}条", lastTimeBathchLog.getBatchNo(), etlTransStatusLogs == null ? 0 : etlTransStatusLogs.size());

            LOGGER.info("将一天内有过状态为1的正在执行的任务取出重新计算");
            TransTimebatchLogQuery runningTransQuery = new TransTimebatchLogQuery();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            runningTransQuery.setBeginTime(calendar.getTime());
            runningTransQuery.setBatchNo(lastTimeBathchLog.getBatchNo());
            // 取出小于最后抽取编号的前一天内的数据
            List<TransTimebatchLog> runningTrans = transTimebatchLogService.selectRunningTrans(runningTransQuery);
            for(TransTimebatchLog etlTranceTimebatchLog : runningTrans) {
                TransStatusLogQuery statusLogQuery = new TransStatusLogQuery();
                statusLogQuery.setBatchNo(etlTranceTimebatchLog.getBatchNo());
                // 查出对应批号的List
                List<TransStatusLog> runList = transStatusLogService.selectByEntity(statusLogQuery);
                if (runList != null && runList.size() > 0) {
                    // 插入到需要遍历的集合中，并删除抽取状态表
                    etlTransStatusLogs.addAll(runList);
                    transTimebatchLogService.deleteByPK(etlTranceTimebatchLog);
                }
            }
        }

        LOGGER.info("开始遍历查询集合，开始汇总Map");
        Map<String, TransTimebatchLog> syncTrancMap = new HashMap<>();
        TransTimebatchLog log;
        //遍历集合汇总数据
        for(TransStatusLog statusLog : etlTransStatusLogs) {
            if (syncTrancMap.containsKey(statusLog.getBatchNo())) {
                log = syncTrancMap.get(statusLog.getBatchNo());
                log.setLastUpdateTime(new Date());
                if (log.getBeginTime() == null && statusLog.getBeginTime() != null) {
                    log.setBeginTime(statusLog.getBeginTime());
                }
                log.setTableCount(log.getTableCount() + 1);
                if (TransConstants.TRANSLOG_STATUS_1.equals(statusLog.getStatus())) {
                    log.setTransingCount(log.getTransingCount() + 1);
                }
                // 状态为正常结束
                if (TransConstants.TRANSLOG_STATUS_2.equals(statusLog.getStatus())) {
                    if (log.getEndTime() == null) {
                        log.setEndTime(statusLog.getEndTime());
                    } else {
                        log.setEndTime(statusLog.getEndTime() != null && statusLog.getEndTime().after(log.getEndTime()) ? statusLog.getEndTime() : log.getEndTime());
                    }
                    if (log.getBeginTime() != null && log.getEndTime() != null) {
                        log.setUseTime(getUseTime(log.getBeginTime(), log.getEndTime()));
                    }
                    log.setSuccessCount(log.getSuccessCount() + 1);
                }

                // 状态为异常或者其他线程正在执行
                if (TransConstants.TRANSLOG_STATUS_3.equals(statusLog.getStatus())) {
                    log.setExceptionCount(log.getExceptionCount() + 1);
                }
                if (TransConstants.TRANSLOG_STATUS_4.equals(statusLog.getStatus())) {
                    log.setOterRunningCount(log.getOterRunningCount() + 1);
                }
            } else {
                log = new TransTimebatchLog();
                log.setId(keyGeneratorContainer.getNextKey("transTimebatchLogId"));
                log.setBatchNo(statusLog.getBatchNo());
                log.setBeginTime(statusLog.getBeginTime());
                log.setType(statusLog.getType());
                log.setJobName(statusLog.getJobName());
                log.setTableName(statusLog.getTableName());
                log.setTransType(statusLog.getTransType());
                log.setTableCount(1);
                log.setSuccessCount(0);
                log.setTransingCount(0);
                log.setExceptionCount(0);
                log.setOterRunningCount(0);
                log.setUseTime(0);
                if (TransConstants.TRANSLOG_STATUS_1.equals(statusLog.getStatus())) {
                    log.setTransingCount(1);
                }
                if (TransConstants.TRANSLOG_STATUS_2.equals(statusLog.getStatus()) || TransConstants.TRANSLOG_STATUS_5.equals(statusLog.getStatus())) {
                    log.setEndTime(statusLog.getEndTime());
                    log.setUseTime(statusLog.getUseTime());
                    log.setSuccessCount(1);
                }
                if (TransConstants.TRANSLOG_STATUS_3.equals(statusLog.getStatus())) {
                    log.setExceptionCount(1);
                }
                if (TransConstants.TRANSLOG_STATUS_4.equals(statusLog.getStatus())) {
                    log.setOterRunningCount(1);
                }
                log.setCreateTime(new Date());
                log.setLastUpdateTime(new Date());
            }
            syncTrancMap.put(statusLog.getBatchNo(), log);
        }
        LOGGER.info("map遍历结束共插入Map{}条数据", syncTrancMap.size());

        PmParam param = pmParamService.selectByCode("MaxTranceTime");
        this.maxTranceTime = Integer.parseInt(param.getValue());
        StringBuffer sb = new StringBuffer();

        LOGGER.info("开始循环插入跑批数据");
        // 遍历Map集合，存入数据库
        for(Map.Entry<String, TransTimebatchLog> entry : syncTrancMap.entrySet()) {
            LOGGER.info("插入批号为{}的跑批记录", entry.getKey());
            transTimebatchLogService.insert(entry.getValue());

            // 拼接邮件
            if (entry.getValue().getUseTime() > this.maxTranceTime) {
                sb.append(subStringSendMail(entry.getValue()));
            }
        }
        if (sb.length() > 0) {
//			LOGGER.info("开始发送邮件");
//			checkIfSendMail(sb.toString());
        }
        LOGGER.info("循环插入跑批数据结束");

        LOGGER.info("--------------  转换报表跑批任务结束，共耗时{}毫秒   --------------", System.currentTimeMillis() - beginTime);

    }


    /**
     * 判断是否需要发送邮件
     */
    /*public void checkIfSendMail(String content) {
		SysMailConfig mailConfig=mailToService.getEnableConfig("transWranOverTime");
		Map<String, String> parmMap = new HashMap<String, String>();
		parmMap.put("content", content);

		LOGGER.info("准备发送邮件！");
		try {
			// 发送邮件内容
			parmMap.put("from", mailConfig.getAddresser());
			parmMap.put("title", mailConfig.getTitle());
			parmMap.put("body", mailConfig.getBody());
			parmMap.put("group", "transWran");
			parmMap.put("type", "0");  //SYSConstants.SEND_SIMPLE_EMAIL
			LOGGER.info(parmMap.toString());

			String obj = JSON.toJSON(parmMap).toString();
			etlSendService.send("toemailKey", obj);
		} catch (Exception e) {
			LOGGER.info("发送邮件失败！");
			e.printStackTrace();
		}
	}*/

    /**
     * 判断时间是否超时
     *
     * @param useTime
     * @return
     */
    public Boolean isOverTime (int useTime) {
        PmParam param = pmParamService.selectByCode("MaxTranceTime");
        Integer maxTranceTime = Integer.parseInt(param.getValue());
        return useTime > maxTranceTime;
    }

    /**
     * 拼接发送邮件报文
     *
     * @return
     */
    public String subStringSendMail (TransTimebatchLog batchLog) {
        String mail = "跑批：<span style=\"text-decoration:underline\"><b>{0}</b></span>的时间为<span style=\"text-decoration:underline\"><b>{1}</b></span>秒,预定时间<span style=\"text-decoration:underline\"><b>{2}</b></span>秒,本次共转换表<span style=\"text-decoration:underline\"><b>{3}</b></span>条,成功转换<span style=\"text-decoration:underline\"><b>{4}</b></span>条,正在转换<span style=\"text-decoration:underline\"><b>{5}</b></span>条,转换失败<span style=\"text-decoration:underline\"><b>{6}</b></span>条,本次跑批含有其他线程正在执行的任务<span style=\"text-decoration:underline\"><b>{7}</b></span>条；<br>";
        return MessageFormat.format(mail, batchLog.getBatchNo(), batchLog.getUseTime(), this.maxTranceTime, batchLog.getTableCount(), batchLog.getSuccessCount(), batchLog.getTransingCount(), batchLog.getExceptionCount(), batchLog.getOterRunningCount());
    }

    /**
     * 计算两个时间相差毫秒数
     *
     * @param start
     * @param end
     * @return
     */
    public int getUseTime (Date start, Date end) {
        long a = end.getTime();
        long b = start.getTime();
        int c = (int) (a - b);
        return c;
    }
}
