package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.RateStoreModel;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName;
import com.techive.mydailygoodscustomer.Models.StoreListByName;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListRepository {
    private static final String TAG = "StoreListRepository";

    private static StoreListRepository storeListRepositoryInstance = null;

    private static Context applicationContext;

    private MutableLiveData<StoreListByName> storeListByNameMutableLiveData;
    private MutableLiveData<CityList> cityListMutableLiveData;
    //USING THE BELOW SAME MUTABLE LIVE DATA FOR BOTH STORE NAMES BY CITY & LAT LNG AND ALSO FOR FAV STORES LIST.
    private MutableLiveData<StoreListByCityName> storeListByCityNameMutableLiveData;
    private MutableLiveData<GeneralResponse> setFavStoreMutableLiveData;
    private MutableLiveData<ViewShopRating> viewShopRatingMutableLiveData;
    private MutableLiveData<GeneralResponse> storeRatingResponseMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;

    public StoreListRepository() {
        Log.i(TAG, "StoreListRepository: Empty Constructor fired!");

        initStoreListRepository();
    }

    public static StoreListRepository getStoreListRepositoryInstance(Context context) {
        Log.i(TAG, "getStoreListRepositoryInstance: fired!");
        if (storeListRepositoryInstance == null) {
            Log.i(TAG, "getStoreListRepositoryInstance: Repository Initialized now!");
            applicationContext = context;
            storeListRepositoryInstance = new StoreListRepository();
        }
        return storeListRepositoryInstance;
    }

    /*public*/
    private void initStoreListRepository() {
        Log.i(TAG, "initStoreListRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);
        ApplicationData.initializeRetrofitShort(applicationContext);

        storeListByNameMutableLiveData = new MutableLiveData<>();
        cityListMutableLiveData = new MutableLiveData<>();
        storeListByCityNameMutableLiveData = new MutableLiveData<>();
        setFavStoreMutableLiveData = new MutableLiveData<>();
        viewShopRatingMutableLiveData = new MutableLiveData<>();
        storeRatingResponseMutableLiveData = new MutableLiveData<>();
        orderAcceptResponseMutableLiveData = new MutableLiveData<>();
    }

    /* GETTERS - START */
    public MutableLiveData<StoreListByName> getStoreListByNameMutableLiveData() {
        return storeListByNameMutableLiveData;
    }

    public MutableLiveData<CityList> getCityListMutableLiveData() {
        return cityListMutableLiveData;
    }

    public MutableLiveData<StoreListByCityName> getStoreListByCityNameMutableLiveData() {
        return storeListByCityNameMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getSetFavStoreMutableLiveData() {
        return setFavStoreMutableLiveData;
    }

    public MutableLiveData<ViewShopRating> getViewShopRatingMutableLiveData() {
        return viewShopRatingMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getStoreRatingResponseMutableLiveData() {
        return storeRatingResponseMutableLiveData;
    }

    public MutableLiveData<OrderAcceptResponse> getOrderAcceptResponseMutableLiveData() {
        return orderAcceptResponseMutableLiveData;
    }
    /* GETTERS - END */

    /*SETTERS - START*/
    public void setStoreListByCityNameMutableLiveDataValue(StoreListByCityName storeListByCityName) {
        this.storeListByCityNameMutableLiveData.setValue(storeListByCityName);
    }

    public void setOrderAcceptResponseMutableLiveDataValue(OrderAcceptResponse orderAcceptResponse) {
        this.orderAcceptResponseMutableLiveData.setValue(orderAcceptResponse);
    }

    public void setStoreListByNameMutableLiveDataValue(StoreListByName storeListByName) {
        this.storeListByNameMutableLiveData.setValue(storeListByName);
    }

    public void setCityListMutableLiveDataValue(CityList cityList) {
        this.cityListMutableLiveData.setValue(cityList);
    }

    public void setSetFavStoreMutableLiveDataValue(GeneralResponse setFavStore) {
        this.setFavStoreMutableLiveData.setValue(setFavStore);
    }

    public void setViewShopRatingMutableLiveDataValue(ViewShopRating viewShopRating) {
        this.viewShopRatingMutableLiveData.setValue(viewShopRating);
    }

    public void setStoreRatingResponseMutableLiveDataValue(GeneralResponse storeRatingResponse) {
        this.storeRatingResponseMutableLiveData.setValue(storeRatingResponse);
    }
    /*SETTERS - END*/

    public boolean getStoreListByStoreNameSegment(String storeNameSegment) {
        Log.i(TAG, "getStoreListByStoreNameSegment: fired! storeNameSegment: (" + storeNameSegment + ")");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getStoreListByStoreNameSegment: Network Available!");

            Call<StoreListByName> storeListByNameCall = ApplicationData.mdg_customerAPI_interfaceShort.getStoreListByName(storeNameSegment);
            storeListByNameCall.enqueue(new Callback<StoreListByName>() {
                @Override
                public void onResponse(Call<StoreListByName> call, Response<StoreListByName> response) {
                    Log.i(TAG, "onResponse: STORE LIST BY NAME Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        StoreListByName storeListByNameUnsuccessful = new StoreListByName(response.code(), "Server didn't respond.\nPlease try again later!", null);
                        storeListByNameMutableLiveData.setValue(storeListByNameUnsuccessful);
                        return;
                    }
                    StoreListByName storeListByName = response.body();
                    Log.i(TAG, "onResponse: storeListByName: " + storeListByName);
                    storeListByNameMutableLiveData.setValue(storeListByName);
                }

                @Override
                public void onFailure(Call<StoreListByName> call, Throwable t) {
                    Log.i(TAG, "onFailure: STORE LIST BY NAME Response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        StoreListByName storeListByNameFailedSocketTimeout = new StoreListByName(1001, "Weak Network Connection. Request Failed!\nPlease try again later!", null);
                        storeListByNameMutableLiveData.setValue(storeListByNameFailedSocketTimeout);
                        return;
                    }

                    StoreListByName storeListByNameFailed = new StoreListByName(1000, "Server Request Failed.\nPlease try again later!", null);
                    storeListByNameMutableLiveData.setValue(storeListByNameFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getCityListByCityNameSegment(String cityNameSegment) {
        Log.i(TAG, "getCityListByCityNameSegment: fired! cityNameSegment: (" + cityNameSegment + ")");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getCityListByCityNameSegment: Network Available!");

            Call<CityList> cityListCall = ApplicationData.mdg_customerAPI_interfaceShort.getCityListByCityNameSegment(cityNameSegment, null);
            cityListCall.enqueue(new Callback<CityList>() {
                @Override
                public void onResponse(Call<CityList> call, Response<CityList> response) {
                    Log.i(TAG, "onResponse: CITY LIST Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        CityList cityListUnsuccessful = new CityList(response.code(), "Server didn't respond.\nPlease try again later!", null);
                        cityListMutableLiveData.setValue(cityListUnsuccessful);
                        return;
                    }
                    CityList cityList = response.body();
                    Log.i(TAG, "onResponse: cityList: " + cityList);
                    cityListMutableLiveData.setValue(cityList);
                }

                @Override
                public void onFailure(Call<CityList> call, Throwable t) {
                    Log.i(TAG, "onFailure: CITY LIST Response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        CityList cityListFailedSocketTimeout = new CityList(1001, "Weak Network Connection. Request Failed!\nPlease try again later!", null);
                        cityListMutableLiveData.setValue(cityListFailedSocketTimeout);
                        return;
                    }

                    CityList cityListFailed = new CityList(1000, "Server Request Failed.\nPlease try again later!", null);
                    cityListMutableLiveData.setValue(cityListFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getStoreListByLatLng(String latitude, String longitude) {
        Log.i(TAG, "getStoreListByLatLng: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getStoreListByLatLng: Network Available!");

            // PATNA
//            latitude = "25.6146715";
//            longitude = "85.1962226";

            Call<StoreListByCityName> storeListByLatLngCall = ApplicationData.mdg_customerAPI_interface.getStoreListByLatLng(latitude, longitude);
            storeListByLatLngCall.enqueue(new Callback<StoreListByCityName>() {
                @Override
                public void onResponse(Call<StoreListByCityName> call, Response<StoreListByCityName> response) {
                    Log.i(TAG, "onResponse: STORE LIST BY LAT LNG Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        StoreListByCityName storeListByCityNameUnsuccessful = new StoreListByCityName(response.code(), "Somehow Server didn't respond!" +
                                "\nPlease try again later!", null);
                        storeListByCityNameMutableLiveData.setValue(storeListByCityNameUnsuccessful);
                        return;
                    }
                    StoreListByCityName storeListByCityName = response.body();
                    Log.i(TAG, "onResponse: STORE LIST BY LAT-LONG, storeListByCityName: " + storeListByCityName);
                    storeListByCityNameMutableLiveData.setValue(storeListByCityName);
                }

                @Override
                public void onFailure(Call<StoreListByCityName> call, Throwable t) {
                    Log.i(TAG, "onFailure: STORE LIST BY LAT LNG Response somehow failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        StoreListByCityName storeListByCityNameFailedSocketTimeout = new StoreListByCityName(1001, "Weak Network Connection. Request Failed!\nPlease try again later!", null);
                        storeListByCityNameMutableLiveData.setValue(storeListByCityNameFailedSocketTimeout);
                        return;
                    }

                    StoreListByCityName storeListByCityNameFailed = new StoreListByCityName(1000, "Somehow Store List By City Name Response Failed!" +
                            "\nPlease try again!", null);
                    storeListByCityNameMutableLiveData.setValue(storeListByCityNameFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* STORE LIST BY CITY NAME */
    public boolean getStoreListByCityName(String cityName, int cityId) {
        Log.i(TAG, "getStoreListByCityName: fired! " + cityName);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getStoreListByCityName: Network Available!");

            Call<StoreListByCityName> storeListByCityNameCall = ApplicationData.mdg_customerAPI_interface.getStoreListByCityName(cityId);
            storeListByCityNameCall.enqueue(new Callback<StoreListByCityName>() {
                @Override
                public void onResponse(Call<StoreListByCityName> call, Response<StoreListByCityName> response) {
                    Log.i(TAG, "onResponse: STORE LIST BY CITY NAME Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        StoreListByCityName storeListByCityNameUnsuccessful = new StoreListByCityName(response.code(), "Somehow Server didn't respond!" +
                                "\nPlease try again later!", null);
                        storeListByCityNameMutableLiveData.setValue(storeListByCityNameUnsuccessful);
                        return;
                    }
                    StoreListByCityName storeListByCityName = response.body();
                    Log.i(TAG, "onResponse: STORE LIST BY CITY NAME, storeListByCityName: " + storeListByCityName);
                    storeListByCityNameMutableLiveData.setValue(storeListByCityName);
                }

                @Override
                public void onFailure(Call<StoreListByCityName> call, Throwable t) {
                    Log.i(TAG, "onFailure: STORE LIST BY CITY NAME Response somehow failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        StoreListByCityName storeListByCityNameFailedSocketTimeout = new StoreListByCityName(1001, "Weak Network Connection. Request Failed!\nPlease try again later!", null);
                        storeListByCityNameMutableLiveData.setValue(storeListByCityNameFailedSocketTimeout);
                        return;
                    }

                    StoreListByCityName storeListByCityNameFailed = new StoreListByCityName(1000, "Somehow Store List By City Name Response Failed!" +
                            "\nPlease try again!", null);
                    storeListByCityNameMutableLiveData.setValue(storeListByCityNameFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* FAV STORES LIST */
    public boolean getFavStoresList(int buyerId) {
        Log.i(TAG, "getFavStoresList: fired! buyerId: " + buyerId);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getFavStoresList: Network Available!");

            Call<StoreListByCityName> favStoresCall = ApplicationData.mdg_customerAPI_interface.getFavStoresList(buyerId);
            favStoresCall.enqueue(new Callback<StoreListByCityName>() {
                @Override
                public void onResponse(Call<StoreListByCityName> call, Response<StoreListByCityName> response) {
                    Log.i(TAG, "onResponse: FAV STORES LIST Response seems to be a Success!");

                    if (!response.isSuccessful()) {
                        StoreListByCityName storeListByCityNameUnsuccessful = new StoreListByCityName(response.code(), "Somehow Server didn't respond!" +
                                "\nPlease try again later!", null);
                        storeListByCityNameMutableLiveData.setValue(storeListByCityNameUnsuccessful);
                        return;
                    }
                    StoreListByCityName storeListByCityName = response.body();
                    Log.i(TAG, "onResponse: FAV STORES LIST, storeListByCityName: " + storeListByCityName);
                    storeListByCityNameMutableLiveData.setValue(storeListByCityName);
                }

                @Override
                public void onFailure(Call<StoreListByCityName> call, Throwable t) {
                    Log.i(TAG, "onFailure: FAV STORES LIST Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        StoreListByCityName storeListByCityNameFailedSocketTimeout = new StoreListByCityName(1001, "Weak Network Connection. Request Failed!\nPlease try again later!", null);
                        storeListByCityNameMutableLiveData.setValue(storeListByCityNameFailedSocketTimeout);
                        return;
                    }

                    StoreListByCityName storeListByCityNameFailed = new StoreListByCityName(1000, "Somehow Store List By City Name Response Failed!" +
                            "\nPlease try again!", null);
                    storeListByCityNameMutableLiveData.setValue(storeListByCityNameFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean setFavStore(int buyerId, int vendorId) {
        Log.i(TAG, "setFavStore: fired! buyerId: " + buyerId + "\tvendorId: " + vendorId);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "setFavStore: Network Available!");

            Call<GeneralResponse> setFavStoreResponseCall = ApplicationData.mdg_customerAPI_interface.setFavStore(buyerId, vendorId);
            setFavStoreResponseCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: SET FAV STORE Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse setFavStoreResponseUnsuccessful = new GeneralResponse("Somehow Server didn't respond!" +
                                "\nPlease try again later!", "", response.code());
                        setFavStoreMutableLiveData.setValue(setFavStoreResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse setFavStoreResponse = response.body();
                    Log.i(TAG, "onResponse: SET FAV STORE, setFavStoreResponse: " + setFavStoreResponse);
                    setFavStoreMutableLiveData.setValue(setFavStoreResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: SET FAV STORE Response somehow failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse setFavStoreResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        setFavStoreMutableLiveData.setValue(setFavStoreResponseFailedSocketTimeout);
                        return;
                    }

                    GeneralResponse setFavStoreResponseFailed = new GeneralResponse("Somehow Set Favourite Store Response Failed!" +
                            "\nPlease try again later!", "", 1000);
                    setFavStoreMutableLiveData.setValue(setFavStoreResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean viewAllRatingsOfShop(int storeId) {
        Log.i(TAG, "viewAllRatingsOfShop: fired! storeId: " + storeId);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "viewAllRatingsOfShop: Network Available!");

            Call<ViewShopRating> viewShopRatingCall = ApplicationData.mdg_customerAPI_interface.viewAllRatingsOfShop(storeId);
            viewShopRatingCall.enqueue(new Callback<ViewShopRating>() {
                @Override
                public void onResponse(Call<ViewShopRating> call, Response<ViewShopRating> response) {
                    Log.i(TAG, "onResponse: VIEW ALL RATINGS OF SHOP Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        ViewShopRating viewShopRating = new ViewShopRating("Somehow Server didn't respond!", null, 0f, response.code());
                        viewShopRatingMutableLiveData.setValue(viewShopRating);
                        return;
                    }
                    ViewShopRating viewShopRating = response.body();
                    Log.i(TAG, "onResponse: viewShopRating: " + viewShopRating);
                    viewShopRatingMutableLiveData.setValue(viewShopRating);
                }

                @Override
                public void onFailure(Call<ViewShopRating> call, Throwable t) {
                    Log.i(TAG, "onFailure: VIEW ALL RATINGS OF SHOP Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ViewShopRating viewShopRatingFailedSocketTimeout = new ViewShopRating(applicationContext.getString(R.string.weak_internet_connection), null, 0f, 1001);
                        viewShopRatingMutableLiveData.setValue(viewShopRatingFailedSocketTimeout);
                        return;
                    }
                    ViewShopRating viewShopRatingFailed = new ViewShopRating("Somehow View All Ratings request seems to have failed!", null, 0f, 1000);
                    viewShopRatingMutableLiveData.setValue(viewShopRatingFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean rateStore(RateStoreModel rateStoreModel) {
        Log.i(TAG, "rateStore: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "rateStore: Network Available!");

            Call<GeneralResponse> storeRatingCall = ApplicationData.mdg_customerAPI_interface.rateStore(rateStoreModel);
            storeRatingCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: STORE RATING Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!" +
                                "\nPlease try again later!", null, response.code());
                        storeRatingResponseMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: generalResponse: " + generalResponse);
                    storeRatingResponseMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: STORE RATING Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponse = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        storeRatingResponseMutableLiveData.setValue(generalResponse);
                        return;
                    }
                    GeneralResponse generalResponse = new GeneralResponse("Somehow Store Rating Request Failed!", null, 1000);
                    storeRatingResponseMutableLiveData.setValue(generalResponse);
                }
            });
            return true;
        }
        return false;
    }

    /* CHECK IF STORE CAN ACCEPT ORDERS. */
//    public boolean checkVendorOrderAcceptance(int vendorId) {
//        Log.i(TAG, "checkVendorOrderAcceptance: fired! vendorId: " + vendorId);
//
//        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
//            Log.i(TAG, "checkVendorOrderAcceptance: Network Available!");
//
//            Call<OrderAcceptResponse> orderAcceptResponseCall = ApplicationData.mdg_customerAPI_interface.checkVendorOrderAcceptance(vendorId);
//            orderAcceptResponseCall.enqueue(new Callback<OrderAcceptResponse>() {
//                @Override
//                public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
//                    Log.i(TAG, "onResponse: ORDER ACCEPT Response seems to be a success!");
//
//                    if (!response.isSuccessful()) {
//                        OrderAcceptResponse orderAcceptResponseUnsuccessful = new OrderAcceptResponse(applicationContext.getString(R.string.server_didnt_respond), -1, response.code());
//                        orderAcceptResponseMutableLiveData.setValue(orderAcceptResponseUnsuccessful);
//                        return;
//                    }
//                    OrderAcceptResponse orderAcceptResponse = response.body();
//                    Log.i(TAG, "onResponse: orderAcceptResponse: " + orderAcceptResponse);
//                    orderAcceptResponseMutableLiveData.setValue(orderAcceptResponse);
//                }
//
//                @Override
//                public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
//                    Log.i(TAG, "onFailure: ORDER ACCEPT Request seems to have failed!\tt.getMessage(): " + t.getMessage());
//                    t.printStackTrace();
//
//                    if (t instanceof SocketTimeoutException) {
//                        OrderAcceptResponse orderAcceptResponseFailedSocketTimeout = new OrderAcceptResponse(applicationContext.getString(R.string.weak_internet_connection), -1, 1001);
//                        orderAcceptResponseMutableLiveData.setValue(orderAcceptResponseFailedSocketTimeout);
//                        return;
//                    }
//                    OrderAcceptResponse orderAcceptResponseFailed = new OrderAcceptResponse("Somehow Order Accept Response failed!\nPlease try again later!", -1, 1000);
//                    orderAcceptResponseMutableLiveData.setValue(orderAcceptResponseFailed);
//                }
//            });
//            return true;
//        }
//        return false;
//    }
}
