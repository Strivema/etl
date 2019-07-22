package com.yinker.etl.mongodb.basedao;

import com.yinker.etl.util.BeanUtil;
import org.zwork.framework.base.BaseDao;
import org.zwork.framework.base.BaseEntityPK;
import org.zwork.framework.base.support.Pagination;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * <b>类描述:</b>基于泛型的MongoDB的dao基本实现
 * <b>作者:</b>李得亮
 * <b>创建日期:</b>2016年11月28日 下午5:17:44
 * </pre>
 */
public abstract class BaseEntityMongoDBDao<EntityPK extends BaseEntityPK, Entity extends EntityPK> extends BaseMongoDBDao implements
		BaseDao<EntityPK, Entity> {
	private static final String INSERT = "insert";
	private static final String UPDATE_BY_PK = "updateByPk";
	private static final String DELETE_BY_PK = "deleteByPk";
	private static final String SELECT_BY_PK = "selectByPk";
	private static final String SELECT_ALL = "selectAll";
	private static final String SELECT_BY_ENTITY = "selectByEntity";
	private static final String SELECT_BY_PAGE = "selectByPage";

	@Override
	public int insert(Entity entity) {
		return insert(INSERT, entity);
	}

	@Override
	public int deleteByPK(EntityPK pk) {
		return delete(DELETE_BY_PK, pk);
	}

	@Override
	public int updateByPK(Entity entity) {
		return update(UPDATE_BY_PK, entity, entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Entity selectByPK(EntityPK pk) {
		return (Entity) selectOne(SELECT_BY_PK, pk);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Entity> selectAll() {
		List<Object> list = selectList(SELECT_ALL, null, null);
		List<Entity> result = new ArrayList<Entity>();
		for (Object o : list) {
			result.add((Entity) o);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Entity> selectByEntity(Entity entity) {
		String sortColumns = (String) BeanUtil.getFieldValue(entity, "sortColumns");
		List<Object> list = selectList(SELECT_BY_ENTITY, entity, sortColumns);
		List<Entity> result = new ArrayList<Entity>();
		for (Object o : list) {
			result.add((Entity) o);
		}
		return result;
	}

	@Override
	public <EntityQueryObject extends Entity> Pagination pageQuery(EntityQueryObject entity) {
		Integer pageNumber = (Integer) BeanUtil.getFieldValue(entity, "pageNumber");
		Integer pageSize = (Integer) BeanUtil.getFieldValue(entity, "pageSize");
		String sortColumns = (String) BeanUtil.getFieldValue(entity, "sortColumns");
		return pageQuery(SELECT_BY_PAGE, entity, pageNumber.intValue(), pageSize.intValue(), sortColumns);
	}

}
