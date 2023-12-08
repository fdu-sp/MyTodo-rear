package com.zmark.mytodo.dto.group;

import com.zmark.mytodo.vo.group.resp.TaskGroupSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author ZMark
 * @date 2023/12/7 19:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupSimpDTO {
    Long id;

    String name;

    private String description;

    Long count;

    Timestamp createTime;

    Timestamp updateTime;

    public TaskGroupSimpleResp toSimpleResp() {
        return TaskGroupSimpleResp.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .count(count)
                .createTime(this.createTime.toString())
                .updateTime(this.updateTime.toString())
                .build();
    }
}
