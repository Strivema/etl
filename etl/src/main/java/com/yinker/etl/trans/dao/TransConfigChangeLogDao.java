package com.yinker.etl.trans.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.trans.model.base.TransConfigChangeLog;
import com.yinker.etl.trans.model.base.TransConfigChangeLogPK;

/** 
 * <pre>
 * <b>类描述:</b>TransConfigChangeLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:59
 * </pre>
 */
public class TransConfigChangeLogDao extends AbstractMybatisDao<TransConfigChangeLogPK, TransConfigChangeLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TransConfigChangeLogDao.class);

    protected static final String NAMESPACE = "TransConfigChangeLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}