package com.zmark.mytodo.bo.list.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/8 14:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListSimpleResp {
    Long id;
    String name;
    Long count;
    String description;
    Long groupId;
    String createTime;
    String updateTime;
}
