package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmMailSendLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:05
 * </pre>
 */
public class PmMailSendLog extends PmMailSendLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 发件人名称
	 */
    public static final String ALIAS_ADDRESSER = "addresser";
	/**
	 * 标题
	 */
    public static final String ALIAS_TITLE = "title";
	/**
	 * 内容
	 */
    public static final String ALIAS_BODY = "body";
	/**
	 * 是否包含附件
	 */
    public static final String ALIAS_IS_FILE = "is_file";
	/**
	 * 收件人列表
	 */
    public static final String ALIAS_RECIPIENTS_IDS = "recipients_ids";
	/**
	 * 收件人姓名列表
	 */
    public static final String ALIAS_RECIPIENTS_NAMES = "recipients_names";
	/**
	 * 收件人组名称
	 */
    public static final String ALIAS_RECIPIENTS_GROUP = "recipients_group";
	/**
	 * 创建者
	 */
    public static final String ALIAS_OWNER = "owner";
	/**
	 * 最后更新时间
	 */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
	/**
	 * 创建时间
	 */
    public static final String ALIAS_CREATE_TIME = "create_time";
	
	/* 发件人名称 */
    private String addresser;
	
	/* 标题 */
    private String title;
	
	/* 内容 */
    private String body;
	
	/* 是否包含附件 */
    private String isFile;
	
	/* 收件人列表 */
    private String recipientsIds;
	
	/* 收件人姓名列表 */
    private String recipientsNames;
	
	/* 收件人组名称 */
    private String recipientsGroup;
	
	/* 创建者 */
    private String owner;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;
	
	/* 创建时间 */
    private Date createTime;
	
	public String getAddresser(){
		return addresser;
	}
	public void setAddresser(String addresser){
		this.addresser = addresser;
	}
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getBody(){
		return body;
	}
	public void setBody(String body){
		this.body = body;
	}
	
	public String getIsFile(){
		return isFile;
	}
	public void setIsFile(String isFile){
		this.isFile = isFile;
	}
	
	public String getRecipientsIds(){
		return recipientsIds;
	}
	public void setRecipientsIds(String recipientsIds){
		this.recipientsIds = recipientsIds;
	}
	
	public String getRecipientsNames(){
		return recipientsNames;
	}
	public void setRecipientsNames(String recipientsNames){
		this.recipientsNames = recipientsNames;
	}
	
	public String getRecipientsGroup(){
		return recipientsGroup;
	}
	public void setRecipientsGroup(String recipientsGroup){
		this.recipientsGroup = recipientsGroup;
	}
	
	public String getOwner(){
		return owner;
	}
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	public Date getLastUpdateTime(){
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	addresser=").append(getAddresser()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	title=").append(getTitle()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	body=").append(getBody()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	isFile=").append(getIsFile()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	recipientsIds=").append(getRecipientsIds()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	recipientsNames=").append(getRecipientsNames()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	recipientsGroup=").append(getRecipientsGroup()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}