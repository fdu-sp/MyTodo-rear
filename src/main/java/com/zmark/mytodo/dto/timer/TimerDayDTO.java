package com.zmark.mytodo.dto.timer;

import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

//        Long timeDiffInMinutes = TimeUtils.minutesDiff(startTime, endTime);
//        System.out.println("timeDiffInMinutes = " + timeDiffInMinutes.toString());

        Timestamp currTime = startTime;
        while (currTime.before(endTime)) {
            if (TimeUtils.isSameDay(currTime, endTime)) {
                // 计时器在同一天内开始和结束
                timerDayDTOList.add(
                        TimerDayDTO.builder()
                                .day(currTime)
                                .focusTime(TimeUtils.minutesDiff(currTime, endTime))
                                .build()
                );
                break; // 达到末尾
            } else {
                timerDayDTOList.add(
                        TimerDayDTO.builder()
                                .day(currTime)
                                .focusTime(TimeUtils.minutesUntilMidnight(currTime)) // 当前时间到今晚24点的分钟数
                                .build()
                );
                // 将当前时间更新为第二天0点
                currTime = Timestamp.valueOf(currTime.toLocalDateTime().toLocalDate().plusDays(1).atStartOfDay());
            }
        }

        return timerDayDTOList;
    }
}
