package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.TaskList;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 17:47
 */
public interface IMyDayTaskService {
    void addToMyDayList(Long taskId);

    void removeFromMyDayList(Long taskId);

    void clearMyDayList();

    List<TaskDTO> getMyDayList();

    TaskList getMyDayTaskList();
}
