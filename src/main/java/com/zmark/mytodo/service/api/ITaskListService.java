package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.list.req.TaskListCreateReq;
import com.zmark.mytodo.bo.list.req.TaskListUpdateReq;
import com.zmark.mytodo.dto.list.TaskListDTO;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 19:22
 */
public interface ITaskListService {

    List<TaskListDTO> findAll() throws NoDataInDataBaseException;

    TaskListDTO findById(long taskListId) throws NoDataInDataBaseException;

    /**
     * @throws NoDataInDataBaseException 如果没有找到对应的分组
     * @throws RepeatedEntityInDatabase  如果分组中同名的清单已经存在
     */
    TaskListDTO createNewTaskList(TaskListCreateReq creatReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase;

    long countByTaskGroup(long tagGroupId);

    long countByTaskGroup(String groupName);

    /**
     * 更新清单信息：清单名称（在一个分组内，清单名称不能重复）、清单描述、清单所属分组
     *
     * @throws NoDataInDataBaseException 如果没有找到对应的分组
     * @throws RepeatedEntityInDatabase  如果（new）分组中同名的清单已经存在
     */
    @Transactional
    TaskListDTO updateTaskList(TaskListUpdateReq updateReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase;
}
