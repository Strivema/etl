package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>PmMailTemplate表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:01
 * </pre>
 */
public class PmMailTemplate extends PmMailTemplatePK {

    private static final long serialVersionUID = 1L;
    /**
     * 发送服务器编号
     */
    public static final String ALIAS_SERVER_ID = "server_id";
    /**
     * Code
     */
    public static final String ALIAS_CODE = "code";
    /**
     * 发件人名称
     */
    public static final String ALIAS_ADDRESSER = "addresser";
    /**
     * 标题
     */
    public static final String ALIAS_TITLE = "title";
    /**
     * 内容
     */
    public static final String ALIAS_BODY = "body";
    /**
     * 状态
     */
    public static final String ALIAS_STATUS = "status";
    /**
     * 描述
     */
    public static final String ALIAS_DESCRIPTION = "description";
    /**
     * 创建者
     */
    public static final String ALIAS_OWNER = "owner";
    /**
     * 最后更新时间
     */
    public static final String ALIAS_LAST_UPDATE_TIME = "last_update_time";
    /**
     * 创建时间
     */
    public static final String ALIAS_CREATE_TIME = "create_time";

    /* 发送服务器编号 */
    private String serverId;

    /* Code */
    private String code;

    /* 发件人名称 */
    private String addresser;

    /* 标题 */
    private String title;

    /* 内容 */
    private String body;

    /* 状态 */
    private String status;

    /* 描述 */
    private String description;

    /* 创建者 */
    private String owner;

    /* 最后更新时间 */
    private Date lastUpdateTime;

    /* 创建时间 */
    private Date createTime;

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getAddresser () {
        return addresser;
    }

    public void setAddresser (String addresser) {
        this.addresser = addresser;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getBody () {
        return body;
    }

    public void setBody (String body) {
        this.body = body;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getOwner () {
        return owner;
    }

    public void setOwner (String owner) {
        this.owner = owner;
    }

    public Date getLastUpdateTime () {
        return lastUpdateTime;
    }

    public void setLastUpdateTime (Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }

    public String getServerId () {
        return serverId;
    }

    public void setServerId (String serverId) {
        this.serverId = serverId;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	code=").append(getCode()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	addresser=").append(getAddresser()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	title=").append(getTitle()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	body=").append(getBody()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	description=").append(getDescription()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	owner=").append(getOwner()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	lastUpdateTime=").append(DateUtils.dateTimeToString(getLastUpdateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	createTime=").append(DateUtils.dateTimeToString(getCreateTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}