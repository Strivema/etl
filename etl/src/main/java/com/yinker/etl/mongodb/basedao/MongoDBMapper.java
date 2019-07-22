package com.yinker.etl.mongodb.basedao;

import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>MongoDB的Mapper配置
 * <b>作者:</b>李得亮
 * <b>创建日期:</b>2016年11月28日 下午4:57:04
 * </pre>
 */
public class MongoDBMapper {

	/**
	 * MongDB的集合名称（对应数据库中的表名）
	 */
	private String collectionName;

	/**
	 * 结果集与对象属性名称的映射表
	 */
	private Map<String, Map<String, String>> resultMap;

	/**
	 * MongoDB数据库操作的sql
	 */
	private Map<String, MongoDBMapperSql> sql;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Map<String, Map<String, String>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Map<String, String>> resultMap) {
		this.resultMap = resultMap;
	}

	public Map<String, MongoDBMapperSql> getSql() {
		return sql;
	}

	public void setSql(Map<String, MongoDBMapperSql> sql) {
		this.sql = sql;
	}

}
