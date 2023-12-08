package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.vo.list.req.TaskListCreatReq;

/**
 * @author ZMark
 * @date 2023/12/7 19:22
 */
public interface ITaskListService {

    TaskListDTO findById(long taskListId) throws NoDataInDataBaseException;

    TaskListDTO findByName(String name) throws NoDataInDataBaseException;

    /**
     * @throws NoDataInDataBaseException 如果没有找到对应的分组
     * @throws NewEntityException        如果name已经存在
     */
    TaskListDTO createNewTaskList(TaskListCreatReq creatReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase;

    long countByTaskGroup(long tagGroupId);

    long countByTaskGroup(String groupName);
}
