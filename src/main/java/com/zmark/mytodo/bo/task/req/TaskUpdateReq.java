package com.zmark.mytodo.bo.task.req;

import com.zmark.mytodo.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.bo.task.resp.inner.TaskTimeInfoResp;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/14 1:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateReq {
    @NotNull(message = "任务id必填")
    private Long id;
    @NotEmpty(message = "任务标题不能为空")
    private String title;
    @NotNull
    private Boolean completed;
    private String completedTime;
    @NotNull
    private Boolean archived;
    @NotNull
    private List<TagSimpleResp> tags;
    @NotNull
    private Long taskListId;
    @NotNull
    private String taskListName;
    @NotNull
    private Boolean inMyDay;
    @NotNull
    private TaskContentInfoResp taskContentInfo;
    @NotNull
    private TaskPriorityInfoResp taskPriorityInfo;
    @NotNull
    private TaskTimeInfoResp taskTimeInfo;

}
