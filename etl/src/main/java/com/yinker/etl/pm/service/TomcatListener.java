package com.yinker.etl.pm.service;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.action.PmDatasourceGroupAction;
import com.yinker.etl.pm.model.base.PmSystemInfoLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * 此方法用于Tomcat的启动与停止更新pm_system_info
 */
public class TomcatListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmDatasourceGroupAction.class);

    private PmSystemInfoLogService pmSystemInfoLogService;

    private String IP;
    private String serverName;
    private String osName;


    @Override
    public void contextDestroyed (ServletContextEvent arg0) {
        LOGGER.info("Tomcat关闭：更新系统参数表");
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
        pmSystemInfoLogService = (PmSystemInfoLogService) springContext.getBean("pmSystemInfoLogService");
        try {
            getSystemPargram();
            LOGGER.info("更新系统信息表");
            PmSystemInfoLog pmSystemInfoLog = new PmSystemInfoLog();
            pmSystemInfoLog.setIp(IP);
            pmSystemInfoLog.setStatus(PMConstants.SYSTEM_INFO_LOG_STATUS_OFF);
            pmSystemInfoLog.setStatusNow(PMConstants.SYSTEM_INFO_LOG_STATUS_ON);
            pmSystemInfoLog.setDestoryTime(new Date());
            pmSystemInfoLogService.updateBatchByIP(pmSystemInfoLog);
            LOGGER.debug("更新系统表结束");
        } catch(Exception ex) {
            LOGGER.error("Tomcat 停止更新系统信息异常" + ex.getMessage());
            ex.getMessage();
        }
    }

    @Override
    public void contextInitialized (ServletContextEvent arg0) {
        LOGGER.info("Tomcat启动：初始化系统表参数");
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
        pmSystemInfoLogService = (PmSystemInfoLogService) springContext.getBean("pmSystemInfoLogService");
        try {
            getSystemPargram();
            LOGGER.info("更新系统信息表");
            PmSystemInfoLog pmSystemInfoLog = new PmSystemInfoLog();
            pmSystemInfoLog.setIp(IP);
            pmSystemInfoLog.setStatus(PMConstants.SYSTEM_INFO_LOG_STATUS_STOP);
            pmSystemInfoLog.setStatusNow(PMConstants.SYSTEM_INFO_LOG_STATUS_ON);
            pmSystemInfoLog.setDestoryTime(new Date());
            pmSystemInfoLogService.updateBatchByIP(pmSystemInfoLog);
            LOGGER.debug("更新系统表结束");

            LOGGER.info("创建系统信息");
            pmSystemInfoLog = new PmSystemInfoLog();
            pmSystemInfoLog.setSn(UUID.randomUUID().toString());
            pmSystemInfoLog.setIp(IP);
            pmSystemInfoLog.setStatus(PMConstants.SYSTEM_INFO_LOG_STATUS_ON);
            pmSystemInfoLog.setServerName(serverName);
            pmSystemInfoLog.setOsName(osName);
            pmSystemInfoLog.setStartTime(new Date());
            pmSystemInfoLogService.insert(pmSystemInfoLog);
        } catch(Exception ex) {
            LOGGER.error("Tomcat 启动初始化系统信息异常" + ex.getMessage());
            ex.getMessage();
        }

    }

    /**
     * 获得系统参数
     *
     * @throws UnknownHostException
     */
    private void getSystemPargram () throws Exception {
        LOGGER.info("开始获取系统参数信息");
        InetAddress address = InetAddress.getLocalHost();
        IP = address.getHostAddress();//192.168.0.121
        serverName = address.getHostName();
        Properties sysProperty = System.getProperties();
        osName = sysProperty.getProperty("os.name");
        LOGGER.info("IP---------->" + IP);
        LOGGER.info("serverName-->" + serverName);
        LOGGER.info("osName------>" + osName);
    }

}