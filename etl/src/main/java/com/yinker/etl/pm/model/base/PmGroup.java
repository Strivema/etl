package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;
import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>PmGroup表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-19 15:17:42
 * </pre>
 */
public class PmGroup extends PmGroupPK {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 名称
	 */
    public static final String ALIAS_NAME = "name";
	/**
	 * 可创建转换的数量
	 */
    public static final String ALIAS_TRANS_COUNT = "trans_count";
	/**
	 * 描述
	 */
    public static final String ALIAS_REMARK = "remark";
	/**
	 * 状态
	 */
    public static final String ALIAS_STATUS = "status";
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
	
	/* 名称 */
    private String name;
	
	/* 可创建转换的数量 */
    private Integer transCount;
	
	/* 描述 */
    private String remark;
	
	/* 状态 */
    private String status;
	
	/* 创建人 */
    private String owner;
	
	/* 创建时间 */
    private Date createTime;
	
	/* 最后更新时间 */
    private Date lastUpdateTime;

	//源、目标数据源字符串，用于list和view显示
	private String srcListView;
	private String tgtListView;
	private String userListView;

    /* 关联对象-子表信息 */
    private List<PmGroupUser> pmGroupUsers;
    private List<PmDatasourceGroup> pmDatasourceGroups;
    private List<PmDatasourceGroup> pmDSSourceGroups;
    private List<PmDatasourceGroup> pmDSTargetGroups;

	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public Integer getTransCount(){
		return transCount;
	}
	public void setTransCount(Integer transCount){
		this.transCount = transCount;
	}
	
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
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

	public String getSrcListView() {
		return srcListView;
	}

	public void setSrcListView(String srcListView) {
		this.srcListView = srcListView;
	}

	public String getTgtListView() {
		return tgtListView;
	}

	public void setTgtListView(String tgtListView) {
		this.tgtListView = tgtListView;
	}

	public String getUserListView() {
		return userListView;
	}

	public void setUserListView(String userListView) {
		this.userListView = userListView;
	}

	public List<PmGroupUser> getPmGroupUsers(){
        return pmGroupUsers;
    }

    public void setPmGroupUsers (List<PmGroupUser> pmGroupUsers) {
        this.pmGroupUsers = pmGroupUsers;
    }

    public List<PmDatasourceGroup> getPmDatasourceGroups () {
        return pmDatasourceGroups;
    }

    public void setPmDatasourceGroups (List<PmDatasourceGroup> pmDatasourceGroups) {
        this.pmDatasourceGroups = pmDatasourceGroups;
    }


    public void setPmDSSourceGroups (List<PmDatasourceGroup> pmDSSourceGroups) {
        this.pmDSSourceGroups = pmDSSourceGroups;
    }


    public void setPmDSTargetGroups (List<PmDatasourceGroup> pmDSTargetGroups) {
        this.pmDSTargetGroups = pmDSTargetGroups;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	name=").append(getName()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	transCount=").append(getTransCount()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
		str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
		str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}