package com.zmark.mytodo.bo.tag.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
