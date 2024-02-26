package com.zmark.mytodo.dto.list;

import com.zmark.mytodo.bo.list.resp.TaskListDetailResp;
import com.zmark.mytodo.bo.list.resp.TaskListSimpleResp;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.TaskList;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.utils.TimeUtils;
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
public class TaskListDTO {
    Long id;

    String name;

    String description;

    Long groupId;

    List<TaskDTO> taskDTOList;

    Timestamp createTime;

    Timestamp updateTime;

    public TaskListSimpleDTO toSimpleDTO() {
        return TaskListSimpleDTO.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .groupId(this.groupId)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .build();
    }

    public static TaskListDTO from(TaskList taskList, ITaskService taskService) {
        return TaskListDTO.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .description(taskList.getDescription())
                .groupId(taskList.getGroupId())
                .taskDTOList(taskList.getTaskList().stream().map(taskService::toDTO).toList())
                .createTime(taskList.getCreateTime())
                .updateTime(taskList.getUpdateTime())
                .build();
    }

    public static List<TaskListDTO> from(List<TaskList> taskLists, ITaskService taskService) {
        return taskLists.stream().map(taskList -> TaskListDTO.from(taskList, taskService)).toList();
    }

    public TaskListDetailResp toDetailResp() {
        return TaskListDetailResp.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .groupId(this.groupId)
                .count((long) this.taskDTOList.size())
                .tasks(this.taskDTOList.stream().map(TaskDTO::toSimpleResp).toList())
                .createTime(TimeUtils.toString(this.createTime))
                .updateTime(TimeUtils.toString(this.updateTime))
                .build();
    }

    public TaskListSimpleResp toSimpleResp() {
        return TaskListSimpleResp.builder()
                .id(this.id)
                .name(this.name)
                .count((long) this.taskDTOList.size())
                .description(this.description)
                .groupId(this.groupId)
                .createTime(TimeUtils.toString(this.createTime))
                .updateTime(TimeUtils.toString(this.updateTime))
                .build();
    }

    public static List<TaskListSimpleResp> toSimpleResp(List<TaskListDTO> taskListDTOList) {
        return taskListDTOList.stream().map(TaskListDTO::toSimpleResp).toList();
    }
}
