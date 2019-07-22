package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>PmErrorTableStructureLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-13 11:03:59
 * </pre>
 */
public class PmErrorTableStructureLog extends PmErrorTableStructureLogPK {

    private static final long serialVersionUID = 1L;
    /**
     * err_ttrans_id
     */
    public static final String ALIAS_ERR_TRANS_ID = "err_trans_id";
    /**
     * err_trans_name
     */
    public static final String ALIAS_ERR_TRANS_NAME = "err_trans_name";

    /**
     * err_table_name
     */
    public static final String ALIAS_ERR_TABLE_NAME = "err_table_name";
    /**
     * err_column_name
     */
    public static final String ALIAS_ERR_COLUMN_NAME = "err_column_name";
    /**
     * err_status
     */
    public static final String ALIAS_ERR_STATUS = "err_status";
    /**
     * err_create_time
     */
    public static final String ALIAS_ERR_CREATE_TIME = "err_create_time";
    /**
     * err_desc
     */
    public static final String ALIAS_ERR_DESC = "err_desc";
    /**
     * remark
     */
    public static final String ALIAS_REMARK = "remark";

    /* err_trans_id */
    private String errTransId;

    /* err_trans_name */
    private String errTransName;
    /* err_table_name */
    private String errTableName;

    /* err_column_name */
    private String errColumnName;

    /* err_status */
    private String errStatus;

    /* err_create_time */
    private Date errCreateTime;

    /* err_desc */
    private String errDesc;

    /* remark */
    private String remark;

    public String getErrTableName () {
        return errTableName;
    }

    public void setErrTableName (String errTableName) {
        this.errTableName = errTableName;
    }

    public String getErrColumnName () {
        return errColumnName;
    }

    public void setErrColumnName (String errColumnName) {
        this.errColumnName = errColumnName;
    }

    public String getErrStatus () {
        return errStatus;
    }

    public void setErrStatus (String errStatus) {
        this.errStatus = errStatus;
    }

    public Date getErrCreateTime () {
        return errCreateTime;
    }

    public void setErrCreateTime (Date errCreateTime) {
        this.errCreateTime = errCreateTime;
    }

    public String getErrDesc () {
        return errDesc;
    }

    public void setErrDesc (String errDesc) {
        this.errDesc = errDesc;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getErrTransId () {
        return errTransId;
    }

    public void setErrTransId (String errTransId) {
        this.errTransId = errTransId;
    }

    public String getErrTransName () {
        return errTransName;
    }

    public void setErrTransName (String errTransName) {
        this.errTransName = errTransName;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errTableName=").append(getErrTableName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errColumnName=").append(getErrColumnName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errStatus=").append(getErrStatus()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errCreateTime=").append(DateUtils.dateTimeToString(getErrCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errDesc=").append(getErrDesc()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}