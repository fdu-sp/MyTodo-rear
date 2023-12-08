package com.zmark.mytodo.bo.task.resp.inner;

import com.zmark.mytodo.entity.TaskContentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/4 10:09
 * @see TaskContentInfo 对应的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskContentInfoResp {
    private String description;
    private String createTime;
    private String updateTime;
}
