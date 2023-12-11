package com.zmark.mytodo.bo.list.resp;

import com.zmark.mytodo.bo.task.resp.TaskSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 19:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendTaskListResp {
    String title;
    List<TaskSimpleResp> taskSimpleRespList;
}
