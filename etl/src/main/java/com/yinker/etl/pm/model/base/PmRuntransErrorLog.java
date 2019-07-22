package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmRuntransErrorLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:56
 * </pre>
 */
public class PmRuntransErrorLog extends PmRuntransErrorLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 主键
	 */
    public static final String ALIAS_ID = "id";
	/**
	 * 转换id
	 */
    public static final String ALIAS_TRANS_ID = "trans_id";
	/**
	 * 转换名称
	 */
    public static final String ALIAS_TRANS_NAME = "trans_name";
	/**
	 * 日志记录时间
	 */
    public static final String ALIAS_LOG_DATE = "log_date";
	/**
	 * 日志描述
	 */
    public static final String ALIAS_ERROR_DESC = "error_desc";
	/**
	 * 错误类型
	 */
    public static final String ALIAS_ERROR_TYPE = "error_type";
	
	/* 主键 */
    private String id;
	
	/* 转换id */
    private String transId;
	
	/* 转换名称 */
    private String transName;
	
	/* 日志记录时间 */
    private Date logDate;
	
	/* 日志描述 */
    private String errorDesc;
	
	/* 错误类型 */
    private String errorType;
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public String getTransId(){
		return transId;
	}
	public void setTransId(String transId){
		this.transId = transId;
	}
	
	public String getTransName(){
		return transName;
	}
	public void setTransName(String transName){
		this.transName = transName;
	}
	
	public Date getLogDate(){
		return logDate;
	}
	public void setLogDate(Date logDate){
		this.logDate = logDate;
	}
	
	public String getErrorDesc(){
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc){
		this.errorDesc = errorDesc;
	}
	
	public String getErrorType(){
		return errorType;
	}
	public void setErrorType(String errorType){
		this.errorType = errorType;
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
		str.append("	logDate=").append(DateUtils.dateTimeToString(getLogDate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorDesc=").append(getErrorDesc()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorType=").append(getErrorType()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}