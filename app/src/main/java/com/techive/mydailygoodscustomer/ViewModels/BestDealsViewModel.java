package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.BestDealsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.Models.ProductsModel_Data;
import com.techive.mydailygoodscustomer.Repositories.BestDealsRepository;
import com.techive.mydailygoodscustomer.Repositories.CartRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BestDealsViewModel extends AndroidViewModel {
    private static final String TAG = "BestDealsViewModel";

    private BestDealsRepository bestDealsRepository;
    private CartRepository cartRepository;

    public BestDealsRecyclerViewAdapter bestDealsRecyclerViewAdapter;

    private MutableLiveData<ProductsModel> bestDealsProductsModelMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;

    private List<ProductsModel_Data> pooledBestDealsProductsModel_dataList;

    public HashMap<Integer, Integer> toBeNotifiedProdIdQtyHashMap;

    public int currentPage = 0, lastPage = 0,
            toBeAddedProductId, toBeAddedProductQty;

    public boolean isBestDealsApiExecuting = false;

    public BestDealsViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "BestDealsViewModel: fired!");

        initBestDealsViewModel();
    }

    private void initBestDealsViewModel() {
        Log.i(TAG, "initBestDealsViewModel: fired!");

        if (cartRepository != null) {
            Log.i(TAG, "initBestDealsViewModel: CartRepository already initialized in BestDealsViewModel.");
        } else {
            cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());
//            cartRepository.initCartRepository();

            addToCartMutableLiveData = cartRepository.getAddToCartMutableLiveData();
            removeFromCartMutableLiveData = cartRepository.getRemoveFromCartMutableLiveData();
            completelyRemoveFromCartMutableLiveData = cartRepository.getCompletelyRemoveFromCartMutableLiveData();
            orderAcceptResponseMutableLiveData = cartRepository.getOrderAcceptResponseMutableLiveData();
        }

        if (bestDealsRepository != null) {
            Log.i(TAG, "initBestDealsViewModel: BestDealsRepository already initialized in BestDealsViewModel.");
            return;
        }
        bestDealsRepository = BestDealsRepository.getBestDealsRepositoryInstance(getApplication().getApplicationContext());
//        bestDealsRepository.initBestDealsRepository();

        bestDealsRecyclerViewAdapter = new BestDealsRecyclerViewAdapter(getApplication().getApplicationContext());

        bestDealsProductsModelMutableLiveData = bestDealsRepository.getBestDealsProductsModelMutableLiveData();

        pooledBestDealsProductsModel_dataList = new ArrayList<>();

        toBeNotifiedProdIdQtyHashMap = new HashMap<>();
    }

    /* GETTERS - START */
    public MutableLiveData<ProductsModel> getBestDealsProductsModelMutableLiveData() {
        return bestDealsProductsModelMutableLiveData;
    }

    public List<ProductsModel_Data> getPooledBestDealsProductsModel_dataList() {
        return pooledBestDealsProductsModel_dataList;
    }

    public void setPooledBestDealsProductsModel_dataList(List<ProductsModel_Data> pooledBestDealsProductsModel_dataList) {
        if (pooledBestDealsProductsModel_dataList == null) {
            this.pooledBestDealsProductsModel_dataList.clear();
            return;
        }
        if (this.pooledBestDealsProductsModel_dataList.size() > 0) {
            this.pooledBestDealsProductsModel_dataList.addAll(pooledBestDealsProductsModel_dataList);
            return;
        }
        this.pooledBestDealsProductsModel_dataList = pooledBestDealsProductsModel_dataList;
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

    public void setBestDealsProductsModelMutableLiveData(ProductsModel bestDealsProducts) {
        bestDealsRepository.setBestDealsProductsModelMutableLiveData(bestDealsProducts);
    }

    public void setOrderAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setOrderAcceptResponseMutableLiveData(orderAcceptResponse);
    }
    /* SETTERS - END */

    public boolean getBestDealsProducts(int vendorId, int pageNumber) {
        Log.i(TAG, "getBestDealsProducts: fired!");

        return bestDealsRepository.getBestDealsProducts(vendorId, pageNumber);
    }

    /* CART REPOSITORY - ADD/INCREMENT TO CART */
    public boolean addToCartFromBestDeals(int productId, int qty) {
        Log.i(TAG, "addToCartFromBestDeals: fired!");

        //Wont be needing the below 2 LOCS because prodId & qty is already inserted in the hashmap during vendor order check
//        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
//        Log.i(TAG, "addToCartFromBestDeals: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        return cartRepository.addToCart(productId);
    }

    /* CART REPOSITORY - DECREMENT FROM CART */
    public boolean removeFromCartFromBestDeals(int productId, int qty) {
        Log.i(TAG, "removeFromCartFromBestDeals: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "removeFromCartFromBestDeals: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

//        Log.i(TAG, "removeFromCartFromBestDeals: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//        prodIdQtyHashMap.put(productId, qty);
//        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//        Log.i(TAG, "removeFromCartFromBestDeals: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());

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
