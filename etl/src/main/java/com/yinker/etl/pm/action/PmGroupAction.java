package com.yinker.etl.pm.action;

import com.yinker.etl.pm.model.PmDataSourceQuery;
import com.yinker.etl.pm.model.base.*;
import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.pm.service.PmDatasourceGroupService;
import com.yinker.etl.pm.service.PmGroupUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import com.yinker.etl.pm.model.PmGroupQuery;
import com.yinker.etl.pm.service.PmGroupService;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.model.RbacUserQuery;
import org.zwork.srdp.rbac.model.base.RbacUser;
import org.zwork.srdp.rbac.portal.RbacService;
import org.zwork.srdp.rbac.portal.rmi.RemoteServiceConstant;
import org.zwork.srdp.rbac.portal.rmi.RemoteServiceManager;
import org.zwork.srdp.rbac.session.User;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmGroup表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-12-15 19:25:48
 * </pre>
 */
public class PmGroupAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmGroupAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmGroup entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmGroupQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmGroupService entityService;

    /* 数据源管理服务类 */
    @Resource(name = "pmDataSourceService")
    private PmDataSourceService pmDataSourceService;
    @Resource(name = "pmDatasourceGroupService")
    private PmDatasourceGroupService pmDatasourceGroupService;
    @Resource(name = "pmGroupUserService")
    private PmGroupUserService pmGroupUserService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;

    /* 数据源类型 */
    protected List<PmDataDictItem> groupStatus;

    /* 数据字典-数据源列表 */
    private List<PmDataSource> srcDataSourceInfos;

    /* 读写数据源 - 目标数据源 */
    private List<PmDataSource> targetDataSourceInfos;

    //权限人员map，用于edit显示
    private Map<String,String> userMap;

    //源、目标数据源、权限人员字符串，用于add和edit
    private String userListString;
    private String srcDbListString;
    private String tgtDbListString;
	
	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new PmGroup();
		queryEntity = new PmGroupQuery();
        userMap = new HashMap<String, String>();

        groupStatus = pmDataDictItemService.selectByDictCode("pm.groupStatus");

        srcDataSourceInfos = pmDataSourceService.selectAll();
        PmDataSourceQuery targetDSQuery = new PmDataSourceQuery();
        targetDSQuery.setDatabaseCategory("2");
        targetDataSourceInfos = pmDataSourceService.selectByEntity(targetDSQuery);
	}
    public PmGroupAction() {
    }

    /**
     * 新增及新增保存页面
     * 
     * @return
     * @throws Exception
     */
    public String create() throws Exception {
        if ("toCreate".equals(operate)) {// 转向新增页面
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存
			// 设置Id
			String id = keyGeneratorContainer.getNextKey("pmGroupId");
            entity.setId(id);
            Date now = new Date();
            entity.setCreateTime(now);
            entity.setLastUpdateTime(now);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOwner(user.getName());
            LOGGER.debug("新增数据，新增前对象信息：{}", entity);

            //向子表插入数据
            entityService.insertDSGroupAndUser(entity, srcDbListString, tgtDbListString, userListString);

            entityService.insert(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("新增数据成功，转向修改页面");


            return EDIT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }

    /**
     * 修改及修改保存页面
     * 
     * @return
     * @throws Exception
     */
    public String edit() throws Exception {
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);

            prepareEdit(entity);

            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);
            Date now = new Date();
            entity.setCreateTime(now);
            entity.setLastUpdateTime(now);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOwner(user.getName());

            //更新子表数据
            entityService.updateDSGroupAndUser(entity, srcDbListString, tgtDbListString, userListString);

            entityService.updateByPK(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("更新数据成功!");
            return PROMPT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }

    /**
     * 准备EDIT显示数据
     *
     * @return
     * @throws Exception
     */
    private void prepareEdit(PmGroup entity) throws Exception{
        List<PmDatasourceGroup> pgList = entity.getPmDatasourceGroups();
        List<PmGroupUser> puList = entity.getPmGroupUsers();

        srcDbListString = "";
        tgtDbListString = "";
        for (PmDatasourceGroup pg : pgList){
            if("0".equals(pg.getDatasourceType())){
                srcDbListString += pg.getDatasourceId() + ",";
            }else if ("1".equals(pg.getDatasourceType())){
                tgtDbListString += pg.getDatasourceId() + ",";
            }
            if(srcDbListString.endsWith(",")){
                srcDbListString.substring(0, srcDbListString.length() - 1);
            }
            if(tgtDbListString.endsWith(",")){
                tgtDbListString.substring(0, tgtDbListString.length() - 1);
            }
        }

        Map<String, String> rbacMap = entityService.getRbacMap();
        for (PmGroupUser pu : puList){
            userMap.put(pu.getName(), rbacMap.get(pu.getName()));
        }

    }

    /**
     * 根据主键删除数据记录
     * 
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {
		String selectedIds = request.getParameter("selectedIds");
		if (StringUtils.isNotEmpty(selectedIds)) {
			String[] idArray = selectedIds.split("\\|");
			for (int i = 0; i < idArray.length; i++) {
                PmGroupPK pk = new PmGroupPK();
				pk.setId(idArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());

                //删除子表数据
                entity.setId(idArray[i]);
                entityService.deleteDSGroupAndUser(entity);

                entityService.deleteByPK(pk);
            }
		}
        return PROMPT;
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     * 
     * @return
     * @throws Exception
     */
    public String view() throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
		PmGroup pmGroup =  entityService.selectByPK(entity);
        if (entity == null) {
           throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            Map<String, String> dsMap = new HashMap<String, String>();
            for (PmDataSource ds : srcDataSourceInfos){
                dsMap.put(ds.getId(), ds.getName());
            }

			entity = entityService.initSrcTgtUserListView(pmGroup, dsMap, entityService.getRbacMap());
		}
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    public String toList(){
        return LIST;
    }

    /**
     * 查询列表页面
     * 
     * @return
     * @throws Exception
     */
    public String list() throws Exception {
        if ("query".equals(operate)) {// 执行数据库查询操作
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            page = entityService.myPageQuery(queryEntity, srcDataSourceInfos);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
            return "list-result";
        } else { //转向查询页面，但不执行数据库查询操作
            return "list-result";
        }
    }
	
	public PmGroup getEntity() {
        return entity;
    }

    public void setEntity(PmGroup entity) {
        this.entity = entity;
    }

    public PmGroupQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(PmGroupQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(PmGroupService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List<PmDataSource> getSrcDataSourceInfos() {
        return srcDataSourceInfos;
    }

    public List<PmDataSource> getTargetDataSourceInfos() {
        return targetDataSourceInfos;
    }

    public List<PmDataDictItem> getGroupStatus() {
        return groupStatus;
    }

    public Map<String, String> getUserMap() {
        return userMap;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserListString() {
        return userListString;
    }

    public void setUserListString(String userListString) {
        this.userListString = userListString;
    }

    public String getSrcDbListString() {
        return srcDbListString;
    }

    public void setSrcDbListString(String srcDbListString) {
        this.srcDbListString = srcDbListString;
    }

    public String getTgtDbListString() {
        return tgtDbListString;
    }

    public void setTgtDbListString(String tgtDbListString) {
        this.tgtDbListString = tgtDbListString;
    }

}
