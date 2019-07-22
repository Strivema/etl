package com.yinker.etl.mongodb.dao;

import com.yinker.etl.pm.model.base.PmKettleJobLog;
import org.zwork.framework.base.support.Pagination;

import java.util.List;
import java.util.Map;



/**
 * <pre>
 * <b>类描述:</b>作业日志Dao接口
 * <b>作者:</b> 崔博文
 * <b>创建日期:</b>2017年5月26日15:49:51
 * </pre>
 */
public interface MongoDBPmKettleJobLogDao {

	/**
	 * 插入数据
	 */
	public int insert (PmKettleJobLog etlKettleJobLog);
	
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
	 * @return
	 */
	public PmKettleJobLog selectByOne (Map<String, Object> conditions);


	public Pagination findByConditon(Map<String, Object> conditions, int pageNumber, int pageSize);
}
