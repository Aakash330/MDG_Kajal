package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OTPResponse {

    @SerializedName("balance")
    private int balance;

    @SerializedName("batch_id")
    private String batch_id;

    @SerializedName("cost")
    private int cost;

    @SerializedName("num_messages")
    private int num_messages;

    @SerializedName("message")
    private OTPInnerMessage message;

    @SerializedName("messages")
    private List<OTPInnerMessage2> messages;

    @SerializedName("status")
    private String status;

    @SerializedName("errors")
    private List<OTPError> errors;

    public OTPResponse(int balance, String batch_id, int cost, int num_messages, OTPInnerMessage message, List<OTPInnerMessage2> messages, String status, List<OTPError> errors) {
        this.balance = balance;
        this.batch_id = batch_id;
        this.cost = cost;
        this.num_messages = num_messages;
        this.message = message;
        this.messages = messages;
        this.status = status;
        this.errors = errors;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNum_messages() {
        return num_messages;
    }

    public void setNum_messages(int num_messages) {
        this.num_messages = num_messages;
    }

    public OTPInnerMessage getMessage() {
        return message;
    }

    public void setMessage(OTPInnerMessage message) {
        this.message = message;
    }

    public List<OTPInnerMessage2> getMessages() {
        return messages;
    }

    public void setMessages(List<OTPInnerMessage2> messages) {
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OTPError> getErrors() {
        return errors;
    }

    public void setErrors(List<OTPError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "OTPResponse{" +
                "balance=" + balance +
                ", batch_id='" + batch_id + '\'' +
                ", cost=" + cost +
                ", num_messages=" + num_messages +
                ", message=" + message +
                ", messages=" + messages +
                ", status='" + status + '\'' +
                ", errors=" + errors +
                '}';
    }
}
