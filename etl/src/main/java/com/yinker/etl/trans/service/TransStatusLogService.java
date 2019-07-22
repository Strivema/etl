package com.yinker.etl.trans.service;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.model.base.PmMailTemplate;
import com.yinker.etl.pm.service.PmMailTemplateService;
import com.yinker.etl.pm.service.PmSystemInfoLogService;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.sys.service.MailSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.dao.TransStatusLogDao;
import com.yinker.etl.trans.model.EchartData;
import com.yinker.etl.trans.model.TransStatusLogQuery;
import com.yinker.etl.trans.model.TransWarnConfigQuery;
import com.yinker.etl.trans.model.base.*;
import com.yinker.etl.util.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseService;
import org.zwork.srdp.rbac.model.base.RbacUser;
import org.zwork.srdp.rbac.model.base.RbacUserPK;
import org.zwork.srdp.rbac.service.RbacUserService;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * <pre>
 * <b>类描述:</b>TransStatusLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:02
 * </pre>
 */
public class TransStatusLogService extends BaseService<TransStatusLogPK, TransStatusLog, TransStatusLogDao> implements org.zwork.framework.base.BaseService<TransStatusLogPK, TransStatusLog> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransStatusLogService.class);

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Resource(name = "pmSystemInfoLogService")
    private PmSystemInfoLogService pmSystemInfoLogService;

    @Resource(name = "transWarnConfigService")
    private TransWarnConfigService transWarnConfigService;

    @Resource(name = "mailSendService")
    private MailSendService mailSendService;

    @Resource(name = "pmMailTemplateService")
    private PmMailTemplateService pmMailTemplateService;

    @Resource(name = "rbacUserService")
    private RbacUserService rbacUserService;

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    /**
     * 判断N小时之内是否有正在运行的转换
     *
     * @param transId 转换配置ID
     * @return true: 有    false：无
     */
    public List<TransStatusLog> isTransRunning (String transId) {
        TransStatusLogQuery query = new TransStatusLogQuery();
        query.setTransId(transId);
        query.setStatus(TransConstants.TRANSLOG_STATUS_1);
        //一小时之内的运行中的
        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -60*3);
        query.setBeginTimeStart(calendar.getTime());*/
        query.setSortColumns(" begin_time desc");
        List<TransStatusLog> statusLogs = selectByEntity(query);

        if (statusLogs != null && statusLogs.size() > 0) {
            LOGGER.info("查询正在执行的数量为 - > " + statusLogs.size());
            String[] systemSns = pmSystemInfoLogService.getSystemSNs();
            LOGGER.info("获取当前系统识别码 - > " + ArrayUtils.toString(systemSns));
            TransStatusLog statusLog = statusLogs.get(0);
            if (Arrays.binarySearch(systemSns, StringUtils.isNotEmpty(statusLog.getSystemSn()) ? statusLog.getSystemSn() : "") >= 0) {
                LOGGER.info(statusLog.getTransName() + "正在执行。服务器为" + statusLog.getOsName() + "，批次为" + statusLog.getBatchNo() + "，机器码为" + statusLog.getSystemSn());
                return statusLogs;
            }
        }
        return null;
    }

    /**
     * 创建执行日志 -- 正在执行中
     */
    public void insertRunningTrance (String id, String name, String batchNo, String jobName, String type, String targetTbCode, String fullTransType, String log) {
        TransStatusLog statusLog = new TransStatusLog();
        statusLog.setId(UUID.randomUUID().toString());
        String systemSn = null;
        try {
            systemSn = pmSystemInfoLogService.getSystemSN();
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            statusLog.setOsName(InetAddress.getLocalHost().getHostAddress());
        } catch(UnknownHostException e) {
            statusLog.setOsName("0.0.0.0");
            e.printStackTrace();
            LOGGER.info("获取系统IP异常！");
        }
        statusLog.setTransThread(Thread.currentThread().getName());
        statusLog.setTransId(id);
        statusLog.setTransName(name);
        statusLog.setCreateTime(new Date());
        statusLog.setLastUpdateTime(new Date());
        statusLog.setStatus(TransConstants.TRANSLOG_STATUS_4);
        statusLog.setTransLog(log);
        statusLog.setBatchNo(batchNo);
        statusLog.setJobName(jobName);
        statusLog.setType(type);
        statusLog.setTableName(targetTbCode);
        statusLog.setTransType(fullTransType);
        statusLog.setSystemSn(systemSn);
        this.insert(statusLog);
    }

    /**
     * 创建执行日志 -- 执行开始
     */
    public String insertBeginTrance (String id, String name, String batchNo, String jobName, String type, String targetTbCode, String fullTransType) {
        TransStatusLog statusLog = new TransStatusLog();
        statusLog.setId(UUID.randomUUID().toString());
        String systemSn = null;
        try {
            systemSn = pmSystemInfoLogService.getSystemSN();
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            statusLog.setOsName(InetAddress.getLocalHost().getHostAddress());
        } catch(UnknownHostException e) {
            statusLog.setOsName("0.0.0.0");
            e.printStackTrace();
            LOGGER.info("获取系统IP异常！");
        }
        statusLog.setTransThread(Thread.currentThread().getName());
        statusLog.setTransId(id);
        statusLog.setTransName(name);
        statusLog.setBeginTime(new Date());
        statusLog.setCreateTime(new Date());
        statusLog.setLastUpdateTime(new Date());
        statusLog.setStatus(TransConstants.TRANSLOG_STATUS_1);
        statusLog.setTransLog("开始执行转换");
        statusLog.setBatchNo(batchNo);
        statusLog.setJobName(jobName);
        statusLog.setType(type);
        statusLog.setTableName(targetTbCode);
        statusLog.setTransType(fullTransType);
        statusLog.setSystemSn(systemSn);
        this.insert(statusLog);
        return statusLog.getId();
    }

    /**
     * 创建执行日志 -- 执行结束
     */
    public void updateEndTrance (String id, Integer useTime) {
        TransStatusLogPK pk = new TransStatusLogPK();
        pk.setId(id);
        TransStatusLog statusLog = this.selectByPK(pk);
        statusLog.setEndTime(new Date());
        statusLog.setLastUpdateTime(new Date());
        statusLog.setStatus(TransConstants.TRANSLOG_STATUS_2);
        statusLog.setTransLog("转换结束");
        statusLog.setUseTime(useTime);
        this.updateByPK(statusLog);
        // 判断是否发送邮件
        checkIfSendMail(statusLog);
    }

    /**
     * 创建执行日志 -- 执行异常
     */
    public void updateExceptionTrance (String id, String exceptionMsg) {
        TransStatusLogPK pk = new TransStatusLogPK();
        pk.setId(id);
        TransStatusLog statusLog = this.selectByPK(pk);
        statusLog.setEndTime(new Date());
        statusLog.setLastUpdateTime(new Date());
        statusLog.setStatus(TransConstants.TRANSLOG_STATUS_3);
        statusLog.setTransLog("转换异常：" + exceptionMsg);
        statusLog.setUseTime(getUseTime(statusLog.getBeginTime(), statusLog.getEndTime()));
        this.updateByPK(statusLog);
        // 判断是否发送邮件
/*
        checkIfSendMail(statusLog);
*/
    }

    public void setKeyGeneratorContainer (KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    /**
     * 计算两个时间相差秒数
     *
     * @param start
     * @param end
     * @return
     */
    public int getUseTime (Date start, Date end) {
        long a = end.getTime();
        long b = start.getTime();
        int c = (int) (a - b);
        return c < 0 ? 0 : c;
    }

    /**
     * 判断是否需要发送邮件
     */
    public void checkIfSendMail (TransStatusLog statusLog) {
        LOGGER.debug("校验转换编号为{}的转换是否需要发送邮件", statusLog.getTransId());
        TransWarnConfigQuery warnQuery = new TransWarnConfigQuery();
        warnQuery.setTransId(statusLog.getTransId());
        warnQuery.setStatus(TransConstants.WARN_CONFIG_ENABLE_1);
        List<TransWarnConfig> configs = transWarnConfigService.selectByEntity(warnQuery);
        if (configs != null && configs.size() > 0) {
            TransWarnConfig warn = configs.get(0);
            LOGGER.info("本条转换用时" + statusLog.getUseTime() + "ms,安全时间为" + warn.getSafeTime() + "ms");
            if (statusLog.getUseTime() > warn.getSafeTime()) {
                RbacUser user = getUserInfoByTransId(statusLog.getTransId());
                if (user != null && StringUtils.isNotEmpty(user.getEmail())) {
                    PmMailTemplate template = pmMailTemplateService.getWarnConfigByCode(PMConstants.MAIL_TEMPLEATE_TRANS_WARN);
                    Map<String, String> parameMap = new HashMap<>();
                    parameMap.put("name", user.getUserName());
                    parameMap.put("transName", statusLog.getTransName());
                    parameMap.put("useTime", statusLog.getUseTime() + "");
                    parameMap.put("safeTime", warn.getSafeTime() + "");
                    String body = StringUtil.parseString(template.getBody(), parameMap);
                    LOGGER.info("开始向 邮件消息队列发送 消息");
                    mailSendService.sendMailToMQ(template.getServerId(), template.getTitle(), template.getAddresser(), user.getEmail(), user.getUserName(), body, false);
                    LOGGER.info("发送MQ结束");
                } else {
                    LOGGER.error("用户[" + user.getUserName() + "]邮箱没有配置");
                }
            }
        }
    }

    /**
     * 根据TransId获取用户信息
     *
     * @param transId
     * @return
     */
    private RbacUser getUserInfoByTransId (String transId) {
        String userId;
        if (transId.indexOf("STS") >= 0) {
            TransTableStructureInfoPK transInfoPK = new TransTableStructureInfoPK();
            transInfoPK.setId(transId);
            TransTableStructureInfo transInfo = transTableStructureInfoService.selectByPK(transInfoPK);
            userId = transInfo.getOwner();
        } else {
            TransInfoPK transInfoPK = new TransInfoPK();
            transInfoPK.setId(transId);
            TransInfo transInfo = transInfoService.selectByPK(transInfoPK);
            userId = transInfo.getOwner();
        }

        RbacUserPK userPK = new RbacUserPK();
        userPK.setId(userId);
        RbacUser user = rbacUserService.selectByPK(userPK);
        return user;
    }

    public List<TransStatusLog> selectAllByBathch (TransStatusLogQuery etlTransStatusLogQuery) {
        return modelDao.selectAllByBathch(etlTransStatusLogQuery);
    }

    /**
     * 获取指定时间范围内转换时间在最大的集合
     * @param queryEntity
     * @return
     */
    public List<EchartData> getUseTimeMaxTransByQuery (TransStatusLogQuery queryEntity) {
        // 获取数据集合
        List<TransStatusLog> statusLogs = modelDao.selectUseTimeMaxTrans(queryEntity);
        return getEchartDataBytransStatusLogs(statusLogs);
    }

    /**
     * 查询指定时间范围内平均最大几条跑批信息
     * @param queryEntity
     * @return
     */
    public List<EchartData> getUseTimeAvgTransByQuery (TransStatusLogQuery queryEntity) {
        List<TransStatusLog> statusLogs = modelDao.selectUseTimeAvgTrans(queryEntity);
        return getEchartDataBytransStatusLogs(statusLogs);
    }

    /**
     * 查询指定范围内状态转换率信息
     * @param queryEntity
     * @return
     */
    public List<EchartData> selectImplementationRate (TransStatusLogQuery queryEntity) {
        List<TransStatusLog> statusLogs = modelDao.selectImplementationRate(queryEntity);
        List<EchartData> dataList = new ArrayList<>();
        // 遍历集合，加载到List中
        for(TransStatusLog statusLog : statusLogs) {
            EchartData data = new EchartData();
            data.setValue(String.valueOf(statusLog.getImplementationRate()));
            data.setName(statusLog.getTransName());
            dataList.add(data);
        }
        return dataList;
    }

    private List<EchartData> getEchartDataBytransStatusLogs(List<TransStatusLog> statusLogs){
        List<EchartData> dataList = new ArrayList<>();
        // 遍历集合，加载到List中
        for(TransStatusLog statusLog : statusLogs) {
            EchartData data = new EchartData();
            data.setValue(String.valueOf(statusLog.getUseTime()));
            data.setName(statusLog.getTransName());
            dataList.add(data);
        }
        return dataList;
    }
}
