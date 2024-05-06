package com.zmark.mytodo.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author ZMark
 * @date 2023/12/4 17:48
 */
@Slf4j
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

    public static String toString(Time time) {
        return Optional.ofNullable(time).map(Time::toString).orElse(null);
    }

    public static Date toDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return Optional.of(date).map(Date::valueOf).orElse(null);
    }

    public static Time toTime(String time) {
        if (time == null || time.isEmpty()) {
            return null;
        }
        return Optional.of(time)
                .map(s -> {
                    try {
                        return new Time(new SimpleDateFormat("HH:mm:ss").parse(s + ":00").getTime());
                    } catch (ParseException e) {
                        log.error("时间转换失败", e);
                        return null;
                    }
                })
                .orElse(null);
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

    /**
     * 获取dayNum天前的时间戳
     */
    public static Timestamp before(int dayNum) {
        return new Timestamp(System.currentTimeMillis() - (long) dayNum * 24 * 60 * 60 * 1000);
    }

    public static Date today() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取dayNum天后的日期
     */
    public static Date afterDays(int dayNum) {
        return new Date(System.currentTimeMillis() + (long) dayNum * 24 * 60 * 60 * 1000);
    }

    /**
     * 解析Timestamp字符串，如果timestampStr为null，则返回null
     */
    public static Timestamp toTimestamp(String timestampStr) {
        if (timestampStr == null || timestampStr.isEmpty()) {
            return null;
        }
        return Optional.of(timestampStr).map(Timestamp::valueOf).orElse(null);
    }

    /**
     * 判断timestampAfter是否在timestampBefore之后
     */
    public static boolean isAfter(Timestamp timestampBefore, Timestamp timestampAfter) {
        return timestampAfter != null && timestampBefore != null && timestampAfter.after(timestampBefore);
    }

    public static Timestamp addHour(Timestamp timestamp, Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return new Timestamp(calendar.getTimeInMillis());
    }
}
