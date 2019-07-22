package com.yinker.etl.rabbitmq.receive.service;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.trans.service.BatchTransInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.thirdparty.org.springframework.SpringBeanContext;

public class EtlIncrementThreadService implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EtlIncrementThreadService.class);
	
	private JSONObject json;
	
	public EtlIncrementThreadService (){
	}
	
	public EtlIncrementThreadService (JSONObject json){
		this.json=json;
	}
	
	@Override
	public void run() {
		LOGGER.info("多线程执行JSON------->"+json);
		BatchTransInfoService transInfoService=(BatchTransInfoService) SpringBeanContext.getBean("batchTransInfoService");;
		transInfoService.tableDataTrans(json);
	}
	
}
