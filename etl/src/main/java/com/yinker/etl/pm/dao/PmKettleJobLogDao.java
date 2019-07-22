package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmKettleJobLog;
import com.yinker.etl.pm.model.base.PmKettleJobLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:51
 * </pre>
 */
public class PmKettleJobLogDao extends AbstractMybatisDao<PmKettleJobLogPK, PmKettleJobLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmKettleJobLogDao.class);

    protected static final String NAMESPACE = "PmKettleJobLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}