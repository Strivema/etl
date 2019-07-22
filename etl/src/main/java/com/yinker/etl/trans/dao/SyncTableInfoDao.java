package com.yinker.etl.trans.dao;

import com.yinker.etl.trans.model.SyncTableInfoQuery;
import com.yinker.etl.trans.model.base.SyncTableInfo;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 同步表结构
 * @author Lenovo
 *
 */
public class SyncTableInfoDao extends SqlSessionDaoSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTableInfoDao.class);

    protected static final String NAMESPACE = "SyncTableInfo";

    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    
    /**
     * 根据条件查询数据
     * @param entity
     * @return
     */
    public List<SyncTableInfo> selectByEntity(SyncTableInfoQuery entity) {
    	
    	String statement = getIbatisMapperNamesapce() + ".selectByEntity";
	    if (entity == null) {
	    	LOGGER.warn("查询条件为null,执行全表查询!");
	    }
	    List<SyncTableInfo> list = getSqlSessionTemplate().selectList(statement, entity);
	    return list;
    }

    /**
     * 执行新增字段sql
     * @param entity
     * @return
     */
    public int executeAlterSqlBanKey(SyncTableInfo entity) {

        String statement = getIbatisMapperNamesapce() + ".executeAlterSqlBanKey";

        return getSqlSessionTemplate().update(statement, entity);
    }

    /**
     * 执行修改字段sql
     * @param entity
     * @return
     */
    public int executeModifySqlBanKey(SyncTableInfo entity) {

        String statement = getIbatisMapperNamesapce() + ".executeModifySqlBanKey";

        return getSqlSessionTemplate().update(statement, entity);
    }

    /**
     * 创建dp数据表
     * @param entity
     * @return
     */
    public int createDpTable(SyncTableInfo entity) {

        String statement = getIbatisMapperNamesapce() + ".createDpTable";

        return getSqlSessionTemplate().update(statement, entity);
    }

    /**
     * 查询对应数据源下的所有表名
     * @param entity
     * @return
     */
    public List<String> selectTablesList(SyncTableInfo entity) {

        String statement = getIbatisMapperNamesapce() + ".selectTablesList";

        return getSqlSessionTemplate().selectList(statement, entity);
    }

    /**
     * 表结构同步去除目标表中的主键
     *
     * @param tableName
     *
     */
    public void dropKeys(String tableName) {

        String statement = getIbatisMapperNamesapce() + ".dropKeys";

        getSqlSessionTemplate().update(statement, tableName);
    }

    public void addKeys(Map<String, String> addKeyMap) {
        String statement = getIbatisMapperNamesapce() + ".addKeys";

        getSqlSessionTemplate().update(statement, addKeyMap);
    }

    /**
     * 表结构同步去掉auto_increament属性
     *
     * @param map
     *
     */
    public void exeAutoMove(Map map) {
        String statement = getIbatisMapperNamesapce() + ".exeAutoMove";

        getSqlSessionTemplate().update(statement, map);
    }
}