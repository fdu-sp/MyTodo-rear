package com.zmark.mytodo.dto.timer;

import com.zmark.mytodo.bo.timer.resp.TimerMonthAnalysisResp;
import com.zmark.mytodo.entity.Timer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Violette
 * @date 2024/5/7 2:39
 * @description 当月某清单专注时长
 * @see Timer 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerMonthDTO {
    private Long taskListId;
    private Long focusTime;

    // 暂时不考虑跨月的计时器
    //    public static TimerMonthDTO from(Timer timer) {
    //
    //    }

    public static TimerMonthAnalysisResp toMonthAnalysisResp(List<TimerMonthDTO> timerMonthDTOList) {
        return TimerMonthAnalysisResp.builder()
                .focusTimeEveryTaskList(
                        timerMonthDTOList.stream()
                                .collect(Collectors.groupingBy(
                                        TimerMonthDTO::getTaskListId,
                                        Collectors.summingLong(TimerMonthDTO::getFocusTime)
                                ))
                )
                .build();
    }
}
