package com.yinker.etl.pm.model.base;

import org.springframework.stereotype.Component;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/* MYSQL TO MONGO跑批记录表 */
@Component("pmMongoTransferTableNameInfo")
public class PmMongoTransferTableNameInfo extends PmMongoTransferTableNameInfoPK {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 表Id
     */
    public static final String ALIAS_ID = "id";

    /**
     * 表名
     */
    public static final String ALIAS_TABLE_NAME = "table_name";

    /**
     * 是否自定义mysql数据留存时间
     */
    public static final String ALIAS_IS_DIY_RETENTION_TIME = "is_diy_retention_time";

    /**
     * 留存时间
     */
    public static final String ALIAS_RETENTION_TIME = "retention_time";

    /**
     * 留存时间对应字段名
     */
    public static final String ALIAS_RETENTION_COLUMN_NAME = "retention_column_name";

    /**
     * 跑批数量
     */
    public static final String ALIAS_STEP = "step";

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

    /* 表名 */
    private String tableName;

    /* 是否自定义mysql数据留存时间 */
    private String isDiyRetentionTime;

    /* 留存时间 */
    private Integer retentionTime;

    /* 留存时间对应字段名 */
    private String retentionColumnName;

    /* 跑批数量 */
    private Integer step;

    /* 是否删除 */
    private String isDelete;

    /* 状态 */
    private String state;

    /* 对应全路径 */
    private String classPath;

    /* 备注 */
    private String remark;

    /* 记录时间 */
    private Date createTime;

    /* 最后更新时间 */
    private Date lastUpdateTime;

    /* 操作员 */
    private String operator;


    @Override
    public String getId () {
        return id;
    }


    @Override
    public void setId (String id) {
        this.id = id;
    }


    public String getTableName () {
        return tableName;
    }


    public void setTableName (String tableName) {
        this.tableName = tableName;
    }

    public String getIsDiyRetentionTime() {
        return isDiyRetentionTime;
    }

    public void setIsDiyRetentionTime(String isDiyRetentionTime) {
        this.isDiyRetentionTime = isDiyRetentionTime;
    }

    public Integer getRetentionTime() {
        return retentionTime;
    }

    public void setRetentionTime(Integer retentionTime) {
        this.retentionTime = retentionTime;
    }

    public String getRetentionColumnName() {
        return retentionColumnName;
    }

    public void setRetentionColumnName(String retentionColumnName) {
        this.retentionColumnName = retentionColumnName;
    }

    public Integer getStep () {
        return step;
    }


    public void setStep (Integer step) {
        this.step = step;
    }


    public String getIsDelete () {
        return isDelete;
    }


    public void setIsDelete (String isDelete) {
        this.isDelete = isDelete;
    }


    public String getState () {
        return state;
    }


    public void setState (String state) {
        this.state = state;
    }


    public String getClassPath () {
        return classPath;
    }


    public void setClassPath (String classPath) {
        this.classPath = classPath;
    }


    public String getRemark () {
        return remark;
    }


    public void setRemark (String remark) {
        this.remark = remark;
    }


    public Date getCreateTime () {
        return createTime;
    }


    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }


    public Date getLastUpdateTime () {
        return lastUpdateTime;
    }


    public void setLastUpdateTime (Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }


    public String getOperator () {
        return operator;
    }


    public void setOperator (String operator) {
        this.operator = operator;
    }


    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("    id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    tableName=").append(getTableName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    isDiyRetentionTime=").append(getIsDiyRetentionTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    retentionTime=").append(getRetentionTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    retentionColumnName=").append(getRetentionColumnName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    step=").append(getStep()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    isDelete=").append(getIsDelete()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    state=").append(getState()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    classPath=").append(getClassPath()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    operator=").append(getOperator()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
