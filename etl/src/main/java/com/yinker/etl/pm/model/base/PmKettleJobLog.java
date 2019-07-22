package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:52
 * </pre>
 */
public class PmKettleJobLog extends PmKettleJobLogPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 通道ID
	 */
    public static final String ALIAS_CHANNEL_ID = "CHANNEL_ID";
	/**
	 * JOB名称
	 */
    public static final String ALIAS_JOBNAME = "JOBNAME";
	/**
	 * 状态（start,end,stopped,running）
	 */
    public static final String ALIAS_STATUS = "STATUS";
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
	/**
	 * 开始时间
	 */
    public static final String ALIAS_STARTDATE = "STARTDATE";
	/**
	 * 结束时间
	 */
    public static final String ALIAS_ENDDATE = "ENDDATE";
	/**
	 * 日志更新时间
	 */
    public static final String ALIAS_LOGDATE = "LOGDATE";
	/**
	 * 依赖规则设定时间
	 */
    public static final String ALIAS_DEPDATE = "DEPDATE";
	/**
	 * 重新执行时间
	 */
    public static final String ALIAS_REPLAYDATE = "REPLAYDATE";
	/**
	 * 日志内容
	 */
    public static final String ALIAS_LOG_FIELD = "LOG_FIELD";
	
	/* 通道ID */
    private String channelId;
	
	/* JOB名称 */
    private String jobname;
	
	/* 状态（start,end,stopped,running） */
    private String status;
	
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
	
	/* 开始时间 */
    private Date startdate;
	
	/* 结束时间 */
    private Date enddate;
	
	/* 日志更新时间 */
    private Date logdate;
	
	/* 依赖规则设定时间 */
    private Date depdate;
	
	/* 重新执行时间 */
    private Date replaydate;
	
	/* 日志内容 */
    private String logField;
	
	public String getChannelId(){
		return channelId;
	}
	public void setChannelId(String channelId){
		this.channelId = channelId;
	}
	
	public String getJobname(){
		return jobname;
	}
	public void setJobname(String jobname){
		this.jobname = jobname;
	}
	
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
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
	
	public Date getStartdate(){
		return startdate;
	}
	public void setStartdate(Date startdate){
		this.startdate = startdate;
	}
	
	public Date getEnddate(){
		return enddate;
	}
	public void setEnddate(Date enddate){
		this.enddate = enddate;
	}
	
	public Date getLogdate(){
		return logdate;
	}
	public void setLogdate(Date logdate){
		this.logdate = logdate;
	}
	
	public Date getDepdate(){
		return depdate;
	}
	public void setDepdate(Date depdate){
		this.depdate = depdate;
	}
	
	public Date getReplaydate(){
		return replaydate;
	}
	public void setReplaydate(Date replaydate){
		this.replaydate = replaydate;
	}
	
	public String getLogField(){
		return logField;
	}
	public void setLogField(String logField){
		this.logField = logField;
	}

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	idJob=").append(getIdJob()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	channelId=").append(getChannelId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	jobname=").append(getJobname()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRead=").append(getLinesRead()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesWritten=").append(getLinesWritten()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesUpdated=").append(getLinesUpdated()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesInput=").append(getLinesInput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesOutput=").append(getLinesOutput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRejected=").append(getLinesRejected()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errors=").append(getErrors()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startdate=").append(DateUtils.dateTimeToString(getStartdate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	enddate=").append(DateUtils.dateTimeToString(getEnddate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logdate=").append(DateUtils.dateTimeToString(getLogdate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	depdate=").append(DateUtils.dateTimeToString(getDepdate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	replaydate=").append(DateUtils.dateTimeToString(getReplaydate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logField=").append(getLogField()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}