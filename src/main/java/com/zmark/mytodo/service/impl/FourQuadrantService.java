package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.bo.quadrant.resp.OneQuadrantDetailResp;
import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.dto.list.TaskListSimpleDTO;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.entity.TaskPriorityInfo;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.IFourQuadrantService;
import com.zmark.mytodo.service.api.IMyDayTaskService;
import com.zmark.mytodo.service.api.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/13 3:19
 */
@Service
public class FourQuadrantService implements IFourQuadrantService {

    private final ITaskListService taskListService;

    private final IMyDayTaskService myDayTaskService;

    @Autowired
    public FourQuadrantService(TaskListService taskListService,
                               MyDayTaskService myDayTaskService) {
        this.taskListService = taskListService;
        this.myDayTaskService = myDayTaskService;
    }

    @Override
    public FourQuadrantDetailResp getFourQuadrantDetailByList(Long taskListId) throws NoDataInDataBaseException {
        TaskListDTO taskListDTO = taskListService.findById(taskListId);
        return covertFrom(taskListDTO.getTaskDTOList(), taskListDTO.toSimpleDTO());
    }

    @Override
    public FourQuadrantDetailResp getFourQuadrantDetailByMyDay() {
        List<TaskDTO> taskDTOList = myDayTaskService.getMyDayList();
        TaskListSimpleDTO taskListSimpleDTO = myDayTaskService.getMyDayTaskListSimple();
        return covertFrom(taskDTOList, taskListSimpleDTO);
    }

    private FourQuadrantDetailResp covertFrom(List<TaskDTO> taskDTOList, TaskListSimpleDTO taskListSimpleDTO) {
        OneQuadrantDetailResp urgentAndImportant = new OneQuadrantDetailResp("紧急且重要");
        OneQuadrantDetailResp urgentButNotImportant = new OneQuadrantDetailResp("紧急不重要");
        OneQuadrantDetailResp notUrgentButImportant = new OneQuadrantDetailResp("不紧急但重要");
        OneQuadrantDetailResp notUrgentAndNotImportant = new OneQuadrantDetailResp("不紧急不重要");
        taskDTOList.forEach(taskDTO -> {
            TaskPriorityInfo taskPriorityInfo = taskDTO.getTaskPriorityInfo();
            if (taskPriorityInfo.getIsImportant()
                    && taskPriorityInfo.getIsUrgent()) {
                urgentAndImportant.addTask(taskDTO);
            } else if (taskPriorityInfo.getIsImportant()) {
                notUrgentButImportant.addTask(taskDTO);
            } else if (taskPriorityInfo.getIsUrgent()) {
                urgentButNotImportant.addTask(taskDTO);
            } else {
                notUrgentAndNotImportant.addTask(taskDTO);
            }
        });
        return FourQuadrantDetailResp.builder()
                .taskListInfo(taskListSimpleDTO.toSimpleResp())
                .urgentAndImportant(urgentAndImportant)
                .urgentAndNotImportant(urgentButNotImportant)
                .notUrgentAndImportant(notUrgentButImportant)
                .notUrgentAndNotImportant(notUrgentAndNotImportant)
                .build();
    }

}
