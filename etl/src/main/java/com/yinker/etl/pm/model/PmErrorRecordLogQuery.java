package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmErrorRecordLog;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorRecordLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:50
 * </pre>
 */ 
public class PmErrorRecordLogQuery extends PmErrorRecordLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:记录表ID-开始 */
    private Integer errorIdStart;
	
	/* 区间查询条件:记录表ID-结束 */
    private Integer errorIdEnd;
	
    /* 区间查询条件:记录日期-开始 */
    private Date errCreateTimeStart;
	
	/* 区间查询条件:记录日期-结束 */
    private Date errCreateTimeEnd;
	
	public Integer getErrorIdStart(){
        return errorIdStart;
    }
	
    public void setErrorIdStart(Integer errorIdStart){
        this.errorIdStart = errorIdStart;
    }
	
	public Integer getErrorIdEnd(){
        return errorIdEnd;
    }
	
    public void setErrorIdEnd(Integer errorIdEnd){
        this.errorIdEnd = errorIdEnd;
    }
	public Date getErrCreateTimeStart(){
        return errCreateTimeStart;
    }
	
    public void setErrCreateTimeStart(Date errCreateTimeStart){
        this.errCreateTimeStart = errCreateTimeStart;
    }
	
	public Date getErrCreateTimeEnd(){
        return errCreateTimeEnd;
    }
	
    public void setErrCreateTimeEnd(Date errCreateTimeEnd){
        this.errCreateTimeEnd = errCreateTimeEnd;
    }
	
	/**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorIdStart=").append(getErrorIdStart()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorId=").append(getErrorId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorIdEnd=").append(getErrorIdEnd()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errTransName=").append(getErrTransName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errTableName=").append(getErrTableName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errStatus=").append(getErrStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTimeStart=").append(DateUtils.dateTimeToString(getErrCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTime=").append(DateUtils.dateTimeToString(getErrCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTimeEnd=").append(DateUtils.dateTimeToString(getErrCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCount=").append(getErrCount()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errDesc=").append(getErrDesc()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errField=").append(getErrField()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCode=").append(getErrCode()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}