package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskTimeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskTimeInfoDAO extends JpaRepository<TaskTimeInfo, Long> {
    List<TaskTimeInfo> findAllByReminderTimestampNotNullAndTask_Completed(Boolean completed);
}
