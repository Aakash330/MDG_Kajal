package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse {

    @SerializedName("msg")
    private String msg;

    @SerializedName("prod_id")
    private String prod_id;

    @SerializedName("error")
    private int error;

    public GeneralResponse(String msg, String prod_id, int error) {
        this.msg = msg;
        this.prod_id = prod_id;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "msg='" + msg + '\'' +
                ", prod_id='" + prod_id + '\'' +
                ", error=" + error +
                '}';
    }
}
