package com.zmark.mytodo.dao;

import com.zmark.mytodo.entity.TaskTagMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskTagMatchDAO extends JpaRepository<TaskTagMatch, Long> {
    List<TaskTagMatch> findAllByTagIdAndTaskId(Long tagId, Long taskId);

    List<TaskTagMatch> findAllByTagId(Long tagId);

    List<TaskTagMatch> findAllByTaskId(Long taskId);

    void deleteAllByTaskId(Long taskId);

    void deleteAllByTagId(Long tagId);
}
