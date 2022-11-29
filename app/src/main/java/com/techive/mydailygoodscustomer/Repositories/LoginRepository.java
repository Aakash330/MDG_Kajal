package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.LoginResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPError;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPInnerMessage2;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.LoginModel;
import com.techive.mydailygoodscustomer.Models.RegisterModel;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private static final String TAG = "LoginRepository";

    private static LoginRepository loginRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<CityList> cityListMutableLiveData;
    private MutableLiveData<StateList> stateListMutableLiveData;
    private MutableLiveData<OTPResponse> otpResponseMutableLiveData;
    private MutableLiveData<GeneralResponse> userValidityResponseMutableLiveData;
    //NOT USING A DIFFERENT MUTABLE LIVE DATA, BECAUSE THROUGH BOTH LOGIN & REGISTER, WILL BE GOING TO THE SAME PAGE.
//    private MutableLiveData<LoginResponse> registerResponseMutableLiveData;

    public static LoginRepository getLoginRepositoryInstance(Context context) {
        if (loginRepositoryInstance == null) {
            loginRepositoryInstance = new LoginRepository();
            applicationContext = context;
        }
        return loginRepositoryInstance;
    }

    public void initializeLoginRepository() {
        Log.i(TAG, "initializeLoginRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        loginResponseMutableLiveData = new MutableLiveData<>();
        cityListMutableLiveData = new MutableLiveData<>();
        stateListMutableLiveData = new MutableLiveData<>();
        otpResponseMutableLiveData = new MutableLiveData<>();
        userValidityResponseMutableLiveData = new MutableLiveData<>();
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

    /*1127 - mudit2 mittal2
     * mob: 9219534642
     * pass: buyer123*/

    /* 1126 - test buyer 18-7-22
     * mob: 7417417417
     * pass: testbuyer */

    /* 1120 - Cosmic Byte
     * mob: 7988821805
     * pass: cosmicbyte */

    /*
    7988821805
    * password7988821805*/
    /*7988821803 - password - changed on 5-7-22*/


    /*<string name="PASSWORD">mdgcustomer</string>
    <int name="USER_ID" value="126" />
    <string name="MOB_NO">7894561230</string>*/

    /*8630744629
        buyer@123
        Neha ma'am customer login*/

    /*7897897897
     * abcxyz
     * My Created Buyer for registration check - 153*/

    //    public boolean login(String mobNo, String password) {
    public boolean login(LoginModel loginModel) {
        Log.i(TAG, "login: fired! loginModel: " + loginModel);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "login: Network Available!");

            Call<LoginResponse> loginResponseCall = ApplicationData.mdg_customerAPI_interface.login(loginModel);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.i(TAG, "onResponse: LOGIN Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        LoginResponse loginResponseUnsuccessful = new LoginResponse("Somehow server didn't respond.\nPlease try again later.", response.code(), -1, "");
                        loginResponseMutableLiveData.setValue(loginResponseUnsuccessful);
                        return;
                    }
                    LoginResponse loginResponse = response.body();
                    Log.i(TAG, "onResponse: loginResponse: " + loginResponse);
                    loginResponseMutableLiveData.setValue(loginResponse);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: LOGIN Response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        LoginResponse loginResponseFailedSocketTimeout = new LoginResponse(applicationContext.getString(R.string.weak_internet_connection), 1001, -1, "");
                        loginResponseMutableLiveData.setValue(loginResponseFailedSocketTimeout);
                        return;
                    }

                    LoginResponse loginResponseFailed = new LoginResponse("Somehow Login Response failed.\nPlease try again later!", 1000, -1, "");
                    loginResponseMutableLiveData.setValue(loginResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean register(RegisterModel registerModel) {
        Log.i(TAG, "register: fired! registerModel: " + registerModel);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "register: Network Available!");

            Call<LoginResponse> registerResponseCall = ApplicationData.mdg_customerAPI_interface.register(registerModel);
            registerResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.i(TAG, "onResponse: REGISTER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        LoginResponse registerResponseUnsuccessful = new LoginResponse("Somehow server didn't respond.\nPlease try again later!", response.code(), -1, "");
                        loginResponseMutableLiveData.setValue(registerResponseUnsuccessful);
                        return;
                    }
                    LoginResponse registerResponse = response.body();
                    Log.i(TAG, "onResponse: registerResponse: " + registerResponse);
                    loginResponseMutableLiveData.setValue(registerResponse);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: REGISTER Response seems to have failed!\nt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        LoginResponse registerResponseFailedSocketTimeout = new LoginResponse(applicationContext.getString(R.string.weak_internet_connection), 1001, -1, "");
                        loginResponseMutableLiveData.setValue(registerResponseFailedSocketTimeout);
                        return;
                    }
                    LoginResponse registerResponseFailed = new LoginResponse("Register Response somehow Failed.\nPlease try again later!", 1000, -1, "");
                    loginResponseMutableLiveData.setValue(registerResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean getCityListByCityNameSegment(String cityNameSegment, int stateId) {
        Log.i(TAG, "getCityListByCityNameSegment: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getCityListByCityNameSegment: Network Available!");

            Call<CityList> cityListCall = ApplicationData.mdg_customerAPI_interface.getCityListByCityNameSegment(cityNameSegment, stateId);
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

    public boolean sendOTP(int random5DigitNo, long recipient) {
        Log.i(TAG, "sendOTP: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "sendOTP: Network available!");

            // WARNING:: DO NOT TRY TO ALTER THE MESSAGE SEGMENTS.
            String message1 = "Your OTP to register with MyDailyGoods (91 Techive Online Services Pvt. Ltd.) is ";
            String message2 = ". The OTP will remain valid for next 15 minutes only.";
            String message = message1 + random5DigitNo + message2;
            Log.i(TAG, "sendOTP: message: " + message);
            Log.i(TAG, "sendOTP: recipient: " + recipient);
            String sender = "MDGOTP";
            Log.i(TAG, "sendOTP: sender: " + sender);
            String apiKey = "NDY2MjU1NDc2ZjMxNjE1MDQ0NGE3OTdhNDc2MzQ5Njc=";
            Log.i(TAG, "sendOTP: apiKey: " + apiKey);

//            String test = "true";
            String test = "false";

            Call<OTPResponse> otpResponseCall2 = ApplicationData.mdg_customerAPI_interface.sendOTP2(apiKey, message, sender, recipient, test);
            otpResponseCall2.enqueue(new Callback<OTPResponse>() {
                @Override
                public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                    Log.i(TAG, "onResponse: OTP response seems to be successful.");

                    if (!response.isSuccessful()) {
                        Log.i(TAG, "onResponse: Response code: " + response.code());
                        Log.i(TAG, "onResponse: Response toString(): " + response.toString());
                        OTPResponse otpResponseUnsuccessful = new OTPResponse(-1, "-1", -1,
                                -1, null, null, "Unsuccessful", null);
                        otpResponseMutableLiveData.setValue(otpResponseUnsuccessful);
                        return;
                    }
                    OTPResponse otpResponse = response.body();
                    Log.i(TAG, "onResponse: otpResponse.getStatus(): " + otpResponse.getStatus());
                    Log.i(TAG, "onResponse: otpResponse.getBalance(): " + otpResponse.getBalance());

                    if ("failure".equals(otpResponse.getStatus())) {
                        Log.i(TAG, "onResponse: inside failure response.");

                        String errorMessageForUser = "";
                        for (OTPError otpError : otpResponse.getErrors()) {
                            Log.i(TAG, "onResponse: otpError.getCode(): " + otpError.getCode());
                            Log.i(TAG, "onResponse: otpError.getMessage(): " + otpError.getMessage());
                            String partMessage = otpError.getMessage() + " Code: " + otpError.getCode();
                            errorMessageForUser += partMessage;
                        }
                        Toast.makeText(applicationContext, errorMessageForUser, Toast.LENGTH_LONG).show();
                        otpResponseMutableLiveData.setValue(otpResponse);

                        // RETURN FAILURE
                    } else {
                        Log.i(TAG, "onResponse: inside success response.");

                        String successMessageForUser = "";
                        for (OTPInnerMessage2 otpInnerMessage2 : otpResponse.getMessages()) {
                            successMessageForUser = "OTP Successfully sent to: " + otpInnerMessage2.getRecipient();
                        }
                        Toast.makeText(applicationContext, successMessageForUser, Toast.LENGTH_LONG).show();
                        otpResponseMutableLiveData.setValue(otpResponse);

                    }

////                Log.i(TAG, "onResponse: otpResponse.getMessages().get(0).getRecipient(): " + otpResponse.getMessages().get(0).getRecipient());
////                Log.i(TAG, "onResponse: otpResponse.getMessages().get(0).getId(): " + otpResponse.getMessages().get(0).getId());
////                Log.i(TAG, "onResponse: otpResponse.getMessage().getContent(): " + otpResponse.getMessage().getContent());

//                Log.i(TAG, "onResponse: otpResponse.getErrors().get(0).getCode(): " + otpResponse.getErrors().get(0).getCode());
//                Log.i(TAG, "onResponse: otpResponse.getErrors().get(0).getMessage(): " + otpResponse.getErrors().get(0).getMessage());

                }

                @Override
                public void onFailure(Call<OTPResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: OTP Response Failure Somehow! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    OTPResponse otpResponseFailed = new OTPResponse(-1, "-1", -1,
                            -1, null, null, "Failed", null);
                    otpResponseMutableLiveData.setValue(otpResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean userValidityCheck(String email, String mobNo) {
        Log.i(TAG, "userValidityCheck: fired! email: " + email + "\tmobNo: " + mobNo);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "userValidityCheck: Network available!");

            Call<GeneralResponse> userValidityCall = ApplicationData.mdg_customerAPI_interface.userValidityCheck(email, mobNo);
            userValidityCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: USER VALIDITY Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        Log.i(TAG, "onResponse: response.code(): " + response.code());
                        GeneralResponse userValidityResponseUnsuccessful = new GeneralResponse("Server response Unsuccessful!", "", response.code());
                        userValidityResponseMutableLiveData.setValue(userValidityResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse userValidityResponse = response.body();
                    Log.i(TAG, "onResponse: userValidityResponse: " + userValidityResponse);
                    userValidityResponseMutableLiveData.setValue(userValidityResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: USER VALIDITY Response seems to have failed! t.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse userValidityResponseFailed = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        userValidityResponseMutableLiveData.setValue(userValidityResponseFailed);
                    }
                    GeneralResponse userValidityResponseFailed = new GeneralResponse("Somehow server response failed!", "", 1000);
                    userValidityResponseMutableLiveData.setValue(userValidityResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

}
