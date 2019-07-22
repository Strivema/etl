package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmDataSource表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-21 11:12:28
 * </pre>
 */
public class PmDataSource extends PmDataSourcePK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 中文名
	 */
    public static final String ALIAS_NAME = "name";
	/**
	 * 识别名称
	 */
    public static final String ALIAS_CODE = "code";
	/**
	 * 数据库名称
	 */
    public static final String ALIAS_DATABASE_NAME = "database_name";
	/**
	 * 数据源类型
	 */
    public static final String ALIAS_DATABASE_TYPE = "database_type";
	/**
	 * 数据源分类
	 */
    public static final String ALIAS_DATABASE_CATEGORY = "database_category";
	/**
	 * 服务地址
	 */
    public static final String ALIAS_HOST_NAME = "host_name";
	/**
	 * 用户名
	 */
    public static final String ALIAS_USER_NAME = "user_name";
	/**
	 * 端口号
	 */
    public static final String ALIAS_PORT = "port";
	/**
	 * 密码
	 */
    public static final String ALIAS_PASSWORD = "password";
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
	
	/* 中文名 */
    private String name;
	
	/* 识别名称 */
    private String code;
	
	/* 数据库名称 */
    private String databaseName;
	
	/* 数据源类型 */
    private String databaseType;
	
	/* 数据源分类 */
    private String databaseCategory;
	
	/* 服务地址 */
    private String hostName;
	
	/* 用户名 */
    private String userName;
	
	/* 端口号 */
    private Integer port;
	
	/* 密码 */
    private String password;
	
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
	
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	
	public String getDatabaseName(){
		return databaseName;
	}
	public void setDatabaseName(String databaseName){
		this.databaseName = databaseName;
	}
	
	public String getDatabaseType(){
		return databaseType;
	}
	public void setDatabaseType(String databaseType){
		this.databaseType = databaseType;
	}
	
	public String getDatabaseCategory(){
		return databaseCategory;
	}
	public void setDatabaseCategory(String databaseCategory){
		this.databaseCategory = databaseCategory;
	}
	
	public String getHostName(){
		return hostName;
	}
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public Integer getPort(){
		return port;
	}
	public void setPort(Integer port){
		this.port = port;
	}
	
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
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
		str.append("	code=").append(getCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	databaseName=").append(getDatabaseName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	databaseType=").append(getDatabaseType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	databaseCategory=").append(getDatabaseCategory()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	hostName=").append(getHostName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	userName=").append(getUserName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	port=").append(getPort()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	password=").append(getPassword()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}