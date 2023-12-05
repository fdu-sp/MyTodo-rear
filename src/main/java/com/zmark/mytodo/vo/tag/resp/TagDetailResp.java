package com.zmark.mytodo.vo.tag.resp;

import com.zmark.mytodo.dto.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/5 13:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDetailResp {
    private Long id;
    private String tagName;
    private String tagPath;
    private List<TagDTO> children;
}
