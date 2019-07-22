package com.yinker.etl.pm.model.base;

import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

/** 
 * <pre>
 * <b>类描述:</b>PmMailServerConfig表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-03-06 10:15:28
 * </pre>
 */
public class PmMailServerConfig extends PmMailServerConfigPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * CODE
	 */
    public static final String ALIAS_CODE = "code";
	/**
	 * 中文名称
	 */
    public static final String ALIAS_NAME = "name";
	/**
	 * 服务器
	 */
    public static final String ALIAS_HOST = "host";
	/**
	 * 端口号
	 */
    public static final String ALIAS_PORT = "port";
	/**
	 * 协议
	 */
    public static final String ALIAS_PROTOCOL = "protocol";
	/**
	 * 用户名
	 */
    public static final String ALIAS_USER_NAME = "user_name";
	/**
	 * 密码
	 */
    public static final String ALIAS_PASSWORD = "password";
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
	
	/* CODE */
    private String code;
	
	/* 中文名称 */
    private String name;
	
	/* 服务器 */
    private String host;
	
	/* 端口号 */
    private String port;
	
	/* 协议 */
    private String protocol;
	
	/* 用户名 */
    private String userName;
	
	/* 密码 */
    private String password;
	
	/* 创建者 */
    private String owner;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;
	
	/* 创建时间 */
    private Date createTime;
	
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getHost(){
		return host;
	}
	public void setHost(String host){
		this.host = host;
	}
	
	public String getPort(){
		return port;
	}
	public void setPort(String port){
		this.port = port;
	}
	
	public String getProtocol(){
		return protocol;
	}
	public void setProtocol(String protocol){
		this.protocol = protocol;
	}
	
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
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
		str.append("	code=").append(getCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	name=").append(getName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	host=").append(getHost()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	port=").append(getPort()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	protocol=").append(getProtocol()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	userName=").append(getUserName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	password=").append(getPassword()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}