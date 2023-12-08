package com.zmark.mytodo.bo.task.resp;

import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.bo.tag.resp.TagSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 10:20
 * @see Task 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSimpleResp {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private String expectedDate;
    private Boolean isImportant;
    private Boolean isUrgent;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private List<TagSimpleResp> tags;
    private String createTime;
    private String updateTime;
}
