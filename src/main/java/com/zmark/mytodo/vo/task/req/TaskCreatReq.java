package com.zmark.mytodo.vo.task.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreatReq {
    private String title;
    private String description;
    private String priority;
    private String deadline;
    private String tag;
}
