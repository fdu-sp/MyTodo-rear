package com.zmark.mytodo.vo.task.resp.inner;

import com.zmark.mytodo.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZMark
 * @date 2023/12/4 10:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskTagInfoResp {
    private Long id;
    private String tagName;
    private String tagPath;

    public static TaskTagInfoResp from(Tag tag) {
        return TaskTagInfoResp.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .build();
    }

    public static List<TaskTagInfoResp> from(List<Tag> tags) {
        return tags.stream().map(TaskTagInfoResp::from).collect(Collectors.toList());
    }
}
