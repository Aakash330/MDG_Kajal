package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class PlaceOrderModel {

    /*buy_id:
v_id:
self_pickup:
coupon_discount:
delivery_charge:
coupon_id:
freebie_id:*/

    /*orderId:  (20-6-22)*/

    @SerializedName("buy_id")
    private int buy_id;

    @SerializedName("v_id")
    private int v_id;

    @SerializedName("self_pickup")
    private int self_pickup;

    @SerializedName("coupon_discount")
    private float coupon_discount;

    @SerializedName("delivery_charge")
    private int delivery_charge;

    @SerializedName("coupon_id")
    private int coupon_id;

    @SerializedName("freebie_id")
    private int freebie_id;

    @SerializedName("paymentStatus")
    private int paymentStatus;

    @SerializedName("orderId")
    private String orderId;

    public PlaceOrderModel(int buy_id, int v_id, int self_pickup, float coupon_discount, int delivery_charge, int coupon_id, int freebie_id, int paymentStatus, String orderId) {
        this.buy_id = buy_id;
        this.v_id = v_id;
        this.self_pickup = self_pickup;
        this.coupon_discount = coupon_discount;
        this.delivery_charge = delivery_charge;
        this.coupon_id = coupon_id;
        this.freebie_id = freebie_id;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
    }

    public int getBuy_id() {
        return buy_id;
    }

    public void setBuy_id(int buy_id) {
        this.buy_id = buy_id;
    }

    public int getV_id() {
        return v_id;
    }

    public void setV_id(int v_id) {
        this.v_id = v_id;
    }

    public int getSelf_pickup() {
        return self_pickup;
    }

    public void setSelf_pickup(int self_pickup) {
        this.self_pickup = self_pickup;
    }

    public float getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(float coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public int getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(int delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getFreebie_id() {
        return freebie_id;
    }

    public void setFreebie_id(int freebie_id) {
        this.freebie_id = freebie_id;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "PlaceOrderModel{" +
                "buy_id=" + buy_id +
                ", v_id=" + v_id +
                ", self_pickup=" + self_pickup +
                ", coupon_discount=" + coupon_discount +
                ", delivery_charge=" + delivery_charge +
                ", coupon_id=" + coupon_id +
                ", freebie_id=" + freebie_id +
                ", paymentStatus=" + paymentStatus +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
