package com.zmark.mytodo.bo.task.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2024/4/5 16:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskQueryByTagsReq {
    @NotNull(message = "标签id列表必填")
    private List<Long> tagIds;
}
