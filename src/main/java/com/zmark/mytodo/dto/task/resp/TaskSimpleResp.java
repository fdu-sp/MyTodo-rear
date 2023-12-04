package com.zmark.mytodo.dto.task.resp;

import com.zmark.mytodo.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:20
 * @see Task 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSimpleResp {
    private Integer id;
    private String title;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private String createTime;
    private String updateTime;

    public static TaskSimpleResp from(Task task) {
        return TaskSimpleResp.builder()
                .id(task.getId())
                .title(task.getTitle())
                .completed(task.getCompleted())
                .completedTime(task.getCompletedTime().toString())
                .archived(task.getArchived())
                .createTime(task.getCreateTime().toString())
                .updateTime(task.getUpdateTime().toString())
                .build();
    }
}
