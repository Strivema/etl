package com.yinker.etl.trans;

/**
 * 抽取模块常量类
 * 
 * @author Lenovo
 */
public class TransConstants {
	
	/**
	 * 资源库名
	 */
	public static final String KETTLE_REPOSITORY_META_NAME = "admin";
	
	/**
	 * 资源库链接
	 */
	public static final String KETTLE_REPOSITORY_META_PWD = "admin";

	/**
	 * kettle job启动状态
	 */
	public static final String JOB_EXECUTE_STATUS_START = "1";
	
	/**
	 * kettle job停止状态
	 */
	public static final String JOB_EXECUTE_STATUS_STOP = "2";
	
	/**
	 * 错误日志记录表
	 */
	public static final String LOG_DATA_SOURCE_NAME = "etl";
	
	/**
	 * 日志错误表名称
	 */
    public static final String LOG_ERROR_TABLE_NAME = "pm_error_record_log";
    
    /**
     * 错误日志表字段,用于保存错误日志
     */
    public static final String[] FIELD_DATA_BASE = { "id","err_count","err_desc","err_field","err_code", "err_table_name", "err_trans_name", "err_create_time","transId" };

    /**
     * 错误日志表字段,用于保存错误日志
     */
    public static final String[] FIELD_DATA_BASE_NOID = {"err_count","err_desc","err_field","err_code", "err_table_name", "err_trans_name", "err_create_time","transId" };


    /**
     * 错误日志表字段,用于保存错误日志
     */
    public static final String[] FIELD_DATA_BASE_ACT = { "id_","err_count","err_desc","err_field","err_code", "err_table_name", "err_trans_name", "err_create_time","transId" };

    /**
     * ETL增量字段类型 - Int 类型
     */
    public static final String[] COLUMN_TYPE_INT = {"INT","INTEGER","SMALLINT","TINYINT","BIGINT","MEDIUMINT"};
    /**
     * ETL增量字段类型 - Date 类型
     */
    public static final String[] COLUMN_TYPE_DATE = {"DATETIME","TIMESTAMP"};

    /**
     * 增量转换
     */
    public static final String TRANS_TYPE_1 = "1";
    
    /**
     * 全量更新抽取
     */
    public static final String TRANS_TYPE_2 = "2";
    
    /**
     * 清空全量抽取
     */
    public static final String TRANS_TYPE_3 = "3";
    
    /**
     * 时间拉链抽取
     */
    public static final String TRANS_TYPE_4 = "4";
    
    /**
     * 是否开启转换 - 开启
     */
    public static final String IS_ENABLE_TRANS_ON = "Y";
    /**
     * 是否开启转换 - 关闭
     */
    public static final String IS_ENABLE_TRANS_OFF = "N";

    /**
     * 是否开启表结构同步 - 开启
     */
    public static final String IS_SYNC_TABLE_ON = "Y";
    /**
     * 是否开启表结构同步 - 关闭
     */
    public static final String IS_SYNC_TABLE_OFF = "N";

    /**
     * job短线重连状态 0：正常执行
     */
    public static final String JOB_BREAK_STATUS_0 = "0";
    
    /**
     * job短线重连状态 1：启动状态
     */
    public static final String JOB_BREAK_STATUS_1 = "1";
    
    /**
     * job短线重连状态 2：终止状态
     */
    public static final String JOB_BREAK_STATUS_2 = "2";
    
    /**
     * 数据源类型：0 源数据库
     */
    public static final String DATA_SOURCE_SRC_TABLE = "0";
    
    /**
     * 数据源类型：0 目标数据库
     */
    public static final String DATA_SOURCE_DEST_TABLE = "1";
    
    /**
     * 数据抽取转换错误日志类型：1 步骤错误
     */
    public static final String RUN_STEP_ERROR="1";
    
    /**
     * 数据抽取转换错误日志类型：1 转换错误
     */
    public static final String RUN_TRANS_ERROR="2";
    
    /**
     * 数据抽取转换错误日志类型：1 其他错误
     */
    public static final String RUN_OTHER_ERROR="3";
    
    /**
     * 表抽取定时任务group
     */
    public static final String TABLE_TRANS_GROUP_NAME = "TABLE_TRANS";
    
    /**
     * 导入导出数据操作类型 -- 导出
     */
    public static final String OPERATE_TYPE_1 = "1";
    /**
     * 导入导出数据操作类型 -- 导入
     */
    public static final String OPERATE_TYPE_2 = "2";
    /**
     * 转换日志状态 -- 执行中
     */
    public static final String TRANSLOG_STATUS_1 = "1";
    /**
     * 转换日志状态 -- 执行完成
     */
    public static final String TRANSLOG_STATUS_2 = "2";
    /**
     * 转换日志状态 -- 执行异常
     */
    public static final String TRANSLOG_STATUS_3 = "3";
    /**
     * 转换日志状态 -- 此表正在抽取的任务，自动跳过
     */
    public static final String TRANSLOG_STATUS_4 = "4";
    /**
     * 转换日志状态 -- 手动终止
     */
    public static final String TRANSLOG_STATUS_5 = "5";
    /**
     * 日报发送定时任务group
     */
    public static final String TABLE_DAY_REPORT_GROUP_NAME = "DAY_REPORT";
    
    /**
     * 转换预警开关 -- 关
     */
    public static final String WARN_CONFIG_ENABLE_0 = "0";
    /**
     * 转换预警开关 -- 开
     */
    public static final String WARN_CONFIG_ENABLE_1 = "1";
    
    /**
     * 抽取转换预警表名
     */
    public static final String TARGET_TABLE_NAME = "etl_trans_report";
    
    /**
     * 抽取转换预警表名-表类型：源表
     */
    public static final String TRANS_REPORT_TRANS_STATUS_1 = "1";
    
    /**
     * 抽取转换预警表名-表抽取类型：目标表
     */
    public static final String TRANS_REPORT_TRANS_STATUS_2 = "2";
    
    /**
     * 日报发送-日报类型 :错误数据校验报表
     */
    public static final String REPORT_TYPE_ERROR_CHECK = "errorDataCheckReport";
    
    /**
     * 日报发送-日报类型：抽取预警报表
     */
    public static final String REPORT_TYPE_TRANS_CHECK = "transCheckReport";
    
    /**
     * 日报发送-附件路径
     */
    public static final String REPORT_TYPE_FILE_NAME = "transDayReport.xls";
    
    /**
     * 日报发送-job名称：错误数据报表
     */
    public static final String ERROR_DATA_DAY_REPORT = "error_data_day_report";
   
    /**
     * 日报发送-job名称：抽取预警报表
     */
    public static final String TRANS_DATA_REPORT = "trans_data_report";
    
    /**
     * 表结构字段 - 同步新增字段
     */
    public static final String SYNC_TABLE_INFO_ADD = "0";
    
    /**
     * 表结构字段 - 同步修改字段
     */
    public static final String SYNC_TABLE_INFO_UPDATE = "1";
    
    /**
     * mysql数据抽取到MongoDB - group
     */
    public static final String MYSQL_TO_MONGODB_LOG = "MYSQL_MONGODB";

    /**
     * kettle资源库名称
     */
    public static final String KETTLE_DATABASE_NAME = "kettleDBRep";

    /**
     * trans_info 的 type  =  1
     */
    public static final String TRANS_INFO_TYPE_THREAD_1 = "1";

    /**
     * trans_info 的 type  =  2
     */
    public static final String TRANS_INFO_TYPE_THREAD_2 = "2";

    /**
     * trans_info 的 type  =  3
     */
    public static final String TRANS_INFO_TYPE_THREAD_3 = "3";

    /**
     * trans_info 的 type  =  4
     */
    public static final String TRANS_INFO_TYPE_THREAD_4 = "4";

    /**
     * trans_info 的 type  =  5
     */
    public static final String TRANS_INFO_TYPE_THREAD_5 = "5";

    /**
     * trans_info 的 type  =  6
     */
    public static final String TRANS_INFO_TYPE_THREAD_6 = "7";

    /**
     * trans_info 的 type  =  7
     */
    public static final String TRANS_INFO_TYPE_THREAD_7 = "10";

    /**
     * trans_info 的 type = 99 自定义时间跑批抽取
     */
    public static final String TRANS_INFO_TYPE_THREAD_99 = "99";

    /**
     * ETL抽取配置包含的Trans_type
     */
    public static final String[] ETL_CONTANTS_TRANS_TYPE = {"1","2","3","4"};

    /**
     * 自定义结构抽取包含的Trans_type
     */
    public static final String[] DIYSTRUCT_CONTANTS_TRANS_TYPE = {"5","6"};
    /**
     * ETL抽取配置-增量转换
     */
    public static final String FULL_TRANS_TYPE_1 = "1";

    /**
     * ETL抽取配置-全量更新转换
     */
    public static final String FULL_TRANS_TYPE_2 = "2";

    /**
     * ETL抽取配置-清空全量转换
     */
    public static final String FULL_TRANS_TYPE_3 = "3";

    /**
     * ETL抽取配置-时间拉链转换
     */
    public static final String FULL_TRANS_TYPE_4 = "4";

    /**
     * 自定义表结构-全量更新转换
     */
    public static final String FULL_TRANS_TYPE_5 = "5";

    /**
     * 自定义表结构-清空全量转换
     */
    public static final String FULL_TRANS_TYPE_6 = "6";

    /**
     * 自定义表结构-增量抽取转换
     */
    public static final String FULL_TRANS_TYPE_7 = "7";


    /**
     * ETL抽取配置-最大转换数
     */
    public static final String ETL_TRANS_MAX_COUNT = "etlTransMaxCount";

    /**
     * 表结构自定义抽取-最大转换数
     */
    public static final String DIY_TABLE_TRANS_MAX_COUNT = "diyTableTransMaxCount";

}
