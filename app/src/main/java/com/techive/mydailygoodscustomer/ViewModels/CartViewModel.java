package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.CartCouponRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.CartFreebieProductsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.CartFreebieRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.CartRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptCartFragmentResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.SplitOrderResponse;
import com.techive.mydailygoodscustomer.Models.Cart;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Coupons;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.CashFreeOrder;
import com.techive.mydailygoodscustomer.Models.PlaceOrderModel;
import com.techive.mydailygoodscustomer.Repositories.CartRepository;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CartViewModel extends AndroidViewModel {
    private static final String TAG = "CartViewModel";

    private CartRepository cartRepository;

    public CartRecyclerViewAdapter cartRecyclerViewAdapter;
    public CartCouponRecyclerViewAdapter cartCouponRecyclerViewAdapter;
    public CartFreebieProductsRecyclerViewAdapter cartFreebieProductsRecyclerViewAdapter;
    public CartFreebieRecyclerViewAdapter cartFreebieRecyclerViewAdapter;

    private MutableLiveData<Cart> cartMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> placeOrderMutableLiveData;
    private MutableLiveData<CashFreeOrder> cashFreeOrderMutableLiveData;
    private MutableLiveData<SplitOrderResponse> splitOrderResponseMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;
    private MutableLiveData<OrderAcceptCartFragmentResponse> orderAcceptCartFragmentResponseMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;


    //STORING CART DATA SEPARATELY, SO AS TO HANDLE FAILURES IN CARTMUTABLELIVEDATA.
    private Cart cart;

    public String selectedCouponType = "", cashfreeOrderId = "", cashfreeOrderToken = "";
    public int selectedCouponAmount, selectedCouponMaxDiscount, selectedCouponMinSpend, selectedCouponId,
            freebieMinCartValue, selectedFreebieId, toBeAddedProductId, toBeAddedProductQty;
    public float appliedCouponDiscount;

    public HashMap<Integer, Integer> toBeNotifiedProdIdQtyHashMap;

    public boolean codPayment = false;

    public CartViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "CartViewModel: fired!");

        initCartViewModel();
    }

    private void initCartViewModel() {
        Log.i(TAG, "initCartViewModel: fired!");

        if (cartRepository != null) {
            Log.i(TAG, "initCartViewModel: Cart Repository already initialized in CartViewModel.");
            return;
        }

        cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());
//        cartRepository.initCartRepository();

        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(getApplication().getApplicationContext());
        cartCouponRecyclerViewAdapter = new CartCouponRecyclerViewAdapter();
        cartFreebieProductsRecyclerViewAdapter = new CartFreebieProductsRecyclerViewAdapter(getApplication().getApplicationContext());
        cartFreebieRecyclerViewAdapter = new CartFreebieRecyclerViewAdapter();

        Log.i(TAG, "initCartViewModel: BEFORE, cartMutableLiveData: " + cartMutableLiveData);
        cartMutableLiveData = cartRepository.getCartMutableLiveData();
        Log.i(TAG, "initCartViewModel: AFTER, cartMutableLiveData: " + cartMutableLiveData);
        addToCartMutableLiveData = cartRepository.getAddToCartMutableLiveData();
        removeFromCartMutableLiveData = cartRepository.getRemoveFromCartMutableLiveData();
        completelyRemoveFromCartMutableLiveData = cartRepository.getCompletelyRemoveFromCartMutableLiveData();
        placeOrderMutableLiveData = cartRepository.getPlaceOrderMutableLiveData();
        cashFreeOrderMutableLiveData = cartRepository.getCashFreeOrderMutableLiveData();
        splitOrderResponseMutableLiveData = cartRepository.getSplitOrderResponseMutableLiveData();
        orderAcceptResponseMutableLiveData = cartRepository.getOrderAcceptResponseMutableLiveData();
        orderAcceptCartFragmentResponseMutableLiveData = cartRepository.getOrderAcceptCartFragmentResponseMutableLiveData();//@kajal 11_16_22
        prodAddAcceptResponseMutableLiveData = cartRepository.getProdAddAcceptResponseMutableLiveData();

        toBeNotifiedProdIdQtyHashMap = new HashMap<>();
    }

    /* ==========================GETTERS - START========================== */
    public MutableLiveData<Cart> getCartMutableLiveData() {
        return cartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getAddToCartMutableLiveData() {
        return addToCartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getRemoveFromCartMutableLiveData() {
        return removeFromCartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getCompletelyRemoveFromCartMutableLiveData() {
        return completelyRemoveFromCartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getPlaceOrderMutableLiveData() {
        return placeOrderMutableLiveData;
    }

    public MutableLiveData<CashFreeOrder> getCashFreeOrderMutableLiveData() {
        return cashFreeOrderMutableLiveData;
    }

    public MutableLiveData<SplitOrderResponse> getSplitOrderResponseMutableLiveData() {
        return splitOrderResponseMutableLiveData;
    }

    public MutableLiveData<OrderAcceptResponse> getOrderAcceptResponseMutableLiveData() {
        return orderAcceptResponseMutableLiveData;
    }

    public MutableLiveData<OrderAcceptCartFragmentResponse> getOrderAcceptCartFragmentResponseMutableLiveData() {
        return orderAcceptCartFragmentResponseMutableLiveData;
    }//@kajal 11_16_22

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }
    /* ==========================GETTERS - END========================== */

    /* SETTERS - START */
    public void setAddToCartMutableLiveData(GeneralResponse addToCart) {
        cartRepository.setAddToCartMutableLiveData(addToCart);
    }

    public void setRemoveFromCartMutableLiveData(GeneralResponse removeFromCart) {
        cartRepository.setRemoveFromCartMutableLiveData(removeFromCart);
    }

    public void setCompletelyRemoveFromCartMutableLiveData(GeneralResponse completelyRemoveFromCart) {
        cartRepository.setCompletelyRemoveFromCartMutableLiveData(completelyRemoveFromCart);
    }

    public void setCashFreeOrderMutableLiveData(CashFreeOrder cashFreeOrder) {
        cartRepository.setCashFreeOrderMutableLiveData(cashFreeOrder);
    }

    public void setPlaceOrderMutableLiveData(GeneralResponse placeOrder) {
        cartRepository.setPlaceOrderMutableLiveData(placeOrder);
    }

    public void setSplitOrderResponseMutableLiveData(SplitOrderResponse splitOrderResponse) {
        cartRepository.setSplitOrderResponseMutableLiveData(splitOrderResponse);
    }

    public void setOrderAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setOrderAcceptResponseMutableLiveData(orderAcceptResponse);
    }

    public void setCartMutableLiveData(Cart cart) {
        cartRepository.setCartMutableLiveData(cart);
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setProdAddAcceptResponseMutableLiveData(orderAcceptResponse);
    }
    /* SETTERS - END */

    public Cart_CartData_Data updateCartDataUponAdd(int productId) {
        Log.i(TAG, "updateCartDataUponAdd: fired! productId: " + productId);

        for (int i = 0; i < cart.getCart_data().getData().size(); i++) {
            if (cart.getCart_data().getData().get(i).getProd_id() == productId) {

                Log.i(TAG, "updateCartDataUponAdd: BEFORE, cart.getCart_data().getData().get(i): "
                        + cart.getCart_data().getData().get(i));
                int totalQty = cart.getCart_data().getData().get(i).getQty();
                Log.i(TAG, "updateCartDataUponAdd: totalQty: " + totalQty);
                float totalPriceFloat = Float.parseFloat(cart.getCart_data().getData().get(i).getTotalPrice());
                Log.i(TAG, "updateCartDataUponAdd: totalPriceFloat: " + totalPriceFloat);
                float singlePriceFloat = (totalPriceFloat / totalQty);
                Log.i(TAG, "updateCartDataUponAdd: singlePriceFloat: " + singlePriceFloat);

                //UPDATING TOTAL PRICE & TOTAL SAVING.
                float savingsFloat = Float.parseFloat(cart.getCart_data().getData().get(i).getSaving());
                float singleSavingFloat = savingsFloat / totalQty;
                cart.getCart_data().getData().get(i).setSaving(String.valueOf((savingsFloat + singleSavingFloat)));

                cart.getCart_data().getData().get(i).setTotalPrice(String.valueOf((totalPriceFloat + singlePriceFloat)));
                cart.getCart_data().getData().get(i).setQty(++totalQty);
                Log.i(TAG, "updateCartDataUponAdd: AFTER, cart.getCart_data().getData().get(i): "
                        + cart.getCart_data().getData().get(i));

                float totalSavingsFloat = Float.parseFloat(cart.getCart_data().getTotal_savings());
                float grossTotalPriceFloat = Float.parseFloat(cart.getCart_data().getTotal_price());
                cart.getCart_data().setTotal_price(String.valueOf((grossTotalPriceFloat + singlePriceFloat)));
                cart.getCart_data().setTotal_savings(String.valueOf((totalSavingsFloat + singleSavingFloat)));

                return cart.getCart_data().getData().get(i);
            }
        }
        return null;
    }

    public Cart_CartData_Data updateCartDataUponRemove(int productId) {
        Log.i(TAG, "updateCartDataUponRemove: fired! productId: " + productId);

        for (int i = 0; i < cart.getCart_data().getData().size(); i++) {
            if (cart.getCart_data().getData().get(i).getProd_id() == productId) {

                Log.i(TAG, "updateCartDataUponRemove: BEFORE, cart.getCart_data().getData().get(i): "
                        + cart.getCart_data().getData().get(i));
                int totalQty = cart.getCart_data().getData().get(i).getQty();
                Log.i(TAG, "updateCartDataUponRemove: totalQty: " + totalQty);
                float totalPriceFloat = Float.parseFloat(cart.getCart_data().getData().get(i).getTotalPrice());
                Log.i(TAG, "updateCartDataUponRemove: totalPriceFloat: " + totalPriceFloat);
                float singlePriceFloat = (totalPriceFloat / totalQty);
                Log.i(TAG, "updateCartDataUponRemove: singlePriceFloat: " + singlePriceFloat);

                float savingsFloat = Float.parseFloat(cart.getCart_data().getData().get(i).getSaving());
                float singleSavingFloat = savingsFloat / totalQty;
                cart.getCart_data().getData().get(i).setSaving(String.valueOf((savingsFloat - singleSavingFloat)));

                cart.getCart_data().getData().get(i).setTotalPrice(String.valueOf((totalPriceFloat - singlePriceFloat)));
                cart.getCart_data().getData().get(i).setQty(--totalQty);
                Log.i(TAG, "updateCartDataUponRemove: AFTER, cart.getCart_data().getData().get(i): "
                        + cart.getCart_data().getData().get(i));

                //UPDATING TOTAL PRICE & TOTAL SAVING.
                float totalSavingsFloat = Float.parseFloat(cart.getCart_data().getTotal_savings());
                float grossTotalPriceFloat = Float.parseFloat(cart.getCart_data().getTotal_price());
                cart.getCart_data().setTotal_price(String.valueOf((grossTotalPriceFloat - singlePriceFloat)));
                cart.getCart_data().setTotal_savings(String.valueOf((totalSavingsFloat - singleSavingFloat)));

                return cart.getCart_data().getData().get(i);
            }
        }
        return null;
    }

    public void updateCartDataUponCompletelyRemove(int productId) {
        Log.i(TAG, "updateCartDataUponCompletelyRemove: fired! productId: " + productId);

        for (int i = 0; i < cart.getCart_data().getData().size(); i++) {
            if (cart.getCart_data().getData().get(i).getProd_id() == productId) {

                Log.i(TAG, "updateCartDataUponCompletelyRemove: BEFORE, cart.getCart_data().getData().get(i): "
                        + cart.getCart_data().getData().get(i));
                int totalQty = cart.getCart_data().getData().get(i).getQty();
                Log.i(TAG, "updateCartDataUponCompletelyRemove: totalQty: " + totalQty);
                float totalPriceFloat = Float.parseFloat(cart.getCart_data().getData().get(i).getTotalPrice());
                Log.i(TAG, "updateCartDataUponCompletelyRemove: totalPriceFloat: " + totalPriceFloat);
                float singlePriceFloat = (totalPriceFloat / totalQty);
                Log.i(TAG, "updateCartDataUponCompletelyRemove: singlePriceFloat: " + singlePriceFloat);

                float savingsFloat = Float.parseFloat(cart.getCart_data().getData().get(i).getSaving());
                float singleSavingFloat = savingsFloat / totalQty;

                //UPDATING TOTAL PRICE & TOTAL SAVING.
                float totalSavingsFloat = Float.parseFloat(cart.getCart_data().getTotal_savings());
                float grossTotalPriceFloat = Float.parseFloat(cart.getCart_data().getTotal_price());
                cart.getCart_data().setTotal_price(String.valueOf((grossTotalPriceFloat - singlePriceFloat)));
                cart.getCart_data().setTotal_savings(String.valueOf((totalSavingsFloat - singleSavingFloat)));

                cart.getCart_data().getData().remove(i);
            }
        }
    }

    public boolean checkCouponApplicability(Cart_CartData_Coupons cart_cartData_coupons) {
        Log.i(TAG, "checkCouponApplicability: fired!");

        float totalPrice = Float.parseFloat(cart.getCart_data().getTotal_price());

        Log.i(TAG, "checkCouponApplicability: cart_cartData_coupons.getMin_spend(): " + cart_cartData_coupons.getMin_spend());
        if (totalPrice >= cart_cartData_coupons.getMin_spend()) {
            Log.i(TAG, "checkCouponApplicability: Total Cart Price greater than min spend.");
            selectedCouponId = cart_cartData_coupons.getId();
            selectedCouponMinSpend = cart_cartData_coupons.getMin_spend();
            selectedCouponType = cart_cartData_coupons.getType();
            if (cart_cartData_coupons.getType().matches("fcd")) {   //fcd - FIXED COUPON DISCOUNT
                selectedCouponAmount = cart_cartData_coupons.getAmmount();
            } else {                                                      //p - PERCENTAGE DISCOUNT
                selectedCouponMaxDiscount = cart_cartData_coupons.getMax_discount();
                selectedCouponAmount = cart_cartData_coupons.getAmmount();
            }
            return true;
        } else {
            selectedCouponType = "";
            selectedCouponId = 0;
        }
        return false;
    }

    public boolean checkFreebieApplicability(int minCartValue) {
        Log.i(TAG, "checkFreebieApplicability: fired!");

        float totalPrice = Float.parseFloat(cart.getCart_data().getTotal_price());

        if (totalPrice >= minCartValue) {
            Log.i(TAG, "checkFreebieApplicability: Total Cart Price greater than minCartValue.");
            freebieMinCartValue = minCartValue;
            return true;
        }
        return false;
    }

    public int checkBestApplicableFreebie() {
        Log.i(TAG, "checkBestApplicableFreebie: fired!");

        int bestApplicableFreebieIndex = -1;

        for (int i = 0; i < cart.getCart_data().getFreebies().size(); i++) {
            if (cart.getCart_data().getFreebies().get(i).getMin_cart_value() <= Float.parseFloat(cart.getCart_data().getTotal_price())) {
                if (freebieMinCartValue <= cart.getCart_data().getFreebies().get(i).getMin_cart_value()) {
                    bestApplicableFreebieIndex = i;
                    freebieMinCartValue = cart.getCart_data().getFreebies().get(i).getMin_cart_value();
                    selectedFreebieId = cart.getCart_data().getFreebies().get(i).getId();
                }
            }
        }

        if (cart.getCart_data().getFreebies().size() > 0) {
            if (bestApplicableFreebieIndex != -1) {
                cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(
                        cart.getCart_data().getFreebies().get(bestApplicableFreebieIndex).getProducts());
            } else {
                cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
            }
        } else {
            cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
        }
        return bestApplicableFreebieIndex;
    }

    public int checkBestApplicableCoupon() {
        Log.i(TAG, "checkBestApplicableCoupon: fired!");

        int bestApplicableCouponIndex = -1;

        for (int i = 0; i < cart.getCart_data().getCoupons().size(); i++) {
            if (cart.getCart_data().getCoupons().get(i).getStatus().matches("1")) {
                if (cart.getCart_data().getCoupons().get(i).getMin_spend() <= Float.parseFloat(cart.getCart_data().getTotal_price())) {
                    if (selectedCouponMinSpend <= cart.getCart_data().getCoupons().get(i).getMin_spend()) {
                        bestApplicableCouponIndex = i;
                        selectedCouponAmount = cart.getCart_data().getCoupons().get(i).getAmmount();
                        selectedCouponMaxDiscount = cart.getCart_data().getCoupons().get(i).getMax_discount();
                        selectedCouponMinSpend = cart.getCart_data().getCoupons().get(i).getMin_spend();
                        selectedCouponId = cart.getCart_data().getCoupons().get(i).getId();
                        selectedCouponType = cart.getCart_data().getCoupons().get(i).getType();
                    }
                }
            }
        }
        return bestApplicableCouponIndex;
    }

    public void updateDeliveryAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress) {
        Log.i(TAG, "updateDeliveryAddress: fired!");

        cart.getCart_data().setBuyerDefaultAddress(cart_cartData_buyerAddress);
    }

    public void updateEditedDeliveryAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress) {
        Log.i(TAG, "updateEditedDeliveryAddress: fired!");

        if (cart_cartData_buyerAddress.getStatus() == 1) {
            Log.i(TAG, "updateEditedDeliveryAddress: Default address was edited, thus setting the incoming edited default address!");
            cart.getCart_data().setBuyerDefaultAddress(cart_cartData_buyerAddress);
        } else {
            Log.i(TAG, "updateEditedDeliveryAddress: Default address was not edited, thus not setting the incoming edited address!!");
        }
    }

    public boolean getCartDetails() {
        Log.i(TAG, "getCartDetails: fired!");

        return cartRepository.getCartDetails();
    }

//    public boolean addToCart(int productId, int qty) {
//        Log.i(TAG, "addToCart: fired!");
//
//        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
//        Log.i(TAG, "addToCart: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());
//
//        return cartRepository.addToCart(productId);
//    }

    public boolean removeFromCart(int productId, int qty) {
        Log.i(TAG, "removeFromCart: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "removeFromCart: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        //WILL CHECK IF QTY IS 0, THEN WILL CALL ANOTHER FUNCTION TO DELETE PRODUCT FROM CART.
        if (qty == 0) {
            return cartRepository.completelyRemoveFromCart(productId);
        } else {
            return cartRepository.removeFromCart(productId);
        }
    }

    public boolean placeOrder(PlaceOrderModel placeOrderModel) {
        Log.i(TAG, "placeOrder: fired!");

        return cartRepository.placeOrder(placeOrderModel);
    }

    public boolean createCashFreeOrder(float orderAmount/*, String uniqueOrderId*/) {
        Log.i(TAG, "createCashFreeOrder: fired!");

        return cartRepository.createCashFreeOrder(orderAmount/*, uniqueOrderId*/);
    }

    public boolean postPaymentNotifyMDGServer(String orderId) {
        Log.i(TAG, "postPaymentNotifyMDGServer: fired!");

        return cartRepository.postPaymentNotifyMDGServer(orderId);
    }

    //TO CHECK IF ORDER CAN BE PLACED
    public boolean checkVendorOrderAcceptance() {
        Log.i(TAG, "checkVendorOrderAcceptance: fired!");

        return cartRepository.checkVendorOrderAcceptance();
    }

    //TO CHECK IF PROD CAN BE ADDED TO CART
    public boolean checkVendorOrderAcceptance(int productId, int qty) {
        Log.i(TAG, "checkVendorOrderAcceptance: fired! productId: " + productId);

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "checkVendorOrderAcceptance: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        toBeAddedProductId = productId;
        toBeAddedProductQty = qty;

        return cartRepository.checkVendorOrderAcceptance2(productId);
    }
}
