package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmKettleJobItemLog;
import com.yinker.etl.pm.model.base.PmKettleJobItemLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobItemLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:53
 * </pre>
 */
public class PmKettleJobItemLogDao extends AbstractMybatisDao<PmKettleJobItemLogPK, PmKettleJobItemLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmKettleJobItemLogDao.class);

    protected static final String NAMESPACE = "PmKettleJobItemLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}