package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDAO extends JpaRepository<Task, Integer> {
    Task findTaskById(int id);
}
