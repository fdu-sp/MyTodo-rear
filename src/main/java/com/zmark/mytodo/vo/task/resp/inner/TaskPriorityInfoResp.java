package com.zmark.mytodo.vo.task.resp.inner;

import com.zmark.mytodo.entity.TaskPriorityInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:14
 * @see TaskPriorityInfo 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityInfoResp {
    private Boolean isImportant;
    private Boolean isUrgent;

    public static TaskPriorityInfoResp from(TaskPriorityInfo taskPriorityInfo) {
        return TaskPriorityInfoResp.builder()
                .isImportant(taskPriorityInfo.getIsImportant())
                .isUrgent(taskPriorityInfo.getIsUrgent())
                .build();
    }
}
