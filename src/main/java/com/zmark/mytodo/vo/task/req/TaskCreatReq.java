package com.zmark.mytodo.vo.task.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 10:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreatReq {
    @NotNull(message = "任务标题必填")
    @NotEmpty(message = "任务标题不能为空")
    private String title;

    @NotNull(message = "任务标签必填")
    private List<String> tagNames;

    /**
     * content description
     */
    @NotNull(message = "任务描述必填")
    private String description;

    /**
     * priority info
     */
    private Boolean isImportant;
    private Boolean isUrgent;

    /**
     * time info
     */
    private Date endDate;
    private Time endTime;
    private Boolean activateCountdown;
    private Date expectedExecutionDate;
    private Time expectedExecutionStartPeriod;
    private Time expectedExecutionEndPeriod;
}
