package com.graduation.common.date;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils{

    //日期格式
    private static String[] parsePatterns = {
        "yyyy-MM-dd",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM",
        "yyyy/MM/dd",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy/MM/dd HH:mm",
        "yyyy/MM",
        "yyyy.MM.dd",
        "yyyy.MM.dd HH:mm:ss",
        "yyyy.MM.dd HH:mm",
        "yyyy.MM"
    };

    //得到当前日期 格式yyyy-MM-dd
    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    //得到当前日期 格式yyyy-MM-dd pattern可以为"yyyy-MM-dd" "HH:mm:ss" "E"
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    //得到当前日期 类型如上
    public static String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return null;
        }
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    //转换当前日期 格式yyyy_MM-dd HH:mm:ss
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    //得到当前时间 格式HH:mm:ss
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    //得到当前日期 格式yyyy-MM-dd HH:mm:ss
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    //得到当前年份 yyyy
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    //得到当前月份 MM
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    //得到当前天数 dd
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    //得到当前星期几
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    //获取过去的天数 Date为之前的日期
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    //获取当前时间的字符串
    public static String toStr() {
        return toStr(new Date());
    }

    //转换时间字符串类型yyyy-MM-dd HH:mm:ss
    public static String toStr(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    //将Date转换为时间字符串
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    //获取过去的小时
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    //获取过去的分钟
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    //将毫秒转换为时间
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    //获取日期之间相差的年数
    public static int getMonthsBetweenDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return Math.abs(yearInterval * 12 + monthInterval);
    }


    //获取日期间的天数
    public static double getDaysBetweenDate(Date before, Date after) {
        return (double)getMillisecBetweenDate(before,after) / (1000 * 60 * 60 * 24);
    }

    //获取日期间的毫秒数
    public static long getMillisecBetweenDate(Date before, Date after){
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return afterTime - beforeTime;
    }

    //获取日期之间的年数
    public static int getYearsBetweenDate(Date before, Date after){
        Integer beforeYear = Integer.valueOf(format(before, "yyyy"));
        Integer afterYear = Integer.valueOf(format(after, "yyyy"));
        return afterYear - beforeYear;
    }

    //获取当月的第一天
    public static String getFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return format.format(c.getTime());
    }

    //获取当前日期时间戳
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    //返回时间的double
    public static double getDoubleType(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateString);
        return date.getTime();
    }


    //获取两个Date之间的秒数
    public static int calLastedTime(Date startTime, Date endTime) {
        long a = startTime.getTime();
        long b = endTime.getTime();
        return (int) ((b - a) / 1000);
    }

    //获取若干个小时后的时间
    public static String getPassHours(String startTime, int hours) {
        // 获取一个小时以后的时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = df.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
            return df.format(calendar.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    //"yyyy-MM-dd HH:mm:ss"格式的日期在若干天数后的时间
    public static String getAddDate(String time,int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        } // 指定日期
        Date newDate = null;
        try {
            newDate = addDate(date, days);
        } catch (ParseException e) {
            e.printStackTrace();
        } // 指定日期加上20天
        String st = dateFormat.format(newDate);
        return st;
    }

    //指定时间在若干天后的时间
    public static Date addDate(Date date,long day) throws ParseException {
        // 得到指定日期的毫秒数
        long time = date.getTime();
        // 要加上的天数转换成毫秒数
        day = day*24*60*60*1000;
        // 相加得到新的毫秒数
        time += day;
        // 将毫秒数转换成日期
        return new Date(time);
    }

    //计算新的date，原理同上
    public static Date calculate(Date date, long number, TimeUnit timeUnit){
        long time = date.getTime(); // 得到指定日期的毫秒数
        number = timeUnit.toMillis(number);
        time += number;
        return new Date(time);
    }

    //获取当天的某一时刻Date
    public static Date getMoment(int hour, int min, int sec, int mill){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,sec);
        calendar.set(Calendar.MILLISECOND,mill);
        return calendar.getTime();
    }

    //获得指定某年某月某日某刻的Date
    public static Date getMoment(int year,int month,int day,int hour,int min,int sec,int mill){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,sec);
        calendar.set(Calendar.MILLISECOND,mill);
        return calendar.getTime();
    }

    //得到系统时间到第二天凌晨的时间差
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        // 改成这样就好了
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
