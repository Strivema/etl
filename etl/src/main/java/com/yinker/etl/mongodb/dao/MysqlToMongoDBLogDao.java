package com.yinker.etl.mongodb.dao;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>kettle转换日志Dao接口
 * <b>作者:</b>Lenovo
 * <b>创建日期:</b>2017年5月10日 下午12:05:04
 * </pre>
 */
public interface MysqlToMongoDBLogDao {
	
	/**
	 * Mysql查询日志数据
	 */
	public List<Map<String,Object>> selectMysql (String tableName, Integer step);

	/**
	 * Mongodb插入数据
	 */
	public void insert (Object tableObject, String collectionName);
	
	
	public void insertLogBatch (Map<String, Object> map);

	
	public void updateLogBatch (Map<String, Object> map);
	
	

}
