package com.zmark.mytodo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author ZMark
 * @date 2024/4/3 16:13
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class ExperimentalTest {

    @Test
    public void test_TimeToDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());
        long milliseconds = timestamp.getTime();
        Date convertedDate = new Date(milliseconds);
        log.info("Timestamp: {}", timestamp);
        log.info("Date: {}", date);
        log.info("Converted Date: {}", convertedDate);
        Assertions.assertEquals(date, convertedDate);
    }
}
