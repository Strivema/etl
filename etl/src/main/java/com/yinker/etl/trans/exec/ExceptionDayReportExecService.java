package com.yinker.etl.trans.exec;

import com.yinker.etl.pm.PMConstants;
import com.yinker.etl.pm.model.base.PmMailTemplate;
import com.yinker.etl.pm.service.PmMailTemplateService;
import com.yinker.etl.pm.service.dataSource.DataSourceContextHolder;
import com.yinker.etl.sys.service.MailSendService;
import com.yinker.etl.trans.TransConstants;
import com.yinker.etl.trans.service.TransInfoService;
import com.yinker.etl.trans.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 简单的实现了Spring QuartzJobBean接口
 * </p>
 * <p>
 * 用于发送数据日报定时任务
 */
public class ExceptionDayReportExecService extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionDayReportExecService.class);

    @Resource(name = "transInfoService")
    private TransInfoService transInfoService;

    @Resource(name = "pmMailTemplateService")
    private PmMailTemplateService pmMailTemplateService;

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
                transExceptionReport(template);
            } else {
                throw new Exception("无效group参数值[" + jobKey.getGroup() + "]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送转换异常日报任务放到rabbitMQ中
     */
    public void transExceptionReport(PmMailTemplate template) {
        try {
            template.setTitle("日报：错误转换日报");
            //日报接收者
            List<Map<String, Object>> userList = transInfoService.getExceptionReportUsers();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            for(Map userMap : userList){
                String name = (String) userMap.get("user_name");
                String email = (String) userMap.get("email");

                //个人日报
                List<Map<String, Object>> ErrList = transInfoService.getErrorRecordDayReport(email);
                List<Map<String, Object>> ETSList = transInfoService.getErrorTSDayReport(email);
                if((ErrList == null && ETSList == null) || (ErrList.size() == 0 && ETSList.size() == 0)){
                    continue;
                }
                //设置邮件内容
                StringBuffer body = new StringBuffer();
                body.append("您好！【" + name +"】这是今日【"+ CommonUtils.getCurDateString() + "】的转换异常日报，请查看并处理：<br>");
                String tdStyle1 = "style=\"word-break:keep-all;white-space:nowrap;text-decoration: none;text-align:center;padding:5px\"";
                String tdStyle2 = "style=\"word-break:keep-all;white-space:nowrap;text-decoration: none;text-align:left;padding:5px\"";
                if(ErrList != null && ErrList.size() > 0){
                    body.append("<br>转换错误记录：<br>");
                    body.append("<div><table width=\"600\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\">");
                    body.append("<tr>");
                    body.append("<td " + tdStyle1 + " >错误转换名</td>");
                    body.append("<td " + tdStyle1 + " >错误表名</td>");
                    body.append("<td " + tdStyle1 + " >错误状态</td>");
                    body.append("<td " + tdStyle1 + " >错误数</td>");
                    body.append("<td " + tdStyle1 + " >时间</td>");
                    body.append("<td " + tdStyle1 + " >错误描述</td>");
                    body.append("</tr>");
                    for(Map m : ErrList){
                        body.append("<tr>");
                        body.append("<td " + tdStyle2 + ">" + m.get("err_trans_name").toString() + "</td>");
                        body.append("<td " + tdStyle2 + ">" + (m.get("err_table_name") == null ? "":m.get("err_table_name").toString()) + "</td>");
                        body.append("<td " + tdStyle1 + ">" + m.get("err_status").toString() + "</td>");
                        body.append("<td " + tdStyle1 + ">" + m.get("err_count").toString() + "</td>");
                        body.append("<td " + tdStyle1 + ">" + sdf.format(m.get("err_create_time")) + "</td>");
                        body.append("<td " + tdStyle1 +"title=\"" + m.get("err_desc").toString() + "\">" + (m.get("err_desc").toString().length() < 200 ? m.get("err_desc").toString() : m.get("err_desc").toString().substring(0,30) + "...") + "</td>");
                        body.append("</tr>");
                    }
                    body.append("</table></div>");
                }
                if(ETSList != null && ETSList.size() > 0){
                    body.append("<br>表结构同步错误记录：<br>");
                    body.append("<div><table width=\"400\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\">");
                    body.append("<td " + tdStyle1 + " >错误转换名</td>");
                    body.append("<td " + tdStyle1 + " >错误表名</td>");
                    body.append("<td " + tdStyle1 + " >错误状态</td>");
                    body.append("<td " + tdStyle1 + " >时间</td>");
                    body.append("<td " + tdStyle1 + " >错误描述</td>");
                    for(Map m : ETSList){
                        body.append("<tr>");
                        body.append("<td " + tdStyle2 + ">" + m.get("err_trans_name").toString() + "</td>");
                        body.append("<td " + tdStyle2 + ">" + (m.get("err_table_name") == null ? "":m.get("err_table_name").toString()) + "</td>");
                        body.append("<td " + tdStyle1 + ">" + m.get("err_status").toString() + "</td>");
                        body.append("<td " + tdStyle1 + ">" + sdf.format(m.get("err_create_time")) + "</td>");
                        body.append("<td " + tdStyle1 +"title=\"" + m.get("err_desc").toString() + "\">" + (m.get("err_desc").toString().length() < 200 ? m.get("err_desc").toString() : m.get("err_desc").toString().substring(0,30) + "...") + "</td>");
                        body.append("</tr>");
                    }
                    body.append("</table></div>");
                }

                //发送至MQ
                mailSendService.sendMailToMQ(template.getServerId(),template.getTitle(), template.getAddresser(),email, name, body.toString(), false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
