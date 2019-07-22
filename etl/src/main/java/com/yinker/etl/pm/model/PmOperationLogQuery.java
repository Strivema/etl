package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmOperationLog;

/** 
 * <pre>
 * <b>类描述:</b>PmOperationLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:46
 * </pre>
 */ 
public class PmOperationLogQuery extends PmOperationLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;
	
	/* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;
	
    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateDateStart;
	
	/* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateDateEnd;
	
	public Date getCreateTimeStart(){
        return createTimeStart;
    }
	
    public void setCreateTimeStart(Date createTimeStart){
        this.createTimeStart = createTimeStart;
    }
	
	public Date getCreateTimeEnd(){
        return createTimeEnd;
    }
	
    public void setCreateTimeEnd(Date createTimeEnd){
        this.createTimeEnd = createTimeEnd;
    }
	public Date getLastUpdateDateStart(){
        return lastUpdateDateStart;
    }
	
    public void setLastUpdateDateStart(Date lastUpdateDateStart){
        this.lastUpdateDateStart = lastUpdateDateStart;
    }
	
	public Date getLastUpdateDateEnd(){
        return lastUpdateDateEnd;
    }
	
    public void setLastUpdateDateEnd(Date lastUpdateDateEnd){
        this.lastUpdateDateEnd = lastUpdateDateEnd;
    }
	
	/**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	userId=").append(getUserId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	userName=").append(getUserName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operationType=").append(getOperationType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	changeDetail=").append(getChangeDetail()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	changeBefore=").append(getChangeBefore()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	changeAfter=").append(getChangeAfter()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDateStart=").append(DateUtils.dateTimeToString(getLastUpdateDateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDate=").append(DateUtils.dateTimeToString(getLastUpdateDate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDateEnd=").append(DateUtils.dateTimeToString(getLastUpdateDateEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}