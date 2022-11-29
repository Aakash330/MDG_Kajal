package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.LogoutModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private static final String TAG = "AccountRepository";

    private Context applicationContext;

    private static AccountRepository accountRepository;

    private MutableLiveData<GeneralResponse> logoutResponseMutableLiveData;

    public AccountRepository(Context applicationContext) {
        Log.i(TAG, "AccountRepository: Context based constructor fired!");
        this.applicationContext = applicationContext;

        initAccountRepository();
    }

    public static AccountRepository getAccountRepository(Context context) {
        Log.i(TAG, "getAccountRepository: fired!");

        if (accountRepository == null) {
            Log.i(TAG, "getAccountRepository: AccountRepository was null. Now initialized!");
            accountRepository = new AccountRepository(context);
        }
        return accountRepository;
    }

    private void initAccountRepository() {
        Log.i(TAG, "initAccountRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        logoutResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<GeneralResponse> getLogoutResponseMutableLiveData() {
        return logoutResponseMutableLiveData;
    }

    public void setLogoutResponseMutableLiveData(GeneralResponse logoutResponse) {
        logoutResponseMutableLiveData.setValue(logoutResponse);
    }

    //LOGOUT VENDOR
    public boolean logoutCustomer() {
        Log.i(TAG, "logoutCustomer: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "logoutCustomer: Network Available!");

            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(applicationContext);
            SharedPreferences sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();
            String token = sharedPreferences.getString(SharedPreferencesManager.token, "token");
            Log.i(TAG, "logoutCustomer: token: " + token);

            LogoutModel logoutModel = new LogoutModel(ApplicationData.getLoggedInBuyerId(), token);
            Log.i(TAG, "logoutCustomer: logoutModel: " + logoutModel);

            Call<GeneralResponse> logoutCall = ApplicationData.mdg_customerAPI_interface.logoutCustomer(logoutModel);
            logoutCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: LOGOUT CUSTOMER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse logoutResponseUnsuccessful = new GeneralResponse(applicationContext.getString(R.string.server_didnt_respond) + "-" + response.code(), "", response.code());
                        logoutResponseMutableLiveData.setValue(logoutResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse logoutResponse = response.body();
                    Log.i(TAG, "onResponse: storeSetupResponse: " + logoutResponse);
                    logoutResponseMutableLiveData.setValue(logoutResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: LOGOUT CUSTOMER response seems to have failed! \tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse logoutResponseFailedSocketTimeout = new GeneralResponse(
                                applicationContext.getString(R.string.connection_timed_out), "", 1001);
                        logoutResponseMutableLiveData.setValue(logoutResponseFailedSocketTimeout);
                        return;
                    }

                    String failureMsg = "Logout Response has failed!";
                    GeneralResponse logoutResponseFailed = new GeneralResponse(failureMsg, "", 1000);
                    logoutResponseMutableLiveData.setValue(logoutResponseFailed);
                }
            });
            return true;
        }
        return false;
    }
}
