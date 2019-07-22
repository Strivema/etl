package com.yinker.etl.trans.action;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.service.PmGroupService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.EchartData;
import com.yinker.etl.trans.model.TransStatusLogQuery;
import com.yinker.etl.trans.model.TransTimebatchLogQuery;
import com.yinker.etl.trans.model.base.TransStatusLog;
import com.yinker.etl.trans.model.base.TransStatusLogPK;
import com.yinker.etl.trans.model.base.TransTimebatchLog;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransStatusLogService;
import com.yinker.etl.trans.service.TransTimebatchLogService;
import com.yinker.etl.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.common.utils.StringUtils;
import org.zwork.srdp.flow.action.BaseFlowAction;
import org.zwork.srdp.pm.model.base.PmDataDictItem;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.session.User;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>TransStatusLog表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-14 20:12:11
 * </pre>
 */
public class TransStatusLogAction extends BaseFlowAction {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransStatusLogAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected TransStatusLog entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected TransStatusLogQuery queryEntity;

    /* 定义服务层操作类 */
    protected TransStatusLogService entityService;

    /* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    /* 序列键容器 */
    private KeyGeneratorContainer keyGeneratorContainer;

    /* 数据字典-数据源列表 */
    private List<PmDataSource> srcDataSourceInfos;

    /* 读写数据源 - 目标数据源 */
    private List<PmDataSource> targetDataSourceInfos;

    @Resource(name = "transTimebatchLogService")
    private TransTimebatchLogService transTimebatchLogService;

    @Resource(name = "pmGroupService")
    private PmGroupService pmGroupService;

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    /* 配置状态 */
    private List<PmDataDictItem> statusDictItem;
    private List<PmDataDictItem> fullTransType;
    private List<PmDataDictItem> transInfoType;

    private String from;
    private Date queryDate;
    private Date maxDate;
    private Date minDate;
    private String queryStatus;

    /**
     * 查询多少条数据
     */
    private static final Integer SELECT_COUNT = 10;

    /**
     * 执行初始化方法
     */
    public void init () {
        entity = new TransStatusLog();
        queryEntity = new TransStatusLogQuery();

        statusDictItem = pmDataDictItemService.selectByDictCode("transStatusLog.status ");
        fullTransType = pmDataDictItemService.selectByDictCode("trans.fullTransType");
        transInfoType = pmDataDictItemService.selectByDictCode("trans.transInfoType");
    }

    public TransStatusLogAction () {
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
            String id = keyGeneratorContainer.getNextKey("transStatusLogId");
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
                TransStatusLogPK pk = new TransStatusLogPK();
                pk.setId(idArray[i]);
                LOGGER.debug("删除数据:{}", pk.pkString());
                entityService.deleteByPK(pk);
            }

        }
        LOGGER.debug("删除数据:{}", entity.pkString());
        entityService.deleteByPK((TransStatusLogPK) entity);
        return PROMPT;
    }

    /**
     * 状态变更页面
     *
     * @return
     * @throws Exception
     */
    public String changeStatus () throws Exception {
        if ("query".equals(operate)) {
            String selectedIds = request.getParameter("selectedIds");
            if (StringUtils.isNotEmpty(selectedIds)) {
                String[] idArray = selectedIds.split("\\|");
                if (idArray.length > 1) {
                    setWarnMessage("只能选择一行进行状态变更，请勿多选!");
                    return PROMPT;
                }
                TransStatusLogPK pk = new TransStatusLogPK();
                pk.setId(selectedIds);
                entity = entityService.selectByPK(pk);
                if (!"1".equals(entity.getStatus())) {
                    setWarnMessage("只能变更'执行中'的转换状态！");
                    return PROMPT;
                }
            }
        } else if ("submit".equals(operate)) {
            String desc = entity.getTransLog();
            String status = entity.getStatus();
            TransStatusLogPK pk = new TransStatusLogPK();
            pk.setId(entity.getId());
            entity = entityService.selectByPK(pk);
            if (!"1".equals(entity.getStatus())) {
                setWarnMessage("该转换已经执行完毕，不能继续更改状态");
                return PROMPT;
            }
            entity.setTransLog(desc);
            entity.setStatus(status);
            Date date = new Date();
            entity.setEndTime(date);
            long useTime = date.getTime() - entity.getBeginTime().getTime();
            entity.setUseTime((int) useTime);
            entity.setLastUpdateTime(date);
            entityService.updateByPK(entity);
            setInfoMessage("修改成功");
        }
        return "changeStatus";
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
        TransStatusLog transStatusLog = entityService.selectByPK(entity);
        if (entity == null) {
            throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
            entity = transStatusLog;
        }
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    public String toList () {
        return LIST;
    }

    public String toNormalQuery () {
        return "normalQuery";
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
            if ("transStatusList".equals(from)) {
                queryEntity.setSortColumns(" use_time desc");
            } else {
                queryEntity.setSortColumns(" create_time desc");
            }
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
            if ("normalQuery".equals(from)) {
                return "normalQuery-main";
            } else if ("transStatusList".equals(from)) {
                return "transStatus-result";
            } else {
                return "list-result";
            }
        } else { //转向查询页面，但不执行数据库查询操作
            return "list-result";
        }
    }


    public String toShowDetail () {
        if (StringUtils.isEmpty(queryEntity.getBatchNo())) {
            if (entity.getBeginTime() != null) {
                TransTimebatchLogQuery timebatchLogQuery = new TransTimebatchLogQuery();
                timebatchLogQuery.setBeginTime(entity.getBeginTime());
                List<TransTimebatchLog> timebatchLogs = transTimebatchLogService.selectByEntity(timebatchLogQuery);
                if (timebatchLogs != null && timebatchLogs.size() > 0) {
                    queryEntity.setBatchNo(timebatchLogs.get(0).getBatchNo());
                }
            } else {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
                return "showDetail";
            }
        }
        queryEntity.setSortColumns(" use_time desc");
        page = entityService.pageQuery(queryEntity);
        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        return "showDetail";
    }

    public String daychart () {
        queryDate = new Date();
        maxDate = new Date();
        minDate = DateUtil.getAddDays(-6);
        return "daychart";
    }

    public String daychartResult () {
        return "daychart-result";
    }

    /**
     * 平均耗时最高的转换
     */
    public void useTimeMaxTrans () {
        setQueryEntiyInfo();

        List<EchartData> echartDatas = entityService.getUseTimeMaxTransByQuery(queryEntity);

        outJson(JSON.toJSON(echartDatas).toString());
    }

    /**
     * 平均耗时最高的转换
     */
    public void useTimeAvgTrans () {
        setQueryEntiyInfo();
        List<EchartData> echartDatas = entityService.getUseTimeAvgTransByQuery(queryEntity);

        outJson(JSON.toJSON(echartDatas).toString());
    }

    /**
     * 查询指定范围内状态转换率信息
     */
    public void selectImplementationRate () {
        setQueryEntiyInfo();
        queryEntity.setStatus(queryStatus);
        List<EchartData> echartDatas = entityService.selectImplementationRate(queryEntity);
        outJson(JSON.toJSON(echartDatas).toString());
    }

    /**
     * 初始化一些报表信息
     *
     * @return
     */
    private TransStatusLogQuery setQueryEntiyInfo () {
        setDataSourceValue();
        LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
        String[] srcArray = transInfoService.getDSArray(srcDataSourceInfos);
        String[] targetArray = transInfoService.getDSArray(targetDataSourceInfos);
        if (srcArray == null || srcArray.length == 0 || targetArray == null || targetArray.length == 0) {
            queryEntity.setId("9999999999");
        }
        queryEntity.setSrcDBArray(srcArray);
        queryEntity.setTargetDBArray(targetArray);
        try {
            queryEntity.setBeginTimeStart(DateUtil.getTodayStartDateTime(queryDate == null ? new Date() : queryDate));
            queryEntity.setBeginTimeEnd(DateUtil.getTodayEndDateTime(queryDate == null ? new Date() : queryDate));
        } catch(ParseException e) {
            e.printStackTrace();
        }
        queryEntity.setSelectCount(SELECT_COUNT);
        return queryEntity;
    }

    private void setDataSourceValue () {
        User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
        srcDataSourceInfos = pmGroupService.getDataSourceByUserId(user.getId(), TransConstants.DATA_SOURCE_SRC_TABLE);
        targetDataSourceInfos = pmGroupService.getDataSourceByUserId(user.getId(), TransConstants.DATA_SOURCE_DEST_TABLE);
    }

    /**
     * 查询详情
     *
     * @return
     * @throws Exception
     */
    public String showDetail () throws Exception {
        if (StringUtils.isEmpty(queryEntity.getBatchNo())) {
            if (entity.getBeginTime() != null) {
                TransTimebatchLogQuery timebatchLogQuery = new TransTimebatchLogQuery();
                timebatchLogQuery.setBeginTime(entity.getBeginTime());
                List<TransTimebatchLog> timebatchLogs = transTimebatchLogService.selectByEntity(timebatchLogQuery);
                if (timebatchLogs != null && timebatchLogs.size() > 0) {
                    queryEntity.setBatchNo(timebatchLogs.get(0).getBatchNo());
                }
            } else {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
                return "showDetail-result";
            }
        }
        queryEntity.setSortColumns(" use_time desc");
        page = entityService.pageQuery(queryEntity);
        if (page.getCount() <= 0) {
            setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
        }
        LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
        return "showDetail-result";
    }

    public TransStatusLog getEntity () {
        return entity;
    }

    public void setEntity (TransStatusLog entity) {
        this.entity = entity;
    }

    public TransStatusLogQuery getQueryEntity () {
        return queryEntity;
    }

    public void setQueryEntity (TransStatusLogQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService (TransStatusLogService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService (PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }

    public void setKeyGeneratorContainer (KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }

    public List<PmDataDictItem> getStatusDictItem () {
        return statusDictItem;
    }

    public List<PmDataDictItem> getFullTransType () {
        return fullTransType;
    }

    public List<PmDataDictItem> getTransInfoType () {
        return transInfoType;
    }

    public List<PmDataSource> getSrcDataSourceInfos () {
        return srcDataSourceInfos;
    }

    public void setSrcDataSourceInfos (List<PmDataSource> srcDataSourceInfos) {
        this.srcDataSourceInfos = srcDataSourceInfos;
    }

    public List<PmDataSource> getTargetDataSourceInfos () {
        return targetDataSourceInfos;
    }

    public void setTargetDataSourceInfos (List<PmDataSource> targetDataSourceInfos) {
        this.targetDataSourceInfos = targetDataSourceInfos;
    }

    public String getFrom () {
        return from;
    }

    public void setFrom (String from) {
        this.from = from;
    }

    @Override
    public String getDeployUrl () {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public void outJson (String json) {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(JSON.toJSON(json));
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Date getQueryDate () {
        return queryDate;
    }

    public void setQueryDate (Date queryDate) {
        this.queryDate = queryDate;
    }

    public Date getMaxDate () {
        return maxDate;
    }

    public void setMaxDate (Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMinDate () {
        return minDate;
    }

    public void setMinDate (Date minDate) {
        this.minDate = minDate;
    }

    public String getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus (String queryStatus) {
        this.queryStatus = queryStatus;
    }
}
