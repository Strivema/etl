package com.yinker.etl.rabbitmq.receive.service;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.trans.service.SyncTableInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.thirdparty.org.springframework.SpringBeanContext;

public class SyncTableInfoMQService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncTableInfoMQService.class);


	public void handle(String message) {
		LOGGER.info("RabbitMQ接收数据...");
		JSONObject json = JSONObject.parseObject(message);
		SyncTableInfoService syncTableInfoService = (SyncTableInfoService) SpringBeanContext.getBean("syncTableInfoService");
		try{
			syncTableInfoService.syncTableTrans(json);
		}catch (Exception e){
			LOGGER.error("表结构同步出现异常：{}",e.getMessage());
			e.printStackTrace();
		}

	}

}
