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
import com.techive.mydailygoodscustomer.ViewModels.BestDealsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BestDealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BestDealsFragment extends Fragment implements OnProductCartListener {
    private static final String TAG = "BestDealsFragment";
    private static final String BEST_DEALS_PRODUCTS_TAG = "BEST_DEALS_PRODUCTS_TAG";

    private RecyclerView bestDealsRecyclerView;

    private CartInterface cartInterface;

    private BestDealsViewModel bestDealsViewModel;

    public BestDealsFragment() {
        Log.i(TAG, "BestDealsFragment: Empty Constructor fired!");
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BestDealsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BestDealsFragment newInstance() {
        Log.i(TAG, "newInstance: fired!");

        return new BestDealsFragment();
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

        bestDealsViewModel = new ViewModelProvider(this).get(BestDealsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired!");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best_deals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponents(view);

        initAdapters();

        initObservers();

        initListeners();

//        if (!bestDealsViewModel.getBestDealsProducts(ApplicationData.getDefaultStoreId(), 1)) {
//            DialogUtil.showNoInternetToast(requireActivity());
//        } else {
//            bestDealsViewModel.isBestDealsApiExecuting = true;
//            DialogUtil.showLoadingDialog1(requireActivity(), BEST_DEALS_PRODUCTS_TAG);
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
        Log.i(TAG, "onResume: BestDeals fired!");

        /*CHECK FOR DIFFERENCE BETWEEN OLD ID & DEFAULT STORE ID IN APPLICATION DATA.*/
        Log.i(TAG, "onResume: DefaultStoreId = " + ApplicationData.getDefaultStoreId()
                + "\tApplicationData.getOldStoreIdBestDeals = " + ApplicationData.getOldStoreIdBestDeals());
        if (ApplicationData.getDefaultStoreId() != ApplicationData.getOldStoreIdBestDeals()) {

            /*WILL ALSO HAVE TO CLEAR THE EXISTING DATA BOTH IN MEMORY & IN VIEW.*/
            clearBestDealsData();

            if (!bestDealsViewModel.getBestDealsProducts(ApplicationData.getDefaultStoreId(), 1)) {
                DialogUtil.showNoInternetToast(requireActivity());
                if (bestDealsViewModel.currentPage == 0) {
                    ((HomeFragment) BestDealsFragment.this.getParentFragment()).toggleReloadIcon(true);
                }
            } else {
                bestDealsViewModel.isBestDealsApiExecuting = true;
                DialogUtil.showLoadingDialog1(requireActivity(), BEST_DEALS_PRODUCTS_TAG);
            }

            int cartTotalQty = 0;
            for (Integer integer : ApplicationData.getProdIdOrderQtyHashMap().values()) {
                cartTotalQty = cartTotalQty + integer;
            }
            Log.i(TAG, "onResume: cartTotalQty: " + cartTotalQty);
            ApplicationData.setCartTotalQty(cartTotalQty);
            cartInterface.showBadgeOnCart(cartTotalQty);

            ApplicationData.setOldStoreIdBestDeals(ApplicationData.getDefaultStoreId());
            Log.i(TAG, "onResume: After setting DefaultStoreId in the OldStoreIdBestDeals.");
        } else {
            Log.i(TAG, "onResume: Not doing anything since OldStoreIdBestDeals = DefaultStoreId");

            Log.i(TAG, "onResume: bestDealsViewModel.getPooledBestDealsProductsModel_dataList().size(): "
                    + bestDealsViewModel.getPooledBestDealsProductsModel_dataList().size());

            if (bestDealsViewModel.getPooledBestDealsProductsModel_dataList().size() == 0) {
                if (!bestDealsViewModel.getBestDealsProducts(ApplicationData.getDefaultStoreId(), 1)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                    if (bestDealsViewModel.currentPage == 0) {
                        ((HomeFragment) BestDealsFragment.this.getParentFragment()).toggleReloadIcon(true);
                    }
                } else {
                    bestDealsViewModel.isBestDealsApiExecuting = true;
                    DialogUtil.showLoadingDialog1(requireActivity(), BEST_DEALS_PRODUCTS_TAG);
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

        bestDealsRecyclerView = view.findViewById(R.id.bestDealsRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        bestDealsRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        bestDealsRecyclerView.setAdapter(bestDealsViewModel.bestDealsRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        bestDealsViewModel.getBestDealsProductsModelMutableLiveData().observe(this, bestDealsProductsModelObserver);
        bestDealsViewModel.getAddToCartMutableLiveData().observe(this, addToCartObserver);
        bestDealsViewModel.getRemoveFromCartMutableLiveData().observe(this, removeFromCartObserver);
        bestDealsViewModel.getCompletelyRemoveFromCartMutableLiveData().observe(this, completelyRemoveFromCartObserver);

        bestDealsViewModel.getOrderAcceptResponseMutableLiveData().observe(this, orderAcceptResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        bestDealsViewModel.bestDealsRecyclerViewAdapter.setOnProductCartListener(this);
    }

    private final Observer<ProductsModel> bestDealsProductsModelObserver = new Observer<ProductsModel>() {
        @Override
        public void onChanged(ProductsModel productsModel) {
            Log.i(TAG, "onChanged: BEST DEALS Observer fired! \nproductsModel: " + productsModel);

            if (productsModel != null) {
                DialogUtil.dismissLoadingDialog1(BEST_DEALS_PRODUCTS_TAG);
                bestDealsViewModel.isBestDealsApiExecuting = false;

                if (productsModel.getError() == 200) {
                    if (bestDealsViewModel.currentPage == 0) {
                        ((HomeFragment) BestDealsFragment.this.getParentFragment()).toggleReloadIcon(false);
                    }

                    bestDealsViewModel.currentPage = productsModel.getCurrent_page();
                    bestDealsViewModel.lastPage = productsModel.getLast_page();

                    Log.i(TAG, "onChanged: bestDealsViewModel.currentPage: " + bestDealsViewModel.currentPage
                            + "\tbestDealsViewModel.lastPage: " + bestDealsViewModel.lastPage);

                    bestDealsViewModel.setPooledBestDealsProductsModel_dataList(productsModel.getData());

                    bestDealsViewModel.bestDealsRecyclerViewAdapter.setProductsModel_dataList(
                            bestDealsViewModel.getPooledBestDealsProductsModel_dataList());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while fetching Best Deals Products!");
                    Toast.makeText(requireActivity(), productsModel.getMsg(), Toast.LENGTH_SHORT).show();

                    //WILL HAVE TO ONLY DO FOR THE 1ST TIME
                    if (bestDealsViewModel.currentPage == 0) {
                        ((HomeFragment) BestDealsFragment.this.getParentFragment()).toggleReloadIcon(true);
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
            Log.i(TAG, "onChanged: ADD TO CART (Best Deals) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    if (bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {

                        bestDealsViewModel.toBeAddedProductId = 0;

                        //NOTIFY BEST DEALS ADAPTER FOR QTY CHANGE
                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), true);
//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
//                        ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())), true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);
                /*WHEN USING APPLICATION DATA DIRECTLY, THEN THERE'S NO NEED TO UPDATE IT OVER HERE, BECAUSE ALREADY
                UPDATED VALUE IS STORED WHEN CALLING THE ADD PRODUCT FUNCTION.*/

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(true);

                        bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Prod add not done from Best Deals fragment!");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while adding product to cart!");

                    if (bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(Integer.parseInt(generalResponse.getProd_id()),
                                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                        bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Prod add not done from Best Deals fragment!");
//                        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//                        int qty = ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id()));
//                        if (qty > 1) {
//                            prodIdQtyHashMap.put(Integer.parseInt(generalResponse.getProd_id()), --qty);
//                        } else {
//                            prodIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
//                        }
//                        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//
//                        ((HomeFragment) getParentFragment()).getHomeViewModel().searchProductsRecyclerViewAdapter.notifyProductAdded(
//                                Integer.parseInt(generalResponse.getProd_id()), qty, false);
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
            Log.i(TAG, "onChanged: REMOVE FROM CART (Best Deals) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));
//                Log.i(TAG, "onChanged: ApplicationData.getProdIdOrderQtyHashMap().get(generalResponse.getProd_id()): "
//                        + ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())));

                    if (bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {

                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                true);

//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(Integer.parseInt(generalResponse.getProd_id()),
//                        ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())), true);

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), false);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                        bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from best deals.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while removing product from cart from Cart!");

//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(
//                        Integer.parseInt(generalResponse.getProd_id()),
//                        bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
//                        false);

//                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//                int qty = ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id()));
//                prodIdQtyHashMap.put(Integer.parseInt(generalResponse.getProd_id()), ++qty);
//                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//
//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(Integer.parseInt(generalResponse.getProd_id()),
//                        ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())),
//                        false);

                    if (bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(Integer.parseInt(generalResponse.getProd_id()),
                                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())), false);
                        bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(Integer.parseInt(generalResponse.getProd_id()));
                    } else {
                        Log.i(TAG, "onChanged: Product not removed from best deals.");
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
            Log.i(TAG, "onChanged: COMPLETELY REMOVE FROM CART (Best Deals) Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                if (generalResponse.getError() == 200) {
                    Log.i(TAG, "onChanged: bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(generalResponse.getProd_id()): "
                            + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())));

                    if (bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductCompletelyRemoved(Integer.parseInt(generalResponse.getProd_id()));

                        //UPDATE PROD ID & QTY IN APPLICATION DATA.
                        updateCartData(Integer.parseInt(generalResponse.getProd_id()), true);

                        //UPDATE CART QTY IN BADGE
                        updateCartCount(false);

                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from Best Deals.");
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while completely removing product from cart from Cart!");

//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(
//                        Integer.parseInt(generalResponse.getProd_id()),
//                        bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),false);

//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(
//                        Integer.parseInt(generalResponse.getProd_id()),
//                        ApplicationData.getProdIdOrderQtyHashMap().get(Integer.parseInt(generalResponse.getProd_id())),false);

                    if (bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())) != null) {
                        Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(
                                Integer.parseInt(generalResponse.getProd_id()),
                                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(Integer.parseInt(generalResponse.getProd_id())),
                                false);
                    } else {
                        Log.i(TAG, "onChanged: Product not completely removed from Best Deals.");
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
                        Log.i(TAG, "onChanged: Vendor Accepting Orders, Proceeding to addToCart.");

                        if (bestDealsViewModel.toBeAddedProductId != 0) {
                            //PROCEED TO ADD PRODUCT TO CART
                            if (!bestDealsViewModel.addToCartFromBestDeals(bestDealsViewModel.toBeAddedProductId, bestDealsViewModel.toBeAddedProductQty)) {
                                DialogUtil.showNoInternetToast(requireActivity());

                                Log.i(TAG, "addProductToCart: BEFORE, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(bestDealsViewModel.toBeAddedProductId);
                                Log.i(TAG, "addProductToCart: AFTER, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                                if (bestDealsViewModel.toBeAddedProductQty == 1) {
                                    prodIdQtyHashMap.remove(bestDealsViewModel.toBeAddedProductId);
                                } else {
                                    prodIdQtyHashMap.put(bestDealsViewModel.toBeAddedProductId, --bestDealsViewModel.toBeAddedProductQty);
                                }
                                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(bestDealsViewModel.toBeAddedProductId, bestDealsViewModel.toBeAddedProductQty, false);
                            }
                        } else {
                            Log.i(TAG, "onChanged: Product Id was 0, prod not checked from BestDeals.");
                        }
                    } else {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't add to cart.");
                        DialogUtil.showCustomSnackbar(requireActivity(), bestDealsRecyclerView, orderAcceptResponse.getMsg(), -1);

                        bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(bestDealsViewModel.toBeAddedProductId, bestDealsViewModel.toBeAddedProductQty, false);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Order Acceptance!");
                    Toast.makeText(requireActivity(), orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(bestDealsViewModel.toBeAddedProductId, bestDealsViewModel.toBeAddedProductQty, false);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };


    public void loadMoreBestDealsProducts() {
        Log.i(TAG, "loadMoreBestDealsProducts: fired!");

        if (!bestDealsViewModel.isBestDealsApiExecuting) {
            Log.i(TAG, "loadMoreBestDealsProducts: bestDealsViewModel.currentPage: " + bestDealsViewModel.currentPage);

            int nextPage = bestDealsViewModel.currentPage;

            Log.i(TAG, "loadMoreBestDealsProducts: bestDealsViewModel.currentPage: " + bestDealsViewModel.currentPage
                    + "\tnextPage: " + ++nextPage + "\tbestDealsViewModel.lastPage: " + bestDealsViewModel.lastPage);

            if (nextPage <= bestDealsViewModel.lastPage) {
                Log.i(TAG, "loadMoreBestDealsProducts: Loading More Best Deal Products!");
                if (!bestDealsViewModel.getBestDealsProducts(ApplicationData.getDefaultStoreId(), nextPage)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showLoadingDialog1(requireActivity(), BEST_DEALS_PRODUCTS_TAG);
                }
            } else {
                Log.i(TAG, "loadMoreBestDealsProducts: Reached END of PAGE!");
                Toast.makeText(requireActivity(), "No More Products to Show!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(TAG, "loadMoreBestDealsProducts: More BestDeals products already being loaded!");
        }
    }

    public void reloadBestDeals() {
        Log.i(TAG, "reloadBestDeals: fired!");

        if (!bestDealsViewModel.getBestDealsProducts(ApplicationData.getDefaultStoreId(), 1)) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            bestDealsViewModel.isBestDealsApiExecuting = true;
            DialogUtil.showLoadingDialog1(requireActivity(), BEST_DEALS_PRODUCTS_TAG);
        }
    }

    public void showCartDataInBestDeals(HashMap<Integer, Integer> prodIdOrderQtyHashMap) {
        Log.i(TAG, "showCartDataInBestDeals: fired! prodIdOrderQtyHashMap.toString(): " + prodIdOrderQtyHashMap.toString());
        Log.i(TAG, "showCartDataInBestDeals: fired! prodIdOrderQtyHashMap.size(): " + prodIdOrderQtyHashMap.size());

        if (bestDealsViewModel != null) {
            if (prodIdOrderQtyHashMap.size() != 0) {
                bestDealsViewModel.bestDealsRecyclerViewAdapter.setProdIdOrderQtyHashMap(prodIdOrderQtyHashMap);
            } else {
                Log.i(TAG, "showCartDataInBestDeals: prodIdQtyHashMap Size was 0.");
            }
        } else {
            Log.i(TAG, "showCartDataInBestDeals: bestDealsViewModel currently NULL!");
        }
    }

    //TO UPDATE THE SELECTED PRODUCTS IN CART
    private void updateCartData(int prodId, boolean completelyRemove) {
        Log.i(TAG, "updateCartData: fired!");

        Log.i(TAG, "updateCartData: BESTDEALS, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        if (!completelyRemove) {
            prodIdQtyHashMap.put(prodId, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.get(prodId));
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
    private void clearBestDealsData() {
        Log.i(TAG, "clearBestDealsData: fired!");

        bestDealsViewModel.bestDealsRecyclerViewAdapter.setProdIdOrderQtyHashMap(null);
        bestDealsViewModel.bestDealsRecyclerViewAdapter.setProductsModel_dataList(null);

        bestDealsViewModel.setPooledBestDealsProductsModel_dataList(null);
        bestDealsViewModel.currentPage = 0;
        bestDealsViewModel.lastPage = 0;

        cartInterface.showBadgeOnCart(0);
        ApplicationData.setCartTotalQty(0);
    }

    public void clearMutableLiveData() {
        Log.i(TAG, "clearMutableLiveData: BEST DEALS!");

        if (bestDealsViewModel.getAddToCartMutableLiveData().getValue() != null) {
            bestDealsViewModel.setAddToCartMutableLiveData(null);
        }
        if (bestDealsViewModel.getRemoveFromCartMutableLiveData().getValue() != null) {
            bestDealsViewModel.setRemoveFromCartMutableLiveData(null);
        }
        if (bestDealsViewModel.getCompletelyRemoveFromCartMutableLiveData().getValue() != null) {
            bestDealsViewModel.setCompletelyRemoveFromCartMutableLiveData(null);
        }
        bestDealsViewModel.setBestDealsProductsModelMutableLiveData(null);

        if (bestDealsViewModel.getOrderAcceptResponseMutableLiveData().getValue() != null) {
            bestDealsViewModel.setOrderAcceptResponseMutableLiveData(null);
        }
    }

    @Override
    public void addProductToCart(int productId, int qty) {
        Log.i(TAG, "addProductToCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {

            //MODIFIED LOGIC ON 23-8-22
            //PROCEED TO CHECK FOR ORDER ACCEPTANCE
            if (!bestDealsViewModel.checkVendorOrderAcceptance(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 1) {
                    prodIdQtyHashMap.remove(productId);
                } else {
                    prodIdQtyHashMap.put(productId, --qty);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);

                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            } else {
                DialogUtil.showProcessingInfoDialog(requireActivity());
            }

            //PROCEED TO ADD PRODUCT TO CART
//            if (!bestDealsViewModel.addToCartFromBestDeals(productId, qty)) {
//                DialogUtil.showNoInternetToast(requireActivity());
//
//                Log.i(TAG, "addProductToCart: BEFORE, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
//                Log.i(TAG, "addProductToCart: AFTER, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
//                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());
//
//                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//                if (qty == 1) {
//                    prodIdQtyHashMap.remove(productId);
//                } else {
//                    prodIdQtyHashMap.put(productId, --qty);
//                }
//                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//
//                bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
//            }
        } else {
            bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductAdded(productId, qty, false);
            LoginUtil.redirectToLogin(requireActivity(), "Please proceed to Login/Register to begin shopping!");
        }
    }

    @Override
    public void removeProductFromCart(int productId, int qty) {
        Log.i(TAG, "removeProductFromCart: fired! productId: " + productId + "\tqty: " + qty);

        //WILL HAVE TO FIRST CHECK FOR BUYER LOGIN.
        if (ApplicationData.getLoggedInBuyerId() != 0) {
            //PROCEED TO REMOVE PRODUCT FROM CART
            if (!bestDealsViewModel.removeFromCartFromBestDeals(productId, qty)) {
                DialogUtil.showNoInternetToast(requireActivity());

                Log.i(TAG, "addProductToCart: BEFORE, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());
                bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.remove(productId);
                Log.i(TAG, "addProductToCart: AFTER, bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString(): "
                        + bestDealsViewModel.toBeNotifiedProdIdQtyHashMap.toString());

                HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
                if (qty == 0) {
                    prodIdQtyHashMap.remove(productId);
                    bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
                } else {
                    prodIdQtyHashMap.put(productId, ++qty);
                    bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
            }
        } else {
            bestDealsViewModel.bestDealsRecyclerViewAdapter.notifyProductRemoved(productId, qty, false);
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