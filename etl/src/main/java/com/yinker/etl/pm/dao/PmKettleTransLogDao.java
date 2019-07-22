package com.yinker.etl.pm.dao;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmKettleTransLog;
import com.yinker.etl.pm.model.base.PmKettleTransLogPK;
import org.zwork.framework.base.support.Pagination;

import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleTransLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:47
 * </pre>
 */
public class PmKettleTransLogDao extends AbstractMybatisDao<PmKettleTransLogPK, PmKettleTransLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmKettleTransLogDao.class);

    protected static final String NAMESPACE = "PmKettleTransLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    public Pagination myPageQuery(Object queryObject) {
        String statement = getIbatisMapperNamesapce() + ".myPageQuery";
        Pagination page = null;
        try {
            Integer count = (Integer)this.getSqlSessionTemplate().selectOne(statement + "Count", queryObject);
            Integer pageNumber = (Integer) Ognl.getValue("pageNumber", queryObject);
            Integer pageSize = (Integer)Ognl.getValue("pageSize", queryObject);
            RowBounds rowBounds = new RowBounds(pageSize.intValue() * (pageNumber.intValue() - 1), pageSize.intValue());
            List<Object> data = this.getSqlSessionTemplate().selectList(statement, queryObject, rowBounds);
            page = new Pagination();
            page.setData(data);
            page.setPageNumber(pageNumber.intValue());
            page.setPageSize(pageSize.intValue());
            page.setCount(count.intValue());
        } catch (OgnlException var9) {
            LOGGER.error("分页查询出错!", var9);
        }
        return page;
    }
}