package com.yinker.etl.pm.service;

import com.yinker.etl.mongodb.dao.MongoDBPmKettleTransLogDao;
import org.zwork.framework.base.support.BaseService;
import com.yinker.etl.pm.dao.PmKettleTransLogDao;
import com.yinker.etl.pm.model.base.PmKettleTransLog;
import com.yinker.etl.pm.model.base.PmKettleTransLogPK;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleTransLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:48
 * </pre>
 */
public class PmKettleTransLogService extends BaseService<PmKettleTransLogPK, PmKettleTransLog, PmKettleTransLogDao> implements 
                org.zwork.framework.base.BaseService<PmKettleTransLogPK, PmKettleTransLog> {

    @Resource(name = "mongoDBPmKettleTransLogDao")
    private MongoDBPmKettleTransLogDao mongoDBPmKettleTransLogDao;

    public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {

        return mongoDBPmKettleTransLogDao.getByCondition(conditions, pageNumber, pageSize, sortColumns);
    }

    public PmKettleTransLog selectById(Map<String, Object> conditions) {

        return mongoDBPmKettleTransLogDao.selectByOne(conditions);
    }

    public <EntityQueryObject extends PmKettleTransLog> Pagination myPageQuery(EntityQueryObject queryObject, List<String> idList) {
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
        return mongoDBPmKettleTransLogDao.findByCondition(conditions, pageNumber, pageSize);
    }
}
