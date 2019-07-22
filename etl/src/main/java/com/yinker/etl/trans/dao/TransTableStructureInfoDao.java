package com.yinker.etl.trans.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.trans.model.base.TransTableStructureInfo;
import com.yinker.etl.trans.model.base.TransTableStructureInfoPK;

import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>TransTableStructureInfo表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-21 15:58:42
 * </pre>
 */
public class TransTableStructureInfoDao extends AbstractMybatisDao<TransTableStructureInfoPK, TransTableStructureInfo> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TransTableStructureInfoDao.class);

    protected static final String NAMESPACE = "TransTableStructureInfo";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    public Integer selectTransCount() {
        String statement = getIbatisMapperNamesapce() + ".selectTransCount";

        return getSqlSessionTemplate().selectOne(statement);
    }

    public void updateDbCode(Map map) {
        String statement = getIbatisMapperNamesapce() + ".updateDbCode";
        getSqlSessionTemplate().update(statement, map);
        statement = getIbatisMapperNamesapce() + ".updateDbCode2";
        getSqlSessionTemplate().update(statement, map);
    }
}