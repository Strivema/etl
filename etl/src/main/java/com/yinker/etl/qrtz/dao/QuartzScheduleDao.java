package com.yinker.etl.qrtz.dao;

import com.yinker.etl.qrtz.model.base.QuartzSchedule;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class QuartzScheduleDao extends SqlSessionDaoSupport {
	
	@SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzScheduleDao.class);

    protected static final String NAMESPACE = "QuartzSchedule";

    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }
    
    protected SqlSession getSqlSessionTemplate() { return getSqlSession(); }

    /**
     * 判断是否存在
     * @param entity
     * @return
     */
    public QuartzSchedule exists(QuartzSchedule entity) {
    	
    	String statement = getIbatisMapperNamesapce() + ".exists";
	    if (entity == null) {
	    this.logger.warn("查询条件为null,执行全表查询!");
	    }
        List<QuartzSchedule> list = getSqlSessionTemplate().selectList(statement, entity);
        QuartzSchedule quartzSchedule = null;
        if(list.size() > 0){
            quartzSchedule = list.get(0);
        }
        return quartzSchedule;
    }
}
