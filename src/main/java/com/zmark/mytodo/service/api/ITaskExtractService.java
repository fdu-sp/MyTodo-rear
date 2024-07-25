package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.task.req.TaskExtractFromAudioReq;
import com.zmark.mytodo.bo.task.req.TaskExtractFromTextReq;
import com.zmark.mytodo.bo.task.resp.TaskExtractResp;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;

import java.io.IOException;

/**
 * @author ZMark
 * @date 2024/7/23 下午12:24
 */
public interface ITaskExtractService {

    TaskExtractResp extractFromText(TaskExtractFromTextReq taskExtractReq) throws IOException;

    TaskDTO extractAndAddFromText(TaskExtractFromTextReq taskExtractReq) throws IOException, NewEntityException, NoDataInDataBaseException;

    TaskExtractResp extractFromAudio(TaskExtractFromAudioReq taskExtractReq);
}
