package com.yinker.etl.rabbitmq.receive.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

public class EtlIncrementMQService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EtlIncrementMQService.class);
	
	private TaskExecutor taskExecutor;
	
	public void handle(String message) {
		LOGGER.info("RabbitMQ接收数据...");
		JSONObject json = JSONObject.parseObject(message);
		EtlIncrementThreadService etlIncrementThreadService=new EtlIncrementThreadService(json);
		taskExecutor.execute(etlIncrementThreadService);
    }

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
