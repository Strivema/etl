package com.yinker.etl.mongodb.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yinker.etl.mongodb.basedao.BaseMongoDBDao;
import com.yinker.etl.mongodb.dao.MongoDBPmErrorRecordLogDao;
import com.yinker.etl.pm.model.base.PmErrorRecordLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
public class MongoDBPmErrorRecordLogDaoImpl extends BaseMongoDBDao implements MongoDBPmErrorRecordLogDao {
	
	@Override
	protected String getMongoMapperId() {
		return "etl.PmErrorRecordLog";
	}

	private String getCollectionName() {return  "pm_error_record_log"; }

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
    public int insert(PmErrorRecordLog pmErrorRecordLog) {
		
		return insert("insert", pmErrorRecordLog);
	}

	@Override
	public Pagination getByCondition(Map<String, Object> conditions, int pageNumber, int pageSize, String sortColumns) {
		
		if (conditions == null) {
			conditions = new HashMap<String, Object>();
		}
		return pageQuery("getByCondition", conditions, pageNumber, pageSize, sortColumns);
	}

	@Override
	public PmErrorRecordLog selectByOne(Map<String, Object> conditions) {
		
		return (PmErrorRecordLog) selectOne("selectByOne", conditions);
	}

	/**
	 * Mongo错误日志根据errorId查询实体
	 */
	@Override
    public PmErrorRecordLog selectMongoByErrorId(String errorId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("errorId").is(Integer.parseInt(errorId)));
		PmErrorRecordLog entity = mongoTemplate.findOne(query, PmErrorRecordLog.class, getCollectionName());
		return entity;
	}

	/**
	 * Mongo错误日志批量处理
	 */
	@Override
    public void updateById(PmErrorRecordLog entity) {
		Update update = new Update();
		update.set("errStatus", entity.getErrStatus());
		update.set("remark", entity.getRemark());
		Query query = new Query();
		query.addCriteria(Criteria.where("errTableName").is(entity.getErrTableName()));
		query.addCriteria(Criteria.where("errDesc").is(entity.getErrDesc()));
		mongoTemplate.updateMulti(query, update, "pm_error_record_log");
	}

	/**
	 * Mongo错误日志分组分页查询
	 */
	@Override
    public Pagination findByCondition(Map<String, Object> conditions, int pageNumber, int pageSize) {
		int skip = pageSize * (pageNumber - 1);
		int limit = pageSize;

		DBCollection dbCollection = mongoTemplate.getCollection(getCollectionName());

		String sortColumn = "errCreateTime";

		//添加查询条件，简单处理
		Criteria criteria = new Criteria();
		if(conditions.get("errTransName") != null && !"".equals(conditions.get("errTransName"))){
			criteria.and("errTransName").regex(escapeExprSpecialWord(String.valueOf(conditions.get("errTransName"))));
		}
		if(conditions.get("errTableName") != null && !"".equals(conditions.get("errTableName"))){
			criteria.and("errTableName").regex(escapeExprSpecialWord(String.valueOf(conditions.get("errTableName"))));
		}
		if(conditions.get("errCreateTimeStart") != null && !"".equals(conditions.get("errCreateTimeStart")) && (conditions.get("errCreateTimeEnd") == null || "".equals(conditions.get("errCreateTimeEnd")))){
			criteria.and("errCreateTime").gte(conditions.get("errCreateTimeStart"));
		}
		if(conditions.get("errCreateTimeEnd") != null && !"".equals(conditions.get("errCreateTimeEnd")) && (conditions.get("errCreateTimeStart") == null || "".equals(conditions.get("errCreateTimeStart")))){
			criteria.and("errCreateTime").lte(conditions.get("errCreateTimeEnd"));
		}
		if(conditions.get("errCreateTimeStart") != null && !"".equals(conditions.get("errCreateTimeStart")) && conditions.get("errCreateTimeEnd") != null && !"".equals(conditions.get("errCreateTimeEnd"))){
			criteria.and("errCreateTime").gte(conditions.get("errCreateTimeStart")).lte(conditions.get("errCreateTimeEnd"));
		}

		//简单处理索引,建一个排序字段的降序索引
		List<DBObject> indexList = dbCollection.getIndexInfo();
		if(indexList != null && indexList.size() <= 1){
			dbCollection.createIndex(new BasicDBObject(sortColumn, -1));
		}

        //mongo聚合查询
		TypedAggregation<PmErrorRecordLog> agg = Aggregation.newAggregation(
				PmErrorRecordLog.class
				,Aggregation.match(criteria)
				,Aggregation.group("errTableName","errDesc").count().as("errCount")
						.first("errorId").as("errorId")
						.first("errTransName").as("errTransName")
						.first("errStatus").as("errStatus")
						.first("errCreateTime").as("errCreateTime")
						.first("errDesc").as("errDesc")
						.first("errCode").as("errCode")
						.first("remark").as("remark")
						.first("errField").as("errField")
				,Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.ASC,"errStatus"), new Sort.Order(Sort.Direction.DESC,"errCreateTime")))
		);
		//取得分组数据
		AggregationResults aggregationResults = mongoTemplate.aggregate(agg, getCollectionName(), PmErrorRecordLog.class);
		List<PmErrorRecordLog> list = aggregationResults.getMappedResults();

		Pagination page = new Pagination();
		page.setCount(list.size());
		//分页
		if(list.size() >= skip + limit){
			list = list.subList(skip, skip + limit);
		} else if(list.size() < skip + limit && list.size() > skip) {
			list = list.subList(skip, list.size());
		}

		page.setData(list);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		return page;
	}

	/**
	 * Mongo错误日志VIEW页面查询
	 */
	@Override
    public Pagination mongoPageQueryView(Map<String, Object> conditions, Integer pageNumber, Integer pageSize) {
		int skip = pageSize * (pageNumber - 1);
		int limit = pageSize;

		String sortColumn = "errCreateTime";

		Query query = new Query().limit(limit);//分页条数设置

		//排序,简单处理
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, sortColumn));
		query.with(sort);
		//添加查询条件，简单处理
		if(conditions.get("errTableName") != null && !"".equals(conditions.get("errTableName"))){
			query.addCriteria(Criteria.where("errTableName").is(conditions.get("errTableName")));
		}
		if(conditions.get("errDesc") != null && !"".equals(conditions.get("errDesc"))){
			query.addCriteria(Criteria.where("errDesc").is(conditions.get("errDesc")));
		}
		query.skip(skip);

		List<PmErrorRecordLog> list = mongoTemplate.find(query, PmErrorRecordLog.class, getCollectionName());

		//查询表数据量
		Long count = mongoTemplate.count(query, getCollectionName());
		Pagination page = new Pagination();
		page.setData(list);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setCount(Integer.parseInt(count.toString()));
		return page;
	}

	/**
	 * Criteria regex查询正则转义
	 */
	private String escapeExprSpecialWord(String keyword) {
		if (StringUtils.isNotBlank(keyword)) {
			String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
			for (String key : fbsArr) {
				if (keyword.contains(key)) {
					keyword = keyword.replace(key, "\\" + key);
				}
			}
		}
		return keyword;
	}

}
