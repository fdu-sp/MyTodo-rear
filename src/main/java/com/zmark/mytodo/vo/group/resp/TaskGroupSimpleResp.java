package com.zmark.mytodo.vo.group.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    String createTime;
    String updateTime;
}
