package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmGroupUser;

/** 
 * <pre>
 * <b>类描述:</b>PmGroupUser表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:50
 * </pre>
 */ 
public class PmGroupUserQuery extends PmGroupUser {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;
	
	/* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;
	
    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateTimeStart;
	
	/* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateTimeEnd;
	
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
		str.append("	groupId=").append(getGroupId()).append(SystemUtils.LINE_SEPARATOR);
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