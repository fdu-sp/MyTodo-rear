package com.zmark.mytodo.bo.list.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/11 19:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendMyDayResp {
    /**
     * todo 截止日期为今天的任务--应该自动加入到我的一天
     */
    RecommendTaskListResp tasksEndToday;

    /**
     * 截止日期为之后三天的任务
     */
    RecommendTaskListResp tasksEndInThreeDays;

    /**
     * 截止日期为之后四到七天的任务
     */
    RecommendTaskListResp tasksEndInFourToSevenDays;

    /**
     * 已经过期，但是没有完成的任务
     */
    RecommendTaskListResp uncompletedTasksEndBeforeToday;

    /**
     * 最新一天创建的任务
     */
    RecommendTaskListResp latestCreatedTasks;
}
