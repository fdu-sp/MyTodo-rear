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

    TaskListSimpleDTO getMyDayTaskListSimple();

    RecommendMyDayDTO getRecommendTasks();

    /**
     * 在新的一天到达后才执行<br/>
     * 1. 清空我的一天列表<br/>
     * 2. 加入今日截止的任务、设定今日提醒的任务、规划今日执行的任务<br/>
     * 任务是否完成不重要
     *
     * @return 返回添加到我的一天列表的任务数量
     */
    int updateMyDayTaskList();
}
