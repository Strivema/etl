package com.yinker.etl.pm.model.base;

import org.zwork.common.utils.SystemUtils;
import org.zwork.framework.base.BaseEntityPK;
import org.zwork.framework.base.support.BasePageQuery;

public class PmMongoTransferBatchLogPK extends BasePageQuery implements BaseEntityPK {

    private static final long serialVersionUID = 1L;
    
    /**
	 * 表的名称 
	 */
	public static final String TABLE_NAME = "pm_mongo_transfer_batch_log";
    
    /**
     * 表Id
     */
    public static final String ALIAS_ID = "id";
    
    /* 数据源配置表Id */
    private String id;
    
    
    public String getId(){
        return id;
    }
    public void setId(String errorIdArray){
        this.id = errorIdArray;
    }
    @Override
    public String getEntityName() {
        return PmMongoTransferBatchLogPK.TABLE_NAME;
    }

    @Override
    public boolean hasPKColums() {
        return true;
    }

    @Override
    public String pkString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName()).append("@").append(hashCode()).append("{").append(SystemUtils.LINE_SEPARATOR);
        str.append("    id=").append(getId()).append(SystemUtils.LINE_SEPARATOR);
        str.append("}").append(SystemUtils.LINE_SEPARATOR);
        return str.toString();
    }
}
