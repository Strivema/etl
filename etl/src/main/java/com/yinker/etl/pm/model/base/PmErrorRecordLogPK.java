package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.SystemUtils;
import org.zwork.framework.base.BaseEntityPK;
import org.zwork.framework.base.support.BasePageQuery;

/** 
 * <pre>
 * <b>类描述:</b>PmErrorRecordLog表的主键对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2017-11-06 10:39:50
 * </pre>
 */
public class PmErrorRecordLogPK extends BasePageQuery implements BaseEntityPK {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 表的名称 
	 */
	public static final String TABLE_NAME = "pm_error_record_log";
	
	/**
	 * 记录表ID
	 */
    public static final String ALIAS_ERROR_ID = "errorId";
	
	/* 记录表ID */
    private Integer errorId;
	
	
    public Integer getErrorId(){
        return errorId;
    }
    public void setErrorId(Integer errorId){
        this.errorId = errorId;
    }
	
	@Override
    public String getEntityName() {
        return PmErrorRecordLogPK.TABLE_NAME;
    }

    @Override
	public boolean hasPKColums() {
		return true;
	}

    @Override
    public String pkString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	errorId=").append(getErrorId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
