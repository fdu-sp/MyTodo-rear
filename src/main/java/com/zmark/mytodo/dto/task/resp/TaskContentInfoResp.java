package com.zmark.mytodo.dto.task.resp;

import com.zmark.mytodo.entity.TaskContentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:09
 * @see TaskContentInfo 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskContentInfoResp {
    private String description;
    private String createTime;
    private String updateTime;

    public static TaskContentInfoResp from(TaskContentInfo taskContentInfo) {
        return TaskContentInfoResp.builder()
                .description(taskContentInfo.getDescription())
                .createTime(taskContentInfo.getCreateTime().toString())
                .updateTime(taskContentInfo.getCreateTime().toString())
                .build();
    }
}
