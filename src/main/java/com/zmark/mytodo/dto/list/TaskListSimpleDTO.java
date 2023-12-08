package com.zmark.mytodo.dto.list;

import com.zmark.mytodo.utils.TimeUtils;
import com.zmark.mytodo.bo.list.resp.TaskListSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author ZMark
 * @date 2023/12/8 15:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListSimpleDTO {
    Long id;

    String name;

    Long count;

    String description;

    Long groupId;

    Timestamp createTime;

    Timestamp updateTime;

    public TaskListSimpleResp toSimpleResp() {
        return TaskListSimpleResp.builder()
                .id(this.id)
                .name(this.name)
                .count(this.count)
                .description(this.description)
                .groupId(this.groupId)
                .createTime(TimeUtils.toString(this.createTime))
                .updateTime(TimeUtils.toString(this.updateTime))
                .build();
    }
}
