package com.yinker.etl.pm.service.dataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;

/**
 * 数据库切换类
 * Created by cuibowen 2017年11月20日15:00:59
 */
public class DataSourceContextHolder {
    private static Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);
    private static final ThreadLocal<String> contextHolder = new ThreadLocal();
    private static final String DEFAULT_DATASOURCE = "etl";

    public DataSourceContextHolder () {
    }

    public static void setDataSourceKey (String dataSourceKey) {
        contextHolder.set(dataSourceKey);
    }

    public static String getDataSourceKey () {
        String key = contextHolder.get();
        if(StringUtils.isEmpty(key)){
            logger.debug("数据源为空，采取默认数据源"+DEFAULT_DATASOURCE);
            key = DEFAULT_DATASOURCE;

        }
        logger.info("Thread:" + Thread.currentThread().getName() + "dataSource key is " + key);
        return key;
    }

    public static void clearDataSourceKey () {
        contextHolder.remove();
    }

}
