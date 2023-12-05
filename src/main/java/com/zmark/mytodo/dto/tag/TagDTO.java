package com.zmark.mytodo.dto.tag;

import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.vo.tag.resp.TagDetailResp;
import com.zmark.mytodo.vo.tag.resp.TagSimpleResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long id;
    private String tagName;
    private String tagPath;
    private List<TagDTO> children;

    public static TagDTO from(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .children(new ArrayList<>())
                .build();
    }

    public static TagDTO from(Tag tag, List<TagDTO> childrenTagDTOList) {
        return TagDTO.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .children(childrenTagDTOList)
                .build();
    }

    public static TagDetailResp toDetailResp(TagDTO tagDTO) throws IllegalArgumentException {
        if (tagDTO == null) {
            throw new IllegalArgumentException("tagDTO cannot be null");
        }
        return TagDetailResp.builder()
                .id(tagDTO.getId())
                .tagName(tagDTO.getTagName())
                .tagPath(tagDTO.getTagPath())
                .children(tagDTO.getChildren())
                .build();
    }

    public static List<TagDetailResp> toDetailResp(List<TagDTO> tagDTOList) {
        return tagDTOList.stream().map(TagDTO::toDetailResp).toList();
    }

    public static TagSimpleResp toSimpleResp(TagDTO tagDTO) throws IllegalArgumentException {
        if (tagDTO == null) {
            throw new IllegalArgumentException("tagDTO cannot be null");
        }
        return TagSimpleResp.builder()
                .id(tagDTO.getId())
                .tagName(tagDTO.getTagName())
                .tagPath(tagDTO.getTagPath())
                .build();
    }

    public static List<TagSimpleResp> toSimpleResp(List<TagDTO> tagDTOList) {
        return tagDTOList.stream().map(TagDTO::toSimpleResp).collect(Collectors.toList());
    }

    public static TagSimpleResp toSimpleResp(Tag tag) {
        return TagSimpleResp.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .build();
    }

    public static List<TagSimpleResp> toSimpleRespFromTag(List<Tag> tags) {
        return tags.stream().map(TagDTO::toSimpleResp).collect(Collectors.toList());
    }
}
