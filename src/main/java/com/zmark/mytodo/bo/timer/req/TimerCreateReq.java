package com.zmark.mytodo.bo.timer.req;

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
}
