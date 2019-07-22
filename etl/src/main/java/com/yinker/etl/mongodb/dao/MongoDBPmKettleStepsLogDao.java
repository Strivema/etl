package com.yinker.etl.mongodb.dao;

import com.yinker.etl.pm.model.base.PmKettleStepsLog;
import org.zwork.framework.base.support.Pagination;

import java.util.Map;


/**
 * <pre>
 * <b>类描述:</b>kettle转换日志Dao接口
 * <b>作者:</b>Lenovo
 * <b>创建日期:</b>2017年5月10日 下午12:05:04
 * </pre>
 */
public interface MongoDBPmKettleStepsLogDao {

    /**
     * 插入数据
     */
    public int insert (PmKettleStepsLog etlKettleSimplestepsLog);

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
     *
     * @return
     */
    public PmKettleStepsLog selectByOne (Map<String, Object> conditions);

    public Pagination findByCondition(Map<String, Object> conditions, Integer pageNumber, Integer pageSize);
}
