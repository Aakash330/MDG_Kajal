package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class OTPInnerMessage {

    @SerializedName("num_parts")
    private int num_parts;

    @SerializedName("sender")
    private String sender;

    @SerializedName("content")
    private String content;

    public OTPInnerMessage(int num_parts, String sender, String content) {
        this.num_parts = num_parts;
        this.sender = sender;
        this.content = content;
    }

    public int getNum_parts() {
        return num_parts;
    }

    public void setNum_parts(int num_parts) {
        this.num_parts = num_parts;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "OTPInnerMessage{" +
                "num_parts=" + num_parts +
                ", sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
