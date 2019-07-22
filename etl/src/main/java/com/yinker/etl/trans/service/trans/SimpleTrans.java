package com.yinker.etl.trans.service.trans;

import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.util.CommonUtils;
import com.yinker.etl.trans.util.KettleDataSorceInit;
import com.yinker.etl.trans.util.KettleUtil;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepErrorMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 简易转换 - 适用于大多数转换 需要根据不同的需求创建不同的转换供调用
 *
 * @author Lenovo
 */
public class SimpleTrans {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTrans.class);

    /**
     * 加载数据源信息
     */
    private static List<DatabaseMeta> databaseMetaList = KettleDataSorceInit.initDataSource();

    /**
     * 用来查询的关键字：表字段
     */
    private static final String[] KEY_LOOKUP = {"id"};

    /**
     * 用来查询的关键字：流里的字段1
     */
    private static final String[] KEY_STREAM = {"id"};

    /**
     * 用来查询的关键字：比较符
     */
    private static String[] keyCondition = {"="};

    /**
     * 所有表中的转换时间字段名称
     */
    private static final String TRANS_TIME = "trans_time";

    private static PmDataSourceService pmDataSourceService;


    /**
     * 接口转换 - 增量抽取 (老方法)
     *
     * @param transName          转换名称
     * @param srcDataBaseMeta    源数据源名称
     * @param targetDataBaseMeta 目标数据源名称
     * @param srcTableName       源数据表名称
     * @param targetTableName    目标数据表名称
     * @param srcDescript        源表描述
     * @param targetDescript     目标表描述
     * @throws KettleException
     */
    public void interFaceTrans (String id, String transName, String srcDataBaseMeta, String targetDataBaseMeta, String srcTableName, String targetTableName, String srcDescript, String targetDescript, String whereStr, String batchNo) throws Exception {
        TransMeta transMeta = new TransMeta();
        for(int i = 0; i < databaseMetaList.size(); i++) {
            transMeta.addDatabase(databaseMetaList.get(i));
        }
        transMeta.setName(id);

        // 获取源数据源与目标数据源
        DatabaseMeta database_src = transMeta.findDatabase(srcDataBaseMeta);
        DatabaseMeta database_target = transMeta.findDatabase(targetDataBaseMeta);

        // 步骤1： 创建步骤1 表输入控件
        String sql1 = "SELECT case when max(last_update_time) is null then '1970-01-01' else date_sub(max(last_update_time), interval 60 second) end as max_t from " + targetTableName;
        StepMeta step1 = KettleUtil.initTableInput("表输入:获取更新的时间", database_target, sql1);
        transMeta.getInfoStep(step1);

        // 步骤2： 创建步骤2 表输入控件
        String sql2 = "select * from " + srcTableName + " where last_update_time > ? ";
        if (StringUtils.isNotEmpty(whereStr)) {
            sql2 = sql2 + "and " + whereStr;
        }
        StepMeta step2 = KettleUtil.initTableInput("表输入:"+srcDescript, database_src, sql2, true, false, step1);

        // 获取步骤2的查询字段，用于下一步骤的字段映射
        RowMetaInterface rowMetaInterface = transMeta.getStepFields(step2);
        String[] names = rowMetaInterface.getFieldNames();

        // 创建步骤3 插入/更新组件 每个参数不能为空
        StepMeta step3 = KettleUtil.InitInsertUpdateMeta("插入/更新数据:"+targetDescript, database_target, targetTableName, KEY_LOOKUP, KEY_STREAM, keyCondition, names, names);
        //StepMeta step3 = KettleUtil.initTableOutput("表输出数据", database_target, targetTableName, true, names, names);

        // 获取步骤3的查询字段，用于下一步骤的字段映射。
        StepMeta step4 = getConstantInit(srcTableName, transName, id);

        StepErrorMeta errorMeta = KettleUtil.initStepErrorMeta(transMeta, step3, step4);
        errorMeta.setEnabled(true);
        step3.setStepErrorMeta(errorMeta);

        DatabaseMeta database_err = transMeta.findDatabase(TransConstants.LOG_DATA_SOURCE_NAME);
        StepMeta step5 = KettleUtil.initTableOutput("保存错误记录", database_err, TransConstants.LOG_ERROR_TABLE_NAME, true, TransConstants.FIELD_DATA_BASE, TransConstants.FIELD_DATA_BASE);

        // 把每一步添加到转换中
        transMeta.addStep(step1);
        transMeta.addStep(step2);
        transMeta.addStep(step3);
        transMeta.addStep(step4);
        transMeta.addStep(step5);

        // 为每一步骤添加连线
        transMeta.addTransHop(new TransHopMeta(step1, step2));
        transMeta.addTransHop(new TransHopMeta(step2, step3));
        transMeta.addTransHop(new TransHopMeta(step3, step4));
        transMeta.addTransHop(new TransHopMeta(step4, step5));

        // 保存转换日志（List<StepMeta>为步骤集合）
        List<StepMeta> steps = new ArrayList<>();
        steps.add(step3);
        //KettleUtil.saveTransLog(transMeta, steps);
        // 记录步骤日志
        KettleUtil.saveStepLog(transMeta);
        //  记录通道日志
        //KettleUtil.saveChannelLog(transMeta);

        // 执行转换
        Trans trans = new Trans(transMeta);
        trans.execute(null);
        trans.waitUntilFinished();
    }

    /**
     * 接口转换 - 增量抽取
     * @param id                 转换编号
     * @param transName          转换名称
     * @param srcDataBaseMeta    源数据源名称
     * @param targetDataBaseMeta 目标数据源名称
     * @param srcTableName       源数据表名称
     * @param targetTableName    目标数据表名称
     * @param srcDescript        源表描述
     * @param targetDescript     目标表描述
     * @param maxTimeSQL         获取更新节点的SQL
     * @param excuteSQL          执行查询SQL
     * @param Key                比较字段
     * @param keyCondition       比较符号
     * @param errorCloumes       记录错误字段
     * @throws KettleException
     */
    public void incrementTrans (String id, String transName, String srcDataBaseMeta, String targetDataBaseMeta, String srcTableName, String targetTableName, String srcDescript, String targetDescript, String maxTimeSQL, String excuteSQL,String[] Key,String[] keyCondition,String[] errorCloumes) throws Exception {
        TransMeta transMeta = new TransMeta();
        for(int i = 0; i < databaseMetaList.size(); i++) {
            transMeta.addDatabase(databaseMetaList.get(i));
        }
        transMeta.setName(id);

        // 获取源数据源与目标数据源
        DatabaseMeta database_src = transMeta.findDatabase(srcDataBaseMeta);
        DatabaseMeta database_target = transMeta.findDatabase(targetDataBaseMeta);

        // 步骤1： 创建步骤1 表输入控件
        StepMeta step1 = KettleUtil.initTableInput("表输入:获取更新的时间", database_target, maxTimeSQL);
        transMeta.getInfoStep(step1);

        // 步骤2： 创建步骤2 表输入控件
        StepMeta step2 = KettleUtil.initTableInput("表输入:"+srcDescript, database_src, excuteSQL, true, false, step1);

        // 获取步骤2的查询字段，用于下一步骤的字段映射
        RowMetaInterface rowMetaInterface = transMeta.getStepFields(step2);
        String[] names = rowMetaInterface.getFieldNames();

        // 创建步骤3 插入/更新组件 每个参数不能为空
        StepMeta step3 = KettleUtil.InitInsertUpdateMeta("插入/更新数据:"+targetDescript, database_target, targetTableName, Key, Key, keyCondition, names, names);
        //StepMeta step3 = KettleUtil.initTableOutput("表输出数据", database_target, targetTableName, true, names, names);

        // 获取步骤3的查询字段，用于下一步骤的字段映射。
        StepMeta step4 = getConstantInit(srcTableName, transName, id);


        StepErrorMeta errorMeta = KettleUtil.initStepErrorMeta(transMeta, step3, step4);
        errorMeta.setEnabled(true);
        step3.setStepErrorMeta(errorMeta);

        DatabaseMeta database_err = transMeta.findDatabase(TransConstants.LOG_DATA_SOURCE_NAME);
        StepMeta step5 = KettleUtil.initTableOutput("保存错误记录", database_err, TransConstants.LOG_ERROR_TABLE_NAME, true, errorCloumes, errorCloumes);


        // 把每一步添加到转换中
        transMeta.addStep(step1);
        transMeta.addStep(step2);
        transMeta.addStep(step3);
        transMeta.addStep(step4);
        transMeta.addStep(step5);

        // 为每一步骤添加连线
        transMeta.addTransHop(new TransHopMeta(step1, step2));
        transMeta.addTransHop(new TransHopMeta(step2, step3));
        transMeta.addTransHop(new TransHopMeta(step3, step4));
        transMeta.addTransHop(new TransHopMeta(step4, step5));

        // 保存转换日志（List<StepMeta>为步骤集合）
        //List<StepMeta> steps = new ArrayList<>();
        //steps.add(step3);
        //KettleUtil.saveTransLog(transMeta, steps);
        // 记录步骤日志
        KettleUtil.saveStepLog(transMeta);
        //  记录通道日志
        //KettleUtil.saveChannelLog(transMeta);

        // 执行转换
        Trans trans = new Trans(transMeta);
        trans.execute(null);
        trans.waitUntilFinished();
    }

    /**
     * 数据平台转换 - 全量表备份转换(时间拉链)
     *
     * @param transName          转换名称
     * @param srcDataBaseMeta    源数据源名称
     * @param targetDataBaseMeta 目标数据源名称
     * @param srcTableName       源数据表名称
     * @param targetTableName    目标数据表名称
     * @param srcDescript        源表描述
     * @param targetDescript     目标表描述
     */
    public void allTableTrans (String id, String transName, String srcDataBaseMeta, String targetDataBaseMeta, String srcTableName, String targetTableName, String srcDescript, String targetDescript, String whereStr) throws Exception{
        try {
            TransMeta transMeta = new TransMeta();
            // 给转换添加的数据库连接
            for(int i = 0; i < databaseMetaList.size(); i++) {
                transMeta.addDatabase(databaseMetaList.get(i));
            }
            transMeta.setName(id);

            // 获取源数据源与目标数据源
            DatabaseMeta database_src = transMeta.findDatabase(srcDataBaseMeta);
            DatabaseMeta database_target = transMeta.findDatabase(targetDataBaseMeta);

            // 步骤1： 创建步骤1 表输入控件
            String sql = "select * from " + srcTableName + " where 1 = 1 ";
            if (StringUtils.isNotEmpty(whereStr)) {
                sql = sql + "and " + whereStr;
            }
            StepMeta step1 = KettleUtil.initTableInput(targetDescript, database_src, sql);

            // 创建插入/更新组件 每个参数不能为空
            StepMeta step2 = KettleUtil.initTableOutput("全量备份", database_target, targetTableName, false, null, null);

            String channelId = transMeta.getLogChannelId();
            // 创建步骤3-增加常量
            StepMeta step3 = getConstantInit(srcTableName, transName, id);

            StepErrorMeta errorMeta = KettleUtil.initStepErrorMeta(transMeta, step2, step3);
            step2.setStepErrorMeta(errorMeta);

            DatabaseMeta database_err = transMeta.findDatabase(TransConstants.LOG_DATA_SOURCE_NAME);
            StepMeta step4 = KettleUtil.initTableOutput("保存错误记录", database_err, TransConstants.LOG_ERROR_TABLE_NAME, true, TransConstants.FIELD_DATA_BASE, TransConstants.FIELD_DATA_BASE);

            // 把每一步添加到转换中
            transMeta.addStep(step1);
            transMeta.addStep(step2);
            transMeta.addStep(step3);
            transMeta.addStep(step4);

            // 为每一步骤添加连线
            transMeta.addTransHop(new TransHopMeta(step1, step2));
            transMeta.addTransHop(new TransHopMeta(step2, step3));
            transMeta.addTransHop(new TransHopMeta(step3, step4));

            // 记录步骤日志
            KettleUtil.saveStepLog(transMeta);
            //  记录通道日志
            //KettleUtil.saveChannelLog(transMeta);
            // 保存转换日志（List<StepMeta>为步骤集合）
            //List<StepMeta> steps = new ArrayList<StepMeta>();
            //steps.add(step2);
            //KettleUtil.saveTransLog(transMeta, steps);

            // 执行转换
            Trans trans = new Trans(transMeta);
            trans.execute(null);
            trans.waitUntilFinished();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全量更新抽取。
     *
     * @param transName          转换名称
     * @param srcDataBaseMeta    源数据源名称
     * @param targetDataBaseMeta 目标数据源名称
     * @param srcTableName       源数据表名称
     * @param targetTableName    目标数据表名称
     * @param srcDescript        源表描述
     * @param targetDescript     目标表描述
     * @param exeSQL             源表执行的SQL
     * @param matchKey           需要平匹配的字段类型
     */
    public void fullUpdateTrans (String id, String transName, String srcDataBaseMeta, String targetDataBaseMeta, String srcTableName, String targetTableName, String srcDescript, String targetDescript, String whereStr, String exeSQL, String[] matchKey) throws Exception{
        Long beginTime = System.currentTimeMillis();
        try {
            TransMeta transMeta = new TransMeta();
            for(int i = 0; i < databaseMetaList.size(); i++) {
                transMeta.addDatabase(databaseMetaList.get(i));
            }
            transMeta.setName(id);

            // 获取源数据源与目标数据源
            DatabaseMeta database_src = transMeta.findDatabase(srcDataBaseMeta);
            DatabaseMeta database_target = transMeta.findDatabase(targetDataBaseMeta);

            // 步骤1： 创建步骤1 表输入控件
            if (StringUtils.isEmpty(exeSQL)) {
                exeSQL = "select * from " + srcTableName + " where 1 = 1 ";
                if (StringUtils.isNotEmpty(whereStr)) {
                    exeSQL = exeSQL + "and " + whereStr;
                }
            }
            StepMeta step1 = KettleUtil.initTableInput(srcDescript, database_src, exeSQL, false, false, null);

            // 获取步骤1的查询字段，用于下一步骤的字段映射
            RowMetaInterface rowMetaInterface = transMeta.getStepFields(step1);
            String[] names = rowMetaInterface.getFieldNames();

            // 创建步骤2 插入/更新组件 每个参数不能为空
            String[] key;
            String[] fieldStream;
            if (StringUtils.isNotEmpty(srcTableName) && srcTableName.toLowerCase().contains("act_")) {
                key = new String[]{"id_"};
                fieldStream = TransConstants.FIELD_DATA_BASE_ACT;
            } else {
                key = new String[]{"id"};
                fieldStream = TransConstants.FIELD_DATA_BASE;
            }
            if(id.indexOf("STS")>=0){
                fieldStream = TransConstants.FIELD_DATA_BASE_NOID;
            }

            // 如果对比条件较多的时候，创建多条对比条件。
            if (matchKey != null && matchKey.length > 0) {
                key = matchKey;
                keyCondition = new String[matchKey.length];
                for(int i = 0; i < matchKey.length; i++) {
                    keyCondition[i] = "=";
                }
            }
            StepMeta step2 = KettleUtil.InitInsertUpdateMeta("插入/更新数据", database_target, targetTableName, key, key, keyCondition, names, names);

            String channelId = transMeta.getLogChannelId();
            // 创建步骤3-增加常量
            StepMeta step3 = getConstantInit(srcTableName, transName, id);

            StepErrorMeta errorMeta = KettleUtil.initStepErrorMeta(transMeta, step2, step3);
            errorMeta.setEnabled(true);
            step2.setStepErrorMeta(errorMeta);

            DatabaseMeta database_err = transMeta.findDatabase(TransConstants.LOG_DATA_SOURCE_NAME);
            StepMeta step4 = KettleUtil.initTableOutput("保存错误记录", database_err, TransConstants.LOG_ERROR_TABLE_NAME, true, fieldStream, fieldStream);

            // 记录步骤日志
            KettleUtil.saveStepLog(transMeta);

            // 保存转换日志（List<StepMeta>为步骤集合）
            //List<StepMeta> steps = new ArrayList<StepMeta>();
            //steps.add(step2);
            //KettleUtil.saveTransLog(transMeta, steps);

            //  记录通道日志
            //KettleUtil.saveChannelLog(transMeta);

            // 把每一步添加到转换中
            transMeta.addStep(step1);
            transMeta.addStep(step2);
            transMeta.addStep(step3);
                transMeta.addStep(step4);

            // 为每一步骤添加连线
            transMeta.addTransHop(new TransHopMeta(step1, step2));
            transMeta.addTransHop(new TransHopMeta(step2, step3));
                transMeta.addTransHop(new TransHopMeta(step3, step4));

            // 执行转换
            Trans trans = new Trans(transMeta);
            trans.execute(null);
            trans.waitUntilFinished();
            LOGGER.info("转换：{}，执行完毕，共耗时{}ms",transName,(System.currentTimeMillis() - beginTime));
        } catch(KettleStepException e) {
            e.printStackTrace();
        } catch(KettleException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清表后全量抽取-适用于临时抽取使用
     *
     * @param transName          转换名称
     * @param srcDataBaseMeta    源数据源名称
     * @param targetDataBaseMeta 目标数据源名称
     * @param srcTableName       源数据表名称
     * @param targetTableName    目标数据表名称
     * @param srcDescript        源表描述
     * @param targetDescript     目标表描述
     */
    public void truncateAfterFullTrans (String id, String transName, String srcDataBaseMeta, String targetDataBaseMeta, String srcTableName, String targetTableName, String srcDescript, String targetDescript, String whereStr, String exeSQL) throws Exception{
        Long beginTime = System.currentTimeMillis();
        try {
            TransMeta transMeta = new TransMeta();
            for(int i = 0; i < databaseMetaList.size(); i++) {
                transMeta.addDatabase(databaseMetaList.get(i));
            }
            transMeta.setName(id);
            // 获取源数据源与目标数据源
            DatabaseMeta database_src = transMeta.findDatabase(srcDataBaseMeta);
            DatabaseMeta database_target = transMeta.findDatabase(targetDataBaseMeta);

            // 清空目标表数据
            StepMeta step1 = KettleUtil.initExcuteSql("清空目标表" + targetTableName, database_target, "TRUNCATE " + targetTableName);

            // 步骤1： 创建步骤1 表输入控件
            if (StringUtils.isEmpty(exeSQL)) {
                exeSQL = "select * from " + srcTableName + " where 1 = 1 ";
                if (StringUtils.isNotEmpty(whereStr)) {
                    exeSQL = exeSQL + "and " + whereStr;
                }
            }
            StepMeta step2 = KettleUtil.initTableInput(srcDescript, database_src, exeSQL, false, false, null);

            // 获取步骤1的查询字段，用于下一步骤的字段映射
            RowMetaInterface rowMetaInterface = transMeta.getStepFields(step2);
            String[] names = rowMetaInterface.getFieldNames();

            // 创建步骤2 表输出控件
            StepMeta step3 = KettleUtil.initTableOutput(targetDescript, database_target, targetTableName, true, names, names);

            // 获取步骤3的查询字段，用于下一步骤的字段映射。
            StepMeta step4 = getConstantInit(targetTableName, transName, id);


            StepErrorMeta errorMeta = KettleUtil.initStepErrorMeta(transMeta, step3, step4);
            errorMeta.setEnabled(true);
            step3.setStepErrorMeta(errorMeta);

            DatabaseMeta database_err = transMeta.findDatabase(TransConstants.LOG_DATA_SOURCE_NAME);
            StepMeta step5 = KettleUtil.initTableOutput("保存错误记录", database_err, TransConstants.LOG_ERROR_TABLE_NAME, true, TransConstants.FIELD_DATA_BASE_NOID, TransConstants.FIELD_DATA_BASE_NOID);



            // 记录步骤日志
            KettleUtil.saveStepLog(transMeta);

            // 保存转换日志（List<StepMeta>为步骤集合）
           // List<StepMeta> steps = new ArrayList<StepMeta>();
           // steps.add(step3);
            //KettleUtil.saveTransLog(transMeta, steps);

            //  记录通道日志
            //KettleUtil.saveChannelLog(transMeta);
            // 把每一步添加到转换中
            transMeta.addStep(step1);
            transMeta.addStep(step2);
            transMeta.addStep(step3);
            transMeta.addStep(step4);
            transMeta.addStep(step5);

            // 为每一步骤添加连线
            transMeta.addTransHop(new TransHopMeta(step1, step2));
            transMeta.addTransHop(new TransHopMeta(step2, step3));
            transMeta.addTransHop(new TransHopMeta(step3, step4));
            transMeta.addTransHop(new TransHopMeta(step4, step5));

            // 执行转换
            Trans trans = new Trans(transMeta);
            trans.execute(null);
            trans.waitUntilFinished();
            LOGGER.info("转换：{}，执行完毕，共耗时{}ms",transName,(System.currentTimeMillis() - beginTime));
        } catch(KettleStepException e) {
            e.printStackTrace();
        } catch(KettleException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据平台转换 - 全量备份更新
     *
     * @param transName          转换名称
     * @param srcDataBaseMeta    源数据源名称
     * @param targetDataBaseMeta 目标数据源名称
     * @param srcTableName       源数据表名称
     * @param targetTableName    目标数据表名称
     * @param srcDescript        源表描述
     * @param targetDescript     目标表描述
     * @throws KettleException
     */
    public void dataPlatFormTrans (String id, String transName, String srcDataBaseMeta, String targetDataBaseMeta, String srcTableName, String targetTableName, String srcDescript, String targetDescript, String whereStr, String batchNo) throws Exception {

        TransMeta transMeta = new TransMeta();
        // 给转换添加的数据库连接
        for(int i = 0; i < databaseMetaList.size(); i++) {
            transMeta.addDatabase(databaseMetaList.get(i));
        }

        transMeta.setName(id);

        // 获取源数据源与目标数据源
        DatabaseMeta database_src = transMeta.findDatabase(srcDataBaseMeta);
        DatabaseMeta database_target = transMeta.findDatabase(targetDataBaseMeta);

        // 步骤1： 创建步骤1 表输入控件
        String sql1 = "select * from " + srcTableName + " where 1 = 1 ";
        if (StringUtils.isNotEmpty(whereStr)) {
            sql1 = sql1 + "and " + whereStr;
        }
        StepMeta step1 = KettleUtil.initTableInput(targetDescript, database_src, sql1, false, false, null);

        // 获取步骤1的查询字段，用于下一步骤的字段映射
        RowMetaInterface rowMetaInterface = transMeta.getStepFields(step1);
        String[] names = rowMetaInterface.getFieldNames();
        String[] names_ = removeTransTimeColumn(names);
        // 创建步骤2 插入/更新组件 每个参数不能为空
        StepMeta step2 = KettleUtil.InitInsertUpdateMeta("插入/更新数据", database_target, targetTableName, KEY_LOOKUP, KEY_STREAM, keyCondition, names_, names_);

        String channelId = transMeta.getLogChannelId();
        // 创建步骤3-增加常量
        StepMeta step3 = getConstantInit(srcTableName, transName, id);

        StepErrorMeta errorMeta = KettleUtil.initStepErrorMeta(transMeta, step2, step3);
        errorMeta.setEnabled(true);
        step2.setStepErrorMeta(errorMeta);

        DatabaseMeta database_err = transMeta.findDatabase(TransConstants.LOG_DATA_SOURCE_NAME);
        StepMeta step4 = KettleUtil.initTableOutput("保存错误记录", database_err, TransConstants.LOG_ERROR_TABLE_NAME, true, TransConstants.FIELD_DATA_BASE, TransConstants.FIELD_DATA_BASE);

        // 把每一步添加到转换中
        transMeta.addStep(step1);
        transMeta.addStep(step2);
        transMeta.addStep(step3);
        transMeta.addStep(step4);

        // 为每一步骤添加连线
        transMeta.addTransHop(new TransHopMeta(step1, step2));
        transMeta.addTransHop(new TransHopMeta(step2, step3));
        transMeta.addTransHop(new TransHopMeta(step3, step4));

        // 保存转换日志（List<StepMeta>为步骤集合）
        //List<StepMeta> steps = new ArrayList<StepMeta>();
       // steps.add(step2);
       // KettleUtil.saveTransLog(transMeta, steps);
        // 记录步骤日志
        KettleUtil.saveStepLog(transMeta);
        //  记录通道日志
       // KettleUtil.saveChannelLog(transMeta);
        // 执行转换
        Trans trans = new Trans(transMeta);
        trans.execute(null);
        trans.waitUntilFinished();
    }

    /**
     * 初始化常量
     *
     * @param srcTableName
     * @param transName
     * @return
     */
    private static StepMeta getConstantInit (String srcTableName, String transName, String channelId) {
        String[] fieldName = Arrays.copyOfRange(TransConstants.FIELD_DATA_BASE, 5, TransConstants.FIELD_DATA_BASE.length);
        String[] value = {srcTableName, transName, CommonUtils.getNowDateString(), channelId};
        StepMeta step = KettleUtil.initConstant("初始化常量", fieldName, value);

        return step;
    }

    /**
     * 清空表数据
     *
     * @param tableName    表名称
     * @param dataBaseMeta 数据源
     */
    public void truncateTableData (String tableName, String dataBaseMeta) {
        try {
            TransMeta transMeta = new TransMeta();
            // 给转换添加的数据库连接
            for(int i = 0; i < databaseMetaList.size(); i++) {
                transMeta.addDatabase(databaseMetaList.get(i));
            }

            transMeta.setName("清空表数据 - " + tableName);

            // 获取源数据源与目标数据源
            DatabaseMeta database = transMeta.findDatabase(dataBaseMeta);

            StepMeta step = KettleUtil.initExcuteSql("清空接口层表" + tableName, database, "TRUNCATE " + tableName);

            String sql1 = "select count(*) from " + tableName;
            StepMeta step1 = KettleUtil.initTableInput("记录清空数据数据", database, sql1, false, false, null);

            transMeta.addStep(step);
            transMeta.addStep(step1);
            // 为每一步骤添加连线
            transMeta.addTransHop(new TransHopMeta(step, step1));

            Trans trans = new Trans(transMeta);
            trans.execute(null);
            trans.waitUntilFinished();
        } catch(KettleStepException e) {
            e.printStackTrace();
        } catch(KettleException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 剔除trans_time 字段，使trans_time 可以自动插入当前时间。
     *
     * @param names
     * @return 剔除后的names[]
     */
    private String[] removeTransTimeColumn (String[] names) {
        List<String> li = new ArrayList<String>();
        for(int i = 0; i < names.length; i++) {
            if (!"trans_time".equals(names[i])) {
                li.add(names[i]);
            }

        }

        String[] names_ = new String[li.size()];
        for(int i = 0; i < li.size(); i++) {
            names_[i] = li.get(i);
        }
        return names_;
    }

    /*private List<DatabaseMeta> getDatabaseMetaList() throws Exception {
        if(databaseMetaList == null || databaseMetaList.size()==0)
            databaseMetaList = DatabaseMetaUtil.getInstance().getDatabaseMetaListInstance(pmDataSourceService);
        return databaseMetaList;
    }*/

    public static PmDataSourceService getPmDataSourceService () {
        return pmDataSourceService;
    }

    public static void setPmDataSourceService (PmDataSourceService pmDataSourceService) {
        SimpleTrans.pmDataSourceService = pmDataSourceService;
    }

    public static void setDatabaseMetaList (List<DatabaseMeta> databaseMetaList) {
        SimpleTrans.databaseMetaList = databaseMetaList;
    }
}
