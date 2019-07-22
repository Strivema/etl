package com.yinker.etl.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by 崔博文 on 2018/2/5.14:18
 */
public class StringUtil {

    /**
     * 将传入数组按照逗号连接
     * @param array
     * @return
     */
    public static String linkArrayToString(String[] array){
        StringBuffer sb = new StringBuffer();
        if(array.length>0){
            sb.append(array[0]);
        }
        for(int i = 1; i < array.length; i++) {
            sb.append(","+array[i]);
        }
        return sb.toString();
    }

    /**
     * 替换占位符的String
     * @param beforeStr
     * @param parameter
     * @return
     */
    public static String parseString(String beforeStr, Map<String,String> parameter){
        if(parameter != null){
            Iterator<Map.Entry<String, String>> it = parameter.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                beforeStr = beforeStr.replaceAll("\\{" + key + "\\}", value);
            }
        }
        return beforeStr;
    }

}
