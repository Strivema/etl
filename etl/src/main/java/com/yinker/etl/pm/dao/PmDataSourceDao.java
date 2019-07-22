package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.model.base.PmDataSourcePK;

/** 
 * <pre>
 * <b>类描述:</b>PmDataSource表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:42
 * </pre>
 */
public class PmDataSourceDao extends AbstractMybatisDao<PmDataSourcePK, PmDataSource> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmDataSourceDao.class);

    protected static final String NAMESPACE = "PmDataSource";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}