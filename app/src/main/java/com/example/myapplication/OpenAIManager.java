package com.example.myapplication;

import android.util.Log;

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

    private static final String BASE_URL = "https://api.openai-proxy.org";
    private static final String BASE_URL_2 = "https://open.bigmodel.cn/api/paas/v4";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private static final String API_KEY = "sk-4KOEl7suXmazckBdkjnwRsS7tJ92o96azpIBz6rnIYtG3Bu2"; // Your API key
    private static final String API_KEY_2 = ""; // Your API key

    private static final int RETRY_TIMES = 12;
    private static Integer INDEX = -1;

    public OpenAIManager() {

        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }
    public void queryGPTV3(String prompt, GPTV2ResponseCallback callback) {

        String url = BASE_URL_2 + "/chat/completions";

        String json = "{\"model\":\"glm-3-turbo\"," +
                "\"messages\":[{\"role\":\"user\",\"content\":\""
                + prompt
                + "\"}]}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY_2)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
                Log.e("CONTENT:", e.getMessage());
                callback.onFailure(e);
                // 调用回调函数通知发生异常

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("END");

                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.e("CONTENT_RESPONSE:", responseData);
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
//
//    public void queryGPTV2(String prompt, GPTV2ResponseCallback callback) {
//
//        String url = BASE_URL + "/chat/completions";
//
//        String json = "{\"model\":\"gpt-3.5-turbo\"," +
//                "\"messages\":[{\"role\":\"user\",\"content\":\""
//                + prompt
//                + "\"}],\"max_tokens\":1024,\"top_p\":1,\"temperature\":0.5,\"frequency_penalty\":0,\"presence_penalty\":0}";
//
//        RequestBody body = RequestBody.create(json, JSON);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
////                e.printStackTrace();
//                Log.e("CONTENT:", e.getMessage());
//                callback.onFailure(e);
//                // 调用回调函数通知发生异常
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("END");
//                Log.e("CONTENT:", response.body().string());
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    System.out.println("Response: " + responseData);
//                    // 解析 JSON 数据
//                    Gson gson = new Gson();
//                    JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
//                    JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
//                    String content = "";
//                    // 遍历 choices 数组
//                    for (JsonElement element : choicesArray) {
//                        JsonObject choiceObject = element.getAsJsonObject();
//                        int index = choiceObject.get("index").getAsInt();
//                        JsonObject messageObject = choiceObject.getAsJsonObject("message");
//                        String role = messageObject.get("role").getAsString();
//                        String content1 = messageObject.get("content").getAsString();
//                        String finishReason = choiceObject.get("finish_reason").getAsString();
//
//                        System.out.println("Index: " + index);
//                        INDEX = index;
//                        System.out.println("Role: " + role);
//                        System.out.println("Content: " + content1);
//                        content = content1;
//                        System.out.println("Finish Reason: " + finishReason);
//                    }
////                    System.out.println("END");
//                    callback.onSuccess(INDEX, content);
//                } else {
//                    System.out.println("Request failed: " + response.code() + " - " + response.message());
//                }
//            }
//        });
//    }

}
