package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleChannelLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-26 14:56:13
 * </pre>
 */
public class PmKettleChannelLog extends PmKettleChannelLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 批处理ID
	 */
    public static final String ALIAS_ID_BATCH = "ID_BATCH";
	/**
	 * 通道ID
	 */
    public static final String ALIAS_CHANNEL_ID = "CHANNEL_ID";
	/**
	 * 日志时间
	 */
    public static final String ALIAS_LOG_DATE = "LOG_DATE";
	/**
	 * 日志类型
	 */
    public static final String ALIAS_LOGGING_OBJECT_TYPE = "LOGGING_OBJECT_TYPE";
	/**
	 * 对象名称
	 */
    public static final String ALIAS_OBJECT_NAME = "OBJECT_NAME";
	/**
	 * 复制
	 */
    public static final String ALIAS_OBJECT_COPY = "OBJECT_COPY";
	/**
	 * 存放目录
	 */
    public static final String ALIAS_REPOSITORY_DIRECTORY = "REPOSITORY_DIRECTORY";
	/**
	 * 文件名称
	 */
    public static final String ALIAS_FILENAME = "FILENAME";
	/**
	 * 对象编号
	 */
    public static final String ALIAS_OBJECT_ID = "OBJECT_ID";
	/**
	 * 对象版本号
	 */
    public static final String ALIAS_OBJECT_REVISION = "OBJECT_REVISION";
	/**
	 * 父通道ID
	 */
    public static final String ALIAS_PARENT_CHANNEL_ID = "PARENT_CHANNEL_ID";
	/**
	 * 根通道ID
	 */
    public static final String ALIAS_ROOT_CHANNEL_ID = "ROOT_CHANNEL_ID";
	
	/* 批处理ID */
    private Integer idBatch;
	
	/* 通道ID */
    private String channelId;
	
	/* 日志时间 */
    private Date logDate;
	
	/* 日志类型 */
    private String loggingObjectType;
	
	/* 对象名称 */
    private String objectName;
	
	/* 复制 */
    private String objectCopy;
	
	/* 存放目录 */
    private String repositoryDirectory;
	
	/* 文件名称 */
    private String filename;
	
	/* 对象编号 */
    private String objectId;
	
	/* 对象版本号 */
    private String objectRevision;
	
	/* 父通道ID */
    private String parentChannelId;
	
	/* 根通道ID */
    private String rootChannelId;
	
	public Integer getIdBatch(){
		return idBatch;
	}
	public void setIdBatch(Integer idBatch){
		this.idBatch = idBatch;
	}
	
	public String getChannelId(){
		return channelId;
	}
	public void setChannelId(String channelId){
		this.channelId = channelId;
	}
	
	public Date getLogDate(){
		return logDate;
	}
	public void setLogDate(Date logDate){
		this.logDate = logDate;
	}
	
	public String getLoggingObjectType(){
		return loggingObjectType;
	}
	public void setLoggingObjectType(String loggingObjectType){
		this.loggingObjectType = loggingObjectType;
	}
	
	public String getObjectName(){
		return objectName;
	}
	public void setObjectName(String objectName){
		this.objectName = objectName;
	}
	
	public String getObjectCopy(){
		return objectCopy;
	}
	public void setObjectCopy(String objectCopy){
		this.objectCopy = objectCopy;
	}
	
	public String getRepositoryDirectory(){
		return repositoryDirectory;
	}
	public void setRepositoryDirectory(String repositoryDirectory){
		this.repositoryDirectory = repositoryDirectory;
	}
	
	public String getFilename(){
		return filename;
	}
	public void setFilename(String filename){
		this.filename = filename;
	}
	
	public String getObjectId(){
		return objectId;
	}
	public void setObjectId(String objectId){
		this.objectId = objectId;
	}
	
	public String getObjectRevision(){
		return objectRevision;
	}
	public void setObjectRevision(String objectRevision){
		this.objectRevision = objectRevision;
	}
	
	public String getParentChannelId(){
		return parentChannelId;
	}
	public void setParentChannelId(String parentChannelId){
		this.parentChannelId = parentChannelId;
	}
	
	public String getRootChannelId(){
		return rootChannelId;
	}
	public void setRootChannelId(String rootChannelId){
		this.rootChannelId = rootChannelId;
	}

	/* 关联对象-父表信息 */
	/*private PmKettleStepsLog pmKettleStepsLog;

    public PmKettleStepsLog getPmKettleStepsLog(){
        return this.pmKettleStepsLog;
    }
    public void setPmKettleStepsLog(PmKettleStepsLog pmKettleStepsLog){
        this.pmKettleStepsLog = pmKettleStepsLog;
    }*/

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatch=").append(getIdBatch()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	channelId=").append(getChannelId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDate=").append(DateUtils.dateTimeToString(getLogDate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	loggingObjectType=").append(getLoggingObjectType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	objectName=").append(getObjectName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	objectCopy=").append(getObjectCopy()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	repositoryDirectory=").append(getRepositoryDirectory()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	filename=").append(getFilename()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	objectId=").append(getObjectId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	objectRevision=").append(getObjectRevision()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	parentChannelId=").append(getParentChannelId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	rootChannelId=").append(getRootChannelId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}