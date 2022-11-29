package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class OTPError {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public OTPError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OTPError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
