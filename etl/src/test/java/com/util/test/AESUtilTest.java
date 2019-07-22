package com.util.test;

import com.yinker.etl.pm.util.AESUtil;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by 崔博文 on 2017/11/8.14:56
 */
public class AESUtilTest {

    @Test
    public void jiemi() throws Exception {
        System.out.println(AESUtil.decrypt("5153515351535153","0IHJ7SWKmX8qFbTZQditLEYyACbachkmTZREyl1RdHI=")+"----------");
    }

    @Test
    public void testArray(){
        String[] arr = new String[]{"123","456"};
        System.out.println(Arrays.binarySearch(arr, "45612"));
    }

}
