package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.BuyerAddAddressModel;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.CityList_Data;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.Models.StateList_Data;
import com.techive.mydailygoodscustomer.Repositories.DeliveryAddressRepository;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DeliveryAddressViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveryAddressViewMode";

    private DeliveryAddressRepository deliveryAddressRepository;

    private MutableLiveData<GeneralResponse> addDeliveryAddressMutableLiveData;
    private MutableLiveData<CityList> cityListMutableLiveData;
    private MutableLiveData<StateList> stateListMutableLiveData;
    private MutableLiveData<GeneralResponse> updateDeliveryAddressMutableLiveData;

    public ArrayAdapter<String> cityArrayAdapter, statesArrayAdapter;

    public HashMap<String, Integer> stateHashMap, cityHashMap;

    public int selectedStateId;

    public DeliveryAddressViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "DeliveryAddressViewModel: fired!");

        initDeliveryAddressViewModel();
    }

    private void initDeliveryAddressViewModel() {
        Log.i(TAG, "initDeliveryAddressViewModel: fired!");

        if (deliveryAddressRepository != null) {
            Log.i(TAG, "initDeliveryAddressViewModel: DeliveryAddressRepository already initialized in DeliveryAddressViewModel!");
            return;
        }
        deliveryAddressRepository = DeliveryAddressRepository.getDeliveryAddressRepository(getApplication().getApplicationContext());
        deliveryAddressRepository.initDeliveryAddressRepository();

        addDeliveryAddressMutableLiveData = deliveryAddressRepository.getAddDeliveryAddressMutableLiveData();
        cityListMutableLiveData = deliveryAddressRepository.getCityListMutableLiveData();
        stateListMutableLiveData = deliveryAddressRepository.getStateListMutableLiveData();
        updateDeliveryAddressMutableLiveData = deliveryAddressRepository.getUpdateDeliveryAddressMutableLiveData();

        cityArrayAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(), android.R.layout.simple_list_item_1);
        statesArrayAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(), android.R.layout.simple_list_item_1);

        stateHashMap = new HashMap<>();
        cityHashMap = new HashMap<>();
    }

    /* GETTERS - START */
    public MutableLiveData<GeneralResponse> getAddDeliveryAddressMutableLiveData() {
        return addDeliveryAddressMutableLiveData;
    }

    public MutableLiveData<CityList> getCityListMutableLiveData() {
        return cityListMutableLiveData;
    }

    public MutableLiveData<StateList> getStateListMutableLiveData() {
        return stateListMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getUpdateDeliveryAddressMutableLiveData() {
        return updateDeliveryAddressMutableLiveData;
    }
    /* GETTERS - END */

    public void initStateDataInMaterialAutoCompleteTextView() {
        Log.i(TAG, "initStateDataInMaterialAutoCompleteTextView: fired!");

        Log.i(TAG, "initStateDataInMaterialAutoCompleteTextView: BEFORE statesArrayAdapter.getCount(): " + statesArrayAdapter.getCount());
        statesArrayAdapter.clear();
        stateHashMap.clear();
        Log.i(TAG, "initStateDataInMaterialAutoCompleteTextView: BEFORE DATA stateHashMap.toString(): " + stateHashMap.toString());

        for (StateList_Data stateList_data : stateListMutableLiveData.getValue().getData()) {
            statesArrayAdapter.add(stateList_data.getState());
            stateHashMap.put(stateList_data.getState(), stateList_data.getId());
        }
        Log.i(TAG, "initStateDataInMaterialAutoCompleteTextView: AFTER DATA stateHashMap.toString(): " + stateHashMap.toString());
        Log.i(TAG, "initStateDataInMaterialAutoCompleteTextView: AFTER statesArrayAdapter.getCount(): " + statesArrayAdapter.getCount());
        statesArrayAdapter.notifyDataSetChanged();
    }

    public void initCityDataInMaterialAutoCompleteTextView() {
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: fired!");

        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: BEFORE cityArrayAdapter.getCount(): " + cityArrayAdapter.getCount());
        cityArrayAdapter.clear();
        cityHashMap.clear();
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: BEFORE DATA cityHashMap.toString(): " + cityHashMap.toString());

        for (CityList_Data cityList_data : cityListMutableLiveData.getValue().getData()) {
            cityArrayAdapter.add(cityList_data.getCity());
            cityHashMap.put(cityList_data.getCity(), cityList_data.getId());
        }
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: AFTER DATA cityHashMap.toString(): " + cityHashMap.toString());
        Log.i(TAG, "initCityDataInMaterialAutoCompleteTextView: AFTER cityArrayAdapter.getCount(): " + cityArrayAdapter.getCount());
        cityArrayAdapter.notifyDataSetChanged();
    }

    public boolean addDeliveryAddress(BuyerAddAddressModel buyerAddAddressModel) {
        Log.i(TAG, "addDeliveryAddress: fired!");

        return deliveryAddressRepository.addDeliveryAddress(buyerAddAddressModel);
    }

    public boolean getCityListByCityNameSegment(String cityNameSegment, int stateId) {
        Log.i(TAG, "getCityListByCityNameSegment: fired!");

        return deliveryAddressRepository.getCityListByCityNameSegment(cityNameSegment, stateId);
    }

    public boolean getStateListByStateNameSegment(String stateNameSegment) {
        Log.i(TAG, "getStateListByStateNameSegment: fired!");

        return deliveryAddressRepository.getStateListByStateNameSegment(stateNameSegment);
    }

    public boolean updateDeliveryAddress(BuyerAddAddressModel buyerAddAddressModel) {
        Log.i(TAG, "updateDeliveryAddress: fired!");

        return deliveryAddressRepository.updateDeliveryAddress(buyerAddAddressModel);
    }
}
