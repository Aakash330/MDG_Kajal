package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.HomeBanner1SliderAdapter;
import com.techive.mydailygoodscustomer.Adapters.HomeCategoryRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.HomeCouponSliderAdapter;
import com.techive.mydailygoodscustomer.Adapters.SearchProductsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_Data;
import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.HomeModel;
import com.techive.mydailygoodscustomer.Models.SearchProducts;
import com.techive.mydailygoodscustomer.Models.SearchProducts_Products_Data;
import com.techive.mydailygoodscustomer.Repositories.CartRepository;
import com.techive.mydailygoodscustomer.Repositories.HomeRepository;
import com.techive.mydailygoodscustomer.Util.ApplicationData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";

    private HomeRepository homeRepository;
    private CartRepository cartRepository;

    private MutableLiveData<HomeModel> homeModelMutableLiveData;
    private MutableLiveData<HomeCart> homeCartMutableLiveData;
    private MutableLiveData<SearchProducts> searchProductsMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<SearchProducts> productsByCategoryMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;

    public HomeCouponSliderAdapter homeCouponSliderAdapter;
    public HomeCategoryRecyclerViewAdapter homeCategoryRecyclerViewAdapter;
    public HomeBanner1SliderAdapter homeBanner1SliderAdapter;
    public SearchProductsRecyclerViewAdapter searchProductsRecyclerViewAdapter;

    private List<SearchProducts_Products_Data> pooledSearchProducts_products_dataList;
    private List<SearchProducts_Products_Data> pooledCategoryBasedProducts_products_dataList;

    public String searchQuery = "";
    public int searchProductsCurrentPage, searchProductsLastPage, categoryBasedProductsCurrentPage, categoryBasedProductsLastPage;
    public int currentCategoryId, toBeAddedProductId, toBeAddedProductQty;

    public HashMap<Integer, Integer> toBeNotifiedProdIdQtyHashMap;

    public boolean homeModelSuccess = false, homeCartSuccess = false;

    public HomeViewModel(@NonNull @NotNull Application application) {
        super(application);

        initHomeViewModel();
    }

    private void initHomeViewModel() {
        Log.i(TAG, "initHomeViewModel: fired!");

        if (cartRepository == null) {
            cartRepository = CartRepository.getCartRepositoryInstance(getApplication().getApplicationContext());
//            cartRepository.initCartRepository();

            addToCartMutableLiveData = cartRepository.getAddToCartMutableLiveData();
            removeFromCartMutableLiveData = cartRepository.getRemoveFromCartMutableLiveData();
            completelyRemoveFromCartMutableLiveData = cartRepository.getCompletelyRemoveFromCartMutableLiveData();
            prodAddAcceptResponseMutableLiveData = cartRepository.getProdAddAcceptResponseMutableLiveData();
        } else {
            Log.i(TAG, "initHomeViewModel: Cart Repository already initialized in HomeViewModel!");
        }

        if (homeRepository != null) {
            return;
        }
        homeRepository = HomeRepository.getHomeRepositoryInstance(getApplication().getApplicationContext());
//        homeRepository.initHomeRepository();

        homeModelMutableLiveData = homeRepository.getHomeModelMutableLiveData();
        homeCartMutableLiveData = homeRepository.getHomeCartMutableLiveData();
        searchProductsMutableLiveData = homeRepository.getSearchProductsMutableLiveData();
        productsByCategoryMutableLiveData = homeRepository.getProductsByCategoryMutableLiveData();

        homeCouponSliderAdapter = new HomeCouponSliderAdapter();
        homeCategoryRecyclerViewAdapter = new HomeCategoryRecyclerViewAdapter(getApplication().getApplicationContext());
        homeBanner1SliderAdapter = new HomeBanner1SliderAdapter(getApplication().getApplicationContext());
        searchProductsRecyclerViewAdapter = new SearchProductsRecyclerViewAdapter(getApplication().getApplicationContext());

        toBeNotifiedProdIdQtyHashMap = new HashMap<>();

        pooledSearchProducts_products_dataList = new ArrayList<>();
        pooledCategoryBasedProducts_products_dataList = new ArrayList<>();
    }

    /* GETTERS - START */
    public MutableLiveData<HomeModel> getHomeModelMutableLiveData() {
        return homeModelMutableLiveData;
    }

    public MutableLiveData<HomeCart> getHomeCartMutableLiveData() {
        return homeCartMutableLiveData;
    }

    public MutableLiveData<SearchProducts> getSearchProductsMutableLiveData() {
        return searchProductsMutableLiveData;
    }

    public List<SearchProducts_Products_Data> getPooledSearchProducts_products_dataList() {
        return pooledSearchProducts_products_dataList;
    }

    public void setPooledSearchProducts_products_dataList(List<SearchProducts_Products_Data> pooledSearchProducts_products_dataList) {
        if (pooledSearchProducts_products_dataList == null) {
            this.pooledSearchProducts_products_dataList.clear();
            return;
        }
        if (this.pooledSearchProducts_products_dataList.size() > 0) {
            this.pooledSearchProducts_products_dataList.addAll(pooledSearchProducts_products_dataList);
            return;
        }
        this.pooledSearchProducts_products_dataList = pooledSearchProducts_products_dataList;
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

    public MutableLiveData<SearchProducts> getProductsByCategoryMutableLiveData() {
        return productsByCategoryMutableLiveData;
    }

    public List<SearchProducts_Products_Data> getPooledCategoryBasedProducts_products_dataList() {
        return pooledCategoryBasedProducts_products_dataList;
    }

    public void setPooledCategoryBasedProducts_products_dataList(List<SearchProducts_Products_Data> pooledCategoryBasedProducts_products_dataList) {
        if (pooledCategoryBasedProducts_products_dataList == null) {
            this.pooledCategoryBasedProducts_products_dataList.clear();
            return;
        }
        if (this.pooledCategoryBasedProducts_products_dataList.size() > 0) {
            this.pooledCategoryBasedProducts_products_dataList.addAll(pooledCategoryBasedProducts_products_dataList);
            return;
        }
        this.pooledCategoryBasedProducts_products_dataList = pooledCategoryBasedProducts_products_dataList;
    }

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setSearchProductsMutableLiveData(SearchProducts searchProducts) {
        homeRepository.setSearchProductsMutableLiveData(searchProducts);
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

    public void setProductsByCategoryMutableLiveData(SearchProducts searchProducts) {
        homeRepository.setProductsByCategoryMutableLiveData(searchProducts);
    }

    public void setHomeCartMutableLiveData(HomeCart homeCart) {
        homeRepository.setHomeCartMutableLiveData(homeCart);
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        cartRepository.setProdAddAcceptResponseMutableLiveData(orderAcceptResponse);
    }
    /* SETTERS - END */

    public HashMap<Integer, Integer> computeProdIdOrderQtyHashMap() {
        Log.i(TAG, "computeProdIdOrderQtyHashMap: fired!");
        HashMap<Integer, Integer> prodIdOrderQtyHashMap = new HashMap<>();

        if (homeCartMutableLiveData.getValue() != null) {
            if (homeCartMutableLiveData.getValue().getData() != null) {
                int totalQty = 0;
                for (Cart_CartData_Data cart_cartData_data : homeCartMutableLiveData.getValue().getData()) {
                    prodIdOrderQtyHashMap.put(cart_cartData_data.getProd_id(), cart_cartData_data.getQty());
                    totalQty = totalQty + cart_cartData_data.getQty();
                }
                ApplicationData.setProdIdOrderQtyHashMap(prodIdOrderQtyHashMap);
                ApplicationData.setCartTotalQty(totalQty);
            } else {
                Log.i(TAG, "computeProdIdOrderQtyHashMap: homeCartMutableLiveData.getValue().getData() is NULL!");
            }
        } else {
            Log.i(TAG, "computeProdIdOrderQtyHashMap: homeCartMutableLiveData.getValue() is NULL!");
        }
        Log.i(TAG, "computeProdIdOrderQtyHashMap: prodIdOrderQtyHashMap.toString(): " + prodIdOrderQtyHashMap.toString());

        return prodIdOrderQtyHashMap;
    }

    public boolean getHomeBannerCategoryData(int vendorId) {
        Log.i(TAG, "getHomeBannerCategoryData: fired!");

        return homeRepository.getHomeBannerCategoryData(vendorId);
    }

    public boolean getHomeCart() {
        Log.i(TAG, "getHomeCart: fired!");

        return homeRepository.getCart();
    }

    public boolean searchProducts(String searchQuery, int page) {
        Log.i(TAG, "searchProducts: fired!");

        if (!searchQuery.matches(this.searchQuery)) {
            //CLEAR THE EXISTING SEARCH DATA FROM VIEWMODEL & ADAPTER
            setPooledSearchProducts_products_dataList(null);
            searchProductsRecyclerViewAdapter.setSearchProducts_products_dataList(pooledSearchProducts_products_dataList);
        }

        this.searchQuery = searchQuery;
        return homeRepository.searchProducts(searchQuery, page);
    }

//    public boolean addToCartFromHome(int productId, int qty) {
//        Log.i(TAG, "addToCartFromHome: fired!");
//
//        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
//        Log.i(TAG, "addToCartFromHome: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());
//
//        Log.i(TAG, "addToCartFromHome: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
//        prodIdQtyHashMap.put(productId, qty);
//        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
//        Log.i(TAG, "addToCartFromHome: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
//
//        return cartRepository.addToCart(productId);
//    }

    public boolean removeFromCartFromHome(int productId, int qty) {
        Log.i(TAG, "removeFromCartFromHome: fired!");

        toBeNotifiedProdIdQtyHashMap.put(productId, qty);
        Log.i(TAG, "removeFromCartFromHome: toBeNotifiedProdIdQtyHashMap.toString(): " + toBeNotifiedProdIdQtyHashMap.toString());

        Log.i(TAG, "removeFromCartFromHome: BEFORE, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        prodIdQtyHashMap.put(productId, qty);
        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
        Log.i(TAG, "removeFromCartFromHome: AFTER, ApplicationData.getProdIdOrderQtyHashMap(): " + ApplicationData.getProdIdOrderQtyHashMap());

        //WILL CHECK IF QTY IS 0, THEN WILL CALL ANOTHER FUNCTION TO DELETE PRODUCT FROM CART.
        if (qty == 0) {
            return cartRepository.completelyRemoveFromCart(productId);
        } else {
            return cartRepository.removeFromCart(productId);
        }
    }

    public boolean getProductsByCategory(int categoryId, int page) {
        Log.i(TAG, "getProductsByCategory: fired!");

        if (categoryId != currentCategoryId) {
            //CLEAR THE EXISTING CATEGORY DATA FROM VIEWMODEL & ADAPTER.
            setPooledCategoryBasedProducts_products_dataList(null);
            searchProductsRecyclerViewAdapter.setSearchProducts_products_dataList(pooledCategoryBasedProducts_products_dataList);
        }

        currentCategoryId = categoryId;
        return homeRepository.getCategoryBasedProducts(categoryId, page);
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
