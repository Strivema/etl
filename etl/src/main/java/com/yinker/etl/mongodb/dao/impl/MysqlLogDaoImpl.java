package com.yinker.etl.mongodb.dao.impl;

import com.yinker.etl.mongodb.dao.MysqlToMongoDBLogDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>查询mysql日志数据
 * <b>作者:</b>Lenovo
 * <b>创建日期:</b>2017年7月15日 下午12:06:01
 * </pre>
 */
@Component
public class MysqlLogDaoImpl implements MysqlToMongoDBLogDao {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlLogDaoImpl.class);
	
	protected static final String NAMESPACE = "SelectMysql";
	
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
    
    @Resource
    private SqlSession sqlSession;
    
    /**
     * 查询mysql数据库表
     * @param tableName
	 *  @param step
     * @return
     */
	@Override
    public List<Map<String,Object>> selectMysql(String tableName, Integer step) {
		LOGGER.info("查询kettle日志" + tableName + "数据..");
		String statement = getIbatisMapperNamesapce() + ".selectList";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		map.put("step", step);
		return sqlSession.selectList(statement, map);
	}

	public List<Map<String,Object>> selectMysqlByOrder(String tableName, Integer step, String retentionColumn) {
		LOGGER.info("查询kettle日志" + tableName + "数据..");
		String statement = getIbatisMapperNamesapce() + ".selectMysqlByOrder";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		map.put("step", step);
		map.put("retentionColumn",retentionColumn);
		return sqlSession.selectList(statement, map);
	}

	/**
	 * 查询mysql数据库表
	 * @param tableName
	 * @param index
	 * @param step
	 * @return
	 */
	public List<Map<String,Object>> selectMysqlLimit(String tableName, Integer index, Integer step) {
		LOGGER.info("查询kettle日志" + tableName + "数据..");
		String statement = getIbatisMapperNamesapce() + ".selectMysqlLimit";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		map.put("index", index);
		map.put("index2", index + step);
		return sqlSession.selectList(statement, map);
	}

	/**
     * 获取mysql数据库日志迁移表List
     * @return
     */
	public List<Map<String, Object>> selectTablesList(Map<String, Object> map) {
		String statement = getIbatisMapperNamesapce() + ".selectTablesList";
		return sqlSession.selectList(statement, map);
	}

	@Override
	public void insert(Object tableObject,String collectionName) {
		
	}

	@Override
	public void insertLogBatch(Map<String, Object> map) {
		String statement = getIbatisMapperNamesapce() + ".insertLogBatch";	
		sqlSession.insert(statement, map);
	}
	

	@Override
    public void updateLogBatch(Map<String, Object> map) {
		String statement = getIbatisMapperNamesapce() + ".updateLogBatch";	
		sqlSession.update(statement, map);
	}


	public int selectNum(Map<String, Object> map) {
		String statement = getIbatisMapperNamesapce() + ".selectNum";
		return sqlSession.selectOne(statement, map);
	}

	public int selectNumBeforeDay(Map<String, Object> map) {
		String statement = getIbatisMapperNamesapce() + ".selectNumBeforeDay";
		return sqlSession.selectOne(statement, map);
	}

	public void delete(String tableName, Integer step) {
		String statement = getIbatisMapperNamesapce() + ".delete";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		map.put("step", step);
		sqlSession.delete(statement, map);
	}

	public void deleteByOrder(String tableName, Integer step, String retentionColumn) {
		String statement = getIbatisMapperNamesapce() + ".deleteByOrder";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		map.put("step", step);
		map.put("retentionColumn",retentionColumn);
		sqlSession.delete(statement, map);
	}

}
