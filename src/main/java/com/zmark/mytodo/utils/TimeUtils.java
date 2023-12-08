package com.zmark.mytodo.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author ZMark
 * @date 2023/12/4 17:48
 */
public class TimeUtils {
    /**
     * 返回Timestamp的字符串形式，如果timestamp为null，则返回null
     */
    public static String toString(Timestamp timestamp) {
        return Optional.ofNullable(timestamp).map(Timestamp::toString).orElse(null);
    }

    /**
     * 返回Date的字符串形式，如果date为null，则返回null
     */
    public static String toString(Date date) {
        return Optional.ofNullable(date).map(Date::toString).orElse(null);
    }

    /**
     * 转化为Timestamp，如果date或者time为null，则返回null
     *
     * @param date 日期
     * @param time 时间
     * @return Timestamp
     */
    public static Timestamp combineDateAndTime(Date date, Time time) {
        return Optional.ofNullable(date).map(date1 -> {
            if (null == time) {
                return null;
            }
            return Timestamp.valueOf(date1 + " " + time);
        }).orElse(null);
    }

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
