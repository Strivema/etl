package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmKettleTransLog;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleTransLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:48
 * </pre>
 */ 
public class PmKettleTransLogQuery extends PmKettleTransLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:批处理ID(唯一标识)-开始 */
    private Integer idBatchStart;
	
	/* 区间查询条件:批处理ID(唯一标识)-结束 */
    private Integer idBatchEnd;
	
    /* 区间查询条件:开始时间-开始 */
    private Date startdateStart;
	
	/* 区间查询条件:开始时间-结束 */
    private Date startdateEnd;
	
    /* 区间查询条件:结束时间-开始 */
    private Date enddateStart;
	
	/* 区间查询条件:结束时间-结束 */
    private Date enddateEnd;
	
    /* 区间查询条件:日志更新时间-开始 */
    private Date logdateStart;
	
	/* 区间查询条件:日志更新时间-结束 */
    private Date logdateEnd;
	
    /* 区间查询条件:依赖规则设定时间-开始 */
    private Date depdateStart;
	
	/* 区间查询条件:依赖规则设定时间-结束 */
    private Date depdateEnd;
	
    /* 区间查询条件:重新执行时间-开始 */
    private Date replaydateStart;
	
	/* 区间查询条件:重新执行时间-结束 */
    private Date replaydateEnd;

    private String extendSQL;
	
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
	public Date getStartdateStart(){
        return startdateStart;
    }
	
    public void setStartdateStart(Date startdateStart){
        this.startdateStart = startdateStart;
    }
	
	public Date getStartdateEnd(){
        return startdateEnd;
    }
	
    public void setStartdateEnd(Date startdateEnd){
        this.startdateEnd = startdateEnd;
    }
	public Date getEnddateStart(){
        return enddateStart;
    }
	
    public void setEnddateStart(Date enddateStart){
        this.enddateStart = enddateStart;
    }
	
	public Date getEnddateEnd(){
        return enddateEnd;
    }
	
    public void setEnddateEnd(Date enddateEnd){
        this.enddateEnd = enddateEnd;
    }
	public Date getLogdateStart(){
        return logdateStart;
    }
	
    public void setLogdateStart(Date logdateStart){
        this.logdateStart = logdateStart;
    }
	
	public Date getLogdateEnd(){
        return logdateEnd;
    }
	
    public void setLogdateEnd(Date logdateEnd){
        this.logdateEnd = logdateEnd;
    }
	public Date getDepdateStart(){
        return depdateStart;
    }
	
    public void setDepdateStart(Date depdateStart){
        this.depdateStart = depdateStart;
    }
	
	public Date getDepdateEnd(){
        return depdateEnd;
    }
	
    public void setDepdateEnd(Date depdateEnd){
        this.depdateEnd = depdateEnd;
    }
	public Date getReplaydateStart(){
        return replaydateStart;
    }
	
    public void setReplaydateStart(Date replaydateStart){
        this.replaydateStart = replaydateStart;
    }
	
	public Date getReplaydateEnd(){
        return replaydateEnd;
    }
	
    public void setReplaydateEnd(Date replaydateEnd){
        this.replaydateEnd = replaydateEnd;
    }

    public String getExtendSQL() {
        return extendSQL;
    }

    public void setExtendSQL(String extendSQL) {
        this.extendSQL = extendSQL;
    }

    /**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatchStart=").append(getIdBatchStart()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatch=").append(getIdBatch()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	idBatchEnd=").append(getIdBatchEnd()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	channelId=").append(getChannelId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transname=").append(getTransname()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRead=").append(getLinesRead()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesWritten=").append(getLinesWritten()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesUpdated=").append(getLinesUpdated()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesInput=").append(getLinesInput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesOutput=").append(getLinesOutput()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	linesRejected=").append(getLinesRejected()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	errors=").append(getErrors()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startdateStart=").append(DateUtils.dateTimeToString(getStartdateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startdate=").append(DateUtils.dateTimeToString(getStartdate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startdateEnd=").append(DateUtils.dateTimeToString(getStartdateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	enddateStart=").append(DateUtils.dateTimeToString(getEnddateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	enddate=").append(DateUtils.dateTimeToString(getEnddate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	enddateEnd=").append(DateUtils.dateTimeToString(getEnddateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logdateStart=").append(DateUtils.dateTimeToString(getLogdateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logdate=").append(DateUtils.dateTimeToString(getLogdate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logdateEnd=").append(DateUtils.dateTimeToString(getLogdateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	depdateStart=").append(DateUtils.dateTimeToString(getDepdateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	depdate=").append(DateUtils.dateTimeToString(getDepdate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	depdateEnd=").append(DateUtils.dateTimeToString(getDepdateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	replaydateStart=").append(DateUtils.dateTimeToString(getReplaydateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	replaydate=").append(DateUtils.dateTimeToString(getReplaydate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	replaydateEnd=").append(DateUtils.dateTimeToString(getReplaydateEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	logField=").append(getLogField()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}