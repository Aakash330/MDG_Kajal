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
import com.techive.mydailygoodscustomer.ViewModels.RecentlyAddedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RecentlyAddedFragment extends Fragment implements OnProductCartListener {
    private static final String TAG = "RecentlyAddedFragment";
    private static final String RECENTLY_ADDED_TAG = "RECENTLY_ADDED_TAG";

    private RecyclerView recentlyAddedRecyclerView;

    private CartInterface cartInterface;

    private RecentlyAddedViewModel recentlyAddedViewModel;

    // Required empty public constructor
    public RecentlyAddedFragment() {
        Log.i(TAG, "RecentlyAddedFragment: Empty Constructor fired!");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment RecentlyAddedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentlyAddedFragment newInstance(/*String param1, String param2*/) {
        Log.i(TAG, "newInstance: fired!");
        //        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return new RecentlyAddedFragment();
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

        recentlyAddedViewModel = new ViewModelProvider(this).get(RecentlyAddedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired!");
        return inflater.inflate(R.layout.fragment_recently_added, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

        initAdapters();

        initObservers();

        initListeners();

//        if (!recentlyAddedViewModel.getRecentlyAddedProducts(ApplicationData.getDefaultStoreId(), 1)) {
//            DialogUtil.showNoInternetToast(requireActivity());
//        } else {
//            recentlyAddedViewModel.isRecentlyAddedApiExecuting = true;
//            DialogUtil.showLoadingDialog1(requireActivity(), RECENTLY_ADDED_TAG);
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
        Log.i(TAG, "onResume: RecentlyAdded fired!");

        /*CHECK FOR DIFFERENCE BETWEEN OLD ID & DEFAULT STORE ID IN APPLICATION DATA.*/
        Log.i(TAG, "onResume: DefaultStoreId = " + ApplicationData.getDefaultStoreId()
                + "\tApplicationData.getOldStoreIdRecentlyAdded() = " + ApplicationData.getOldStoreIdRecentlyAdded());
        if (ApplicationData.getDefaultStoreId() != ApplicationData.getOldStoreIdRecentlyAdded()) {

            /*WILL ALSO HAVE TO CLEAR THE EXISTING DATA BOTH IN MEMORY & IN VIEW.*/
            clearRecentlyAddedData();

            if (!recentlyAddedViewModel.getRecentlyAddedProducts(ApplicationData.getDefaultStoreId(), 1)) {
                DialogUtil.showNoInternetToast(requireActivity());
                if (recentlyAddedViewModel.currentPage == 0) {
                    ((HomeFragment) RecentlyAddedFragment.this.getParentFragment()).toggleReloadIcon(true);
                }
            } else {
                recentlyAddedViewModel.isRecentlyAddedApiExecuting = true;
                DialogUtil.showLoadingDialog1(requireActivity(), RECENTLY_ADDED_TAG);
            }

            int cartTotalQty = 0;
            for (Integer integer : ApplicationData.getProdIdOrderQtyHashMap().values()) {
                cartTotalQty = cartTotalQty + integer;
            }
            Log.i(TAG, "onResume: cartTotalQty: " + cartTotalQty);
            ApplicationData.setCartTotalQty(cartTotalQty);
            cartInterface.showBadgeOnCart(cartTotalQty);

            ApplicationData.setOldStoreIdRecentlyAdded(ApplicationData.getDefaultStoreId());
            Log.i(TAG, "onResume: After setting DefaultStoreId in the OldStoreIdRecentlyAdded.");
        } else {
            Log.i(TAG, "onResume: Not doing anything since OldStoreIdRecentlyAdded = DefaultStoreId");

            Log.i(TAG, "onResume: recentlyAddedViewModel.getPooledRecentlyAddedProductsModel_dataList().size(): "
                    + recentlyAddedViewModel.getPooledRecentlyAddedProductsModel_dataList().size());

            if (recentlyAddedViewModel.getPooledRecentlyAddedProductsModel_dataList().size() == 0) {
                if (!recentlyAddedViewModel.getRecentlyAddedProducts(ApplicationData.getDefaultStoreId(), 1)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                    if (recentlyAddedViewModel.currentPage == 0) {
                        ((HomeFragment) RecentlyAddedFragment.this.getParentFragment()).toggleReloadIcon(true);
                    }
                } else {
                    recentlyAddedViewModel.isRecentlyAddedApiExecuting = true;
                    DialogUtil.showLoadingDialog1(requireActivity(), RECENTLY_ADDED_TAG);
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

    private void initComponentViews(View view) {
        Log.i(TAG, "initComponentViews: fired!");

        recentlyAddedRecyclerView = view.findViewById(R.id.recentlyAddedRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        recentlyAddedRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        recentlyAddedRecyclerView.setAdapter(recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        recentlyAddedViewModel.getRecentlyAddedProductsModelMutableLiveData().observe(this, recentlyAddedProductsModelObserver);
        recentlyAddedViewModel.getAddToCartMutableLiveData().observe(this, addToCartObserver);
        recentlyAddedViewModel.getRemoveFromCartMutableLiveData().observe(this, removeFromCartObserver);
        recentlyAddedViewModel.getCompletelyRemoveFromCartMutableLiveData().observe(this, completelyRemoveFromCartObserver);

        recentlyAddedViewModel.getOrderAcceptResponseMutableLiveData().observe(this, orderAcceptResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.setOnProductCartListener(this);
    }

    private final Observer<ProductsModel> recentlyAddedProductsModelObserver = new Observer<ProductsModel>() {
        @Override
        public void onChanged(ProductsModel productsModel) {
            Log.i(TAG, "onChanged: RECENTLY ADDED OBSERVER fired!\nproductsModel: " + productsModel);

            if (productsModel != null) {
                DialogUtil.dismissLoadingDialog1(RECENTLY_ADDED_TAG);
                recentlyAddedViewModel.isRecentlyAddedApiExecuting = false;

                if (productsModel.getError() == 200) {
                    if (recentlyAddedViewModel.currentPage == 0) {
                        ((HomeFragment) RecentlyAddedFragment.this.getParentFragment()).toggleReloadIcon(false);
                    }

                    recentlyAddedViewModel.currentPage = productsModel.getCurrent_page();
                    recentlyAddedViewModel.lastPage = productsModel.getLast_page();

                    Log.i(TAG, "onChanged: recentlyAddedViewModel.currentPage: " + recentlyAddedViewModel.currentPage
                            + "\trecentlyAddedViewModel.lastPage: " + recentlyAddedViewModel.lastPage);

                    recentlyAddedViewModel.setPooledRecentlyAddedProductsModel_dataList(productsModel.getData());

                    recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.setProductsModel_dataList(
                            recentlyAddedViewModel.getPooledRecentlyAddedProductsModel_dataList());

                    recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.setProdIdOrderQtyHashMap(
                            ApplicationData.getProdIdOrderQtyHashMap());
                } else {
                    Log.i(TAG, "onChanged: Some problem occurred while loading Recently Added Products.");
                    Toast.makeText(requireActivity(), productsModel.getMsg(), Toast.LENGTH_SHORT).show();

                    if (recentlyAddedViewModel.currentPage == 0) {
                        ((HomeFragment) RecentlyAddedFragment.this.getParentFragment()).toggleReloadIcon(true);
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
            Log.i(TAG, "onChanged: ADD TO CART (Recently Added) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //THIS NULL CHECK IS TO HANDLE THE SCENARIO OF PRODUCT NOT AVAILABLE ON CURRENT PAGE.
                    if (recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {

                        recentlyAddedViewModel.toBeAddedProductId = 0;
                        //NOTIFY BEST DEALS ADAPTER FOR QTY CHANGE
                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), true);

//                    recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
//                            ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())), true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(true);

                        recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Prod add not done from Recently added fragment!");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while adding product to cart!");

                    if (recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                        recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Prod add not done from Recently Added fragment!");
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
            Log.i(TAG, "onChanged: REMOVE FROM CART (Recently Added) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //THIS NULL CHECK IS TO HANDLE THE SCENARIO OF PRODUCT NOT AVAILABLE ON CURRENT PAGE.
                    if (recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {

                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from recently added.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while removing product from cart from Cart!");

                    if (recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from recently added.");
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
            Log.i(TAG, "onChanged: COMPLETELY REMOVE FROM CART (Recently Added) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    //THIS NULL CHECK IS TO HANDLE THE SCENARIO OF PRODUCT NOT AVAILABLE ON CURRENT PAGE.
                    if (recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductCompletelyRemoved(Integer.parseInt(generalResponse.getProd_id()));

                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), true);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));

                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from Recently Added.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while completely removing product from cart from Cart!");

                    if (recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from Recently Added.");
                    }
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<OrderAcceptResponse> orderAcceptResponseObserver = new Observer<OrderAcceptResponse>() {
        @Override
        public void onChanged(OrderAcceptResponse orderAcceptResponse) {
            Log.i(TAG, "onChanged: ORDER ACCEPTANCE Observer fired!\norderAcceptResponse: " + orderAcceptResponse);

            if (orderAcceptResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (orderAcceptResponse.getError() == 200) {
                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getStatus() == 0) {
                        Log.i(TAG, "onChanged: Vendor Accepting Orders, Proceeding to place Order.");

                        if (recentlyAddedViewModel.toBeAddedProductId != 0) {
                            //PROCEED TO ADD PRODUCT TO CART
                            if (!recentlyAddedViewModel.addToCartFromRecentlyAdded(recentlyAddedViewModel.toBeAddedProductId, recentlyAddedViewModel.toBeAddedProductQty)) {
                                DialogUtil.showNoInternetToast(requireActivity());

                                Log.i(TAG, "addProductToCart: BEFORE, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(recentlyAddedViewModel.toBeAddedProductId);
                                Log.i(TAG, "addProductToCart: AFTER, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                                if (recentlyAddedViewModel.toBeAddedProductQty == 1) {
                                    prodIdQtyHashMap.remove(recentlyAddedViewModel.toBeAddedProductId);
                                } else {
                                    prodIdQtyHashMap.put(recentlyAddedViewModel.toBeAddedProductId, --recentlyAddedViewModel.toBeAddedProductQty);
                                }
                                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                                recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(recentlyAddedViewModel.toBeAddedProductId, recentlyAddedViewModel.toBeAddedProductQty, false);
                            }
                        }
                    } else {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't add to cart.");
                        DialogUtil.showCustomSnackbar(requireActivity(), recentlyAddedRecyclerView, orderAcceptResponse.getMsg(), -1);

                        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(recentlyAddedViewModel.toBeAddedProductId, recentlyAddedViewModel.toBeAddedProductQty, false);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Order Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(recentlyAddedViewModel.toBeAddedProductId, recentlyAddedViewModel.toBeAddedProductQty, false);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };


    public void loadMoreRecentlyAddedProducts() {
        Log.i(TAG, "loadMoreRecentlyAddedProducts: fired!");

        if (!recentlyAddedViewModel.isRecentlyAddedApiExecuting) {
            Log.i(TAG, "loadMoreRecentlyAddedProducts: recentlyAddedViewModel.currentPage: " + recentlyAddedViewModel.currentPage);

            int nextPage = recentlyAddedViewModel.currentPage;

            Log.i(TAG, "loadMoreRecentlyAddedProducts: recentlyAddedViewModel.currentPage: " + recentlyAddedViewModel.currentPage
                    + "\tnextPage: " + ++nextPage + "\trecentlyAddedViewModel.lastPage: " + recentlyAddedViewModel.lastPage);

            if (nextPage <= recentlyAddedViewModel.lastPage) {
                Log.i(TAG, "loadMoreRecentlyAddedProducts: Loading More Best Deal Products!");
                if (!recentlyAddedViewModel.getRecentlyAddedProducts(ApplicationData.getDefaultStoreId(), nextPage)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showLoadingDialog1(requireActivity(), RECENTLY_ADDED_TAG);
                }
            } else {
                Log.i(TAG, "loadMoreRecentlyAddedProducts: Reached END of PAGE!");
                Toast.makeText(requireActivity(), "No More Products to Show!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "loadMoreRecentlyAddedProducts: More Recently Added products already being loaded!");
        }
    }

    /*NEEDED FOR 1ST TIME LOADING.*/
    public void reloadRecentlyAdded() {
        Log.i(TAG, "reloadRecentlyAdded: fired!");

        if (!recentlyAddedViewModel.getRecentlyAddedProducts(ApplicationData.getDefaultStoreId(), 1)) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            DialogUtil.showLoadingDialog1(requireActivity(), RECENTLY_ADDED_TAG);
        }
    }

    public void showCartDataInRecentlyAdded(HashMap<Integer, Integer> prodIdOrderQtyHashMap) {
        Log.i(TAG, "showCartDataInRecentlyAdded: fired!");

        if (recentlyAddedViewModel != null) {
            if (prodIdOrderQtyHashMap.size() != 0) {
                recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.setProdIdOrderQtyHashMap(prodIdOrderQtyHashMap);
            } else {
                Log.i(TAG, "showCartDataInRecentlyAdded: prodIdQtyHashMap Size was 0.");
            }
        } else {
            Log.i(TAG, "showCartDataInRecentlyAdded: recentlyAddedViewModel currently NULL!");
        }
    }

    //TO UPDATE THE SELECTED PRODUCTS IN CART
    private void updateCartData(int prodId, boolean completelyRemove) {
        Log.i(TAG, "updateCartData: fired!");

        Log.i(TAG, "updateCartData: RECENTLYADDED, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        if (!completelyRemove) {
            prodIdQtyHashMap.put(prodId, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.get(prodId));
        } else {
            prodIdQtyHashMap.remove(prodId);
        }
        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
        Log.i(TAG, "updateCartData: RECENTLYADDED, AFTER, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
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
    private void clearRecentlyAddedData() {
        Log.i(TAG, "clearRecentlyAddedData: fired!");

        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.setProdIdOrderQtyHashMap(null);
        recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.setProductsModel_dataList(null);

        recentlyAddedViewModel.setPooledRecentlyAddedProductsModel_dataList(null);
        recentlyAddedViewModel.currentPage = 0;
        recentlyAddedViewModel.lastPage = 0;

        cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
    }

    public void clearMutableLiveData() {
        Log.i(TAG, "clearMutableLiveData: RECENTLY ADDED!");

        if (recentlyAddedViewModel.getAddToCartMutableLiveData().getValue() != null) {
            recentlyAddedViewModel.setAddToCartMutableLiveData(null);
        }
        if (recentlyAddedViewModel.getRemoveFromCartMutableLiveData().getValue() != null) {
            recentlyAddedViewModel.setRemoveFromCartMutableLiveData(null);
        }
        if (recentlyAddedViewModel.getCompletelyRemoveFromCartMutableLiveData().getValue() != null) {
            recentlyAddedViewModel.setCompletelyRemoveFromCartMutableLiveData(null);
        }
        recentlyAddedViewModel.setRecentlyAddedProductsModelMutableLiveData(null);

        if (recentlyAddedViewModel.getOrderAcceptResponseMutableLiveData().getValue() != null) {
            recentlyAddedViewModel.setOrderAcceptResponseMutableLiveData(null);
        }
    }

    @Override
    public void addProductToCart(int productId, int qty) {
        Log.i(TAG, "addProductToCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {

            //PROCEED TO CHECK FOR ORDER ACCEPTANCE
            if (!recentlyAddedViewModel.checkVendorOrderAcceptance(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 1) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, --qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            } else {
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }

            //PROCEED TO ADD PRODUCT TO CART
//            if (!recentlyAddedViewModel.addToCartFromRecentlyAdded(productId, qty)) {
//                DialogUtil.showNoInternetToast(requireActivity());
//
//                Log.i(TAG, "addProductToCart: BEFORE, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
//                Log.i(TAG, "addProductToCart: AFTER, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//
//                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//                if (qty == 1) {
//                    prodIdQtyHashMap.remove(productId);
//                } else {
//                    prodIdQtyHashMap.put(productId, --qty);
//                }
//                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//
//                recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
//            }
        } else {
            recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    @Override
    public void removeProductFromCart(int productId, int qty) {
        Log.i(TAG, "removeProductFromCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            //PROCEED TO REMOVE PRODUCT FROM CART
            if (!recentlyAddedViewModel.removeFromCartFromRecentlyAdded(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + recentlyAddedViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 0) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, ++qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
            }
        } else {
            recentlyAddedViewModel.recentlyAddedRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
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