package com.zmark.mytodo.bo.timer.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Violette
 * @date 2024/5/5 0:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerUpdateReq {
    @NotNull(message = "计时器id必填")
    private Long id;
}
