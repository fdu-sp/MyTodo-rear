package com.zmark.mytodo.bo.timer.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Violette
 * @date 2024/5/4 15:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerCreateReq {
    @NotNull(message = "关联任务id必填")
    private Long taskId;

    @NotNull(message = "计时器开始时间必填")
    @NotEmpty(message = "计时器开始时间不能为空")
    private String startTimestamp;
}
