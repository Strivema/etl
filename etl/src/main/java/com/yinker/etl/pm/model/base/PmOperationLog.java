package com.yinker.etl.pm.model.base;

import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

/** 
 * <pre>
 * <b>类描述:</b>PmOperationLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:46
 * </pre>
 */
public class PmOperationLog extends PmOperationLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 操作人
	 */
    public static final String ALIAS_USER_ID = "user_id";
	/**
	 * 操作人姓名
	 */
    public static final String ALIAS_USER_NAME = "user_name";
	/**
	 * 操作类型
	 */
    public static final String ALIAS_OPERATION_TYPE = "operation_type";
	/**
	 * 变更内容
	 */
    public static final String ALIAS_CHANGE_DETAIL = "change_detail";
	/**
	 * 操作前内容
	 */
    public static final String ALIAS_CHANGE_BEFORE = "change_before";
	/**
	 * 操作后内容
	 */
    public static final String ALIAS_CHANGE_AFTER = "change_after";
	/**
	 * 创建时间
	 */
    public static final String ALIAS_CREATE_TIME = "create_time";
	/**
	 * 最后更新时间
	 */
    public static final String ALIAS_LAST_UPDATE_DATE = "last_update_date";
	
	/* 操作人 */
    private String userId;
	
	/* 操作人姓名 */
    private String userName;
	
	/* 操作类型 */
    private String operationType;
	
	/* 变更内容 */
    private String changeDetail;
	
	/* 操作前内容 */
    private String changeBefore;
	
	/* 操作后内容 */
    private String changeAfter;
	
	/* 创建时间 */
    private Date createTime;
	
	/* 最后更新时间 */
    private Date lastUpdateDate;
	
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getOperationType(){
		return operationType;
	}
	public void setOperationType(String operationType){
		this.operationType = operationType;
	}
	
	public String getChangeDetail(){
		return changeDetail;
	}
	public void setChangeDetail(String changeDetail){
		this.changeDetail = changeDetail;
	}
	
	public String getChangeBefore(){
		return changeBefore;
	}
	public void setChangeBefore(String changeBefore){
		this.changeBefore = changeBefore;
	}
	
	public String getChangeAfter(){
		return changeAfter;
	}
	public void setChangeAfter(String changeAfter){
		this.changeAfter = changeAfter;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	public Date getLastUpdateDate(){
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate){
		this.lastUpdateDate = lastUpdateDate;
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
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDate=").append(DateUtils.dateTimeToString(getLastUpdateDate())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}