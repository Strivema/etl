package com.yinker.etl.mongodb.dao.impl;

import com.yinker.etl.mongodb.dao.MysqlToMongoDBLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 日志导入到mongodb数据库
 * 
 * @author Lenovo
 */
@Component
public class MongoDBLogDaoImpl implements MysqlToMongoDBLogDao{
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@Override
    public void insert(Object tableObject, String collectionName) {
		mongoTemplate.insert(tableObject,collectionName);
	}


	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void insertLogBatch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogBatch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Map<String, Object>> selectMysql(String tableName, Integer step) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
