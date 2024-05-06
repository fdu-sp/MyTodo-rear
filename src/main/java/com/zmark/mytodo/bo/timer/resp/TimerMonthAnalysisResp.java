package com.zmark.mytodo.bo.timer.resp;

import com.zmark.mytodo.entity.Timer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Violette
 * @date 2024/5/7 2:22
 * @description 统计本月每个清单的专注时长
 * @see Timer 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerMonthAnalysisResp {
    // 清单id : 该清单当月总专注时长
    private Map<Long, Long> focusTimeEveryTaskList;
}
