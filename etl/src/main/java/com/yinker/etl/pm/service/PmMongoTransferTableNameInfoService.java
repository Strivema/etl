package com.yinker.etl.pm.service;


import com.yinker.etl.pm.dao.PmMongoTransferTableNameInfoDao;
import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfo;
import com.yinker.etl.pm.model.base.PmMongoTransferTableNameInfoPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.framework.base.support.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>PmMongoTransferTableNameInfo表对应的业务处理类。
 * </pre>
 */
public class PmMongoTransferTableNameInfoService extends BaseService<PmMongoTransferTableNameInfoPK, PmMongoTransferTableNameInfo, PmMongoTransferTableNameInfoDao> implements
                org.zwork.framework.base.BaseService<PmMongoTransferTableNameInfoPK, PmMongoTransferTableNameInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmMongoTransferTableNameInfoService.class);


    /**
     * 获取ETL数据库的日志表名称List
     * @return
     */
    public List<Map<String,Object>> selectEtlLogList(){
        return this.modelDao.selectEtlLogList();
    }

    /**
     * 获取对应表的迁移步长（20M数据）
     * @return
     */
    public int selectTransSteps(String tableName){
        return this.modelDao.selectTransSteps(tableName);
    }

}
