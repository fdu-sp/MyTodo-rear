package com.zmark.mytodo.dto.task;

import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.entity.*;
import com.zmark.mytodo.utils.TimeUtils;
import com.zmark.mytodo.vo.task.resp.TaskDetailResp;
import com.zmark.mytodo.vo.task.resp.TaskSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于TaskService传递数据的DTO
 *
 * @author ZMark
 * @date 2023/12/4 10:52
 * @see Task 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;

    private String title;

    private Boolean completed;

    private Timestamp completedTime;

    private Boolean archived;

    private List<Tag> tags;

    private TaskContentInfo taskContentInfo;

    private TaskPriorityInfo taskPriorityInfo;

    private TaskTimeInfo taskTimeInfo;

    private Timestamp createTime;

    private Timestamp updateTime;

    public static TaskDTO from(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .completed(task.getCompleted())
                .completedTime(task.getCompletedTime())
                .archived(task.getArchived())
                .tags(new ArrayList<>())
                .taskContentInfo(task.getTaskContentInfo())
                .taskPriorityInfo(task.getTaskPriorityInfo())
                .taskTimeInfo(task.getTaskTimeInfo())
                .createTime(task.getCreateTime())
                .updateTime(task.getUpdateTime())
                .build();
    }

    public static TaskDTO from(Task task, List<Tag> tags) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .completed(task.getCompleted())
                .completedTime(task.getCompletedTime())
                .archived(task.getArchived())
                .tags(tags)
                .taskContentInfo(task.getTaskContentInfo())
                .taskPriorityInfo(task.getTaskPriorityInfo())
                .taskTimeInfo(task.getTaskTimeInfo())
                .createTime(task.getCreateTime())
                .updateTime(task.getUpdateTime())
                .build();
    }

    public static TaskDetailResp toDetailResp(TaskDTO taskDTO) {
        return TaskDetailResp.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .completed(taskDTO.getCompleted())
                .completedTime(TimeUtils.toString(taskDTO.getCompletedTime()))
                .archived(taskDTO.getArchived())
                .tags(TagDTO.toSimpleRespFromTag(taskDTO.getTags()))
                .taskContentInfo(TaskContentInfo.from(taskDTO.getTaskContentInfo()))
                .taskPriorityInfo(TaskPriorityInfo.from(taskDTO.getTaskPriorityInfo()))
                .taskTimeInfo(TaskTimeInfo.from(taskDTO.getTaskTimeInfo()))
                .createTime(TimeUtils.toString(taskDTO.getCreateTime()))
                .updateTime(TimeUtils.toString(taskDTO.getUpdateTime()))
                .build();
    }

    public static List<TaskDetailResp> toDetailResp(List<TaskDTO> taskDTOS) {
        return taskDTOS.stream().map(TaskDTO::toDetailResp).collect(Collectors.toList());
    }

    public static TaskSimpleResp toSimpleResp(TaskDTO taskDTO) {
        return TaskSimpleResp.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getTaskContentInfo().getDescription())
                .dueDate(TimeUtils.toString(taskDTO.getTaskTimeInfo().getEndDate()))
                .completed(taskDTO.getCompleted())
                .completedTime(TimeUtils.toString(taskDTO.getCompletedTime()))
                .archived(taskDTO.getArchived())
                .tags(TagDTO.toSimpleRespFromTag(taskDTO.getTags()))
                .createTime(TimeUtils.toString(taskDTO.getCreateTime()))
                .updateTime(TimeUtils.toString(taskDTO.getUpdateTime()))
                .build();
    }

    public static List<TaskSimpleResp> toSimpleResp(List<TaskDTO> taskDTOS) {
        return taskDTOS.stream().map(TaskDTO::toSimpleResp).collect(Collectors.toList());
    }
}
