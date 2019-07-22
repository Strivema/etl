package com.util.test;

import com.yinker.etl.pm.util.DataSourceUtil;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 崔博文 on 2017/11/6.15:05
 */
public class DataSourceUtilTest {

    /**
     * 测试mysql
     */
    @Test
    public void testMysqlConnect () {
        Connection con = null;
        try {
            // mysql 链接方式
            con = DataSourceUtil.getMysqlConnection("10.103.60.149", "3306", "etl_test", "etl_test", "etl");
            System.out.println();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            // 使用结束后别忘记关闭资源
            try {
                con.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试oracle
     */
    @Test
    public void testOracleConnect () {
        Connection con = null;
        try {
            // oracle 链接方式
            con = DataSourceUtil.getOracleConnection("10.103.61.23", "1523", "oauser", "oauser", "UAT");
            System.out.println();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            // 使用结束后别忘记关闭资源
            try {
                con.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据源下的所有表名字
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void testShowTable () throws SQLException, ClassNotFoundException {
        Connection con = DataSourceUtil.getMysqlConnection("10.103.60.149", "3306", "etl_test", "etl_test", "etl");
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(null, null, null,
                    new String[]{"TABLE"});
            while(rs.next()) {
                System.out.println("表名：" + rs.getString(3));
                System.out.println("表所属用户名：" + rs.getString(2));
                System.out.println("------------------------------");
            }
            con.close();
        } catch(Exception e) {
            try {
                con.close();
            } catch(SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testKettle () throws KettleException {
        KettleEnvironment.init();
        DatabaseMeta dbm = new DatabaseMeta("DB", "Oracle", "Native", "10.103.61.23", "UAT", "1523", "oauser", "oauser");
/*
        DatabaseMeta dbm= new DatabaseMeta("DB", "MySQL","Native","10.103.60.149","etl","3306","etl_test","etl_test");
*/
        System.out.println(dbm);
    }

    @Test
    public void testKettle1 () throws KettleException {
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta();
    }


}
