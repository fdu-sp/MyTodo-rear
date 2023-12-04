package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskPriorityInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskPriorityInfoDAO extends JpaRepository<TaskPriorityInfo, Long> {
}
