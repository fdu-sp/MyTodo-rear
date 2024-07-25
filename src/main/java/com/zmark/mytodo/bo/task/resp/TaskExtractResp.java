package com.zmark.mytodo.bo.task.resp;

import com.alibaba.fastjson.JSON;
import com.zmark.mytodo.bo.task.req.TaskCreateReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2024/7/23 下午12:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExtractResp {
    private String title;
    private String description;
    private List<String> tags;
    private String dueTime;
    private String remindTime;
    private String planningFromTime;
    private String planningToTime;
    private Boolean isUrgent;
    private Boolean isImportant;
    private Boolean inMyDay;

    public static TaskExtractResp fromJson(String json) {
        return JSON.parseObject(json, TaskExtractResp.class);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public TaskCreateReq toTaskCreateReq() {
        return TaskCreateReq.builder()
                .title(this.title)
                .description(this.description)
                .tagNames(this.tags)
                .completed(Boolean.FALSE)
                .endDate(this.dueTime)
                .reminderTimestamp(this.remindTime)
                .expectedExecutionStartPeriod(this.planningFromTime)
                .expectedExecutionEndPeriod(this.planningToTime)
                .isUrgent(this.isUrgent)
                .isImportant(this.isImportant)
                .inMyDay(this.inMyDay)
                .build();
    }
}
