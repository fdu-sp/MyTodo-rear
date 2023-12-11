package com.zmark.mytodo.dto.list;

import com.zmark.mytodo.bo.list.resp.RecommendMyDayResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/11 19:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendMyDayDTO {
    // 截止日期为今天的任务
    RecommendTaskListDTO tasksEndToday;
    // 截止日期为之后三天的任务
    RecommendTaskListDTO tasksEndInThreeDays;
    // 截止日期为之后四到七天的任务
    RecommendTaskListDTO tasksEndInFourToSevenDays;
    // 已经过期，但是没有完成的任务
    RecommendTaskListDTO uncompletedTasksEndBeforeToday;
    // 最新一天创建的任务
    RecommendTaskListDTO latestCreatedTasks;

    public RecommendMyDayResp toResp() {
        return RecommendMyDayResp.builder()
                .tasksEndToday(tasksEndToday.toResp())
                .tasksEndInThreeDays(tasksEndInThreeDays.toResp())
                .tasksEndInFourToSevenDays(tasksEndInFourToSevenDays.toResp())
                .uncompletedTasksEndBeforeToday(uncompletedTasksEndBeforeToday.toResp())
                .latestCreatedTasks(latestCreatedTasks.toResp())
                .build();
    }
}
