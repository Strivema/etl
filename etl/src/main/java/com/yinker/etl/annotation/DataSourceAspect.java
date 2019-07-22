package com.yinker.etl.annotation;

import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * spring aop切入注解切换数据源为从库
 * Created by cuibowen 2017年11月20日15:00:59
 */
public class DataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);

    @Override
    public void afterReturning (Object returnValue, Method method,Object[] args, Object target) throws Throwable {
        DataSourceContextHolder.clearDataSourceKey();
        LOGGER.info("清空数据源！");
    }

    @Override
    public void before (Method method, Object[] args, Object target)
            throws Throwable {
        if (method.isAnnotationPresent(DataSourceSlave.class)) {
            DataSourceSlave datasource = method.getAnnotation(DataSourceSlave.class);
            DataSourceContextHolder.setDataSourceKey(datasource.value());
        } else {
            LOGGER.info("设置默认数据源！");
            DataSourceContextHolder.setDataSourceKey(DataSourceSlave.defaultDataSource);
        }
    }
}
