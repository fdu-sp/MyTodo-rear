package com.zmark.mytodo.bo.task.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2024/7/23 下午12:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskExtractFromTextReq {
    @NotNull(message = "请输入一句话")
    @NotEmpty(message = "请输入一句话")
    private String text;
}
