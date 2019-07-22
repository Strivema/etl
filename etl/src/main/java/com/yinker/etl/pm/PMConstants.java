package com.yinker.etl.pm;

/**
 * 基础模块常量类
 * 
 * @author Lenovo
 */
public class PMConstants {
	
	/**
	 * 数据库类型 - oracle
	 */
	public static final String DATA_BASE_TYPE_ORACLE = "1";
	
	/**
	 * 数据库类型 - mysql
	 */
	public static final String DATA_BASE_TYPE_MYSQL = "2";

	/**
	 * 数据源权限状态 - 正常
	 */
	public static final String DATA_SOURCE_GROUP_STATE = "1";

	/**
	 * 数据源权限状态 - 冻结
	 */
	public static final String DATA_SOURCE_GROUP_FREEZE = "0";

	/**
	 * 系统表状态 - 开启
	 */
	public static final String SYSTEM_INFO_LOG_STATUS_OFF = "0";

	/**
	 * 系统表状态 - 关闭
	 */
	public static final String SYSTEM_INFO_LOG_STATUS_ON = "1";
	/**
	 * 系统表状态 - 强制销毁
	 */
	public static final String SYSTEM_INFO_LOG_STATUS_STOP = "99";

	/**
	 * 发送邮件模板 - 单个转换超时预警
	 */
	public static final String MAIL_TEMPLEATE_TRANS_WARN = "transWran";

	/**
	 * 发送邮件模板 - 转换日报
	 */
	public static final String MAIL_TEMPLEATE_DAY_REPORT = "dayReport";
}
