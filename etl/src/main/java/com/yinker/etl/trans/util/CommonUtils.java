package com.yinker.etl.trans.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    public static String[] repeatString(int length, String string) {
        String[] str = new String[length];
        for (int i = 0; i < length; i++) {
            str[i] = string;
        }
        return str;
    }

    public static Boolean[] repeatBooleanObj(int length, boolean flag) {
        Boolean[] bo = new Boolean[length];
        for (int i = 0; i < length; i++) {
            bo[i] = flag;
        }
        return bo;
    }
    
    public static boolean[] repeatBoolean(int length, boolean flag) {
        boolean[] bo = new boolean[length];
        for (int i = 0; i < length; i++) {
            bo[i] = flag;
        }
        return bo;
    }

    public static String getNowDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getCurDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    public static int[] repeatInt(int length, int value) {
        int[] in = new int[length];
        for (int i = 0; i < length; i++) {
            in[i] = value;
        }
        return in;
    }
}
