package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface TaskDAO extends JpaRepository<Task, Long> {
    Task findTaskById(Long id);

    List<Task> findAllByTaskTimeInfo_EndDate(Date endDate);

    List<Task> findAllByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThanEqual(Timestamp start, Timestamp end);
}
