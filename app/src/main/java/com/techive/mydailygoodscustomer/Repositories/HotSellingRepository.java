package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotSellingRepository {
    private static final String TAG = "HotSellingRepository";

    private static HotSellingRepository hotSellingRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<ProductsModel> hotSellingProductsModelMutableLiveData;

    public HotSellingRepository() {
        Log.i(TAG, "HotSellingRepository: Empty Constructor fired!");

        initHotSellingRepository();
    }

    public static HotSellingRepository getHotSellingRepositoryInstance(Context context) {
        Log.i(TAG, "getHotSellingRepositoryInstance: fired!");
        if (hotSellingRepositoryInstance == null) {
            Log.i(TAG, "getHotSellingRepositoryInstance: Hot Selling Repository initialized now!");
            applicationContext = context;
            hotSellingRepositoryInstance = new HotSellingRepository();
        }
        return hotSellingRepositoryInstance;
    }

    private void initHotSellingRepository() {
        Log.i(TAG, "initHotSellingRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        hotSellingProductsModelMutableLiveData = new MutableLiveData<>();
    }

    /* GETTERS - START */
    public MutableLiveData<ProductsModel> getHotSellingProductsModelMutableLiveData() {
        return hotSellingProductsModelMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setHotSellingProductsModelMutableLiveData(ProductsModel bestDealsProducts) {
        hotSellingProductsModelMutableLiveData.setValue(bestDealsProducts);
    }
    /* SETTERS - END */

    public boolean getHotSellingProducts(int vendorId, int pageNumber) {
        Log.i(TAG, "getHotSellingProducts: vendorId: " + vendorId + "\tpageNumber: " + pageNumber);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getHotSellingProducts: Network Available!");

            Call<ProductsModel> getHotSellingProductCall = ApplicationData.mdg_customerAPI_interface.getHotSellingProducts(vendorId, pageNumber);
            getHotSellingProductCall.enqueue(new Callback<ProductsModel>() {
                @Override
                public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                    Log.i(TAG, "onResponse: HOT SELLING PRODUCT Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        ProductsModel productsModelUnsuccessful = new ProductsModel(applicationContext.getString(R.string.server_didnt_respond) + "-" + response.code(), null, -1, -1, response.code());
                        hotSellingProductsModelMutableLiveData.setValue(productsModelUnsuccessful);
                        return;
                    }
                    ProductsModel productsModel = response.body();
                    Log.i(TAG, "onResponse: HOT SELLING PRODUCTS, productsModel: " + productsModel);
                    hotSellingProductsModelMutableLiveData.setValue(productsModel);
                }

                @Override
                public void onFailure(Call<ProductsModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: HOT SELLING PRODUCT Response somehow failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ProductsModel productsModelFailedSocketTimeout = new ProductsModel(applicationContext.getString(R.string.weak_internet_connection), null, -1, -1, 1001);
                        hotSellingProductsModelMutableLiveData.setValue(productsModelFailedSocketTimeout);
                        return;
                    }
                    ProductsModel productsModelFailed = new ProductsModel("Somehow Hot Selling Product Response Failed!", null, -1, -1, 1000);
                    hotSellingProductsModelMutableLiveData.setValue(productsModelFailed);
                }
            });
            return true;
        }
        return false;
    }
}
