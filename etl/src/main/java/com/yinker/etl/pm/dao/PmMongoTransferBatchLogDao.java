package com.yinker.etl.pm.dao;

import com.yinker.etl.pm.model.base.PmMongoTransferBatchLog;
import com.yinker.etl.pm.model.base.PmMongoTransferBatchLogPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.base.support.AbstractMybatisDao;

/**
 * <pre>
 * <b>类描述:</b>pm_mongo_transfer_batch_log表对应的数据库操作类。
 * </pre>
 */
public class PmMongoTransferBatchLogDao extends AbstractMybatisDao<PmMongoTransferBatchLogPK, PmMongoTransferBatchLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmMongoTransferBatchLogDao.class);

    protected static final String NAMESPACE = "PmMongoTransferBatchLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
}