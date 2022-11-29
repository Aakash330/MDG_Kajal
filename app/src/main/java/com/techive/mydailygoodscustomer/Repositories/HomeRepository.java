package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.HomeModel;
import com.techive.mydailygoodscustomer.Models.SearchProducts;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private static final String TAG = "HomeRepository";

    private static HomeRepository homeRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<HomeModel> homeModelMutableLiveData;
    private MutableLiveData<HomeCart> homeCartMutableLiveData;
    private MutableLiveData<SearchProducts> searchProductsMutableLiveData;
    private MutableLiveData<SearchProducts> productsByCategoryMutableLiveData;

    public HomeRepository() {
        Log.i(TAG, "HomeRepository: Empty Constructor fired!");

        initHomeRepository();
    }

    public static HomeRepository getHomeRepositoryInstance(Context context) {
        Log.i(TAG, "getHomeRepositoryInstance: fired!");
        if (homeRepositoryInstance == null) {
            Log.i(TAG, "getHomeRepositoryInstance: Repository Initialized NOW!");
            applicationContext = context;
            homeRepositoryInstance = new HomeRepository();
        }
        return homeRepositoryInstance;
    }

    /*public*/
    private void initHomeRepository() {
        Log.i(TAG, "initHomeRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        homeModelMutableLiveData = new MutableLiveData<>();
        homeCartMutableLiveData = new MutableLiveData<>();
        searchProductsMutableLiveData = new MutableLiveData<>();
        productsByCategoryMutableLiveData = new MutableLiveData<>();
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

    public MutableLiveData<SearchProducts> getProductsByCategoryMutableLiveData() {
        return productsByCategoryMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setSearchProductsMutableLiveData(SearchProducts searchProducts) {
        searchProductsMutableLiveData.setValue(searchProducts);
    }

    public void setProductsByCategoryMutableLiveData(SearchProducts productsByCategory) {
        productsByCategoryMutableLiveData.setValue(productsByCategory);
    }

    public void setHomeCartMutableLiveData(HomeCart homeCart) {
        homeCartMutableLiveData.setValue(homeCart);
    }
    /* SETTERS - END */

    public boolean getHomeBannerCategoryData(int vendorId) {
        Log.i(TAG, "getHomeBannerCategoryData: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getHomeBannerCategoryData: Network Available!");

            Call<HomeModel> homeModelCall = ApplicationData.mdg_customerAPI_interface.getHomeBannerCategoryData(vendorId);
            homeModelCall.enqueue(new Callback<HomeModel>() {
                @Override
                public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                    Log.i(TAG, "onResponse: HOME BANNER CATEGORY Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        HomeModel homeModelUnsuccessful = new HomeModel(response.code(), "Server didn't respond.\nPlease try again later!", null, null, null, null, null, null);
                        homeModelMutableLiveData.setValue(homeModelUnsuccessful);
                        return;
                    }
                    HomeModel homeModel = response.body();
                    Log.i(TAG, "onResponse: homeModel: " + homeModel);
                    //testing
//                    homeModel.setError(1000);
//                    Log.i(TAG, "onResponse: homeModel: " + homeModel);
                    //testing
                    homeModelMutableLiveData.setValue(homeModel);
                }

                @Override
                public void onFailure(Call<HomeModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: HOME BANNER CATEGORY Response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        HomeModel homeModelFailedSocketTimeout = new HomeModel(1001, applicationContext.getString(R.string.connection_timed_out), null, null, null, null, null, null);
                        homeModelMutableLiveData.setValue(homeModelFailedSocketTimeout);
                        return;
                    }

                    HomeModel homeModelFailed = new HomeModel(1000, "Server Request Failed.\nPlease try again later!", null, null, null, null, null, null);
                    homeModelMutableLiveData.setValue(homeModelFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getCart() {
        Log.i(TAG, "getCart: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getCart: Network Available!");

            Call<HomeCart> homeCartCall = ApplicationData.mdg_customerAPI_interface.getCart(ApplicationData.getLoggedInBuyerId(),
                    ApplicationData.getDefaultStoreId());
            homeCartCall.enqueue(new Callback<HomeCart>() {
                @Override
                public void onResponse(Call<HomeCart> call, Response<HomeCart> response) {
                    Log.i(TAG, "onResponse: HOME CART Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        HomeCart homeCartUnsuccessful = new HomeCart("Server didn't respond.\nPlease try again later!", null, response.code());
                        homeCartMutableLiveData.setValue(homeCartUnsuccessful);
                        return;
                    }
                    HomeCart homeCart = response.body();
                    Log.i(TAG, "onResponse: homeCart: " + homeCart);
                    //testing
//                    homeCart.setError(1000);
//                    Log.i(TAG, "onResponse: homeCart: " + homeCart);
                    //testing
                    homeCartMutableLiveData.setValue(homeCart);
                }

                @Override
                public void onFailure(Call<HomeCart> call, Throwable t) {
                    Log.i(TAG, "onFailure: HOME CART Response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        HomeCart homeCartFailedSocketTimeout = new HomeCart(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        homeCartMutableLiveData.setValue(homeCartFailedSocketTimeout);
                        return;
                    }

                    HomeCart homeCartFailed = new HomeCart("Server Request Failed.\nPlease try again later!", null, 1000);
                    homeCartMutableLiveData.setValue(homeCartFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean searchProducts(String searchQuery, int page) {
        Log.i(TAG, "searchProducts: fired! searchQuery: " + searchQuery + "\tpage: " + page);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "searchProducts: Network Available!");

            Call<SearchProducts> searchProductsCall = ApplicationData.mdg_customerAPI_interface.buyerSearchProducts(
                    ApplicationData.getDefaultStoreId(), searchQuery, page);
            searchProductsCall.enqueue(new Callback<SearchProducts>() {
                @Override
                public void onResponse(Call<SearchProducts> call, Response<SearchProducts> response) {
                    Log.i(TAG, "onResponse: SEARCH PRODUCTS Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        SearchProducts searchProductsUnsuccessful = new SearchProducts("Somehow server didn't respond!\nPlease try again later!", response.code(), null);
                        searchProductsMutableLiveData.setValue(searchProductsUnsuccessful);
                        return;
                    }
                    SearchProducts searchProducts = response.body();
                    Log.i(TAG, "onResponse: searchProducts: " + searchProducts);
                    searchProductsMutableLiveData.setValue(searchProducts);
                }

                @Override
                public void onFailure(Call<SearchProducts> call, Throwable t) {
                    Log.i(TAG, "onFailure: SEARCH PRODUCTS Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        SearchProducts searchProductsFailedSocketTimeout = new SearchProducts(applicationContext.getString(R.string.weak_internet_connection), 1001, null);
                        searchProductsMutableLiveData.setValue(searchProductsFailedSocketTimeout);
                        return;
                    }
                    SearchProducts searchProductsFailed = new SearchProducts("Somehow Search Request Failed!\nPlease try again later!", 1000, null);
                    searchProductsMutableLiveData.setValue(searchProductsFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getCategoryBasedProducts(int categoryId, int page) {
        Log.i(TAG, "getCategoryBasedProducts: fired! categoryId: " + categoryId + "\tpage: " + page);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getCategoryBasedProducts: Network Available!");

            Call<SearchProducts> getProductByCategoryCall = ApplicationData.mdg_customerAPI_interface.getProductsByCategory(
                    ApplicationData.getDefaultStoreId(), categoryId, page);
            getProductByCategoryCall.enqueue(new Callback<SearchProducts>() {
                @Override
                public void onResponse(Call<SearchProducts> call, Response<SearchProducts> response) {
                    Log.i(TAG, "onResponse: GET PRODUCTS BY CATEGORY Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        SearchProducts searchProductsUnsuccessful = new SearchProducts(applicationContext.getString(R.string.server_didnt_respond), response.code(), null);
                        productsByCategoryMutableLiveData.setValue(searchProductsUnsuccessful);
                        return;
                    }
                    SearchProducts searchProducts = response.body();
                    Log.i(TAG, "onResponse: searchProducts: " + searchProducts);
                    productsByCategoryMutableLiveData.setValue(searchProducts);
                }

                @Override
                public void onFailure(Call<SearchProducts> call, Throwable t) {
                    Log.i(TAG, "onFailure: GET PRODUCTS BY CATEGORY Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        SearchProducts searchProductsFailedSocketTimeout = new SearchProducts(applicationContext.getString(R.string.weak_internet_connection), 1001, null);
                        productsByCategoryMutableLiveData.setValue(searchProductsFailedSocketTimeout);
                        return;
                    }
                    SearchProducts searchProductsFailed = new SearchProducts("Somehow Products by Category Request Failed!\nPlease try again later!", 1000, null);
                    productsByCategoryMutableLiveData.setValue(searchProductsFailed);
                }
            });
            return true;
        }
        return false;
    }
}
