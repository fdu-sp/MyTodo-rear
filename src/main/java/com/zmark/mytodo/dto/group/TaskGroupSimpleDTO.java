package com.zmark.mytodo.dto.group;

import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.vo.group.resp.TaskGroupSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZMark
 * @date 2023/12/7 19:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupSimpleDTO {
    Long id;

    String name;

    private String description;

    Long count;

    List<TaskListSimpleDTO> taskListDTOS;

    Timestamp createTime;

    Timestamp updateTime;

    public TaskGroupSimpleResp toSimpleResp() {
        return TaskGroupSimpleResp.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .count(count)
                .taskLists(this.taskListDTOS.stream().map(TaskListSimpleDTO::toSimpleResp).collect(Collectors.toList()))
                .createTime(this.createTime.toString())
                .updateTime(this.updateTime.toString())
                .build();
    }

    public static List<TaskGroupSimpleResp> toSimpleResp(List<TaskGroupSimpleDTO> taskGroupSimpleDTOList) {
        return taskGroupSimpleDTOList.stream().map(TaskGroupSimpleDTO::toSimpleResp).collect(Collectors.toList());
    }
}
