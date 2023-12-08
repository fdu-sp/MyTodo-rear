package com.zmark.mytodo.bo.task.resp;

import com.zmark.mytodo.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskTimeInfoResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 10:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailResp {
    private Long id;
    private String title;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private List<TagSimpleResp> tags;
    private TaskContentInfoResp taskContentInfo;
    private TaskPriorityInfoResp taskPriorityInfo;
    private TaskTimeInfoResp taskTimeInfo;
    private String createTime;
    private String updateTime;
}
