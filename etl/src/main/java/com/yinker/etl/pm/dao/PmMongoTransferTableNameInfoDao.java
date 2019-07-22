package com.yinker.etl.pm.dao;

import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfo;
import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfoPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.base.support.AbstractMybatisDao;

import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmMongoTransferTableNameInfo表对应的数据库操作类。
 * </pre>
 */
public class PmMongoTransferTableNameInfoDao extends AbstractMybatisDao<PmMongoTransferTableNameInfoPK, PmMongoTransferTableNameInfo> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmMongoTransferTableNameInfoDao.class);

    protected static final String NAMESPACE = "PmMongoTransferTableNameInfo";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    /**
     * 获取ETL数据库的日志表名称List
     * @return
     */
    public List<Map<String,Object>> selectEtlLogList() {

        String statement = getIbatisMapperNamesapce() + ".selectEtlLogList";

        return getSqlSessionTemplate().selectList(statement);
    }

    /**
     * 获取对应表的迁移步长（20M数据）
     * @return
     */
    public int selectTransSteps(String tableName){
        String statement = getIbatisMapperNamesapce() + ".selectTransSteps";

        return getSqlSessionTemplate().selectOne(statement, tableName);
    }
}