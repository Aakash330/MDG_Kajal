package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.BuyerAddAddressModel;
import com.techive.mydailygoodscustomer.Models.BuyerAllDeliveryAddress;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddressRepository {
    private static final String TAG = "DeliveryAddressReposito";

    private static DeliveryAddressRepository deliveryAddressRepository;

    private static Context applicationContext;

    private MutableLiveData<GeneralResponse> addDeliveryAddressMutableLiveData;
    private MutableLiveData<CityList> cityListMutableLiveData;
    private MutableLiveData<StateList> stateListMutableLiveData;
    private MutableLiveData<BuyerAllDeliveryAddress> buyerAllDeliveryAddressMutableLiveData;
    private MutableLiveData<GeneralResponse> setDefaultDeliveryAddressMutableLiveData;
    private MutableLiveData<GeneralResponse> updateDeliveryAddressMutableLiveData;

    public static DeliveryAddressRepository getDeliveryAddressRepository(Context context) {
        Log.i(TAG, "getDeliveryAddressRepository: fired!");

        if (deliveryAddressRepository == null) {
            Log.i(TAG, "getDeliveryAddressRepository: DeliveryAddressRepository initialized now!");
            deliveryAddressRepository = new DeliveryAddressRepository();
            applicationContext = context;
        }
        return deliveryAddressRepository;
    }

    public void initDeliveryAddressRepository() {
        Log.i(TAG, "initDeliveryAddressRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        addDeliveryAddressMutableLiveData = new MutableLiveData<>();
        cityListMutableLiveData = new MutableLiveData<>();
        stateListMutableLiveData = new MutableLiveData<>();
        buyerAllDeliveryAddressMutableLiveData = new MutableLiveData<>();
        setDefaultDeliveryAddressMutableLiveData = new MutableLiveData<>();
        updateDeliveryAddressMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<GeneralResponse> getAddDeliveryAddressMutableLiveData() {
        return addDeliveryAddressMutableLiveData;
    }

    public MutableLiveData<CityList> getCityListMutableLiveData() {
        return cityListMutableLiveData;
    }

    public MutableLiveData<StateList> getStateListMutableLiveData() {
        return stateListMutableLiveData;
    }

    public MutableLiveData<BuyerAllDeliveryAddress> getBuyerAllDeliveryAddressMutableLiveData() {
        return buyerAllDeliveryAddressMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getSetDefaultDeliveryAddressMutableLiveData() {
        return setDefaultDeliveryAddressMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getUpdateDeliveryAddressMutableLiveData() {
        return updateDeliveryAddressMutableLiveData;
    }

    public boolean addDeliveryAddress(BuyerAddAddressModel buyerAddAddressModel) {
        Log.i(TAG, "addDeliveryAddress: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "addDeliveryAddress: Network Available!");

            Call<GeneralResponse> addBuyerDeliveryAddressCall = ApplicationData.mdg_customerAPI_interface.addBuyerDeliveryAddress(buyerAddAddressModel);
            addBuyerDeliveryAddressCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: ADD BUYER DELIVERY ADDRESS Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!", "", response.code());
                        addDeliveryAddressMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: ADD BUYER DELIVERY ADDRESS, generalResponse: " + generalResponse);
                    addDeliveryAddressMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: ADD BUYER DELIVERY ADDRESS Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        addDeliveryAddressMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Add Buyer Delivery Address Request Failed!\nPlease try again later!", "", 1000);
                    addDeliveryAddressMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getBuyerDeliveryAddress() {
        Log.i(TAG, "getBuyerDeliveryAddress: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getBuyerDeliveryAddress: Network Available!");

            return true;
        }
        return false;
    }

    public boolean getCityListByCityNameSegment(String cityNameSegment, int stateId) {
        Log.i(TAG, "getCityListByCityNameSegment: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getCityListByCityNameSegment: Network Available!");

            Call<CityList> cityListCall = ApplicationData.mdg_customerAPI_interface.getCityListByCityNameSegment(
                    cityNameSegment, stateId);
            cityListCall.enqueue(new Callback<CityList>() {
                @Override
                public void onResponse(Call<CityList> call, Response<CityList> response) {
                    Log.i(TAG, "onResponse: CITY LIST Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        CityList cityListUnsuccessful = new CityList(response.code(), "Somehow server didn't respond.\nPlease try again later!", null);
                        cityListMutableLiveData.setValue(cityListUnsuccessful);
                        return;
                    }
                    CityList cityList = response.body();
                    Log.i(TAG, "onResponse: cityList: " + cityList);
                    cityListMutableLiveData.setValue(cityList);
                }

                @Override
                public void onFailure(Call<CityList> call, Throwable t) {
                    Log.i(TAG, "onFailure: CITY LIST Response Somehow failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    CityList cityListFailed = new CityList(1000, "Somehow CityList response failed!\nPlease try again.", null);
                    cityListMutableLiveData.setValue(cityListFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getStateListByStateNameSegment(String stateNameSegment) {
        Log.i(TAG, "getStateListByStateNameSegment: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getStateListByStateNameSegment: Network Available!");

            Call<StateList> stateListCall = ApplicationData.mdg_customerAPI_interface.getStateListByStateNameSegment(stateNameSegment);
            stateListCall.enqueue(new Callback<StateList>() {
                @Override
                public void onResponse(Call<StateList> call, Response<StateList> response) {
                    Log.i(TAG, "onResponse: STATE LIST Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        StateList stateListUnsuccessful = new StateList(response.code(), "Somehow server didn't respond.\nPlease try again later!", null);
                        stateListMutableLiveData.setValue(stateListUnsuccessful);
                        return;
                    }
                    StateList stateList = response.body();
                    Log.i(TAG, "onResponse: stateList: " + stateList);
                    stateListMutableLiveData.setValue(stateList);
                }

                @Override
                public void onFailure(Call<StateList> call, Throwable t) {
                    Log.i(TAG, "onFailure: STATE LIST Response Somehow failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    StateList stateListFailed = new StateList(1000, "Somehow StateList response failed!\nPlease try again.", null);
                    stateListMutableLiveData.setValue(stateListFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getBuyerAllDeliveryAddress() {
        Log.i(TAG, "getBuyerAllDeliveryAddress: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getStateListByStateNameSegment: Network Available!");

            Call<BuyerAllDeliveryAddress> buyerAllDeliveryAddressCall = ApplicationData.mdg_customerAPI_interface.getBuyerAllDeliveryAddress(ApplicationData.getLoggedInBuyerId());
            buyerAllDeliveryAddressCall.enqueue(new Callback<BuyerAllDeliveryAddress>() {
                @Override
                public void onResponse(Call<BuyerAllDeliveryAddress> call, Response<BuyerAllDeliveryAddress> response) {
                    Log.i(TAG, "onResponse: DELIVERY ADDRESS LIST Response seems to be successful!");

                    if (!response.isSuccessful()) {
                        BuyerAllDeliveryAddress buyerAllDeliveryAddressUnsuccessful = new BuyerAllDeliveryAddress("Somehow server didn't respond!", null, response.code());
                        buyerAllDeliveryAddressMutableLiveData.setValue(buyerAllDeliveryAddressUnsuccessful);
                        return;
                    }
                    BuyerAllDeliveryAddress buyerAllDeliveryAddress = response.body();
                    Log.i(TAG, "onResponse: buyerAllDeliveryAddress: " + buyerAllDeliveryAddress);
                    buyerAllDeliveryAddressMutableLiveData.setValue(buyerAllDeliveryAddress);
                }

                @Override
                public void onFailure(Call<BuyerAllDeliveryAddress> call, Throwable t) {
                    Log.i(TAG, "onFailure: DELIVERY ADDRESS LIST Request somehow failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        BuyerAllDeliveryAddress buyerAllDeliveryAddressFailedSocketTimeout = new BuyerAllDeliveryAddress(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        buyerAllDeliveryAddressMutableLiveData.setValue(buyerAllDeliveryAddressFailedSocketTimeout);
                        return;
                    }
                    BuyerAllDeliveryAddress buyerAllDeliveryAddressFailed = new BuyerAllDeliveryAddress("Somehow Buyer Delivery Address List Request failed!", null, 1000);
                    buyerAllDeliveryAddressMutableLiveData.setValue(buyerAllDeliveryAddressFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean setDefaultDeliveryAddress(int addressId) {
        Log.i(TAG, "setDefaultDeliveryAddress: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getStateListByStateNameSegment: Network Available!");

            Call<GeneralResponse> setDefaultDeliveryAddressCall = ApplicationData.mdg_customerAPI_interface.setDefaultBuyerAddress(ApplicationData.getLoggedInBuyerId(), addressId);
            setDefaultDeliveryAddressCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: SET DEFAULT DELIVERY ADDRESS Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!\nPlease try again later!", "", response.code());
                        setDefaultDeliveryAddressMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: SET DEFAULT DELIVERY ADDRESS: generalResponse: " + generalResponse);
                    setDefaultDeliveryAddressMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: SET DEFAULT DELIVERY ADDRESS Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        setDefaultDeliveryAddressMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Set Default Delivery Address Request failed!", "", 1000);
                    setDefaultDeliveryAddressMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean updateDeliveryAddress(BuyerAddAddressModel buyerAddAddressModel) {
        Log.i(TAG, "updateDeliveryAddress: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "updateDeliveryAddress: Network Available!");

            Call<GeneralResponse> updateBuyerDeliveryAddressCall = ApplicationData.mdg_customerAPI_interface.updateBuyerDeliveryAddress(buyerAddAddressModel);
            updateBuyerDeliveryAddressCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: UPDATE BUYER DELIVERY ADDRESS Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!", "", response.code());
                        updateDeliveryAddressMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: UPDATE BUYER DELIVERY ADDRESS, generalResponse: " + generalResponse);
                    updateDeliveryAddressMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: UPDATE BUYER DELIVERY ADDRESS Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        updateDeliveryAddressMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Update Buyer Delivery Address Request Failed!\nPlease try again later!", "", 1000);
                    updateDeliveryAddressMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }
}
