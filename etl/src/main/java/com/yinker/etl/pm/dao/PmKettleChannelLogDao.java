package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmKettleChannelLog;
import com.yinker.etl.pm.model.base.PmKettleChannelLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleChannelLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-26 14:56:13
 * </pre>
 */
public class PmKettleChannelLogDao extends AbstractMybatisDao<PmKettleChannelLogPK, PmKettleChannelLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmKettleChannelLogDao.class);

    protected static final String NAMESPACE = "PmKettleChannelLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}