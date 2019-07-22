package com.yinker.etl.trans.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

public class TransStuInfo extends TransStuInfoPK {

    private static final long serialVersionUID = 1L;



    private Integer stuNo;

    private String stuName;

    private String stuGender;

    private String stuClazz;

    private String stuMajor;

    private Date stuBirth;

    private Date stuEnrolTime;

    private Date createTime;

    private Date lastUpdateTime;

    public Integer getStuNo() {
        return stuNo;
    }

    public void setStuNo(Integer stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuGender() {
        return stuGender;
    }

    public void setStuGender(String stuGender) {
        this.stuGender = stuGender;
    }

    public String getStuClazz() {
        return stuClazz;
    }

    public void setStuClazz(String stuClazz) {
        this.stuClazz = stuClazz;
    }

    public String getStuMajor() {
        return stuMajor;
    }

    public void setStuMajor(String stuMajor) {
        this.stuMajor = stuMajor;
    }

    public Date getStuBirth() {
        return stuBirth;
    }

    public void setStuBirth(Date stuBirth) {
        this.stuBirth = stuBirth;
    }

    public Date getStuEnrolTime() {
        return stuEnrolTime;
    }

    public void setStuEnrolTime(Date stuEnrolTime) {
        this.stuEnrolTime = stuEnrolTime;
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
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuNo=").append(getStuNo()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuName=").append(getStuName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuGender=").append(getStuGender()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuClazz=").append(getStuClazz()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuMajor=").append(getStuMajor()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuBirth=").append(getStuBirth()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	stuEnrolTime=").append(getStuEnrolTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateDate=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }


}