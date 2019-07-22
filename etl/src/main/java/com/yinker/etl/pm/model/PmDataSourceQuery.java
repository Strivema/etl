package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmDataSource;

/** 
 * <pre>
 * <b>类描述:</b>PmDataSource表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-21 11:12:28
 * </pre>
 */ 
public class PmDataSourceQuery extends PmDataSource {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:端口号-开始 */
    private Integer portStart;
	
	/* 区间查询条件:端口号-结束 */
    private Integer portEnd;
	
    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;
	
	/* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;
	
    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateTimeStart;
	
	/* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateTimeEnd;
	
	public Integer getPortStart(){
        return portStart;
    }
	
    public void setPortStart(Integer portStart){
        this.portStart = portStart;
    }
	
	public Integer getPortEnd(){
        return portEnd;
    }
	
    public void setPortEnd(Integer portEnd){
        this.portEnd = portEnd;
    }
	public Date getCreateTimeStart(){
        return createTimeStart;
    }
	
    public void setCreateTimeStart(Date createTimeStart){
        this.createTimeStart = createTimeStart;
    }
	
	public Date getCreateTimeEnd(){
        return createTimeEnd;
    }
	
    public void setCreateTimeEnd(Date createTimeEnd){
        this.createTimeEnd = createTimeEnd;
    }
	public Date getLastUpdateTimeStart(){
        return lastUpdateTimeStart;
    }
	
    public void setLastUpdateTimeStart(Date lastUpdateTimeStart){
        this.lastUpdateTimeStart = lastUpdateTimeStart;
    }
	
	public Date getLastUpdateTimeEnd(){
        return lastUpdateTimeEnd;
    }
	
    public void setLastUpdateTimeEnd(Date lastUpdateTimeEnd){
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
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
		str.append("	portStart=").append(getPortStart()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	port=").append(getPort()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	portEnd=").append(getPortEnd()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	password=").append(getPassword()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTimeStart=").append(DateUtils.dateTimeToString(getLastUpdateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTimeEnd=").append(DateUtils.dateTimeToString(getLastUpdateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}