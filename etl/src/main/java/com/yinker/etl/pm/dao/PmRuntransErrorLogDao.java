package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmRuntransErrorLog;
import com.yinker.etl.pm.model.base.PmRuntransErrorLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmRuntransErrorLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:56
 * </pre>
 */
public class PmRuntransErrorLogDao extends AbstractMybatisDao<PmRuntransErrorLogPK, PmRuntransErrorLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmRuntransErrorLogDao.class);

    protected static final String NAMESPACE = "PmRuntransErrorLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}