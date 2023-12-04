package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskContentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskContentInfoDAO extends JpaRepository<TaskContentInfo, Long> {
}
