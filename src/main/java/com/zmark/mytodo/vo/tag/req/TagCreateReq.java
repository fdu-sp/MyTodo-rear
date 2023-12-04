package com.zmark.mytodo.vo.tag.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 20:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagCreateReq {
    @Valid
    @NotNull(message = "标签名称不能为空")
    String tagPath;
}
