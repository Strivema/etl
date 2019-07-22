package com.yinker.etl.pm.service;

import com.yinker.etl.mongodb.dao.MongoDBPmErrorRecordLogDao;
import com.yinker.etl.pm.model.PmErrorRecordLogQuery;
import org.zwork.framework.base.support.BaseService;
import com.yinker.etl.pm.dao.PmErrorRecordLogDao;
import com.yinker.etl.pm.model.base.PmErrorRecordLog;
import com.yinker.etl.pm.model.base.PmErrorRecordLogPK;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorRecordLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:50
 * </pre>
 */
public class PmErrorRecordLogService extends BaseService<PmErrorRecordLogPK, PmErrorRecordLog, PmErrorRecordLogDao> implements 
                org.zwork.framework.base.BaseService<PmErrorRecordLogPK, PmErrorRecordLog> {

    @Resource(name = "mongoDBPmErrorRecordLogDao")
    private MongoDBPmErrorRecordLogDao mongoDBPmErrorRecordLogDao;

    public PmErrorRecordLog selectById(Map<String, Object> conditions) {

        return mongoDBPmErrorRecordLogDao.selectByOne(conditions);
    }


    public void updateById(PmErrorRecordLog entity){
        mongoDBPmErrorRecordLogDao.updateById(entity);
    }

    public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {

        return mongoDBPmErrorRecordLogDao.getByCondition(conditions, pageNumber, pageSize, sortColumns);
    }

    public Pagination findByCondition(Map<String, Object> conditions, int pageNumber, int pageSize) {

        return mongoDBPmErrorRecordLogDao.findByCondition(conditions, pageNumber, pageSize);
    }

    public Pagination mongoPageQueryView(Map<String, Object> conditions, Integer pageNumber, Integer pageSize) {

        return mongoDBPmErrorRecordLogDao.mongoPageQueryView(conditions, pageNumber, pageSize);
    }

    public PmErrorRecordLog selectMongoByErrorId(String s) {

        return mongoDBPmErrorRecordLogDao.selectMongoByErrorId(s);
    }

    public Pagination pageQueryView(PmErrorRecordLogQuery queryEntity) {
        return modelDao.pageQueryView(queryEntity);
    }




}
