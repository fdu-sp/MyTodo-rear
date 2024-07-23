package com.zmark.mytodo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zmark.mytodo.bo.task.req.TaskExtractFromAudioReq;
import com.zmark.mytodo.bo.task.req.TaskExtractFromTextReq;
import com.zmark.mytodo.bo.task.resp.TaskExtractResp;
import com.zmark.mytodo.gpt.IGptClient;
import com.zmark.mytodo.gpt.OpenAIClient;
import com.zmark.mytodo.service.api.ITaskExtractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author ZMark
 * @date 2024/7/23 下午12:31
 */
@Slf4j
@Service
public class TaskExtractService implements ITaskExtractService {

    private IGptClient gptClient;

    @Autowired
    public void setGptClient(OpenAIClient gptClient) {
        this.gptClient = gptClient;
    }

    /**
     * 从文本中提取任务信息，包括任务标题、描述、标签（0-多个）、截止时间、提醒时间、规划执行时间（time to time）、是否紧急、是否重要、是否需要在今天完成、重复规则等信息
     */
    @Override
    public TaskExtractResp extraFromText(TaskExtractFromTextReq taskExtractReq) throws IOException {
        String text = taskExtractReq.getText();
        String prompt = generatePrompt(text);
        String response = gptClient.call(prompt);
        JSONObject jsonObject = JSON.parseObject(response);
        String ansJson = jsonObject.getJSONArray("choices").getJSONObject(0).getString("text");
        return TaskExtractResp.fromJson(ansJson);
    }

    @Override
    public TaskExtractResp extraFromAudio(TaskExtractFromAudioReq taskExtractReq) {
        // TODO
        return null;
    }

    private String generatePrompt(String text) {
        return "请从以下文字中提取任务相关的信息，包括任务标题、描述、标签（0-多个字符串）、" +
                "截止时间、提醒时间、规划执行时间（time to time）、" +
                "是否紧急、是否重要、是否需要在今天完成，" +
                "语言和下面的文字匹配，并按json格式输出：\n\n" +
                "文字：\n\"" + text + "\"\n\n" +
                "json格式：\n" +
                "{\n" +
                "  \"title\": \"\",\n" +
                "  \"description\": \"\",\n" +
                "  \"tags\": [\"\", \"\"],\n" +
                "  \"dueTime\": \"\",\n" +
                "  \"remindTime\": \"\",\n" +
                "  \"planningToTime\": \"\",\n" +
                "  \"planningFromTime\": \"\"\n" +
                "  \"isUrgent\": false,\n" +
                "  \"isImportant\": false,\n" +
                "  \"inMyDay\": false\n" +
                "}\n" +
                "请注意：\n" +
                "1. 请确保提取的信息是准确且完整的，不要遗漏或错误提取信息；\n" +
                "2. 对于不确定的信息，置为 null 或者 false；\n" +
                "2. inMyDay表示是否需要在今天完成。\n"
                ;
    }
}