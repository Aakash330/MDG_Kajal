package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.RegularListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.Models.ProductsModel_Data;
import com.techive.mydailygoodscustomer.Repositories.CartRepository;
import com.techive.mydailygoodscustomer.Repositories.RegularListRepository;
import com.techive.mydailygoodscustomer.Util.ApplicationData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegularListViewModel extends AndroidViewModel {
    private static final String TAG = "RegularListViewModel";

    private RegularListRepository regularListRepository;
    private CartRepository cartRepository;

    public RegularListRecyclerViewAdapter regularListRecyclerViewAdapter;

    private MutableLiveData<ProductsModel> regularListMutableLiveData;
    private MutableLiveData<HomeCart> regularCartMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;

    private List<ProductsModel_Data> pooledRegularListProductsModel_dataList;

    public HashMap<Integer, Integer> toBeNotifiedProdIdQtyHashMap;

    public int currentPage = 0, lastPage = 0,
            toBeAddedProductId, toBeAddedProductQty;

    public boolean regularListApiExecuteSuccess = false;

    public RegularListViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "RegularListViewModel: fired!");

        initRegularListViewModel();
    }

    private void initRegularListViewModel() {
        Log.i(TAG, "initRegularListViewModel: fired!");

        if (cartRepository != null) {
            Log.i(TAG, "initBestDealsViewModel: CartRepository already initialized in BestDealsViewModel.");
        } else {
            cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());
//            cartRepository.initCartRepository();

            addToCartMutableLiveData = cartRepository.getAddToCartMutableLiveData();
            removeFromCartMutableLiveData = cartRepository.getRemoveFromCartMutableLiveData();
            completelyRemoveFromCartMutableLiveData = cartRepository.getCompletelyRemoveFromCartMutableLiveData();
            prodAddAcceptResponseMutableLiveData = cartRepository.getProdAddAcceptResponseMutableLiveData();
        }

        if (regularListRepository != null) {
            Log.i(TAG, "initRegularListViewModel: RegularListRepository already initialized in ViewModel.");
            return;
        }

        regularListRepository = RegularListRepository.getRegularListRepositoryInstance(getApplication().getApplicationContext());
//        regularListRepository.initRegularListRepository();

        regularListRecyclerViewAdapter = new RegularListRecyclerViewAdapter(getApplication().getApplicationContext());

        regularListMutableLiveData = regularListRepository.getRegularListMutableLiveData();
        regularCartMutableLiveData = regularListRepository.getRegularCartMutableLiveData();

        pooledRegularListProductsModel_dataList = new ArrayList<>();

        toBeNotifiedProdIdQtyHashMap = new HashMap<>();
    }

    public void setCartRepository() {
        Log.i(TAG, "setCartRepository: fired!");
        cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());
    }

    /*GETTERS - START*/
    public MutableLiveData<ProductsModel> getRegularListMutableLiveData() {
        return regularListMutableLiveData;
    }

    public MutableLiveData<HomeCart> getRegularCartMutableLiveData() {
        return regularCartMutableLiveData;
    }

    public List<ProductsModel_Data> getPooledRegularListProductsModel_dataList() {
        return pooledRegularListProductsModel_dataList;
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

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }
    /*GETTERS - END*/

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

    public void setRegularListMutableLiveData(ProductsModel regularList) {
        regularListRepository.setRegularListMutableLiveData(regularList);
    }

    public void setRegularCartMutableLiveData(HomeCart regularCart) {
        regularListRepository.setRegularCartMutableLiveData(regularCart);
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setProdAddAcceptResponseMutableLiveData(orderAcceptResponse);
    }
    /* SETTERS - END */

    public void setPooledRegularListProductsModel_dataList(List<ProductsModel_Data> pooledRegularListProductsModel_dataList) {
        if (pooledRegularListProductsModel_dataList == null) {
            Log.i(TAG, "setPooledRegularListProductsModel_dataList: Clearing the pooled RegularList!");
            this.pooledRegularListProductsModel_dataList.clear();
            return;
        } else if (this.pooledRegularListProductsModel_dataList.size() > 0) {
            this.pooledRegularListProductsModel_dataList.addAll(pooledRegularListProductsModel_dataList);
            return;
        }
        this.pooledRegularListProductsModel_dataList = pooledRegularListProductsModel_dataList;
    }

    public HashMap<Integer, Integer> computeProdIdOrderQtyHashMap() {
        Log.i(TAG, "computeProdIdOrderQtyHashMap: fired!");
        HashMap<Integer, Integer> prodIdOrderQtyHashMap = new HashMap<>();

        if (regularCartMutableLiveData.getValue() != null) {
            if (regularCartMutableLiveData.getValue().getData() != null) {
                for (Cart_CartData_Data cart_cartData_data : regularCartMutableLiveData.getValue().getData()) {
                    prodIdOrderQtyHashMap.put(cart_cartData_data.getProd_id(), cart_cartData_data.getQty());
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdOrderQtyHashMap);
            } else {
                Log.i(TAG, "computeProdIdOrderQtyHashMap: regularCartMutableLiveData.getValue().getData() is NULL!");
            }
        } else {
            Log.i(TAG, "computeProdIdOrderQtyHashMap: regularCartMutableLiveData.getValue() is NULL!");
        }
        Log.i(TAG, "computeProdIdOrderQtyHashMap: prodIdOrderQtyHashMap.toString(): " + prodIdOrderQtyHashMap.toString());

        return prodIdOrderQtyHashMap;
    }

    public boolean getBuyerRegularList(int buyerId, int page) {
        Log.i(TAG, "getBuyerRegularList: fired!");

        return regularListRepository.getBuyerRegularList(buyerId, page);
    }

    public boolean getRegularCart() {
        Log.i(TAG, "getRegularCart: fired!");

        return regularListRepository.getCart();
    }

    /* CART REPOSITORY - ADD/INCREMENT TO CART */
    public boolean addToCartFromRegularList(int productId, int qty) {
        Log.i(TAG, "addToCartFromRegularList: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "addToCartFromRegularList: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

//        Log.i(TAG, "addToCartFromRegularList: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//        prodIdQtyHashMap.put(productId, qty);
//        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//        Log.i(TAG, "addToCartFromRegularList: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());

        return cartRepository.addToCart(productId);
    }

    /* CART REPOSITORY - DECREMENT FROM CART */
    public boolean removeFromCartFromRegularList(int productId, int qty) {
        Log.i(TAG, "removeFromCartFromRegularList: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "removeFromCartFromRegularList: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

//        Log.i(TAG, "removeFromCartFromRegularList: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//        prodIdQtyHashMap.put(productId, qty);
//        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//        Log.i(TAG, "removeFromCartFromRegularList: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());

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

        return cartRepository.checkVendorOrderAcceptance2(productId);
    }
}
