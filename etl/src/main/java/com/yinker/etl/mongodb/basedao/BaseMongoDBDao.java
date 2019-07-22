package com.yinker.etl.mongodb.basedao;

import com.mongodb.*;
import com.mongodb.util.JSON;
import com.yinker.etl.util.BeanUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.Pagination;
import org.zwork.framework.thirdparty.org.springframework.SpringBeanContext;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <b>类描述:</b>MongoDB的dao基本实现
 * <b>作者:</b>李得亮
 * <b>创建日期:</b>2016年11月28日 下午4:57:04
 * </pre>
 */
public abstract class BaseMongoDBDao {
	/**
	 * 日志对象
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaseMongoDBDao.class);
	/**
	 * MongoDB的mapper配置缓存
	 */
	private static ConcurrentHashMap<String, MongoDBMapper> _mappers = new ConcurrentHashMap<String, MongoDBMapper>();
	/**
	 * 结果集类型：hashmap
	 */
	private static final String RESULT_TYPE_HASHMAP = "hashmap";
	/**
	 * 结果集类型：integer
	 */
	private static final String RESULT_TYPE_INTEGER = "integer";

	/**
	 * Spring封装的mongoDB连接对象
	 */
	private MongoTemplate mongoTemplate;

	/**
	 * 需子类实现：获取MongoDB的mapper配置标识
	 * 
	 * @return MongoDB的Mapper标识
	 */
	protected abstract String getMongoMapperId();

	/**
	 * MongoDB的插入操作；可被子类继承；
	 * 
	 * @param sqlKey
	 *            sql配置的key值
	 * @param entity
	 *            插入的对象
	 * @return 插入的记录数
	 */
	protected int insert(String sqlKey, Object entity) {
		DBObject dbo = toEntityDbo(sqlKey, entity, false);
		WriteResult result = getCollection().insert(dbo);
		return result.getN();
	}

	/**
	 * MongoDB的更新操作；可被子类继承；
	 * 
	 * @param sqlKey
	 *            sql配置key值
	 * @param query
	 *            查询对象
	 * @param entity
	 *            更新的对象信息
	 * @return sql影响的记录数
	 */
	protected int update(String sqlKey, Object query, Object entity) {
		DBObject queryDbo = toQueryDbo(sqlKey, query);
		DBObject entityDbo = toEntityDbo(sqlKey, entity, true);
		// 参数列表意义依次为：更新条件；更新的值；查询条件不满足时，是否更新；是否一次更新多条;
		WriteResult result = getCollection().update(queryDbo, entityDbo, false, true);
		return result.getN();
	}

	/**
	 * MongoDB的删除操作；可被子类继承；
	 * 
	 * @param sqlKey
	 *            sql配置key值
	 * @param query
	 *            被删除的对象查询条件
	 * @return sql影响的记录数
	 */
	protected int delete(String sqlKey, Object query) {
		DBObject queryDbo = toQueryDbo(sqlKey, query);
		WriteResult result = getCollection().remove(queryDbo);
		return result.getN();
	}

	/**
	 * MogoDB查询单条记录的操作；可被子类继承；
	 * 
	 * @param sqlKey
	 *            sql配置的key值；
	 * 
	 * @param query
	 *            查询条件
	 * @return 符合条件的记录
	 */
	protected Object selectOne(String sqlKey, Object query) {
		DBObject queryDbo = toQueryDbo(sqlKey, query);
		DBObject result = getCollection().findOne(queryDbo);
		if (result != null) {
			return toObjectFromJson(sqlKey, result);
		}
		return null;
	}

	/**
	 * MongoDB查询列表数据的操作；可被子类继承；
	 * 
	 * @param sqlKey
	 *            sqlkey值；
	 * @param query
	 *            查询条件
	 * @param sortColumns
	 *            排序字符串
	 * @return 符合条件的记录列表
	 */
	protected List<Object> selectList(String sqlKey, Object query, String sortColumns) {
		List<Object> list = new ArrayList<Object>();
		DBCursor cursor = null;
		try {
			DBObject queryDbo = toQueryDbo(sqlKey, query);
			cursor = getCollection().find(queryDbo);
			// 排序处理
			DBObject sortDbo = toSortDbo(sqlKey, sortColumns.toLowerCase());
			if (sortDbo != null) {
				cursor = cursor.sort(sortDbo);
			}
			//处理索引，对需要排序的字段添加索引
			createIndex(sortDbo);
			while (cursor.hasNext()) {
				DBObject dbo = cursor.next();
				list.add(toObjectFromJson(sqlKey, dbo));
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * MongoDB的分页查询;可被子类继承；
	 * 
	 * @param sqlKey
	 *            sql配置的key值
	 * @param query
	 *            查询条件
	 * @param pageNumber
	 *            当前页数
	 * @param pageSize
	 *            每页的记录条数
	 * @param sortColumns
	 *            排序字符串
	 * @return 符合条件的数据列表
	 */
	protected Pagination pageQuery(String sqlKey, Object query, int pageNumber, int pageSize, String sortColumns) {
		int skip = pageSize * (pageNumber - 1);
		int limit = pageSize;

		List<Object> list = new ArrayList<Object>();
		DBCursor cursor = null;
		try {
			DBObject queryDbo = toQueryDbo(sqlKey, query);
			String ss = queryDbo.toString();
			cursor = getCollection().find(queryDbo);

			// 排序处理
			DBObject sortDbo = toSortDbo(sqlKey, sortColumns.toLowerCase());
			if (sortDbo != null) {
				cursor = cursor.sort(sortDbo);
			}
			//处理索引，对需要排序的字段添加索引
			createIndex(sortDbo);

			cursor = cursor.skip(skip).limit(limit);
			while (cursor.hasNext()) {
				DBObject dbo = cursor.next();
				list.add(toObjectFromJson(sqlKey, dbo));
			}
			Pagination page = new Pagination();
			page.setData(list);
			page.setPageNumber(pageNumber);
			page.setPageSize(pageSize);
			page.setCount(cursor.count());
			return page;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	

	/**
	 * 聚合操作
	 * 
	 * @param sqlKey sql配置的key值
	 * @param query 查询条件
	 * @return 聚合结果集
	 */
	protected List<Object> aggregate(String sqlKey, Object query) {
		List<Object> list = new ArrayList<Object>();
		try {
			List<DBObject> queryDbos = toAggregateDbos(sqlKey, query);
			Iterator<DBObject> results = getCollection().aggregate(queryDbos).results().iterator();
			while (results.hasNext()) {
				DBObject dbo = results.next();
				list.add(toObjectFromJson(sqlKey, dbo));
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到满足条件的记录的数量
	 * 
	 * @param sqlKey sql配置的key值
	 * @param query 查询条件
	 * @return 符合条件的记录数 
	 */
	protected int count(String sqlKey, Object query) {
		DBObject queryDbo = toQueryDbo(sqlKey, query);
		return getCollection().find(queryDbo).count();
	}



	/**
	 * 构造MongoDB的查询JSON
	 * 
	 * @param sqlKey 
	 * @param query
	 * @return
	 */
	private DBObject toQueryDbo(String sqlKey, Object query) {
		try {
			if (query == null) {
				LOG.warn("警告：对集合[" + getMapper().getCollectionName() + "]的全部数据进行处理[mapperId:" + getMongoMapperId() + ",sqlKey:" + sqlKey + ",query:{"
						+ query + "}]");
				return new BasicDBObject();
			}
			MongoDBMapper mapper = getMapper();
			// 对查询sql利用groovy进行参数替换
			MongoDBMapperSql sql = mapper.getSql().get(sqlKey);
			String conditonSql = sql.getCondition();
			if (StringUtils.isEmpty(conditonSql)) {
				LOG.warn("警告：对集合[" + getMapper().getCollectionName() + "]的全部数据进行处理[mapperId:" + getMongoMapperId() + ",sqlKey:" + sqlKey + ",query:{"
						+ query + "}]");
				return new BasicDBObject();
			}

			// groovy解析
			Binding bind = new Binding();
			bind.setProperty("obj", query);
			GroovyShell shell = new GroovyShell(bind);

			String resultStr = (String) shell.evaluate(conditonSql);
			DBObject dbo = (BasicDBObject) JSON.parse(resultStr);
			if (dbo == null) {
				dbo = new BasicDBObject();
			}
			if (dbo.keySet().size() == 0) {
				LOG.warn("警告：对集合[" + getMapper().getCollectionName() + "]的全部数据进行处理[mapperId:" + getMongoMapperId() + ",sqlKey:" + sqlKey
						+ ",entity:{" + query + "}]");
			}
			return dbo;
		} catch (Exception e) {
			LOG.error("", e);
			throw new RuntimeException("构造查询条件异常,[mapperId:" + getMongoMapperId() + ",mapperKey:" + getMongoMapperId() + ",sqlKey:" + sqlKey
					+ ",query:{" + query + "}]：", e);
		}
	}

	/**
	 * 得到聚合查询对象列表
	 * 
	 * @param sqlKey
	 * @param query
	 * @return
	 */
	private List<DBObject> toAggregateDbos(String sqlKey, Object query) {
		try {
			MongoDBMapper mapper = getMapper();
			// 对查询sql利用groovy进行参数替换
			MongoDBMapperSql sql = mapper.getSql().get(sqlKey);
			String conditonSql = sql.getCondition();
			if (StringUtils.isEmpty(conditonSql)) {
				LOG.warn("警告：集合[" + getMapper().getCollectionName() + "]的Aggregate操作的条件为空[mapperId:" + getMongoMapperId() + ",sqlKey:" + sqlKey
						+ ",query:{" + query + "}]");
				return new ArrayList<DBObject>();
			}

			// groovy解析
			Binding bind = new Binding();
			bind.setProperty("obj", query);
			GroovyShell shell = new GroovyShell(bind);
			String resultStr = (String) shell.evaluate(conditonSql);
			BasicDBList _list = (BasicDBList) JSON.parse(resultStr);
			List<DBObject> list = new ArrayList<DBObject>();
			for (int i = 0; i < _list.size(); i++) {
				list.add((BasicDBObject) _list.get(i));
			}
			return list;
		} catch (Exception e) {
			LOG.error("", e);
			throw new RuntimeException("构造查询条件异常,[mapperId:" + getMongoMapperId() + ",mapperKey:" + getMongoMapperId() + ",sqlKey:" + sqlKey
					+ ",query:{" + query + "}]：", e);
		}
	}

	/**
	 * 构造MongoDB的排序JSON
	 * 
	 * @param sqlKey
	 *            sql键值
	 * @param sortColumns
	 *            排序字符串
	 * @return
	 */
	private DBObject toSortDbo(String sqlKey, String sortColumns) {
		// 优先使用传入的排序字段；否则，取sql默认的排序字段
		if (StringUtils.isEmpty(sortColumns)) {
			sortColumns = getMapper().getSql().get(sqlKey).getSortColumns();
		}
		if (StringUtils.isNotEmpty(sortColumns)) {
			BasicDBObject sortDbo = new BasicDBObject();
			String[] sa = sortColumns.split(",");
			int i = 0;
			for (String string : sa) {
				if(string.contains("asc")){
					sortDbo.put(string.replace("asc", "").trim(), 1);
				}
				if(string.contains("desc")){
					sortDbo.put(string.replace("desc", "").trim(), -1);
				}
				if(!string.contains("desc") && !string.contains("asc")){
					int j = i + 1;
					int flag = 0;
					while(j < sa.length){
						if(sa[j].contains("asc")){
							sortDbo.put(string.replace("asc", "").trim(), 1);
							flag = 1;
							break;
						}else if(sa[j].contains("desc")){
							sortDbo.put(string.replace("desc", "").trim(), -1);
							flag = 1;
							break;
						}
						j++;
					}
					if(flag == 0){ //flag = 0即不含asc 或 desc
						sortDbo.put(string.replace("asc", "").trim(), 1);
					}
					flag = 0;
				}
				i++;
			}
			return sortDbo;
		}
		return null;
	}
	

	/**
	 * 构造MongoDB的存储JSON
	 * 
	 * @param sqlKey
	 * @param entity
	 * @param isUpdate
	 * @return
	 */
	private DBObject toEntityDbo(String sqlKey, Object entity, boolean isUpdate) {
		MongoDBMapper mapper = getMapper();

		// 对象转换为属性Map
		Map<String, Object> objMap = BeanUtil.convertToMap(entity);
		BasicDBObject dbo = new BasicDBObject();
		MongoDBMapperSql sql = mapper.getSql().get(sqlKey);
		Map<String, String> fieldMap = mapper.getResultMap().get(sql.getResultMap());
		Iterator<String> keyItr = fieldMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			String fieldKey = fieldMap.get(key);
			Object value = objMap.get(fieldKey);
			if (value != null && !"".equals(value)) {
				dbo.put(key, value);
			}
		}
		// update操作
		if (isUpdate) {
			BasicDBObject newDbo = new BasicDBObject().append("$set", dbo);
			return newDbo;
		}
		return dbo;
	}
	
	

	/**
	 * 将查询结果JSON构造为结果对象
	 * 
	 * @param sqlKey
	 * @param dbo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object toObject(String sqlKey, DBObject dbo) {
		MongoDBMapper mapper = getMapper();
		try {
			MongoDBMapperSql sql = mapper.getSql().get(sqlKey);
			String resultType = sql.getResultType();
			Map<String, Object> oldMap = dbo.toMap();
			// 数字
			if (RESULT_TYPE_INTEGER.equals(resultType)) {
				Iterator<Object> itr = oldMap.values().iterator();
				if (itr.hasNext()) {
					return Integer.parseInt((String) itr.next());
				} else {
					return 0;
				}
			}

			// 进行属性映射
			Map<String, String> fieldMap = mapper.getResultMap().get(sql.getResultMap());
			Map<String, Object> newMap = new HashMap<String, Object>();
			Iterator<String> keyItr = fieldMap.keySet().iterator();
			while (keyItr.hasNext()) {
				String key = keyItr.next();
				String fieldKey = fieldMap.get(key);
				Object value = oldMap.get(key);
				if (value != null && !"".equals(value)) {
					newMap.put(fieldKey, value);
				} 
			}

			// hashmap
			if (RESULT_TYPE_HASHMAP.equals(resultType)) {
				return newMap;
			} else {
				// 对象
				return BeanUtil.convertToBean(newMap, resultType);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	

	/**
	 * 得到Mapper配置
	 * 
	 * @return
	 */
	private MongoDBMapper getMapper() {
		String id = getMongoMapperId();
		if (_mappers.get(id) == null) {
			MongoDBMapper mapper = (MongoDBMapper) SpringBeanContext.getBean(id);
			if (mapper == null) {
				throw new RuntimeException("根据[mapperId:" + getMongoMapperId() + "]不能获取MongoDB mapper配置!");
			}
			_mappers.put(id, mapper);
		}

		return (_mappers.get(id));
	}

	/**
	 * 得到MonggoDB的集合对象，用于数据库操作
	 * 
	 * @return
	 */
	private DBCollection getCollection() {
		MongoDBMapper mapper = getMapper();
		return mongoTemplate.getCollection(mapper.getCollectionName());
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	
	/**--2017-09-08修改记录--**/
	
	/**
	 * 根据对象将查询结果JSON构造为结果对象
	 * （解决了javabean与mongo的反显字段名不与mysql一致的问题，应用于selectList()、pageQuery()方法）
	 * @param sqlKey
	 * @param dbo
	 * @return
	 */
	private Object toObjectFromJson(String sqlKey, DBObject dbo) {
		MongoDBMapper mapper = getMapper();
		try {
			MongoDBMapperSql sql = mapper.getSql().get(sqlKey);
			String resultType = sql.getResultType();
			
			// 对象
//			dbo.removeField("_id"); //去除mongodb的_id，防止类型转换异常
//			dbo.removeField("sortColumns");
//			dbo.removeField("pageNumber");
//			dbo.removeField("pageSize");

			Map dboMap = dbo.toMap();
			Map newMap = new HashMap();
			Set<String> fields = dbo.keySet();

			for(String field : fields){
				if(dboMap.get(field) != null){
					String s = dboMap.get(field).toString();
					newMap.put(field, s);
				}
			}
            newMap.remove("sortColumns");
            newMap.remove("pageNumber");
            newMap.remove("pageSize");
			Object obj = transMap2Bean(newMap,BeanUtil.newClass(resultType).newInstance());

			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object transMap2Bean(Map<String, Object> map, Object obj) throws Exception{

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (map.containsKey(key)) {
					Object value = map.get(key);
					// 得到property对应的setter方法
					Method setter = property.getWriteMethod();
					if(setter != null){
						Class[] cp = setter.getParameterTypes();
						if(cp[0].isInstance(new Date())){
							String pattern = "EEE MMM dd HH:mm:ss zzz yyyy";
							SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.US);
							Date date = df.parse(value.toString());
							setter.invoke(obj, date);
							continue;
						}
						if(cp[0].isInstance(new Long(1))){
							setter.invoke(obj, Long.parseLong(value.toString()));
							continue;
						}
                        if(cp[0].isInstance(new Integer(1))){
                            setter.invoke(obj, Integer.parseInt(value.toString()));
                            continue;
                        }
						setter.invoke(obj, value);
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 建立Mongo索引
	 * （解决了数据量过大时mongo分页查询RAM溢出问题，应用于selectList()、pageQuery()方法）
	 * @param sortDbo
	 * @return
	 */
	public void createIndex(DBObject sortDbo){
		Map<String,Integer> mapSort = sortDbo.toMap(); //转化排序字段为map
		List<DBObject> listIndex = getCollection().getIndexInfo();
		int flag = 1; // 1:创建索引    0:不创建
		for (String index : mapSort.keySet()) {
			for (DBObject dbObject : listIndex) {
				Map<String,Integer> map = (Map<String, Integer>) dbObject.get("key");
				for (String string : map.keySet()) {
					if(!StringUtils.isEmpty(index) && index.equals(string) && mapSort.get(index).equals(map.get(string))){
						flag = 0; //若已有相同索引，则不创建
					}
				}
			}
			if(flag == 1){
				getCollection().createIndex(new BasicDBObject(index, mapSort.get(index)));//建立索引
			}
		}
	}
	
}
