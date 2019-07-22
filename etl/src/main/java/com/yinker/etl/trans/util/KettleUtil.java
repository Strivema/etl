package com.yinker.etl.trans.util;

import com.yinker.etl.trans.TransConstants;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.logging.ChannelLogTable;
import org.pentaho.di.core.logging.StepLogTable;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepErrorMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.blockuntilstepsfinish.BlockUntilStepsFinishMeta;
import org.pentaho.di.trans.steps.constant.ConstantMeta;
import org.pentaho.di.trans.steps.dbproc.DBProcMeta;
import org.pentaho.di.trans.steps.delete.DeleteMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.sql.ExecSQLMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;
import org.pentaho.di.trans.steps.update.UpdateMeta;

import java.util.List;

public class KettleUtil {
	
	private static final String ETL_KETTLE_STEP_LOG = "pm_kettle_steps_log";
	private static final String ETL_KETTLE_TRANS_LOG = "pm_kettle_trans_log";
	private static final String ETL_KETTLE_CHANNEL_LOG = "pm_kettle_channel_log";

    /**
     * @param name ：表输入名称
     * @param sql ：要执行的sql
     * @return
     */
    public static StepMeta initTableInput(String name, DatabaseMeta dataBase, String sql) {
        return initTableInput(name, dataBase, sql, false, false, null);
    }

    /**
     * @param name ：表输入名称
     * @param sql ：要执行的sql
     * @param isReplaceVar ：是否替换变量
     * @return
     */
    public static StepMeta initTableInput(String name, DatabaseMeta dataBase, String sql, boolean isReplaceVar) {
        return initTableInput(name, dataBase, sql, isReplaceVar, false, null);
    }

    /**
     * /**
     * 
     * @param name ：表输入名称
     * @param sql ：要执行的sql
     * @param isReplaceVar ：是否替换变量
     * @param isNeedSimpleTrans ：是否延迟转换/允许简易转换
     * @param st ：以上步骤
     * @return
     */
    public static StepMeta initTableInput(String name, DatabaseMeta dataBase, String sql, boolean isReplaceVar, boolean isNeedSimpleTrans, StepMeta st) {
        TableInputMeta tableInput = new TableInputMeta();

        tableInput.setDatabaseMeta(dataBase);
        tableInput.setSQL(sql);
        tableInput.setVariableReplacementActive(isReplaceVar);
        tableInput.setLazyConversionActive(isNeedSimpleTrans);
        tableInput.setLookupFromStep(st);
        StepMeta metaStep = new StepMeta(name, tableInput);
        return metaStep;

    }

    /**
     * @param name ：步骤名称
     * @param dataBaseName ：数据库名称
     * @param sql ：执行的sql
     * @return
     */
    public static StepMeta initExcuteSql(String name, DatabaseMeta dataBaseName, String sql) {
        return initExcuteSql(name, dataBaseName, sql, false, false, false, false, null);
    }

    /**
     * @param name ：步骤名称
     * @param sql ：执行的sql
     * @param isExcuteEveryLine ：是否执行每一行
     * @param isAsSingleStm ：as a single statement
     * @param isReplaceVar ：是否替换变量
     * @param quoteString ：quote string
     * @param arguments ：作为参数的字段
     * @return
     */
    public static StepMeta initExcuteSql(String name, DatabaseMeta dataBase, String sql, boolean isExcuteEveryLine, boolean isAsSingleStm, boolean isReplaceVar, boolean quoteString, String[] arguments) {
        ExecSQLMeta exec = new ExecSQLMeta();

        exec.setDatabaseMeta(dataBase);
        exec.setSql(sql);
        exec.setSingleStatement(isAsSingleStm);
        exec.setVariableReplacementActive(isReplaceVar);
        exec.setExecutedEachInputRow(isExcuteEveryLine);
        if (!isExcuteEveryLine) {
            exec.setQuoteString(false);
        } else {// 勾选执行每一行，才能选择 quoteString和arguments
            exec.setQuoteString(quoteString);
            exec.setArguments(arguments);
        }

        StepMeta metaStep = new StepMeta(name, exec);
        return metaStep;

    }

    /**
     * 插入更新组件
     * 
     * @param name 组件名称
     * @param database 数据库链接
     * @param tableName 目标表
     * @param keyLookup 用来查询的关键字：表字段
     * @param keyStream 用来查询的关键字：流里的字段1
     * @param keyCondition 用来查询的关键字：比较符
     * @param updateLookup 更新字段:表字段
     * @param updateStream 更新字段：流字段
     * @return StepMeta
     */
    public static StepMeta InitInsertUpdateMeta(String name, DatabaseMeta database, String tableName, String[] keyLookup, String[] keyStream, String[] keyCondition, String[] updateLookup,String[] updateStream) {
        InsertUpdateMeta insertUpdateMeta = new InsertUpdateMeta();
        insertUpdateMeta.setDatabaseMeta(database);
        insertUpdateMeta.setTableName(tableName);
        insertUpdateMeta.setKeyLookup(keyLookup);
        insertUpdateMeta.setKeyStream(keyStream);
        insertUpdateMeta.setKeyStream2(new String[keyStream.length] );
        insertUpdateMeta.setKeyCondition(keyCondition);
        insertUpdateMeta.setUpdateLookup(updateLookup);
        insertUpdateMeta.setUpdateStream(updateStream);
        insertUpdateMeta.setUpdate(CommonUtils.repeatBooleanObj(updateStream.length, true));

        StepMeta metaStep = new StepMeta(name, insertUpdateMeta);

        return metaStep;
    }
    
    /**
     * 更新组件
     * 
     * @param name 组件名称
     * @param database 数据库链接
     * @param tableName 目标表
     * @param keyLookup 用来查询的关键字：表字段
     * @param keyStream 用来查询的关键字：流里的字段1
     * @param keyCondition 用来查询的关键字：比较符
     * @param updateLookup 更新字段:表字段
     * @param updateStream 更新字段：流字段
     * @return StepMeta
     */
    public static StepMeta InitUpdateMeta(String name, DatabaseMeta database, String tableName, String[] keyLookup, String[] keyStream, String[] keyCondition, String[] updateLookup,
                    String[] updateStream) {
    	UpdateMeta updateMeta = new UpdateMeta();
    	updateMeta.setDatabaseMeta(database);
        updateMeta.setTableName(tableName);
        updateMeta.setKeyLookup(keyLookup);
        updateMeta.setKeyStream(keyStream);
        updateMeta.setKeyStream2(new String[keyStream.length] );
        updateMeta.setKeyCondition(keyCondition);
        updateMeta.setUpdateLookup(updateLookup);
        updateMeta.setUpdateStream(updateStream);
        updateMeta.setErrorIgnored(true);
        StepMeta metaStep = new StepMeta(name, updateMeta);

        return metaStep;
    }

    /**
     * @param database 数据库
     * @param procedure 存储过程名字
     * @param resultType :返回值类型
     * @param argument ：名字
     * @param argumentDirection ：方向
     * @param argumentType ：类型
     * @return DBProcMeta
     */
    public static StepMeta initDBProc(DatabaseMeta database, String procedure, int resultType, String[] argument, String[] argumentDirection, int[] argumentType) {
        DBProcMeta dbProcMeta = new DBProcMeta();
        dbProcMeta.setDatabase(database);
        dbProcMeta.setProcedure(procedure);
        dbProcMeta.setAutoCommit(true);
        dbProcMeta.setResultName("result");
        dbProcMeta.setResultType(resultType);
        dbProcMeta.setArgument(argument);
        dbProcMeta.setArgumentDirection(argumentDirection);
        dbProcMeta.setArgumentType(argumentType);
        StepMeta st = new StepMeta(procedure, dbProcMeta);
        return st;
    }

    /**
     * 表输出控件
     * 
     * @param stepName 步骤名
     * @param database 数据库链接
     * @param tableName 目标表名
     * @param isSpecify 是否指定数据库字段
     * @param fieldDatabase 插入的字段：表字段
     * @param fieldStream 插入的字段：流字段
     * @return StepMeta
     */
    public static StepMeta initTableOutput(String stepName, DatabaseMeta database, String tableName, boolean isSpecify, String[] fieldDatabase, String[] fieldStream) {

        TableOutputMeta outputMeta = new TableOutputMeta();
        outputMeta.setDatabaseMeta(database);
        outputMeta.setTableName(tableName);
        outputMeta.setSpecifyFields(isSpecify);
        outputMeta.setFieldDatabase(fieldDatabase);
        outputMeta.setFieldStream(fieldStream);
        StepMeta metaStep = new StepMeta(stepName, outputMeta);
        return metaStep;
    }

    /**
     * 初始化将字段值设置为常量控件
     * 
     * @param stepName 步骤名
     * @param fieldName 字段列表
     * @param value 字段:字段替换值列表
     * @return StepMeta
     */
    public static StepMeta initConstant(String stepName, String[] fieldName, String[] value) {
        ConstantMeta constantMeta = new ConstantMeta();
        constantMeta.setFieldName(fieldName);
        constantMeta.setValue(value);

        // 初始化必要的字段映射属性
        constantMeta.setFieldType(CommonUtils.repeatString(fieldName.length, "String"));
        constantMeta.setFieldLength(CommonUtils.repeatInt(fieldName.length, 255));
        constantMeta.setFieldPrecision(CommonUtils.repeatInt(fieldName.length, -1));
        constantMeta.setEmptyString(CommonUtils.repeatBoolean(fieldName.length, false));
        StepMeta step = new StepMeta(stepName, constantMeta);
        return step;
    }

    public static void saveStepLog(TransMeta transMeta) {
        saveStepLog(transMeta, TransConstants.LOG_DATA_SOURCE_NAME);
    }

    public static void saveStepLog(TransMeta transMeta, String dataSourceName) {
        saveStepLog(transMeta, dataSourceName, ETL_KETTLE_STEP_LOG);
    }

    public static void saveChannelLog(TransMeta transMeta) {
        saveChannelLog(transMeta, TransConstants.LOG_DATA_SOURCE_NAME,ETL_KETTLE_CHANNEL_LOG, ETL_KETTLE_STEP_LOG);
    }

    public static void saveChannelLog(TransMeta transMeta, String dataSourceName) {
        saveChannelLog(transMeta, dataSourceName,ETL_KETTLE_CHANNEL_LOG, ETL_KETTLE_STEP_LOG);
    }

    /**
     * 添加kettle连接 并且记录转换日志
     */
    public static void saveStepLog(TransMeta transMeta, String dataSourceName, String logTableName) {
        VariableSpace space = new Variables();
        // 将step日志数据库配置名加入到变量集中
        space.setVariable(ETL_KETTLE_STEP_LOG, dataSourceName);
        space.initializeVariablesFrom(null);
        StepLogTable stepLogTable = StepLogTable.getDefault(space, transMeta);
        // StepLogTable使用的数据库连接名（上面配置的变量名）。
        stepLogTable.setConnectionName(dataSourceName);
        // 设置Step日志的表名
        stepLogTable.setTableName(logTableName);
        // 设置TransMeta的StepLogTable
        transMeta.setStepLogTable(stepLogTable);
    }

    public static void saveTransLog(TransMeta transMeta, List<StepMeta> steps) {
        saveTransLog(transMeta, steps, TransConstants.LOG_DATA_SOURCE_NAME);
    }

    public static void saveTransLog(TransMeta transMeta, List<StepMeta> steps, String dataSourceName) {
        saveTransLog(transMeta, steps, dataSourceName, ETL_KETTLE_TRANS_LOG);
    }

    /**
     * 记录转换日志
     * 
     * @param transMeta 转换元
     * @param dataSourceName 数据源名称
     * @param logTableName 日志表名称
     * @param steps 步骤List其中第一条数据为记录步骤日志
     */
    public static void saveTransLog(TransMeta transMeta, List<StepMeta> steps, String dataSourceName, String logTableName) {
    	
        VariableSpace space = new Variables();
        // 将Trans日志数据库配置名加入到变量集中
        space.setVariable(ETL_KETTLE_TRANS_LOG, dataSourceName);
        space.initializeVariablesFrom(null);
        TransLogTable transLogTable = TransLogTable.getDefault(space, transMeta, steps);
        transLogTable.setConnectionName(dataSourceName);
        // 设置Trans日志的表名
        transLogTable.setTableName(logTableName);
        transLogTable.setStepRead(steps.get(0));
        transLogTable.setStepWritten(steps.get(0));
        transLogTable.setStepUpdate(steps.get(0));
        transLogTable.setStepInput(steps.get(0));
        transLogTable.setStepOutput(steps.get(0));
        transLogTable.setStepRejected(steps.get(0));
        // 设置TransMeta的TransLogTable
        transMeta.setTransLogTable(transLogTable);
    }

    /**
     * 记录通道日志
     *
     * @param transMeta 转换元
     * @param dataSourceName 数据源名称
     */
    public static void saveChannelLog(TransMeta transMeta,  String dataSourceName,String channelTableName,String transTableName) {

        VariableSpace space = new Variables();
        // 将Channel日志数据库配置名加入到变量集中
        space.setVariable(channelTableName, dataSourceName);
        space.initializeVariablesFrom(null);

        ChannelLogTable channelLogTable = ChannelLogTable.getDefault( space, transMeta );
        channelLogTable.setConnectionName( dataSourceName );
        channelLogTable.setTableName( channelTableName );
        transMeta.setChannelLogTable( channelLogTable );
    }

    /**
     * 错误处理组件
     * @param transMeta 转换名称
     * @param sourceStep 源步骤
     * @param targetStep 目标步骤
     * @return
     */
    public static StepErrorMeta initStepErrorMeta(TransMeta transMeta, StepMeta sourceStep, StepMeta targetStep) {
        StepErrorMeta errorMeta = new StepErrorMeta(transMeta, sourceStep, targetStep);
        errorMeta.setEnabled(true);
        errorMeta.setNrErrorsValuename("err_count");
        errorMeta.setErrorCodesValuename("err_code");
        errorMeta.setErrorDescriptionsValuename("err_desc");
        errorMeta.setErrorFieldsValuename("err_field");
        return errorMeta;
    }
    
    /**
     * 删除数据组件
     * @param name 组件名称
     * @param db 数据源
     * @param targetTableName 删除表名
     * @param keysCondition
     * @param keyLookup
     * @param keyStream
     * @param parentStepMeta
     * @return
     */
    public static StepMeta initDeleteDataMeta(String name,DatabaseMeta db,String targetTableName,String[] keysCondition,String[] keyLookup,String[] keyStream,StepMeta parentStepMeta){
    	DeleteMeta dl = new DeleteMeta();
    	dl.setDatabaseMeta(db);
    	dl.setKeyCondition(keysCondition);
    	dl.setKeyLookup(keyLookup);
    	dl.setKeyStream(keyStream);
    	dl.setKeyStream2(new String[] { "" });
    	dl.setParentStepMeta(parentStepMeta);
    	dl.setTableName(targetTableName);
    	StepMeta metaStep = new StepMeta(name, dl);
    	
    	return metaStep;
    }
    
    /**
     * 阻塞数据直到步骤都完成
     * 
     * @param steps 步骤数组名称
     * @param CopyNrs 复制次数
     */
    public static StepMeta initBlockUntilSteps(String name,String[] steps, String[] CopyNrs){
    	BlockUntilStepsFinishMeta meta = new BlockUntilStepsFinishMeta();
        meta.allocate(steps.length);
        meta.setStepName(steps);
        meta.setStepCopyNr(CopyNrs);
        StepMeta st = new StepMeta(name,meta);
        return st;
    }
}
