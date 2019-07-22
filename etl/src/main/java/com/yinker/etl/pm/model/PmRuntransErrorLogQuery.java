package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmRuntransErrorLog;

/** 
 * <pre>
 * <b>类描述:</b>PmRuntransErrorLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:56
 * </pre>
 */ 
public class PmRuntransErrorLogQuery extends PmRuntransErrorLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:日志记录时间-开始 */
    private Date logDateStart;
	
	/* 区间查询条件:日志记录时间-结束 */
    private Date logDateEnd;
	
	public Date getLogDateStart(){
        return logDateStart;
    }
	
    public void setLogDateStart(Date logDateStart){
        this.logDateStart = logDateStart;
    }
	
	public Date getLogDateEnd(){
        return logDateEnd;
    }
	
    public void setLogDateEnd(Date logDateEnd){
        this.logDateEnd = logDateEnd;
    }
	
	/**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transId=").append(getTransId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transName=").append(getTransName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDateStart=").append(DateUtils.dateTimeToString(getLogDateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDate=").append(DateUtils.dateTimeToString(getLogDate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDateEnd=").append(DateUtils.dateTimeToString(getLogDateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorDesc=").append(getErrorDesc()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorType=").append(getErrorType()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}