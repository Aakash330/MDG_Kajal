package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class OTPInnerMessage2 {

    @SerializedName("id")
    private String id;

    @SerializedName("recipient")
    private long recipient;

    public OTPInnerMessage2(String id, long recipient) {
        this.id = id;
        this.recipient = recipient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getRecipient() {
        return recipient;
    }

    public void setRecipient(long recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "OTPInnerMessage2{" +
                "id='" + id + '\'' +
                ", recipient=" + recipient +
                '}';
    }
}
