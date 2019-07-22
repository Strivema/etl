package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmGroupUser;
import com.yinker.etl.pm.model.base.PmGroupUserPK;

/** 
 * <pre>
 * <b>类描述:</b>PmGroupUser表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:50
 * </pre>
 */
public class PmGroupUserDao extends AbstractMybatisDao<PmGroupUserPK, PmGroupUser> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmGroupUserDao.class);

    protected static final String NAMESPACE = "PmGroupUser";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}