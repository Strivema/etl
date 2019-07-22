package com.yinker.etl.trans.dao;

import com.yinker.etl.trans.model.base.TransStuInfo;
import com.yinker.etl.trans.model.base.TransStuInfoPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.base.support.AbstractMybatisDao;

import java.util.List;

public class TransStuInfoDao extends AbstractMybatisDao<TransStuInfoPK,TransStuInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransStuInfoDao.class);

    protected static final String NAMESPACE = "TransStuInfo";

    public  String getIbatisMapperNamesapce(){
        return NAMESPACE;
    }
    public TransStuInfo selectByPK(TransStuInfoPK entity){
        String statement = getIbatisMapperNamesapce() +".selectByPK";
     //   System.out.println(getSqlSessionTemplate().selectOne(statement,entity).toString());
        return getSqlSessionTemplate().selectOne(statement,entity);
    }

    @Override
    public List<TransStuInfo> selectByEntity(TransStuInfo entity) {

        String statement = getIbatisMapperNamesapce()+".selectByEntity";
        return getSqlSessionTemplate().selectList(statement,entity);
    }


}
