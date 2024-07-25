package com.zmark.mytodo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmark.mytodo.bo.task.req.TaskExtractFromAudioReq;
import com.zmark.mytodo.bo.task.req.TaskExtractFromTextReq;
import com.zmark.mytodo.bo.task.resp.TaskExtractResp;
import com.zmark.mytodo.dto.task.TaskDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.gpt.IGptClient;
import com.zmark.mytodo.gpt.OpenAIClient;
import com.zmark.mytodo.service.api.ITaskExtractService;
import com.zmark.mytodo.service.api.ITaskService;
import com.zmark.mytodo.utils.TimeUtils;
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

    private ITaskService taskService;

    public static final String JSON_TEMPLATE = """
            {
              "title": "",
              "description": "",
              "tags": ["", ""],
              "dueTime": "",
              "remindTime": "",
              "planningFromTime": ""
              "planningToTime": "",
              "isUrgent": false,
              "isImportant": false,
              "inMyDay": false
            }
            """;

    public static final String JSON_CONSTRAINED_PROMPT = """
            1. 请确保提取的信息是准确且完整的，不要遗漏或错误提取信息；
            2. 对于不确定的信息，置为 空数组、null 或者 false；
            3. title、description、tags的语言和文字匹配；
            4. tags为字符串数组，可以为空数组；
            5. dueTime、remindTime、planningFromTime、planningToTime 的格式为 yyyy-MM-dd HH:mm:ss；
            6. inMyDay表示是否需要在今天完成；
            7. isUrgent、isImportant、inMyDay的值为 true 或 false；
            8. 请严格按照上述格式输出json格式的任务信息，不要输出其他内容。
            """;

    @Autowired
    public void setGptClient(OpenAIClient gptClient) {
        this.gptClient = gptClient;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 从文本中提取任务信息，包括任务标题、描述、标签（0-多个）、截止时间、提醒时间、规划执行时间（time to time）、是否紧急、是否重要、是否需要在今天完成、重复规则等信息
     */
    @Override
    public TaskExtractResp extractFromText(TaskExtractFromTextReq taskExtractReq) throws IOException {
        String text = taskExtractReq.getText();
        String prompt = generatePrompt(text);
        log.info("prompt: {}", prompt);
        String responseBody = gptClient.call("你是一个任务信息提取助手，专门从文本描述中提取和生成结构化的任务信息。你的主要目标是确保提取的信息准确、完整，并以预定义的JSON格式输出。", prompt);
        log.info("responseBody: {}", responseBody);
        return extractResponse(responseBody);
    }

    @Override
    public TaskDTO extractAndAddFromText(TaskExtractFromTextReq taskExtractReq) throws IOException, NewEntityException, NoDataInDataBaseException {
        TaskExtractResp taskExtractResp = extractFromText(taskExtractReq);
        return taskService.createNewTask(taskExtractResp.toTaskCreateReq());
    }


    @Override
    public TaskExtractResp extractFromAudio(TaskExtractFromAudioReq taskExtractReq) {
        // TODO
        return null;
    }

    private static String generatePrompt(String text) {
        String timeNow = TimeUtils.toString(TimeUtils.now());
        return """
                现在是%s，请从用户输入中提取任务相关的信息，包括任务标题、描述、标签（0-多个字符串）、截止时间、提醒时间、规划执行时间（time to time）、是否紧急、是否重要、是否需要在今天完成，注意其中隐含的时间信息。按json格式输出。
                ## 用户输入：
                %s
                ## json格式：
                %s
                ## 约束条件：
                %s
                """.formatted(timeNow, text, JSON_TEMPLATE, JSON_CONSTRAINED_PROMPT);
    }

    private TaskExtractResp extractResponse(String responseBody) {
        // 解析响应体
        JSONObject jsonObject = JSONObject.parseObject(responseBody);

        // 获取第一个选择
        JSONObject choice = jsonObject.getJSONArray("choices").getJSONObject(0);
        // 提取 message.content 字段
        String ansJson = choice.getJSONObject("message").getString("content").trim();

        // 去除 ``` 包裹符号
        if (ansJson.startsWith("```json") && ansJson.endsWith("```")) {
            ansJson = ansJson.substring(7, ansJson.length() - 3).trim(); // 去除开头的 ```json 和结尾的 ```
        }

        // 解析 JSON 内容为 TaskExtractResp 对象
        return TaskExtractResp.fromJson(ansJson);
    }

    public static void main(String[] args) {
        // test generatePrompt
        System.out.println(generatePrompt("提醒我下午3点去开会"));
    }
}
