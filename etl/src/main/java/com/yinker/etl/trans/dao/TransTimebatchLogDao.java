package com.yinker.etl.trans.dao;

import com.yinker.etl.trans.model.TransTimebatchLogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.trans.model.base.TransTimebatchLog;
import com.yinker.etl.trans.model.base.TransTimebatchLogPK;

import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>TransTimebatchLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:03
 * </pre>
 */
public class TransTimebatchLogDao extends AbstractMybatisDao<TransTimebatchLogPK, TransTimebatchLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TransTimebatchLogDao.class);

    protected static final String NAMESPACE = "TransTimebatchLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    /**
     * 查询最后一条内容
     */
    public TransTimebatchLog selectLastOneColumn(){
        String statement = getIbatisMapperNamesapce() + ".selectLastOneColumn";
        return getSqlSessionTemplate().selectOne(statement);
    }

    /**
     * 查询正在跑批的任务
     * @param transStatusLogQuery
     * @return
     */
    public List<TransTimebatchLog> selectRunningTrans(TransTimebatchLogQuery transStatusLogQuery){
        String statement = getIbatisMapperNamesapce() + ".selectRunningTrans";
        return getSqlSessionTemplate().selectList(statement, transStatusLogQuery);
    }
}