package com.zmark.mytodo.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    /**
     * 获取hour小时后的时间戳
     */
    public static Timestamp addHour(Timestamp timestamp, Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取当天0点的日期
     */
    public static Date getStartOfDay(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取当周第一天0点的时间戳
     */
    public static Timestamp getStartOfWeek(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取当月第一天0点的时间戳
     */
    public static Timestamp getStartOfMonth(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 判断两个时间戳是否在同一天
     */
    public static Boolean isSameDay(Timestamp timestamp1, Timestamp timestamp2) {
        LocalDate date1 = timestamp1.toLocalDateTime().toLocalDate();
        LocalDate date2 = timestamp2.toLocalDateTime().toLocalDate();
        return date1.equals(date2);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        LocalDate localDate1 = date1.toLocalDate();
        LocalDate localDate2 = date2.toLocalDate();
        return localDate1.equals(localDate2);
    }

    /**
     * 获取两个时间戳相差的分钟数
     */
    public static Long minutesDiff(Timestamp timestamp1, Timestamp timestamp2) {
        long diffInMillis = Math.abs(timestamp1.getTime() - timestamp2.getTime());
        return TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取当前时间戳距离当天24点的分钟数
     */
    public static long minutesUntilMidnight(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        LocalDateTime midnight = localDateTime.toLocalDate().plusDays(1).atStartOfDay();
        return ChronoUnit.MINUTES.between(localDateTime, midnight);
    }
}
