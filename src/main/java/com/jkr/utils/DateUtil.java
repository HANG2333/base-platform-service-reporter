package com.jkr.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateUtil<main> {

    /**
    * method_name:differentDaysByDate
    * create_user: DaiFuyou
    * create_date:2020/9/3
    * create_time:10:53
    * describe: 两个日期相差的天数
    * param:[date1, date2]
    * return:int
    */
    public static int differentDaysByDate(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 描述 获取当前日期 格式：(yyyy-MM-dd hh:mm:ss)
     *
     * @param
     * @return String
     * @author ***
     * @date 2018/11/26 10:33
     */
    public static String getCurrentFullTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 描述 获取当前日期 格式：(yyyy-MM-dd)
     *
     * @param
     * @return String
     * @author ***
     * @date 2018/11/26 10:33
     */
    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return String.valueOf(currentDate);
    }

    /**
     * 描述 获取今天星期几
     *
     * @param
     * @return String
     * @author ***
     * @date 2018/11/26 10:53
     */
    public static String getDayOfTheWeek() {
        String[][] strArray = {{"MONDAY", "一"}, {"TUESDAY", "二"}, {"WEDNESDAY", "三"}, {"THURSDAY", "四"}, {"FRIDAY", "五"}, {"SATURDAY", "六"}, {"SUNDAY", "日"}};

        LocalDate currentDate = LocalDate.now();
        String k = String.valueOf(currentDate.getDayOfWeek());
        //获取行数
        for (int i = 0; i < strArray.length; i++) {
            if (k.equals(strArray[i][0])) {
                k = strArray[i][1];
                break;
            }
        }
        return "星期" + k;
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds   精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || "null".equals(seconds)) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date   字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /**
    * method_name:addOneday
    * create_user: DaiFuyou
    * create_date:2020/9/3
    * create_time:10:54
    * describe: 日期加一天
    * param:[today]
    * return:java.lang.String
    */
    public static String addOneday(String today) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = new Date(f.parse(today).getTime() + 24 * 3600 * 1000);
            return f.format(d);
        } catch (Exception ex) {
            return "输入格式错误";
        }
    }

    /**
    * method_name:addDays
    * create_user: DaiFuyou
    * create_date:2020/9/3
    * create_time:10:54
    * describe: 日期计算
    * param:[today, i]
    * return:java.lang.String
    */
    public static String addDays(String today, int i) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = new Date(f.parse(today).getTime() + i * 24 * 3600 * 1000);
            return f.format(d);
        } catch (Exception ex) {
            return "输入格式错误";
        }
    }

    public static String getCurrentDateStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }
    public static String getCurrentYear() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
    }
    public static String getCurrentMonth() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String getLastDayByDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return String.valueOf(date.with(TemporalAdjusters.lastDayOfMonth()));
    }

}
