package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskTagMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagMatchDAO extends JpaRepository<TaskTagMatch, Integer> {
}
