package com.yinker.etl.mongodb.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yinker.etl.mongodb.basedao.BaseMongoDBDao;
import com.yinker.etl.mongodb.dao.MongoDBPmKettleTransLogDao;
import com.yinker.etl.pm.model.base.PmKettleTransLog;
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
 * <b>作者:</b>李得亮
 * <b>创建日期:</b>2017年2月15日 下午12:06:01
 * </pre>
 */
public class MongoDBPmKettleTransLogDaoImpl extends BaseMongoDBDao implements MongoDBPmKettleTransLogDao {
	
	@Override
	protected String getMongoMapperId() {
		return "etl.PmKettleTransLog";
	}
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
    public int insert(PmKettleTransLog pmKettleTransLog) {
		
		return insert("insert", pmKettleTransLog);
	}

	@Override
	public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {
		
		if (conditions == null) {
			conditions = new HashMap<String, Object>();
		}
		return pageQuery("getByCondition", conditions, pageNumber, pageSize, sortColumns);
	}

	@Override
	public PmKettleTransLog selectByOne(Map<String, Object> conditions) {
		
		return (PmKettleTransLog) selectOne("selectByOne", conditions);
	}

	@Override
    public Pagination findByCondition(Map<String, Object> conditions, Integer pageNumber, Integer pageSize) {
		int skip = pageSize * (pageNumber - 1);
		int limit = pageSize;

		String collectionName = "pm_kettle_trans_log";
		DBCollection dbCollection = mongoTemplate.getCollection(collectionName);

		String sortColumn = "startdate";

		Query query = new Query().limit(limit);//分页条数设置

		//排序,简单处理
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, sortColumn));
		query.with(sort);
		//添加查询条件，简单处理
		List<String> transNameList = (List<String>) conditions.get("TRANSNAME");
		if(transNameList != null && transNameList.size() > 0){
			query.addCriteria(Criteria.where("transname").in(transNameList));
		}
		if(conditions.get("STATUS") != null && !"".equals(conditions.get("STATUS"))){
			query.addCriteria(Criteria.where("status").is(conditions.get("STATUS")));
		}
		if(conditions.get("LOGDATESTART") != null && !"".equals(conditions.get("LOGDATESTART")) && (conditions.get("LOGDATEEND") == null || "".equals(conditions.get("LOGDATEEND")))){
			query.addCriteria(Criteria.where("startdate").gte(conditions.get("LOGDATESTART")));
		}
		if(conditions.get("LOGDATEEND") != null && !"".equals(conditions.get("LOGDATEEND")) && (conditions.get("LOGDATESTART") == null || "".equals(conditions.get("LOGDATESTART")))){
			query.addCriteria(Criteria.where("startdate").lte(conditions.get("LOGDATEEND")));
		}
		if(conditions.get("LOGDATESTART") != null && !"".equals(conditions.get("LOGDATESTART")) && conditions.get("LOGDATEEND") != null && !"".equals(conditions.get("LOGDATEEND"))){
			query.addCriteria(Criteria.where("startdate").gte(conditions.get("LOGDATESTART")).lte(conditions.get("LOGDATEEND")));
		}

		query.skip(skip);
		//简单处理索引,建一个排序字段的降序索引
		List<DBObject> indexList = dbCollection.getIndexInfo();
		if(indexList != null && indexList.size() <= 1){
			dbCollection.createIndex(new BasicDBObject(sortColumn, -1));
		}
		List<PmKettleTransLog> list = mongoTemplate.find(query, PmKettleTransLog.class, collectionName);

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
