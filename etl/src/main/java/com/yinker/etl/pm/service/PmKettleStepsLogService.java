package com.yinker.etl.pm.service;

import com.yinker.etl.mongodb.dao.MongoDBPmKettleStepsLogDao;
import com.yinker.etl.pm.model.PmKettleStepsLogQuery;
import org.zwork.framework.base.support.BaseService;
import com.yinker.etl.pm.dao.PmKettleStepsLogDao;
import com.yinker.etl.pm.model.base.PmKettleStepsLog;
import com.yinker.etl.pm.model.base.PmKettleStepsLogPK;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleStepsLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:49
 * </pre>
 */
public class PmKettleStepsLogService extends BaseService<PmKettleStepsLogPK, PmKettleStepsLog, PmKettleStepsLogDao> implements 
                org.zwork.framework.base.BaseService<PmKettleStepsLogPK, PmKettleStepsLog> {

    @Resource(name = "mongoDBPmKettleStepsLogDao")
    private MongoDBPmKettleStepsLogDao mongoDBPmKettleStepsLogDao;

    public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {

        return mongoDBPmKettleStepsLogDao.getByCondition(conditions, pageNumber, pageSize, sortColumns);
    }

    public <EntityQueryObject extends PmKettleStepsLog> Pagination myPageQuery(EntityQueryObject queryObject, List<String> idList) {
        String idStr = "(";
        if(idList != null && idList.size() > 0){
            for(String id : idList){
                if(id.indexOf("STS") >= 0){
                    idStr += "'" + id + "',";
                }else {
                    idStr += id + ",";
                }
            }
            idStr = idStr.substring(0, idStr.length() - 1) + ")";
        }else {
            idStr = "(null)";
        }
        queryObject.setTransname(idStr);
        return modelDao.myPageQuery(queryObject);
    }

    public Pagination findByCondition(Map<String, Object> conditions, Integer pageNumber, Integer pageSize) {

        return mongoDBPmKettleStepsLogDao.findByCondition(conditions, pageNumber, pageSize);
    }

    public Pagination channelPageQuery(PmKettleStepsLogQuery queryEntity) {
        return modelDao.channelPageQuery(queryEntity);
    }
}
