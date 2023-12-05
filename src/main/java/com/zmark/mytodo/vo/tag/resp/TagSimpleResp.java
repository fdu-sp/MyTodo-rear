package com.zmark.mytodo.vo.tag.resp;

import com.zmark.mytodo.dto.tag.TagDTO;
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
public class TagSimpleResp {
    private Long id;
    private String tagName;
    private String tagPath;

    public static TagSimpleResp from(TagDTO tagDTO) {
        return TagSimpleResp.builder()
                .id(tagDTO.getId())
                .tagName(tagDTO.getTagName())
                .tagPath(tagDTO.getTagPath())
                .build();
    }

    public static TagSimpleResp from(Tag tag) {
        return TagSimpleResp.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .build();
    }

    public static List<TagSimpleResp> from(List<Tag> tags) {
        return tags.stream().map(TagSimpleResp::from).collect(Collectors.toList());
    }
}
