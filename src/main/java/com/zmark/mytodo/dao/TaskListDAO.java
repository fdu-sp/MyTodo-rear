package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZMark
 * @date 2023/12/7 19:20
 */
public interface TaskListDAO extends JpaRepository<TaskList, Long> {
    TaskList findById(long id);

    TaskList findTaskListById(long id);

    TaskList findByNameAndGroupId(String name, long groupId);

    long countTaskListsByGroupId(long taskGroupId);
}
