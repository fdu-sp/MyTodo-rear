package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskTagMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskTagMatchDAO extends JpaRepository<TaskTagMatch, Integer> {
    List<TaskTagMatch> findAllByTagId(Integer tagId);
}
