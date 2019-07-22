package com.yinker.etl.pm.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.dao.PmDataSourceDao;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.model.base.PmDataSourcePK;
import com.yinker.etl.pm.service.dataSource.DynamicDataSource;
import com.yinker.etl.pm.util.AESUtil;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import com.yinker.etl.trans.util.KettleDataSorceInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.zwork.framework.base.support.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>PmDataSource表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:43
 * </pre>
 */
public class PmDataSourceService extends BaseService<PmDataSourcePK, PmDataSource, PmDataSourceDao> implements
        org.zwork.framework.base.BaseService<PmDataSourcePK, PmDataSource> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmDataSourceService.class);

    @Value("%{AESKey}")
    private String AESKey;

    public void clearDataSourceAndInit(){
        DynamicDataSource.getInstance().setTargetDataSources(new HashMap<>());
        initDataSource();
    }

    public void initDataSource(){
        try {
            Map<Object, Object> map = new HashMap<>();
            List<PmDataSource> dataSources = this.selectAll();
            for(PmDataSource dataSource : dataSources) {
                LOGGER.info("加载数据源：{}", dataSource.getCode());
                DruidDataSource dds = getDDSByDataSource(dataSource);
                dds.init();
                map.put(dataSource.getCode(), dds);
            }
            DynamicDataSource.getInstance().setTargetDataSources(map);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void initDataSource (PmDataSource dataSource) {
        try {
            Map<Object, Object> map = new HashMap<>();
            LOGGER.info("加载数据源：{}", dataSource.getCode());
            DruidDataSource dds = getDDSByDataSource(dataSource);
            dds.init();
            map.put(dataSource.getCode(), dds);
            DynamicDataSource.getInstance().setTargetDataSources(map);
            SimpleTrans.setDatabaseMetaList(KettleDataSorceInit.initDataSource());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public DruidDataSource getDDSByDataSource(PmDataSource dataSource) throws Exception {
        DruidDataSource dds = new DruidDataSource();

        if (PMConstants.DATA_BASE_TYPE_ORACLE.equals(dataSource.getDatabaseType())) {
            dds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            dds.setUrl("jdbc:oracle:" + "thin:@" + dataSource.getHostName() + ":" + dataSource.getPort() + ":" + dataSource.getDatabaseName());
            dds.setValidationQuery("SELECT 'x'  FROM DUAL");
        }
        if (PMConstants.DATA_BASE_TYPE_MYSQL.equals(dataSource.getDatabaseType())) {
            dds.setDriverClassName("com.mysql.jdbc.Driver");
            dds.setUrl("jdbc:mysql://" + dataSource.getHostName() + ":" + dataSource.getPort() + "/" + dataSource.getDatabaseName() + "?useUnicode=true&characterEncoding=utf-8");
            dds.setValidationQuery("SELECT 'x'");
        }
        dds.setUsername(dataSource.getUserName());
        dds.setPassword(AESUtil.decrypt(AESKey, dataSource.getPassword()).trim());
        dds.setInitialSize(1);
        dds.setMinIdle(1);
        dds.setMaxActive(20);
        dds.setTestWhileIdle(true);
        dds.setTestOnBorrow(false);
        dds.setTestOnReturn(false);
        return dds;
    }

    /**
     * 获取所有的数据源集合
     * @return
     */
    public Map<String,PmDataSource> getDataSourceMap(){
        Map<String,PmDataSource> dataSourceMap = new HashMap<>();

        List<PmDataSource> dataSourceList = selectAll();
        for(PmDataSource dataSource : dataSourceList) {
            dataSourceMap.put(dataSource.getCode(),dataSource);
        }

        return dataSourceMap;
    }

}
