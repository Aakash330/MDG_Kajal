package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegularListRepository {
    private static final String TAG = "RegularListRepository";

    private static RegularListRepository regularListRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<ProductsModel> regularListMutableLiveData;
    private MutableLiveData<HomeCart> regularCartMutableLiveData;

    public RegularListRepository() {
        Log.i(TAG, "RegularListRepository: Empty constructor fired!");

        initRegularListRepository();
    }

    public static RegularListRepository getRegularListRepositoryInstance(Context context) {
        Log.i(TAG, "getRegularListRepositoryInstance: fired!");

        if (regularListRepositoryInstance == null) {
            applicationContext = context;
            regularListRepositoryInstance = new RegularListRepository();
            Log.i(TAG, "getRegularListRepositoryInstance: RegularList Repository Initialized now!");
        }
        return regularListRepositoryInstance;
    }

    /*public*/
    private void initRegularListRepository() {
        Log.i(TAG, "initRegularListRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        regularListMutableLiveData = new MutableLiveData<>();
        regularCartMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ProductsModel> getRegularListMutableLiveData() {
        return regularListMutableLiveData;
    }

    public MutableLiveData<HomeCart> getRegularCartMutableLiveData() {
        return regularCartMutableLiveData;
    }

    /* SETTERS - START */
    public void setRegularListMutableLiveData(ProductsModel regularList) {
        regularListMutableLiveData.setValue(regularList);
    }

    public void setRegularCartMutableLiveData(HomeCart regularCart) {
        regularCartMutableLiveData.setValue(regularCart);
    }
    /* SETTERS - END */

    public boolean getBuyerRegularList(int buyerId, int page) {
        Log.i(TAG, "getBuyerRegularList: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getBuyerRegularList: Network Available!");

            Call<ProductsModel> regularListCall = ApplicationData.mdg_customerAPI_interface.getBuyerRegularList(
                    buyerId, ApplicationData.getDefaultStoreId(), page);
            regularListCall.enqueue(new Callback<ProductsModel>() {
                @Override
                public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                    Log.i(TAG, "onResponse: REGULAR LIST Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        ProductsModel productsModelUnsuccessful = new ProductsModel("Somehow server didn't respond!", null, -1, -1, response.code());
                        regularListMutableLiveData.setValue(productsModelUnsuccessful);
                        return;
                    }
                    ProductsModel productsModel = response.body();
                    Log.i(TAG, "onResponse: REGULAR LIST, productsModel: " + productsModel);
                    regularListMutableLiveData.setValue(productsModel);
                }

                @Override
                public void onFailure(Call<ProductsModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: REGULAR LIST Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ProductsModel productsModelFailedSocketTimeout = new ProductsModel(applicationContext.getString(R.string.weak_internet_connection), null, -1, -1, 1001);
                        regularListMutableLiveData.setValue(productsModelFailedSocketTimeout);
                        return;
                    }
                    ProductsModel productsModelFailed = new ProductsModel("Somehow Regular List Request Failed!", null, -1, -1, 1000);
                    regularListMutableLiveData.setValue(productsModelFailed);
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
                        regularCartMutableLiveData.setValue(homeCartUnsuccessful);
                        return;
                    }
                    HomeCart homeCart = response.body();
                    Log.i(TAG, "onResponse: homeCart: " + homeCart);
                    regularCartMutableLiveData.setValue(homeCart);
                }

                @Override
                public void onFailure(Call<HomeCart> call, Throwable t) {
                    Log.i(TAG, "onFailure: HOME CART Response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        HomeCart homeCartFailedSocketTimeout = new HomeCart(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        regularCartMutableLiveData.setValue(homeCartFailedSocketTimeout);
                        return;
                    }

                    HomeCart homeCartFailed = new HomeCart("Server Request Failed.\nPlease try again later!", null, 1000);
                    regularCartMutableLiveData.setValue(homeCartFailed);
                }
            });
            return true;
        }
        return false;
    }

}
