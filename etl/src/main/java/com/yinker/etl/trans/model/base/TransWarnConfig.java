package com.yinker.etl.trans.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>TransWarnConfig表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 20:20:31
 * </pre>
 */
public class TransWarnConfig extends TransWarnConfigPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 转换编号
	 */
    public static final String ALIAS_TRANS_ID = "trans_id";
	/**
	 * 安全时间
	 */
    public static final String ALIAS_SAFE_TIME = "safe_time";
	/**
	 * 状态
	 */
    public static final String ALIAS_STATUS = "status";
	/**
	 * 备注
	 */
    public static final String ALIAS_REMARK = "remark";
	/**
	 * 创建时间
	 */
    public static final String ALIAS_CREATE_TIME = "create_time";
	/**
	 * 最后更新时间
	 */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
	
	/* 转换编号 */
    private String transId;
	
	/* 安全时间 */
    private Integer safeTime;
	
	/* 状态 */
    private String status;
	
	/* 备注 */
    private String remark;
	
	/* 创建时间 */
    private Date createTime;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;
	
	public String getTransId(){
		return transId;
	}
	public void setTransId(String transId){
		this.transId = transId;
	}
	
	public Integer getSafeTime(){
		return safeTime;
	}
	public void setSafeTime(Integer safeTime){
		this.safeTime = safeTime;
	}
	
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	public Date getLastUpdateTime(){
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transId=").append(getTransId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	safeTime=").append(getSafeTime()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}