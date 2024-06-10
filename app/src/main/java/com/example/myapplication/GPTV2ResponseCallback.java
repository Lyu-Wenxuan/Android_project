package com.example.myapplication;

public interface GPTV2ResponseCallback {
    void onSuccess(Integer integer, String content);

    void onFailure(Exception e);
}