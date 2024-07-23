package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.task.req.TaskExtractFromAudioReq;
import com.zmark.mytodo.bo.task.req.TaskExtractFromTextReq;
import com.zmark.mytodo.bo.task.resp.TaskExtractResp;
import com.zmark.mytodo.service.api.ITaskExtractService;

/**
 * @author ZMark
 * @date 2024/7/23 下午12:31
 */
public class TaskExtractService implements ITaskExtractService {
    /**
     * 从文本中提取任务信息，包括任务的标题、描述、标签（0-多个）、截止时间、提醒时间、规划执行时间（time to time）、重复规则
     */
    @Override
    public TaskExtractResp extraFromText(TaskExtractFromTextReq taskExtractReq) {
        String text = taskExtractReq.getText();
        return null;
    }

    @Override
    public TaskExtractResp extraFromAudio(TaskExtractFromAudioReq taskExtractReq) {
        // TODO
        return null;
    }
}
