package com.zmark.mytodo.dto.timer;

import com.zmark.mytodo.bo.timer.resp.TimerMonthAnalysisResp;
import com.zmark.mytodo.bo.timer.resp.inner.TimerMonthResp;
import com.zmark.mytodo.entity.Timer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        // 使用Map来存储每个任务列表的累计专注时间
        Map<Long, Long> taskListFocusTimeMap = new HashMap<>();

        for (TimerMonthDTO dto : timerMonthDTOList) {
            Long taskListId = dto.getTaskListId();
            Long focusTime = dto.getFocusTime();

            // 累加同一个任务列表的专注时间
            taskListFocusTimeMap.merge(taskListId, focusTime, Long::sum);
        }

        // 将Map转换为List
        List<TimerMonthResp> focusTimeEveryTaskList = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : taskListFocusTimeMap.entrySet()) {
            focusTimeEveryTaskList.add(
                    TimerMonthResp.builder()
                            .taskListId(entry.getKey())
                            .focusTime(entry.getValue())
                            .build()
            );
        }

        return TimerMonthAnalysisResp.builder()
                .focusTimeEveryTaskList(focusTimeEveryTaskList)
                .build();
    }
}
