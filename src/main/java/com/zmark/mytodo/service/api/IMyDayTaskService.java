package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.list.RecommendMyDayDTO;
import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 17:47
 */
public interface IMyDayTaskService {
    void addToMyDayList(Long taskId) throws NoDataInDataBaseException, NewEntityException;

    void removeFromMyDayList(Long taskId) throws NoDataInDataBaseException;

    void clearMyDayList();

    List<TaskDTO> getMyDayList();

    TaskListSimpleDTO getMyDayTaskList();

    RecommendMyDayDTO getRecommendTasks();
}
