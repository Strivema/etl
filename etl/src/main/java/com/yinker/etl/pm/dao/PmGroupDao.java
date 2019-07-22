package com.yinker.etl.pm.dao;

import com.yinker.etl.pm.model.base.*;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zwork.framework.base.support.AbstractMybatisDao;
import org.zwork.framework.base.support.Pagination;
import org.zwork.srdp.rbac.model.RbacUserQuery;
import org.zwork.srdp.rbac.model.base.RbacUser;
import org.zwork.srdp.rbac.portal.RbacService;
import org.zwork.srdp.rbac.portal.rmi.RemoteServiceConstant;
import org.zwork.srdp.rbac.portal.rmi.RemoteServiceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmGroup表对应的数据库操作类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:48
 * </pre>
 */
public class PmGroupDao extends AbstractMybatisDao<PmGroupPK, PmGroup> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PmGroupDao.class);

    protected static final String NAMESPACE = "PmGroup";



    @Override
    public String getIbatisMapperNamesapce() {
        return NAMESPACE;
    }


    public Pagination myPageQuery(Object queryObject, List<PmDataSource> dsInfos) throws Exception{
        String statement = getIbatisMapperNamesapce() + ".selectByPage";
        Pagination page = null;
        try {
            Integer count = (Integer)this.getSqlSessionTemplate().selectOne(statement + "Count", queryObject);
            Integer pageNumber = (Integer) Ognl.getValue("pageNumber", queryObject);
            Integer pageSize = (Integer)Ognl.getValue("pageSize", queryObject);
            RowBounds rowBounds = new RowBounds(pageSize.intValue() * (pageNumber.intValue() - 1), pageSize.intValue());
            List<PmGroup> data = this.getSqlSessionTemplate().selectList(statement, queryObject, rowBounds);
            List<PmGroup> returnData = new ArrayList<PmGroup>();
            Map<String, String> dsMap = new HashMap<String, String>();
            Map<String, String> rbacMap = getRbacMap();
            for (PmDataSource ds : dsInfos){
                dsMap.put(ds.getId(), ds.getName());
            }

            for(PmGroup pg : data){
                returnData.add(initSrcTgtUserListView(pg, dsMap, rbacMap));
            }

            page = new Pagination();
            page.setData(returnData);
            page.setPageNumber(pageNumber.intValue());
            page.setPageSize(pageSize.intValue());
            page.setCount(count.intValue());
        } catch (OgnlException var9) {
            LOGGER.error("分页查询出错!", var9);
        }
        return page;
    }

    public PmGroup initSrcTgtUserListView(PmGroup pg, Map dsMap, Map rbacMap) {
        String srcListView = "";
        String tgtListView = "";
        String userListView = "";
        for (PmDatasourceGroup pdg : pg.getPmDatasourceGroups()){
            if("0".equals(pdg.getDatasourceType())){
                srcListView += dsMap.get(pdg.getDatasourceId()) + ", ";
            }else if("1".equals(pdg.getDatasourceType())){
                tgtListView += dsMap.get(pdg.getDatasourceId()) + ", ";
            }
        }
        for (PmGroupUser pgu : pg.getPmGroupUsers()){
            userListView += rbacMap.get(pgu.getName()) + ", ";
        }

        if(srcListView.endsWith(", ")){
            srcListView = srcListView.substring(0, srcListView.length() - 2);
        }
        if(tgtListView.endsWith(", ")){
            tgtListView = tgtListView.substring(0, tgtListView.length() - 2);
        }
        if(userListView.endsWith(", ")){
            userListView = userListView.substring(0, userListView.length() - 2);
        }

        pg.setSrcListView(srcListView);
        pg.setTgtListView(tgtListView);
        pg.setUserListView(userListView);

        return pg;
    }

    public Map<String, String> getRbacMap() throws Exception{
        RbacService rbacService = (RbacService) RemoteServiceManager.instance().getService(RemoteServiceConstant.SERVICE_RBAC_SERVICE);
        RbacUserQuery rq = new RbacUserQuery();
        rq.setState("1");
        List<RbacUser> rbacList = rbacService.getRbacUsers(rq);
        Map<String, String> rbacMap = new HashMap<String, String>();
        for (RbacUser ru : rbacList){
            rbacMap.put(ru.getId(), ru.getUserName());
        }
        return rbacMap;
    }


}