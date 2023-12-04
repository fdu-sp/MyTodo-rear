package com.zmark.mytodo.utils;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author ZMark
 * @date 2023/12/4 17:48
 */
public class TimeUtils {
    public static String toString(Timestamp timestamp) {
        return Optional.ofNullable(timestamp).map(Timestamp::toString).orElse(null);
    }
}
