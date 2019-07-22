package com.yinker.etl.pm.model;

import com.yinker.etl.pm.model.base.PmMongoTransferBatchLog;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>PmMongoTransferBatchLog表的对应的查询对象，增加Date/Double/int型字段的区间查询属性字段。
 * </pre>
 */
public class PmMongoTransferBatchLogQuery extends PmMongoTransferBatchLog {

    private static final long serialVersionUID = 1L;

    /* 模糊查询条件:id里的时间字段 */
    private String idDate;
    /* 区间查询条件:记录日期-开始 */
    private Date errCreateTimeStart;

    /* 区间查询条件:记录日期-结束 */
    private Date errCreateTimeEnd;


    public String getIdDate () {
        return idDate;
    }

    public void setIdDate (String idDate) {
        this.idDate = idDate;
    }

    public Date getErrCreateTimeStart () {
        return errCreateTimeStart;
    }

    public void setErrCreateTimeStart (Date errCreateTimeStart) {
        this.errCreateTimeStart = errCreateTimeStart;
    }

    public Date getErrCreateTimeEnd () {
        return errCreateTimeEnd;
    }

    public void setErrCreateTimeEnd (Date errCreateTimeEnd) {
        this.errCreateTimeEnd = errCreateTimeEnd;
    }


    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    batchId=").append(getBatchId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errCreateTimeStart=").append(DateUtils.dateTimeToString(getErrCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	errCreateTimeEnd=").append(DateUtils.dateTimeToString(getErrCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("    remark=").append(getRemark()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}