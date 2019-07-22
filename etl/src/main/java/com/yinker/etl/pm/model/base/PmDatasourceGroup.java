package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/** 
 * <pre>
 * <b>类描述:</b>PmDatasourceGroup表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:52
 * </pre>
 */
public class PmDatasourceGroup extends PmDatasourceGroupPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 组编号
	 */
    public static final String ALIAS_GROUP_ID = "group_id";
	/**
	 * 数据源类型
	 */
    public static final String ALIAS_DATASOURCE_TYPE = "datasource_type";
	/**
	 * 数据源编号
	 */
    public static final String ALIAS_DATASOURCE_ID = "datasource_id";
	/**
	 * 创建人
	 */
    public static final String ALIAS_OWNER = "owner";
	/**
	 * 创建时间
	 */
    public static final String ALIAS_CREATE_TIME = "create_time";
	/**
	 * 最后更新时间
	 */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
	
	/* 组编号 */
    private String groupId;
	
	/* 数据源类型 */
    private String datasourceType;
	
	/* 数据源编号 */
    private String datasourceId;
	
	/* 创建人 */
    private String owner;
	
	/* 创建时间 */
    private Date createTime;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;

	/* 关联对象-父表信息 */
	private PmDataSource pmDataSource;
	
	public String getGroupId(){
		return groupId;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	
	public String getDatasourceType(){
		return datasourceType;
	}
	public void setDatasourceType(String datasourceType){
		this.datasourceType = datasourceType;
	}
	
	public String getDatasourceId(){
		return datasourceId;
	}
	public void setDatasourceId(String datasourceId){
		this.datasourceId = datasourceId;
	}
	
	public String getOwner(){
		return owner;
	}
	public void setOwner(String owner){
		this.owner = owner;
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
    
    public PmDataSource getPmDataSource(){
        return this.pmDataSource;
    }
    public void setPmDataSource(PmDataSource pmDataSource){
        this.pmDataSource = pmDataSource;
    }

	/**
	 * toString方法
	 */
	@Override
    public String toString() {
		StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	groupId=").append(getGroupId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	datasourceType=").append(getDatasourceType()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	datasourceId=").append(getDatasourceId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}