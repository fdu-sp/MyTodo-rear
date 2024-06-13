package com.zmark.mytodo.bo.timer.resp.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Violette
 * @date 2024/6/13 21:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeWeekResp {
    String day;
    Long focusTime;
}
