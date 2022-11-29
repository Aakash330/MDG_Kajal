package com.techive.mydailygoodscustomer.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.ProductDetailsActivity;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.CartInterface;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.LoginUtil;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;
import com.techive.mydailygoodscustomer.ViewModels.HotSellingViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HotSellingFragment extends Fragment implements OnProductCartListener {
    private static final String TAG = "HotSellingFragment";
    private static final String HOT_SELLING_TAG = "HOT_SELLING_TAG";

    private RecyclerView hotSellingRecyclerView;

    private CartInterface cartInterface;

    private HotSellingViewModel hotSellingViewModel;

    public HotSellingFragment() {
        Log.i(TAG, "HotSellingFragment: Empty Constructor fired!");
        // Required empty public constructor
    }

    public static HotSellingFragment newInstance() {

        return new HotSellingFragment();
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

        hotSellingViewModel = new ViewModelProvider(this).get(HotSellingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_selling, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);

        initAdapters();

        initObservers();

        initListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fired!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired!");

        /*CHECK FOR DIFFERENCE BETWEEN OLD ID & DEFAULT STORE ID IN APPLICATION DATA.*/
        Log.i(TAG, "onResume: DefaultStoreId = " + ApplicationData.getDefaultStoreId()
                + "\tApplicationData.getOldStoreIdHotSelling() = " + ApplicationData.getOldStoreIdHotSelling());

        if (ApplicationData.getDefaultStoreId() != ApplicationData.getOldStoreIdHotSelling()) {

            /*WILL ALSO HAVE TO CLEAR THE EXISTING DATA BOTH IN MEMORY & IN VIEW.*/
            clearHotSellingData();

            if (!hotSellingViewModel.getHotSellingProducts(ApplicationData.getDefaultStoreId(), 1)) {
                DialogUtil.showNoInternetToast(requireActivity());
                if (hotSellingViewModel.currentPage == 0) {
                    ((HomeFragment) HotSellingFragment.this.getParentFragment()).toggleReloadIcon(true);
                }
            } else {
                hotSellingViewModel.isHotSellingApiExecuting = true;
                DialogUtil.showLoadingDialog1(requireActivity(), HOT_SELLING_TAG);
            }

            int cartTotalQty = 0;
            for (Integer integer : ApplicationData.getProdIdOrderQtyHashMap().values()) {
                cartTotalQty = cartTotalQty + integer;
            }
            Log.i(TAG, "onResume: cartTotalQty: " + cartTotalQty);
            ApplicationData.setCartTotalQty(cartTotalQty);
            cartInterface.showBadgeOnCart(cartTotalQty);

            ApplicationData.setOldStoreIdHotSelling(ApplicationData.getDefaultStoreId());
            Log.i(TAG, "onResume: After setting DefaultStoreId in the OldStoreIdHotSelling.");
        } else {
            Log.i(TAG, "onResume: Not doing anything since OldStoreIdHotSelling = DefaultStoreId");

            Log.i(TAG, "onResume: hotSellingViewModel.getPooledHotSellingProductsModel_dataList().size(): "
                    + hotSellingViewModel.getPooledHotSellingProductsModel_dataList().size());

            if (hotSellingViewModel.getPooledHotSellingProductsModel_dataList().size() == 0) {
                if (!hotSellingViewModel.getHotSellingProducts(ApplicationData.getDefaultStoreId(), 1)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                    if (hotSellingViewModel.currentPage == 0) {
                        ((HomeFragment) HotSellingFragment.this.getParentFragment()).toggleReloadIcon(true);
                    }
                } else {
                    hotSellingViewModel.isHotSellingApiExecuting = true;
                    DialogUtil.showLoadingDialog1(requireActivity(), HOT_SELLING_TAG);
                }
            } else {
                Log.i(TAG, "onResume: Literally not doing anything!");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");

        clearMutableLiveData();
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

    private void initComponents(View view) {
        Log.i(TAG, "initComponents: fired!");

        hotSellingRecyclerView = view.findViewById(R.id.hotSellingRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        hotSellingRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        hotSellingRecyclerView.setAdapter(hotSellingViewModel.hotSellingRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        hotSellingViewModel.getHotSellingProductsModelMutableLiveData().observe(this, hotSellingProductsModelObserver);
        hotSellingViewModel.getAddToCartMutableLiveData().observe(this, addToCartObserver);
        hotSellingViewModel.getRemoveFromCartMutableLiveData().observe(this, removeFromCartObserver);
        hotSellingViewModel.getCompletelyRemoveFromCartMutableLiveData().observe(this, completelyRemoveFromCartObserver);
        hotSellingViewModel.getProdAddAcceptResponseMutableLiveData().observe(this, prodAddAcceptResponseObserver);
    }

    /*IN VENDOR ORDER OBSERVER CHECK FOR SOCKET TIMEOUT & NO INTERNET, PRODS DISABLED.
     * MAKE THE VARIABLES FOR PRODID IN HOT SELLING VIEW MODEL.*/

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        hotSellingViewModel.hotSellingRecyclerViewAdapter.setOnProductCartListener(this);
    }

    private final Observer<ProductsModel> hotSellingProductsModelObserver = new Observer<ProductsModel>() {
        @Override
        public void onChanged(ProductsModel productsModel) {
            Log.i(TAG, "onChanged: HOT SELLING PRODUCTS Observer fired!\nproductsModel: " + productsModel);

            if (productsModel != null) {
                DialogUtil.dismissLoadingDialog1(HOT_SELLING_TAG);
                hotSellingViewModel.isHotSellingApiExecuting = false;

                if (productsModel.getError() == 200) {
                    if (hotSellingViewModel.currentPage == 0) {
                        ((HomeFragment) HotSellingFragment.this.getParentFragment()).toggleReloadIcon(false);
                    }

                    hotSellingViewModel.currentPage = productsModel.getCurrent_page();
                    hotSellingViewModel.lastPage = productsModel.getLast_page();

                    Log.i(TAG, "onChanged: hotSellingViewModel.currentPage: " + hotSellingViewModel.currentPage
                            + "\thotSellingViewModel.lastPage: " + hotSellingViewModel.lastPage);

                    hotSellingViewModel.setPooledHotSellingProductsModel_dataList(productsModel.getData());

                    hotSellingViewModel.hotSellingRecyclerViewAdapter.setProductsModel_dataList(
                            hotSellingViewModel.getPooledHotSellingProductsModel_dataList());

                    hotSellingViewModel.hotSellingRecyclerViewAdapter.setProdIdOrderQtyHashMap(
                            ApplicationData.getProdIdOrderQtyHashMap());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while loading Hot Selling products.");
                    Toast.makeText(requireActivity(), productsModel.getMsg(), Toast.LENGTH_SHORT).show();

                    if (hotSellingViewModel.currentPage == 0) {
                        ((HomeFragment) HotSellingFragment.this.getParentFragment()).toggleReloadIcon(true);
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> addToCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: ADD TO CART (Hot Selling) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //THIS NULL CHECK IS TO HANDLE THE SCENARIO OF PRODUCT NOT AVAILABLE ON CURRENT PAGE.
                    if (hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {

//                        hotSellingViewModel.toBeAddedProductId = 0;

                        //NOTIFY BEST DEALS ADAPTER FOR QTY CHANGE
                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), true);

//                    hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
//                            ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())), true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(true);

                        hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Prod add not done from HotSelling fragment!");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while adding product to cart!");

                    if (hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                        hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Prod add not done from HotSelling fragment!");
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> removeFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: REMOVE FROM CART (Hot Selling) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //THIS NULL CHECK IS TO HANDLE THE SCENARIO OF PRODUCT NOT AVAILABLE ON CURRENT PAGE.
                    if (hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {

                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from HotSelling.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while removing product from cart!");

                    if (hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from HotSelling.");
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> completelyRemoveFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: COMPLETELY REMOVE FROM CART (Hot Selling) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //THIS NULL CHECK IS TO HANDLE THE SCENARIO OF PRODUCT NOT AVAILABLE ON CURRENT PAGE.
                    if (hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductCompletelyRemoved(Integer.parseInt(generalResponse.getProd_id()));

                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), true);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));

                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from HotSelling.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while completely removing product from cart!");

                    if (hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from HotSelling.");
                    }
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
                        DialogUtil.showCustomSnackbar(requireActivity(), hotSellingRecyclerView, orderAcceptResponse.getMsg(), -1);

                        hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(hotSellingViewModel.toBeAddedProductId, hotSellingViewModel.toBeAddedProductQty, false);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Prod Add Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(hotSellingViewModel.toBeAddedProductId, hotSellingViewModel.toBeAddedProductQty, false);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };


    public void loadMoreHotSellingProducts() {
        Log.i(TAG, "loadMoreHotSellingProducts: fired!");

        if (!hotSellingViewModel.isHotSellingApiExecuting) {
            Log.i(TAG, "loadMoreHotSellingProducts: hotSellingViewModel.currentPage: " + hotSellingViewModel.currentPage);

            int nextPage = hotSellingViewModel.currentPage;

            Log.i(TAG, "loadMoreHotSellingProducts: hotSellingViewModel.currentPage: " + hotSellingViewModel.currentPage
                    + "\tnextPage: " + ++nextPage + "\thotSellingViewModel.lastPage: " + hotSellingViewModel.lastPage);

            if (nextPage <= hotSellingViewModel.lastPage) {
                Log.i(TAG, "loadMoreHotSellingProducts: Loading More Best Deal Products!");
                if (!hotSellingViewModel.getHotSellingProducts(ApplicationData.getDefaultStoreId(), nextPage)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showLoadingDialog1(requireActivity(), HOT_SELLING_TAG);
                }
            } else {
                Log.i(TAG, "loadMoreHotSellingProducts: Reached END of PAGE!");
                Toast.makeText(requireActivity(), "No More Products to Show!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "loadMoreHotSellingProducts: More Hot Selling products already being loaded!");
        }
    }

    /*NEEDED FOR 1ST TIME LOADING.*/
    public void reloadHotSelling() {
        Log.i(TAG, "reloadHotSelling: fired!");

        if (!hotSellingViewModel.getHotSellingProducts(ApplicationData.getDefaultStoreId(), 1)) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            DialogUtil.showLoadingDialog1(requireActivity(), HOT_SELLING_TAG);
        }
    }

    public void showCartDataInHotSelling(HashMap<Integer, Integer> prodIdOrderQtyHashMap) {
        Log.i(TAG, "showCartDataInHotSelling: fired!");

        if (hotSellingViewModel != null) {
            if (prodIdOrderQtyHashMap.size() != 0) {
                hotSellingViewModel.hotSellingRecyclerViewAdapter.setProdIdOrderQtyHashMap(prodIdOrderQtyHashMap);
            } else {
                Log.i(TAG, "showCartDataInHotSelling: prodIdQtyHashMap Size was 0.");
            }
        } else {
            Log.i(TAG, "showCartDataInHotSelling: hotSellingViewModel currently NULL!");
        }
    }

    //TO UPDATE THE SELECTED PRODUCTS IN CART
    private void updateCartData(int prodId, boolean completelyRemove) {
        Log.i(TAG, "updateCartData: fired!");

        Log.i(TAG, "updateCartData: HOT SELLING, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        if (!completelyRemove) {
            prodIdQtyHashMap.put(prodId, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.get(prodId));
        } else {
            prodIdQtyHashMap.remove(prodId);
        }
        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
        Log.i(TAG, "updateCartData: HOT SELLING, AFTER, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
    }

    //TO UPDATE THE CART COUNT
    private void updateCartCount(boolean increment) {
        Log.i(TAG, "updateCartCount: fired!");

        Log.i(TAG, "updateCartCount: BEFORE, ApplicationData.getCartTotalQty(): " + ApplicationData.getCartTotalQty());
        if (increment) {
            ApplicationData.setCartTotalQty(ApplicationData.getCartTotalQty() + 1);
        } else {
            ApplicationData.setCartTotalQty(ApplicationData.getCartTotalQty() - 1);
        }
        Log.i(TAG, "updateCartCount: AFTER, ApplicationData.getCartTotalQty(): " + ApplicationData.getCartTotalQty());
        cartInterface.showBadgeOnCart(ApplicationData.getCartTotalQty());
    }

    /*WILL BE USED TO CLEAR PREVIOUS DATA WHEN SWITCHING STORES.*/
    private void clearHotSellingData() {
        Log.i(TAG, "clearHotSellingData: fired!");

        hotSellingViewModel.hotSellingRecyclerViewAdapter.setProdIdOrderQtyHashMap(null);
        hotSellingViewModel.hotSellingRecyclerViewAdapter.setProductsModel_dataList(null);

        hotSellingViewModel.setPooledHotSellingProductsModel_dataList(null);
        hotSellingViewModel.currentPage = 0;
        hotSellingViewModel.lastPage = 0;

        cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
    }

    public void clearMutableLiveData() {
        Log.i(TAG, "clearMutableLiveData: HOT SELLING!");

        if (hotSellingViewModel.getAddToCartMutableLiveData().getValue() != null) {
            hotSellingViewModel.setAddToCartMutableLiveData(null);
        }
        if (hotSellingViewModel.getRemoveFromCartMutableLiveData().getValue() != null) {
            hotSellingViewModel.setRemoveFromCartMutableLiveData(null);
        }
        if (hotSellingViewModel.getCompletelyRemoveFromCartMutableLiveData().getValue() != null) {
            hotSellingViewModel.setCompletelyRemoveFromCartMutableLiveData(null);
        }
        hotSellingViewModel.setHotSellingProductsModelMutableLiveData(null);

        if (hotSellingViewModel.getProdAddAcceptResponseMutableLiveData().getValue() != null) {
            hotSellingViewModel.setProdAddAcceptResponseMutableLiveData(null);
        }
    }

    @Override
    public void addProductToCart(int productId, int qty) {
        Log.i(TAG, "addProductToCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {

            //PROCEED TO CHECK FOR PROD ADD ACCEPTANCE
            if (!hotSellingViewModel.checkVendorOrderAcceptance(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 1) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, --qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            } else {
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }

            //PROCEED TO ADD PRODUCT TO CART
//            if (!hotSellingViewModel.addToCartFromHotSelling(productId, qty)) {
//                DialogUtil.showNoInternetToast(requireActivity());
//
//                Log.i(TAG, "addProductToCart: BEFORE, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
//                Log.i(TAG, "addProductToCart: AFTER, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//
//                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//                if (qty == 1) {
//                    prodIdQtyHashMap.remove(productId);
//                } else {
//                    prodIdQtyHashMap.put(productId, --qty);
//                }
//                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//
//                hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
//            }
        } else {
            hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    @Override
    public void removeProductFromCart(int productId, int qty) {
        Log.i(TAG, "removeProductFromCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            //PROCEED TO REMOVE PRODUCT FROM CART
            if (!hotSellingViewModel.removeFromCartFromHotSelling(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + hotSellingViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 0) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, ++qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
            }
        } else {
            hotSellingViewModel.hotSellingRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    @Override
    public void productClicked(int productId) {
        Log.i(TAG, "productClicked: fired! productId: " + productId);

        Intent productDetailsIntent = new Intent(requireActivity(), ProductDetailsActivity.class);
        productDetailsIntent.putExtra("PRODUCT_ID", productId);
        startActivity(productDetailsIntent);
    }
}