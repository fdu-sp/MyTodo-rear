package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskTimeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TaskTimeInfoDAO extends JpaRepository<TaskTimeInfo, Long> {
    List<TaskTimeInfo> findAllByEndDateIsGreaterThanEqualAndEndDateIsLessThanEqual(Date endDateStart, Date endDateEnd);
    
    List<TaskTimeInfo> findAllByReminderTimestampNotNullAndTask_Completed(Boolean completed);
}
