package com.yinker.etl.trans.model;

import com.yinker.etl.trans.model.base.TransTimebatchLog;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>TransTimebatchLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:03
 * </pre>
 */
public class TransTimebatchLogQuery extends TransTimebatchLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:开始时间-开始 */
    private Date beginTimeStart;

    /* 区间查询条件:开始时间-结束 */
    private Date beginTimeEnd;

    /* 区间查询条件:结束时间-开始 */
    private Date endTimeStart;

    /* 区间查询条件:结束时间-结束 */
    private Date endTimeEnd;

    /* 区间查询条件:共耗时-开始 */
    private Integer useTimeStart;

    /* 区间查询条件:共耗时-结束 */
    private Integer useTimeEnd;

    /* 区间查询条件:共执行表数-开始 */
    private Integer tableCountStart;

    /* 区间查询条件:共执行表数-结束 */
    private Integer tableCountEnd;

    /* 区间查询条件:成功数-开始 */
    private Integer successCountStart;

    /* 区间查询条件:成功数-结束 */
    private Integer successCountEnd;

    /* 区间查询条件:转换中-开始 */
    private Integer transingCountStart;

    /* 区间查询条件:转换中-结束 */
    private Integer transingCountEnd;

    /* 区间查询条件:异常数-开始 */
    private Integer exceptionCountStart;

    /* 区间查询条件:异常数-结束 */
    private Integer exceptionCountEnd;

    /* 区间查询条件:其他线程正在执行数-开始 */
    private Integer oterRunningCountStart;

    /* 区间查询条件:其他线程正在执行数-结束 */
    private Integer oterRunningCountEnd;

    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;

    /* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;

    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateTimeStart;

    /* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateTimeEnd;

    /* 用于在查询条件加GroupBy字段 */
    private String groupByCloume;

    public Date getBeginTimeStart () {
        return beginTimeStart;
    }

    public void setBeginTimeStart (Date beginTimeStart) {
        this.beginTimeStart = beginTimeStart;
    }

    public Date getBeginTimeEnd () {
        return beginTimeEnd;
    }

    public void setBeginTimeEnd (Date beginTimeEnd) {
        this.beginTimeEnd = beginTimeEnd;
    }

    public Date getEndTimeStart () {
        return endTimeStart;
    }

    public void setEndTimeStart (Date endTimeStart) {
        this.endTimeStart = endTimeStart;
    }

    public Date getEndTimeEnd () {
        return endTimeEnd;
    }

    public void setEndTimeEnd (Date endTimeEnd) {
        this.endTimeEnd = endTimeEnd;
    }

    public Integer getUseTimeStart () {
        return useTimeStart;
    }

    public void setUseTimeStart (Integer useTimeStart) {
        this.useTimeStart = useTimeStart;
    }

    public Integer getUseTimeEnd () {
        return useTimeEnd;
    }

    public void setUseTimeEnd (Integer useTimeEnd) {
        this.useTimeEnd = useTimeEnd;
    }

    public Integer getTableCountStart () {
        return tableCountStart;
    }

    public void setTableCountStart (Integer tableCountStart) {
        this.tableCountStart = tableCountStart;
    }

    public Integer getTableCountEnd () {
        return tableCountEnd;
    }

    public void setTableCountEnd (Integer tableCountEnd) {
        this.tableCountEnd = tableCountEnd;
    }

    public Integer getSuccessCountStart () {
        return successCountStart;
    }

    public void setSuccessCountStart (Integer successCountStart) {
        this.successCountStart = successCountStart;
    }

    public Integer getSuccessCountEnd () {
        return successCountEnd;
    }

    public void setSuccessCountEnd (Integer successCountEnd) {
        this.successCountEnd = successCountEnd;
    }

    public Integer getTransingCountStart () {
        return transingCountStart;
    }

    public void setTransingCountStart (Integer transingCountStart) {
        this.transingCountStart = transingCountStart;
    }

    public Integer getTransingCountEnd () {
        return transingCountEnd;
    }

    public void setTransingCountEnd (Integer transingCountEnd) {
        this.transingCountEnd = transingCountEnd;
    }

    public Integer getExceptionCountStart () {
        return exceptionCountStart;
    }

    public void setExceptionCountStart (Integer exceptionCountStart) {
        this.exceptionCountStart = exceptionCountStart;
    }

    public Integer getExceptionCountEnd () {
        return exceptionCountEnd;
    }

    public void setExceptionCountEnd (Integer exceptionCountEnd) {
        this.exceptionCountEnd = exceptionCountEnd;
    }

    public Integer getOterRunningCountStart () {
        return oterRunningCountStart;
    }

    public void setOterRunningCountStart (Integer oterRunningCountStart) {
        this.oterRunningCountStart = oterRunningCountStart;
    }

    public Integer getOterRunningCountEnd () {
        return oterRunningCountEnd;
    }

    public void setOterRunningCountEnd (Integer oterRunningCountEnd) {
        this.oterRunningCountEnd = oterRunningCountEnd;
    }

    public Date getCreateTimeStart () {
        return createTimeStart;
    }

    public void setCreateTimeStart (Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public Date getCreateTimeEnd () {
        return createTimeEnd;
    }

    public void setCreateTimeEnd (Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getLastUpdateTimeStart () {
        return lastUpdateTimeStart;
    }

    public void setLastUpdateTimeStart (Date lastUpdateTimeStart) {
        this.lastUpdateTimeStart = lastUpdateTimeStart;
    }

    public Date getLastUpdateTimeEnd () {
        return lastUpdateTimeEnd;
    }

    public void setLastUpdateTimeEnd (Date lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public String getGroupByCloume () {
        return groupByCloume;
    }

    public void setGroupByCloume (String groupByCloume) {
        this.groupByCloume = groupByCloume;
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
        str.append("	beginTimeStart=").append(DateUtils.dateTimeToString(getBeginTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	beginTime=").append(DateUtils.dateTimeToString(getBeginTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	beginTimeEnd=").append(DateUtils.dateTimeToString(getBeginTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTimeStart=").append(DateUtils.dateTimeToString(getEndTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTime=").append(DateUtils.dateTimeToString(getEndTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTimeEnd=").append(DateUtils.dateTimeToString(getEndTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTimeStart=").append(getUseTimeStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTime=").append(getUseTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTimeEnd=").append(getUseTimeEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	tableCountStart=").append(getTableCountStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	tableCount=").append(getTableCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	tableCountEnd=").append(getTableCountEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	successCountStart=").append(getSuccessCountStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	successCount=").append(getSuccessCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	successCountEnd=").append(getSuccessCountEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transingCountStart=").append(getTransingCountStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transingCount=").append(getTransingCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transingCountEnd=").append(getTransingCountEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	exceptionCountStart=").append(getExceptionCountStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	exceptionCount=").append(getExceptionCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	exceptionCountEnd=").append(getExceptionCountEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	oterRunningCountStart=").append(getOterRunningCountStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	oterRunningCount=").append(getOterRunningCount()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	oterRunningCountEnd=").append(getOterRunningCountEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTimeStart=").append(DateUtils.dateTimeToString(getLastUpdateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTimeEnd=").append(DateUtils.dateTimeToString(getLastUpdateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}