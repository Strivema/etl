package com.yinker.etl.trans.model;
import com.yinker.etl.trans.model.base.TransInfo;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>TransInfo表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:58
 * </pre>
 */ 
public class TransInfoQuery extends TransInfo {

    private static final long serialVersionUID = 1L;

    /* 区间查询条件:创建时间-开始 */
    private Date createTimeStart;
	
	/* 区间查询条件:创建时间-结束 */
    private Date createTimeEnd;
	
    /* 区间查询条件:最后更新时间-开始 */
    private Date lastUpdateDateStart;
	
	/* 区间查询条件:最后更新时间-结束 */
    private Date lastUpdateDateEnd;

    /* 用于在查询条件加GroupBy字段 */
    private String groupByCloume;

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
	public Date getLastUpdateDateStart(){
        return lastUpdateDateStart;
    }
	
    public void setLastUpdateDateStart(Date lastUpdateDateStart){
        this.lastUpdateDateStart = lastUpdateDateStart;
    }
	
	public Date getLastUpdateDateEnd(){
        return lastUpdateDateEnd;
    }
	
    public void setLastUpdateDateEnd(Date lastUpdateDateEnd){
        this.lastUpdateDateEnd = lastUpdateDateEnd;
    }

    public String getGroupByCloume () {
        return groupByCloume;
    }

    public void setGroupByCloume (String groupByCloume) {
        this.groupByCloume = groupByCloume;
    }

    /**
	 * toString方法
	 */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transName=").append(getTransName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transType=").append(getTransType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	srcDbId=").append(getSrcDbId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	srcDbCode=").append(getSrcDbCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	srcTbCode=").append(getSrcTbCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	srcDescript=").append(getSrcDescript()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	targetDbId=").append(getTargetDbId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	targetDbCode=").append(getTargetDbCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	targetTbCode=").append(getTargetTbCode()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	targetDescript=").append(getTargetDescript()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	whereStr=").append(getWhereStr()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	isEnable=").append(getIsEnable()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	isDiyQuartz=").append(getIsDiyQuartz()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	cron=").append(getCron()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDateStart=").append(DateUtils.dateTimeToString(getLastUpdateDateStart())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDate=").append(DateUtils.dateTimeToString(getLastUpdateDate())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateDateEnd=").append(DateUtils.dateTimeToString(getLastUpdateDateEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}