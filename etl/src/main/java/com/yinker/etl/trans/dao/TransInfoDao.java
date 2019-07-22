package com.yinker.etl.trans.dao;

import com.yinker.etl.trans.model.TransInfoQuery;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.model.base.TransInfoPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.base.support.AbstractMybatisDao;
import org.zwork.framework.base.support.Pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>TransInfo表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:57
 * </pre>
 */
public class TransInfoDao extends AbstractMybatisDao<TransInfoPK, TransInfo> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TransInfoDao.class);

    protected static final String NAMESPACE = "TransInfo";

    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }


    /**
     * 获取配置表的id和transName字段
     * @return
     */
    public List<Map<String,Object>> selectIdAndTransNameMap() {

        String statement = getIbatisMapperNamesapce() + ".selectIdAndTransNameMap";

        return getSqlSessionTemplate().selectList(statement);
    }

    public Pagination getTransErrorList(TransInfoQuery entityQuery){
        return super.pageQuery("selectTransErrorList",entityQuery);
    }

    public void toCreateData(String sql){
        String statement = getIbatisMapperNamesapce() + ".toCreateData";

        getSqlSessionTemplate().insert(statement,sql);
    }

    public List<String> selectIdByTransName(TransInfoQuery entityQuery) {
        String statement = getIbatisMapperNamesapce() + ".selectIdByTransName";

        return getSqlSessionTemplate().selectList(statement, entityQuery);
    }

    public Integer selectTransCount() {
        String statement = getIbatisMapperNamesapce() + ".selectTransCount";

        return getSqlSessionTemplate().selectOne(statement);
    }

    public void updateDbCode(Map map) {
        String statement = getIbatisMapperNamesapce() + ".updateDbCode";
        getSqlSessionTemplate().update(statement, map);
        statement = getIbatisMapperNamesapce() + ".updateDbCode2";
        getSqlSessionTemplate().update(statement, map);
    }

    public List<HashMap<String, Object>> superManagerSelect(String sql){
        String statement = getIbatisMapperNamesapce() + ".superManagerSelect";
        return getSqlSessionTemplate().selectList(statement, sql);
    }

    public List<Map<String,Object>> getTransDayReport(String email) {
        String statement = getIbatisMapperNamesapce()+".getTransDayReport";
        return getSqlSessionTemplate().selectList(statement, email);
    }

    public List<Map<String,Object>> getErrorRecordDayReport(String email) {
        String statement = getIbatisMapperNamesapce()+".getErrorRecordDayReport";
        return getSqlSessionTemplate().selectList(statement, email);
    }

    public List<Map<String,Object>> getErrorTSDayReport(String email) {
        String statement = getIbatisMapperNamesapce()+".getErrorTSDayReport";
        return getSqlSessionTemplate().selectList(statement, email);
    }

    public List<Map<String,Object>> getDayReportUsers() {
        String statement = getIbatisMapperNamesapce()+".getDayReportUsers";
        return getSqlSessionTemplate().selectList(statement);
    }
    public List<Map<String,Object>> getExceptionReportUsers() {
        String statement = getIbatisMapperNamesapce()+".getExceptionReportUsers";
        return getSqlSessionTemplate().selectList(statement);
    }

}