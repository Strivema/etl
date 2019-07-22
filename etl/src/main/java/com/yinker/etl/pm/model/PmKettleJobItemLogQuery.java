package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmKettleJobItemLog;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobItemLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:53
 * </pre>
 */ 
public class PmKettleJobItemLogQuery extends PmKettleJobItemLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:id-开始 */
    private Integer idStart;
	
	/* 区间查询条件:id-结束 */
    private Integer idEnd;
	
    /* 区间查询条件:批处理ID(唯一标识)-开始 */
    private Integer idBatchStart;
	
	/* 区间查询条件:批处理ID(唯一标识)-结束 */
    private Integer idBatchEnd;
	
    /* 区间查询条件:日志记录时间-开始 */
    private Date logDateStart;
	
	/* 区间查询条件:日志记录时间-结束 */
    private Date logDateEnd;
	
    /* 区间查询条件:结果-开始 */
    private Integer resultStart;
	
	/* 区间查询条件:结果-结束 */
    private Integer resultEnd;
	
	public Integer getIdStart(){
        return idStart;
    }
	
    public void setIdStart(Integer idStart){
        this.idStart = idStart;
    }
	
	public Integer getIdEnd(){
        return idEnd;
    }
	
    public void setIdEnd(Integer idEnd){
        this.idEnd = idEnd;
    }
	public Integer getIdBatchStart(){
        return idBatchStart;
    }
	
    public void setIdBatchStart(Integer idBatchStart){
        this.idBatchStart = idBatchStart;
    }
	
	public Integer getIdBatchEnd(){
        return idBatchEnd;
    }
	
    public void setIdBatchEnd(Integer idBatchEnd){
        this.idBatchEnd = idBatchEnd;
    }
	public Date getLogDateStart(){
        return logDateStart;
    }
	
    public void setLogDateStart(Date logDateStart){
        this.logDateStart = logDateStart;
    }
	
	public Date getLogDateEnd(){
        return logDateEnd;
    }
	
    public void setLogDateEnd(Date logDateEnd){
        this.logDateEnd = logDateEnd;
    }
	public Integer getResultStart(){
        return resultStart;
    }
	
    public void setResultStart(Integer resultStart){
        this.resultStart = resultStart;
    }
	
	public Integer getResultEnd(){
        return resultEnd;
    }
	
    public void setResultEnd(Integer resultEnd){
        this.resultEnd = resultEnd;
    }
	
	/**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	idStart=").append(getIdStart()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idEnd=").append(getIdEnd()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatchStart=").append(getIdBatchStart()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatch=").append(getIdBatch()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatchEnd=").append(getIdBatchEnd()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	channelId=").append(getChannelId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDateStart=").append(DateUtils.dateTimeToString(getLogDateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDate=").append(DateUtils.dateTimeToString(getLogDate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logDateEnd=").append(DateUtils.dateTimeToString(getLogDateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transname=").append(getTransname()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	stepname=").append(getStepname()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRead=").append(getLinesRead()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesWritten=").append(getLinesWritten()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesUpdated=").append(getLinesUpdated()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesInput=").append(getLinesInput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesOutput=").append(getLinesOutput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRejected=").append(getLinesRejected()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errors=").append(getErrors()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	resultStart=").append(getResultStart()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	result=").append(getResult()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	resultEnd=").append(getResultEnd()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	nrResultRows=").append(getNrResultRows()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	nrResultFiles=").append(getNrResultFiles()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}