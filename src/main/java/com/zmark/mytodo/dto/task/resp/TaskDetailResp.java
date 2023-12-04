package com.zmark.mytodo.dto.task.resp;

import com.zmark.mytodo.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailResp {
    private Integer id;
    private String title;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private TaskContentInfoResp taskContentInfo;
    private TaskPriorityInfoResp taskPriorityInfo;
    private TaskTimeInfoResp taskTimeInfo;
    private String createTime;
    private String updateTime;

    public static TaskDetailResp from(Task task) {
        return TaskDetailResp.builder()
                .id(task.getId())
                .title(task.getTitle())
                .completed(task.getCompleted())
                .completedTime(task.getCompletedTime().toString())
                .archived(task.getArchived())
                .taskContentInfo(TaskContentInfoResp.from(task.getTaskContentInfo()))
                .taskPriorityInfo(TaskPriorityInfoResp.from(task.getTaskPriorityInfo()))
                .taskTimeInfo(TaskTimeInfoResp.from(task.getTaskTimeInfo()))
                .createTime(task.getCreateTime().toString())
                .updateTime(task.getUpdateTime().toString())
                .build();
    }
}
