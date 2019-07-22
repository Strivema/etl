package com.yinker.etl.trans.model;

import com.yinker.etl.trans.model.base.TransStatusLog;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>TransStatusLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-14 20:12:11
 * </pre>
 */
public class TransStatusLogQuery extends TransStatusLog {

    private static final long serialVersionUID = 1L;

    /** 区间查询条件:开始抽取时间-开始 */
    private Date beginTimeStart;

    /** 区间查询条件:开始抽取时间-结束 */
    private Date beginTimeEnd;

    /** 区间查询条件:结束抽取时间-开始 */
    private Date endTimeStart;

    /** 区间查询条件:结束抽取时间-结束 */
    private Date endTimeEnd;

    /** 区间查询条件:执行耗时-开始 */
    private Integer useTimeStart;

    /** 区间查询条件:执行耗时-结束 */
    private Integer useTimeEnd;

    /** 区间查询条件:创建时间-开始 */
    private Date createTimeStart;

    /** 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;

    /** 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateTimeStart;

    /** 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateTimeEnd;

    private Integer selectCount;

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

    public Integer getSelectCount () {
        return selectCount;
    }

    public void setSelectCount (Integer selectCount) {
        this.selectCount = selectCount;
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
        str.append("	beginTimeStart=").append(DateUtils.dateTimeToString(getBeginTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	beginTime=").append(DateUtils.dateTimeToString(getBeginTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	beginTimeEnd=").append(DateUtils.dateTimeToString(getBeginTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTimeStart=").append(DateUtils.dateTimeToString(getEndTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTime=").append(DateUtils.dateTimeToString(getEndTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	endTimeEnd=").append(DateUtils.dateTimeToString(getEndTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTimeStart=").append(getUseTimeStart()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTime=").append(getUseTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	useTimeEnd=").append(getUseTimeEnd()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transLog=").append(getTransLog()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTimeStart=").append(DateUtils.dateTimeToString(getLastUpdateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTimeEnd=").append(DateUtils.dateTimeToString(getLastUpdateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	batchNo=").append(getBatchNo()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}