package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenAIManager {

    private static final String BASE_URL = "https://api.openai-proxy.org/v1";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private static final String API_KEY = "sk-Qp3T1iPXsV5jg839LqGuQ2cmMK1rHL0XuZVbKPkxvn0kSGaV"; // Your API key
    private static final int RETRY_TIMES = 12;
    private static Integer INDEX = -1;

    public OpenAIManager() {

        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }


    public void queryGPTV2(String prompt, GPTV2ResponseCallback callback) {

        String url = BASE_URL + "/chat/completions";

        String json = "{\"model\":\"gpt-3.5-turbo\"," +
                "\"messages\":[{\"role\":\"user\",\"content\":\""
                + prompt
                + "\"}],\"max_tokens\":1024,\"top_p\":1,\"temperature\":0.5,\"frequency_penalty\":0,\"presence_penalty\":0}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(e); // 调用回调函数通知发生异常
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("END");
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println("Response: " + responseData);
                    // 解析 JSON 数据
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                    JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
                    String content = "";
                    // 遍历 choices 数组
                    for (JsonElement element : choicesArray) {
                        JsonObject choiceObject = element.getAsJsonObject();
                        int index = choiceObject.get("index").getAsInt();
                        JsonObject messageObject = choiceObject.getAsJsonObject("message");
                        String role = messageObject.get("role").getAsString();
                        String content1 = messageObject.get("content").getAsString();
                        String finishReason = choiceObject.get("finish_reason").getAsString();

                        System.out.println("Index: " + index);
                        INDEX = index;
                        System.out.println("Role: " + role);
                        System.out.println("Content: " + content1);
                        content = content1;
                        System.out.println("Finish Reason: " + finishReason);
                    }
//                    System.out.println("END");
                    callback.onSuccess(INDEX, content);
                } else {
                    System.out.println("Request failed: " + response.code() + " - " + response.message());
                }
            }
        });
    }

    // 定义Message和CompletionResponse类以匹配OpenAI API结构
//    private static class Message {
//
//        public void setRole(String role) {
//        }
//
//        public void setContent(String content) {
//        }
//
//        // Getter & Setter methods...
//    }
//    public void queryGPTV2(String prompt, GPTV2ResponseCallback callback) throws IOException {
//
//        MediaType jsonMediaType = MediaType.get("application/json; charset=utf-8");
//        List<Message> messages = new ArrayList<>();
//        Message userMessage = new Message();
//        userMessage.setRole("user");
//        userMessage.setContent(prompt);
//        messages.add(userMessage);
//
//        String requestBodyJson = new Gson().toJson(messages); // 假设你已经引入了Gson库
//
//        RequestBody requestBody = RequestBody.create(jsonMediaType, requestBodyJson);
//        String url = BASE_URL + "/chat/completions";
//
//        for (int retry = 0; retry < RETRY_TIMES; retry++) {
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Authorization", "Bearer " + API_KEY)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    e.printStackTrace();
//                    callback.onFailure(e); // 调用回调函数通知发生异常
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    System.out.println("END");
//                    if (response.isSuccessful()) {
//                        assert response.body() != null;
//                        String responseData = response.body().string();
//                        System.out.println("Response: " + responseData);
//                        // 解析 JSON 数据
//                        Gson gson = new Gson();
//                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
//                        JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
//                        String content = "";
//                        // 遍历 choices 数组
//                        for (JsonElement element : choicesArray) {
//                            JsonObject choiceObject = element.getAsJsonObject();
//                            int index = choiceObject.get("index").getAsInt();
//                            JsonObject messageObject = choiceObject.getAsJsonObject("message");
//                            String role = messageObject.get("role").getAsString();
//                            String content1 = messageObject.get("content").getAsString();
//                            String finishReason = choiceObject.get("finish_reason").getAsString();
//
//                            System.out.println("Index: " + index);
//                            INDEX = index;
//                            System.out.println("Role: " + role);
//                            System.out.println("Content: " + content1);
//                            content = content1;
//                            System.out.println("Finish Reason: " + finishReason);
//                        }
////                    System.out.println("END");
//                        callback.onSuccess(INDEX, content);
//                    } else {
//                        System.out.println("Request failed: " + response.code() + " - " + response.message());
//                    }
//                }
//            });
//        }
//
//        throw new IOException("Failed to get a successful response after maximum retries.");
//    }
}
