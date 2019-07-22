package com.yinker.etl.mongodb.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yinker.etl.mongodb.basedao.BaseMongoDBDao;
import com.yinker.etl.mongodb.dao.MongoDBPmKettleJobLogDao;
import com.yinker.etl.pm.model.base.PmKettleJobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.zwork.framework.base.support.Pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>基于MongoDB的任务列表DAO实现类
 * <b>作者:</b>崔博文
 * <b>创建日期:</b>2017年5月26日15:50:27
 * </pre>
 */
public class MongoDBPmKettleJobLogDaoImpl extends BaseMongoDBDao implements MongoDBPmKettleJobLogDao {
	
	@Override
	protected String getMongoMapperId() {
		return "etl.PmKettleJobLog";
	}
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
    public int insert(PmKettleJobLog etlKettleJobLog) {
		
		return insert("insert", etlKettleJobLog);
	}

	@Override
	public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {
		
		if (conditions == null) {
			conditions = new HashMap<String, Object>();
		}
		return pageQuery("getByCondition", conditions, pageNumber, pageSize, sortColumns);
	}

	@Override
	public PmKettleJobLog selectByOne(Map<String, Object> conditions) {
		
		return (PmKettleJobLog) selectOne("selectByOne", conditions);
	}

	@Override
    public Pagination findByConditon(Map<String, Object> conditions, int pageNumber, int pageSize){
		int skip = pageSize * (pageNumber - 1);
		int limit = pageSize;

		String collectionName = "pm_kettle_job_log";
		DBCollection dbCollection = mongoTemplate.getCollection(collectionName);

		String sortColumn = "logdate";

		Query query = new Query().limit(limit);//分页条数设置

		//排序,简单处理
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, sortColumn));
		query.with(sort);
		//添加查询条件，简单处理
		if(conditions.get("JOBNAME") != null && !"".equals(conditions.get("JOBNAME"))){
			query.addCriteria(Criteria.where("jobname").regex(".*?" + conditions.get("JOBNAME") + ".*"));
		}
		if(conditions.get("STATUS") != null && !"".equals(conditions.get("STATUS"))){
			query.addCriteria(Criteria.where("status").is(conditions.get("STATUS")));
		}
		if(conditions.get("LOGDATESTART") != null && !"".equals(conditions.get("LOGDATESTART")) && (conditions.get("LOGDATEEND") == null || "".equals(conditions.get("LOGDATEEND")))){
			query.addCriteria(Criteria.where("logdate").gte(conditions.get("LOGDATESTART")));
		}
		if(conditions.get("LOGDATEEND") != null && !"".equals(conditions.get("LOGDATEEND")) && (conditions.get("LOGDATESTART") == null || "".equals(conditions.get("LOGDATESTART")))){
			query.addCriteria(Criteria.where("logdate").lte(conditions.get("LOGDATEEND")));
		}
		if(conditions.get("LOGDATESTART") != null && !"".equals(conditions.get("LOGDATESTART")) && conditions.get("LOGDATEEND") != null && !"".equals(conditions.get("LOGDATEEND"))){
			query.addCriteria(Criteria.where("logdate").gte(conditions.get("LOGDATESTART")).lte(conditions.get("LOGDATEEND")));
		}

		query.skip(skip);
		//简单处理索引,建一个排序字段的降序索引
		List<DBObject> indexList = dbCollection.getIndexInfo();
		if(indexList != null && indexList.size() <= 1){
			dbCollection.createIndex(new BasicDBObject(sortColumn, -1));
		}
		List<PmKettleJobLog> list = mongoTemplate.find(query, PmKettleJobLog.class, collectionName);

		//查询表数据量
		Long count = mongoTemplate.count(query, collectionName);
		Pagination page = new Pagination();
		page.setData(list);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setCount(Integer.parseInt(count.toString()));
		return page;
	}
}
