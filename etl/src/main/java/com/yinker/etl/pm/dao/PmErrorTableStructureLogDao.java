package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmErrorTableStructureLog;
import com.yinker.etl.pm.model.base.PmErrorTableStructureLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorTableStructureLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-13 11:03:59
 * </pre>
 */
public class PmErrorTableStructureLogDao extends AbstractMybatisDao<PmErrorTableStructureLogPK, PmErrorTableStructureLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmErrorTableStructureLogDao.class);

    protected static final String NAMESPACE = "PmErrorTableStructureLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}