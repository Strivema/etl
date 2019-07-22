package com.yinker.etl.rabbitmq.receive.service;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.model.base.TransTableStructureInfoPK;
import com.yinker.etl.trans.service.BatchTransTableStructureInfoService;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;

public class TableStructureIncrementMQService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableStructureIncrementMQService.class);

	@Resource(name = "transTableStructureInfoService")
	private TransTableStructureInfoService transTableStructureInfoService;

	@Resource(name = "batchTransTableStructureInfoService")
	private BatchTransTableStructureInfoService batchTransTableStructureInfoService;

	private TaskExecutor taskExecutor;

	public void handle (String message) {
		LOGGER.info("MQ接收消息类：自定义结构抽取 - 增量抽取");
		try {
			JSONObject json = JSONObject.parseObject(message);
			String transId = json.get("id").toString();
			String batchNo = json.get("batchNo").toString();
			String jobName = json.get("jobName").toString();

			TransTableStructureInfoPK pk = new TransTableStructureInfoPK();
			pk.setId(transId);
			TransTableStructureInfo transInfo = transTableStructureInfoService.selectByPK(pk);
			if (transInfo != null) {
				batchTransTableStructureInfoService.incrementExcute(transInfo, jobName, batchNo);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("自定义结构抽取 - 增量抽取 异常信息 :" + ex.getMessage());
		}

	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
