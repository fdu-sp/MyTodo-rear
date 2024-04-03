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
    /**
     * @deprecated 我的一天 建议功能中不再需要这个字段，不过安卓端仍然需要
     */
    @Deprecated
    RecommendTaskListDTO tasksEndToday;
    // 三天内：截止日期为之后三天的任务
    RecommendTaskListDTO tasksEndInThreeDays;
    // 未来：截止日期为之后四到七天的任务
    RecommendTaskListDTO tasksEndInFourToSevenDays;
    // 先前：已经过期(截止日期、提醒日期、规划日期)，但是没有完成的任务
    RecommendTaskListDTO uncompletedTasksBeforeToday;
    // 最近添加：最近三天创建的任务
    RecommendTaskListDTO latestCreatedTasks;

    public RecommendMyDayResp toResp() {
        return RecommendMyDayResp.builder()
                .tasksEndToday(tasksEndToday.toResp())
                .tasksEndInThreeDays(tasksEndInThreeDays.toResp())
                .tasksEndInFourToSevenDays(tasksEndInFourToSevenDays.toResp())
                .uncompletedTasksEndBeforeToday(uncompletedTasksBeforeToday.toResp())
                .latestCreatedTasks(latestCreatedTasks.toResp())
                .build();
    }
}
