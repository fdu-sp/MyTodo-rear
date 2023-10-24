package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskTimeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTimeInfoDAO extends JpaRepository<TaskTimeInfo, Integer> {
}
