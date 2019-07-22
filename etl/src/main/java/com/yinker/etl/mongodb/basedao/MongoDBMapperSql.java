package com.yinker.etl.mongodb.basedao;

public class MongoDBMapperSql {

	/**
	 * 结果集映射表的key值
	 */
	private String resultMap;

	/**
	 * 结果集的Java对象类型
	 */
	private String resultType;

	/**
	 * MongoDB查询条件JSON配置（符合groovy语法规范）
	 */
	private String condition;
	
	/**
	 * 排序字符串
	 */
	private String sortColumns;

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getResultMap() {
		return resultMap;
	}

	public void setResultMap(String resultMap) {
		this.resultMap = resultMap;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}

}
