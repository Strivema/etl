package com.yinker.etl.trans.util;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.action.PmDataSourceAction;
import com.yinker.etl.pm.model.PmDataSourceQuery;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.pm.util.AESUtil;
import com.yinker.etl.pm.util.FileUtil;
import com.yinker.etl.trans.TransConstants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.zwork.framework.thirdparty.org.springframework.SpringBeanContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KettleDataSorceInit {

    private static final String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * 初始化Kettle
     */
    static {
        try {
            // 设置Kettle的初始化配置信息路径
            String user_dir = System.getProperty("user.dir");
            String kettleHome = KettleDataSorceInit.class.getClassLoader().getResource("").getPath();
            kettleHome = kettleHome.substring(0,kettleHome.lastIndexOf("classes")-1);
            System.out.println("-----------------------------------------" + user_dir);
            System.out.println("-----------------------------------------" + kettleHome);
            // Kettle初始化需要修改相应的配置路径
            System.setProperty("user.dir", kettleHome);
            System.setProperty("KETTLE_HOME", kettleHome);
            // 运行环境初始化（设置主目录、注册必须的插件等）
            KettleEnvironment.init();
            // Kettle初始化完毕，还原执行类的当前路径
            System.setProperty("user.dir", user_dir);
        } catch(KettleException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Kettle的初始化配置信息路径
     *
     * @throws KettleException
     */
    private static void initKettleEnvironment () throws KettleException {
        // 获得执行类的当前路径

    }

    /**
     * 加载xml文件
     *
     * @return
     */
    public static Document getXml () {
        SAXReader reader = new SAXReader();
        InputStream in = KettleDataSorceInit.class.getResourceAsStream("/etl_dataSorce.xml");

        Document document = null;
        try {
            document = reader.read(in);
        } catch(DocumentException e) {
            e.printStackTrace();
        }

        return document;
    }

    /**
     * 加载所有kettle转换数据源xml配置文件
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getDataSorces () {

        List<String> dataSourceList = new ArrayList<String>();
        try {
            Document document = getXml();
            Element root = document.getRootElement();
            List<Element> elelist = root.elements();
            for(int i = 1; i < elelist.size(); i++) {
                Element ele = elelist.get(i);
                dataSourceList.add(xmlHead + ele.asXML());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return dataSourceList;
    }

    /**
     * 加载kettle资源库元数据源xml配置文件
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getDataSorce () {
        String dataSourceList = "";
        try {
            Document document = getXml();
            Element root = document.getRootElement();
            List<Element> elelist = root.elements();
            if (elelist.size() > 0) {
                dataSourceList = xmlHead + elelist.get(0).asXML();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return dataSourceList;
    }

    /**
     * 初始化kettle元数据
     * @return
     */
    /*public static List<DatabaseMeta> initDataSource(){
		List<DatabaseMeta> databaseMetaList = new ArrayList<>();
		try {
			//添加的数据库连接
			List<String> dataSourceList = getDataSorces();
	        for (int i=0;i<dataSourceList.size();i++){
	            DatabaseMeta databaseMeta = new DatabaseMeta(dataSourceList.get(i));
	            databaseMeta.addOptions();
	            databaseMeta.addExtraOption("MYSQL", "characterEncoding", "UTF-8");
	            databaseMeta.addExtraOption("MYSQL", "useSSL", "false");
	            databaseMetaList.add(databaseMeta);
	        }
		} catch (KettleException e) {
			e.printStackTrace();
		}
		return databaseMetaList;
	}*/

    /**
     * 初始化kettle元数据
     *
     * @return
     */
    public static List<DatabaseMeta> initDataSource () {
        PmDataSourceService dataSourceService = (PmDataSourceService) SpringBeanContext.getBean("pmDataSourceService");
        List<PmDataSource> dataSources = dataSourceService.selectAll();
        List<DatabaseMeta> databaseMetaList = new ArrayList<>();
        try {
            for(PmDataSource dataSource : dataSources) {
                DatabaseMeta databaseMeta = null;
                if (PMConstants.DATA_BASE_TYPE_MYSQL.equals(dataSource.getDatabaseType())) {
                    databaseMeta = new DatabaseMeta(dataSource.getCode(), "MySQL", "Native", dataSource.getHostName(), dataSource.getDatabaseName(), dataSource.getPort() + "", dataSource.getUserName(), getDescPassword(dataSource.getPassword()));
                    databaseMeta.addOptions();
                    databaseMeta.addExtraOption("MYSQL", "characterEncoding", "UTF-8");
                    databaseMeta.addExtraOption("MYSQL", "useSSL", "false");
                } else if (PMConstants.DATA_BASE_TYPE_ORACLE.equals(dataSource.getDatabaseType())) {
                    databaseMeta = new DatabaseMeta(dataSource.getCode(), "Oracle", "Native", dataSource.getHostName(), dataSource.getDatabaseName(), dataSource.getPort() + "", dataSource.getUserName(), getDescPassword(dataSource.getPassword()));
                }
                databaseMetaList.add(databaseMeta);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return databaseMetaList;
    }

    /**
     * 初始化kettle资源库元数据
     *
     * @return
     */
    public static KettleDatabaseRepository initDatabaseMeta () {

        // 创建资源库对象，此时的对象还是一个空对象
        KettleDatabaseRepository rep = new KettleDatabaseRepository();
        try {
            DatabaseMeta databaseMeta = getKettleDataBaseMeta();

            // 创建资源库数据库对象，类似我们在spoon里面创建资源库
            KettleDatabaseRepositoryMeta repInfo = new KettleDatabaseRepositoryMeta();
            // 链接资源库元对象
            repInfo.setConnection(databaseMeta);

            // 给资源库赋值
            rep.init(repInfo);
            // 连接资源库
            Properties properties = null;
            try {
                properties = FileUtil.getPropertiesByFile(KettleDataSorceInit.class.getClassLoader().getResource("/").getPath() + "etl_config.properties");
            } catch(IOException e) {
                e.printStackTrace();
            }
            rep.connect((String) properties.get("KETTLE_USER_NAME"), (String) properties.get("KETTLE_PASSWORD"));
        } catch(KettleXMLException e) {
            e.printStackTrace();
        } catch(KettleException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return rep;
    }

    private static String getDescPassword (String pwd) throws Exception {
        Properties properties = FileUtil.getPropertiesByFile(PmDataSourceAction.class.getClassLoader().getResource("/").getPath() + "etl_config.properties");
        String AESKey = (String) properties.get("AESKey");
        return AESUtil.decrypt(AESKey, pwd).trim();
    }

    private static DatabaseMeta getKettleDataBaseMeta () throws Exception {
        PmDataSourceService dataSourceService = (PmDataSourceService) SpringBeanContext.getBean("pmDataSourceService");
        DatabaseMeta databaseMeta = null;
        PmDataSourceQuery query = new PmDataSourceQuery();
        query.setCode(TransConstants.KETTLE_DATABASE_NAME);
        List<PmDataSource> pmDataSources = dataSourceService.selectByEntity(query);
        if (pmDataSources == null || pmDataSources.size() == 0) {
            throw new Exception("在表pm_data_srouce中没有存在code为" + TransConstants.KETTLE_DATABASE_NAME + "的数据源");
        } else {
            databaseMeta = new DatabaseMeta(pmDataSources.get(0).getCode(), "MySQL", "Native", pmDataSources.get(0).getHostName(),
                    pmDataSources.get(0).getDatabaseName(), pmDataSources.get(0).getPort() + "", pmDataSources.get(0).getUserName(), getDescPassword(pmDataSources.get(0).getPassword()));
            databaseMeta.addOptions();
            databaseMeta.addExtraOption("MYSQL", "characterEncoding", "UTF-8");
            databaseMeta.addExtraOption("MYSQL", "useSSL", "false");
        }
        return databaseMeta;
    }

}
