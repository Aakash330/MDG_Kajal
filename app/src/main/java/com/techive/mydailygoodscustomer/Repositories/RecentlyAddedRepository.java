package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentlyAddedRepository {
    private static final String TAG = "RecentlyAddedRepository";

    private static RecentlyAddedRepository recentlyAddedRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<ProductsModel> recentlyAddedProductsModelMutableLiveData;

    public RecentlyAddedRepository() {
        Log.i(TAG, "RecentlyAddedRepository: Empty Constructor fired!");

        initRecentlyAddedRepository();
    }

    public static RecentlyAddedRepository getRecentlyAddedRepositoryInstance(Context context) {
        Log.i(TAG, "getRecentlyAddedRepositoryInstance: fired!");
        if (recentlyAddedRepositoryInstance == null) {
            Log.i(TAG, "getRecentlyAddedRepositoryInstance: Recently Added Repository initialized now!");
            applicationContext = context;
            recentlyAddedRepositoryInstance = new RecentlyAddedRepository();
        }
        return recentlyAddedRepositoryInstance;
    }

    /*public*/private void initRecentlyAddedRepository() {
        Log.i(TAG, "initRecentlyAddedRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        recentlyAddedProductsModelMutableLiveData = new MutableLiveData<>();
    }

    /* GETTERS - START */
    public MutableLiveData<ProductsModel> getRecentlyAddedProductsModelMutableLiveData() {
        return recentlyAddedProductsModelMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setRecentlyAddedProductsModelMutableLiveData(ProductsModel recentlyAddedProducts) {
        recentlyAddedProductsModelMutableLiveData.setValue(recentlyAddedProducts);
    }
    /* SETTERS - END */

    public boolean getRecentlyAddedProducts(int vendorId, int pageNumber) {
        Log.i(TAG, "getRecentlyAddedProducts: vendorId: " + vendorId + "\tpageNumber: " + pageNumber);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getRecentlyAddedProducts: Network Available!");

            Call<ProductsModel> getRecentlyAddedProductCall = ApplicationData.mdg_customerAPI_interface.getRecentlyAddedProducts(vendorId, pageNumber);
            getRecentlyAddedProductCall.enqueue(new Callback<ProductsModel>() {
                @Override
                public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                    Log.i(TAG, "onResponse: RECENTLY ADDED PRODUCTS Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        ProductsModel productsModelUnsuccessful = new ProductsModel("Somehow Server didn't respond!" +
                                "Please try again later!", null, -1, -1, response.code());
                        recentlyAddedProductsModelMutableLiveData.setValue(productsModelUnsuccessful);
                        return;
                    }
                    ProductsModel productsModel = response.body();
                    Log.i(TAG, "onResponse: RECENTLY ADDED PRODUCTS, productsModel: " + productsModel);
                    recentlyAddedProductsModelMutableLiveData.setValue(productsModel);
                }

                @Override
                public void onFailure(Call<ProductsModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: RECENTLY ADDED PRODUCT Response somehow failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ProductsModel productsModelFailedSocketTimeout = new ProductsModel("Weak Network Connection. Request Failed!\nPlease try again later!", null, -1, -1, 1001);
                        recentlyAddedProductsModelMutableLiveData.setValue(productsModelFailedSocketTimeout);
                        return;
                    }

                    ProductsModel productsModelFailed = new ProductsModel("Somehow Recently Added Product Request Failed!", null, -1, -1, 1000);
                    recentlyAddedProductsModelMutableLiveData.setValue(productsModelFailed);
                }
            });
            return true;
        }
        return false;
    }
}
