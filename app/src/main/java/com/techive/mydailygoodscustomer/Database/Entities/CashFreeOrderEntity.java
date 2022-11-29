package com.techive.mydailygoodscustomer.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cashfree_table")
public class CashFreeOrderEntity {

    @PrimaryKey(autoGenerate = true)
    private int primaryKeyId;

    private int buyerId;

    private int vendorId;

    private String cf_order_id;

    private String order_id;

    private String order_amount;

    private String order_expiry_time;

    private String order_status;

    private String order_token;

    private String order_note;

    public CashFreeOrderEntity(int buyerId, int vendorId, String cf_order_id, String order_id, String order_amount, String order_expiry_time, String order_status, String order_token, String order_note) {
        this.buyerId = buyerId;
        this.vendorId = vendorId;
        this.cf_order_id = cf_order_id;
        this.order_id = order_id;
        this.order_amount = order_amount;
        this.order_expiry_time = order_expiry_time;
        this.order_status = order_status;
        this.order_token = order_token;
        this.order_note = order_note;
    }

    public int getPrimaryKeyId() {
        return primaryKeyId;
    }

    public void setPrimaryKeyId(int primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
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
        return "CashFreeOrderEntity{" +
                "primaryKeyId=" + primaryKeyId +
                ", buyerId=" + buyerId +
                ", vendorId=" + vendorId +
                ", cf_order_id='" + cf_order_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_amount='" + order_amount + '\'' +
                ", order_expiry_time='" + order_expiry_time + '\'' +
                ", order_status='" + order_status + '\'' +
                ", order_token='" + order_token + '\'' +
                ", order_note='" + order_note + '\'' +
                '}';
    }
}
