package com.zmark.mytodo.service.api;

import com.zmark.mytodo.entity.TaskGroup;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 19:25
 */
public interface ITaskGroupService {

    TaskGroup findById(long taskGroupId);

    TaskGroup findByName(String name);

    List<TaskGroup> findAll();
}
