package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZMark
 * @date 2023/12/7 19:17
 */
public interface TaskGroupDAO extends JpaRepository<TaskGroup, Long> {

    TaskGroup findById(long id);

    TaskGroup findByName(String name);
}
