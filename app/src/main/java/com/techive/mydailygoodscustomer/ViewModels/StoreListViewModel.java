package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.StoresListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;
import com.techive.mydailygoodscustomer.Repositories.StoreListRepository;

import org.jetbrains.annotations.NotNull;

public class StoreListViewModel extends AndroidViewModel {
    private static final String TAG = "StoreListViewModel";

    private Application application;

    private StoreListRepository storeListRepository;

    private MutableLiveData<StoreListByCityName> storeListByCityNameMutableLiveData;
    private MutableLiveData<ViewShopRating> viewShopRatingMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;

    public StoresListRecyclerViewAdapter storesListRecyclerViewAdapter;

    public int toBeOpenedStoreId;
    public String toBeOpenedStoreName;

    public StoreListViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.application = application;

        initStoreListViewModel();
    }

    private void initStoreListViewModel() {
        Log.i(TAG, "initStoreListViewModel: fired!");

        if (storeListRepository != null) {
            Log.i(TAG, "initStoreListViewModel: StoreListRepository already initialized here!");
            return;
        }
        storeListRepository = StoreListRepository.getStoreListRepositoryInstance(application.getApplicationContext());
//        storeListRepository.initStoreListRepository();

        storesListRecyclerViewAdapter = new StoresListRecyclerViewAdapter(application.getApplicationContext(), false);

        storeListByCityNameMutableLiveData = storeListRepository.getStoreListByCityNameMutableLiveData();
        viewShopRatingMutableLiveData = storeListRepository.getViewShopRatingMutableLiveData();
        orderAcceptResponseMutableLiveData = storeListRepository.getOrderAcceptResponseMutableLiveData();
    }

    /* GETTERS - START */
    public MutableLiveData<StoreListByCityName> getStoreListByCityNameMutableLiveData() {
        return storeListByCityNameMutableLiveData;
    }

    public MutableLiveData<ViewShopRating> getViewShopRatingMutableLiveData() {
        return viewShopRatingMutableLiveData;
    }

    public MutableLiveData<OrderAcceptResponse> getOrderAcceptResponseMutableLiveData() {
        return orderAcceptResponseMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setStoreListByCityNameMutableLiveData(StoreListByCityName storeListByCityName) {
        storeListRepository.setStoreListByCityNameMutableLiveDataValue(storeListByCityName);
    }

    public void setViewShopRatingMutableLiveData(ViewShopRating viewShopRating) {
        storeListRepository.setViewShopRatingMutableLiveDataValue(viewShopRating);
    }

    public void setOrderAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        storeListRepository.setOrderAcceptResponseMutableLiveDataValue(orderAcceptResponse);
    }
    /* SETTERS - END */

    public boolean getStoreListByCityName(String cityName, int cityId) {
        Log.i(TAG, "getStoreListByCityName: fired!");

        return storeListRepository.getStoreListByCityName(cityName, cityId);
    }

    public boolean getStoreListByLatLng(String latitude, String longitude) {
        Log.i(TAG, "getStoreListByLatLng: fired!");

        return storeListRepository.getStoreListByLatLng(latitude, longitude);
    }

    public boolean viewAllRatingsOfShop(int storeId) {
        Log.i(TAG, "viewAllRatingsOfShop: fired!");

        return storeListRepository.viewAllRatingsOfShop(storeId);
    }

//    public boolean checkVendorOrderAcceptance() {
//        Log.i(TAG, "checkVendorOrderAcceptance: fired!");
//
//        return storeListRepository.checkVendorOrderAcceptance(toBeOpenedStoreId);
//    }
}
