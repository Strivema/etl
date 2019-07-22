package com.yinker.etl.pm.service;


import com.yinker.etl.pm.dao.PmMongoTransferBatchLogDao;
import com.yinker.etl.pm.model.base.PmMongoTransferBatchLog;
import com.yinker.etl.pm.model.base.PmMongoTransferBatchLogPK;
import org.springframework.stereotype.Component;
import org.zwork.framework.base.support.BaseService;

/**
 * <pre>
 * <b>类描述:</b>pm_mongo_transfer_batch_log表对应的业务处理类。
 * </pre>
 */
@Component("pmMongoTransferBatchLogService")
public class PmMongoTransferBatchLogService extends BaseService<PmMongoTransferBatchLogPK, PmMongoTransferBatchLog, PmMongoTransferBatchLogDao> implements
        org.zwork.framework.base.BaseService<PmMongoTransferBatchLogPK, PmMongoTransferBatchLog> {

}
