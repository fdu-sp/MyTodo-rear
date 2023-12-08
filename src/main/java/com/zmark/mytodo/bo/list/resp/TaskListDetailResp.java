package com.zmark.mytodo.bo.list.resp;

import com.zmark.mytodo.bo.task.resp.TaskSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 20:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDetailResp {
    Long id;

    String name;

    String description;

    Long groupId;

    Long count;

    List<TaskSimpleResp> tasks;

    String createTime;

    String updateTime;
}
