package com.yinker.etl.sys.service;

import com.alibaba.fastjson.JSON;
import com.yinker.etl.pm.model.base.PmMailSendLog;
import com.yinker.etl.pm.model.base.PmMailServerConfig;
import com.yinker.etl.pm.model.base.PmMailServerConfigPK;
import com.yinker.etl.pm.service.PmMailSendLogService;
import com.yinker.etl.pm.service.PmMailServerConfigService;
import com.yinker.etl.pm.service.PmMailUserService;
import com.yinker.etl.rabbitmq.send.service.impl.EtlSendService;
import com.yinker.etl.sys.SYSConstants;
import com.yinker.etl.trans.util.CommonUtils;
import com.yinker.etl.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by 崔博文 on 2018/2/5.11:05
 */
@Service
public class MailSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendService.class);

    @Resource(name = "pmMailSendLogService")
    private PmMailSendLogService pmMailSendLogService;

    @Resource(name = "pmMailServerConfigService")
    private PmMailServerConfigService pmMailServerConfigService;

    @Resource(name = "pmMailUserService")
    private PmMailUserService pmMailUserService;

    @Resource(name = "etlSendService")
    private EtlSendService etlSendService;

    @Value("%{rabbitmq.projectName}")
    private String rabbitmqName;

    private String group;
    String[] address;
    String[] names;

    public void sendMailByGroup (String serverId, String title, String group, String body) {
        this.group = group;
        pmMailUserService.getAddressByGroup(group, address, names);
        simpleSendMail(serverId, title, address, names, body);
    }

    public void simpleSendMail (String serverId, String title, String[] address, String[] names, String body) {
        this.sendMailWithFile(serverId, title, SYSConstants.SEND_EMAIL_FROMNAME, address, names, body, null, null);
    }

    /**
     * 多人多附件发送邮件，如果不想带附件，fileName 与 file 传NULL 即可
     *
     * @param serverId 发送邮件服务器编号
     * @param title    邮件标题
     * @param from     发件人名称
     * @param address  收件人地址
     * @param names    收件人地址
     * @param body     内容体
     * @param fileName 附件名称
     * @param file     附件
     * @throws Exception
     */
    public void sendMailWithFile (String serverId, String title, String from, String[] address, String[] names, String body, String[] fileName, File[] file) {
        try {
            // 发送器
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            // 建立邮件消息,发送简单邮件和html邮件的区别
            MimeMessage mailMessage = mailSender.createMimeMessage();
            // 为防止乱码，添加编码集设置  发送附件 则 参数为 multipart 为 ture
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, fileName != null && fileName.length > 0, "UTF-8");
            // 通过数据库查询对应的发送邮件的服务配置
            PmMailServerConfigPK serverConfigPK = new PmMailServerConfigPK();
            serverConfigPK.setId(serverId);
            PmMailServerConfig serverConfig = pmMailServerConfigService.selectByPK(serverConfigPK);
            if (serverConfig == null) {
                throw new Exception("没有找到编号为[" + serverId + "]的服务器配置");
            }
            //设定mail server
            mailSender.setHost(serverConfig.getHost());
            mailSender.setPort(Integer.parseInt(serverConfig.getPort()));
            mailSender.setUsername(serverConfig.getUserName());
            mailSender.setPassword(serverConfig.getPassword());
            mailSender.setDefaultEncoding("GBK");
            Properties pro = System.getProperties();
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.ssl.enable", "true");
            pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            mailSender.setJavaMailProperties(pro);
            //建立邮件消息
            //设置收件人、寄件人
            messageHelper.setTo(address);
            messageHelper.setFrom(serverConfig.getUserName(), from);
            messageHelper.setSubject(title);
            messageHelper.setText(body, true);
            //附件内容
            if (fileName != null && file != null) {
                if (fileName.length == file.length) {
                    for(int i = 0; i < fileName.length; i++) {
                        messageHelper.addAttachment(fileName[i], file[i]);
                    }
                } else {
                    throw new Exception("附件和附件名称不匹配~！");
                }
            }

            //发送邮件
            mailSender.send(mailMessage);
            insertSendMailLog(from, title, body, true, address, names);
            LOGGER.info("邮件发送成功！");
        } catch(MessagingException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 多人多附件发送邮件(直接方式，不通过serverId)，如果不想带附件，fileName 与 file 传NULL 即可
     *
     * @param host     服务器
     * @param port     端口号
     * @param userName 用户名
     * @param password 密码
     * @param title    邮件标题
     * @param from     发件人名称
     * @param address  收件人地址
     * @param names    收件人地址
     * @param body     内容体
     * @param fileName 附件名称
     * @param file     附件
     * @throws Exception
     */
    public void sendMailWithFile (String host, String port, String userName, String password, String title, String from, String[] address, String[] names, String body, String[] fileName, File[] file) throws Exception{

            // 发送器
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            // 建立邮件消息,发送简单邮件和html邮件的区别
            MimeMessage mailMessage = mailSender.createMimeMessage();
            // 为防止乱码，添加编码集设置  发送附件 则 参数为 multipart 为 ture
            MimeMessageHelper messageHelper  = new MimeMessageHelper(mailMessage, fileName != null && fileName.length > 0, "UTF-8");

            //设定mail server
            mailSender.setHost(host);
            mailSender.setPort(Integer.parseInt(port));
            mailSender.setUsername(userName);
            mailSender.setPassword(password);
            mailSender.setDefaultEncoding("GBK");
            Properties pro = System.getProperties();
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.ssl.enable", "true");
            pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            mailSender.setJavaMailProperties(pro);
            //建立邮件消息
            //设置收件人、寄件人
            messageHelper.setTo(address);
            messageHelper.setFrom(userName, from);
            messageHelper.setSubject(title);
            messageHelper.setText(body, true);
            //附件内容
            if (fileName != null && file != null) {
                if (fileName.length == file.length) {
                    for(int i = 0; i < fileName.length; i++) {
                        messageHelper.addAttachment(fileName[i], file[i]);
                    }
                } else {
                    throw new Exception("附件和附件名称不匹配~！");
                }
            }

            //发送邮件
            mailSender.send(mailMessage);
            insertSendMailLog(from, title, body, true, address, names);
            LOGGER.info("邮件发送成功！");
    }

    /**
     * 保存邮件日志
     *
     * @param fromAddress 发件人地址
     * @param title       邮件标题
     * @param body        内容
     * @param isFile      是否包含附件：Boolean类型
     * @param address     收件人地址（数组）
     * @param names       收件人名称（数组）
     */
    public void insertSendMailLog (String fromAddress, String title, String body, Boolean isFile, String[] address, String[] names) {

        PmMailSendLog log = new PmMailSendLog();
        log.setId(UUID.randomUUID().toString());
        log.setAddresser(fromAddress);
        log.setTitle(title);
        log.setBody(body);
        log.setIsFile(isFile ? "1" : "0");
        log.setRecipientsIds(StringUtil.linkArrayToString(address));
        log.setRecipientsNames(StringUtil.linkArrayToString(names));
        log.setRecipientsGroup(group);

        log.setOwner("admin");
        log.setLastUpdateTime(new Date());
        log.setCreateTime(new Date());
        pmMailSendLogService.insert(log);

    }

    /**
     * 发送邮件到MQ
     *
     * @param title   标题
     * @param from    发件人地址
     * @param address 收件人地址
     * @param names   收件人名称
     * @param body    内容
     * @param isFile  是否包含附件
     */
    public void sendMailToMQ (String serverId, String title, String from, String address, String names, String body, Boolean isFile) {
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("serverId", serverId);
        parMap.put("title", title);
        parMap.put("from", from);
        parMap.put("address", address);
        parMap.put("names", names);
        parMap.put("body", body);
        parMap.put("isFile", isFile);
        String obj = JSON.toJSON(parMap).toString();
        LOGGER.info("发送给MQ的报文为：" + obj);
        etlSendService.send(rabbitmqName + "_toemailKey", obj);
    }

    /**
     * 发送邮件到MQ(带Excel附件)
     *
     * @param title    标题
     * @param from     发件人地址
     * @param address  收件人地址
     * @param names    收件人名称
     * @param body     内容
     * @param isFile   是否包含附件
     * @param newList
     * @param nameList
     * @param map
     */
    public void sendMailToMQWithExcel (String serverId, String title, String from, String address, String names, String body, Boolean isFile, String dirPath, String filePath, List<Map<String, Object>> newList, List<String> nameList, Map<String, String> map) {
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("serverId", serverId);
        parMap.put("title", title);
        parMap.put("from", from);
        parMap.put("address", address);
        parMap.put("names", names);
        parMap.put("body", body);
        parMap.put("isFile", isFile);
        parMap.put("fileName", "抽取日报【" + CommonUtils.getCurDateString() + "】.xls");
        parMap.put("dirPath", dirPath);
        parMap.put("filePath", filePath);
        parMap.put("newList", newList);
        parMap.put("nameList", nameList);
        parMap.put("map", map);
        String obj = JSON.toJSON(parMap).toString();
        LOGGER.info("发送给MQ的报文为：" + obj);
        etlSendService.send(rabbitmqName + "_toemailKey", obj);
    }

}
