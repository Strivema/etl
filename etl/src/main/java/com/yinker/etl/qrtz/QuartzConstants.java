package com.yinker.etl.qrtz;

public class QuartzConstants {

    public static final String KETTLE_REPO = "repo";

    public final static String SIMPLE_TRIGGER = "0";
    public final static String CRON_TRIGGER = "1";

    /**
     * job绑定定时抽取类型
     */
    public static final String JOB_EXEC_CLASS_TYPE = "0";
    /**
     * job绑定定时抽取类
     */
    public static final String JOB_EXEC_CLASS = "com.yinker.etl.exec.KettleExecService";

    /**
     * 表抽取定时抽取类型
     */
    public static final String TABLE_TRANS_EXEC_CLASS_TYPE = "1";
    /**
     * 表抽取定时抽取类
     */
    public static final String TABLE_TRANS_EXEC_CLASS = "com.yinker.etl.trans.batch.TableTransBatchService";

    /**
     * 转换报表抽取类型
     */
    public static final String TRANS_REPORT_EXEC_CLASS_TYPE = "2";
    /**
     * 转换报表抽取类型
     */
    public static final String TRANS_REPORT_EXEC_CLASS = "com.yinker.etl.exec.TransReportExecService";

    /**
     * 日报发送类型
     */
    public static final String DAY_REPORT_TO_EMAIL = "3";
    /**
     * 日报发送类
     **/
    public static final String DAY_REPORT_TO_MAIL_CLASS = "com.yinker.etl.exec.DayReportExecService";

    /**
     * MYSQL TO MONGO
     */
    public static final String TRANS_TO_MONGO_CLASS_TYPE = "4";
    /**
     * MYSQL TO MONGO
     */
    public static final String TRANS_TO_MONGO_CLASS = "com.yinker.etl.mongodb.exec.MysqlToMongoExecService";

    /**
     * 自定义跑批执行类
     */
    public static final String TRANS_DIY_ETL_CLASS_TYPE = "5";
    /**
     * 自定义跑批执行类 增量
     */
    public static final String TRANS_DIY_ETL_CLASS_1 = "com.yinker.etl.trans.batch.TableTransBatchService";
    /**
     * 自定义跑批执行类 全量更新
     */
    public static final String TRANS_DIY_ETL_CLASS_2 = "com.yinker.etl.trans.batch.DpFullUpdateBatchService";
    /**
     * 自定义跑批执行类 清空全量
     */
    public static final String TRANS_DIY_ETL_CLASS_3 = "com.yinker.etl.trans.batch.TruncateAfterFullTransBatchService";
    /**
     * 自定义跑批执行类 时间拉链
     */
    public static final String TRANS_DIY_ETL_CLASS_4 = "com.yinker.etl.trans.batch.FullBakBatchService";

    /**
     * 自定义跑批执行类(自定义表结构抽取)
     */
    public static final String TRANS_DIY_ETL_TABLESTRUCT_CLASS_TYPE = "6";
    /**
     * 自定义跑批执行类(自定义表结构抽取) 全量更新
     */
    public static final String TRANS_DIY_ETL_TABLESTRUCT_CLASS_1 = "com.yinker.etl.trans.batch.DpFullUpdateTableStructureBatchService";
    /**
     * 自定义跑批执行类(自定义表结构抽取) 清空全量
     */
    public static final String TRANS_DIY_ETL_TABLESTRUCT_CLASS_2 = "com.yinker.etl.trans.batch.TruncateAfterFullTransTableStructureBatchService";
    /**
     * 自定义跑批执行类(自定义表结构抽取) 增量抽取
     */
    public static final String TRANS_DIY_ETL_TABLESTRUCT_CLASS_3 = "com.yinker.etl.trans.batch.TableStructureIncrementBatchService";

    /**
     * 定时任务组 ： Table_Trans
     */
    public static final String TRIGGER_GROUP_TABLE_TRANS = "TABLE_TRANS";
    /**
     * 定时任务组 ： DIY_ETLTRANS 普通转换抽取
     */
    public static final String TRIGGER_GROUP_DIY_ETLTRANS = "DIY_ETLTRANS";
    /**
     * 定时任务：自定义抽取时间的调度任务名的头（普通转换）增量
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_1 = "TABLE_TRANS_INCREMENT_99_";

    /**
     * 定时任务：自定义抽取时间的调度任务名的头（普通转换） 全量更新
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_2 = "TABLE_TRANS_FULL_UPDATE_99_";

    /**
     * 定时任务：自定义抽取时间的调度任务名的头（普通转换） 清空全量
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_3 = "TABLE_TRANS_TUNCATE_FULL_99_";

    /**
     * 定时任务：自定义抽取时间的调度任务名的头（普通转换） 时间拉链
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_4 = "TABLE_TRANS_FULL_BAK_99_";


    /**
     * 定时任务：自定义抽取时间的调度任务名的头（自定义表结构） 全量更新抽取
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_1 = "TABLESTRUCT_TABLE_TRANS_FULL_UPDATE_99_";

    /**
     * 定时任务：自定义抽取时间的调度任务名的头（自定义表结构） 清空全量抽取
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_2 = "TABLESTRUCT_TABLE_TRANS_TRUNCATE_FULL_99_";

    /**
     * 定时任务：自定义抽取时间的调度任务名的头（自定义表结构） 增量抽取
     */
    public static final String TRIGGER_NAME_DIY_ETLTRANS_TABLESTRUCT_3 = "TABLESTRUCT_TABLE_TRANS_INCREMENT_99_";

}
