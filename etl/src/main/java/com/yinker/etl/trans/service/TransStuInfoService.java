package com.yinker.etl.trans.service;

import com.yinker.etl.trans.dao.TransStuInfoDao;
import com.yinker.etl.trans.model.base.TransStuInfo;
import com.yinker.etl.trans.model.base.TransStuInfoPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseService;

import javax.annotation.Resource;
import java.util.List;

public class TransStuInfoService extends BaseService<TransStuInfoPK, TransStuInfo, TransStuInfoDao> implements org.zwork.framework.base.BaseService<TransStuInfoPK, TransStuInfo>{

    private static final Logger LOGGER = LoggerFactory.getLogger(TransStuInfoService.class);

    @Resource(name="transStuInfoDao")
    private TransStuInfoDao transStuInfoDao;

    public TransStuInfo selectByPK (String id){
        if(StringUtils.isNotEmpty(id)){
            TransStuInfo info = new TransStuInfo();
            TransStuInfoPK pk = new TransStuInfoPK();
            pk.setId(id);
            info = this.selectByPK(pk);

            return info;
        }
        return null;
    }
    public List<TransStuInfo> selectByEntity(TransStuInfo entity){
        if(entity!=null){
            return transStuInfoDao.selectByEntity(entity);
        }
        return null;
    }

    public void insertTransStuInfo (TransStuInfo entity) {
        insert(entity);
    }

    public void updateTransStuInfo (TransStuInfo entity) {

        updateByPK(entity);
    }


}
