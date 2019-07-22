package com.yinker.etl.mongodb.exec;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.mongodb.dao.impl.MysqlLogDaoImpl;
import com.yinker.etl.pm.model.PmMongoTransferTableNameInfoQuery;
import com.yinker.etl.pm.model.base.PmMongoTransferBatchLog;
import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfo;
import com.yinker.etl.pm.service.PmMongoTransferBatchLogService;
import com.yinker.etl.pm.service.PmMongoTransferTableNameInfoService;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.trans.TransConstants;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.zwork.common.key.KeyGeneratorContainer;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 简单的实现了Spring QuartzJobBean接口
 * </p>
 * <p>
 * mysql数据定时更新到mongodb数据库
 * 将mysql数据定时发送到rabbitMQ待处理
 *
 * @author Lenovo
 */

public class MysqlToMongoExecService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlToMongoExecService.class);

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Resource(name = "mysqlLogDaoImpl")
    private MysqlLogDaoImpl mysqlLogDaoImpl;

    /* 定义业务实体对象，用于新增/修改/查看 */
    @Resource(name = "pmMongoTransferBatchLog")
    protected PmMongoTransferBatchLog pmMongoTransferBatchLogEntity;

    /* 定义服务层操作类 */
    @Resource(name = "pmMongoTransferBatchLogService")
    protected PmMongoTransferBatchLogService pmMongoTransferBatchLogService;
    @Resource(name = "pmMongoTransferTableNameInfoService")
    protected PmMongoTransferTableNameInfoService pmMongoTransferTableNameInfoService;

    /* 序列键容器 */
    @Resource(name = "etlKeyGeneratorContainerPm")
    private KeyGeneratorContainer keyGeneratorContainer;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;


    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        LOGGER.info("定时抽取mysql数据到mongodb数据库..");
        JobKey jobKey = context.getJobDetail().getKey();
        if (StringUtils.isNotEmpty(jobKey.getGroup()) && TransConstants.MYSQL_TO_MONGODB_LOG.equals(jobKey.getGroup())) {
            try {
                long startSysTime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdfid = new SimpleDateFormat("yyyy-MM-dd");

                pmMongoTransferBatchLogEntity.setId(keyGeneratorContainer.getNextKey("pmMongoTransferBatchLogId"));
                pmMongoTransferBatchLogEntity.setBatchId(sdfid.format(startSysTime) + "-" + UUID.randomUUID().toString());
                pmMongoTransferBatchLogEntity.setStartTime(sdf.parse(sdf.format(startSysTime)));
                pmMongoTransferBatchLogEntity.setCreateTime(sdf.parse(sdf.format(startSysTime)));
                pmMongoTransferBatchLogEntity.setLastUpdateTime(sdf.parse(sdf.format(startSysTime)));
                pmMongoTransferBatchLogService.insert(pmMongoTransferBatchLogEntity);

                PmMongoTransferTableNameInfoQuery query = new PmMongoTransferTableNameInfoQuery();
                query.setState("Y");
                List<PmMongoTransferTableNameInfo> Tablelist = pmMongoTransferTableNameInfoService.selectByEntity(query);

                for (PmMongoTransferTableNameInfo entity : Tablelist) {
                    execute(entity);
                }
                pmMongoTransferBatchLogEntity.setRemark("");
                pmMongoTransferBatchLogEntity.setBoolException("");
                long endSysTime = System.currentTimeMillis();
                pmMongoTransferBatchLogEntity.setEndTime(sdf.parse(sdf.format(endSysTime)));
                pmMongoTransferBatchLogEntity.setLastUpdateTime(sdf.parse(sdf.format(endSysTime)));
                pmMongoTransferBatchLogService.updateByPK(pmMongoTransferBatchLogEntity);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 业务处理类 - mysql To mongodb
     *
     * @throws ParseException
     */
    private void execute( PmMongoTransferTableNameInfo entity) throws ParseException {
            try {
                if("Y".equals(entity.getIsDiyRetentionTime())){
                    //自定义mysql数据留存时间的mongo数据迁移方法
                    executeWithDiyRetentionTime(entity);
                }else {
                    String tableName = entity.getTableName(); // 获取跑批表名

                    //获取步长，默认定为一次发送20M数据
                    int step = pmMongoTransferTableNameInfoService.selectTransSteps(tableName);
                    if (step == 0) {
                        step = 10000;
                    }

                    LOGGER.info("执行{}表跑批中。。", tableName);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("tableName", tableName);
                    int num = mysqlLogDaoImpl.selectNum(map); // 查询表的数据量
                    int index = 0;

                    String classPath = entity.getClassPath();
                    map.put("classPath", classPath);

                    List<Map<String, Object>> list;
                    while (index <= num) {
                        if ("Y".equals(entity.getIsDelete())) {
                            list = mysqlLogDaoImpl.selectMysql(tableName, step); // 此步可能会遇到表字段很大卡死程序情况，故定批量时需注意
                            mysqlLogDaoImpl.delete(tableName, step); // 将mysql对应跑批表中相应数据清除
                        } else {
                            list = mysqlLogDaoImpl.selectMysqlLimit(tableName, index, step);
                        }
                        index += step;
                        list.add(map);

                        String jsonS = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss");
                        etlSendService.send(rabbitmqName + "_mysqltomongoKey", jsonS);
                    }
                }
            } catch (Exception e) {
                exceptionAndErrHandle("异常", e.toString());
                e.printStackTrace();
            } catch (Error e) {
                exceptionAndErrHandle("错误", e.toString());
                e.printStackTrace();
            }
    }

    private void exceptionAndErrHandle(String err, String errorMessage) throws ParseException {
        LOGGER.info("RabbitMQ向Mongo插入操作失败,产生{}", err);
        long errorCreateTime = System.currentTimeMillis(); //记录错误产生时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        errorMessage = errorMessage.replaceAll("'", "''");
        if (errorMessage.length() > 1024) {
            errorMessage = errorMessage.substring(0, 1023);
        }
        pmMongoTransferBatchLogEntity.setBoolException("异常");
        pmMongoTransferBatchLogEntity.setLastUpdateTime(sdf.parse(sdf.format(errorCreateTime)));
        pmMongoTransferBatchLogEntity.setRemark(errorMessage);
        pmMongoTransferBatchLogService.updateByPK(pmMongoTransferBatchLogEntity);//记录错误信息
    }

    private void executeWithDiyRetentionTime(PmMongoTransferTableNameInfo entity) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //当前时间前推留存时间天
        c.setTime(new Date());
        c.add(Calendar.DATE, -entity.getRetentionTime());
        Date d = c.getTime();
        String retentionDate = format.format(d);

        String tableName = entity.getTableName(); // 获取跑批表名

        //获取步长，默认定为一次发送20M数据
        int step = pmMongoTransferTableNameInfoService.selectTransSteps(tableName);
        if (step == 0) {
            step = 10000;
        }

        LOGGER.info("执行{}表跑批中。。", tableName);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableName", tableName);
        map.put("retentionColumn", entity.getRetentionColumnName());
        map.put("retentionDate", retentionDate);
        int num = mysqlLogDaoImpl.selectNumBeforeDay(map); // 查询表的数据量
        map.remove("retentionColumn");
        map.remove("retentionDate");
        int index = 0;

        String classPath = entity.getClassPath();
        map.put("classPath", classPath);

        List<Map<String, Object>> list;
        while (index + step <= num) {
            list = mysqlLogDaoImpl.selectMysqlByOrder(tableName, step, entity.getRetentionColumnName()); // 此步可能会遇到表字段很大JVM内存溢出情况，故定批量
            mysqlLogDaoImpl.deleteByOrder(tableName, step, entity.getRetentionColumnName()); // 将mysql对应跑批表中相应数据清除

            index += step;
            list.add(map);

            String jsonS = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss");
            etlSendService.send(rabbitmqName + "_mysqltomongoKey", jsonS);
        }
        if(index < num && index + step > num){
            list = mysqlLogDaoImpl.selectMysqlByOrder(tableName, num - index, entity.getRetentionColumnName()); // 此步可能会遇到表字段很大JVM内存溢出情况，故定批量
            mysqlLogDaoImpl.deleteByOrder(tableName, num - index, entity.getRetentionColumnName()); // 将mysql对应跑批表中相应数据清除

            list.add(map);

            String jsonS = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss");
            etlSendService.send(rabbitmqName + "_mysqltomongoKey", jsonS);
        }
    }

}