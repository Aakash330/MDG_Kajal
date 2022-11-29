package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.LocationResult;
import com.techive.mydailygoodscustomer.Adapters.StoresListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.CityList_Data;
import com.techive.mydailygoodscustomer.Models.RateStoreModel;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName;
import com.techive.mydailygoodscustomer.Models.StoreListByName;
import com.techive.mydailygoodscustomer.Models.StoreListByName_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Repositories.AccountRepository;
import com.techive.mydailygoodscustomer.Repositories.StoreListRepository;
import com.techive.mydailygoodscustomer.Util.LocationFetcher;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class StoreLocatorViewModel extends AndroidViewModel {
    private static final String TAG = "StoreLocatorViewModel";

    private Application application;

    private StoreListRepository storeListRepository;
    private AccountRepository accountRepository;

    public LocationFetcher locationFetcher;

    private MutableLiveData<LocationResult> locationResultMutableLiveData;
    private MutableLiveData<CityList> cityListMutableLiveData;
    private MutableLiveData<StoreListByName> storeListByNameMutableLiveData;
    private MutableLiveData<StoreListByCityName> storeListByCityNameMutableLiveData;
    private MutableLiveData<GeneralResponse> setFavStoreMutableLiveData;
    //    private MutableLiveData<ViewShopRating> viewShopRatingMutableLiveData;
    private MutableLiveData<GeneralResponse> storeRatingResponseMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;

    private MutableLiveData<GeneralResponse> logoutResponseMutableLiveData;

    public ArrayAdapter<String> cityArrayAdapter, storeNamesArrayAdapter;

    public HashMap<String, Integer> cityHashMap, storesHashMap;

    public StoresListRecyclerViewAdapter storesListRecyclerViewAdapter;

//    public String selectedStoreName = "";

    public int toBeDefaultStorePosition, lastCheckedPosition, toBeNotifiedStoreReviewPosition, toBeOpenedStoreId;
    public String toBeOpenedStoreName;

    public RateStoreModel rateStoreModel;

    public StoreLocatorViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.application = application;

        initStoreLocatorViewModel();
    }

    private void initStoreLocatorViewModel() {
        Log.i(TAG, "initStoreLocatorViewModel: fired!");

        if (accountRepository != null) {
            Log.i(TAG, "initStoreLocatorViewModel: Account Repository already initialized in StoreLocatorViewModel!");
        } else {
            accountRepository = AccountRepository.getAccountRepository(getApplication().getApplicationContext());

            logoutResponseMutableLiveData = accountRepository.getLogoutResponseMutableLiveData();
        }

        if (storeListRepository != null) {
            Log.i(TAG, "initStoreLocatorViewModel: storeListRepository already initialized!");
            return;
        }
        storeListRepository = StoreListRepository.getStoreListRepositoryInstance(application.getApplicationContext());
//        storeListRepository.initStoreListRepository();

        locationFetcher = new LocationFetcher(application.getApplicationContext());

        locationResultMutableLiveData = locationFetcher.getLocationResultMutableLiveData();
        cityListMutableLiveData = storeListRepository.getCityListMutableLiveData();
        storeListByNameMutableLiveData = storeListRepository.getStoreListByNameMutableLiveData();
        storeListByCityNameMutableLiveData = storeListRepository.getStoreListByCityNameMutableLiveData();
        setFavStoreMutableLiveData = storeListRepository.getSetFavStoreMutableLiveData();
//        viewShopRatingMutableLiveData = storeListRepository.getViewShopRatingMutableLiveData();
        storeRatingResponseMutableLiveData = storeListRepository.getStoreRatingResponseMutableLiveData();
        orderAcceptResponseMutableLiveData = storeListRepository.getOrderAcceptResponseMutableLiveData();

        cityArrayAdapter = new ArrayAdapter<>(application.getApplicationContext(), R.layout.layout_list_item);
//        storeNamesArrayAdapter = new ArrayAdapter<>(application.getApplicationContext(), android.R.layout.simple_list_item_1);
        storeNamesArrayAdapter = new ArrayAdapter<>(application.getApplicationContext(), R.layout.layout_list_item);

        cityHashMap = new HashMap<>();
        storesHashMap = new HashMap<>();

        //WILL USE THE SAME ADAPTER, INCLUDING A PARAMETER TO IDENTIFY FROM WHERE IS IT BEING USED.
        storesListRecyclerViewAdapter = new StoresListRecyclerViewAdapter(application.getApplicationContext(), true);
    }

    //NO LONGER NEEDED! WILL REMOVE SOON!
    /*Looks promising, but kind of unstable & unpredictable.*/
//    public void setStoreListRepository() {
//        Log.i(TAG, "setStoreListRepository: fired!");
//        storeListRepository = StoreListRepository.getStoreListRepositoryInstance(application.getApplicationContext());
//    }

    /* GETTERS - START */
    public MutableLiveData<LocationResult> getLocationResultMutableLiveData() {
        return locationResultMutableLiveData;
    }

    public MutableLiveData<CityList> getCityListMutableLiveData() {
        return cityListMutableLiveData;
    }

    public MutableLiveData<StoreListByName> getStoreListByNameMutableLiveData() {
        return storeListByNameMutableLiveData;
    }

    public MutableLiveData<StoreListByCityName> getStoreListByCityNameMutableLiveData() {
        return storeListByCityNameMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getSetFavStoreMutableLiveData() {
        return setFavStoreMutableLiveData;
    }

//    public MutableLiveData<ViewShopRating> getViewShopRatingMutableLiveData() {
//        return viewShopRatingMutableLiveData;
//    }

    public MutableLiveData<GeneralResponse> getStoreRatingResponseMutableLiveData() {
        return storeRatingResponseMutableLiveData;
    }

    public MutableLiveData<OrderAcceptResponse> getOrderAcceptResponseMutableLiveData() {
        return orderAcceptResponseMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getLogoutResponseMutableLiveData() {
        return logoutResponseMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setOrderAcceptResponseMutableLiveDataValue(OrderAcceptResponse orderAcceptResponse) {
        Log.i(TAG, "setOrderAcceptResponseMutableLiveDataValue: orderAcceptResponse: " + orderAcceptResponse);
        storeListRepository.setOrderAcceptResponseMutableLiveDataValue(orderAcceptResponse);
    }

    public void setStoreListByCityNameMutableLiveDataValue(StoreListByCityName storeListByCityName) {
        Log.i(TAG, "setStoreListByCityNameMutableLiveDataValue: storeListByCityName: " + storeListByCityName);
        storeListRepository.setStoreListByCityNameMutableLiveDataValue(storeListByCityName);
    }

//    public void setLocationResultMutableLiveData(MutableLiveData<LocationResult> locationResultMutableLiveData) {
//        this.locationResultMutableLiveData = locationResultMutableLiveData;
//    }

    public void setCityListMutableLiveDataValue(CityList cityList) {
        storeListRepository.setCityListMutableLiveDataValue(cityList);
    }

    public void setStoreListByNameMutableLiveDataValue(StoreListByName storeListByName) {
        storeListRepository.setStoreListByNameMutableLiveDataValue(storeListByName);

    }

    public void setSetFavStoreMutableLiveDataValue(GeneralResponse setFavStore) {
        storeListRepository.setSetFavStoreMutableLiveDataValue(setFavStore);
    }

    public void setStoreRatingResponseMutableLiveDataValue(GeneralResponse storeRatingResponse) {
        storeListRepository.setStoreRatingResponseMutableLiveDataValue(storeRatingResponse);
    }

    public void setLogoutResponseMutableLiveData(GeneralResponse logoutResponse) {
        accountRepository.setLogoutResponseMutableLiveData(logoutResponse);
    }
    /* SETTERS - END */

    public boolean fetchLocation() {
        Log.i(TAG, "fetchLocation: fired!");

        return locationFetcher.fetchLocation();
    }

    public void initCityDataInMaterialAutoCompleteTextView() {
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: fired!");

        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: BEFORE cityArrayAdapter.getCount(): " + cityArrayAdapter.getCount());
        cityArrayAdapter.clear();
        cityHashMap.clear();

        for (CityList_Data cityList_data : cityListMutableLiveData.getValue().getData()) {
            cityArrayAdapter.add(cityList_data.getCity());
            cityHashMap.put(cityList_data.getCity(), cityList_data.getId());
        }
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: AFTER cityArrayAdapter.getCount(): " + cityArrayAdapter.getCount());
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: cityHashMap.toString(): " + cityHashMap.toString());
        cityArrayAdapter.notifyDataSetChanged();
    }

    public void initStoreNameDataInMaterialAutoCompleteTextView() {
        Log.i(TAG, "initStoreNameDataInMaterialAutoCompleteTextView: fired!");

        Log.i(TAG, "initStoreNameDataInMaterialAutoCompleteTextView: BEFORE storeNamesArrayAdapter.getCount(): " + storeNamesArrayAdapter.getCount());
        storeNamesArrayAdapter.clear();
        storesHashMap.clear();

        for (StoreListByName_Data storeListByName_data : storeListByNameMutableLiveData.getValue().getData()) {
            storeNamesArrayAdapter.add(storeListByName_data.getStore_name());
            storesHashMap.put(storeListByName_data.getStore_name(), storeListByName_data.getStore_id());
        }
        Log.i(TAG, "initStoreNameDataInMaterialAutoCompleteTextView: storesHashMap.toString(): " + storesHashMap.toString());
        Log.i(TAG, "initStoreNameDataInMaterialAutoCompleteTextView: AFTER storeNamesArrayAdapter.getCount(): " + storeNamesArrayAdapter.getCount());
        storeNamesArrayAdapter.notifyDataSetChanged();
    }

    public boolean getCityListByCityNameSegment(String cityNameSegment) {
        Log.i(TAG, "getCityListByCityNameSegment: fired!");

        return storeListRepository.getCityListByCityNameSegment(cityNameSegment);
    }

    public boolean getStoreListByStoreNameSegment(String storeNameSegment) {
        Log.i(TAG, "getStoreListByStoreName: fired!");

        return storeListRepository.getStoreListByStoreNameSegment(storeNameSegment);
    }

    public boolean getFavStoresList(int buyerId) {
        Log.i(TAG, "getFavStoresList: fired!");

        return storeListRepository.getFavStoresList(buyerId);
    }

    public boolean setFavStore(int buyerId, int vendorId) {
        Log.i(TAG, "setFavStore: fired!");

        return storeListRepository.setFavStore(buyerId, vendorId);
    }

//    public boolean viewAllRatingsOfShop(int storeId) {
//        Log.i(TAG, "viewAllRatingsOfShop: fired!");
//
//        return storeListRepository.viewAllRatingsOfShop(storeId);
//    }

    public boolean rateStore(RateStoreModel rateStoreModel) {
        Log.i(TAG, "rateStore: fired!");

        return storeListRepository.rateStore(rateStoreModel);
    }

//    public boolean checkVendorOrderAcceptance() {
//        Log.i(TAG, "checkVendorOrderAcceptance: fired!");
//
//        return storeListRepository.checkVendorOrderAcceptance(toBeOpenedStoreId);
//    }

    public boolean logoutCustomer() {
        Log.i(TAG, "logoutCustomer: fired!");

        return accountRepository.logoutCustomer();
    }
}
