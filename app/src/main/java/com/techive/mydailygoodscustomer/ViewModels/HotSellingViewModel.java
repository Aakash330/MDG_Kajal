package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.HotSellingRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.Models.ProductsModel_Data;
import com.techive.mydailygoodscustomer.Repositories.CartRepository;
import com.techive.mydailygoodscustomer.Repositories.HotSellingRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HotSellingViewModel extends AndroidViewModel {
    private static final String TAG = "HotSellingViewModel";

    private HotSellingRepository hotSellingRepository;
    private CartRepository cartRepository;

    public HotSellingRecyclerViewAdapter hotSellingRecyclerViewAdapter;

    private MutableLiveData<ProductsModel> hotSellingProductsModelMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;

    private List<ProductsModel_Data> pooledHotSellingProductsModel_dataList;

    public HashMap<Integer, Integer> toBeNotifiedProdIdQtyHashMap;

    public int currentPage = 0, lastPage = 0,
            toBeAddedProductId, toBeAddedProductQty;

    public boolean isHotSellingApiExecuting = false;

    public HotSellingViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "HotSellingViewModel: fired!");

        initHotSellingViewModel();
    }

    private void initHotSellingViewModel() {
        Log.i(TAG, "initHotSellingViewModel: fired!");

        if (cartRepository != null) {
            Log.i(TAG, "initHotSellingViewModel: CartRepository already initialized in HotSellingViewModel.");
        } else {
            cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());

            addToCartMutableLiveData = cartRepository.getAddToCartMutableLiveData();
            removeFromCartMutableLiveData = cartRepository.getRemoveFromCartMutableLiveData();
            completelyRemoveFromCartMutableLiveData = cartRepository.getCompletelyRemoveFromCartMutableLiveData();
            prodAddAcceptResponseMutableLiveData = cartRepository.getProdAddAcceptResponseMutableLiveData();
        }

        if (hotSellingRepository != null) {
            Log.i(TAG, "initHotSellingViewModel: HotSelling Repository already initialized in HotSelling ViewModel.");
            return;
        }
        hotSellingRepository = HotSellingRepository.getHotSellingRepositoryInstance(getApplication().getApplicationContext());

        hotSellingRecyclerViewAdapter = new HotSellingRecyclerViewAdapter(getApplication().getApplicationContext());

        hotSellingProductsModelMutableLiveData = hotSellingRepository.getHotSellingProductsModelMutableLiveData();

        pooledHotSellingProductsModel_dataList = new ArrayList<>();

        toBeNotifiedProdIdQtyHashMap = new HashMap<>();
    }

    /* GETTERS - START */
    public MutableLiveData<ProductsModel> getHotSellingProductsModelMutableLiveData() {
        return hotSellingProductsModelMutableLiveData;
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

    public List<ProductsModel_Data> getPooledHotSellingProductsModel_dataList() {
        return pooledHotSellingProductsModel_dataList;
    }

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setHotSellingProductsModelMutableLiveData(ProductsModel hotSellingProductsModel) {
        hotSellingRepository.setHotSellingProductsModelMutableLiveData(hotSellingProductsModel);
    }

    public void setAddToCartMutableLiveData(GeneralResponse addToCart) {
        cartRepository.setAddToCartMutableLiveData(addToCart);
    }

    public void setRemoveFromCartMutableLiveData(GeneralResponse removeFromCart) {
        cartRepository.setRemoveFromCartMutableLiveData(removeFromCart);
    }

    public void setCompletelyRemoveFromCartMutableLiveData(GeneralResponse completelyRemoveFromCart) {
        cartRepository.setCompletelyRemoveFromCartMutableLiveData(completelyRemoveFromCart);
    }

    public void setPooledHotSellingProductsModel_dataList(List<ProductsModel_Data> pooledHotSellingProductsModel_dataList) {
        if (pooledHotSellingProductsModel_dataList == null) {
            this.pooledHotSellingProductsModel_dataList.clear();
            return;
        }
        if (this.pooledHotSellingProductsModel_dataList.size() > 0) {
            this.pooledHotSellingProductsModel_dataList.addAll(pooledHotSellingProductsModel_dataList);
            return;
        }
        this.pooledHotSellingProductsModel_dataList = pooledHotSellingProductsModel_dataList;
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setProdAddAcceptResponseMutableLiveData(orderAcceptResponse);
    }
    /* SETTERS - END */

    // DOING ADD/INCREMENT TO CART FROM WITHIN THE CART REPOSITORY.
    /* CART REPOSITORY - ADD/INCREMENT TO CART */
//    public boolean addToCartFromHotSelling(int productId, int qty) {
//        Log.i(TAG, "addToCartFromHotSelling: fired!");
//
//        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
//        Log.i(TAG, "addToCartFromHotSelling: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());
//
//        return cartRepository.addToCart(productId);
//    }

    /* CART REPOSITORY - DECREMENT FROM CART */
    public boolean removeFromCartFromHotSelling(int productId, int qty) {
        Log.i(TAG, "removeFromCartFromHotSelling: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "removeFromCartFromHotSelling: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        //WILL CHECK IF QTY IS 0, THEN WILL CALL ANOTHER FUNCTION TO DELETE PRODUCT FROM CART.
        if (qty == 0) {
            return cartRepository.completelyRemoveFromCart(productId);
        } else {
            return cartRepository.removeFromCart(productId);
        }
    }

    public boolean getHotSellingProducts(int vendorId, int pageNumber) {
        Log.i(TAG, "getHotSellingProducts: fired!");

        return hotSellingRepository.getHotSellingProducts(vendorId, pageNumber);
    }

    public boolean checkVendorOrderAcceptance(int productId, int qty) {
        Log.i(TAG, "checkVendorOrderAcceptance: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "checkVendorOrderAcceptance: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        toBeAddedProductId = productId;
        toBeAddedProductQty = qty;

        return cartRepository.checkVendorOrderAcceptance2(productId);
    }
}
