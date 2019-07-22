package com.yinker.etl.pm.service.dataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态切换数据库
 * Created by cuibowen 2017年11月20日15:00:59
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    private static DynamicDataSource instance;
    private static byte[] lock = new byte[0];
    private static Map<Object, Object> dataSourceMap = new HashMap<>();


    @Override
    public void setTargetDataSources (Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        //重要的方法,一定要加上不然会出现动态添加数据源的时候无法生效的情况
        super.afterPropertiesSet();
    }

    public static synchronized DynamicDataSource getInstance () {
        if (instance == null) {
            synchronized(lock) {
                if (instance == null) {
                    instance = new DynamicDataSource();
                }
            }
        }
        return instance;
    }

    //必须实现其方法
    @Override
    protected Object determineCurrentLookupKey () {
        Object dataSourceKey = DataSourceContextHolder.getDataSourceKey();
        if (dataSourceKey != null) {
            LOGGER.info("采用数据源[dataSourceKey={}]", dataSourceKey);
        } else {
            LOGGER.debug("采用默认数据源!");
        }

        return dataSourceKey;
    }

    public Map<Object, Object> getDataSourceMap () {
        return dataSourceMap;
    }
}
