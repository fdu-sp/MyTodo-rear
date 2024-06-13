package com.zmark.mytodo.dto.timer;

import com.zmark.mytodo.bo.timer.resp.TimerWeekAnalysisResp;
import com.zmark.mytodo.bo.timer.resp.inner.TimerWeekResp;
import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Violette
 * @date 2024/5/6 23:09
 * @description 当天专注时长
 * @see Timer 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerDayDTO {
    private Date day; // 日期
    private Long focusTime; // 当天专注时长，单位为分钟

    public static List<TimerDayDTO> from(Timer timer) {
        List<TimerDayDTO> timerDayDTOList = new ArrayList<>();

        Timestamp startTime = timer.getStartTimestamp();
        Timestamp endTime = timer.getEndTimestamp();

        Timestamp currTime = startTime;
        while (currTime.before(endTime)) {
            if (TimeUtils.isSameDay(currTime, endTime)) {
                // 计时器在同一天内开始和结束
                timerDayDTOList.add(
                        TimerDayDTO.builder()
                                .day(TimeUtils.getStartOfDay(currTime))  // 忽略小时、分、秒信息
                                .focusTime(TimeUtils.minutesDiff(currTime, endTime))
                                .build()
                );
                break; // 达到末尾
            } else {
                timerDayDTOList.add(
                        TimerDayDTO.builder()
                                .day(TimeUtils.getStartOfDay(currTime))
                                .focusTime(TimeUtils.minutesUntilMidnight(currTime)) // 当前时间到今晚24点的分钟数
                                .build()
                );
                System.out.println("to end time = " + TimeUtils.minutesUntilMidnight(currTime));
                // 将当前时间更新为第二天0点
                currTime = Timestamp.valueOf(currTime.toLocalDateTime().toLocalDate().plusDays(1).atStartOfDay());
                System.out.println("new currTime = " + currTime);
            }
        }

        return timerDayDTOList;
    }

    public static TimerWeekAnalysisResp toWeekAnalysisResp(List<TimerDayDTO> timerDayDTOList) {
        // 使用Map来存储每一天的累计专注时间
        Map<String, Long> dailyFocusTimeMap = new HashMap<>();

        for (TimerDayDTO dto : timerDayDTOList) {
            String day = TimeUtils.toString(dto.getDay());
            Long focusTime = dto.getFocusTime();

            // 累加同一天的专注时间
            dailyFocusTimeMap.merge(day, focusTime, Long::sum);
        }

        // 将Map转换为List
        List<TimerWeekResp> focusTimeList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : dailyFocusTimeMap.entrySet()) {
            focusTimeList.add(
                    TimerWeekResp.builder()
                            .day(entry.getKey())
                            .focusTime(entry.getValue())
                            .build()
            );
        }

        return TimerWeekAnalysisResp.builder().focusTimeEveryDay(focusTimeList).build();
    }
}
