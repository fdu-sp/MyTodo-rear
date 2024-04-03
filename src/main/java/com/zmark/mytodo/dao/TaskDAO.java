package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface TaskDAO extends JpaRepository<Task, Long> {
    Task findTaskById(Long id);

    /**
     * 截止日期为endDate的所有任务
     */
    List<Task> findAllByTaskTimeInfo_EndDate(Date endDate);

    /**
     * 预期执行日期为expectedExecutionDate的所有任务
     */
    List<Task> findAllByTaskTimeInfo_ExpectedExecutionDate(Date expectedExecutionDate);

    /**
     * 提示日期为date的所有任务
     */
    @Query("SELECT task " +
            "FROM Task task " +
            "WHERE DATE(task.taskTimeInfo.reminderTimestamp) = DATE(:date)")
    List<Task> findAllTasksOfReminderByDate(@Param("date") Date date);

    List<Task> findAllByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThanEqual(Timestamp start, Timestamp end);
}
