package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmKettleChannelLog;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleChannelLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-26 14:56:13
 * </pre>
 */ 
public class PmKettleChannelLogQuery extends PmKettleChannelLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:转换日志id-开始 */
    private Integer idStart;
	
	/* 区间查询条件:转换日志id-结束 */
    private Integer idEnd;
	
    /* 区间查询条件:批处理ID-开始 */
    private Integer idBatchStart;
	
	/* 区间查询条件:批处理ID-结束 */
    private Integer idBatchEnd;
	
    /* 区间查询条件:日志时间-开始 */
    private Date logDateStart;
	
	/* 区间查询条件:日志时间-结束 */
    private Date logDateEnd;
	
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