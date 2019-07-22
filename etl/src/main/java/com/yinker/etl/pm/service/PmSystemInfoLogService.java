package com.yinker.etl.pm.service;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.dao.PmSystemInfoLogDao;
import com.yinker.etl.pm.model.PmSystemInfoLogQuery;
import com.yinker.etl.pm.model.base.PmSystemInfoLog;
import com.yinker.etl.pm.model.base.PmSystemInfoLogPK;
import org.zwork.framework.base.support.BaseService;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>PmSystemInfoLog表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:07
 * </pre>
 */
public class PmSystemInfoLogService extends BaseService<PmSystemInfoLogPK, PmSystemInfoLog, PmSystemInfoLogDao> implements
        org.zwork.framework.base.BaseService<PmSystemInfoLogPK, PmSystemInfoLog> {

    public void updateBatchByIP (PmSystemInfoLog pmSystemInfoLog) {
        modelDao.updateBatchByIP(pmSystemInfoLog);
    }

    /**
     * 通过IP 查询 系统信息
     *
     * @param ip
     * @return
     */
    public PmSystemInfoLog getSystemInfo (String ip) {
        PmSystemInfoLog log = new PmSystemInfoLog();
        PmSystemInfoLogQuery query = new PmSystemInfoLogQuery();
        query.setIp(ip);
        query.setStatus(PMConstants.SYSTEM_INFO_LOG_STATUS_ON);
        query.setSortColumns(" start_time desc");
        List<PmSystemInfoLog> systemInfoLogs = selectByEntity(query);
        if (systemInfoLogs == null) {
            return null;
        } else {
            return systemInfoLogs.get(0);
        }
    }

    /**
     * 获取当前服务器的SN
     * @return
     * @throws Exception
     */
    public String getSystemSN () throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        String IP = address.getHostAddress();//192.168.0.121
        PmSystemInfoLog infoLog = getSystemInfo(IP);
        if (infoLog != null) {
            return infoLog.getSn();
        }
        else{
            return "";
        }
    }

    /**
     * 获取当前系统的所有SN
     * @return
     */
    public String[] getSystemSNs() {
        Map<String,String> systemMap = new HashMap<>();
        PmSystemInfoLogQuery query = new PmSystemInfoLogQuery();
        query.setStatus(PMConstants.SYSTEM_INFO_LOG_STATUS_ON);
        query.setSortColumns(" start_time asc");
        List<PmSystemInfoLog> systemInfoLogs = selectByEntity(query);

        if(systemInfoLogs != null){
            for(PmSystemInfoLog systemInfoLog : systemInfoLogs) {
                systemMap.put(systemInfoLog.getIp(),systemInfoLog.getSn());
            }
        }

        String[] sns = new String[systemMap.size()];
        int i = 0;
        for(String key : systemMap.keySet()) {
            sns[i++] = systemMap.get(key);
        }

        return sns;

    }
}
