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

    @Query("SELECT task " +
            "FROM Task task " +
            "WHERE DATE(task.taskTimeInfo.endDate) >= DATE(:date1) AND DATE(task.taskTimeInfo.endDate) <= DATE(:date2)" +
            " AND task.completed = :complete")
    List<Task> findAllByEndDateBetweenAndTaskComplete(@Param("date1") Date date1, @Param("date2") Date date2, @Param("complete") Boolean complete);


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

    @Query("SELECT task " +
            "FROM Task task " +
            "WHERE DATE(task.taskTimeInfo.endDate) < DATE(:date) " +
            " AND task.completed = :complete")
    List<Task> findAllByEndDateBeforeAndTaskComplete(@Param("date") Date date, @Param("complete") Boolean complete);


    @Query("SELECT task " +
            "FROM Task task " +
            "WHERE DATE(task.taskTimeInfo.reminderTimestamp) < DATE(:date) " +
            " AND task.completed = :complete")
    List<Task> findAllByReminderDateBeforeAndTaskComplete(@Param("date") Date date, @Param("complete") Boolean complete);

    @Query("SELECT task " +
            "FROM Task task " +
            "WHERE DATE(task.taskTimeInfo.expectedExecutionDate) < DATE(:date) " +
            " AND task.completed = :complete")
    List<Task> findAllByExpectedDateBeforeAndTaskComplete(@Param("date") Date date, @Param("complete") Boolean complete);

    List<Task> findAllByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThanEqual(Timestamp start, Timestamp end);
}
