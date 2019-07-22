package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.SystemUtils;
import org.zwork.framework.base.BaseEntityPK;
import org.zwork.framework.base.support.BasePageQuery;

/** 
 * <pre>
 * <b>类描述:</b>PmKettleJobLog表的主键对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:52
 * </pre>
 */
public class PmKettleJobLogPK extends BasePageQuery implements BaseEntityPK {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 表的名称 
	 */
	public static final String TABLE_NAME = "pm_kettle_job_log";
	
	/**
	 * 转换日志id
	 */
    public static final String ALIAS_ID_JOB = "idJob";
	
	/* 转换日志id */
    private String idJob;
	
	
    public String getIdJob(){
        return idJob;
    }
    public void setIdJob(String idJob){
        this.idJob = idJob;
    }
	
	@Override
    public String getEntityName() {
        return PmKettleJobLogPK.TABLE_NAME;
    }

    @Override
	public boolean hasPKColums() {
		return true;
	}

    @Override
    public String pkString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	idJob=").append(getIdJob()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
