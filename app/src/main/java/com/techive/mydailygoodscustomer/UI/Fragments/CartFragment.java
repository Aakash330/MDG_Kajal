package com.techive.mydailygoodscustomer.UI.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.exception.CFInvalidArgumentException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Adapters.CartCouponRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.CartFreebieRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptCartFragmentResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.SplitOrderResponse;
import com.techive.mydailygoodscustomer.Models.Cart;
import com.techive.mydailygoodscustomer.Models.Cart_CartData;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Coupons;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Freebies_Products;
import com.techive.mydailygoodscustomer.Models.CashFreeOrder;
import com.techive.mydailygoodscustomer.Models.PlaceOrderModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.ParentActivity;
import com.techive.mydailygoodscustomer.UI.Activities.ProductDetailsActivity;

import com.techive.mydailygoodscustomer.UI.Activities.StoreLocatorActivity;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.CartInterface;
import com.techive.mydailygoodscustomer.Util.DeliveryAddressInterface;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.LoginUtil;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;
import com.techive.mydailygoodscustomer.Util.onPaymentVerify;
import com.techive.mydailygoodscustomer.ViewModels.CartViewModel;
import com.techive.mydailygoodscustomer.cashfreeupdatestesting.CashFreeInitializationTestingActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements OnProductCartListener, CartCouponRecyclerViewAdapter.OnCartCouponListener,
        DeliveryAddressInterface, CFCheckoutResponseCallback, CartFreebieRecyclerViewAdapter.OnCartFreebieListener {
    private static final String TAG = "CartFragment";

    private SwipeRefreshLayout cartSwipeRefreshLayout;

    private MaterialTextView totalPriceMaterialTextView, savedPriceMaterialTextView, flatShippingCostMaterialTextView,
            totalPayableMaterialTextView, shopAddressMaterialTextView, shopMobNoMaterialTextView,
            customerDeliveryAddressMaterialTextView, availableFreebiesTitleMaterialTextView, availableCouponsTitleMaterialTextView,
    /*orderSuccessMaterialTextView,*/ cartTotalTitleMaterialTextView, totalPriceTitleMaterialTextView,
            savedPriceTitleMaterialTextView, flatShippingCostTitleMaterialTextView, totalPayableTitleMaterialTextView,
            shopAddressTitleMaterialTextView, shopMobNoTitleMaterialTextView, deliveryAddressTitleMaterialTextView;

    private MaterialCheckBox selfPickupAllowedMaterialCheckBox;

    private RecyclerView cartRecyclerView, cartCouponRecyclerView, cartFreebiesProductsRecyclerView, cartFreebieRecyclerView;

    private MaterialButton placeOrderMaterialButton, continueShoppingMaterialButton;

    private AppCompatImageView editDeliveryAddressAppCompatImageView, addDeliveryAddressAppCompatImageView;

    private NestedScrollView cartNestedScrollView;

    private ConstraintLayout cartIsEmptyConstraintLayout, orderSuccessfulConstraintLayout;

    private LinearLayoutCompat deliveryAddressLinearLayoutCompat;

    private CartInterface cartInterface;

    private CartViewModel cartViewModel;


    private DecimalFormat decimalFormat;

//    private MutableLiveData<String> postPaymentMutableLiveData;

    public CartFragment() {
        // Required empty public constructor
        Log.i(TAG, "CartFragment: Empty Constructor fired!");
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: fired!");

        if (context instanceof CartInterface) {
            cartInterface = (CartInterface) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement CartInterface.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: fired!");

      /*  try {
            // If you are using a fragment then you need to add this line inside onCreate() of your Fragment
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            e.printStackTrace();
        }*/


//        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        //TESTING (27-9-22)
//        if (postPaymentMutableLiveData == null) {
//            Log.i(TAG, "onCreate: postPaymentMutableLiveData was null. Initializing now.");
//            postPaymentMutableLiveData = new MutableLiveData<>();
//        } else {
//            Log.i(TAG, "onCreate: postPaymentMutableLiveData Not null. " + postPaymentMutableLiveData.toString());
//        }

//        try {
//            // If you are using a fragment then you need to add this line inside onCreate() of your Fragment
//            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
//        } catch (CFException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired!");
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);


        Log.i(TAG, "onViewCreated: cartViewModel.getCartMutableLiveData(): " + cartViewModel.getCartMutableLiveData());

        initAdapters();

        initObservers();

        initListeners();

//        if (ApplicationData.getLoggedInBuyerId() != 0) {
//            Log.i(TAG, "onViewCreated: Buyer already logged in! Retrieving Cart for buyer: "
//                    + ApplicationData.getLoggedInBuyerId());
//
//            if (!cartViewModel.getCartDetails()) {
//                DialogUtil.showNoInternetToast(requireActivity());
//            } else {
//                DialogUtil.showLoadingDialog(requireActivity());
//            }
//        } else {
//            Log.i(TAG, "onViewCreated: Buyer NOT logged in! Prompting to login...");
//
//            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to view your Cart.");
//        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fired!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: Cart fired!");

        orderSuccessful(false);

        /*CHECK FOR DIFFERENCE BETWEEN OLD ID & DEFAULT STORE ID IN APPLICATION DATA.*/
        Log.i(TAG, "onResume: DefaultStoreId = " + ApplicationData.getDefaultStoreId()
                + "\tApplicationData.getOldStoreIdCart() = " + ApplicationData.getOldStoreIdCart());
        if (ApplicationData.getDefaultStoreId() != ApplicationData.getOldStoreIdCart()) {
            Log.i(TAG, "onResume: OldStoreIdCart != DefaultStoreId");

            /*WILL ALSO HAVE TO CLEAR THE EXISTING DATA BOTH IN MEMORY & IN VIEW.*/
            clearCart();

            if (ApplicationData.getLoggedInBuyerId() != 0) {
                Log.i(TAG, "onResume: Buyer already logged in! Retrieving Cart for buyer: "
                        + ApplicationData.getLoggedInBuyerId());

                if (!cartViewModel.getCartDetails()) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showLoadingDialog(requireActivity());
                }

                ApplicationData.setOldStoreIdCart(ApplicationData.getDefaultStoreId());
            } else {
                Log.i(TAG, "onResume: Buyer NOT logged in! Prompting to login...");

                LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to view your Cart.");
            }

        } else {
            Log.i(TAG, "onResume: OldStoreIdCart = DefaultStoreId");
            Log.i(TAG, "onResume: cartViewModel.getCart(): " + cartViewModel.getCart());

            if (ApplicationData.getLoggedInBuyerId() != 0) {
                Log.i(TAG, "onResume: Buyer already logged in! Retrieving Cart for buyer: "
                        + ApplicationData.getLoggedInBuyerId());

                if (!cartViewModel.getCartDetails()) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showLoadingDialog(requireActivity());
                }

                ApplicationData.setOldStoreIdCart(ApplicationData.getDefaultStoreId());
            } else {
                Log.i(TAG, "onResume: Buyer NOT logged in! Prompting to login...");

                LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to view your Cart.");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");

        if (cartViewModel.getAddToCartMutableLiveData().getValue() != null) {
            cartViewModel.setAddToCartMutableLiveData(null);
        }
        if (cartViewModel.getRemoveFromCartMutableLiveData().getValue() != null) {
            cartViewModel.setRemoveFromCartMutableLiveData(null);
        }
        if (cartViewModel.getCompletelyRemoveFromCartMutableLiveData().getValue() != null) {
            cartViewModel.setCompletelyRemoveFromCartMutableLiveData(null);
        }
        if (cartViewModel.getCashFreeOrderMutableLiveData().getValue() != null) {
            cartViewModel.setCashFreeOrderMutableLiveData(null);
        }
        if (cartViewModel.getPlaceOrderMutableLiveData().getValue() != null) {
            cartViewModel.setPlaceOrderMutableLiveData(null);
        }
        if (cartViewModel.getSplitOrderResponseMutableLiveData().getValue() != null) {
            cartViewModel.setSplitOrderResponseMutableLiveData(null);
        }
        if (cartViewModel.getOrderAcceptCartFragmentResponseMutableLiveData().getValue() != null) {
            cartViewModel.setOrderAcceptResponseMutableLiveData(null);
        }

        if (cartViewModel.getProdAddAcceptResponseMutableLiveData().getValue() != null) {
            cartViewModel.setProdAddAcceptResponseMutableLiveData(null);
        }

        if (((ParentActivity) requireActivity()).paymentStatusMutableLiveData.getValue() != null) {
            ((ParentActivity) getActivity()).paymentStatusMutableLiveData.setValue(null);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: fired!");

        cartViewModel.setCartMutableLiveData(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: fired!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: fired!");
    }

    private void initComponentViews(View view) {
        Log.i(TAG, "initComponentViews: fired!");

        cartSwipeRefreshLayout = view.findViewById(R.id.cartSwipeRefreshLayout);

        totalPriceMaterialTextView = view.findViewById(R.id.totalPriceMaterialTextView);
        savedPriceMaterialTextView = view.findViewById(R.id.savedPriceMaterialTextView);
        flatShippingCostMaterialTextView = view.findViewById(R.id.flatShippingCostMaterialTextView);
        totalPayableMaterialTextView = view.findViewById(R.id.totalPayableMaterialTextView);
        shopAddressMaterialTextView = view.findViewById(R.id.shopAddressMaterialTextView);
        shopMobNoMaterialTextView = view.findViewById(R.id.shopMobNoMaterialTextView);
        customerDeliveryAddressMaterialTextView = view.findViewById(R.id.customerDeliveryAddressMaterialTextView);
        availableFreebiesTitleMaterialTextView = view.findViewById(R.id.availableFreebiesTitleMaterialTextView);
        availableCouponsTitleMaterialTextView = view.findViewById(R.id.availableCouponsTitleMaterialTextView);
//        orderSuccessMaterialTextView = view.findViewById(R.id.orderSuccessMaterialTextView);
        cartTotalTitleMaterialTextView = view.findViewById(R.id.cartTotalTitleMaterialTextView);
        totalPriceTitleMaterialTextView = view.findViewById(R.id.totalPriceTitleMaterialTextView);
        savedPriceTitleMaterialTextView = view.findViewById(R.id.savedPriceTitleMaterialTextView);
        flatShippingCostTitleMaterialTextView = view.findViewById(R.id.flatShippingCostTitleMaterialTextView);
        totalPayableTitleMaterialTextView = view.findViewById(R.id.totalPayableTitleMaterialTextView);
        shopAddressTitleMaterialTextView = view.findViewById(R.id.shopAddressTitleMaterialTextView);
        shopMobNoTitleMaterialTextView = view.findViewById(R.id.shopMobNoTitleMaterialTextView);
        deliveryAddressTitleMaterialTextView = view.findViewById(R.id.deliveryAddressTitleMaterialTextView);

        selfPickupAllowedMaterialCheckBox = view.findViewById(R.id.selfPickupAllowedMaterialCheckBox);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartCouponRecyclerView = view.findViewById(R.id.cartCouponRecyclerView);
        cartFreebiesProductsRecyclerView = view.findViewById(R.id.cartFreebiesProductsRecyclerView);
        cartFreebieRecyclerView = view.findViewById(R.id.cartFreebieRecyclerView);

        placeOrderMaterialButton = view.findViewById(R.id.placeOrderMaterialButton);
        continueShoppingMaterialButton = view.findViewById(R.id.continueShoppingMaterialButton);

        editDeliveryAddressAppCompatImageView = view.findViewById(R.id.editDeliveryAddressAppCompatImageView);
        addDeliveryAddressAppCompatImageView = view.findViewById(R.id.addDeliveryAddressAppCompatImageView);

        cartNestedScrollView = view.findViewById(R.id.cartNestedScrollView);

        cartIsEmptyConstraintLayout = view.findViewById(R.id.cartIsEmptyConstraintLayout);
        orderSuccessfulConstraintLayout = view.findViewById(R.id.orderSuccessfulConstraintLayout);

        deliveryAddressLinearLayoutCompat = view.findViewById(R.id.deliveryAddressLinearLayoutCompat);

        decimalFormat = new DecimalFormat("#.#");
    }


    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        cartRecyclerView.setAdapter(cartViewModel.cartRecyclerViewAdapter);

        cartCouponRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        cartCouponRecyclerView.setAdapter(cartViewModel.cartCouponRecyclerViewAdapter);

        cartFreebiesProductsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        cartFreebiesProductsRecyclerView.setAdapter(cartViewModel.cartFreebieProductsRecyclerViewAdapter);

        cartFreebieRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        cartFreebieRecyclerView.setAdapter(cartViewModel.cartFreebieRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        cartViewModel.getCartMutableLiveData().observe(getActivity(), cartObserver);
        cartViewModel.getAddToCartMutableLiveData().observe(getActivity(), addToCartObserver);
        cartViewModel.getRemoveFromCartMutableLiveData().observe(getActivity(), removeFromCartObserver);
        cartViewModel.getCompletelyRemoveFromCartMutableLiveData().observe(getActivity(), completelyRemoveFromCartObserver);
        cartViewModel.getPlaceOrderMutableLiveData().observe(getActivity(), placeOrderObserver);
        cartViewModel.getCashFreeOrderMutableLiveData().observe(getActivity(), cashFreeOrderObserver);
        cartViewModel.getSplitOrderResponseMutableLiveData().observe(getActivity(), splitOrderResponseObserver);
       // cartViewModel.getOrderAcceptResponseMutableLiveData().observe(getActivity(), orderAcceptResponseObserver);
        cartViewModel.getOrderAcceptCartFragmentResponseMutableLiveData().observe(getActivity(), orderAcceptResponseObserver);//@kajal 11_16_22
        cartViewModel.getProdAddAcceptResponseMutableLiveData().observe(getActivity(), prodAddAcceptResponseObserver);

//        postPaymentMutableLiveData.observe(this, postPaymentObserver);

        //Testing (28-9-22) - Success after increasing Cashfree SDK version.
       ((ParentActivity) getActivity()).paymentStatusMutableLiveData.observe(getActivity(), postPaymentObserver);//@kajal_12_2_22 checkD

    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        cartViewModel.cartRecyclerViewAdapter.setOnProductCartListener(this);
        cartViewModel.cartCouponRecyclerViewAdapter.setOnCartCouponListener(this);
        cartViewModel.cartFreebieRecyclerViewAdapter.setOnCartFreebieListener(this);
        cartViewModel.cartFreebieProductsRecyclerViewAdapter.setOnProductCartListener(this);
        cartSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        placeOrderMaterialButton.setOnClickListener(onClickListener);
        editDeliveryAddressAppCompatImageView.setOnClickListener(onClickListener);
        addDeliveryAddressAppCompatImageView.setOnClickListener(onClickListener);
        selfPickupAllowedMaterialCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        continueShoppingMaterialButton.setOnClickListener(redirectHomeOnClickListener);
    }

    private final Observer<Cart> cartObserver = new Observer<Cart>() {
        @Override
        public void onChanged(Cart cart) {
            Log.i(TAG, "onChanged: VIEW CART DETAILS Observer fired!\ncart: " + cart);

            if (cart != null) {
                DialogUtil.dismissLoadingDialog();

                if (cart.getError() == 200) {
                    emptyCart(false);
                    cartViewModel.setCart(cart);
                    cartViewModel.cartRecyclerViewAdapter.setCart_cartData_dataList(cart.getCart_data().getData());
                    cartViewModel.cartCouponRecyclerViewAdapter.setCart_cartData_couponsList(cart.getCart_data().getCoupons());
                    cartViewModel.cartFreebieRecyclerViewAdapter.setCart_cartData_freebiesList(cart.getCart_data().getFreebies());

//                cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(cartViewModel.checkBestApplicableFreebie());
//                cartViewModel.cartFreebieRecyclerViewAdapter.setLastCheckedPosition(cartViewModel.bestApplicableFreebieIndex);
                    cartViewModel.cartFreebieRecyclerViewAdapter.setLastCheckedPosition(cartViewModel.checkBestApplicableFreebie());
                    cartViewModel.cartCouponRecyclerViewAdapter.setLastCheckedPosition(cartViewModel.checkBestApplicableCoupon());

                    if (cart.getCart_data().getCoupons() == null) {
                        availableCouponsTitleMaterialTextView.setVisibility(View.GONE);
                        cartCouponRecyclerView.setVisibility(View.GONE);
                    } else {
                        if (cart.getCart_data().getCoupons().size() == 0) {
                            availableCouponsTitleMaterialTextView.setVisibility(View.GONE);
                            cartCouponRecyclerView.setVisibility(View.GONE);
                        } else {
                            availableCouponsTitleMaterialTextView.setVisibility(View.VISIBLE);
                            cartCouponRecyclerView.setVisibility(View.VISIBLE);
                            availableCouponsTitleMaterialTextView.setText(getString(R.string.available_coupons));
                        }
                    }

                    if (cart.getCart_data().getFreebies() == null) {
                        availableFreebiesTitleMaterialTextView.setVisibility(View.GONE);
                        cartFreebieRecyclerView.setVisibility(View.GONE);
                    } else {
                        if (cart.getCart_data().getFreebies().size() == 0) {
                            availableFreebiesTitleMaterialTextView.setVisibility(View.GONE);
                            cartFreebieRecyclerView.setVisibility(View.GONE);
                        } else {
                            availableFreebiesTitleMaterialTextView.setVisibility(View.VISIBLE);
                            cartFreebieRecyclerView.setVisibility(View.VISIBLE);
                            availableFreebiesTitleMaterialTextView.setText(getString(R.string.available_freebies));
                        }
                    }

                    displayCartData(cart.getCart_data());

                    cartTotalTitleMaterialTextView.setText(R.string.cart_total);
                    totalPriceTitleMaterialTextView.setText(R.string.total_price);
                    savedPriceTitleMaterialTextView.setText(R.string.you_saved);
                    flatShippingCostTitleMaterialTextView.setText(R.string.flat_shipping_cost);
                    totalPayableTitleMaterialTextView.setText(R.string.total_payable);
                    shopAddressTitleMaterialTextView.setText(R.string.shop_address);
                    shopMobNoTitleMaterialTextView.setText(R.string.shop_mob_no);
                    deliveryAddressTitleMaterialTextView.setText(R.string.delivery_address);

                    placeOrderMaterialButton.setEnabled(true);
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while fetching cart details.");

                    if (cart.getError() == 400) {   //CART IS EMPTY!
//                        clearCart();
                        emptyCart(true);
                        return;
                    }
                    Toast.makeText(requireActivity(), cart.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> addToCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: ADD TO CART Observer fired!\ngeneralResponse: " + generalResponse);
   try{
    if (generalResponse != null) {
        if (generalResponse.getError() == 200) {

            if(generalResponse.getProd_id()==null)
                return;
                    Log.i(TAG, "onChanged: cartViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));
                    cartViewModel.cartRecyclerViewAdapter.notifyProductAddedInCart(
                            Integer.parseInt(generalResponse.getProd_id()),
                            cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                            true, cartViewModel.updateCartDataUponAdd(Integer.parseInt(generalResponse.getProd_id())));
            //UPDATE PROD ID & QTY IN APPLICATION DATA.
            Log.i(TAG, "onChanged: CART, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
            HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
            prodIdQtyHashMap.put(Integer.parseInt(generalResponse.getProd_id()),
                    cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));
            ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
            Log.i(TAG, "onChanged: CART, AFTER, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());

            displayCartData(cartViewModel.getCart().getCart_data());
        } else {
            Log.i(TAG, "onChanged: Something went wrong while adding product to cart from Cart!");
            Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            cartViewModel.cartRecyclerViewAdapter.notifyProductAddedInCart(
                    Integer.parseInt(generalResponse.getProd_id()),
                    cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                    false, null);
        }
    } else {
        Log.i(TAG, "onChanged: 1st time observer fired!");
    }

}catch (Exception ee){}

        }
    };

    private final Observer<GeneralResponse> removeFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: REMOVE FROM CART Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {

                    try{
                        if(cartViewModel.getCart().getCart_data()==null)
                            return;
                        //@kajal 11_16_22
                    Log.i(TAG, "onChanged: cartViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));
                    cartViewModel.cartRecyclerViewAdapter.notifyProductRemovedFromCart(
                            Integer.parseInt(generalResponse.getProd_id()),
                            cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                            true, cartViewModel.updateCartDataUponRemove(Integer.parseInt(generalResponse.getProd_id())));
                        displayCartData(cartViewModel.getCart().getCart_data());
                    }catch (Exception ee){

                    }

                } else {
                    Log.i(TAG, "onChanged: Something went wrong while removing product from cart from Cart!");
                    Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    cartViewModel.cartRecyclerViewAdapter.notifyProductRemovedFromCart(
                            Integer.parseInt(generalResponse.getProd_id()),
                            cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                            false, null);
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> completelyRemoveFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: COMPLETELY REMOVE FROM CART Observer fired!\ngeneralResponse: " + generalResponse);
try{
    if (generalResponse != null) {
        if (generalResponse.getError() == 200) {
            Log.i(TAG, "onChanged: cartViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));
            cartViewModel.updateCartDataUponCompletelyRemove(Integer.parseInt(generalResponse.getProd_id()));
            cartViewModel.cartRecyclerViewAdapter.notifyProductCompletelyRemovedFromCart();

            displayCartData(cartViewModel.getCart().getCart_data());
        } else {
            Log.i(TAG, "onChanged: Something went wrong while completely removing product from cart from Cart!");
            Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();

            cartViewModel.cartRecyclerViewAdapter.notifyProductRemovedFromCart(
                    Integer.parseInt(generalResponse.getProd_id()),
                    cartViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                    false, null);
        }
    } else {
        Log.i(TAG, "onChanged: 1st time observer fired!");
    }
}catch (Exception ee){}

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
               // DialogUtil.dismissProcessingInfoDialog();
              //  Toast.makeText(getContext(), "null...", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onChanged: 1st time Observer fired!");
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
                clearCart();
                orderSuccessful(true);
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    //private final Observer<OrderAcceptResponse> orderAcceptResponseObserver = new Observer<OrderAcceptResponse>() {
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

                        selectPaymentMode();
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

    private final Observer<OrderAcceptResponse> prodAddAcceptResponseObserver = new Observer<OrderAcceptResponse>() {
        @Override
        public void onChanged(OrderAcceptResponse orderAcceptResponse) {
            Log.i(TAG, "onChanged: PROD ADD ACCEPTANCE Observer fired!\norderAcceptResponse: " + orderAcceptResponse);

            if (orderAcceptResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (orderAcceptResponse.getError() == 200) {
                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getStatus() != 0) {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't add to cart.");
                        DialogUtil.showCustomSnackbar(requireActivity(), cartSwipeRefreshLayout, orderAcceptResponse.getMsg(), -1);

                        cartViewModel.cartRecyclerViewAdapter.notifyProductAddedInCart(cartViewModel.toBeAddedProductId,
                                cartViewModel.toBeAddedProductQty, false, null);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Prod Add Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    cartViewModel.cartRecyclerViewAdapter.notifyProductAddedInCart(cartViewModel.toBeAddedProductId,
                            cartViewModel.toBeAddedProductQty, false, null);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };

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

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");

            if (ApplicationData.getLoggedInBuyerId() != 0) {
                Log.i(TAG, "onClick: Buyer already logged in! Proceeding further for buyer: "
                        + ApplicationData.getLoggedInBuyerId());

                if (view.getId() == addDeliveryAddressAppCompatImageView.getId()) {

//                FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    DeliveryAddressDialogFragment deliveryAddressDialogFragment = new DeliveryAddressDialogFragment();

                    // The device is smaller, so show the fragment fullscreen
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // For a little polish, specify a transition animation
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    // To make it fullscreen, use the 'content' root view as the container
                    // for the fragment, which is always the root view for the activity
                    fragmentTransaction.add(android.R.id.content, deliveryAddressDialogFragment/*, "DELIVERY_ADDRESS_DIALOG_FRAGMENT"*/)
                            .addToBackStack(null)
                            .commit();

                    ApplicationData.setDeliveryAddressDialogFragment(deliveryAddressDialogFragment);

                } else if (view.getId() == editDeliveryAddressAppCompatImageView.getId()) {

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    DeliveryAddressListDialogFragment deliveryAddressListDialogFragment = new DeliveryAddressListDialogFragment();

                    deliveryAddressListDialogFragment.setDeliveryAddressInterface(CartFragment.this);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.add(android.R.id.content, deliveryAddressListDialogFragment)
                            .addToBackStack(null)
                            .commit();

                    ApplicationData.setDeliveryAddressListDialogFragment(deliveryAddressListDialogFragment);

                } else if (view.getId() == placeOrderMaterialButton.getId()) {


                    /* NEW FLOW AS OF 22-6-22. */
                    if (!cartViewModel.checkVendorOrderAcceptance()) {
                        Log.w(TAG,"PlaceOrderBtn CartFragment if");
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        Log.w(TAG,"PlaceOrderBtn CartFragment else");
                        DialogUtil.showProcessingInfoDialog(requireActivity());
                    }

//                    selectPaymentMode();
                }
            } else {
                Log.i(TAG, "onClick: Buyer NOT logged in! Prompting to login...");

                LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to view your Cart.");
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.i(TAG, "onCheckedChanged: clicked! checked: " + b);

            int flatShippingCost = 0;

            if (cartViewModel.getCart().getCart_data().getShipping_data().getFree_del_odr() > 0) {
                //Upto some cart value, flat shipping cost will be applied.
                if (Float.parseFloat(cartViewModel.getCart().getCart_data().getTotal_price()) >
                        cartViewModel.getCart().getCart_data().getShipping_data().getFree_del_odr()) {
                    flatShippingCost = 0;
                } else {
                    flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
                }
            } else {
                //flat shipping cost will always be applied
                flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
            }
            Log.i(TAG, "onCheckedChanged: flatShippingCost: " + flatShippingCost);

//            if (cartViewModel.getCart().getCart_data().getShipping_data() != null) {
//                flatShippingCost = cartViewModel.getCart().getCart_data().getShipping_data().getFlat_sh_cost();
//            }

            float totalPayablePrice = Float.parseFloat(totalPayableMaterialTextView.getText().toString().substring(1).trim());
//            int totalPayablePrice = Integer.parseInt(totalPayableMaterialTextView.getText().toString().substring(1).trim());
            Log.i(TAG, "onCheckedChanged: totalPayablePrice: " + totalPayablePrice);

            if (b) {
                flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");

                totalPayableMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + (totalPayablePrice - flatShippingCost));
            } else {
                flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + flatShippingCost);

                totalPayableMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + (totalPayablePrice + flatShippingCost));
            }
        }
    };

    private final View.OnClickListener redirectHomeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");
            ((ParentActivity) getActivity()).openHomeFragment();
        }
    };

    private void displayCartData(Cart_CartData cart_cartData) {
        Log.i(TAG, "displayCartData: fired!");

        int totalQty = 0, flatShippingCost = 0;
        float cartTotalPrice, cartTotalSavings;
        for (Cart_CartData_Data cart_cartData_data : cart_cartData.getData()) {
            totalQty = totalQty + cart_cartData_data.getQty();
        }
        cartInterface.showBadgeOnCart(totalQty);

        if (totalQty == 0) {
            cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
            cartViewModel.cartFreebieRecyclerViewAdapter.setCart_cartData_freebiesList(null);
            cartViewModel.cartCouponRecyclerViewAdapter.setCart_cartData_couponsList(null);
            placeOrderMaterialButton.setEnabled(false);
            emptyCart(true);
            return;
        }

        cartTotalPrice = Float.parseFloat(cart_cartData.getTotal_price());
        cartTotalSavings = Float.parseFloat(cart_cartData.getTotal_savings());
        Log.i(TAG, "displayCartData: cartTotalPrice: " + cartTotalPrice + "  cartTotalSavings: " + cartTotalSavings);

//        totalPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + ((int) Math.ceil(cartTotalPrice + cartTotalSavings)));
        totalPriceMaterialTextView.setText("\u20B9" + " " + (decimalFormat.format(cartTotalPrice + cartTotalSavings)));
        Log.i(TAG, "displayCartData: decimalFormat.format(cartTotalPrice + cartTotalSavings): " + decimalFormat.format(cartTotalPrice + cartTotalSavings));

        //REMOVING FREEBIE SELECTION AS SOON AS CART TOTAL PRICE GOES BELOW SELECTED FREEBIE MIN CART VALUE.
        if (cartTotalPrice < cartViewModel.freebieMinCartValue) {
            cartViewModel.cartFreebieRecyclerViewAdapter.uncheckCurrentFreebie();
            cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
        }

        //REMOVING COUPON SELECTION AS SOON AS CART TOTAL PRICE GOES BELOW SELECTED COUPON MIN CART VALUE.
        if (!cartViewModel.selectedCouponType.matches("")) {
            //SOME COUPON APPLIED
            if (cartTotalPrice >= cartViewModel.selectedCouponMinSpend) {
                if (cartViewModel.selectedCouponType.matches("fcd")) {  //FIXED COUPON DISCOUNT
                    Log.i(TAG, "displayCartData: Applying Fixed Coupon Discount Coupon!");
                    cartTotalPrice = cartTotalPrice - cartViewModel.selectedCouponAmount;
                    cartTotalSavings = cartTotalSavings + cartViewModel.selectedCouponAmount;
                    cartViewModel.appliedCouponDiscount = cartViewModel.selectedCouponAmount;
                } else {                                                      //PERCENTAGE DISCOUNT
                    Log.i(TAG, "displayCartData: Applying Percentage Discount Coupon!");
                    float percentOfTotalPrice = (cartViewModel.selectedCouponAmount * cartTotalPrice) / 100;
                    Log.i(TAG, "displayCartData: percentOfTotalPrice: " + percentOfTotalPrice);
                    if (percentOfTotalPrice > cartViewModel.selectedCouponMaxDiscount) {
                        cartTotalPrice = cartTotalPrice - cartViewModel.selectedCouponMaxDiscount;
                        cartTotalSavings = cartTotalSavings + cartViewModel.selectedCouponMaxDiscount;
                        cartViewModel.appliedCouponDiscount = cartViewModel.selectedCouponMaxDiscount;
                    } else {
                        cartTotalPrice = cartTotalPrice - percentOfTotalPrice;
                        cartTotalSavings = cartTotalSavings + percentOfTotalPrice;
                        cartViewModel.appliedCouponDiscount = percentOfTotalPrice;
                    }
                }
            } else {
                Log.i(TAG, "displayCartData: Removing coupon because Total Cart price went below min spend of coupon.");
                cartViewModel.cartCouponRecyclerViewAdapter.uncheckCurrentCoupon();
                cartViewModel.appliedCouponDiscount = 0f;
            }
        } else {
            Log.i(TAG, "displayCartData: No Coupon applied yet!");
            //NO COUPON APPLIED
        }

        //DISPLAY OTHER DETAILS OF CART
        Log.i(TAG, "displayCartData: cartTotalPrice: " + cartTotalPrice);
        Log.i(TAG, "displayCartData: cartTotalSavings: " + cartTotalSavings);
        Log.i(TAG, "displayCartData: Math.ceil(cartTotalPrice): " + Math.ceil(cartTotalPrice));
        Log.i(TAG, "displayCartData: Math.ceil(cartTotalSavings): " + Math.ceil(cartTotalSavings));

//        savedPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + ((int) Math.ceil(cartTotalSavings)));
       // savedPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + (decimalFormat.format(cartTotalSavings)));//@kajal12_9_22
        savedPriceMaterialTextView.setText("\u20B9"+ " " + (decimalFormat.format(cartTotalSavings)));
        Log.i(TAG, "displayCartData: decimalFormat.format(cartTotalSavings): " + decimalFormat.format(cartTotalSavings));

        //Repeated check for shipping data null. Merged in one
//        if (cart_cartData.getShipping_data() != null) {
//            flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + cart_cartData.getShipping_data().getFlat_sh_cost());
//        } else {
//            flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
//        }

        //Repeated check for shipping data null. Merged in one
//        if (cart_cartData.getShipping_data() != null) {
//            if (cart_cartData.getShipping_data().getAllow_self_pickup().matches("0")) {
//                selfPickupAllowedMaterialCheckBox.setVisibility(View.GONE);
//            } else {
//                selfPickupAllowedMaterialCheckBox.setVisibility(View.VISIBLE);
//            }
//        }

        //Improved on 23-8-22
        if (cart_cartData.getShipping_data() != null) {

            if (cart_cartData.getShipping_data().getAllow_self_pickup().matches("0")) {
                if (selfPickupAllowedMaterialCheckBox.isChecked()) {
                    selfPickupAllowedMaterialCheckBox.setChecked(false);
                }
                selfPickupAllowedMaterialCheckBox.setVisibility(View.GONE);
            } else {
                selfPickupAllowedMaterialCheckBox.setVisibility(View.VISIBLE);
            }

            if (cart_cartData.getShipping_data().getFree_del_odr() > 0) {
                //Upto some cart value, flat shipping cost will be applied.
                if (Float.parseFloat(cart_cartData.getTotal_price()) > cart_cartData.getShipping_data().getFree_del_odr()) {
                    flatShippingCost = 0;
                } else {
                    flatShippingCost = cart_cartData.getShipping_data().getFlat_sh_cost();
                }
            } else {
                //flat shipping cost will always be applied
                flatShippingCost = cart_cartData.getShipping_data().getFlat_sh_cost();
            }
            Log.i(TAG, "displayCartData: flatShippingCost: " + flatShippingCost);

            if (!selfPickupAllowedMaterialCheckBox.isChecked()) {
                //flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + decimalFormat.format(flatShippingCost));
                flatShippingCostMaterialTextView.setText("\u20B9" + " " + decimalFormat.format(flatShippingCost));
            }

            if (cart_cartData.getShipping_data().getHomedeliver() == 1) {
                /* MAKE VISIBLE THE DELIVERY ADDRESS SECTION */
                deliveryAddressLinearLayoutCompat.setVisibility(View.VISIBLE);

                /*Delivery Address*/
                StringBuilder deliveryAddressStringBuilder = new StringBuilder();
                if (cart_cartData.getBuyerDefaultAddress() != null) {
                    if (cart_cartData.getBuyerDefaultAddress().getName() != null) {
                        deliveryAddressStringBuilder.append(cart_cartData.getBuyerDefaultAddress().getName()).append(" ");
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getLname() != null) {
                        deliveryAddressStringBuilder.append(cart_cartData.getBuyerDefaultAddress().getLname());
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getMobile() != null) {
                        deliveryAddressStringBuilder.append("\n").append(cart_cartData.getBuyerDefaultAddress().getMobile());
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getAddress() != null) {
                        deliveryAddressStringBuilder.append("\n").append(cart_cartData.getBuyerDefaultAddress().getAddress());
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getLandmark() != null) {
                        deliveryAddressStringBuilder.append("\nLandmark: ").append(cart_cartData.getBuyerDefaultAddress().getLandmark());
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getCity() != null) {
                        deliveryAddressStringBuilder.append("\n").append(cart_cartData.getBuyerDefaultAddress().getCity());
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getState() != null) {
                        if (cart_cartData.getBuyerDefaultAddress().getCity() != null) {
                            deliveryAddressStringBuilder.append(", ").append(cart_cartData.getBuyerDefaultAddress().getState());
                        } else {
                            deliveryAddressStringBuilder.append("\n").append(cart_cartData.getBuyerDefaultAddress().getState());
                        }
                    }
                    if (cart_cartData.getBuyerDefaultAddress().getPincode() != null) {
                        if (cart_cartData.getBuyerDefaultAddress().getCity() != null || cart_cartData.getBuyerDefaultAddress().getState() != null) {
                            deliveryAddressStringBuilder.append(", ").append(cart_cartData.getBuyerDefaultAddress().getPincode());
                        } else {
                            deliveryAddressStringBuilder.append("\n").append(cart_cartData.getBuyerDefaultAddress().getPincode());
                        }
                    }
                    customerDeliveryAddressMaterialTextView.setText(deliveryAddressStringBuilder.toString());
                } else {
                    customerDeliveryAddressMaterialTextView.setText("No Delivery Address available! Please add an address!");
                }

            } else {
                /* DISAPPEAR DELIVERY ADDRESS SECTION. NO ADD OR EDIT ALLOWED. */
                deliveryAddressLinearLayoutCompat.setVisibility(View.GONE);
                customerDeliveryAddressMaterialTextView.setText("No Home Delivery ServiceTesting available currently. You could pickup your order from above mentioned shop address.");
            }
        }
        //Shipping data is never null.
        /*else {
            deliveryAddressLinearLayoutCompat.setVisibility(View.GONE);
            customerDeliveryAddressMaterialTextView.setText("No Home Delivery ServiceTesting available currently. You could pickup your order from above mentioned shop address.");

        }*/


        float totalPayable;
        if (selfPickupAllowedMaterialCheckBox.isChecked()) {
            totalPayable = cartTotalPrice;
        } else {
            totalPayable = cartTotalPrice + flatShippingCost;
        }
//        totalPayableMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + ((int) Math.ceil(totalPayable)));
        totalPayableMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " " + (decimalFormat.format(totalPayable)));
        Log.i(TAG, "displayCartData: decimalFormat.format(totalPayable): " + decimalFormat.format(totalPayable));

        shopAddressMaterialTextView.setText(cart_cartData.getShipping_data().getAddress() + ", " + cart_cartData.getShipping_data().getPin());
        shopMobNoMaterialTextView.setText(cart_cartData.getShipping_data().getMob_no1());
    }

    private void selectPaymentMode() {
        Log.i(TAG, "selectPaymentMode: fired!");

        String[] arrayItems /*= new String[]{"Cash On Delivery (COD)", "Online Payment"}*/;

        if (cartViewModel.getCart().getCart_data().getVendors_accounts_data() != null) {
            //VENDOR ACCOUNT DATA AVAILABLE
            if (cartViewModel.getCart().getCart_data().getShipping_data().getCod().matches("0")) {
                //NO COD AVAILABLE
                arrayItems = new String[]{"Online Payment"};
            } else {
                //COD AVAILABLE
                arrayItems = new String[]{"Cash On Delivery (COD)", "Online Payment"};
            }
        } else {
            //VENDOR ACCOUNT DATA NOT AVAILABLE
            arrayItems = new String[]{"Cash On Delivery (COD)"};
        }

        String[] finalArrayItems = arrayItems;
        DialogInterface.OnClickListener dialogInterfaceOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (finalArrayItems[i].equals("Cash On Delivery (COD)")) {
                    Log.i(TAG, "onClick: COD was selected!");

                    //TESTING SUCCESS - Order Placed!
                    cartViewModel.codPayment = true;
                    placeOrder();
                } else {
                    Log.i(TAG, "onClick: Online Payment was selected!");

                    String totalOrderAmount = totalPayableMaterialTextView.getText().toString();
                    Log.i(TAG, "onClick: totalOrderAmount: " + totalOrderAmount);
                    totalOrderAmount = totalOrderAmount.substring(1);
                    Log.i(TAG, "onClick: totalOrderAmount substring: " + totalOrderAmount);
                    Log.i(TAG, "onClick: cartViewModel.getCart().getCart_data().getTotal_price(): " + cartViewModel.getCart().getCart_data().getTotal_price());

                    //TESTING SUCCESS - CashFree order created!
//                    if (!cartViewModel.createCashFreeOrder(Float.parseFloat(cartViewModel.getCart().getCart_data().getTotal_price()))) {
                    if (!cartViewModel.createCashFreeOrder(Float.parseFloat(totalOrderAmount))) {
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        DialogUtil.showProcessingInfoDialog(requireActivity());
                    }

//                    initiateOnlinePayment("VQwl3LAMjdKcnFvgmYX9", "123456");

                    //TESTING SUCCESS - Order Placed!
//                    placeOrder();

                    //NEW FLOW AS OF 27-5-22
//                    if (!cartViewModel.createCashFreeOrder(Float.parseFloat(
//                            cartViewModel.getCart().getCart_data().getTotal_price()), generateUniqueOrderId())) {
//                        DialogUtil.showNoInternetToast(requireActivity());
//                    } else {
//                        DialogUtil.showProcessingInfoDialog(requireActivity());
//                    }
                }
            }
        };

        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Select Mode of Payment")
                .setItems(arrayItems, dialogInterfaceOnClickListener)
                .setIcon(requireContext().getDrawable(R.drawable.ic_payment_24))
                .show();
    }

    private void initiateOnlinePayment(String orderToken, String orderId) {
        Log.i(TAG, "initiateOnlinePayment: fired! orderToken: " + orderToken + "\torderId: " + orderId);

        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
//                    .setEnvironment(CFSession.Environment.SANDBOX)
                    .setEnvironment(CFSession.Environment.PRODUCTION)
                   .setOrderToken(orderToken)
//                    .setOrderToken("VQwl3LAMjdKcnFvgmYX9")
//                  .setPaymentSessionID(orderToken)
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
            gatewayService.doPayment(requireActivity(), cfDropCheckoutPayment);

        } catch (CFInvalidArgumentException cfInvalidArgumentException) {
            Log.i(TAG, "initiateOnlinePayment: cfInvalidArgumentException.getMessage(): " + cfInvalidArgumentException.getMessage());
            cfInvalidArgumentException.printStackTrace();
        } catch (Exception exception) {
            Log.i(TAG, "initiateOnlinePayment: exception.getMessage(): " + exception.getMessage());
            exception.printStackTrace();
        }
    }

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
        }

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

        PlaceOrderModel placeOrderModel = new PlaceOrderModel(ApplicationData.getLoggedInBuyerId(),
                ApplicationData.getDefaultStoreId(), selfPickup, appliedCouponDiscount, flatShippingCost,
                couponId, freebieId, paymentStatus, orderId);
        Log.i(TAG, "placeOrder: placeOrderModel: " + placeOrderModel);

        if (!cartViewModel.placeOrder(placeOrderModel)) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            DialogUtil.showProcessingInfoDialog(requireActivity());
        }
    }

    private void clearCart() {
        Log.i(TAG, "clearCart: fired!");

        cartViewModel.cartRecyclerViewAdapter.setCart_cartData_dataList(null);
        cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
        cartViewModel.cartCouponRecyclerViewAdapter.setCart_cartData_couponsList(null);
        cartViewModel.cartFreebieRecyclerViewAdapter.setCart_cartData_freebiesList(null);

        cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
        ApplicationData.setProdIdOrderQtyHashMap(new HashMap<>());

        totalPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
        savedPriceMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
        flatShippingCostMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
        totalPayableMaterialTextView.setText(requireActivity().getString(R.string.rupee_symbol) + " 0");
        shopAddressMaterialTextView.setText("");
        shopMobNoMaterialTextView.setText("");
        customerDeliveryAddressMaterialTextView.setText("");

        placeOrderMaterialButton.setEnabled(false);
    }

    private void emptyCart(boolean isEmpty) {
        Log.i(TAG, "emptyCart: fired! isEmpty: " + isEmpty);

        if (isEmpty) {
            cartNestedScrollView.setVisibility(View.GONE);
            cartIsEmptyConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            cartNestedScrollView.setVisibility(View.VISIBLE);
            cartIsEmptyConstraintLayout.setVisibility(View.GONE);
        }
    }

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

    @Override
    public void addProductToCart(int productId, int qty) {
        Log.i(TAG, "addProductToCart: fired!\tproductId: " + productId + "\tqty: " + qty);

        if (!cartViewModel.checkVendorOrderAcceptance(productId, qty)) {
            DialogUtil.showNoInternetToast(requireActivity());

            Log.i(TAG, "addProductToCart: BEFORE, cartViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.toString());
            cartViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
            Log.i(TAG, "addProductToCart: AFTER, cartViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.toString());
            cartViewModel.cartRecyclerViewAdapter.notifyProductAddedInCart(
                    productId, qty, false, null);
        } else {
            DialogUtil.showProcessingInfoDialog(requireActivity());
        }

//        if (!cartViewModel.addToCart(productId, qty)) {
//            DialogUtil.showNoInternetToast(requireActivity());
//
//            Log.i(TAG, "addProductToCart: BEFORE, cartViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//            cartViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
//            Log.i(TAG, "addProductToCart: AFTER, cartViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//            cartViewModel.cartRecyclerViewAdapter.notifyProductAddedInCart(
//                    productId, qty, false, null);
//        }
    }

    @Override
    public void removeProductFromCart(int productId, int qty) {
        Log.i(TAG, "removeProductFromCart: fired!\tproductId: " + productId + "\tqty: " + qty);

        if (!cartViewModel.removeFromCart(productId, qty)) {
            DialogUtil.showNoInternetToast(requireActivity());

            Log.i(TAG, "removeProductFromCart: BEFORE, cartViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.toString());
            cartViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
            Log.i(TAG, "removeProductFromCart: AFTER, cartViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                    + cartViewModel.toBeNotifiedProdIdQtyHashMap.toString());
            cartViewModel.cartRecyclerViewAdapter.notifyProductRemovedFromCart(
                    productId, qty, false, null);
        }
    }


    @Override
    public void productClicked(int productId) {
        Log.i(TAG, "productClicked: fired! productId: " + productId);

        Intent productDetailsIntent = new Intent(requireActivity(), ProductDetailsActivity.class);
        productDetailsIntent.putExtra("PRODUCT_ID", productId);
        startActivity(productDetailsIntent);
    }

    @Override
    public void onCouponClick(Cart_CartData_Coupons cart_cartData_coupons) {
        Log.i(TAG, "onCouponClick: fired! cart_cartData_coupons: " + cart_cartData_coupons);

        //HERE WILL CHECK FOR COUPON APPLICABILITY.
        if (!cartViewModel.checkCouponApplicability(cart_cartData_coupons)) {
            Log.i(TAG, "onCouponClick: Selected Coupon not applicable!");
            Toast.makeText(requireActivity(), "Selected Coupon Not Applicable!", Toast.LENGTH_SHORT).show();
            cartViewModel.cartCouponRecyclerViewAdapter.uncheckCurrentCoupon();
        } else {
            Log.i(TAG, "onCouponClick: Selected Coupon Applied!");
        }
//        displayCartData(cartViewModel.getCartMutableLiveData().getValue().getCart_data());
        displayCartData(cartViewModel.getCart().getCart_data());
    }

    @Override
    public void updateDefaultDeliveryAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress) {
        Log.i(TAG, "updateDefaultDeliveryAddress: fired!\ncart_cartData_buyerAddress: " + cart_cartData_buyerAddress);

        cartViewModel.updateDeliveryAddress(cart_cartData_buyerAddress);
//        displayCartData(cartViewModel.getCartMutableLiveData().getValue().getCart_data());
        displayCartData(cartViewModel.getCart().getCart_data());
    }

    @Override
    public void displayEditedAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress) {
        Log.i(TAG, "displayEditedAddress: fired!\ncart_cartData_buyerAddress: " + cart_cartData_buyerAddress);

        cartViewModel.updateEditedDeliveryAddress(cart_cartData_buyerAddress);

        displayCartData(cartViewModel.getCart().getCart_data());
    }

    @Override
    public void onFreebieClick(int freebieId, int minCartValue, List<Cart_CartData_Freebies_Products> cart_cartData_freebies_productsList) {
        Log.i(TAG, "onFreebieClick: fired! freebieId: " + freebieId + "\tminCartValue: " + minCartValue
                + "\tcart_cartData_freebies_productsList.size(): " + cart_cartData_freebies_productsList.size());

        //HERE WILL CHECK FOR FREEBIE APPLICABILITY.
        if (!cartViewModel.checkFreebieApplicability(minCartValue)) {
            Log.i(TAG, "onFreebieClick: Selected Freebie not applicable!");
            Toast.makeText(requireActivity(), "Selected Freebie Not Applicable!", Toast.LENGTH_SHORT).show();
            cartViewModel.cartFreebieRecyclerViewAdapter.uncheckCurrentFreebie();
            cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(null);
        } else {
            Log.i(TAG, "onFreebieClick: Selected Freebie Applicable!");
            cartViewModel.selectedFreebieId = freebieId;
            cartViewModel.cartFreebieProductsRecyclerViewAdapter.setCart_cartData_freebies_productsList(cart_cartData_freebies_productsList);
        }
//        displayCartData(cartViewModel.getCartMutableLiveData().getValue().getCart_data());
    }

    @Override
    public void onPaymentVerify(String s) {



    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {

    }

   /* @Override
    public void onPaymentVerify(String s) {

        Log.i(TAG,"OnlinePayment Verify Try1:  Selected "+s);
        startActivity(new Intent(getActivity(),StoreLocatorActivity.class));
        if (s != null) {
            if (!s.toUpperCase().matches("FAILED")) {
                //SUCCESS PAYMENT CASE
                Log.i(TAG, "onChanged: Payment Success!");

                //New API for updating order with orderId & payment bit 1.
                if (!cartViewModel.postPaymentNotifyMDGServer(s)) {
                    Toast.makeText(getContext(), "PaymentSuccessful if", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(getContext())
                    .setTitle("PaymentSuccessful")
                    .setMessage("check open dialog box in if")
                   .setPositiveButton("OK",null)
                   .show();
                    // DialogUtil.showNoInternetToast(requireActivity());//@kajal11_30_22 ChangeA
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("PaymentSuccessful")
                            .setMessage("check open dialog box in else")
                            .setPositiveButton("OK",null)
                            .show();
                    Toast.makeText(getContext(), "PaymentSuccessful else", Toast.LENGTH_SHORT).show();
                    // DialogUtil.showProcessingInfoDialog(requireActivity());
                }
            } else {
                //FAILED PAYMENT CASE
                Toast.makeText(requireActivity(), "Payment Unsuccessful!\nOrder Not Placed!\nPlease try again later!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "onChanged: Null Observer fired!");
        }

        Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
        Log.i(TAG,"OnlinePayment Failed Try1:  Selected "+s);
        Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
    }//--------------------------------------------------------------------------------
   *//* public static int toCheckCallBackValue =0;

    public void orderExecute()
    {
        Log.i(TAG,"OnlinePayment sucecess Try1:  Selected ");
        placeOrder();
        toCheckCallBackValue=0;
    }*//*
*/
   /* @Override
    public void PaymentSuccessful(String s) {
        if (s != null) {
            if (!s.toUpperCase().matches("FAILED")) {
                //SUCCESS PAYMENT CASE
                Log.i(TAG, "onChanged: Payment Success!");

                //New API for updating order with orderId & payment bit 1.
                if (!cartViewModel.postPaymentNotifyMDGServer(s)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showProcessingInfoDialog(getContext());
                }
            } else {
                //FAILED PAYMENT CASE
                Toast.makeText(requireActivity(), "Payment Unsuccessful!\nOrder Not Placed!\nPlease try again later!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "onChanged: Null Observer fired!");
        }
    }*/


    //CASHFREE PAYMENT GATEWAY CALLBACK
//    @Override
//    public void onPaymentVerify(String s) {
//        Log.i(TAG, "onPaymentVerify: fired! s (OrderId): " + s);
//
//        /*V/FA: Screen exposed for less than 1000 ms. Event not sent. time: 746
//V/FA: Activity paused, time: 2840251005
//I/CartFragment: onStart: fired!
//I/ParentActivity: onStart: fired!
//I/ParentActivity: onResume: fired!
//V/FA: Activity resumed, time: 2840251212
//I/CartFragment: onResume: fired!*/
//
//        /*Because of the above reason, these payment verify/failure callback functions are not called thus,
//        split order API is never called!*/
//
////        placeOrder();
//
//        //Even after successful payment, Payment Verify callback was not called from cashfree. (27-9-22 -- 7:51pm)
//        postPaymentMutableLiveData.setValue(s);
//
//        //Temporarily commenting out to test lifecycle. (27-9-22)
//        //NEW FLOW (AS OF 27-5-22)
//        //New API for updating order with orderId & payment bit 1.
////        if (!cartViewModel.postPaymentNotifyMDGServer(s)) {
////            DialogUtil.showNoInternetToast(requireActivity());
////        } else {
////            DialogUtil.showProcessingInfoDialog(requireActivity());
////        }
//    }

    //CASHFREE PAYMENT GATEWAY CALLBACK
//    @Override
//    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
//        Log.i(TAG, "onPaymentFailure: fired! cfErrorResponse.toJSON().toString(): " + cfErrorResponse.toJSON().toString() + "\ts: " + s);
//        //I/CartFragment: onPaymentFailure: fired! cfErrorResponse.toJSON().toString(): {"status":"FAILED","message":"action has been cancelled","code":"action_cancelled","type":"request_failed"}	s: mdg_1261655893737
//        //DISPLAY ERROR SCENARIO TO THE CUSTOMER.
//
//        //STORE DETAILS OF CASHFREEORDER IN LOCAL DB.
////        cartViewModel.storeCashFreeOrderInLocalDB(new CartHandler());
//
//        //Moving logic to observer (27-9-22)
//        // So far, MDG Customer app run & payment app run, on same device, Successfully notified the observer. (27-9-22 -- 7:41pm)
//        //NEW FLOW (AS OF 27-5-22)
////        Toast.makeText(requireActivity(), "Payment Unsuccessful!\nOrder Not Placed!\nPlease try again later!", Toast.LENGTH_SHORT).show();
//        postPaymentMutableLiveData.setValue("FAILED");
//    }
}