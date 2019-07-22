package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmMailUser表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:03
 * </pre>
 */
public class PmMailUser extends PmMailUserPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 姓名
	 */
    public static final String ALIAS_NAME = "name";
	/**
	 * 电话
	 */
    public static final String ALIAS_PHONE = "phone";
	/**
	 * 邮箱
	 */
    public static final String ALIAS_EMAIL = "email";
	/**
	 * 所属组
	 */
    public static final String ALIAS_GROUP = "group";
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
	
	/* 姓名 */
    private String name;
	
	/* 电话 */
    private String phone;
	
	/* 邮箱 */
    private String email;
	
	/* 所属组 */
    private String group;
	
	/* 创建者 */
    private String owner;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;
	
	/* 创建时间 */
    private Date createTime;
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getGroup(){
		return group;
	}
	public void setGroup(String group){
		this.group = group;
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
		str.append("	name=").append(getName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	phone=").append(getPhone()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	email=").append(getEmail()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	group=").append(getGroup()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}