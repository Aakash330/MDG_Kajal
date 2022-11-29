package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart_CartData_VendorAcct {

     /*"vendors_accounts_data": {
            "id": 1,
            "merchant_id": "merVId_1",
            "v_id": 121,
            "email": "email123@gmail.com",
            "status": 1,
            "accountNumber": null,
            "bankAccountHolder": null,
            "ifsc": null,
            "vpa": "9729805425@ybl",
            "upiAccountHolder": "Aamir Hoda",
            "phone": "7988821806",
            "name": "amir-megamart",
            "settlementCycleId": "2",
            "accountType": 0,
            "percentage_discount": 95,
            "created_at": "2022-05-14 04:50:17",
            "updated_at": "2022-05-16 05:31:39"
        },*/

    @SerializedName("id")
    private int id;

    @SerializedName("merchant_id")
    private String merchant_id;

    @SerializedName("v_id")
    private int v_id;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private int status;

    @SerializedName("accountNumber")
    private String accountNumber;

    @SerializedName("bankAccountHolder")
    private String bankAccountHolder;

    @SerializedName("ifsc")
    private String ifsc;

    @SerializedName("vpa")
    private String vpa;

    @SerializedName("upiAccountHolder")
    private String upiAccountHolder;

    @SerializedName("phone")
    private String phone;

    @SerializedName("name")
    private String name;

    @SerializedName("settlementCycleId")
    private String settlementCycleId;

    @SerializedName("accountType")
    private int accountType;

    @SerializedName("percentage_discount")
    private int percentage_discount;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public Cart_CartData_VendorAcct(int id, String merchant_id, int v_id, String email, int status, String accountNumber, String bankAccountHolder, String ifsc, String vpa, String upiAccountHolder, String phone, String name, String settlementCycleId, int accountType, int percentage_discount, String created_at, String updated_at) {
        this.id = id;
        this.merchant_id = merchant_id;
        this.v_id = v_id;
        this.email = email;
        this.status = status;
        this.accountNumber = accountNumber;
        this.bankAccountHolder = bankAccountHolder;
        this.ifsc = ifsc;
        this.vpa = vpa;
        this.upiAccountHolder = upiAccountHolder;
        this.phone = phone;
        this.name = name;
        this.settlementCycleId = settlementCycleId;
        this.accountType = accountType;
        this.percentage_discount = percentage_discount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public int getV_id() {
        return v_id;
    }

    public void setV_id(int v_id) {
        this.v_id = v_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAccountHolder() {
        return bankAccountHolder;
    }

    public void setBankAccountHolder(String bankAccountHolder) {
        this.bankAccountHolder = bankAccountHolder;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

    public String getUpiAccountHolder() {
        return upiAccountHolder;
    }

    public void setUpiAccountHolder(String upiAccountHolder) {
        this.upiAccountHolder = upiAccountHolder;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSettlementCycleId() {
        return settlementCycleId;
    }

    public void setSettlementCycleId(String settlementCycleId) {
        this.settlementCycleId = settlementCycleId;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getPercentage_discount() {
        return percentage_discount;
    }

    public void setPercentage_discount(int percentage_discount) {
        this.percentage_discount = percentage_discount;
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

    @Override
    public String toString() {
        return "Cart_CartData_VendorAcct{" +
                "id=" + id +
                ", merchant_id='" + merchant_id + '\'' +
                ", v_id=" + v_id +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", accountNumber='" + accountNumber + '\'' +
                ", bankAccountHolder='" + bankAccountHolder + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", vpa='" + vpa + '\'' +
                ", upiAccountHolder='" + upiAccountHolder + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", settlementCycleId='" + settlementCycleId + '\'' +
                ", accountType=" + accountType +
                ", percentage_discount=" + percentage_discount +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
