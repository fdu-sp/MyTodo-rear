package com.zmark.mytodo.gpt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author ZMark
 * @date 2024/7/23 下午1:24
 */
@Component
public class OpenAIClient implements IGptClient {
    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    @Value("${openai.api.model}")
    private String openaiApiModel;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public String call(String userContent) throws IOException {
        return call("You are a helpful assistant.", userContent);
    }

    @Override
    public String call(String agentContent, String userContent) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String jsonPayload = getJsonRequest(agentContent, userContent);
        RequestBody body = RequestBody.create(jsonPayload, mediaType);

        Request request = new Request.Builder()
                .url(openaiApiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + openaiApiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private String getJsonRequest(String agentContent, String userContent) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", openaiApiModel);
        jsonObject.put("stream", false);

        // Create JSON array for messages
        JSONArray messages = new JSONArray();
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", agentContent);
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userContent);
        messages.add(systemMessage);
        messages.add(userMessage);

        jsonObject.put("messages", messages);
        jsonObject.put("max_tokens", 500);

        // Convert JSON payload to string
        return jsonObject.toJSONString();
    }
}
