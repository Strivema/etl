package com.yinker.etl.pm.service;

import com.yinker.etl.mongodb.dao.MongoDBPmKettleJobItemLogDao;
import org.zwork.framework.base.support.BaseService;
import com.yinker.etl.pm.dao.PmKettleJobItemLogDao;
import com.yinker.etl.pm.model.base.PmKettleJobItemLog;
import com.yinker.etl.pm.model.base.PmKettleJobItemLogPK;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobItemLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:54
 * </pre>
 */
public class PmKettleJobItemLogService extends BaseService<PmKettleJobItemLogPK, PmKettleJobItemLog, PmKettleJobItemLogDao> implements 
                org.zwork.framework.base.BaseService<PmKettleJobItemLogPK, PmKettleJobItemLog> {

    @Resource(name = "mongoDBPmKettleJobItemLogDao")
    private MongoDBPmKettleJobItemLogDao mongoDBPmKettleJobItemLogDao;

    public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {
        return mongoDBPmKettleJobItemLogDao.getByCondition(conditions, pageNumber, pageSize, sortColumns);
    }

    public Pagination findByCondition(Map<String, Object> conditions, Integer pageNumber, Integer pageSize) {
        return mongoDBPmKettleJobItemLogDao.findByCondition(conditions, pageNumber, pageSize);
    }
}
