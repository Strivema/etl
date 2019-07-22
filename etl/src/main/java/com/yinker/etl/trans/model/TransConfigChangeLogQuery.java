package com.yinker.etl.trans.model;
import com.yinker.etl.trans.model.base.TransConfigChangeLog;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>TransConfigChangeLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:00
 * </pre>
 */ 
public class TransConfigChangeLogQuery extends TransConfigChangeLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:操作时间-开始 */
    private Date operateTimeStart;
	
	/* 区间查询条件:操作时间-结束 */
    private Date operateTimeEnd;
	
    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;
	
	/* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;
	
    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateTimeStart;
	
	/* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateTimeEnd;
	
	public Date getOperateTimeStart(){
        return operateTimeStart;
    }
	
    public void setOperateTimeStart(Date operateTimeStart){
        this.operateTimeStart = operateTimeStart;
    }
	
	public Date getOperateTimeEnd(){
        return operateTimeEnd;
    }
	
    public void setOperateTimeEnd(Date operateTimeEnd){
        this.operateTimeEnd = operateTimeEnd;
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
		str.append("	userId=").append(getUserId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	tableName=").append(getTableName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateLog=").append(getOperateLog()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateType=").append(getOperateType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateTimeStart=").append(DateUtils.dateTimeToString(getOperateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateTime=").append(DateUtils.dateTimeToString(getOperateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	operateTimeEnd=").append(DateUtils.dateTimeToString(getOperateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
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