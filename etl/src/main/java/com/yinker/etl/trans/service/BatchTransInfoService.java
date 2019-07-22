package com.yinker.etl.trans.service;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.model.base.TransInfo;
import com.yinker.etl.trans.model.base.TransStatusLog;
import com.yinker.etl.trans.service.trans.SimpleTrans;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>表抽取转换处理类。
 * <b>创建日期:</b>2017年11月14日16:04:47
 * </pre>
 * @author 崔博文
 */
public class BatchTransInfoService extends TransInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTransInfoService.class);

    @Resource(name = "simpleTrans")
    private SimpleTrans simpleTrans;

    @Resource(name = "transStatusLogService")
    private TransStatusLogService transStatusLogService;

    // 用来查询的关键字：表字段
    public static final String[] keyLookup = {"id"};

    /**
     * 用来查询的关键字：流里的字段1
     */
    public static final String keyStream[] = {"id"};

    // 用来查询的关键字：比较符
    public static String[] keyCondition = {"="};

    public void tableDataTrans (JSONObject json) {

        //判断此条转换是否正在执行
        List<TransStatusLog> etlTransStatusLogs = transStatusLogService.isTransRunning(json.get("id").toString());
        if (etlTransStatusLogs != null && etlTransStatusLogs.size() > 0) {
            //插入正在执行的记录
            String log = "此条转换有其他线程(批次:" + etlTransStatusLogs.get(0).getBatchNo() + ",服务器IP：" + etlTransStatusLogs.get(0).getOsName() + ",线程：" + etlTransStatusLogs.get(0).getTransThread() + ")正在执行，本次请求将不进行转换！";
            LOGGER.info(log);
            transStatusLogService.insertRunningTrance(json.get("id").toString(), json.get("transName").toString(), json.get("batchNo").toString(), json.get("jobName").toString(), json.get("type").toString(), json.get("targetTbCode").toString(), json.get("transType").toString(), log);
        } else {
            // 插入开始记录日志
            String insertLogID = transStatusLogService.insertBeginTrance(json.get("id").toString(), json.get("transName").toString(), json.get("batchNo").toString(), json.get("jobName").toString(), json.get("type").toString(), json.get("targetTbCode").toString(), json.get("transType").toString());

            try {
                LOGGER.info("接口表[" + json.get("srcTbCode") + "]抽取开始start...");
                Long beginTime = System.currentTimeMillis();

                String maxTimeSQL = "SELECT case when max(last_update_time) is null then '1970-01-01' else date_sub(max(last_update_time), interval 60 second) end as max_t from " + json.get("targetTbCode").toString();

                String excuteSQL = "select * from " + json.get("srcTbCode").toString() + " where last_update_time > ? ";
                if (StringUtils.isNotEmpty(json.get("whereStr").toString())) {
                    excuteSQL = excuteSQL + "and " + json.get("whereStr").toString();
                }

                simpleTrans.incrementTrans(json.get("id").toString(), json.get("transName").toString(), json.get("srcDbCode").toString(),
                        json.get("targetDbCode").toString(), json.get("srcTbCode").toString(), json.get("targetTbCode").toString(),
                        json.get("srcDescript").toString(), json.get("targetDescript").toString(),maxTimeSQL,excuteSQL,keyLookup,keyCondition, TransConstants.FIELD_DATA_BASE);

                // 插入结束日志
                transStatusLogService.updateEndTrance(insertLogID, (int) (System.currentTimeMillis() - beginTime));
            } catch(KettleException e) {
                transStatusLogService.updateExceptionTrance(insertLogID, e.toString());
                e.printStackTrace();
            } catch(Exception e) {
                transStatusLogService.updateExceptionTrance(insertLogID, e.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * 单独执行抽取
     *
     * @param etlIfTransInfo
     */
    public void executeTrans (TransInfo etlIfTransInfo) throws Exception{
        LOGGER.info("全量抽取[" + etlIfTransInfo.getSrcTbCode() + "]表开始start...");
        simpleTrans.truncateAfterFullTrans(etlIfTransInfo.getId(), etlIfTransInfo.getTransName(), etlIfTransInfo.getSrcDbCode(),
                etlIfTransInfo.getTargetDbCode(), etlIfTransInfo.getSrcTbCode(), etlIfTransInfo.getTargetTbCode(),
                etlIfTransInfo.getSrcDescript(), etlIfTransInfo.getTargetDescript(), etlIfTransInfo.getWhereStr(),null);
    }

}
