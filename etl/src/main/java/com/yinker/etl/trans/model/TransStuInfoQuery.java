package com.yinker.etl.trans.model;

import com.yinker.etl.trans.model.base.TransStuInfo;
import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

public class TransStuInfoQuery  extends TransStuInfo{

    private static final long serialVersionUID = 1L;

 /*   private Date createTimeStart;

    private Date createTimeEnd;

    private Date lastUpdateTimeStart;

    private Date lastUpdateTimeEnd;

    private Integer selectCount;

    public Date getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getLastUpdateTimeStart() {
        return lastUpdateTimeStart;
    }

    public void setLastUpdateTimeStart(Date lastUpdateTimeStart) {
        this.lastUpdateTimeStart = lastUpdateTimeStart;
    }

    public Date getLastUpdateTimeEnd() {
        return lastUpdateTimeEnd;
    }

    public void setLastUpdateTimeEnd(Date lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public Integer getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(Integer selectCount) {
        this.selectCount = selectCount;
    }
*/
    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("    stuNo=").append(getStuNo()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuName=").append(getStuName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuGender=").append(getStuGender()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuClazz=").append(getStuClazz()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuMajor=").append(getStuMajor()).append(SystemUtils.LINE_SEPARATOR);
       // str.append("	createTimeStart=").append(DateUtils.dateTimeToString(getCreateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
      // str.append("	createTimeEnd=").append(DateUtils.dateTimeToString(getCreateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
      //  str.append("	lastUpdateTimeStart=").append(DateUtils.dateTimeToString(getLastUpdateTimeStart())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
       // str.append("	lastUpdateTimeEnd=").append(DateUtils.dateTimeToString(getLastUpdateTimeEnd())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
