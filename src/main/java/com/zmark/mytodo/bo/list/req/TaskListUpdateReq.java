package com.zmark.mytodo.bo.list.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/18 22:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListUpdateReq {
    @NotNull(message = "清单id必填")
    Long id;

    @NotNull(message = "清单名称必填")
    @NotEmpty(message = "清单名称不能为空")
    String name;

    /**
     * 必填，但是可以为空
     */
    @NotNull(message = "清单描述必填")
    String description;

    @NotNull(message = "清单所属分组必填")
    Long taskGroupId;
}
