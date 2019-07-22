package com.yinker.etl.pm.model;
import java.util.Date;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;
import com.yinker.etl.pm.model.base.PmMailSendLog;

/** 
 * <pre>
 * <b>类描述:</b>PmMailSendLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:04
 * </pre>
 */ 
public class PmMailSendLogQuery extends PmMailSendLog {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateTimeStart;
	
	/* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateTimeEnd;
	
    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;
	
	/* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;
	
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
	
	/**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	addresser=").append(getAddresser()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	title=").append(getTitle()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	body=").append(getBody()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	isFile=").append(getIsFile()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	recipientsIds=").append(getRecipientsIds()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	recipientsNames=").append(getRecipientsNames()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	recipientsGroup=").append(getRecipientsGroup()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTimeStart=").append(DateUtils.dateTimeToString(getLastUpdateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTimeEnd=").append(DateUtils.dateTimeToString(getLastUpdateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}