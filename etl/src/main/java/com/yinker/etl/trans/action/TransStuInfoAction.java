package com.yinker.etl.trans.action;

import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.TransStuInfoQuery;
import com.yinker.etl.trans.model.base.TransStuInfo;
import com.yinker.etl.trans.model.base.TransStuInfoPK;
import com.yinker.etl.trans.service.TransStuInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.common.utils.SystemUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.session.User;

import java.util.Date;
import java.util.List;

public class TransStuInfoAction extends BaseFlowAction{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransStuInfoAction.class);

    protected TransStuInfo entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected TransStuInfoQuery queryEntity;

    /* 定义服务层操作类 */
    protected TransStuInfoService entityService;

    public void init(){
        entity = new TransStuInfo();
        queryEntity  =new TransStuInfoQuery();
        entityService = new TransStuInfoService();
    }

    public String view () throws Exception {

        setDataSourceValue();
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
        TransStuInfo transStuInfo = entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            entity = transStuInfo;
        }
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }


    public String list() throws Exception{
        setDataSourceValue();
        LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
        queryEntity.setSortColumns(" last_update_date desc");

       /* page = entityService.pageQuery(queryEntity);
        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }*/
        //LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());


        return LIST_RESULT;
    }

    private String create() throws Exception{

       // setDataSourceValue();
        if ("toCreate".equals(operate)) {

            return CREATE;
        } else if ("save".equals(operate)) {

            TransStuInfoQuery transStuInfoQuery = new TransStuInfoQuery();
            transStuInfoQuery.setStuNo(entity.getStuNo());
            transStuInfoQuery.setStuName(entity.getStuName());
            transStuInfoQuery.setStuClazz(entity.getStuClazz());
            transStuInfoQuery.setStuMajor(entity.getStuMajor());
            transStuInfoQuery.setStuGender(entity.getStuGender());
            transStuInfoQuery.setStuBirth(entity.getStuBirth());
            transStuInfoQuery.setStuEnrolTime(entity.getStuEnrolTime());
            entity.setCreateTime(new Date());
            entity.setLastUpdateTime(new Date());

            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
            entityService.insertTransStuInfo(entity);

            setInfoMessage("操作成功!");
            LOGGER.debug("新增数据成功，转向修改页面");
            return EDIT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }
    public String edit () throws Exception {
        setDataSourceValue();
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存

            entity.setLastUpdateTime(new Date());

            LOGGER.debug("更新数据：{}", entity);
            entityService.updateTransStuInfo(entity);
            LOGGER.debug("更新Quartz定时任务");

            setInfoMessage("操作成功!");
            LOGGER.debug("更新数据成功!");
            return PROMPT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }

    public String delete () throws Exception {
        String selectedIds = request.getParameter("selectedIds");
        if (StringUtils.isNotEmpty(selectedIds)) {
            String[] idArray = selectedIds.split("\\|");
            StringBuffer sb = new StringBuffer();
            int noDeleteCount = 0;
            for(int i = 0; i < idArray.length; i++) {
                TransStuInfoPK pk = new TransStuInfoPK();
                pk.setId(idArray[i]);
                TransStuInfo info = entityService.selectByPK(pk);

                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }
            LOGGER.info("本次共选择【{}】条，成功删除【{}】条", idArray.length, idArray.length - noDeleteCount);
            if (noDeleteCount > 0) {
                LOGGER.info("转换名为：{}的状态为开启，故未成功删除", sb.toString());
            }
            setInfoMessage("本次共选择" + idArray.length + "条，成功删除" + (idArray.length - noDeleteCount) + "条!" + (noDeleteCount > 0 ? "<br>转换名为：" + sb.toString() + "的状态为开启，固未成功删除" : ""));
        }
        return PROMPT;
    }

    public String toList () {
        return LIST;
    }

    private void setDataSourceValue () {
        User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);

    }

    public TransStuInfo getEntity() {
        return entity;
    }

    public void setEntity(TransStuInfo entity) {
        this.entity = entity;
    }

    public TransStuInfoQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(TransStuInfoQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public TransStuInfoService getEntityService() {
        return entityService;
    }

    public void setEntityService(TransStuInfoService entityService) {
        this.entityService = entityService;
    }
}
