package com.yinker.etl.pm.model.base;

import org.springframework.data.mongodb.core.mapping.Field;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorRecordLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:50
 * </pre>
 */
public class PmErrorRecordLog extends PmErrorRecordLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 错误记录ID
	 */
    public static final String ALIAS_ID = "id";
	/**
	 * 转换名称
	 */
    public static final String ALIAS_ERR_TRANS_NAME = "err_trans_name";
	/**
	 * 错误记录表名
	 */
    public static final String ALIAS_ERR_TABLE_NAME = "err_table_name";
	/**
	 * 状态
	 */
    public static final String ALIAS_ERR_STATUS = "err_status";
	/**
	 * 记录日期
	 */
    public static final String ALIAS_ERR_CREATE_TIME = "err_create_time";
	/**
	 * 错误数
	 */
    public static final String ALIAS_ERR_COUNT = "err_count";
	/**
	 * 错误描述
	 */
    public static final String ALIAS_ERR_DESC = "err_desc";
	/**
	 * 错误列
	 */
    public static final String ALIAS_ERR_FIELD = "err_field";
	/**
	 * 错我编码
	 */
    public static final String ALIAS_ERR_CODE = "err_code";
	
	/* 错误记录ID */
	@Field("id")
    private String id;
	
	/* 转换名称 */
    private String errTransName;
	
	/* 错误记录表名 */
    private String errTableName;
	
	/* 状态 */
    private String errStatus;
	
	/* 记录日期 */
    private Date errCreateTime;
	
	/* 错误数 */
    private String errCount;
	
	/* 错误描述 */
    private String errDesc;
	
	/* 错误列 */
    private String errField;
	
	/* 错误编码 */
    private String errCode;

    private String transId;

    private String remark;
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public String getErrTransName(){
		return errTransName;
	}
	public void setErrTransName(String errTransName){
		this.errTransName = errTransName;
	}
	
	public String getErrTableName(){
		return errTableName;
	}
	public void setErrTableName(String errTableName){
		this.errTableName = errTableName;
	}
	
	public String getErrStatus(){
		return errStatus;
	}
	public void setErrStatus(String errStatus){
		this.errStatus = errStatus;
	}
	
	public Date getErrCreateTime(){
		return errCreateTime;
	}
	public void setErrCreateTime(Date errCreateTime){
		this.errCreateTime = errCreateTime;
	}
	
	public String getErrCount(){
		return errCount;
	}
	public void setErrCount(String errCount){
		this.errCount = errCount;
	}
	
	public String getErrDesc(){
		return errDesc;
	}
	public void setErrDesc(String errDesc){
		this.errDesc = errDesc;
	}
	
	public String getErrField(){
		return errField;
	}
	public void setErrField(String errField){
		this.errField = errField;
	}
	
	public String getErrCode(){
		return errCode;
	}
	public void setErrCode(String errCode){
		this.errCode = errCode;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorId=").append(getErrorId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errTransName=").append(getErrTransName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errTableName=").append(getErrTableName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errStatus=").append(getErrStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCreateTime=").append(DateUtils.dateTimeToString(getErrCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCount=").append(getErrCount()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errDesc=").append(getErrDesc()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errField=").append(getErrField()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errCode=").append(getErrCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transId=").append(getTransId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}