package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.SystemUtils;
import org.zwork.framework.base.BaseEntityPK;
import org.zwork.framework.base.support.BasePageQuery;

/** 
 * <pre>
 * <b>类描述:</b>PmSystemInfoLog表的主键对应的java对象，该文件为工具自动生成，请勿手工修改!
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:39:08
 * </pre>
 */
public class PmSystemInfoLogPK extends BasePageQuery implements BaseEntityPK {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 表的名称 
	 */
	public static final String TABLE_NAME = "pm_system_info_log";
	
	/**
	 * 编号
	 */
    public static final String ALIAS_ID = "id";
	
	/* 编号 */
    private Integer id;
	
	
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
	
	@Override
    public String getEntityName() {
        return PmSystemInfoLogPK.TABLE_NAME;
    }

    @Override
	public boolean hasPKColums() {
		return true;
	}

    @Override
    public String pkString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
		str.append("	id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
