package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class OrderAcceptResponse {

    @SerializedName("msg")
    private String msg;

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private int error;

    public OrderAcceptResponse(String msg, int status, int error) {
        this.msg = msg;
        this.status = status;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OrderAcceptResponse{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", error=" + error +
                '}';
    }
}
