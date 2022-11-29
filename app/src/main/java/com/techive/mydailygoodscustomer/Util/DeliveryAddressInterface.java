package com.techive.mydailygoodscustomer.Util;

import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;

public interface DeliveryAddressInterface {

    void updateDefaultDeliveryAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress);

    void displayEditedAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress);
}
