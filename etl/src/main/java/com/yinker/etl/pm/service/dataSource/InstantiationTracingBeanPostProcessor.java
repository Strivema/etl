package com.yinker.etl.pm.service.dataSource;

import com.yinker.etl.pm.service.PmDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;

/**
 * Spring 加载完毕 后执行此类，用于加载多个数据源
 * Created by cuibowen 2017年11月20日15:00:59
 */
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantiationTracingBeanPostProcessor.class);

    @Resource(name = "pmDataSourceService")
    private PmDataSourceService pmDataSourceService;

    @Override
    public void onApplicationEvent (ContextRefreshedEvent event) {
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        if (event.getApplicationContext().getParent() == null) {//root application context 没有parent，他就是老大.
            pmDataSourceService.initDataSource();
        }
    }


}