package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmMailUser;
import com.yinker.etl.pm.model.base.PmMailUserPK;

/** 
 * <pre>
 * <b>类描述:</b>PmMailUser表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:03
 * </pre>
 */
public class PmMailUserDao extends AbstractMybatisDao<PmMailUserPK, PmMailUser> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmMailUserDao.class);

    protected static final String NAMESPACE = "PmMailUser";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}