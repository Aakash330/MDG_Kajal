package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory_Data {

    /*{
            "id": 43,
            "invoiceId": "mdg_1261653892605",
            "b_id": "126",
            "vender_id": "134",
            "total": 219,
            "quantity": 2,
            "total_saving": 19,
            "delivery_change": 0,
            "discount": 19,
            "coupon_id": "36",
            "freebie_id": null,
            "self_pickup": 0,
            "buy_addressId": null,
            "order_status": "0",
            "online_payment": "0",
            "status": null,
            "message": null,
            "delivery_date": null,
            "created_at": "2022-05-30 06:36:45",
            "updated_at": null,
            "store_name": "Ashwani Store"
            "image": "1580_PANIPURI.jpg",
            "imagepath": "https://www.mydailygoods.com/admin/product_img/",
            "products": [
                {
                    "orderID": "mdg_1261653892605",
                    "qty": 1,
                    "new_qty": 1,
                    "sale_price": 55,
                    "totalPrice": 55,
                    "name": "PANIPURI Masala",
                    "image": "1580_PANIPURI.jpg",
                    "product_id": 32479
                },
                {
                    "orderID": "mdg_1261653892605",
                    "qty": 1,
                    "new_qty": 1,
                    "sale_price": 133,
                    "totalPrice": 133,
                    "name": "Tata Sampann 100% Organic Toor Dal",
                    "image": "1616_toordal.jpg",
                    "product_id": 32515
                }
            ],
            "buyerAddress": null
        }*/

    /*buyerAddress":{"id":9,"firstname":"Bruce","lastname":"Wayne","mobile":"7896541230","address":"Street Address","landmark":"Landmark","pincode":"145698","state":"Delhi","city":"Delhi"}}],"error":200}*/

    @SerializedName("id")
    private int id;

    @SerializedName("invoiceId")
    private String invoiceId;

    @SerializedName("b_id")
    private String b_id;

    @SerializedName("vender_id")
    private String vender_id;

    @SerializedName("total")
    private float total;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("total_saving")
    private float total_saving;

    @SerializedName("delivery_change")
    private float delivery_change;

    @SerializedName("discount")
    private float discount;

    @SerializedName("coupon_id")
    private String coupon_id;

    @SerializedName("freebie_id")
    private String freebie_id;

    @SerializedName("self_pickup")
    private int self_pickup;

    @SerializedName("buy_addressId")
    private String buy_addressId;

    @SerializedName("order_status")
    private String order_status;

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    @SerializedName("refund_status")
    private String refund_status;


    @SerializedName("online_payment")
    private String online_payment;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("delivery_date")
    private String delivery_date;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("image")
    private String image;

    @SerializedName("imagepath")
    private String imagepath;

    @SerializedName("products")
    private List<OrderHistory_Data_Products> products;

    @SerializedName("buyerAddress")
    private OrderHistory_Data_BuyerAddress buyerAddress;

    public OrderHistory_Data(int id, String invoiceId, String b_id, String vender_id, float total, int quantity, float total_saving, float delivery_change, float discount, String coupon_id, String freebie_id, int self_pickup, String buy_addressId, String order_status, String online_payment, String status, String message, String delivery_date, String created_at, String updated_at, String store_name, String image, String imagepath, List<OrderHistory_Data_Products> products, OrderHistory_Data_BuyerAddress buyerAddress) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.b_id = b_id;
        this.vender_id = vender_id;
        this.total = total;
        this.quantity = quantity;
        this.total_saving = total_saving;
        this.delivery_change = delivery_change;
        this.discount = discount;
        this.coupon_id = coupon_id;
        this.freebie_id = freebie_id;
        this.self_pickup = self_pickup;
        this.buy_addressId = buy_addressId;
        this.order_status = order_status;
        this.online_payment = online_payment;
        this.status = status;
        this.message = message;
        this.delivery_date = delivery_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.store_name = store_name;
        this.image = image;
        this.imagepath = imagepath;
        this.products = products;
        this.buyerAddress = buyerAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getVender_id() {
        return vender_id;
    }

    public void setVender_id(String vender_id) {
        this.vender_id = vender_id;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal_saving() {
        return total_saving;
    }

    public void setTotal_saving(float total_saving) {
        this.total_saving = total_saving;
    }

    public float getDelivery_change() {
        return delivery_change;
    }

    public void setDelivery_change(float delivery_change) {
        this.delivery_change = delivery_change;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getFreebie_id() {
        return freebie_id;
    }

    public void setFreebie_id(String freebie_id) {
        this.freebie_id = freebie_id;
    }

    public int getSelf_pickup() {
        return self_pickup;
    }

    public void setSelf_pickup(int self_pickup) {
        this.self_pickup = self_pickup;
    }

    public String getBuy_addressId() {
        return buy_addressId;
    }

    public void setBuy_addressId(String buy_addressId) {
        this.buy_addressId = buy_addressId;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOnline_payment() {
        return online_payment;
    }

    public void setOnline_payment(String online_payment) {
        this.online_payment = online_payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public List<OrderHistory_Data_Products> getProducts() {
        return products;
    }

    public void setProducts(List<OrderHistory_Data_Products> products) {
        this.products = products;
    }

    public OrderHistory_Data_BuyerAddress getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(OrderHistory_Data_BuyerAddress buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    @Override
    public String toString() {
        return "OrderHistory_Data{" +
                "id=" + id +
                ", invoiceId='" + invoiceId + '\'' +
                ", b_id='" + b_id + '\'' +
                ", vender_id='" + vender_id + '\'' +
                ", total=" + total +
                ", quantity=" + quantity +
                ", total_saving=" + total_saving +
                ", delivery_change=" + delivery_change +
                ", discount=" + discount +
                ", coupon_id='" + coupon_id + '\'' +
                ", freebie_id='" + freebie_id + '\'' +
                ", self_pickup=" + self_pickup +
                ", buy_addressId='" + buy_addressId + '\'' +
                ", order_status='" + order_status + '\'' +
                ", refund_status='" +refund_status + '\'' +
                ", online_payment='" + online_payment + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", delivery_date='" + delivery_date + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", store_name='" + store_name + '\'' +
                ", image='" + image + '\'' +
                ", imagepath='" + imagepath + '\'' +
                ", products=" + products +
                ", buyerAddress=" + buyerAddress +
                '}';
    }
}
