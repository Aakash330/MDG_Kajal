package com.techive.mydailygoodscustomer.cashfreeupdatestesting;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponse {

    @SerializedName("cf_order_id")
    @Expose
    private Integer cfOrderId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("customer_details")
    @Expose
    private CustomerDetails customerDetails;
    @SerializedName("entity")
    @Expose
    private String entity;
    @SerializedName("order_amount")
    @Expose
    private Integer orderAmount;
    @SerializedName("order_currency")
    @Expose
    private String orderCurrency;
    @SerializedName("order_expiry_time")
    @Expose
    private String orderExpiryTime;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_meta")
    @Expose
    private OrderMeta orderMeta;
    @SerializedName("order_note")
    @Expose
    private Object orderNote;
    @SerializedName("order_splits")
    @Expose
    private List<Object> orderSplits = null;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_tags")
    @Expose
    private Object orderTags;
    @SerializedName("order_token")
    @Expose
    private String orderToken;
    @SerializedName("payment_link")
    @Expose
    private String paymentLink;
    @SerializedName("payments")
    @Expose
    private Payments payments;
    @SerializedName("refunds")
    @Expose
    private Refunds refunds;
    @SerializedName("settlements")
    @Expose
    private Settlements settlements;

    public Integer getCfOrderId() {
        return cfOrderId;
    }

    public void setCfOrderId(Integer cfOrderId) {
        this.cfOrderId = cfOrderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public String getOrderExpiryTime() {
        return orderExpiryTime;
    }

    public void setOrderExpiryTime(String orderExpiryTime) {
        this.orderExpiryTime = orderExpiryTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderMeta getOrderMeta() {
        return orderMeta;
    }

    public void setOrderMeta(OrderMeta orderMeta) {
        this.orderMeta = orderMeta;
    }

    public Object getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(Object orderNote) {
        this.orderNote = orderNote;
    }

    public List<Object> getOrderSplits() {
        return orderSplits;
    }

    public void setOrderSplits(List<Object> orderSplits) {
        this.orderSplits = orderSplits;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Object getOrderTags() {
        return orderTags;
    }

    public void setOrderTags(Object orderTags) {
        this.orderTags = orderTags;
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public Refunds getRefunds() {
        return refunds;
    }

    public void setRefunds(Refunds refunds) {
        this.refunds = refunds;
    }

    public Settlements getSettlements() {
        return settlements;
    }

    public void setSettlements(Settlements settlements) {
        this.settlements = settlements;
    }


    public class CustomerDetails {

        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("customer_email")
        @Expose
        private String customerEmail;
        @SerializedName("customer_phone")
        @Expose
        private String customerPhone;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

    }


    public class OrderMeta {

        @SerializedName("return_url")
        @Expose
        private Object returnUrl;
        @SerializedName("notify_url")
        @Expose
        private String notifyUrl;
        @SerializedName("payment_methods")
        @Expose
        private Object paymentMethods;

        public Object getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(Object returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public Object getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(Object paymentMethods) {
            this.paymentMethods = paymentMethods;
        }

    }

    public class Payments {

        @SerializedName("url")
        @Expose
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public class Refunds {

        @SerializedName("url")
        @Expose
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public class Settlements {

        @SerializedName("url")
        @Expose
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}