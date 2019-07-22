package com.yinker.etl.rabbitmq.receive.service;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.sys.service.MailSendService;
import com.yinker.etl.util.CreateExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMailMQService {

    @Resource(name = "mailSendService")
    private MailSendService mailSendService;

    private static final Logger logger = LoggerFactory.getLogger(SendMailMQService.class);

    /**
     * 定时任务方法
     */
    @SuppressWarnings("unchecked")
    public void handle (String message) {

        logger.info("发送邮件start...." + message);
        try {
            HashMap<String, Object> json = JSON.parseObject(message, HashMap.class);
            // 是否发送附件
            if (!(Boolean) json.get("isFile")) {
                sendSimpleEmail(json);
            } else {
                sendAttachmentEmail(json);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSimpleEmail (HashMap<String, Object> json) {

        try {
            mailSendService.sendMailWithFile((String) json.get("serverId"), (String) json.get("title"), (String) json.get("from"), ((String) json.get("address")).split(","), ((String) json.get("names")).split(","), (String) json.get("body"), null, null);
        } catch(Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendAttachmentEmail (HashMap<String, Object> json) {
        try {
            String filePath = new String(json.get("filePath").toString().getBytes("gb2312"), "ISO8859-1");
            logger.debug("文件路径为 ： " + filePath);
            List<Map<String, Object>> newList = (List<Map<String, Object>>) json.get("newList");
            List<String> nameList = (List<String>) json.get("nameList");
            Map<String, String> map = (Map<String, String>) json.get("map");

            //创建Excel
            logger.info("创建Excel");
            CreateExcelUtil.createExcel(filePath, newList, "TRANS_DAY_REPORT", nameList, map);
            //发送
            mailSendService.sendMailWithFile((String) json.get("serverId"), (String) json.get("title"), (String) json.get("from"), ((String) json.get("address")).split(","),
                    ((String) json.get("names")).split(","), (String) json.get("body"), new String[]{(String) json.get("fileName")}, new File[]{new File(filePath)});
        } catch(Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

}