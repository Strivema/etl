package com.yinker.etl.pm.service;

import com.yinker.etl.pm.dao.PmMailTemplateDao;
import com.yinker.etl.pm.model.PmMailTemplateQuery;
import com.yinker.etl.pm.model.base.PmMailTemplate;
import com.yinker.etl.pm.model.base.PmMailTemplatePK;
import org.zwork.framework.base.support.BaseService;

import java.util.List;

/**
 * <pre>
 * <b>类描述:</b>PmMailTemplate表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2018-01-29 16:59:01
 * </pre>
 */
public class PmMailTemplateService extends BaseService<PmMailTemplatePK, PmMailTemplate, PmMailTemplateDao> implements
        org.zwork.framework.base.BaseService<PmMailTemplatePK, PmMailTemplate> {

    /**
     * 通过Code获取对象
     * @param code
     * @return
     */
    public PmMailTemplate getWarnConfigByCode (String code) {
        PmMailTemplateQuery query = new PmMailTemplateQuery();
        query.setCode(code);
        List<PmMailTemplate> templates = selectByEntity(query);
        return templates != null && templates.size() > 0 ? templates.get(0) : null;
    }
}
