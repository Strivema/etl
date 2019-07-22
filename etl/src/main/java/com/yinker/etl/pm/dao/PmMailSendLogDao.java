package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmMailSendLog;
import com.yinker.etl.pm.model.base.PmMailSendLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmMailSendLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:04
 * </pre>
 */
public class PmMailSendLogDao extends AbstractMybatisDao<PmMailSendLogPK, PmMailSendLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmMailSendLogDao.class);

    protected static final String NAMESPACE = "PmMailSendLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}