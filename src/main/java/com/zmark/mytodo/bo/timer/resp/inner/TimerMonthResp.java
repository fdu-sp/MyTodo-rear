package com.zmark.mytodo.bo.timer.resp.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Violette
 * @date 2024/6/13 21:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerMonthResp {
    Long taskListId;
    Long focusTime;
}
