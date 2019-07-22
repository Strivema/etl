package com.yinker.etl.trans.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>TransTimebatchLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:03
 * </pre>
 */
public class TransTimebatchLog extends TransTimebatchLogPK {

    private static final long serialVersionUID = 1L;

    /**
     * 批次
     */
    public static final String ALIAS_BATCH_NO = "batch_no";
    /**
     * 开始时间
     */
    public static final String ALIAS_BEGIN_TIME = "begin_time";
    /**
     * 结束时间
     */
    public static final String ALIAS_END_TIME = "end_time";
    /**
     * 共耗时
     */
    public static final String ALIAS_USE_TIME = "use_time";
    /**
     * 共执行表数
     */
    public static final String ALIAS_TABLE_COUNT = "table_count";
    /**
     * 成功数
     */
    public static final String ALIAS_SUCCESS_COUNT = "success_count";
    /**
     * 转换中
     */
    public static final String ALIAS_TRANSING_COUNT = "transing_count";
    /**
     * 异常数
     */
    public static final String ALIAS_EXCEPTION_COUNT = "exception_count";
    /**
     * 其他线程正在执行数
     */
    public static final String ALIAS_OTER_RUNNING_COUNT = "oter_running_count";
    /**
     * 创建时间
     */
    public static final String ALIAS_CREATE_TIME = "create_time";
    /**
     * 最后更新时间
     */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
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

    /* 批次 */
    private String batchNo;

    /* 开始时间 */
    private Date beginTime;

    /* 结束时间 */
    private Date endTime;

    /* 共耗时 */
    private Integer useTime;

    /* 共执行表数 */
    private Integer tableCount;

    /* 成功数 */
    private Integer successCount;

    /* 转换中 */
    private Integer transingCount;

    /* 异常数 */
    private Integer exceptionCount;

    /* 其他线程正在执行数 */
    private Integer oterRunningCount;

    /* 创建时间 */
    private Date createTime;

    /* 最后更新时间 */
    private Date lastUpdateTime;

    /* 跑批类型 */
    private String type;

    /* 定时任务名称 */
    private String jobName;

    /* 表名 */
    private String tableName;

    /* 转换类型 */
    private String transType;


    /* 关联对象-子表信息 */
    private List<TransStatusLog> transStatusLogs;

    public List<TransStatusLog> getTransStatusLogs () {
        return transStatusLogs;
    }

    public void setTransStatusLogs (List<TransStatusLog> transStatusLogs) {
        this.transStatusLogs = transStatusLogs;
    }

    public String getBatchNo () {
        return batchNo;
    }

    public void setBatchNo (String batchNo) {
        this.batchNo = batchNo;
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

    public Integer getTableCount () {
        return tableCount;
    }

    public void setTableCount (Integer tableCount) {
        this.tableCount = tableCount;
    }

    public Integer getSuccessCount () {
        return successCount;
    }

    public void setSuccessCount (Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getTransingCount () {
        return transingCount;
    }

    public void setTransingCount (Integer transingCount) {
        this.transingCount = transingCount;
    }

    public Integer getExceptionCount () {
        return exceptionCount;
    }

    public void setExceptionCount (Integer exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

    public Integer getOterRunningCount () {
        return oterRunningCount;
    }

    public void setOterRunningCount (Integer oterRunningCount) {
        this.oterRunningCount = oterRunningCount;
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

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	batchNo=").append(getBatchNo()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	beginTime=").append(DateUtils.dateTimeToString(getBeginTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTime=").append(DateUtils.dateTimeToString(getEndTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTime=").append(getUseTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	tableCount=").append(getTableCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	successCount=").append(getSuccessCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transingCount=").append(getTransingCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	exceptionCount=").append(getExceptionCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	oterRunningCount=").append(getOterRunningCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}