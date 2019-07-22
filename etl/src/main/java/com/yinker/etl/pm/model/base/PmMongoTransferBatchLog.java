package com.yinker.etl.pm.model.base;

import org.springframework.stereotype.Component;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/* MYSQL TO MONGO跑批记录表 */
@Component("pmMongoTransferBatchLog")
public class PmMongoTransferBatchLog extends PmMongoTransferBatchLogPK{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 表Id
     */
    public static final String ALIAS_ID = "id";
    
    /**
     * 批次
     */
    public static final String ALIAS_TANLE_NAME = "batch_id";
    
    /**
     * 开始时间
     */
    public static final String ALIAS_START_TIME = "start_time";
    
    /**
     * 结束时间
     */
    public static final String ALIAS_END_TIME = "end_time";
    
    /**
     * 是否异常
     */
    public static final String ALIAS_BOOL_EXCEPTION = "bool_exception";
    
    /**
     * 异常信息
     */
    public static final String ALIAS_REMARK = "remark";
    
    /**
     * 记录时间
     */
    public static final String ALIAS_CREATE_TIME = "create_time";
    
    /**
     * 最后更新时间
     */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
    
    /**
     * 操作员
     */
    public static final String ALIAS_OPERATOR = "operator";
   

    /* id */
    private String id;
    
    /* 批次 */
    private String batchId;
    
    /* 开始时间 */
    private Date startTime;
    
    /* 结束时间 */
    private Date endTime;
    
    /* 是否异常 */
    private String boolException;
    
    /* 异常信息 */
    private String remark;
    
    /* 记录时间 */
    private Date createTime;
    
    /* 最后更新时间 */
    private Date lastUpdateTime;
    
    /* 操作员 */
    private String operator;

    
    
    @Override
    public String getId() {
		return id;
	}



	@Override
    public void setId(String id2) {
		this.id = id2;
	}



	public String getBatchId() {
		return batchId;
	}



	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}



	public Date getStartTime() {
		return startTime;
	}



	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}



	public Date getEndTime() {
		return endTime;
	}



	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}



	public String getBoolException() {
		return boolException;
	}



	public void setBoolException(String boolException) {
		this.boolException = boolException;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}



	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}



	public String getOperator() {
		return operator;
	}



	public void setOperator(String operator) {
		this.operator = operator;
	}



	/**
     * toString方法
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("    id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    batchId=").append(getBatchId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    startTime=").append(DateUtils.dateTimeToString(getStartTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    endTime=").append(DateUtils.dateTimeToString(getEndTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    boolException=").append(getBoolException()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    operator=").append(getOperator()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
