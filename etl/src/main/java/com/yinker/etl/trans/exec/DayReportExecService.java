package com.yinker.etl.trans.exec;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.model.PmDataSourceQuery;
import com.yinker.etl.pm.model.base.PmDataSource;
import com.yinker.etl.pm.model.base.PmMailTemplate;
import com.yinker.etl.pm.service.PmDataSourceService;
import com.yinker.etl.pm.service.PmMailTemplateService;
import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.sys.service.MailSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.service.TransTableStructureInfoService;
import com.yinker.etl.trans.util.CommonUtils;
import com.yinker.etl.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 简单的实现了Spring QuartzJobBean接口
 * </p>
 * <p>
 * 用于发送数据日报定时任务
 */
public class DayReportExecService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DayReportExecService.class);

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "transTableStructureInfoService")
    private TransTableStructureInfoService transTableStructureInfoService;

    @Resource(name = "pmMailTemplateService")
    private PmMailTemplateService pmMailTemplateService;

    @Resource(name = "pmDataSourceService")
    private PmDataSourceService pmDataSourceService;

    @Resource(name = "mailSendService")
    private MailSendService mailSendService;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        try {
            LOGGER.info("发送日报start...");
            JobKey jobKey = context.getJobDetail().getKey();

            execute(jobKey);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 把发送日报任务放到rabbitMQ中
     */
    private void execute(JobKey jobKey) {
        try {
            if (StringUtils.isNotEmpty(jobKey.getGroup()) && TransConstants.TABLE_DAY_REPORT_GROUP_NAME.equals(jobKey.getGroup())) {
                DataSourceContextHolder.clearDataSourceKey();
                //获取邮件模版
                PmMailTemplate template = pmMailTemplateService.getWarnConfigByCode(PMConstants.MAIL_TEMPLEATE_DAY_REPORT);
                transDataReport(template);
            } else {
                throw new Exception("无效group参数值[" + jobKey.getGroup() + "]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 制作转换日报Excel并把发送日报任务放到rabbitMQ中
     */
    public void transDataReport(PmMailTemplate template) {

        List<String> nameList = new ArrayList<String>();
        nameList.add("转换名称");
        nameList.add("源数据源");
        nameList.add("目标数据源");
        nameList.add("源表名称");
        nameList.add("目标表名称");
        nameList.add("源数据量");
        nameList.add("目标数据量");
        nameList.add("当天转换次数");
        nameList.add("最大抽取时间");
        nameList.add("最小抽取时间");
        nameList.add("平均抽取时间");
        // 创建附件中的excel
        Map<String, String> map = new HashMap<String, String>();
        map.put("转换名称", "trans_name");
        map.put("源数据源", "src_db_code");
        map.put("目标数据源", "target_db_code");
        map.put("源表名称", "src_tb_code");
        map.put("目标表名称", "target_tb_code");
        map.put("源数据量", "src_count");
        map.put("目标数据量", "target_count");
        map.put("当天转换次数", "count");
        map.put("最大抽取时间", "max");
        map.put("最小抽取时间", "min");
        map.put("平均抽取时间", "avg");

        try {
            //日报接收者
            List<Map<String, Object>> userList = transInfoService.getDayReportUsers();
            //Excel文件临时位置
            String dirPath = this.getClass().getClassLoader().getResource("../../").getPath();

            PmDataSourceQuery pmDataSourceQuery = new PmDataSourceQuery();
            //遍历接收者并发送至MQ
            for(Map userMap : userList){
                String name = (String) userMap.get("user_name");
                String email = (String) userMap.get("email");

                //个人日报
                List<Map<String, Object>> logList = transInfoService.getTransDayReport(email);
                List<Map<String, Object>> newList = new ArrayList<>();
                //取得源数据量、目标数据量
                for(Map logMap : logList){
                    String transId = logMap.get("trans_id").toString();
                    String srcDbId = logMap.get("src_db_id").toString();
                    String tgtDbId = logMap.get("target_db_id").toString();
                    String srcTb = logMap.get("src_tb_code").toString();
                    String tgtTb = logMap.get("target_tb_code").toString();
                    String sqll = logMap.get("sqll").toString();

                    if("STS".equals(transId.substring(0,3))){
                        String sqlsrc = " select count(1) count from (" + sqll + ") tmp ";
                        pmDataSourceQuery.setId(srcDbId);
                        ResultSet rs = getResultSet(pmDataSourceQuery, sqlsrc);
                        if(rs.next()) {
                            logMap.put("src_count", rs.getInt("count"));
                        }

                        String sqltgt = " select count(1) count from " + tgtTb;
                        pmDataSourceQuery.setId(tgtDbId);
                        rs = getResultSet(pmDataSourceQuery, sqltgt);
                        if(rs.next()) {
                            logMap.put("target_count", rs.getInt("count"));
                        }

                        }else {

                        String sqlsrc = " select count(1) count from " + srcTb;
                        if(sqll != null && !"".equals(sqll) && !"".equals(sqll.trim())){
                            sqlsrc += " where " + sqll;
                        }
                        pmDataSourceQuery.setId(srcDbId);
                        ResultSet rs = getResultSet(pmDataSourceQuery, sqlsrc);
                        if(rs.next()) {
                            logMap.put("src_count", rs.getInt("count"));
                        }

                        String sqltgt = " select count(1) count from " + tgtTb;
                        pmDataSourceQuery.setId(tgtDbId);
                        rs = getResultSet(pmDataSourceQuery, sqltgt);
                        if(rs.next()) {
                            logMap.put("target_count", rs.getInt("count"));
                        }

                    }
                    newList.add(logMap);
                }
                //要发送的Excel文件路径
                String tmpString = dirPath + "filetemp/" + "[" + CommonUtils.getCurDateString() + "][" + email + "]" + TransConstants.REPORT_TYPE_FILE_NAME;
                String filePath = new String(tmpString.getBytes("gb2312"), "ISO8859-1");
                //设置邮件内容
                Map<String, String> parameMap = new HashMap<>();
                parameMap.put("name", name);
                parameMap.put("today", CommonUtils.getCurDateString());
                String body = StringUtil.parseString(template.getBody(), parameMap);
                //发送至MQ
                mailSendService.sendMailToMQWithExcel(template.getServerId(),template.getTitle(), template.getAddresser(), email, name, body, true, dirPath, filePath, newList, nameList, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet(PmDataSourceQuery pmDataSourceQuery, String sql){
        PmDataSource pmDataSource = pmDataSourceService.selectByEntity(pmDataSourceQuery).get(0);
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = transTableStructureInfoService.getConnection(pmDataSource);
            rs = transTableStructureInfoService.checkSQL(connection, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

}
