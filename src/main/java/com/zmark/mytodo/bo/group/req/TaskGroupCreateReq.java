package com.zmark.mytodo.bo.group.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/8 14:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupCreateReq {
    @NotNull(message = "分组名称必填")
    @NotEmpty(message = "分组名称不能为空")
    String name;

    /**
     * 必填，但是可以为空
     */
    @NotNull(message = "分组描述必填")
    String description;
}
