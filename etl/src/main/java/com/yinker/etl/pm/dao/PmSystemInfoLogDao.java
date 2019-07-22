package com.yinker.etl.pm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmSystemInfoLog;
import com.yinker.etl.pm.model.base.PmSystemInfoLogPK;

/** 
 * <pre>
 * <b>类描述:</b>PmSystemInfoLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:39:08
 * </pre>
 */
public class PmSystemInfoLogDao extends AbstractMybatisDao<PmSystemInfoLogPK, PmSystemInfoLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmSystemInfoLogDao.class);

    protected static final String NAMESPACE = "PmSystemInfoLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    public void updateBatchByIP(PmSystemInfoLog pmSystemInfoLog){
        this.getSqlSessionTemplate().update(NAMESPACE+".updateBatchByIP" , pmSystemInfoLog);
    }
}