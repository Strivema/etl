package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmDatasourceGroup;
import com.yinker.etl.pm.model.base.PmDatasourceGroupPK;

/** 
 * <pre>
 * <b>类描述:</b>PmDatasourceGroup表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:51
 * </pre>
 */
public class PmDatasourceGroupDao extends AbstractMybatisDao<PmDatasourceGroupPK, PmDatasourceGroup> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmDatasourceGroupDao.class);

    protected static final String NAMESPACE = "PmDatasourceGroup";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}