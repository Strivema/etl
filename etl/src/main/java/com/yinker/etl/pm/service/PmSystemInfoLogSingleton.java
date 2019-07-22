package com.yinker.etl.pm.service;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.model.PmSystemInfoLogQuery;
import com.yinker.etl.pm.model.base.PmSystemInfoLog;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单例对象，获取系统信息
 * Created by 崔博文 on 2018/1/30.15:25
 */
public class PmSystemInfoLogSingleton {

    @Resource(name = "pmSystemInfoLogService")
    private PmSystemInfoLogService pmSystemInfoLogService;

    private static volatile PmSystemInfoLogSingleton instance = null;

    private PmSystemInfoLogSingleton () {
    }

    public static PmSystemInfoLogSingleton getInstance () {
        if (instance == null) {
            synchronized(PmSystemInfoLogSingleton.class) {
                if (instance == null) {
                    instance = new PmSystemInfoLogSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * 通过IP 查询 系统信息
     * @param ip
     * @return
     */
    public PmSystemInfoLog getSystemInfo (String ip) {
        PmSystemInfoLog log = new PmSystemInfoLog();
        PmSystemInfoLogQuery query = new PmSystemInfoLogQuery();
        query.setIp(ip);
        query.setStatus(PMConstants.SYSTEM_INFO_LOG_STATUS_ON);
        query.setSortColumns(" start_time desc");
        List<PmSystemInfoLog> systemInfoLogs = pmSystemInfoLogService.selectByEntity(query);
        if(systemInfoLogs == null ) {
            return null;
        } else {
            return systemInfoLogs.get(0);
        }
    }
}
