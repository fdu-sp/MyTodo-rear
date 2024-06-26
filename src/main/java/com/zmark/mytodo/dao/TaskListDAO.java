package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 19:20
 */
public interface TaskListDAO extends JpaRepository<TaskList, Long> {
    TaskList findTaskListById(long id);

    TaskList findByNameAndGroupId(String name, long groupId);

    long countTaskListsByGroupId(long taskGroupId);
}
