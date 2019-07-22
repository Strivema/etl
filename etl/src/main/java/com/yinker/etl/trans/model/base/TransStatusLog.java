package com.yinker.etl.trans.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>TransStatusLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-14 20:12:11
 * </pre>
 */
public class TransStatusLog extends TransStatusLogPK {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器名称
     */
    public static final String ALIAS_OS_NAME = "os_name";
    /**
     * 抽取线程
     */
    public static final String ALIAS_TRANS_THREAD = "trans_thread";
    /**
     * 转换编号
     */
    public static final String ALIAS_TRANS_ID = "trans_id";
    /**
     * 转换名称
     */
    public static final String ALIAS_TRANS_NAME = "trans_name";
    /**
     * 开始抽取时间
     */
    public static final String ALIAS_BEGIN_TIME = "begin_time";
    /**
     * 结束抽取时间
     */
    public static final String ALIAS_END_TIME = "end_time";
    /**
     * 执行耗时
     */
    public static final String ALIAS_USE_TIME = "use_time";
    /**
     * 抽取状态
     */
    public static final String ALIAS_STATUS = "status";
    /**
     * 执行记录
     */
    public static final String ALIAS_TRANS_LOG = "trans_log";
    /**
     * 创建时间
     */
    public static final String ALIAS_CREATE_TIME = "create_time";
    /**
     * 最后更新时间
     */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
    /**
     * 批次
     */
    public static final String ALIAS_BATCH_NO = "batch_no";

    /**
     * 跑批类型
     */
    public static final String ALIAS_TYPE = "type";
    /**
     * 定时任务名称
     */
    public static final String ALIAS_JOB_NAME = "job_name";
    /**
     * 表名
     */
    public static final String ALIAS_TABLE_NAME = "table_name";
    /**
     * 转换类型
     */
    public static final String ALIAS_TRANS_TYPE = "trans_type";


    /* 服务器名称 */
    private String osName;

    /* 抽取线程 */
    private String transThread;

    /* 转换编号 */
    private String transId;

    /* 转换名称 */
    private String transName;

    /* 开始抽取时间 */
    private Date beginTime;

    /* 结束抽取时间 */
    private Date endTime;

    /* 执行耗时 */
    private Integer useTime;

    /* 抽取状态 */
    private String status;

    /* 执行记录 */
    private String transLog;

    /* 创建时间 */
    private Date createTime;

    /* 最后更新时间 */
    private Date lastUpdateTime;

    /* 批次 */
    private String batchNo;
    /* 跑批类型 */
    private String type;

    /* 定时任务名称 */
    private String jobName;

    /* 表名 */
    private String tableName;

    /* 转换类型 */
    private String transType;

    private String systemSn;

    private String[] targetDBArray;
    private String[] srcDBArray;
    private Double implementationRate;

    /* 关联对象-父表信息 */
    private TransTimebatchLog transTimebatchLogs;

    public TransTimebatchLog getTransTimebatchLogs () {
        return transTimebatchLogs;
    }

    public void setTransTimebatchLogs (TransTimebatchLog transTimebatchLogs) {
        this.transTimebatchLogs = transTimebatchLogs;
    }

    public String getOsName () {
        return osName;
    }

    public void setOsName (String osName) {
        this.osName = osName;
    }

    public String getTransThread () {
        return transThread;
    }

    public void setTransThread (String transThread) {
        this.transThread = transThread;
    }

    public String getTransId () {
        return transId;
    }

    public void setTransId (String transId) {
        this.transId = transId;
    }

    public String getTransName () {
        return transName;
    }

    public void setTransName (String transName) {
        this.transName = transName;
    }

    public Date getBeginTime () {
        return beginTime;
    }

    public void setBeginTime (Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime () {
        return endTime;
    }

    public void setEndTime (Date endTime) {
        this.endTime = endTime;
    }

    public Integer getUseTime () {
        return useTime;
    }

    public void setUseTime (Integer useTime) {
        this.useTime = useTime;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getTransLog () {
        return transLog;
    }

    public void setTransLog (String transLog) {
        this.transLog = transLog;
    }

    public Date getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime () {
        return lastUpdateTime;
    }

    public void setLastUpdateTime (Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getBatchNo () {
        return batchNo;
    }

    public void setBatchNo (String batchNo) {
        this.batchNo = batchNo;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getJobName () {
        return jobName;
    }

    public void setJobName (String jobName) {
        this.jobName = jobName;
    }

    public String getTableName () {
        return tableName;
    }

    public void setTableName (String tableName) {
        this.tableName = tableName;
    }

    public String getTransType () {
        return transType;
    }

    public void setTransType (String transType) {
        this.transType = transType;
    }

    public String getSystemSn () {
        return systemSn;
    }

    public void setSystemSn (String systemSn) {
        this.systemSn = systemSn;
    }

    public String[] getTargetDBArray () {
        return targetDBArray;
    }

    public void setTargetDBArray (String[] targetDBArray) {
        this.targetDBArray = targetDBArray;
    }

    public String[] getSrcDBArray () {
        return srcDBArray;
    }

    public void setSrcDBArray (String[] srcDBArray) {
        this.srcDBArray = srcDBArray;
    }

    public Double getImplementationRate () {
        return implementationRate;
    }

    public void setImplementationRate (Double implementationRate) {
        this.implementationRate = implementationRate;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	osName=").append(getOsName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transThread=").append(getTransThread()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transId=").append(getTransId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transName=").append(getTransName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	beginTime=").append(DateUtils.dateTimeToString(getBeginTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTime=").append(DateUtils.dateTimeToString(getEndTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTime=").append(getUseTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transLog=").append(getTransLog()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	batchNo=").append(getBatchNo()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}