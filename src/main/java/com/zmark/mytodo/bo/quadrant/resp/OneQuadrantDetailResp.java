package com.zmark.mytodo.bo.quadrant.resp;

import com.zmark.mytodo.bo.task.resp.TaskSimpleResp;
import com.zmark.mytodo.dto.task.TaskDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/13 3:22
 */
@Data
public class OneQuadrantDetailResp {
    private String title;
    private List<TaskSimpleResp> tasks;

    public OneQuadrantDetailResp(String title) {
        this.title = title;
        this.tasks = new ArrayList<>();
    }

    public void addTask(TaskDTO taskDTO) {
        this.tasks.add(TaskDTO.toSimpleResp(taskDTO));
    }
}
