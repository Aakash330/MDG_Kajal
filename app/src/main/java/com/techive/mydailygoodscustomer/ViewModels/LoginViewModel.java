package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.LoginResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.CityList_Data;
import com.techive.mydailygoodscustomer.Models.LoginModel;
import com.techive.mydailygoodscustomer.Models.RegisterModel;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.Models.StateList_Data;
import com.techive.mydailygoodscustomer.Repositories.LoginRepository;
import com.techive.mydailygoodscustomer.Util.LocationFetcher;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModel";

    private LoginRepository loginRepository;

    private LocationFetcher locationFetcher;

    public LoginModel loginModel;
    public RegisterModel registerModel;

    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<CityList> cityListMutableLiveData;
    private MutableLiveData<StateList> stateListMutableLiveData;
    private MutableLiveData<OTPResponse> otpResponseMutableLiveData;
    private MutableLiveData<GeneralResponse> userValidityResponseMutableLiveData;

    public ArrayAdapter<String> cityArrayAdapter, statesArrayAdapter;

    public HashMap<String, Integer> stateHashMap, cityHashMap;

    public boolean loginInitiated = false, registerInitiated = false, otpVerified = false;

    public int OTPSent, selectedStateId;

    public String OPTSentOnNumber = "";

    public LoginViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "LoginViewModel: fired!");

        initLoginViewModel();
    }

    private void initLoginViewModel() {
        Log.i(TAG, "initLoginViewModel: fired!");

        if (loginRepository != null) {
            Log.i(TAG, "initLoginViewModel: Login Repository already initialized!");
            return;
        }
        loginRepository = LoginRepository.getLoginRepositoryInstance(getApplication().getApplicationContext());
        loginRepository.initializeLoginRepository();

        locationFetcher = new LocationFetcher(getApplication().getApplicationContext());

        loginResponseMutableLiveData = loginRepository.getLoginResponseMutableLiveData();
        cityListMutableLiveData = loginRepository.getCityListMutableLiveData();
        stateListMutableLiveData = loginRepository.getStateListMutableLiveData();
        otpResponseMutableLiveData = loginRepository.getOtpResponseMutableLiveData();
        userValidityResponseMutableLiveData = loginRepository.getUserValidityResponseMutableLiveData();

        cityArrayAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(), android.R.layout.simple_list_item_1);
        statesArrayAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(), android.R.layout.simple_list_item_1);

        stateHashMap = new HashMap<>();
        cityHashMap = new HashMap<>();
    }

    /* GETTERS - START */
    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData() {
        return loginResponseMutableLiveData;
    }

    public MutableLiveData<CityList> getCityListMutableLiveData() {
        return cityListMutableLiveData;
    }

    public MutableLiveData<StateList> getStateListMutableLiveData() {
        return stateListMutableLiveData;
    }

    public MutableLiveData<OTPResponse> getOtpResponseMutableLiveData() {
        return otpResponseMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getUserValidityResponseMutableLiveData() {
        return userValidityResponseMutableLiveData;
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

    public void getAddressFromLocationName(String locationName, final Handler handler) {
        Log.i(TAG, "getAddressFromLocationName: fired!");

        locationFetcher.getAddressFromLocationName(locationName, handler);
    }

//    public ArrayList<String> getLocationInfo(LocationResult locationResult) {
//        Log.i(TAG, "getLocationInfo: fired!");
//
//        return locationFetcher.getLocationInfo(locationResult);
//    }

    public boolean login(LoginModel loginModel) {
        Log.i(TAG, "login: fired!");

        loginInitiated = true;
        registerInitiated = false;
        this.loginModel = loginModel;
        return loginRepository.login(loginModel);
    }

    public boolean register(RegisterModel registerModel) {
        Log.i(TAG, "register: fired!");

        loginInitiated = false;
        registerInitiated = true;
        this.registerModel = registerModel;
        return loginRepository.register(registerModel);
    }

    public boolean getCityListByCityNameSegment(String cityNameSegment, int stateId) {
        Log.i(TAG, "getCityListByCityNameSegment: fired!");

        return loginRepository.getCityListByCityNameSegment(cityNameSegment, stateId);
    }

    public boolean getStateListByStateNameSegment(String stateNameSegment) {
        Log.i(TAG, "getStateListByStateNameSegment: fired!");

        return loginRepository.getStateListByStateNameSegment(stateNameSegment);
    }

    public boolean sendOTP(int random5DigitNo, long numbers) {
        Log.i(TAG, "sendOTP: fired!");

        return loginRepository.sendOTP(random5DigitNo, numbers);
    }

    public boolean userValidityCheck(String email, String mobNo) {
        Log.i(TAG, "userValidityCheck: fired!");

        return loginRepository.userValidityCheck(email, mobNo);
    }

}
