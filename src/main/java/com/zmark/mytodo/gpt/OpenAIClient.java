package com.zmark.mytodo.gpt;

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
    public String call(String prompt) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", openaiApiModel);
        jsonObject.put("prompt", prompt);
        jsonObject.put("max_tokens", 200);

        String jsonPayload = jsonObject.toJSONString();
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
}
