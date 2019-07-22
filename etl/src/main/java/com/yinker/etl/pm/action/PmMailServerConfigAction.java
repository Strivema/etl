package com.yinker.etl.pm.action;

import com.alibaba.fastjson.JSONObject;
import com.yinker.etl.pm.model.PmMailTemplateQuery;
import com.yinker.etl.pm.model.base.PmMailTemplate;
import com.yinker.etl.pm.service.PmMailTemplateService;
import com.yinker.etl.sys.service.MailSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwork.common.utils.StringUtils;
import org.zwork.framework.base.support.BaseStruts2Action;
import org.zwork.common.key.KeyGeneratorContainer;
import org.zwork.srdp.pm.service.PmDataDictItemService;
import com.yinker.etl.pm.model.PmMailServerConfigQuery;
import com.yinker.etl.pm.model.base.PmMailServerConfig;
import com.yinker.etl.pm.model.base.PmMailServerConfigPK;
import com.yinker.etl.pm.service.PmMailServerConfigService;
import org.zwork.srdp.rbac.RbacConstants;
import org.zwork.srdp.rbac.session.User;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * <b>类描述:</b>PmMailServerConfig表对应的Action类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-03-06 10:15:28
 * </pre>
 */
public class PmMailServerConfigAction extends BaseStruts2Action {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmMailServerConfigAction.class);

    /* 定义业务实体对象，用于新增/修改/查看 */
    protected PmMailServerConfig entity;

    /* 定义查询类业务实体对象，用于查询条件对象 */
    protected PmMailServerConfigQuery queryEntity;

    /* 定义服务层操作类 */
    protected PmMailServerConfigService entityService;
	
	/* 数据字典服务类 */
    protected PmDataDictItemService pmDataDictItemService;

    @Resource(name = "pmMailTemplateService")
    private PmMailTemplateService pmMailTemplateService;

    @Resource(name = "mailSendService")
    private MailSendService mailSendService;
	
	/* 序列键容器 */
	private KeyGeneratorContainer keyGeneratorContainer;

	private String sendAddress;

    /*@Value("%{AESKey}")
    private String AESKey;*/
	
	/**
	 *	执行初始化方法
	 */
	public void init(){
		entity = new PmMailServerConfig();
		queryEntity = new PmMailServerConfigQuery();
	}
    public PmMailServerConfigAction() {
    }

    /**
     * 新增及新增保存页面
     * 
     * @return
     * @throws Exception
     */
    public String create() throws Exception {
        if ("toCreate".equals(operate)) {// 转向新增页面
            return CREATE;
        } else if ("save".equals(operate)) {// 新增保存
			// 设置Id
			String id = keyGeneratorContainer.getNextKey("pmMailServerConfigId");
            entity.setId(id);

            /*String psw = AESUtil.encrypt(AESKey, entity.getPassword()); //对数据库密码进行AES加密
            LOGGER.debug("对数据库密码进行AES加密并更新数据库：{}", psw);
            entity.setPassword(psw);*/

            Date now = new Date();
            entity.setCreateTime(now);
            entity.setLastUpdateTime(now);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOwner(user.getName());
            LOGGER.debug("新增数据，新增前对象信息：{}", entity);
            entityService.insert(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("新增数据成功，转向修改页面");
            return EDIT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }

    /**
     * 修改及修改保存页面
     * 
     * @return
     * @throws Exception
     */
    public String edit() throws Exception {
        if ("toEdit".equals(operate)) {// 转向修改页面
            // 从数据库中查询数据
            LOGGER.debug("准备更新对象,主键信息：{}", entity.pkString());
            entity = entityService.selectByPK(entity);
            LOGGER.debug("更新前数据库中对象信息[{}]" + entity);
            return EDIT;
        } else if ("save".equals(operate)) {// 修改保存
            // 更新数据至数据库
            /*String psw = AESUtil.encrypt(AESKey, entity.getPassword()); //对数据库密码进行AES加密
            LOGGER.debug("对数据库密码进行AES加密并更新数据库：{}", psw);
            entity.setPassword(psw);*/
            Date now = new Date();
            entity.setLastUpdateTime(now);
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            entity.setOwner(user.getName());
            LOGGER.debug("更新数据：{}", entity);
            entityService.updateByPK(entity);
            setInfoMessage("操作成功!");
            LOGGER.debug("更新数据成功!");
            return PROMPT;
        } else {
            throw new Exception("无效operate参数值[" + operate + "]");
        }
    }
    /**
     * 根据主键删除数据记录
     * 
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {
		String selectedIds = request.getParameter("selectedIds");
		if (StringUtils.isNotEmpty(selectedIds)) {
			String[] idArray = selectedIds.split("\\|");
			for (int i = 0; i < idArray.length; i++) {
                PmMailTemplateQuery pmMailTemplateQuery = new PmMailTemplateQuery();
                pmMailTemplateQuery.setServerId(idArray[i]);
                List<PmMailTemplate> pmtList = pmMailTemplateService.selectByEntity(pmMailTemplateQuery);

                if(pmtList != null && pmtList.size() > 0){
                    setWarnMessage("有邮件发送模版正在使用此发送服务器，不可删除！");
                }else {
                    PmMailServerConfigPK pk = new PmMailServerConfigPK();
                    pk.setId(idArray[i]);
                    LOGGER.debug("删除数据:{}", pk.pkString());
                    entityService.deleteByPK(pk);
                }
            }
		}
        return PROMPT;
    }

    /**
     * 根据主键查询数据库，成功后转向查看页面
     * 
     * @return
     * @throws Exception
     */
    public String view() throws Exception {
        // 从数据库中查询数据
        LOGGER.debug("根据主键查询数据，主键信息:{}", entity.pkString());
		PmMailServerConfig pmMailServerConfig =  entityService.selectByPK(entity);
        if (entity == null) {
           throw new Exception("对象不存在,查询条件:" + entity.pkString());
        } else {
			entity = pmMailServerConfig;
			entity.setPassword("******");
		}
        LOGGER.debug("查询结果:{}", entity);
        return VIEW;
    }

    public String toList(){
        return LIST;
    }
    /**
     * 查询列表页面
     * 
     * @return
     * @throws Exception
     */
    public String list() throws Exception {
        if ("query".equals(operate)) {// 执行数据库查询操作
            LOGGER.debug("开始执行查询操作，查询条件:{}", queryEntity);
            page = entityService.pageQuery(queryEntity);
            if (page.getCount() <= 0) {
                setWarnMessage("未找到匹配的数据记录,请更换查询条件后再试!");
            }
            LOGGER.debug("查询结果:[count={},page data size={}]", page.getCount(), page.getData().size());
            return "list-result";
        } else { //转向查询页面，但不执行数据库查询操作
            return "list-result";
        }
    }

    /**
     * 测试服务器邮件发送功能
     *
     * @throws Exception
     */
    public void sendTest (){
        Map<String, String> dataMap = new HashMap<String, String>(2);
        try {
            User user = (User) session.getAttribute(RbacConstants.SESSION_KEY_USER);
            mailSendService.sendMailWithFile(queryEntity.getHost(),queryEntity.getPort(),queryEntity.getUserName(),queryEntity.getPassword(),"测试发送邮件","ETL系统管理员",new String[]{sendAddress},new String[]{user.getName()},"发送服务器邮件发送测试",null,null);

            dataMap.put("flag","1");
            dataMap.put("infos","测试邮件发送，请注意查收");
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("测试邮件发送遇到异常：{}",e.getMessage());
            dataMap.put("flag","0");
            dataMap.put("infos","测试邮件发送失败：" + e.getMessage());
        }
        toJosnLink(dataMap);
    }

    /**
     * 将Map转化为JSON格式
     *
     * @throws Exception
     */
    public void toJosnLink (Map object) {
        JSONObject t = new JSONObject(true);
        t.put("link", object);
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(t.toString());
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public PmMailServerConfig getEntity() {
        return entity;
    }

    public void setEntity(PmMailServerConfig entity) {
        this.entity = entity;
    }

    public PmMailServerConfigQuery getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(PmMailServerConfigQuery queryEntity) {
        this.queryEntity = queryEntity;
    }

    public void setEntityService(PmMailServerConfigService entityService) {
        this.entityService = entityService;
    }

    public void setPmDataDictItemService(PmDataDictItemService pmDataDictItemService) {
        this.pmDataDictItemService = pmDataDictItemService;
    }
	
	public void setKeyGeneratorContainer(KeyGeneratorContainer keyGeneratorContainer) {
        this.keyGeneratorContainer = keyGeneratorContainer;
    }
}
