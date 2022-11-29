package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class SplitOrderResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public SplitOrderResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SplitOrderResponse{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
