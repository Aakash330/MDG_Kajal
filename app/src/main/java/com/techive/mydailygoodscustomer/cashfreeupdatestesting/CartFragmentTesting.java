package com.techive.mydailygoodscustomer.cashfreeupdatestesting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.exception.CFInvalidArgumentException;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptCartFragmentResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.SplitOrderResponse;
import com.techive.mydailygoodscustomer.Models.CashFreeOrder;
import com.techive.mydailygoodscustomer.Models.PlaceOrderModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Fragments.CartFragment;
import com.techive.mydailygoodscustomer.UI.Fragments.DeliveryAddressDialogFragment;
import com.techive.mydailygoodscustomer.UI.Fragments.DeliveryAddressListDialogFragment;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.LoginUtil;
import com.techive.mydailygoodscustomer.ViewModels.CartViewModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragmentTesting extends Fragment {

    private static final String TAG ="CartFragmentTesting" ;
    private CartViewModel cartViewModel;
    private SwipeRefreshLayout cartSwipeRefreshLayout;
    private NestedScrollView cartNestedScrollView;
    private Button placeOrderMaterialButton;
    String orderAmount="1.0";
    int  buyerid=1132;
    String Order_token,Order_id;
    private MaterialCheckBox selfPickupAllowedMaterialCheckBox;
    private MaterialTextView totalPayableMaterialTextView;


    private ConstraintLayout  orderSuccessfulConstraintLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart_testing, container, false);


        return view;
    }
/*

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponentViews(view);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        Call<CashFreeOrder> cashFreeOrderCall = ServiceTs.getService().createCashFreeOrder(
                buyerid , orderAmount, uniqueOrderId);

        cashFreeOrderCall.enqueue(new Callback<CashFreeOrder>() {
            @Override
            public void onResponse(Call<CashFreeOrder> call, Response<CashFreeOrder> response) {
                Log.i(TAG, "onResponse: CREATE CASH FREE ORDER Response seems to be a success!");
                Log.w(TAG,"initComponentViews  CartFragmentTesting");

                if (!response.isSuccessful()) {
                    // CashFreeOrder cashFreeOrderUnsuccessful = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", "Somehow Server didn't respond! " + response.code());
                    Toast.makeText(getContext(), "unsuccessful...", Toast.LENGTH_SHORT).show();
                    return;
                }

                CashFreeOrder cashFreeOrder = response.body();
                Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder);
                Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder.getOrder_id());
                Log.i(TAG, "onResponse: cashFreeOrder: " +cashFreeOrder.getOrder_token());
                Order_id  = cashFreeOrder.getOrder_id();
                Order_token =cashFreeOrder.getOrder_token();

            }

            @Override
            public void onFailure(Call<CashFreeOrder> call, Throwable t) {
                Log.i(TAG, "onFailure: CREATE CASH FREE ORDER Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        initListeners();
        initObserver();


    }

    private void initComponentViews(View view) {
        Log.w(TAG,"initComponentViews  CartFragmentTesting");
        if(view==null){
            Log.w(TAG,"initComponentViews  CartFragmentTesting if");

            Toast.makeText(getContext(), "null view Reference", Toast.LENGTH_SHORT).show();
        }else{
            Log.w(TAG,"initComponentViews  CartFragmentTesting else");

            cartSwipeRefreshLayout = view.findViewById(R.id.cartSwipeRefreshLayout);

            cartNestedScrollView = view.findViewById(R.id.cartNestedScrollView);
            placeOrderMaterialButton = view.findViewById(R.id.placeOrderMaterialButton);

            orderSuccessfulConstraintLayout = view.findViewById(R.id.orderSuccessfulConstraintLayout);
        }

    }

    private void initObserver() {
        cartViewModel.getSplitOrderResponseMutableLiveData().observe(getActivity(), splitOrderResponseObserver);
        ((CashFreeInitializationTestingActivity) getActivity()).paymentStatusMutableLiveData.observe(getActivity(), postPaymentObserver);//@kajal_12_2_22 checkD
    }
    private void initListeners() {
        Log.w(TAG,"initListeners  CartFragmentTesting");

        cartSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        placeOrderMaterialButton.setOnClickListener(onClickListener);
    }
    @Override
    public void onPause() {
        super.onPause();

        if (((CashFreeInitializationTestingActivity) requireActivity()).paymentStatusMutableLiveData.getValue() != null) {
            ((CashFreeInitializationTestingActivity) getActivity()).paymentStatusMutableLiveData.setValue(null);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");
            initiateOnlinePayment(Order_token, Order_id);


        }
    };

    private void initiateOnlinePayment(String orderToken, String orderId) {
        Log.i(TAG, "initiateOnlinePayment: fired! orderToken: " + orderToken + "\torderId: " + orderId);
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
//                    .setEnvironment(CFSession.Environment.SANDBOX)
                    .setEnvironment(CFSession.Environment.PRODUCTION)
                    //.setPaymentSessionID(orderToken)
                    .setOrderToken(orderToken)
                    //.setOrderToken("VQwl3LAMjdKcnFvgmYX9")
                    .setOrderId(orderId)
//                    .setOrderId("123456")
                    .build();
            //success@upi

//            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
//                    .setNavigationBarBackgroundColor("#6A3FD3")
//                    .setNavigationBarTextColor("#FFFFFF")
//                    .setButtonBackgroundColor("#6A3FD3")
//                    .setButtonTextColor("#FFFFFF")
//                    .setPrimaryTextColor("#000000")
//                    .setSecondaryTextColor("#000000")
//                    .build();

            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#198E0A") toolbar_dark_green
                    .setNavigationBarTextColor("#FFFFFF") white
                    .setButtonBackgroundColor("#198E0A") toolbar_dark_green
                    .setButtonTextColor("#FFFFFF") white
                    .setPrimaryTextColor("#000000") black
                    .setSecondaryTextColor("#000000") black
                    .build();

            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                    .setSession(cfSession)
//                .setCFUIPaymentModes(cfPaymentComponent)
//                    .setCFUIPaymentModes(null)
                    .setCFNativeCheckoutUITheme(cfTheme)
                    .build();

            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(getContext(), cfDropCheckoutPayment);

        } catch (CFInvalidArgumentException cfInvalidArgumentException) {
            Log.i(TAG, "initiateOnlinePayment: cfInvalidArgumentException.getMessage(): " + cfInvalidArgumentException.getMessage());
            cfInvalidArgumentException.printStackTrace();
        } catch (Exception exception) {
            Log.i(TAG, "initiateOnlinePayment: exception.getMessage(): " + exception.getMessage());
            exception.printStackTrace();
        }
    }


    private final Observer<String> postPaymentObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            Log.i(TAG, "onChanged: POST PAYMENT Observer fired! s: " + s);

            if (s != null) {
                if (!s.toUpperCase().matches("FAILED")) {
                    //SUCCESS PAYMENT CASE
                    Log.i(TAG, "onChanged: Payment Success!");

                    //New API for updating order with orderId & payment bit 1.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!cartViewModel.postPaymentNotifyMDGServer(s)) {
                                requireActivity().runOnUiThread(() -> {
                                    DialogUtil.showNoInternetToast(requireActivity());
                                });

                            } else {
                                requireActivity().runOnUiThread(() -> {
                                    DialogUtil.showProcessingInfoDialog(requireActivity());
                                });

                            }
                        }
                    }).start();

                } else {
                    //FAILED PAYMENT CASE
                    Toast.makeText(requireActivity(), "Payment Unsuccessful!\nOrder Not Placed!\nPlease try again later!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: Null Observer fired!");
            }
        }
    };
    private final Observer<SplitOrderResponse> splitOrderResponseObserver = new Observer<SplitOrderResponse>() {
        @Override
        public void onChanged(SplitOrderResponse splitOrderResponse) {
            Log.i(TAG, "onChanged: SPLIT ORDER Observer fired!\nsplitOrderResponse: " + splitOrderResponse);

            if (splitOrderResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (!splitOrderResponse.getStatus().matches("ERROR")) {
                    Log.i(TAG, "onChanged: Split Order API post MDG Place Order Execute Success!");

                    DialogUtil.showCustomSnackbar(requireActivity(), cartSwipeRefreshLayout,
                            "Payment Completed!\nOrder Placed Successfully\nThank You for Shopping with us!", -1);
                } else {
                    Log.i(TAG, "onChanged: Split Order API post MDG Place Order Execute Failed!");
                    Toast.makeText(requireActivity(), "Payment Completed!\nOrder Placed with some difficulties." +
                            "\nThank You for Shopping with us!", Toast.LENGTH_SHORT).show();
                }

                 HIT CART API OVER HERE AS WELL/MIGHT INSTEAD JUST EMPTY THE CART.
                //  clearCart();
                orderSuccessful(true);
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };
    private void orderSuccessful(boolean isSuccessful, String orderSuccess) {
        Log.i(TAG, "orderSuccessful: fired! isSuccessful: " + isSuccessful);

        if (isSuccessful) {
            cartNestedScrollView.setVisibility(View.GONE);
            orderSuccessfulConstraintLayout.setVisibility(View.VISIBLE);
//            orderSuccessMaterialTextView.setText(orderSuccess);
        } else {
            cartNestedScrollView.setVisibility(View.VISIBLE);
            orderSuccessfulConstraintLayout.setVisibility(View.VISIBLE);
        }
    }
    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            if (!cartViewModel.getCartDetails()) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog(requireActivity());
            }

            cartSwipeRefreshLayout.setRefreshing(false);
        }
    };
*//** createOrder and split 500 working: cartclear*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponentViews(view);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
      /*
        Call<CashFreeOrder> cashFreeOrderCall = ServiceTs.getService().createCashFreeOrder(
                buyerid , orderAmount*//*, uniqueOrderId*//*);

        cashFreeOrderCall.enqueue(new Callback<CashFreeOrder>() {
            @Override
            public void onResponse(Call<CashFreeOrder> call, Response<CashFreeOrder> response) {
                Log.i(TAG, "onResponse: CREATE CASH FREE ORDER Response seems to be a success!");
                Log.w(TAG,"initComponentViews  CartFragmentTesting");

                if (!response.isSuccessful()) {
                    // CashFreeOrder cashFreeOrderUnsuccessful = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", "Somehow Server didn't respond! " + response.code());
                    Toast.makeText(getContext(), "unsuccessful...", Toast.LENGTH_SHORT).show();
                    return;
                }

                CashFreeOrder cashFreeOrder = response.body();
                Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder);
                Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder.getOrder_id());
                Log.i(TAG, "onResponse: cashFreeOrder: " +cashFreeOrder.getOrder_token());
                Order_id  = cashFreeOrder.getOrder_id();
                Order_token =cashFreeOrder.getOrder_token();

            }

            @Override
            public void onFailure(Call<CashFreeOrder> call, Throwable t) {
                Log.i(TAG, "onFailure: CREATE CASH FREE ORDER Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });*/
        initListeners();
        initObserver();


    }

    private void initComponentViews(View view) {
        totalPayableMaterialTextView = view.findViewById(R.id.totalPayableMaterialTextView);
        selfPickupAllowedMaterialCheckBox = view.findViewById(R.id.selfPickupAllowedMaterialCheckBox);
        Log.w(TAG,"initComponentViews  CartFragmentTesting");
        if(view==null){
            Log.w(TAG,"initComponentViews  CartFragmentTesting if");

            Toast.makeText(getContext(), "null view Reference", Toast.LENGTH_SHORT).show();
        }else{
            Log.w(TAG,"initComponentViews  CartFragmentTesting else");

            cartSwipeRefreshLayout = view.findViewById(R.id.cartSwipeRefreshLayout);

            cartNestedScrollView = view.findViewById(R.id.cartNestedScrollView);
            placeOrderMaterialButton = view.findViewById(R.id.placeOrderMaterialButton);

            orderSuccessfulConstraintLayout = view.findViewById(R.id.orderSuccessfulConstraintLayout);
        }

    }

    private void initObserver() {
        cartViewModel.getSplitOrderResponseMutableLiveData().observe(getActivity(), splitOrderResponseObserver);
        ((CashFreeInitializationTestingActivity) getActivity()).paymentStatusMutableLiveData.observe(getActivity(), postPaymentObserver);//@kajal_12_2_22 checkD
        cartViewModel.getOrderAcceptCartFragmentResponseMutableLiveData().observe(getActivity(), orderAcceptResponseObserver);
        cartViewModel.getPlaceOrderMutableLiveData().observe(getActivity(), placeOrderObserver);
        cartViewModel.getCashFreeOrderMutableLiveData().observe(getActivity(), cashFreeOrderObserver);
    }
    private void initListeners() {
        Log.w(TAG,"initListeners  CartFragmentTesting");

        cartSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        placeOrderMaterialButton.setOnClickListener(onClickListener);
    }
    @Override
    public void onPause() {
        super.onPause();

              if (((CashFreeInitializationTestingActivity) requireActivity()).paymentStatusMutableLiveData.getValue() != null) {
            ((CashFreeInitializationTestingActivity) getActivity()).paymentStatusMutableLiveData.setValue(null);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");
            //initiateOnlinePayment(Order_token, Order_id);
            /* NEW FLOW AS OF 22-6-22. */
            if (!cartViewModel.checkVendorOrderAcceptance()) {
                Log.w(TAG,"PlaceOrderBtn CartFragment if");
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                Log.w(TAG,"PlaceOrderBtn CartFragment else");
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }



        }
    };
    private final Observer<GeneralResponse> placeOrderObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: PLACE ORDER Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (generalResponse.getError() == 200) {

                    if (cartViewModel.codPayment) {
                        Log.i(TAG, "onChanged: Order Placed Successfully through COD!");
                        Toast.makeText(requireActivity(), "Order Placed Successfully through COD!" +
                                "\nPlease pay the amount at the time of your Order Delivery!", Toast.LENGTH_LONG).show();
                        clearCart();
                        orderSuccessful(true);
                    } else {
                        //NEW FLOW (AS OF 27-5-22)
//                        initiateOnlinePayment(cartViewModel.getCashFreeOrderMutableLiveData().getValue().getOrder_token(),
//                                cartViewModel.getCashFreeOrderMutableLiveData().getValue().getOrder_id());
                        initiateOnlinePayment(cartViewModel.cashfreeOrderToken, cartViewModel.cashfreeOrderId);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while placing order from Cart!");
                    Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };
    private void clearCart() {
        Log.i(TAG, "clearCart: fired!");

        cartViewModel.cartRecyclerViewAdapter.setCart_cartData_dataList(null);
        cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
        cartViewModel.cartCouponRecyclerViewAdapter.setCart_cartData_couponsList(null);
        cartViewModel.cartFreebieRecyclerViewAdapter.setCart_cartData_freebiesList(null);

      //  cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
        ApplicationData.setProdIdOrderQtyHashMap(new HashMap<>());

     //   totalPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
      //  savedPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
       // flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
        totalPayableMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
      //  shopAddressMaterialTextView.setText("");
      //  shopMobNoMaterialTextView.setText("");
      //  customerDeliveryAddressMaterialTextView.setText("");

        placeOrderMaterialButton.setEnabled(false);
    }

    private final Observer<OrderAcceptCartFragmentResponse> orderAcceptResponseObserver = new Observer<OrderAcceptCartFragmentResponse>() {
        @Override
        //  public void onChanged(OrderAcceptResponse orderAcceptResponse) {
        public void onChanged(OrderAcceptCartFragmentResponse orderAcceptResponse) {
            Log.i(TAG, "onChanged: ORDER ACCEPTANCE Observer fired!\norderAcceptResponse: " + orderAcceptResponse);

            if (orderAcceptResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (orderAcceptResponse.getError() == 200) {
                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getStatus() == 0) {
                        Log.i(TAG, "onChanged: Vendor Accepting Orders, Proceeding to place Order.");

                       // selectPaymentMode();
                        Log.i(TAG, "onClick: Online Payment was selected!");

                       // String totalOrderAmount = totalPayableMaterialTextView.getText().toString();
                        //Log.i(TAG, "onClick: totalOrderAmount: " + totalOrderAmount);
                       // totalOrderAmount = totalOrderAmount.substring(1);
                      //  Log.i(TAG, "onClick: totalOrderAmount substring: " + totalOrderAmount);
                     //   Log.i(TAG, "onClick: cartViewModel.getCart().getCart_data().getTotal_price(): " + cartViewModel.getCart().getCart_data().getTotal_price());

                        //TESTING SUCCESS - CashFree order created!
//                    if (!cartViewModel.createCashFreeOrder(Float.parseFloat(cartViewModel.getCart().getCart_data().getTotal_price()))) {
                        if (!cartViewModel.createCashFreeOrder(Float.parseFloat("1"))) {
                            Log.w(TAG,"createCashFreeOrder eee");
                            DialogUtil.showNoInternetToast(requireActivity());
                        } else {
                            DialogUtil.showProcessingInfoDialog(requireActivity());
                        }

                    } else {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't place Orders.");
//                        DialogUtil.showCustomSnackbar(requireActivity(), cartSwipeRefreshLayout,
//                                "Sorry, Vendor is not accepting any Orders at the moment!\nPlease try again later!", -1);
                        DialogUtil.showCustomSnackbar(requireActivity(), cartSwipeRefreshLayout, orderAcceptResponse.getMsg(), -1);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Order Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };
    private final Observer<CashFreeOrder> cashFreeOrderObserver = new Observer<CashFreeOrder>() {
        @Override
        public void onChanged(CashFreeOrder cashFreeOrder) {
            Log.i(TAG, "onChanged: CREATE CASH FREE ORDER OBSERVER fired!\ncashFreeOrder: " + cashFreeOrder);

            if (cashFreeOrder != null) {
                DialogUtil.dismissProcessingInfoDialog();

                //Temp try-catch
                try {

                    if (!cashFreeOrder.getOrder_status().matches("ERROR")) {
                        Log.i(TAG, "onChanged: Proceeding to Place Order on MDG Server!");
//                    initiateOnlinePayment(cashFreeOrder.getOrder_token(), cashFreeOrder.getOrder_id());

                        cartViewModel.cashfreeOrderId = cashFreeOrder.getOrder_id();
                        cartViewModel.cashfreeOrderToken = cashFreeOrder.getOrder_token();
                        Log.i(TAG, "onChanged: id"+cashFreeOrder.getOrder_id());
                        Log.i(TAG, "onChanged: token"+ cashFreeOrder.getOrder_token());


                        //NEW FLOW (AS OF 27-5-22)
                        placeOrder();
                    } else {
                        Log.i(TAG, "onChanged: Something went wrong while placing CashFreeOrder.");
                        Toast.makeText(requireActivity(), cashFreeOrder.getOrder_note(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception exception) {
                    Log.i(TAG, "onChanged: Exception caught in CashFreeOrder Response!");
                    exception.printStackTrace();

                    Toast.makeText(requireActivity(), "Exception in creating order!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
            }
        }
    };

    private void placeOrder() {
        Log.i(TAG, "placeOrder: fired!");

        int selfPickup = 0, flatShippingCost = 0, couponId, freebieId, paymentStatus;
        float appliedCouponDiscount;
        String orderId;

//        if (cart_cartData.getShipping_data().getFree_del_odr() > 0) {
//            //Upto some cart value, flat shipping cost will be applied.
//            if (Float.parseFloat(cart_cartData.getTotal_price()) > cart_cartData.getShipping_data().getFree_del_odr()) {
//                flatShippingCost = 0;
//            } else {
//                flatShippingCost = cart_cartData.getShipping_data().getFlat_sh_cost();
//            }
//        } else {
//            //flat shipping cost will always be applied
//            flatShippingCost = cart_cartData.getShipping_data().getFlat_sh_cost();
//        }
        selfPickup = 1;
        //Improved on 23-8-22
        if (cartViewModel.getCart().getCart_data().getShipping_data() != null) {
            if (cartViewModel.getCart().getCart_data().getShipping_data().getHomedeliver() == 1) {
                if (selfPickupAllowedMaterialCheckBox.isChecked()) {
                    selfPickup = 1;
                } else {

                    if (cartViewModel.getCart().getCart_data().getShipping_data().getFree_del_odr() > 0) {
                        //Upto some cart value, flat shipping cost will be applied.
                        if (Float.parseFloat(cartViewModel.getCart().getCart_data().getTotal_price()) > cartViewModel.getCart().getCart_data().getShipping_data().getFree_del_odr()) {
                            flatShippingCost = 0;
                        } else {
                            flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                        }
                    } else {
                        //flat shipping cost will always be applied
                        flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                    }

//                    flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                }
            } else {
                Log.i(TAG, "placeOrder: Home Delivery not allowed thus passing Self-Pickup = 1");
                selfPickup = 1;
            }
        } else {
            //Just a precautionary measure. Will never reach here.
            Log.i(TAG, "placeOrder: Shipping data was Null.");
            selfPickup = 1;
        }//@kajal_12_9_22

//        if (selfPickupAllowedMaterialCheckBox.isChecked()) {
//            selfPickup = 1;
//        } else {
//            flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
//        }

        /*WILL HAVE TO CHECK ABOUT COUPON SELECTION, IF MANDATORY*/
        if (cartViewModel.selectedCouponId == 0) {
            Log.i(TAG, "placeOrder: No Coupon selected!");
            couponId = 0;
            appliedCouponDiscount = 0;
        } else {
            couponId = cartViewModel.selectedCouponId;
            appliedCouponDiscount = cartViewModel.appliedCouponDiscount;
        }

        /*WILL HAVE TO CHECK ABOUT FREEBIE SELECTION, IF MANDATORY*/
        if (cartViewModel.selectedFreebieId == 0) {
            Log.i(TAG, "placeOrder: No Freebie selected!");
            freebieId = 0;
        } else {
            freebieId = cartViewModel.selectedFreebieId;
        }

        /*paymentStatus = 0-COD, 1-Success, 2-Pending*/
        if (cartViewModel.codPayment) {
            //PAYMENT MODE COD
            paymentStatus = 0;
            orderId = "";
        } else {
            //PAYMENT MODE ONLINE
            paymentStatus = 2;
//            orderId = cartViewModel.getCashFreeOrderMutableLiveData().getValue().getOrder_id();
            orderId = cartViewModel.cashfreeOrderId;
        }

        PlaceOrderModel placeOrderModel = new PlaceOrderModel(1132,
                1130, selfPickup, appliedCouponDiscount, flatShippingCost,
                couponId, freebieId, paymentStatus, orderId);
        Log.i(TAG, "placeOrder: placeOrderModel: " + placeOrderModel);

        if (!cartViewModel.placeOrder(placeOrderModel)) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            DialogUtil.showProcessingInfoDialog(requireActivity());
        }
    }
/*    private void placeOrder() {
        Log.i(TAG, "placeOrder: fired!");

        int selfPickup = 0, flatShippingCost = 0, couponId, freebieId, paymentStatus;
        float appliedCouponDiscount;
        String orderId;

//        if (cart_cartData.getShipping_data().getFree_del_odr() > 0) {
//            //Upto some cart value, flat shipping cost will be applied.
//            if (Float.parseFloat(cart_cartData.getTotal_price()) > cart_cartData.getShipping_data().getFree_del_odr()) {
//                flatShippingCost = 0;
//            } else {
//                flatShippingCost = cart_cartData.getShipping_data().getFlat_sh_cost();
//            }
//        } else {
//            //flat shipping cost will always be applied
//            flatShippingCost = cart_cartData.getShipping_data().getFlat_sh_cost();
//        }

        //Improved on 23-8-22
       *//* if (cartViewModel.getCart().getCart_data().getShipping_data() != null) {
            if (cartViewModel.getCart().getCart_data().getShipping_data().getHomedeliver() == 1) {
             *//**//*  // if (selfPickupAllowedMaterialCheckBox.isChecked()) {
                    selfPickup = 1;
               // }
          *//**//*  else {

                    if (cartViewModel.getCart().getCart_data().getShipping_data().getFree_del_odr() > 0) {
                        //Upto some cart value, flat shipping cost will be applied.
                        if (Float.parseFloat(cartViewModel.getCart().getCart_data().getTotal_price()) > cartViewModel.getCart().getCart_data().getShipping_data().getFree_del_odr()) {
                            flatShippingCost = 0;
                        } else {
                            flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                        }
                    } else {
                        //flat shipping cost will always be applied
                        flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                    }

//                    flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                }*//**//*
          *//**//*  } else {
                Log.i(TAG, "placeOrder: Home Delivery not allowed thus passing Self-Pickup = 1");
                selfPickup = 1;
            }
        } else {
            //Just a precautionary measure. Will never reach here.
            Log.i(TAG, "placeOrder: Shipping data was Null.");
            selfPickup = 1;
        }*//**//*

//        if (selfPickupAllowedMaterialCheckBox.isChecked()) {
//            selfPickup = 1;
//        } else {
//            flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
//        }

      *//**//*  *//**//**//**//*WILL HAVE TO CHECK ABOUT COUPON SELECTION, IF MANDATORY*//**//**//**//*
        if (cartViewModel.selectedCouponId == 0) {
            Log.i(TAG, "placeOrder: No Coupon selected!");
            couponId = 0;
            appliedCouponDiscount = 0;
        } else {
            couponId = cartViewModel.selectedCouponId;
            appliedCouponDiscount = cartViewModel.appliedCouponDiscount;
        }

        *//**//**//**//*WILL HAVE TO CHECK ABOUT FREEBIE SELECTION, IF MANDATORY*//**//**//**//*
        if (cartViewModel.selectedFreebieId == 0) {
            Log.i(TAG, "placeOrder: No Freebie selected!");
            freebieId = 0;
        } else {
            freebieId = cartViewModel.selectedFreebieId;
        }*//**//*
*//*
        *//*paymentStatus = 0-COD, 1-Success, 2-Pending*//*
        if (cartViewModel.codPayment) {
            //PAYMENT MODE COD
            paymentStatus = 0;
            orderId = "";
        } else {
            //PAYMENT MODE ONLINE
            paymentStatus = 2;
//            orderId = cartViewModel.getCashFreeOrderMutableLiveData().getValue().getOrder_id();
            orderId = cartViewModel.cashfreeOrderId;
        }

        PlaceOrderModel placeOrderModel = new PlaceOrderModel(89,
                ApplicationData.getDefaultStoreId(), selfPickup, 1, flatShippingCost,
                0,0, paymentStatus, orderId);
        Log.i(TAG, "placeOrder: placeOrderModel: " + placeOrderModel);

        if (!cartViewModel.placeOrder(placeOrderModel)) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            DialogUtil.showProcessingInfoDialog(requireActivity());
        }
    }*/
    private void initiateOnlinePayment(String orderToken, String orderId) {
            Log.i(TAG, "initiateOnlinePayment: fired! orderToken: " + orderToken + "\torderId: " + orderId);
            try {
                CFSession cfSession = new CFSession.CFSessionBuilder()
//                    .setEnvironment(CFSession.Environment.SANDBOX)
                        .setEnvironment(CFSession.Environment.PRODUCTION)
                        //.setPaymentSessionID(orderToken)
                        .setOrderToken(orderToken)
                        //.setOrderToken("VQwl3LAMjdKcnFvgmYX9")
                        .setOrderId(orderId)
//                    .setOrderId("123456")
                        .build();
                //success@upi

//            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
//                    .setNavigationBarBackgroundColor("#6A3FD3")
//                    .setNavigationBarTextColor("#FFFFFF")
//                    .setButtonBackgroundColor("#6A3FD3")
//                    .setButtonTextColor("#FFFFFF")
//                    .setPrimaryTextColor("#000000")
//                    .setSecondaryTextColor("#000000")
//                    .build();

                CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                        .setNavigationBarBackgroundColor("#198E0A") /*toolbar_dark_green*/
                        .setNavigationBarTextColor("#FFFFFF") /*white*/
                        .setButtonBackgroundColor("#198E0A") /*toolbar_dark_green*/
                        .setButtonTextColor("#FFFFFF") /*white*/
                        .setPrimaryTextColor("#000000") /*black*/
                        .setSecondaryTextColor("#000000") /*black*/
                        .build();

                CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                        .setSession(cfSession)
//                .setCFUIPaymentModes(cfPaymentComponent)
//                    .setCFUIPaymentModes(null)
                        .setCFNativeCheckoutUITheme(cfTheme)
                        .build();

                CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
                gatewayService.doPayment(getContext(), cfDropCheckoutPayment);

            } catch (CFInvalidArgumentException cfInvalidArgumentException) {
                Log.i(TAG, "initiateOnlinePayment: cfInvalidArgumentException.getMessage(): " + cfInvalidArgumentException.getMessage());
                cfInvalidArgumentException.printStackTrace();
            } catch (Exception exception) {
                Log.i(TAG, "initiateOnlinePayment: exception.getMessage(): " + exception.getMessage());
                exception.printStackTrace();
            }
        }


    private final Observer<String> postPaymentObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            Log.i(TAG, "onChanged: POST PAYMENT Observer fired! s: " + s);

            if (s != null) {
                if (!s.toUpperCase().matches("FAILED")) {
                    //SUCCESS PAYMENT CASE
                    Log.i(TAG, "onChanged: Payment Success!");

                    //New API for updating order with orderId & payment bit 1.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!cartViewModel.postPaymentNotifyMDGServer(s)) {
                                requireActivity().runOnUiThread(() -> {
                                    DialogUtil.showNoInternetToast(requireActivity());
                                });

                            } else {
                                requireActivity().runOnUiThread(() -> {
                                    DialogUtil.showProcessingInfoDialog(requireActivity());
                                });

                            }
                        }
                    }).start();

                } else {
                    //FAILED PAYMENT CASE
                    Toast.makeText(requireActivity(), "Payment Unsuccessful!\nOrder Not Placed!\nPlease try again later!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: Null Observer fired!");
            }
        }
    };
    private final Observer<SplitOrderResponse> splitOrderResponseObserver = new Observer<SplitOrderResponse>() {
        @Override
        public void onChanged(SplitOrderResponse splitOrderResponse) {
            Log.i(TAG, "onChanged: SPLIT ORDER Observer fired!\nsplitOrderResponse: " + splitOrderResponse);

            if (splitOrderResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (!splitOrderResponse.getStatus().matches("ERROR")) {
                    Log.i(TAG, "onChanged: Split Order API post MDG Place Order Execute Success!");

                    DialogUtil.showCustomSnackbar(requireActivity(), cartSwipeRefreshLayout,
                            "Payment Completed!\nOrder Placed Successfully\nThank You for Shopping with us!", -1);
                } else {
                    Log.i(TAG, "onChanged: Split Order API post MDG Place Order Execute Failed!");
                    Toast.makeText(requireActivity(), "Payment Completed!\nOrder Placed with some difficulties." +
                            "\nThank You for Shopping with us!", Toast.LENGTH_SHORT).show();
                }

                /* HIT CART API OVER HERE AS WELL/MIGHT INSTEAD JUST EMPTY THE CART. */
              //  clearCart();
                orderSuccessful(true);
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };
    private void orderSuccessful(boolean isSuccessful/*, String orderSuccess*/) {
        Log.i(TAG, "orderSuccessful: fired! isSuccessful: " + isSuccessful);

        if (isSuccessful) {
            cartNestedScrollView.setVisibility(View.GONE);
            orderSuccessfulConstraintLayout.setVisibility(View.VISIBLE);
//            orderSuccessMaterialTextView.setText(orderSuccess);
        } else {
            cartNestedScrollView.setVisibility(View.VISIBLE);
            orderSuccessfulConstraintLayout.setVisibility(View.VISIBLE);
        }
    }
    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            if (!cartViewModel.getCartDetails()) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog(requireActivity());
            }

            cartSwipeRefreshLayout.setRefreshing(false);
        }
    };

}