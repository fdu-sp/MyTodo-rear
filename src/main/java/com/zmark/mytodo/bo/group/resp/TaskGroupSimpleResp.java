package com.zmark.mytodo.bo.group.resp;

import com.zmark.mytodo.bo.list.resp.TaskListSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 20:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupSimpleResp {
    Long id;
    String name;
    String description;
    Long count;
    List<TaskListSimpleResp> taskLists;
    String createTime;
    String updateTime;
}
