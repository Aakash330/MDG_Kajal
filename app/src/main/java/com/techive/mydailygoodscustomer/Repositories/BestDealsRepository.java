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

public class BestDealsRepository {
    private static final String TAG = "BestDealsRepository";

    private static BestDealsRepository bestDealsRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<ProductsModel> bestDealsProductsModelMutableLiveData;

    public BestDealsRepository() {
        Log.i(TAG, "BestDealsRepository: Empty Constructor fired!");

        initBestDealsRepository();
    }

    public static BestDealsRepository getBestDealsRepositoryInstance(Context context) {
        Log.i(TAG, "getBestDealsRepositoryInstance: fired!");
        if (bestDealsRepositoryInstance == null) {
            Log.i(TAG, "getBestDealsRepositoryInstance: Best Deals Repository initialized now!");
            applicationContext = context;
            bestDealsRepositoryInstance = new BestDealsRepository();
        }
        return bestDealsRepositoryInstance;
    }

    /*public*/private void initBestDealsRepository() {
        Log.i(TAG, "initBestDealsRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        bestDealsProductsModelMutableLiveData = new MutableLiveData<>();
    }

    /* GETTERS - START */
    public MutableLiveData<ProductsModel> getBestDealsProductsModelMutableLiveData() {
        return bestDealsProductsModelMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setBestDealsProductsModelMutableLiveData(ProductsModel bestDealsProducts) {
        bestDealsProductsModelMutableLiveData.setValue(bestDealsProducts);
    }
    /* SETTERS - END */

    public boolean getBestDealsProducts(int vendorId, int pageNumber) {
        Log.i(TAG, "getBestDealsProducts: vendorId: " + vendorId + "\tpageNumber: " + pageNumber);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getBestDealsProducts: Network Available!");

            Call<ProductsModel> getBestDealsProductCall = ApplicationData.mdg_customerAPI_interface.getBestDealProducts(vendorId, pageNumber);
            getBestDealsProductCall.enqueue(new Callback<ProductsModel>() {
                @Override
                public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                    Log.i(TAG, "onResponse: BEST DEALS PRODUCT Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        ProductsModel productsModelUnsuccessful = new ProductsModel("Somehow Server didn't respond!" +
                                "Please try again later!", null, -1, -1, response.code());
                        bestDealsProductsModelMutableLiveData.setValue(productsModelUnsuccessful);
                        return;
                    }
                    ProductsModel productsModel = response.body();
                    Log.i(TAG, "onResponse: BEST DEAL PRODUCTS, productsModel: " + productsModel);
                    bestDealsProductsModelMutableLiveData.setValue(productsModel);
                }

                @Override
                public void onFailure(Call<ProductsModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: BEST DEALS PRODUCT Response somehow failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ProductsModel productsModelFailedSocketTimeout = new ProductsModel("Weak Network Connection. Request Failed!\nPlease try again later!", null, -1, -1, 1001);
                        bestDealsProductsModelMutableLiveData.setValue(productsModelFailedSocketTimeout);
                        return;
                    }

                    ProductsModel productsModelFailed = new ProductsModel("Somehow Best Deals Product Response Failed!", null, -1, -1, 1000);
                    bestDealsProductsModelMutableLiveData.setValue(productsModelFailed);
                }
            });
            return true;
        }
        return false;
    }
}
