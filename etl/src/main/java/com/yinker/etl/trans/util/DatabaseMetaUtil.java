package com.yinker.etl.trans.util;

import com.yinker.etl.pm.service.PmDataSourceService;
import org.pentaho.di.core.database.DatabaseMeta;

import java.util.List;

/**
 * Created by 崔博文 on 2017/11/9.20:21
 */
public class DatabaseMetaUtil {

    private static DatabaseMetaUtil instance;
    public List<DatabaseMeta> databaseMetaList = null;

    private DatabaseMetaUtil () {
    }

    public static DatabaseMetaUtil getInstance () {
        if (instance == null) {
            instance = new DatabaseMetaUtil();
        }
        return instance;
    }
    public List<DatabaseMeta> getDatabaseMetaListInstance (PmDataSourceService pmDataSourceService) throws Exception {
        if (databaseMetaList == null) {
            databaseMetaList = KettleDataSorceInit.initDataSource();
        }
        return databaseMetaList;
    }
}
