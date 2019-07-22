package com.yinker.etl.rabbitmq.receive.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.mongodb.dao.impl.MongoDBLogDaoImpl;
import com.yinker.etl.pm.model.base.PmMongoTransferBatchLog;
import com.yinker.etl.pm.service.PmMongoTransferBatchLogService;
import com.yinker.etl.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * @author YKDZ4351612
 * RabbitMQ接收数据，并插入Mongo
 *
 */
public class TransMysqlToMongoMQService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransMysqlToMongoMQService.class);
	
	@Resource(name = "mongoDBLogDaoImpl")
	private MongoDBLogDaoImpl mongoDBLogDaoImpl;
	
	/* 定义业务实体对象，用于新增/修改/查看 */
	@Resource(name = "pmMongoTransferBatchLog")
	protected PmMongoTransferBatchLog pmMongoTransferBatchLogEntity;
	
	/* 定义服务层操作类 */
	@Resource(name = "pmMongoTransferBatchLogService")
	protected PmMongoTransferBatchLogService pmMongoTransferBatchLogService;
	
	public void handle(String message) throws ParseException {
		LOGGER.info("RabbitMQ接收MYSQL数据...");
		JSONArray jsonarray = JSONArray.parseArray(message);
		String tableName = jsonarray.getJSONObject(jsonarray.size() - 1).getString("tableName"); //获取对应表名
		String classPath = jsonarray.getJSONObject(jsonarray.size() - 1).getString("classPath");
		jsonarray.remove(jsonarray.size() - 1); //删除jsonarray中的表名等属性
		DBObject document = new BasicDBObject();
		try {
			Class objClass = BeanUtil.newClass(classPath);
			for (int i = 0; i < jsonarray.size(); i++) {
				String json = jsonarray.getJSONObject(i).toString();

				/*document = (DBObject) com.mongodb.util.JSON.parse(json);
				mongoDBLogDaoImpl.insert(document, tableName);*/
				Object obj = JSON.parseObject(json, objClass);
				mongoDBLogDaoImpl.insert(obj, tableName);
			}
		} catch (Exception e) {
			exceptionAndErrHandle("异常", e.toString());
			e.printStackTrace();
		} catch (Error e) {
			exceptionAndErrHandle("错误", e.toString());
			e.printStackTrace();
		}
    }

    private void exceptionAndErrHandle(String err,String errorMessage) throws ParseException {
		LOGGER.info("RabbitMQ向Mongo插入操作失败,产生" + err);
		long errorCreateTime = System.currentTimeMillis(); //记录错误产生时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		errorMessage = errorMessage.replaceAll("'", "''");
		if(errorMessage.length() > 1024){
			errorMessage = errorMessage.substring(0, 1023);
		}
		pmMongoTransferBatchLogEntity.setBoolException("异常");
		pmMongoTransferBatchLogEntity.setLastUpdateTime(sdf.parse(sdf.format(errorCreateTime)));
		pmMongoTransferBatchLogEntity.setRemark(errorMessage);
		pmMongoTransferBatchLogService.updateByPK(pmMongoTransferBatchLogEntity);//记录错误信息
	}
}
