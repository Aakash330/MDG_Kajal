package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class CashFreeOrder {

    /*{
    "cf_order_id": 514853406,
    "order_id": "order_id",
    "entity": "order",
    "order_currency": "INR",
    "order_amount": 1.00,
    "order_expiry_time": "2021-08-22T13:09:23+05:30",
    "customer_details": {
        "customer_id": "customer_id",
        "customer_name": "customer_name",
        "customer_email": "customer_email",
        "customer_phone": "customer_phone"
    },
    "order_meta": {
        "return_url": "https://test.cashfree.com"
        "notify_url": "https://test.cashfree.com"
        "payment_methods": null
    },
    "settlements": {
        "url": "settlements_url"
    },
    "payments": {
        "url": "payments_url"
    },
    "refunds": {
        "url": "refunds_url"
    },
    "order_status": "ACTIVE",
    "order_token": "W8zVBhADaw2EDP7crdyM", // This is the order_token that has to be sent to the SDK
    "order_note": "some order note here",
    "payment_link": "https://cashfree.com/pgbillpaywebapp/#W8zVBhADaw2EDP7crdyM" // This link opens the payment page provided by Cashfree
}*/

    @SerializedName("cf_order_id")
    private String cf_order_id;

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("entity")
    private String entity;

    @SerializedName("order_currency")
    private String order_currency;

    @SerializedName("order_amount")
    private float order_amount;

    @SerializedName("order_expiry_time")
    private String order_expiry_time;

    @SerializedName("order_status")
    private String order_status;

    @SerializedName("order_token")
    private String order_token;

    @SerializedName("order_note")
    private String order_note;

    public CashFreeOrder(String cf_order_id, String order_id, String entity, String order_currency, float order_amount, String order_expiry_time, String order_status, String order_token, String order_note) {
        this.cf_order_id = cf_order_id;
        this.order_id = order_id;
        this.entity = entity;
        this.order_currency = order_currency;
        this.order_amount = order_amount;
        this.order_expiry_time = order_expiry_time;
        this.order_status = order_status;
        this.order_token = order_token;
        this.order_note = order_note;
    }

    public String getCf_order_id() {
        return cf_order_id;
    }

    public void setCf_order_id(String cf_order_id) {
        this.cf_order_id = cf_order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getOrder_currency() {
        return order_currency;
    }

    public void setOrder_currency(String order_currency) {
        this.order_currency = order_currency;
    }

    public float getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(float order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_expiry_time() {
        return order_expiry_time;
    }

    public void setOrder_expiry_time(String order_expiry_time) {
        this.order_expiry_time = order_expiry_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_token() {
        return order_token;
    }

    public void setOrder_token(String order_token) {
        this.order_token = order_token;
    }

    public String getOrder_note() {
        return order_note;
    }

    public void setOrder_note(String order_note) {
        this.order_note = order_note;
    }

    @Override
    public String toString() {
        return "CashFreeOrder{" +
                "cf_order_id='" + cf_order_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", entity='" + entity + '\'' +
                ", order_currency='" + order_currency + '\'' +
                ", order_amount=" + order_amount +
                ", order_expiry_time='" + order_expiry_time + '\'' +
                ", order_status='" + order_status + '\'' +
                ", order_token='" + order_token + '\'' +
                ", order_note='" + order_note + '\'' +
                '}';
    }
}
