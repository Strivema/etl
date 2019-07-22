package com.yinker.etl.pm.model;
import com.yinker.etl.pm.model.base.PmErrorTableStructureLog;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorTableStructureLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-13 11:03:59
 * </pre>
 */ 
public class PmErrorTableStructureLogQuery extends PmErrorTableStructureLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:err_create_time-开始 */
    private Date errCreateTimeStart;
	
	/* 区间查询条件:err_create_time-结束 */
    private Date errCreateTimeEnd;
	
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
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errTableName=").append(getErrTableName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errColumnName=").append(getErrColumnName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errStatus=").append(getErrStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTimeStart=").append(DateUtils.dateTimeToString(getErrCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTime=").append(DateUtils.dateTimeToString(getErrCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTimeEnd=").append(DateUtils.dateTimeToString(getErrCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errDesc=").append(getErrDesc()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}