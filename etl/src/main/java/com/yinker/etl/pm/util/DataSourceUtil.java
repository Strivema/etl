package com.yinker.etl.pm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 崔博文 on 2017/11/3.16:49
 */
public class DataSourceUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceUtil.class);


    /**
     * 获取mysql链接
     * @param address   地址
     * @param port      端口
     * @param user      用户名
     * @param password  密码
     * @param dataTableName 数据库名
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getMysqlConnection(String address,String port,String user,String password,String dataTableName) throws ClassNotFoundException, SQLException {
        Connection con = null;// 创建一个数据库连接
        Class.forName("com.mysql.jdbc.Driver");
        LOGGER.info("开始尝试连接Mysql数据库！");
        String url="jdbc:mysql://"+address+":"+port+"/"+dataTableName;
        LOGGER.debug("链接的URL为"+url);
        Connection conn= DriverManager.getConnection(url,user,password);
        LOGGER.info("连接成功！");
        return conn;
    }

    /**
     * 获取Oracle链接
     * @param address
     * @param port
     * @param user
     * @param password
     * @param SID
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getOracleConnection (String address, String port, String user, String password, String SID) throws ClassNotFoundException, SQLException {
        Connection con = null;// 创建一个数据库连接
        Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
        LOGGER.info("开始尝试连接Oracle数据库！");
        String url = "jdbc:oracle:" + "thin:@" + address + ":" + port + ":" + SID;
        LOGGER.debug("链接的URL为"+url);
        con = DriverManager.getConnection(url, user, password);// 获取连接
        LOGGER.info("连接成功！");
        return con;
    }

}