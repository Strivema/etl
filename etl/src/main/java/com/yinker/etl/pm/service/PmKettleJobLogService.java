package com.yinker.etl.pm.service;

import com.yinker.etl.mongodb.dao.MongoDBPmKettleJobLogDao;
import org.zwork.framework.base.support.BaseService;
import com.yinker.etl.pm.dao.PmKettleJobLogDao;
import com.yinker.etl.pm.model.base.PmKettleJobLog;
import com.yinker.etl.pm.model.base.PmKettleJobLogPK;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:52
 * </pre>
 */
public class PmKettleJobLogService extends BaseService<PmKettleJobLogPK, PmKettleJobLog, PmKettleJobLogDao> implements 
                org.zwork.framework.base.BaseService<PmKettleJobLogPK, PmKettleJobLog> {

    @Resource(name="mongoDBPmKettleJobLogDao")
    private MongoDBPmKettleJobLogDao mongoDBPmKettleJobLogDao;

    public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {
        return mongoDBPmKettleJobLogDao.getByCondition(conditions, pageNumber, pageSize, sortColumns);
    }

    public PmKettleJobLog selectById(Map<String, Object> conditions) {

        return mongoDBPmKettleJobLogDao.selectByOne(conditions);
    }

    public Pagination findByCondition(Map<String, Object> conditions, int pageNumber, int pageSize) {

        return mongoDBPmKettleJobLogDao.findByConditon(conditions, pageNumber, pageSize);
    }
}
