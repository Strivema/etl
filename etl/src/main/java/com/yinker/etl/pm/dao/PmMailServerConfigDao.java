package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmMailServerConfig;
import com.yinker.etl.pm.model.base.PmMailServerConfigPK;

/** 
 * <pre>
 * <b>类描述:</b>PmMailServerConfig表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-03-06 10:15:28
 * </pre>
 */
public class PmMailServerConfigDao extends AbstractMybatisDao<PmMailServerConfigPK, PmMailServerConfig> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmMailServerConfigDao.class);

    protected static final String NAMESPACE = "PmMailServerConfig";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}