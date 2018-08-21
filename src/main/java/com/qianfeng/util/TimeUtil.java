package com.qianfeng.util;


import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局时间工具类
 */
public class TimeUtil {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    /**
     * 判断时间是否有效,用正则表达式
     *
     * @param date
     * @return
     */
    public static boolean isValidateDate(String date) {
        Matcher matcher = null;
        Boolean res = false;
        String regexp = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
        if (StringUtils.isNotEmpty(date)) {
            Pattern pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(date);
        }
        if (matcher != null) {
            res = matcher.matches();
        }
        return res;
    }

    /**
     * 默认获取昨天的日期 yyyy-MM-dd
     *
     * @return
     */
    public static String getYesterday() {
        return getYesterday(DEFAULT_FORMAT);
    }

    /**
     * 获取指定格式的昨天的日期
     *
     * @param pattern
     * @return
     */
    public static String getYesterday(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return sdf.format(calendar.getTime());
    }

    //将时间戳转换成默认格式的日期
    public static String parseLong2String(long time) {
        return parseLong2String(time, DEFAULT_FORMAT);
    }

    /**
     * 将时间戳转换成指定格式的日期
     *
     * @param time
     * @param parrent
     * @return
     */
    public static String parseLong2String(long time, String parrent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return new SimpleDateFormat(parrent).format(calendar.getTime());
    }

    /**
     * 将默认的日期格式转换成时间戳
     *
     * @param
     * @return
     */
    public static long parseString2Long(String date) {
        return parseString2Long(date, DEFAULT_FORMAT);
    }

    public static long parseString2Long(String date, String parrent) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat(parrent).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt.getTime();
    }

    public static void main(String[] args) {
        System.out.println(TimeUtil.isValidateDate("2018-08-17"));
        System.out.println(TimeUtil.getYesterday());
        System.out.println(TimeUtil.parseString2Long("2018-08-17"));
        System.out.println(TimeUtil.parseLong2String(1534435200000L));
    }
}
