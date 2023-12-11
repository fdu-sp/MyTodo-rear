package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.TaskList;
import com.zmark.mytodo.service.api.IMyDayTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 18:03
 */
@Service
public class MyDayTaskService implements IMyDayTaskService {
    @Override
    public void addToMyDayList(Long taskId) {

    }

    @Override
    public void removeFromMyDayList(Long taskId) {

    }

    @Override
    public void clearMyDayList() {

    }

    @Override
    public List<TaskDTO> getMyDayList() {
        return null;
    }

    @Override
    public TaskList getMyDayTaskList() {
        return null;
    }
}
