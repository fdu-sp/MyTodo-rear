package com.zmark.mytodo.dto.list;

import com.zmark.mytodo.bo.list.resp.RecommendTaskListResp;
import com.zmark.mytodo.dto.task.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 我的一天 推荐任务
 *
 * @author ZMark
 * @date 2023/12/11 19:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendTaskListDTO {
    String title;
    List<TaskDTO> taskDTOList;

    public RecommendTaskListResp toResp() {
        return RecommendTaskListResp.builder()
                .title(title)
                .taskSimpleRespList(TaskDTO.toSimpleResp(taskDTOList))
                .build();
    }

    public void removeCompletedTasks() {
        taskDTOList = taskDTOList.stream()
                .filter(taskDTO -> !taskDTO.getCompleted())
                .collect(Collectors.toList());
    }

    public void removeTasksInList(RecommendTaskListDTO preRecommendTaskListDTO) {
        List<Long> preTaskIds = preRecommendTaskListDTO.getTaskDTOList().stream()
                .map(TaskDTO::getId)
                .toList();
        taskDTOList = taskDTOList.stream()
                .filter(taskDTO -> !preTaskIds.contains(taskDTO.getId()))
                .collect(Collectors.toList());
    }
}
