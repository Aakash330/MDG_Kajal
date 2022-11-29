package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart_CartData_Shipping {

    /*"shipping_data": {
            "homedeliver": 1,
            "cod": "0",
            "paytm": null,
            "google_pay": null,
            "phone_pay": null,
            "address": "Alamganj, Ashok Rajpath, Patna",
            "pin": "800007",
            "mob_no1": "7988821805",
            "sh_chrg_typ": 3,
            "sh_cost": null,
            "free_del_odr": 0,
            "flat_sh_cost": 0,
            "allow_self_pickup": "0"
        },*/

    @SerializedName("homedeliver")
    private int homedeliver;

    @SerializedName("cod")
    private String cod;

    @SerializedName("paytm")
    private String paytm;

    @SerializedName("google_pay")
    private String google_pay;

    @SerializedName("phone_pay")
    private String phone_pay;

    @SerializedName("address")
    private String address;

    @SerializedName("pin")
    private String pin;

    @SerializedName("mob_no1")
    private String mob_no1;

    @SerializedName("sh_chrg_typ")
    private int sh_chrg_typ;

    @SerializedName("sh_cost")
    private String sh_cost;

    @SerializedName("free_del_odr")
    private int free_del_odr;

    @SerializedName("flat_sh_cost")
    private int flat_sh_cost;

    @SerializedName("allow_self_pickup")
    private String allow_self_pickup;

    public Cart_CartData_Shipping(int homedeliver, String cod, String paytm, String google_pay, String phone_pay, String address, String pin, String mob_no1, int sh_chrg_typ, String sh_cost, int free_del_odr, int flat_sh_cost, String allow_self_pickup) {
        this.homedeliver = homedeliver;
        this.cod = cod;
        this.paytm = paytm;
        this.google_pay = google_pay;
        this.phone_pay = phone_pay;
        this.address = address;
        this.pin = pin;
        this.mob_no1 = mob_no1;
        this.sh_chrg_typ = sh_chrg_typ;
        this.sh_cost = sh_cost;
        this.free_del_odr = free_del_odr;
        this.flat_sh_cost = flat_sh_cost;
        this.allow_self_pickup = allow_self_pickup;
    }

    public int getHomedeliver() {
        return homedeliver;
    }

    public void setHomedeliver(int homedeliver) {
        this.homedeliver = homedeliver;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }

    public String getGoogle_pay() {
        return google_pay;
    }

    public void setGoogle_pay(String google_pay) {
        this.google_pay = google_pay;
    }

    public String getPhone_pay() {
        return phone_pay;
    }

    public void setPhone_pay(String phone_pay) {
        this.phone_pay = phone_pay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMob_no1() {
        return mob_no1;
    }

    public void setMob_no1(String mob_no1) {
        this.mob_no1 = mob_no1;
    }

    public int getSh_chrg_typ() {
        return sh_chrg_typ;
    }

    public void setSh_chrg_typ(int sh_chrg_typ) {
        this.sh_chrg_typ = sh_chrg_typ;
    }

    public String getSh_cost() {
        return sh_cost;
    }

    public void setSh_cost(String sh_cost) {
        this.sh_cost = sh_cost;
    }

    public int getFree_del_odr() {
        return free_del_odr;
    }

    public void setFree_del_odr(int free_del_odr) {
        this.free_del_odr = free_del_odr;
    }

    public int getFlat_sh_cost() {
        return flat_sh_cost;
    }

    public void setFlat_sh_cost(int flat_sh_cost) {
        this.flat_sh_cost = flat_sh_cost;
    }

    public String getAllow_self_pickup() {
        return allow_self_pickup;
    }

    public void setAllow_self_pickup(String allow_self_pickup) {
        this.allow_self_pickup = allow_self_pickup;
    }

    @Override
    public String toString() {
        return "Cart_CartData_Shipping{" +
                "homedeliver=" + homedeliver +
                ", cod='" + cod + '\'' +
                ", paytm='" + paytm + '\'' +
                ", google_pay='" + google_pay + '\'' +
                ", phone_pay='" + phone_pay + '\'' +
                ", address='" + address + '\'' +
                ", pin='" + pin + '\'' +
                ", mob_no1='" + mob_no1 + '\'' +
                ", sh_chrg_typ=" + sh_chrg_typ +
                ", sh_cost='" + sh_cost + '\'' +
                ", free_del_odr=" + free_del_odr +
                ", flat_sh_cost=" + flat_sh_cost +
                ", allow_self_pickup='" + allow_self_pickup + '\'' +
                '}';
    }
}
