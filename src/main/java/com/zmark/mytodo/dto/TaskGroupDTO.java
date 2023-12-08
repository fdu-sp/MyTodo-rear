package com.zmark.mytodo.dto;

import com.zmark.mytodo.entity.TaskGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 19:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupDTO {
    Long id;

    String name;

    private String description;

    List<TaskListDTO> taskLists;

    Timestamp createTime;

    Timestamp updateTime;

    public static TaskGroupDTO from(TaskGroup taskGroup) {
        return TaskGroupDTO.builder()
                .id(taskGroup.getId())
                .name(taskGroup.getName())
                .description(taskGroup.getDescription())
                .createTime(taskGroup.getCreateTime())
                .updateTime(taskGroup.getUpdateTime())
                .build();
    }
}
