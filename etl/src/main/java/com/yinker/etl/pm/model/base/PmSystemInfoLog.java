package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.DateUtils;
import org.zwork.common.utils.SystemUtils;

import java.util.Date;

/**
 * <pre>
 * <b>类描述:</b>PmSystemInfoLog表的对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:39:08
 * </pre>
 */
public class PmSystemInfoLog extends PmSystemInfoLogPK {

    private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    public static final String ALIAS_SN = "sn";
    /**
     * 服务器名称
     */
    public static final String ALIAS_SERVER_NAME = "server_name";
    /**
     * ip
     */
    public static final String ALIAS_IP = "ip";
    /**
     * 系统名称
     */
    public static final String ALIAS_OS_NAME = "os_name";
    /**
     * 启动时间
     */
    public static final String ALIAS_START_TIME = "start_time";
    /**
     * 状态
     */
    public static final String ALIAS_STATUS = "status";
    /**
     * 销毁时间
     */
    public static final String ALIAS_DESTORY_TIME = "destory_time";

    /* 序列 */
    private String sn;

    /* 服务器名称 */
    private String serverName;

    /* ip */
    private String ip;

    /* 系统名称 */
    private String osName;

    /* 启动时间 */
    private Date startTime;

    /* 状态 */
    private String status;
    private String statusNow;

    /* 销毁时间 */
    private Date destoryTime;

    public String getSn () {
        return sn;
    }

    public void setSn (String sn) {
        this.sn = sn;
    }

    public String getServerName () {
        return serverName;
    }

    public void setServerName (String serverName) {
        this.serverName = serverName;
    }

    public String getIp () {
        return ip;
    }

    public void setIp (String ip) {
        this.ip = ip;
    }

    public String getOsName () {
        return osName;
    }

    public void setOsName (String osName) {
        this.osName = osName;
    }

    public Date getStartTime () {
        return startTime;
    }

    public void setStartTime (Date startTime) {
        this.startTime = startTime;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public Date getDestoryTime () {
        return destoryTime;
    }

    public void setDestoryTime (Date destoryTime) {
        this.destoryTime = destoryTime;
    }

    public String getStatusNow () {
        return statusNow;
    }

    public void setStatusNow (String statusNow) {
        this.statusNow = statusNow;
    }

    /**
     * toString方法
     */
    @Override
    public String toString () {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	sn=").append(getSn()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	serverName=").append(getServerName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	ip=").append(getIp()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	osName=").append(getOsName()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	startTime=").append(DateUtils.dateTimeToString(getStartTime())).append(SystemUtils.LINE_SEPARATOR);
        str.append("	status=").append(getStatus()).append(SystemUtils.LINE_SEPARATOR);
        str.append("	destoryTime=").append(getDestoryTime()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}