package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmSystemInfoLog;

/** 
 * <pre>
 * <b>类描述:</b>PmSystemInfoLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:39:08
 * </pre>
 */ 
public class PmSystemInfoLogQuery extends PmSystemInfoLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:编号-开始 */
    private Integer idStart;
	
	/* 区间查询条件:编号-结束 */
    private Integer idEnd;
	
    /* 区间查询条件:启动时间-开始 */
    private Date startTimeStart;
	
	/* 区间查询条件:启动时间-结束 */
    private Date startTimeEnd;
	
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
	public Date getStartTimeStart(){
        return startTimeStart;
    }
	
    public void setStartTimeStart(Date startTimeStart){
        this.startTimeStart = startTimeStart;
    }
	
	public Date getStartTimeEnd(){
        return startTimeEnd;
    }
	
    public void setStartTimeEnd(Date startTimeEnd){
        this.startTimeEnd = startTimeEnd;
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
		str.append("	sn=").append(getSn()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	serverName=").append(getServerName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	ip=").append(getIp()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	osName=").append(getOsName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startTimeStart=").append(DateUtils.dateTimeToString(getStartTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startTime=").append(DateUtils.dateTimeToString(getStartTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	startTimeEnd=").append(DateUtils.dateTimeToString(getStartTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	destoryTime=").append(getDestoryTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}