package com.yinker.etl.trans.dao;

import com.yinker.etl.trans.model.TransStatusLogQuery;
import com.yinker.etl.trans.model.base.TransStatusLog;
import com.yinker.etl.trans.model.base.TransStatusLogPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.base.support.AbstractMybatisDao;

import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>TransStatusLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-14 20:12:11
 * </pre>
 */
public class TransStatusLogDao extends AbstractMybatisDao<TransStatusLogPK, TransStatusLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TransStatusLogDao.class);

    protected static final String NAMESPACE = "TransStatusLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    public List<TransStatusLog> selectAllByBathch(TransStatusLogQuery transStatusLogQuery){
        String statement = getIbatisMapperNamesapce() + ".selectAllByBathch";
        return getSqlSessionTemplate().selectList(statement, transStatusLogQuery);
    }

    /**
     * 查询指定时间范围内单条最大几条跑批信息
     * @param transStatusLogQuery
     * @return
     */
    public List<TransStatusLog> selectUseTimeMaxTrans(TransStatusLogQuery transStatusLogQuery){
        String statement = getIbatisMapperNamesapce() + ".selectUseTimeMaxTrans";
        return getSqlSessionTemplate().selectList(statement, transStatusLogQuery);
    }

    /**
     * 查询指定时间范围内平均最大几条跑批信息
     * @param transStatusLogQuery
     * @return
     */
    public List<TransStatusLog> selectUseTimeAvgTrans (TransStatusLogQuery transStatusLogQuery) {
        String statement = getIbatisMapperNamesapce() + ".selectUseTimeAvgTrans";
        return getSqlSessionTemplate().selectList(statement, transStatusLogQuery);
    }

    /**
     * 查询指定范围内状态转换率信息
     * @param transStatusLogQuery
     * @return
     */
    public List<TransStatusLog> selectImplementationRate (TransStatusLogQuery transStatusLogQuery) {
        String statement = getIbatisMapperNamesapce() + ".selectImplementationRate";
        return getSqlSessionTemplate().selectList(statement, transStatusLogQuery);
    }
}