package com.zjht.soft.merchant.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * 订单号生成工具类。
 *
 * @author lijiguang 2017年5月2日下午3:11:22
 * @version 1.0.0
 */
public class OrderUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        "yyyyMMddHHmmssSSS");
    /**
     * 随机数位数。
     */
    private static final int              index            = 5;

    /**
     * 自增数。
     */
    private static int counter = 0;

    /**
     * 生成一个新的订单号。
     *
     * @return 订单号
     */
    public static String getOrderId() {
        StringBuilder sb = new StringBuilder();
        //年月日时分秒毫秒(17位)
        String dateTime = ((SimpleDateFormat) simpleDateFormat.clone()).format(
            Calendar.getInstance().getTime());
        //自增数(2位)
        String seq = getSequence();
        //随机数(5位)
        String random = getRandom();
        //生成一个24位订单号
        sb.append(dateTime).append(seq).append(random);
        return sb.toString();
    }

    /**
     * 2位自增数。
     * 不足的前面补0
     *
     * @return 2位字符串
     */
    private static synchronized String getSequence() {
        if (counter > 99) {
            counter = 0;
        }
        counter += 1;
        return String.format("%02d", counter);
    }

    /**
     * 5位随机数。
     *
     * @return 5位随机数
     */
    private static String getRandom() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < index; i++) {
            sb.append(String.format("%01d", random.nextInt(10)));
        }
        return sb.toString();
    }
}