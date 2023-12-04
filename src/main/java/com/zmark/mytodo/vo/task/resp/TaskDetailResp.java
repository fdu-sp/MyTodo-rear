package com.zmark.mytodo.vo.task.resp;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.vo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.vo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.vo.task.resp.inner.TaskTagInfoResp;
import com.zmark.mytodo.vo.task.resp.inner.TaskTimeInfoResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<TaskTagInfoResp> tags;
    private TaskContentInfoResp taskContentInfo;
    private TaskPriorityInfoResp taskPriorityInfo;
    private TaskTimeInfoResp taskTimeInfo;
    private String createTime;
    private String updateTime;

    public static TaskDetailResp from(TaskDTO taskDTO) {
        return TaskDetailResp.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .completed(taskDTO.getCompleted())
                .completedTime(taskDTO.getCompletedTime().toString())
                .archived(taskDTO.getArchived())
                .tags(TaskTagInfoResp.from(taskDTO.getTags()))
                .taskContentInfo(TaskContentInfoResp.from(taskDTO.getTaskContentInfo()))
                .taskPriorityInfo(TaskPriorityInfoResp.from(taskDTO.getTaskPriorityInfo()))
                .taskTimeInfo(TaskTimeInfoResp.from(taskDTO.getTaskTimeInfo()))
                .createTime(taskDTO.getCreateTime().toString())
                .updateTime(taskDTO.getUpdateTime().toString())
                .build();
    }

    public static List<TaskDetailResp> from(List<TaskDTO> taskDTOS) {
        return taskDTOS.stream().map(TaskDetailResp::from).collect(Collectors.toList());
    }
}
