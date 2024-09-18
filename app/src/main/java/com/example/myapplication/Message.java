package com.example.myapplication;

public class Message {
    public static String SEND_BY_ME = "ME";
    public static String SEND_BY_BOT = "AI";

    String message;
    String sendBy;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public Message(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }

    public static Message toMessage(String value) {
        Message retMes = new Message("", "");
        retMes.setMessage(value.substring(5));
        retMes.setSendBy(value.substring(1, 3));
        return retMes;
    }

    public String toString() {
        String retStr;
        retStr = "[" + sendBy + "]:" + message;
        return retStr;
    }

    public static String toString(String sendBy, String message) {
        String retStr;
        retStr = "[" + sendBy + "]:" + message;
        return retStr;
    }
}