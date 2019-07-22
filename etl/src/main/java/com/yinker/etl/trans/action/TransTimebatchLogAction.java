package com.yinker.etl.trans.action;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.trans.model.SelectJson;
import com.yinker.etl.trans.model.TransTimebatchLogQuery;
import com.yinker.etl.trans.model.base.TimeAnalyzeJson;
import com.yinker.etl.trans.model.base.TransTimebatchLog;
import com.yinker.etl.trans.model.base.TransTimebatchLogPK;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransTimebatchLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>TransTimebatchLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:03
 * </pre>
 */
public class TransTimebatchLogAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransTimebatchLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected TransTimebatchLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected TransTimebatchLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected TransTimebatchLogService entityService;

    /* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    /* 转换时间类型 */
    private List<PmDataDictItem> dataDictItemTransTypes;
    /* 转换类型 */
    private List<PmDataDictItem> dataDictItemTypes;
    /* 表名集合 */
    private List<SelectJson> tableNameList;

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    /**
     * 执行初始化方法
     */
    public void init () {
        entity = new TransTimebatchLog();
        queryEntity = new TransTimebatchLogQuery();
        dataDictItemTransTypes = pmDataDictItemService.selectByDictCode("trans.transInfoType");
        dataDictItemTypes = pmDataDictItemService.selectByDictCode("trans.fullTransType");
        /*for(int i = 0; i < dataDictItemTypes.size(); i++) {
            PmDataDictItem type = dataDictItemTypes.get(i);
            if(TransConstants.FULL_TRANS_TYPE_3.equals(type.getItemCode())||TransConstants.FULL_TRANS_TYPE_6.equals(type.getItemCode())){
                dataDictItemTypes.remove(i);
            }
        }*/
        tableNameList = new ArrayList<>();
    }

    public TransTimebatchLogAction () {
    }

    /**
     * 新增及新增保存页面
     *
     * @return
     * @throws Exception
     */
    public String create () throws Exception {
        if ("toCreate".equals(operate)) {// 转向新增页面
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存
            // 设置Id
            String id = keyGeneratorContainer.getNextKey("transTimebatchLogId");
            entity.setId(id);
            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
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
    public String edit () throws Exception {
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            LOGGER.debug("更新数据：{}", entity);
            entityService.updateByPK(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("更新数据成功!");
            return PROMPT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }

    /**
     * 根据主键删除数据记录
     *
     * @return
     * @throws Exception
     */
    public String delete () throws Exception {
        String selectedIds = request.getParameter("selectedIds");
        if (StringUtils.isNotEmpty(selectedIds)) {
            String[] idArray = selectedIds.split("\\|");
            for(int i = 0; i < idArray.length; i++) {
                TransTimebatchLogPK pk = new TransTimebatchLogPK();
                pk.setId(idArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }

        }
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((TransTimebatchLogPK) entity);
        return PROMPT;
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     *
     * @return
     * @throws Exception
     */
    public String view () throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
        TransTimebatchLog transTimebatchLog = entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            entity = transTimebatchLog;
        }
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    /**
     * 查询列表页面
     *
     * @return
     * @throws Exception
     */
    public String list () throws Exception {
        if ("query".equals(operate)) {// 执行数据库查询操作
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
            return LIST;
        } else { //转向查询页面，但不执行数据库查询操作
            return LIST;
        }
    }

    /**
     * 抽取时间分析
     *
     * @return
     */
    public String timeAnalyze () {
        return "timeAnalyze";
    }

    /**
     * 抽取时间分析 - 功能查询界面
     *
     * @return
     */
    public String transPerformance () {
        queryEntity.setTransType(StringUtils.isNotEmpty(queryEntity.getTransType())?queryEntity.getTransType():"1");
        tableNameList = entityService.getTransTableNameList(queryEntity);
        return "transPerformance";
    }

    public void getTableNameJson(){
        queryEntity.setTransType(StringUtils.isNotEmpty(queryEntity.getTransType())?queryEntity.getTransType():"1");
        tableNameList = entityService.getTransTableNameList(queryEntity);
        String json = JSON.toJSONString(tableNameList);
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 抽取时间分析
     *
     * @return
     */
    public void timeAnalyzeJson () {
        TimeAnalyzeJson timeAnalyzeJson = new TimeAnalyzeJson();
        timeAnalyzeJson = entityService.getTimeAnalyzeJson(queryEntity);
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(JSON.toJSON(timeAnalyzeJson));
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }



    public TransTimebatchLog getEntity () {
        return entity;
    }

    public void setEntity (TransTimebatchLog entity) {
        this.entity = entity;
    }

    public TransTimebatchLogQuery getQueryEntity () {
        return queryEntity;
    }

    public void setQueryEntity (TransTimebatchLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService (TransTimebatchLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService (PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }

    public void setKeyGeneratorContainer (KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List<PmDataDictItem> getDataDictItemTransTypes () {
        return dataDictItemTransTypes;
    }

    public void setDataDictItemTransTypes (List<PmDataDictItem> dataDictItemTransTypes) {
        this.dataDictItemTransTypes = dataDictItemTransTypes;
    }

    public List<PmDataDictItem> getDataDictItemTypes () {
        return dataDictItemTypes;
    }

    public void setDataDictItemTypes (List<PmDataDictItem> dataDictItemTypes) {
        this.dataDictItemTypes = dataDictItemTypes;
    }

    public List<SelectJson> getTableNameList () {
        return tableNameList;
    }

    public void setTableNameList (List<SelectJson> tableNameList) {
        this.tableNameList = tableNameList;
    }
}
