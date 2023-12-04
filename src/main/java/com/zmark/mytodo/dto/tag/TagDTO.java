package com.zmark.mytodo.dto.tag;

import com.zmark.mytodo.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private String tagName;
    private String tagPath;
    private List<TagDTO> children;

    public static TagDTO from(Tag tag) {
        return TagDTO.builder()
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .children(new ArrayList<>())
                .build();
    }

    public static TagDTO from(Tag tag, List<TagDTO> childrenTagDTOList) {
        return TagDTO.builder()
                .tagName(tag.getTagName())
                .tagPath(tag.getTagPath())
                .children(childrenTagDTOList)
                .build();
    }
}
