package com.zmark.mytodo.vo.task.resp;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.vo.task.resp.inner.TaskTagInfoResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<TaskTagInfoResp> tags;
    private String createTime;
    private String updateTime;

    public static TaskSimpleResp from(TaskDTO taskDTO) {
        return TaskSimpleResp.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .completed(taskDTO.getCompleted())
                .completedTime(taskDTO.getCompletedTime().toString())
                .archived(taskDTO.getArchived())
                .tags(TaskTagInfoResp.from(taskDTO.getTags()))
                .createTime(taskDTO.getCreateTime().toString())
                .updateTime(taskDTO.getUpdateTime().toString())
                .build();
    }

    public static List<TaskSimpleResp> from(List<TaskDTO> taskDTOS) {
        return taskDTOS.stream().map(TaskSimpleResp::from).collect(Collectors.toList());
    }
}
