package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Repositories.AccountRepository;

import org.jetbrains.annotations.NotNull;

public class AccountViewModel extends AndroidViewModel {
    private static final String TAG = "AccountViewModel";

    private AccountRepository accountRepository;

    private MutableLiveData<GeneralResponse> logoutResponseMutableLiveData;

    public AccountViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "AccountViewModel: fired!");

        initAccountViewModel();
    }

    private void initAccountViewModel() {
        Log.i(TAG, "initAccountViewModel: fired!");

        if (accountRepository != null) {
            Log.i(TAG, "initAccountViewModel: Account Repository already initialized in AccountViewModel.");
            return;
        }
        accountRepository = AccountRepository.getAccountRepository(getApplication().getApplicationContext());

        logoutResponseMutableLiveData = accountRepository.getLogoutResponseMutableLiveData();
    }

    public MutableLiveData<GeneralResponse> getLogoutResponseMutableLiveData() {
        return logoutResponseMutableLiveData;
    }

    public void setLogoutResponseMutableLiveData(GeneralResponse logoutResponse) {
        accountRepository.setLogoutResponseMutableLiveData(logoutResponse);
    }

    public boolean logoutCustomer() {
        Log.i(TAG, "logoutCustomer: fired!");

        return accountRepository.logoutCustomer();
    }
}
