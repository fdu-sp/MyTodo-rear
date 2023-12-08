package com.zmark.mytodo.bo.tag.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 20:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagCreateReq {
    @NotNull(message = "标签名称不能为空")
    @NotEmpty(message = "至少新增一个标签")
    List<String> tagPathList;
}
