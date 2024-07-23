package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.task.req.TaskExtractFromAudioReq;
import com.zmark.mytodo.bo.task.req.TaskExtractFromTextReq;
import com.zmark.mytodo.bo.task.resp.TaskExtractResp;

/**
 * @author ZMark
 * @date 2024/7/23 下午12:24
 */
public interface ITaskExtractService {

    TaskExtractResp extraFromText(TaskExtractFromTextReq taskExtraReq);

    TaskExtractResp extraFromAudio(TaskExtractFromAudioReq taskExtraReq);
}
