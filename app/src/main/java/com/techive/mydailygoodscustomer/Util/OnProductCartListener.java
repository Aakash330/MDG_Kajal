package com.techive.mydailygoodscustomer.Util;

public interface OnProductCartListener {

    void addProductToCart(int productId, int qty);

    void removeProductFromCart(int productId, int qty);

    void productClicked(int productId);

}
