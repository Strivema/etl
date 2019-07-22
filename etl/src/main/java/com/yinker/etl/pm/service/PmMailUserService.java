package com.yinker.etl.pm.service;

import com.yinker.etl.pm.dao.PmMailUserDao;
import com.yinker.etl.pm.model.PmMailUserQuery;
import com.yinker.etl.pm.model.base.PmMailUser;
import com.yinker.etl.pm.model.base.PmMailUserPK;
import org.zwork.framework.base.support.BaseService;

import java.util.List;

/** 
 * <pre>
 * <b>类描述:</b>PmMailUser表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:03
 * </pre>
 */
public class PmMailUserService extends BaseService<PmMailUserPK, PmMailUser, PmMailUserDao> implements 
                org.zwork.framework.base.BaseService<PmMailUserPK, PmMailUser> {

    /**
     * 根据传入的Group 查询出此组 下的地址与名称
     * @param group     用户组
     * @param address   地址
     * @param names     名称
     */
    public void getAddressByGroup(String group,String[] address,String[] names){
        PmMailUserQuery query = new PmMailUserQuery();
        query.setGroup(group);
        List<PmMailUser> mailUsers = selectByEntity(query);
        address = new String[mailUsers.size()];
        names = new String[mailUsers.size()];
        for(int i = 0; i < mailUsers.size(); i++) {
            PmMailUser user = mailUsers.get(i);
            address[i] = user.getEmail();
            names[i] = user.getName();
        }
    }

}
