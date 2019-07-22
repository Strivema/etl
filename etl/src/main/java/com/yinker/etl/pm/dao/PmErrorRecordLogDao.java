package com.yinker.etl.pm.dao;

import com.yinker.etl.pm.model.PmErrorRecordLogQuery;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import com.yinker.etl.pm.model.base.PmErrorRecordLog;
import com.yinker.etl.pm.model.base.PmErrorRecordLogPK;
import org.zwork.framework.base.support.Pagination;

import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorRecordLog表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:50
 * </pre>
 */
public class PmErrorRecordLogDao extends AbstractMybatisDao<PmErrorRecordLogPK, PmErrorRecordLog> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmErrorRecordLogDao.class);

    protected static final String NAMESPACE = "PmErrorRecordLog";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }

    public Pagination pageQueryView(PmErrorRecordLogQuery queryObject) {
        String statement = getIbatisMapperNamesapce() + ".pageQueryView";
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