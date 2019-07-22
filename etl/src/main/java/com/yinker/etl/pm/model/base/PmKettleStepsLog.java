package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleStepsLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:49
 * </pre>
 */
public class PmKettleStepsLog extends PmKettleStepsLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 批处理ID(唯一标识)
	 */
    public static final String ALIAS_ID_BATCH = "ID_BATCH";
	/**
	 * 通道ID
	 */
    public static final String ALIAS_CHANNEL_ID = "CHANNEL_ID";
	/**
	 * 日志记录时间
	 */
    public static final String ALIAS_LOG_DATE = "LOG_DATE";
	/**
	 * 转换名
	 */
    public static final String ALIAS_TRANSNAME = "TRANSNAME";
	/**
	 * 步骤名
	 */
    public static final String ALIAS_STEPNAME = "STEPNAME";
	/**
	 * 步骤重复编号
	 */
    public static final String ALIAS_STEP_COPY = "STEP_COPY";
	/**
	 * 读取记录条数
	 */
    public static final String ALIAS_LINES_READ = "LINES_READ";
	/**
	 * 写入记录条数
	 */
    public static final String ALIAS_LINES_WRITTEN = "LINES_WRITTEN";
	/**
	 * 更新记录条数
	 */
    public static final String ALIAS_LINES_UPDATED = "LINES_UPDATED";
	/**
	 * 获取记录条数-从文件
	 */
    public static final String ALIAS_LINES_INPUT = "LINES_INPUT";
	/**
	 * 输出记录条数-到文件
	 */
    public static final String ALIAS_LINES_OUTPUT = "LINES_OUTPUT";
	/**
	 * 拒绝记录条数
	 */
    public static final String ALIAS_LINES_REJECTED = "LINES_REJECTED";
	/**
	 * 错误数
	 */
    public static final String ALIAS_ERRORS = "ERRORS";
	
	/* 批处理ID(唯一标识) */
    private Integer idBatch;
	
	/* 通道ID */
    private String channelId;
	
	/* 日志记录时间 */
    private Date logDate;
	
	/* 转换名 */
    private String transname;
	
	/* 步骤名 */
    private String stepname;
	
	/* 步骤重复编号 */
    private String stepCopy;
	
	/* 读取记录条数 */
    private Long linesRead;
	
	/* 写入记录条数 */
    private Long linesWritten;
	
	/* 更新记录条数 */
    private Long linesUpdated;
	
	/* 获取记录条数-从文件 */
    private Long linesInput;
	
	/* 输出记录条数-到文件 */
    private Long linesOutput;
	
	/* 拒绝记录条数 */
    private Long linesRejected;
	
	/* 错误数 */
    private Long errors;
	
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
	
	public String getTransname(){
		return transname;
	}
	public void setTransname(String transname){
		this.transname = transname;
	}
	
	public String getStepname(){
		return stepname;
	}
	public void setStepname(String stepname){
		this.stepname = stepname;
	}
	
	public String getStepCopy(){
		return stepCopy;
	}
	public void setStepCopy(String stepCopy){
		this.stepCopy = stepCopy;
	}
	
	public Long getLinesRead(){
		return linesRead;
	}
	public void setLinesRead(Long linesRead){
		this.linesRead = linesRead;
	}
	
	public Long getLinesWritten(){
		return linesWritten;
	}
	public void setLinesWritten(Long linesWritten){
		this.linesWritten = linesWritten;
	}
	
	public Long getLinesUpdated(){
		return linesUpdated;
	}
	public void setLinesUpdated(Long linesUpdated){
		this.linesUpdated = linesUpdated;
	}
	
	public Long getLinesInput(){
		return linesInput;
	}
	public void setLinesInput(Long linesInput){
		this.linesInput = linesInput;
	}
	
	public Long getLinesOutput(){
		return linesOutput;
	}
	public void setLinesOutput(Long linesOutput){
		this.linesOutput = linesOutput;
	}
	
	public Long getLinesRejected(){
		return linesRejected;
	}
	public void setLinesRejected(Long linesRejected){
		this.linesRejected = linesRejected;
	}
	
	public Long getErrors(){
		return errors;
	}
	public void setErrors(Long errors){
		this.errors = errors;
	}

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
		str.append("	transname=").append(getTransname()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	stepname=").append(getStepname()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	stepCopy=").append(getStepCopy()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRead=").append(getLinesRead()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesWritten=").append(getLinesWritten()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesUpdated=").append(getLinesUpdated()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesInput=").append(getLinesInput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesOutput=").append(getLinesOutput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRejected=").append(getLinesRejected()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errors=").append(getErrors()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}