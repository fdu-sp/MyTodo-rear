package com.zmark.mytodo.bo.timer.resp;

import com.zmark.mytodo.bo.timer.resp.inner.TimeWeekResp;
import com.zmark.mytodo.entity.Timer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Violette
 * @date 2024/5/6 22:52
 * @description 统计本周每天的专注时长
 * @see Timer 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerWeekAnalysisResp {
    // sql.Date类型的字符串 : 当日专注时长
    private List<TimeWeekResp> focusTimeEveryDay;
}