package com.yinker.etl.trans.service;

import java.util.Date;

import org.zwork.framework.base.support.BaseService;
import org.zwork.srdp.rbac.session.User;

import com.yinker.etl.trans.dao.TransConfigChangeLogDao;
import com.yinker.etl.trans.model.base.TransConfigChangeLog;
import com.yinker.etl.trans.model.base.TransConfigChangeLogPK;

/** 
 * <pre>
 * <b>类描述:</b>TransConfigChangeLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:40:00
 * </pre>
 */
public class TransConfigChangeLogService extends BaseService<TransConfigChangeLogPK, TransConfigChangeLog, TransConfigChangeLogDao> implements 
                org.zwork.framework.base.BaseService<TransConfigChangeLogPK, TransConfigChangeLog> {

	
	/**
	 * 插入变更记录日志
	 * @param user 用户
	 * @param tableName 表名
	 * @param operateType 操作类型
	 * @param log 日志
	 */
	public void wirteChangeLog(User user,String tableName,String operateType,String log,String id){
		TransConfigChangeLog changeLog=new TransConfigChangeLog();
		changeLog.setId(id);
		changeLog.setUserId(user.getId());
		changeLog.setOperateType(operateType);
		changeLog.setOperateLog(log);
		changeLog.setOperateTime(new Date());
		changeLog.setCreateTime(new Date());
		changeLog.setLastUpdateTime(new Date());
		changeLog.setTableName(tableName);
		this.insert(changeLog);
	}

	
}
