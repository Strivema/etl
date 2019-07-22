package com.yinker.etl.pm.model;

import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfo;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>PmMongoTransferTableNameInfo表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * </pre>
 */
public class PmMongoTransferTableNameInfoQuery extends PmMongoTransferTableNameInfo {

    private static final long serialVersionUID = 1L;


    /* 区间查询条件:记录日期-开始 */
    private Date createTimeStart;

    /* 区间查询条件:记录日期-结束 */
    private Date createTimeEnd;

    public Date getCreateTimeStart () {
        return createTimeStart;
    }


    public void setCreateTimeStart (Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }


    public Date getCreateTimeEnd () {
        return createTimeEnd;
    }


    public void setCreateTimeEnd (Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }


    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    tableName=").append(getTableName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}