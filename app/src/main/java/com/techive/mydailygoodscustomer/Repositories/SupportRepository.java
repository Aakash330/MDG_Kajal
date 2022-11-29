package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPError;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPInnerMessage2;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.NumberValidate;
import com.techive.mydailygoodscustomer.Models.PasswordModel;
import com.techive.mydailygoodscustomer.Models.ResetPasswordModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportRepository {
    private static final String TAG = "SupportRepository";

    private static SupportRepository supportRepository;

    private static Context applicationContext;

    private MutableLiveData<GeneralResponse> changePasswordResponseMutableLiveData;
    private MutableLiveData<NumberValidate> numberValidateMutableLiveData;
    private MutableLiveData<GeneralResponse> resetPasswordMutableLiveData;
    private MutableLiveData<OTPResponse> otpResponseMutableLiveData;

    public SupportRepository() {
        Log.i(TAG, "SupportRepository: Empty Constructor fired!");

        initSupportRepository();
    }

    public static SupportRepository getSupportRepository(Context context) {
        Log.i(TAG, "getSupportRepository: fired!");

        if (supportRepository == null) {
            applicationContext = context;
            supportRepository = new SupportRepository();
        }
        return supportRepository;
    }

    /*public*/
    private void initSupportRepository() {
        Log.i(TAG, "initSupportRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        changePasswordResponseMutableLiveData = new MutableLiveData<>();
        numberValidateMutableLiveData = new MutableLiveData<>();
        resetPasswordMutableLiveData = new MutableLiveData<>();
        otpResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<GeneralResponse> getChangePasswordResponseMutableLiveData() {
        return changePasswordResponseMutableLiveData;
    }

    public MutableLiveData<NumberValidate> getNumberValidateMutableLiveData() {
        return numberValidateMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getResetPasswordMutableLiveData() {
        return resetPasswordMutableLiveData;
    }

    public MutableLiveData<OTPResponse> getOtpResponseMutableLiveData() {
        return otpResponseMutableLiveData;
    }

    /* UPDATE PASSWORD */
    public boolean updatePassword(PasswordModel passwordModel) {
        Log.i(TAG, "updatePassword: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "updatePassword: Network available!");

            Call<GeneralResponse> changePasswordCall = ApplicationData.mdg_customerAPI_interface.changePassword(passwordModel);
            changePasswordCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: CHANGE PASSWORD Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!\nPlease try again later!", "", response.code());
                        changePasswordResponseMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: changePassword: " + generalResponse);
                    changePasswordResponseMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: CHANGE PASSWORD Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        changePasswordResponseMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Change Password Request failed!", "", 1000);
                    changePasswordResponseMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    // NUMBER VALIDATE
    public boolean numberValidate(String mobileNumber) {
        Log.i(TAG, "numberValidate: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "numberValidate: Network available!");

            Call<NumberValidate> numberValidateCall = ApplicationData.mdg_customerAPI_interface.numberValidate(mobileNumber);
            numberValidateCall.enqueue(new Callback<NumberValidate>() {
                @Override
                public void onResponse(Call<NumberValidate> call, Response<NumberValidate> response) {
                    Log.i(TAG, "onResponse: NUMBER VALIDATE Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        NumberValidate numberValidateUnsuccessful = new NumberValidate(applicationContext.getString(R.string.server_didnt_respond), -1, -1, response.code());
                        numberValidateMutableLiveData.setValue(numberValidateUnsuccessful);
                        return;
                    }
                    NumberValidate numberValidateResponse = response.body();
                    Log.i(TAG, "onResponse: numberValidateResponse: " + numberValidateResponse);
                    numberValidateMutableLiveData.setValue(numberValidateResponse);
                }

                @Override
                public void onFailure(Call<NumberValidate> call, Throwable t) {
                    Log.i(TAG, "onFailure: NUMBER VALIDATE Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        NumberValidate numberValidateFailedSocketTimeout = new NumberValidate(applicationContext.getString(R.string.weak_internet_connection), -1, -1, 1001);
                        numberValidateMutableLiveData.setValue(numberValidateFailedSocketTimeout);
                        return;
                    }
                    NumberValidate numberValidateResponseFailed = new NumberValidate("Somehow Number Validate Request Failed\nPlease try again later!", -1, -1, 1000);
                    numberValidateMutableLiveData.setValue(numberValidateResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    // RESET PASSWORD
    public boolean resetPassword(String newPassword, int loginType, int userId) {
        Log.i(TAG, "resetPassword: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "resetPassword: Network available!");

            Call<GeneralResponse> resetPasswordCall = ApplicationData.mdg_customerAPI_interface.resetPassword(
                    new ResetPasswordModel(userId, loginType, newPassword));
            resetPasswordCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: RESET PASSWORD Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse(applicationContext.getString(R.string.server_didnt_respond), null, response.code());
                        resetPasswordMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: resetPassword, generalResponse: " + generalResponse);
                    resetPasswordMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: RESET PASSWORD Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        resetPasswordMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Reset Password Request Failed\nPlease try again later!", null, 1000);
                    resetPasswordMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    // SEND OTP
    public boolean sendOTP(int random4DigitNo, long recipient) {
        Log.i(TAG, "sendOTP: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "sendOTP: Network available!");

            /*
             * You have requested for reset password on MyDailyGoods - 91 Techive Online Services Pvt. Ltd. Your 4 digit OTP is %%|text^{"inputtype" : "text", "maxlength" : "4"}%% . Ignore in case you have not requested.
             * */

            String msg1 = "You have requested for reset password on MyDailyGoods - 91 Techive Online Services Pvt. Ltd. Your 4 digit OTP is ";
            String msg2 = " . Ignore in case you have not requested.";

            String message = msg1 + random4DigitNo + msg2;
            Log.i(TAG, "sendOTP: message: " + message);
            Log.i(TAG, "sendOTP: recipient: " + recipient);
            String sender = "MYDGDS";
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
}
