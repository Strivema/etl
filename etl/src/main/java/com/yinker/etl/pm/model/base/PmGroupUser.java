package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmGroupUser表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:50
 * </pre>
 */
public class PmGroupUser extends PmGroupUserPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 用户编号
	 */
    public static final String ALIAS_NAME = "name";
	/**
	 * 数据源组编号
	 */
    public static final String ALIAS_GROU_ID = "group_id";
	/**
	 * 创建人
	 */
    public static final String ALIAS_OWNER = "owner";
	/**
	 * 创建时间
	 */
    public static final String ALIAS_CREATE_TIME = "create_time";
	/**
	 * 最后更新时间
	 */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
	
	/* 用户编号 */
    private String name;
	
	/* 数据源组编号 */
    private String groupId;
	
	/* 创建人 */
    private String owner;
	
	/* 创建时间 */
    private Date createTime;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOwner(){
		return owner;
	}
	public void setOwner(String owner){
		this.owner = owner;
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

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	name=").append(getName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	groupId=").append(getGroupId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}