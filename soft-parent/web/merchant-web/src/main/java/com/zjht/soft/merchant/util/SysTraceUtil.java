package com.zjht.soft.merchant.util;

import java.util.UUID;

/**
 * Created by wuqiyang on 2017/9/21。
 */
public class SysTraceUtil {
    /**
     * 获取12位的唯一流水。
     *
     * @return 流水号
     */
    public static String getShortUuid() {
        System.out.println();
        String s = UUID.randomUUID().toString();
        s = s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) +
            s.substring(24);
        return s.substring(0, 12);
    }

    public static String getSystrace(){

        int systrace = (int)((Math.random()*9+1)*100000);

        return String.valueOf(systrace);
    }
}
