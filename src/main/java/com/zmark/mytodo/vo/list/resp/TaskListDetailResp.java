package com.zmark.mytodo.vo.list.resp;

import com.zmark.mytodo.vo.task.resp.TaskSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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

    Long groupId;

    Long count;

    List<TaskSimpleResp> tasks;

    Timestamp createTime;

    Timestamp updateTime;
}
