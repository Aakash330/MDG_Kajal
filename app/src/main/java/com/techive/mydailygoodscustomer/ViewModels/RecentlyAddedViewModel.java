package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.RecentlyAddedRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.Models.ProductsModel_Data;
import com.techive.mydailygoodscustomer.Repositories.CartRepository;
import com.techive.mydailygoodscustomer.Repositories.RecentlyAddedRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecentlyAddedViewModel extends AndroidViewModel {
    private static final String TAG = "RecentlyAddedViewModel";

    private RecentlyAddedRepository recentlyAddedRepository;
    private CartRepository cartRepository;

    public RecentlyAddedRecyclerViewAdapter recentlyAddedRecyclerViewAdapter;

    private MutableLiveData<ProductsModel> recentlyAddedProductsModelMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;

    private List<ProductsModel_Data> pooledRecentlyAddedProductsModel_dataList;

    public HashMap<Integer, Integer> toBeNotifiedProdIdQtyHashMap;

    public int currentPage = 0, lastPage = 0,
            toBeAddedProductId, toBeAddedProductQty;

    public boolean isRecentlyAddedApiExecuting = false;

    public RecentlyAddedViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "RecentlyAddedViewModel: fired!");

        initRecentlyAddedViewModel();
    }

    private void initRecentlyAddedViewModel() {
        Log.i(TAG, "initRecentlyAddedViewModel: fired!");

        if (cartRepository != null) {
            Log.i(TAG, "initRecentlyAddedViewModel: CartRepository already initialized in RecentlyAddedViewModel.");
        } else {
            cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());
//            cartRepository.initCartRepository();

            addToCartMutableLiveData = cartRepository.getAddToCartMutableLiveData();
            removeFromCartMutableLiveData = cartRepository.getRemoveFromCartMutableLiveData();
            completelyRemoveFromCartMutableLiveData = cartRepository.getCompletelyRemoveFromCartMutableLiveData();
            orderAcceptResponseMutableLiveData = cartRepository.getOrderAcceptResponseMutableLiveData();
        }

        if (recentlyAddedRepository != null) {
            Log.i(TAG, "initRecentlyAddedViewModel: BestDealsRepository already initialized in RecentlyAddedViewModel.");
            return;
        }
        recentlyAddedRepository = RecentlyAddedRepository.getRecentlyAddedRepositoryInstance(getApplication().getApplicationContext());
//        recentlyAddedRepository.initRecentlyAddedRepository();

        recentlyAddedRecyclerViewAdapter = new RecentlyAddedRecyclerViewAdapter(getApplication().getApplicationContext());

        recentlyAddedProductsModelMutableLiveData = recentlyAddedRepository.getRecentlyAddedProductsModelMutableLiveData();

        pooledRecentlyAddedProductsModel_dataList = new ArrayList<>();

        toBeNotifiedProdIdQtyHashMap = new HashMap<>();
    }

    /* GETTERS - START */
    public MutableLiveData<ProductsModel> getRecentlyAddedProductsModelMutableLiveData() {
        return recentlyAddedProductsModelMutableLiveData;
    }

    public List<ProductsModel_Data> getPooledRecentlyAddedProductsModel_dataList() {
        return pooledRecentlyAddedProductsModel_dataList;
    }

    public void setPooledRecentlyAddedProductsModel_dataList(List<ProductsModel_Data> pooledRecentlyAddedProductsModel_dataList) {
        if (pooledRecentlyAddedProductsModel_dataList == null) {
            this.pooledRecentlyAddedProductsModel_dataList.clear();
            return;
        }
        if (this.pooledRecentlyAddedProductsModel_dataList.size() > 0) {
            this.pooledRecentlyAddedProductsModel_dataList.addAll(pooledRecentlyAddedProductsModel_dataList);
            return;
        }
        this.pooledRecentlyAddedProductsModel_dataList = pooledRecentlyAddedProductsModel_dataList;
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

    public MutableLiveData<OrderAcceptResponse> getOrderAcceptResponseMutableLiveData() {
        return orderAcceptResponseMutableLiveData;
    }
    /* GETTERS - END */

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

    public void setRecentlyAddedProductsModelMutableLiveData(ProductsModel recentlyAddedProducts) {
        recentlyAddedRepository.setRecentlyAddedProductsModelMutableLiveData(recentlyAddedProducts);
    }

    public void setOrderAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setOrderAcceptResponseMutableLiveData(orderAcceptResponse);
    }
    /* SETTERS - END */

    public boolean getRecentlyAddedProducts(int vendorId, int pageNumber) {
        Log.i(TAG, "getRecentlyAddedProducts: fired!");

        return recentlyAddedRepository.getRecentlyAddedProducts(vendorId, pageNumber);
    }

    /* CART REPOSITORY - ADD/INCREMENT TO CART */
    public boolean addToCartFromRecentlyAdded(int productId, int qty) {
        Log.i(TAG, "addToCartFromRecentlyAdded: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "addToCartFromRecentlyAdded: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

//        Log.i(TAG, "addToCartFromRecentlyAdded: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//        prodIdQtyHashMap.put(productId, qty);
//        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//        Log.i(TAG, "addToCartFromRecentlyAdded: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());

        return cartRepository.addToCart(productId);
    }

    /* CART REPOSITORY - DECREMENT FROM CART */
    public boolean removeFromCartFromRecentlyAdded(int productId, int qty) {
        Log.i(TAG, "removeFromCartFromRecentlyAdded: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "removeFromCartFromRecentlyAdded: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

//        Log.i(TAG, "removeFromCartFromRecentlyAdded: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//        prodIdQtyHashMap.put(productId, qty);
//        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//        Log.i(TAG, "removeFromCartFromRecentlyAdded: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());

        //WILL CHECK IF QTY IS 0, THEN WILL CALL ANOTHER FUNCTION TO DELETE PRODUCT FROM CART.
        if (qty == 0) {
            return cartRepository.completelyRemoveFromCart(productId);
        } else {
            return cartRepository.removeFromCart(productId);
        }
    }

    public boolean checkVendorOrderAcceptance(int productId, int qty) {
        Log.i(TAG, "checkVendorOrderAcceptance: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "checkVendorOrderAcceptance: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        toBeAddedProductId = productId;
        toBeAddedProductQty = qty;

        return cartRepository.checkVendorOrderAcceptance();
    }
}
