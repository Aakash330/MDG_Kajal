package com.techive.mydailygoodscustomer.UI.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.LoginActivity;
import com.techive.mydailygoodscustomer.UI.Activities.ProductDetailsActivity;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.CartInterface;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.LoginUtil;
import com.techive.mydailygoodscustomer.Util.OnProductCartListener;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;
import com.techive.mydailygoodscustomer.ViewModels.RegularListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegularListFragment extends Fragment implements OnProductCartListener {
    private static final String TAG = "RegularListFragment";
    private static final String REGULAR_LIST = "REGULAR_LIST", REGULAR_CART = "REGULAR_CART";

    //BASICALLY NEEDED FOR 1ST TIME DATA FETCH.
    private SwipeRefreshLayout regularListSwipeRefreshLayout;

    private NestedScrollView regularListNestedScrollView;

    private MaterialTextView regularList_storeNameMaterialTextView;

    private RecyclerView regularListRecyclerView;

    private SharedPreferencesManager sharedPreferencesManager;
    private SharedPreferences sharedPreferences;

    private CartInterface cartInterface;

    private RegularListViewModel regularListViewModel;

    // Required empty public constructor
    public RegularListFragment() {
        Log.i(TAG, "RegularListFragment: Empty Constructor fired!");
    }

    public static RegularListFragment newInstance() {
        return new RegularListFragment();
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

//        regularListViewModel = new ViewModelProvider(requireActivity()).get(RegularListViewModel.class);
        regularListViewModel = new ViewModelProvider(this).get(RegularListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired!");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regular_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

        initAdapters();

        initObservers();

        initListeners();

        sharedPreferencesManager = new SharedPreferencesManager(requireActivity());
        sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();

//        if (sharedPreferences.getInt(SharedPreferencesManager.userId, 0) != 0) {
//            Log.i(TAG, "onViewCreated: Buyer already logged in! Retrieving Regular List for buyer: "
//                    + sharedPreferences.getInt(SharedPreferencesManager.userId, 0));
//
//            if (!regularListViewModel.getRegularCart()) {
//                DialogUtil.showNoInternetToast(requireActivity());
//            } else {
//                DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_CART);
//            }
//
//            if (!regularListViewModel.getBuyerRegularList(sharedPreferences.getInt(SharedPreferencesManager.userId, 0), 1)) {
//                DialogUtil.showNoInternetToast(requireActivity());
//            } else {
//                DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_LIST);
//            }
//        } else {
//            Log.i(TAG, "onViewCreated: Buyer NOT logged in! Prompting to login...");
//
//            redirectToLogin();
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
        Log.i(TAG, "onResume: RegularList fired!");

//        regularListViewModel.setCartRepository();

        regularList_storeNameMaterialTextView.setText(requireActivity().getString(R.string.store_name) + ": " + ApplicationData.getDefaultStoreName());

        /*CHECK FOR DIFFERENCE BETWEEN OLD ID & DEFAULT STORE ID IN APPLICATION DATA.*/
        Log.i(TAG, "onResume: DefaultStoreId = " + ApplicationData.getDefaultStoreId()
                + "\tApplicationData.getOldStoreIdRegularList() = " + ApplicationData.getOldStoreIdRegularList());
        if (ApplicationData.getDefaultStoreId() != ApplicationData.getOldStoreIdRegularList()) {

            /*WILL ALSO HAVE TO CLEAR THE EXISTING DATA BOTH IN MEMORY & IN VIEW.*/
            clearRegularListData();

            if (ApplicationData.getLoggedInBuyerId() != 0) {
                Log.i(TAG, "onResume: Buyer already logged in! Retrieving Regular List for buyer: "
                        + ApplicationData.getLoggedInBuyerId());

//                if (!regularListViewModel.getRegularCart()) {
//                    DialogUtil.showNoInternetToast(requireActivity());
//                } else {
//                    DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_CART);
//                }

                if (!regularListViewModel.getBuyerRegularList(ApplicationData.getLoggedInBuyerId(), 1)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_LIST);
                }

                ApplicationData.setOldStoreIdRegularList(ApplicationData.getDefaultStoreId());
                Log.i(TAG, "onResume: After setting DefaultStoreId in the OldStoreIdRegularList.");
            } else {
                Log.i(TAG, "onResume: Buyer NOT logged in! Prompting to login...");

                redirectToLogin();
            }
        } else {
            Log.i(TAG, "onResume: OldStoreIdRegularList = DefaultStoreId");

            if (regularListViewModel.getPooledRegularListProductsModel_dataList().size() == 0) {

                if (ApplicationData.getLoggedInBuyerId() != 0) {
                    Log.i(TAG, "onResume: Buyer already logged in! Retrieving Regular List for buyer: "
                            + ApplicationData.getLoggedInBuyerId());

//                    if (!regularListViewModel.getRegularCart()) {
//                        DialogUtil.showNoInternetToast(requireActivity());
//                    } else {
//                        DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_CART);
//                    }

                    if (!regularListViewModel.getBuyerRegularList(ApplicationData.getLoggedInBuyerId(), 1)) {
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_LIST);
                    }

                    ApplicationData.setOldStoreIdRegularList(ApplicationData.getDefaultStoreId());
                    Log.i(TAG, "onResume: After setting DefaultStoreId in the OldStoreIdRegularList.");
                } else {
                    Log.i(TAG, "onResume: Buyer NOT logged in! Prompting to login...");

                    redirectToLogin();
                }
            } else {
                Log.i(TAG, "onResume: Literally not doing anything, since PooledRegularListData has values!");
            }
        }

        //NO MATTER IF THE STORE CHANGED OR NOT, CART WILL BE FETCHED, EVERYTIME HOME PAGE IS RESUMED.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            Log.i(TAG, "onResume: Buyer already logged in! Retrieving Cart for buyer on RegularList page: "
                    + ApplicationData.getLoggedInBuyerId());

            if (!regularListViewModel.getRegularCart()) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_CART);
            }
        } else {
            Log.i(TAG, "onResume: Buyer NOT logged in! Not fetching cart!");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");

        if (regularListViewModel.getAddToCartMutableLiveData().getValue() != null) {
            regularListViewModel.setAddToCartMutableLiveData(null);
        }
        if (regularListViewModel.getRemoveFromCartMutableLiveData().getValue() != null) {
            regularListViewModel.setRemoveFromCartMutableLiveData(null);
        }
        if (regularListViewModel.getCompletelyRemoveFromCartMutableLiveData().getValue() != null) {
            regularListViewModel.setCompletelyRemoveFromCartMutableLiveData(null);
        }
        regularListViewModel.setRegularListMutableLiveData(null);

        if (regularListViewModel.getProdAddAcceptResponseMutableLiveData().getValue() != null) {
            regularListViewModel.setProdAddAcceptResponseMutableLiveData(null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");

        DialogUtil.dismissCustomSnackBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: fired!");

//        regularListViewModel.setPooledRegularListProductsModel_dataList(null);
        regularListViewModel.setRegularCartMutableLiveData(null);
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

        regularListSwipeRefreshLayout = view.findViewById(R.id.regularListSwipeRefreshLayout);

        regularList_storeNameMaterialTextView = view.findViewById(R.id.regularList_storeNameMaterialTextView);

        regularListNestedScrollView = view.findViewById(R.id.regularListNestedScrollView);

        regularListRecyclerView = view.findViewById(R.id.regularListRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        regularListRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        regularListRecyclerView.setAdapter(regularListViewModel.regularListRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        regularListViewModel.getRegularListMutableLiveData().observe(this, regularListObserver);
        regularListViewModel.getRegularCartMutableLiveData().observe(this, regularCartObserver);
        regularListViewModel.getAddToCartMutableLiveData().observe(this, addToCartObserver);
        regularListViewModel.getRemoveFromCartMutableLiveData().observe(this, removeFromCartObserver);
        regularListViewModel.getCompletelyRemoveFromCartMutableLiveData().observe(this, completelyRemoveFromCartObserver);
        regularListViewModel.getProdAddAcceptResponseMutableLiveData().observe(this, prodAddAcceptResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        regularListSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        regularListNestedScrollView.setOnScrollChangeListener(onScrollChangeListener);

        regularListViewModel.regularListRecyclerViewAdapter.setOnProductCartListener(this);
    }

    private final Observer<ProductsModel> regularListObserver = new Observer<ProductsModel>() {
        @Override
        public void onChanged(ProductsModel productsModel) {
            Log.i(TAG, "onChanged: REGULAR LIST Observer fired!\nproductsModel: " + productsModel);

            if (productsModel != null) {
                DialogUtil.dismissLoadingDialog1(REGULAR_LIST);
                DialogUtil.dismissCustomSnackBar();

                if (productsModel.getError() == 200) {
                    regularListViewModel.currentPage = productsModel.getCurrent_page();
                    regularListViewModel.lastPage = productsModel.getLast_page();
                    Log.i(TAG, "onChanged: regularListViewModel.currentPage: " + regularListViewModel.currentPage
                            + "\tregularListViewModel.lastPage: " + regularListViewModel.lastPage);

                    regularListViewModel.regularListApiExecuteSuccess = true;
//                regularListSwipeRefreshLayout.setEnabled(false);

                    regularListViewModel.setPooledRegularListProductsModel_dataList(productsModel.getData());

                    regularListViewModel.regularListRecyclerViewAdapter.setProductsModel_dataList(
                            regularListViewModel.getPooledRegularListProductsModel_dataList());

                    if (regularListViewModel.getPooledRegularListProductsModel_dataList().size() == 0) {
                        DialogUtil.showCustomSnackbar(requireActivity(), regularListSwipeRefreshLayout, "You haven't made any purchase from this store yet!", -2);
                        regularListViewModel.regularListApiExecuteSuccess = false;
//                    regularListSwipeRefreshLayout.setEnabled(true);
                    }
                } else if (productsModel.getError() == 404) {
                    regularListViewModel.setPooledRegularListProductsModel_dataList(null);
                    regularListViewModel.regularListRecyclerViewAdapter.setProductsModel_dataList(regularListViewModel.getPooledRegularListProductsModel_dataList());
                    DialogUtil.showCustomSnackbar(requireActivity(), regularListSwipeRefreshLayout, "You haven't made any purchase from this store yet!", -2);
                    regularListViewModel.regularListApiExecuteSuccess = false;
//                regularListSwipeRefreshLayout.setEnabled(true);
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while loading RegularListProducts.");
                    Toast.makeText(requireActivity(), productsModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<HomeCart> regularCartObserver = new Observer<HomeCart>() {
        @Override
        public void onChanged(HomeCart homeCart) {
            Log.i(TAG, "onChanged: REGULAR CART Observer fired!\nhomeCart: " + homeCart);

            if (homeCart != null) {
                DialogUtil.dismissLoadingDialog1(REGULAR_CART);

                if (homeCart.getError() == 200) {
                    int totalQty = 0;

                    for (Cart_CartData_Data cart_cartData_data : homeCart.getData()) {
                        totalQty = totalQty + cart_cartData_data.getQty();
                    }
                    ApplicationData.setCartTotalQty(totalQty);
                    cartInterface.showBadgeOnCart(totalQty);

                    regularListViewModel.regularListRecyclerViewAdapter.setProdIdOrderQtyHashMap(
                            regularListViewModel.computeProdIdOrderQtyHashMap());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while loading Regular cart!");
                    if (homeCart.getError() == 400) {   //CART IS EMPTY!
                        ApplicationData.setCartTotalQty(0);
                        cartInterface.showBadgeOnCart(0);
                        return;
                    }
                    Toast.makeText(requireActivity(), homeCart.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> addToCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: ADD TO CART (Regular List) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //NOTIFY BEST DEALS ADAPTER FOR QTY CHANGE
                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                            regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), true);

                    //UPDATE PROD ID & QTY IN APPLICATION DATA.
                    updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                    //UPDATE CART QTY IN BADGE
                    updateCartCount(true);

                } else {
                    Log.i(TAG, "onChanged: Something went wrong while adding product to cart!");
                    Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                            regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> removeFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: REMOVE FROM CART (Regular List) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductRemoved(
                            Integer.parseInt(generalResponse.getProd_id()),
                            regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                            true);

                    //UPDATE PROD ID & QTY IN APPLICATION DATA.
                    updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                    //UPDATE CART QTY IN BADGE
                    updateCartCount(false);

                } else {
                    Log.i(TAG, "onChanged: Something went wrong while removing product from cart from Cart!");
                    Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductRemoved(
                            Integer.parseInt(generalResponse.getProd_id()),
                            regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                            false);
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> completelyRemoveFromCartObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: COMPLETELY REMOVE FROM CART (Regular List) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));
                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductCompletelyRemoved(Integer.parseInt(generalResponse.getProd_id()));

                    //UPDATE PROD ID & QTY IN APPLICATION DATA.
                    updateCartData(Integer.parseInt(generalResponse.getProd_id()), true);

                    //UPDATE CART QTY IN BADGE
                    updateCartCount(false);

                } else {
                    Log.i(TAG, "onChanged: Something went wrong while completely removing product from cart from Cart!");
                    Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductRemoved(
                            Integer.parseInt(generalResponse.getProd_id()),
                            regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                            false);
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
                        DialogUtil.showCustomSnackbar(requireActivity(), regularListRecyclerView, orderAcceptResponse.getMsg(), -1);

                        regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(regularListViewModel.toBeAddedProductId, regularListViewModel.toBeAddedProductQty, false);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Prod Add Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(regularListViewModel.toBeAddedProductId, regularListViewModel.toBeAddedProductQty, false);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };



    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            if (!regularListViewModel.getRegularCart()) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_CART);
            }

            //BASICALLY THIS REFRESH LISTENER IS USED FOR THE 1ST TIME DATA LOADING.
            if (!regularListViewModel.regularListApiExecuteSuccess) {
                if (sharedPreferences.getInt(SharedPreferencesManager.userId, 0) != 0) {
                    if (!regularListViewModel.getBuyerRegularList(sharedPreferences.getInt(SharedPreferencesManager.userId, 0), 1)) {
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_LIST);
                    }
                } else {
                    redirectToLogin();
                }
            } else {
                Log.i(TAG, "onRefresh: Regular List API Already executed successfully!");
            }

            regularListSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private final NestedScrollView.OnScrollChangeListener onScrollChangeListener = new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                Log.i(TAG, "onScrollChange: Reached Page bottom!");

                int nextPage = regularListViewModel.currentPage;

                Log.i(TAG, "onScrollChange: regularListViewModel.currentPage: " + regularListViewModel.currentPage
                        + "\tnextPage: " + ++nextPage + "\tregularListViewModel.lastPage: " + regularListViewModel.lastPage);

                if (nextPage <= regularListViewModel.lastPage) {
                    Log.i(TAG, "onScrollChange: Loading more Regular List products!");
                    if (!regularListViewModel.getBuyerRegularList(sharedPreferences.getInt(SharedPreferencesManager.userId, 0),
                            nextPage)) {
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        DialogUtil.showLoadingDialog1(requireActivity(), REGULAR_LIST);
                    }
                } else {
                    Log.i(TAG, "onScrollChange: Reached end of page!");
                    Toast.makeText(requireActivity(), "No More Products to Show!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void redirectToLogin() {
        Log.i(TAG, "redirectToLogin: fired!");

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setTitle("Proceed to Login/Register!")
                .setMessage("Please proceed to Login/Register to view your Regular List.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "onClick: Canceled!");

                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "onClick: Yes!");

                        //PROCEED TO UPLOAD
                        dialogInterface.dismiss();

                        Intent loginIntent = new Intent(requireActivity(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                })
                .create();
        alertDialog.show();
    }

    //TO UPDATE THE SELECTED PRODUCTS IN CART
    private void updateCartData(int prodId, boolean completelyRemove) {
        Log.i(TAG, "updateCartData: fired!");

        Log.i(TAG, "updateCartData: BESTDEALS, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        if (!completelyRemove) {
            prodIdQtyHashMap.put(prodId, regularListViewModel.toBeNotifiedProdIdQtyHashMap.get(prodId));
        } else {
            prodIdQtyHashMap.remove(prodId);
        }
        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
        Log.i(TAG, "updateCartData: BESTDEALS, AFTER, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
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
    private void clearRegularListData() {
        Log.i(TAG, "clearRegularListData: fired!");

        regularListViewModel.regularListRecyclerViewAdapter.setProdIdOrderQtyHashMap(null);
        regularListViewModel.regularListRecyclerViewAdapter.setProductsModel_dataList(null);

        regularListViewModel.setPooledRegularListProductsModel_dataList(null);
        regularListViewModel.currentPage = 0;
        regularListViewModel.lastPage = 0;

        cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
    }

    @Override
    public void addProductToCart(int productId, int qty) {
        Log.i(TAG, "addProductToCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {

            //PROCEED TO CHECK FOR PROD ADD ACCEPTANCE
            if (!regularListViewModel.checkVendorOrderAcceptance(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                regularListViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 1) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, --qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            } else {
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }

//            if (!regularListViewModel.addToCartFromRegularList(productId, qty)) {
//                DialogUtil.showNoInternetToast(requireActivity());
//
//                Log.i(TAG, "addProductToCart: BEFORE, regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//                regularListViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
//                Log.i(TAG, "addProductToCart: AFTER, regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//
//                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//                if (qty == 1) {
//                    prodIdQtyHashMap.remove(productId);
//                } else {
//                    prodIdQtyHashMap.put(productId, --qty);
//                }
//                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//
//                regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
//            }
        } else {
            regularListViewModel.regularListRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    @Override
    public void removeProductFromCart(int productId, int qty) {
        Log.i(TAG, "removeProductFromCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            //PROCEED TO REMOVE PRODUCT FROM CART
            if (!regularListViewModel.removeFromCartFromRegularList(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                regularListViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + regularListViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 0) {
                    prodIdQtyHashMap.remove(productId);
                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
                } else {
                    prodIdQtyHashMap.put(productId, ++qty);
                    regularListViewModel.regularListRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
            }
        } else {
            regularListViewModel.regularListRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
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