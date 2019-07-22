package com.yinker.etl.pm.service;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.dao.PmGroupDao;
import com.yinker.etl.pm.model.PmDatasourceGroupQuery;
import com.yinker.etl.pm.model.PmGroupQuery;
import com.yinker.etl.pm.model.PmGroupUserQuery;
import com.yinker.etl.pm.model.base.*;
import com.yinker.etl.trans.TransConstants;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.framework.base.support.BaseService;
import org.zwork.framework.base.support.Pagination;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmGroup表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:49
 * </pre>
 */
public class PmGroupService extends BaseService<PmGroupPK, PmGroup, PmGroupDao> implements
                org.zwork.framework.base.BaseService<PmGroupPK, PmGroup> {

    /* 序列键容器 */
    @Resource(name = "etlKeyGeneratorContainerPm")
    private KeyGeneratorContainer keyGeneratorContainer;

    @Resource(name = "pmDatasourceGroupService")
    private PmDatasourceGroupService pmDatasourceGroupService;

    @Resource(name = "pmGroupUserService")
    private PmGroupUserService pmGroupUserService;

    /**
     * 向子表插入数据
     *
     * @return
     */
    public void insertDSGroupAndUser(PmGroup entity, String srcDbListString, String tgtDbListString, String userListString){
        String[] srcList = srcDbListString.split(",");
        String[] tgtList = tgtDbListString.split(",");
        PmDatasourceGroup pg = new PmDatasourceGroup();
        pg.setGroupId(entity.getId());
        pg.setCreateTime(entity.getCreateTime());
        pg.setOwner(entity.getOwner());
        pg.setLastUpdateTime(entity.getLastUpdateTime());

        for(String dbId : srcList){
            pg.setId(keyGeneratorContainer.getNextKey("pmDatasourceGroupId"));
            pg.setDatasourceId(dbId.trim());
            pg.setDatasourceType(TransConstants.DATA_SOURCE_SRC_TABLE);
            pmDatasourceGroupService.insert(pg);
        }
        for(String dbId : tgtList){
            pg.setId(keyGeneratorContainer.getNextKey("pmDatasourceGroupId"));
            pg.setDatasourceId(dbId.trim());
            pg.setDatasourceType(TransConstants.DATA_SOURCE_DEST_TABLE);
            pmDatasourceGroupService.insert(pg);
        }

        String[] userList = userListString.split(",");
        PmGroupUser pu = new PmGroupUser();
        pu.setGroupId(entity.getId());
        pu.setCreateTime(entity.getCreateTime());
        pu.setOwner(entity.getOwner());
        pu.setLastUpdateTime(entity.getLastUpdateTime());
        for(String userId : userList){
            pu.setId(keyGeneratorContainer.getNextKey("pmGroupUserId"));
            pu.setName(userId.trim());
            pmGroupUserService.insert(pu);
        }
    }

    /**
     * 更新子表数据
     *
     * @return
     */
    public void updateDSGroupAndUser(PmGroup entity, String srcDbListString, String tgtDbListString, String userListString) {
        deleteDSGroupAndUser(entity);
        insertDSGroupAndUser(entity, srcDbListString, tgtDbListString, userListString);
    }

    /**
     * 删除子表数据
     *
     * @return
     */
    public void deleteDSGroupAndUser(PmGroup entity){
        PmGroupUserQuery puq = new PmGroupUserQuery();
        puq.setGroupId(entity.getId());
        pmGroupUserService.deleteByPK(puq); //通过group_id删除

        PmDatasourceGroupQuery pgq = new PmDatasourceGroupQuery();
        pgq.setGroupId(entity.getId());
        pmDatasourceGroupService.deleteByPK(pgq); //通过group_id删除
    }

    /**
     * 初始化VIEW显示条件
     *
     * @return
     */
    public PmGroup initSrcTgtUserListView(PmGroup pg, Map dsMap, Map rbacMap){
        return modelDao.initSrcTgtUserListView(pg, dsMap, rbacMap);
    }

    /**
     * 自定义分页查询
     *
     * @return
     */
    public <EntityQueryObject extends PmGroup> Pagination myPageQuery(EntityQueryObject queryObject, List<PmDataSource> infos) throws Exception {
        return modelDao.myPageQuery(queryObject, infos);
    }

    /**
     * 通过用户名查询出对应组的数据源
     * @param userId 用户编号
     * @return
     */
    public List<PmDataSource> getDataSourceByUserId(String userId,String datasourceType){
        List<PmDataSource> dataSources = new ArrayList<>();
        PmGroupUserQuery groupUserQuery = new PmGroupUserQuery();
        groupUserQuery.setName(userId);
        List<PmGroupUser> pmGroupUsers = pmGroupUserService.selectByEntity(groupUserQuery);
        Map<String,PmDataSource> dataSourceMap = new HashMap<>();
        for(PmGroupUser groupUser : pmGroupUsers) {
            PmGroup group = getDataSourceByGroupId(groupUser.getGroupId());
            if(group != null){
                List<PmDatasourceGroup> dsGroups = new ArrayList<>();
                if(TransConstants.DATA_SOURCE_SRC_TABLE.equals(datasourceType)){
                    for(PmDatasourceGroup group1 : group.getPmDatasourceGroups()) {
                        if (TransConstants.DATA_SOURCE_SRC_TABLE.equals(group1.getDatasourceType())) {
                            dataSourceMap.put(group1.getPmDataSource().getId(),group1.getPmDataSource());
                        }
                    }
                }
                if(TransConstants.DATA_SOURCE_DEST_TABLE.equals(datasourceType)){
                    for(PmDatasourceGroup group1 : group.getPmDatasourceGroups()) {
                        if (TransConstants.DATA_SOURCE_DEST_TABLE.equals(group1.getDatasourceType())) {
                            dataSourceMap.put(group1.getPmDataSource().getId(),group1.getPmDataSource());
                        }
                    }
                }
            }
        }
        for(PmDataSource pdg :dataSourceMap.values()){
            dataSources.add(pdg);
        }
        return dataSources;
    }

    public PmGroup getDataSourceByGroupId(String groupId){
        PmGroupQuery groupQuery = new PmGroupQuery();
        groupQuery.setId(groupId);
        groupQuery.setStatus(PMConstants.DATA_SOURCE_GROUP_STATE);
        List<PmGroup> groups = selectByEntity(groupQuery);
        if(groups!=null && groups.size()>0){
            return groups.get(0);
        }else{
            return null;
        }
    }

    public Map<String, String> getRbacMap() throws Exception{
        return modelDao.getRbacMap();
    }
}
