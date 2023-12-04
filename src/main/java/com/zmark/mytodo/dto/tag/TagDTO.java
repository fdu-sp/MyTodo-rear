package com.zmark.mytodo.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private String tagName;
    private String tagPath;
    private List<TagDTO> children;
}
