package com.yinker.etl.trans.model.base;

import com.yinker.etl.pm.model.base.PmDataSource;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>TransTableStructureInfo表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-21 15:58:42
 * </pre>
 */
public class TransTableStructureInfo extends TransTableStructureInfoPK {

    private static final long serialVersionUID = 1L;

    /**
     * 转换名称
     */
    public static final String ALIAS_TRANS_NAME = "trans_name";
    /**
     * 转换中文名称
     */
    public static final String ALIAS_SIMPLE_NAME = "simple_name";
    /**
     * 转换类型
     */
    public static final String ALIAS_TRANS_TYPE = "trans_type";
    /**
     * 源数据源Id
     */
    public static final String ALIAS_SRC_DB_ID = "src_db_id";
    /**
     * 源数据源Code
     */
    public static final String ALIAS_SRC_DB_CODE = "src_db_code";
    /**
     * 源数据表Code
     */
    public static final String ALIAS_SRC_TB_CODE = "src_tb_code";
    /**
     * 源表描述
     */
    public static final String ALIAS_SRC_DESCRIPT = "src_descript";
    /**
     * 目标数据源Id
     */
    public static final String ALIAS_TARGET_DB_ID = "target_db_id";
    /**
     * 目标数据源Code
     */
    public static final String ALIAS_TARGET_DB_CODE = "target_db_code";
    /**
     * 目标数据表Code
     */
    public static final String ALIAS_TARGET_TB_CODE = "target_tb_code";
    /**
     * 目标表描述
     */
    public static final String ALIAS_TARGET_DESCRIPT = "target_descript";
    /**
     * 执行SQL
     */
    public static final String ALIAS_SQLSTR = "sqlStr";
    /**
     * 是否启用
     */
    public static final String ALIAS_IS_ENABLE = "is_enable";
    /**
     * 是否需要自定义跑批任务
     */
    public static final String ALIAS_IS_DIY_QUARTZ = "is_diy_quartz";
    /**
     * 跑批类型
     */
    public static final String ALIAS_TYPE = "type";
    /**
     * 定时cronn表达式
     */
    public static final String ALIAS_CRON = "cron";
    /**
     * 备注
     */
    public static final String ALIAS_REMARK = "remark";
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
    public static final String ALIAS_LAST_UPDATE_DATE = "last_update_date";
    /**
     * 对比字段
     */
    public static final String ALIAS_COMPARE_PARAMETER = "compare_parameter";
    /**
     * 增量比较字段
     */
    public static final String ALIAS_INCREMENT_COLUMN = "increment_column";
    /**
     * 维护人姓名
     */
    public static final String ALIAS_VINDICATOR = "vindicator";
    /**
     * 维护人邮箱
     */
    public static final String ALIAS_VINDICATOR_MAIL = "vindicator_mail";

    /* 转换名称 */
    private String transName;

    /* 转换中文名称 */
    private String simpleName;

    /* 转换类型 */
    private String transType;

    /* 源数据源Id */
    private String srcDbId;

    /* 源数据源Code */
    private String srcDbCode;

    /* 源数据表Code */
    private String srcTbCode;

    /* 源表描述 */
    private String srcDescript;

    /* 目标数据源Id */
    private String targetDbId;

    /* 目标数据源Code */
    private String targetDbCode;

    /* 目标数据表Code */
    private String targetTbCode;

    /* 目标表描述 */
    private String targetDescript;

    /* 执行SQL */
    private String sqlstr;

    /* 对比字段 */
    private String compareParameter;

    /* 是否启用 */
    private String isEnable;

    /* 是否需要自定义跑批任务 */
    private String isDiyQuartz;

    /* 跑批类型 */
    private String type;

    /* 定时cronn表达式 */
    private String cron;

    /* 备注 */
    private String remark;

    /* 创建人 */
    private String owner;

    /* 创建时间 */
    private Date createTime;

    /* 最后更新时间 */
    private Date lastUpdateDate;

    /* 增量比较字段 */
    private String incrementColumn;

    private String batchNo;

    private String jobName;

    private PmDataSource pmDataSourceSrcInfo;

    private PmDataSource pmDataSourceTargetInfo;

    /* 维护人姓名 */
    private String vindicator;

    /* 维护人邮箱 */
    private String vindicatorMail;

    private String[] targetDBArray;
    private String[] srcDBArray;

    public String getTransName () {
        return transName;
    }

    public void setTransName (String transName) {
        this.transName = transName;
    }

    public String getSimpleName () {
        return simpleName;
    }

    public void setSimpleName (String simpleName) {
        this.simpleName = simpleName;
    }

    public String getTransType () {
        return transType;
    }

    public void setTransType (String transType) {
        this.transType = transType;
    }

    public String getSrcDbId () {
        return srcDbId;
    }

    public void setSrcDbId (String srcDbId) {
        this.srcDbId = srcDbId;
    }

    public String getSrcDbCode () {
        return srcDbCode;
    }

    public void setSrcDbCode (String srcDbCode) {
        this.srcDbCode = srcDbCode;
    }

    public String getSrcTbCode () {
        return srcTbCode;
    }

    public void setSrcTbCode (String srcTbCode) {
        this.srcTbCode = srcTbCode;
    }

    public String getSrcDescript () {
        return srcDescript;
    }

    public void setSrcDescript (String srcDescript) {
        this.srcDescript = srcDescript;
    }

    public String getTargetDbId () {
        return targetDbId;
    }

    public void setTargetDbId (String targetDbId) {
        this.targetDbId = targetDbId;
    }

    public String getTargetDbCode () {
        return targetDbCode;
    }

    public void setTargetDbCode (String targetDbCode) {
        this.targetDbCode = targetDbCode;
    }

    public String getTargetTbCode () {
        return targetTbCode;
    }

    public void setTargetTbCode (String targetTbCode) {
        this.targetTbCode = targetTbCode;
    }

    public String getTargetDescript () {
        return targetDescript;
    }

    public void setTargetDescript (String targetDescript) {
        this.targetDescript = targetDescript;
    }

    public String getSqlstr () {
        return sqlstr;
    }

    public void setSqlstr (String sqlstr) {
        this.sqlstr = sqlstr;
    }

    public String getIsEnable () {
        return isEnable;
    }

    public void setIsEnable (String isEnable) {
        this.isEnable = isEnable;
    }

    public String getIsDiyQuartz () {
        return isDiyQuartz;
    }

    public void setIsDiyQuartz (String isDiyQuartz) {
        this.isDiyQuartz = isDiyQuartz;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getCron () {
        return cron;
    }

    public void setCron (String cron) {
        this.cron = cron;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getOwner () {
        return owner;
    }

    public void setOwner (String owner) {
        this.owner = owner;
    }

    public Date getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateDate () {
        return lastUpdateDate;
    }

    public void setLastUpdateDate (Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getBatchNo () {
        return batchNo;
    }

    public void setBatchNo (String batchNo) {
        this.batchNo = batchNo;
    }

    public String getJobName () {
        return jobName;
    }

    public void setJobName (String jobName) {
        this.jobName = jobName;
    }

    public PmDataSource getPmDataSourceSrcInfo () {
        return pmDataSourceSrcInfo;
    }

    public void setPmDataSourceSrcInfo (PmDataSource pmDataSourceSrcInfo) {
        this.pmDataSourceSrcInfo = pmDataSourceSrcInfo;
    }

    public PmDataSource getPmDataSourceTargetInfo () {
        return pmDataSourceTargetInfo;
    }

    public void setPmDataSourceTargetInfo (PmDataSource pmDataSourceTargetInfo) {
        this.pmDataSourceTargetInfo = pmDataSourceTargetInfo;
    }

    public String getCompareParameter () {
        return compareParameter;
    }

    public void setCompareParameter (String compareParameter) {
        this.compareParameter = compareParameter;
    }

    public String[] getTargetDBArray () {
        return targetDBArray;
    }

    public void setTargetDBArray (String[] targetDBArray) {
        this.targetDBArray = targetDBArray;
    }

    public String[] getSrcDBArray () {
        return srcDBArray;
    }

    public void setSrcDBArray (String[] srcDBArray) {
        this.srcDBArray = srcDBArray;
    }

    public String getIncrementColumn () {
        return incrementColumn;
    }

    public void setIncrementColumn (String incrementColumn) {
        this.incrementColumn = incrementColumn;
    }

    public String getVindicator () {
        return vindicator;
    }

    public void setVindicator (String vindicator) {
        this.vindicator = vindicator;
    }

    public String getVindicatorMail () {
        return vindicatorMail;
    }

    public void setVindicatorMail (String vindicatorMail) {
        this.vindicatorMail = vindicatorMail;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	transName=").append(getTransName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	simpleName=").append(getSimpleName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	transType=").append(getTransType()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	srcDbId=").append(getSrcDbId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	srcDbCode=").append(getSrcDbCode()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	srcTbCode=").append(getSrcTbCode()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	srcDescript=").append(getSrcDescript()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	targetDbId=").append(getTargetDbId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	targetDbCode=").append(getTargetDbCode()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	targetTbCode=").append(getTargetTbCode()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	targetDescript=").append(getTargetDescript()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	sqlstr=").append(getSqlstr()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	isEnable=").append(getIsEnable()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	isDiyQuartz=").append(getIsDiyQuartz()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	cron=").append(getCron()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateDate=").append(DateUtils.dateTimeToString(getLastUpdateDate())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}