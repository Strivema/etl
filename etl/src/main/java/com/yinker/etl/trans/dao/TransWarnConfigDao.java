package com.yinker.etl.trans.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.trans.model.base.TransWarnConfig;
import com.yinker.etl.trans.model.base.TransWarnConfigPK;

/** 
 * <pre>
 * <b>类描述:</b>TransWarnConfig表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 20:20:31
 * </pre>
 */
public class TransWarnConfigDao extends AbstractMybatisDao<TransWarnConfigPK, TransWarnConfig> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TransWarnConfigDao.class);

    protected static final String NAMESPACE = "TransWarnConfig";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}