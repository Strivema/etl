package com.yinker.etl.key;

import org.zwork.common.utils.DateUtils;
import org.zwork.srdp.pm.PMManager;

public class KeyGeneratorContext {

    protected String currentDate;

    public String getCurrentDate() {
        return DateUtils.dateToString(PMManager.getCurrentWorkDate());
    }
}
