package com.zmark.mytodo.bo.timer.resp;

import com.zmark.mytodo.entity.Timer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Violette
 * @date 2024/5/4 23:33
 * @see Timer 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerSimpleResp {
    private Long id;
    private Long taskId;
    private String startTimestamp;
}
