package com.zmark.mytodo.bo.task.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Violette
 * @date 2024/6/14 10:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnalysisResp {
    Long totalTaskNum;
    Long finishedTaskNum;
    Long unfinishedTaskNum;
}
