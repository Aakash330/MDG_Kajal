package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart_CartData {

    /*"cart_data": {
        "current_page": 1,
        "data": [
            {
                "cart_id": 110,
                "prod_id": 21162,
                "name": "Bingo! Original Style Salt Sprinkled Potato Chips ",
                "image": "https://www.mydailygoods.com/admin/product_img/1626_YMTFLCUTSALRS20.jpg",
                "qty": 4,
                "totalPrice": "80",
                "saving": "0"
            }
        ],
        "coupons": [
            {
                "id": 30,
                "coupon": "10 % discount | RG68ZU2Z",
                "min_spend": 200,
                "max_discount": 0,
                "limit_per_user": 1,
                "first_run": 0,
                "monthly_run": 0
            }
        ],
        "last_page": 1,
        "total": 1
    }*/

    /*"buyerDefaultAddress": {
            "id": 8,
            "u_id": 126,
            "name": "Aamir",
            "lname": "Hoda",
            "mobile": "7988821805",
            "address": "Alamganj, Ashok Rajpath, Patna, Bihar",
            "landmark": null,
            "pincode": "800007",
            "state": "Bihar",
            "city": "Patna"
        }*/

    @SerializedName("data")
    private List<Cart_CartData_Data> data;

    @SerializedName("total_price")
    private String total_price;

    @SerializedName("total_savings")
    private String total_savings;

    @SerializedName("shipping_data")
    private Cart_CartData_Shipping shipping_data;

    @SerializedName("vendors_accounts_data")
    private Cart_CartData_VendorAcct vendors_accounts_data;

    @SerializedName("coupons")
    private List<Cart_CartData_Coupons> coupons;

    @SerializedName("freebies")
    private List<Cart_CartData_Freebies> freebies;

    @SerializedName("buyerDefaultAddress")
    private Cart_CartData_BuyerAddress buyerDefaultAddress;

    public Cart_CartData(/*int current_page,*/ List<Cart_CartData_Data> data, String total_price, String total_savings, Cart_CartData_Shipping shipping_data, Cart_CartData_VendorAcct vendors_accounts_data, List<Cart_CartData_Coupons> coupons, List<Cart_CartData_Freebies> freebies, /*int last_page, int total,*/ Cart_CartData_BuyerAddress buyerDefaultAddress) {
        this.data = data;
        this.total_price = total_price;
        this.total_savings = total_savings;
        this.shipping_data = shipping_data;
        this.vendors_accounts_data = vendors_accounts_data;
        this.coupons = coupons;
        this.freebies = freebies;
        this.buyerDefaultAddress = buyerDefaultAddress;
    }

    public List<Cart_CartData_Data> getData() {
        return data;
    }

    public void setData(List<Cart_CartData_Data> data) {
        this.data = data;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_savings() {
        return total_savings;
    }

    public void setTotal_savings(String total_savings) {
        this.total_savings = total_savings;
    }

    public Cart_CartData_Shipping getShipping_data() {
        return shipping_data;
    }

    public void setShipping_data(Cart_CartData_Shipping shipping_data) {
        this.shipping_data = shipping_data;
    }

    public Cart_CartData_VendorAcct getVendors_accounts_data() {
        return vendors_accounts_data;
    }

    public void setVendors_accounts_data(Cart_CartData_VendorAcct vendors_accounts_data) {
        this.vendors_accounts_data = vendors_accounts_data;
    }

    public List<Cart_CartData_Coupons> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Cart_CartData_Coupons> coupons) {
        this.coupons = coupons;
    }

    public List<Cart_CartData_Freebies> getFreebies() {
        return freebies;
    }

    public void setFreebies(List<Cart_CartData_Freebies> freebies) {
        this.freebies = freebies;
    }

    public Cart_CartData_BuyerAddress getBuyerDefaultAddress() {
        return buyerDefaultAddress;
    }

    public void setBuyerDefaultAddress(Cart_CartData_BuyerAddress buyerDefaultAddress) {
        this.buyerDefaultAddress = buyerDefaultAddress;
    }

    @Override
    public String toString() {
        return "Cart_CartData{" +
                ", data=" + data +
                ", total_price='" + total_price + '\'' +
                ", total_savings='" + total_savings + '\'' +
                ", shipping_data=" + shipping_data +
                ", vendors_accounts_data=" + vendors_accounts_data +
                ", coupons=" + coupons +
                ", freebies=" + freebies +
                ", buyerDefaultAddress=" + buyerDefaultAddress +
                '}';
    }
}
