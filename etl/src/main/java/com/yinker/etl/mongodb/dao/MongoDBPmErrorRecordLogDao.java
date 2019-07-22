package com.yinker.etl.mongodb.dao;

import com.yinker.etl.pm.model.base.PmErrorRecordLog;
import org.zwork.framework.base.support.Pagination;

import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>kettle转换日志Dao接口
 * <b>作者:</b>Lenovo
 * <b>创建日期:</b>2017年5月10日 下午12:05:04
 * </pre>
 */
public interface MongoDBPmErrorRecordLogDao {

	/**
	 * 插入数据
	 */
	public int insert (PmErrorRecordLog etlErrorRecordLog);
	
	/**
	 * 根据条件查询数据并分页
	 * 
	 * @param conditions
	 * @param pageNumber
	 * @param pageSize
	 * @param sortColumns
	 * @return
	 */
	public Pagination getByCondition (Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns);
	
	/**
	 * 根据条件查询对象
	 */
	public PmErrorRecordLog selectByOne (Map<String, Object> conditions);
	
	/**
	 * 修改数据根据ID
	 */
	public void updateById (PmErrorRecordLog entity);

	/**
	 * mongo错误日志分组分页查询
	 */
	public Pagination findByCondition(Map<String, Object> conditions, int pageNumber, int pageSize);

	/**
	 * mongo错误日志VIEW页面查询
	 */
	public Pagination mongoPageQueryView(Map<String, Object> conditions, Integer pageNumber, Integer pageSize);

	/**
	 * mongo错误日志根据errorId查询实体
	 */
	public PmErrorRecordLog selectMongoByErrorId(String s);
}
