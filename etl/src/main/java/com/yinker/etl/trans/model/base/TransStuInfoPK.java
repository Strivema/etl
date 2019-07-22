package com.yinker.etl.trans.model.base;

import org.zwork.common.utils.SystemUtils;
import org.zwork.framework.base.BaseEntityPK;
import org.zwork.framework.base.support.BasePageQuery;

public class TransStuInfoPK extends BasePageQuery implements BaseEntityPK{

    private static final long serialVersionUID = 1L;


    public static final String TABLE_NAME = "trans_stu_info";


    public static final String ALIAS_ID = "id";

    private String id;


    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    @Override
    public String getEntityName() {
        return TransInfoPK.TABLE_NAME;
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
