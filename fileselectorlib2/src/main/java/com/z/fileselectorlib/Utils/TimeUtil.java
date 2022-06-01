package com.z.fileselectorlib.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String getTimeInString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }
    public static String getDateInString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);
    }
    public static String getCurrentTimeInString() {
        return getTimeInString(getCurrentTimeInDate());
    }
    public static String getCurrentDateInString(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(getCurrentTimeInDate());
    }
    public static Date getCurrentTimeInDate() {
        return new Date(System.currentTimeMillis());
    }
    /**
     * 两个时间间的时间戳计算函数
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @param f  时间差的形式0:秒,1:分种,2:小时,3:天
     * @return  long 秒
     */
    public static long getDifference(Date beginDate, Date endDate, int f) {
        long result = 0;
        if (beginDate == null || endDate == null) {
            return 0;
        }
        try {
            // 日期相减获取日期差X(单位:毫秒)
            long millisecond = endDate.getTime() - beginDate.getTime();
            switch (f) {
                case 0: // second
                    return  (millisecond / 1000);
                case 1: // minute
                    return (millisecond / (1000 * 60));
                case 2: // hour
                    return  (millisecond / (1000 * 60 * 60));
                case 3: // day
                    return (millisecond / (1000 * 60 * 60 * 24));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
