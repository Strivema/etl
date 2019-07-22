package com.yinker.etl.trans.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import org.zwork.srdp.rbac.model.base.RbacUser;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>TransConfigChangeLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:00
 * </pre>
 */
public class TransConfigChangeLog extends TransConfigChangeLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 操作人
	 */
    public static final String ALIAS_USER_ID = "user_id";
	/**
	 * 操作表
	 */
    public static final String ALIAS_TABLE_NAME = "table_name";
	/**
	 * 操作日志
	 */
    public static final String ALIAS_OPERATE_LOG = "operate_log";
	/**
	 * 操作类型
	 */
    public static final String ALIAS_OPERATE_TYPE = "operate_type";
	/**
	 * 操作时间
	 */
    public static final String ALIAS_OPERATE_TIME = "operate_time";
	/**
	 * 创建时间
	 */
    public static final String ALIAS_CREATE_TIME = "create_time";
	/**
	 * 最后更新时间
	 */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
	
	/* 操作人 */
    private String userId;
	
	/* 操作表 */
    private String tableName;
	
	/* 操作日志 */
    private String operateLog;
	
	/* 操作类型 */
    private String operateType;
	
	/* 操作时间 */
    private Date operateTime;
	
	/* 创建时间 */
    private Date createTime;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;
    
    private RbacUser rbacUser;
	
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getTableName(){
		return tableName;
	}
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	
	public String getOperateLog(){
		return operateLog;
	}
	public void setOperateLog(String operateLog){
		this.operateLog = operateLog;
	}
	
	public String getOperateType(){
		return operateType;
	}
	public void setOperateType(String operateType){
		this.operateType = operateType;
	}
	
	public Date getOperateTime(){
		return operateTime;
	}
	public void setOperateTime(Date operateTime){
		this.operateTime = operateTime;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	public Date getLastUpdateTime(){
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}
	

	public RbacUser getRbacUser() {
		return rbacUser;
	}
	public void setRbacUser(RbacUser rbacUser) {
		this.rbacUser = rbacUser;
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
		str.append("	tableName=").append(getTableName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateLog=").append(getOperateLog()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateType=").append(getOperateType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateTime=").append(DateUtils.dateTimeToString(getOperateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}