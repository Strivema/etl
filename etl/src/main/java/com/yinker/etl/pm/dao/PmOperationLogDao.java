package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmOperationLog;
import com.yinker.etl.pm.model.base.PmOperationLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmOperationLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:46
 * </pre>
 */
public class PmOperationLogDao extends AbstractMybatisDao<PmOperationLogPK, PmOperationLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmOperationLogDao.class);

    protected static final String NAMESPACE = "PmOperationLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}