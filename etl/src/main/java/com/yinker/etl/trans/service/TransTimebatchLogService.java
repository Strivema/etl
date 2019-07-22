package com.yinker.etl.trans.service;

import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.dao.TransTimebatchLogDao;
import com.yinker.etl.trans.model.*;
import com.yinker.etl.trans.model.base.*;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>TransTimebatchLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:03
 * </pre>
 */
public class TransTimebatchLogService extends BaseService<TransTimebatchLogPK, TransTimebatchLog, TransTimebatchLogDao> implements org.zwork.framework.base.BaseService<TransTimebatchLogPK, TransTimebatchLog> {

    @Resource(name = "transStatusLogService")
    private TransStatusLogService transStatusLogService;

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;
    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    /**
     * 查询最后一条内容
     */
    public TransTimebatchLog selectLastOneColumn () {
        return modelDao.selectLastOneColumn();
    }

    /**
     * 查询正在跑批的任务
     *
     * @param transStatusLogQuery
     * @return
     */
    public List<TransTimebatchLog> selectRunningTrans (TransTimebatchLogQuery transStatusLogQuery) {
        return modelDao.selectRunningTrans(transStatusLogQuery);
    }

    /**
     * 统计用时
     *  根据前端传过来的 type、transType、tableName进行数据筛选
     * @param queryEntity
     * @return
     */
    public TimeAnalyzeJson getTimeAnalyzeJson (TransTimebatchLogQuery queryEntity) {
        TimeAnalyzeJson timeAnalyzeJson = new TimeAnalyzeJson();
        List<String[]> dataList = new ArrayList<>();
        // 查询 N 小时之前
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 24*7);

        // 如果TableName有值，则去查询transStatusLog表的状态
        if (StringUtils.isNotEmpty(queryEntity.getTableName())) {

            TransStatusLogQuery query = new TransStatusLogQuery();
            query.setBeginTimeStart(calendar.getTime());
            query.setTableName(queryEntity.getTableName());
            query.setTransType(queryEntity.getTransType());
            query.setType(queryEntity.getType());
            query.setSortColumns(" begin_time asc");
            List<TransStatusLog> statusLogs = transStatusLogService.selectByEntity(query);

            for(TransStatusLog timeLog : statusLogs) {
                String[] strs = new String[2];
                strs[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeLog.getBeginTime());
                strs[1] = timeLog.getUseTime() + "";
                dataList.add(strs);
            }

        } else {
            // 如果tableName 为空则查询所有tableName
            //查询一周内的跑批任务
            TransTimebatchLogQuery query = new TransTimebatchLogQuery();
            query.setBeginTimeStart(calendar.getTime());
            query.setTransType(queryEntity.getTransType());
            query.setType(queryEntity.getType());
            query.setSortColumns(" begin_time asc");
            List<TransTimebatchLog> timebatchLogs = selectByEntity(query);

            for(TransTimebatchLog timeLog : timebatchLogs) {
                String[] strs = new String[2];
                strs[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeLog.getBeginTime());
                strs[1] = timeLog.getUseTime() + "";
                dataList.add(strs);
            }
        }

        if (dataList.size() > 100) {
            timeAnalyzeJson.setStartValue(dataList.get(dataList.size() - 100)[0]);
        } else if (dataList != null && dataList.size() > 0) {
            timeAnalyzeJson.setStartValue(dataList.get(0)[0]);
        }
        timeAnalyzeJson.setDataList(dataList);
        return timeAnalyzeJson;
    }

    public List<SelectJson> getTransTableNameList(TransTimebatchLogQuery query){

        List<SelectJson> selectJsons = new ArrayList<>();
        SelectJson sj = null;
        // ETL抽取配置
        if(Arrays.binarySearch(TransConstants.ETL_CONTANTS_TRANS_TYPE, query.getTransType())>=0){
            TransInfoQuery transInfoQuery = new TransInfoQuery();
            transInfoQuery.setTransType(query.getTransType());
            transInfoQuery.setType(query.getType());
            transInfoQuery.setGroupByCloume("target_tb_code");
            transInfoQuery.setSortColumns(" target_db_code,target_tb_code asc");
            List<TransInfo> transInfos = transInfoService.selectByEntity(transInfoQuery);
            for(TransInfo info : transInfos) {
                sj = new SelectJson();
                sj.setId(info.getTargetTbCode());
                sj.setName(info.getTargetDbCode()+" -> "+info.getTargetTbCode());
                selectJsons.add(sj);
            }
        }
        // 自定义结构抽取配置
        if(Arrays.binarySearch(TransConstants.DIYSTRUCT_CONTANTS_TRANS_TYPE, query.getTransType())>=0){
            TransTableStructureInfoQuery structureInfoQuery = new TransTableStructureInfoQuery();
            // 在trans_tatus_log里
            query.setTransType(TransConstants.FULL_TRANS_TYPE_5.equals(query.getTransType())?TransConstants.TRANS_TYPE_2:TransConstants.TRANS_TYPE_3);
            structureInfoQuery.setTransType(query.getTransType());
            structureInfoQuery.setType(query.getType());
            structureInfoQuery.setGroupByCloume("target_tb_code");
            structureInfoQuery.setSortColumns(" target_db_code,target_tb_code asc");
            List<TransTableStructureInfo> transInfos = transTableStructureInfoService.selectByEntity(structureInfoQuery);
            for(TransTableStructureInfo info : transInfos) {
                sj = new SelectJson();
                sj.setId(info.getTargetTbCode());
                sj.setName(info.getTargetDbCode()+" -> "+info.getTargetTbCode());
                selectJsons.add(sj);
            }
        }
        return selectJsons;

    }
}
