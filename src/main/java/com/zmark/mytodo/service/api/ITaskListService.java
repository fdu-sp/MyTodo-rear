package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.TaskListDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.vo.list.req.TaskListCreatReq;

/**
 * @author ZMark
 * @date 2023/12/7 19:22
 */
public interface ITaskListService {

    TaskListDTO findById(long taskListId) throws NoDataInDataBaseException;

    TaskListDTO findByName(String name) throws NoDataInDataBaseException;

    TaskListDTO createNewTaskList(TaskListCreatReq creatReq) throws NoDataInDataBaseException, NewEntityException;

    long countByTaskGroup(long tagGroupId);

    long countByTaskGroup(String groupName);
}
