package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.PasswordModel;
import com.techive.mydailygoodscustomer.Repositories.SupportRepository;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordViewModel extends AndroidViewModel {
    private static final String TAG = "ChangePasswordViewModel";

    private SupportRepository supportRepository;

    private MutableLiveData<GeneralResponse> changePasswordResponseMutableLiveData;

    public ChangePasswordViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "ChangePasswordViewModel: fired!");

        initChangePasswordViewModel();
    }

    private void initChangePasswordViewModel() {
        Log.i(TAG, "initChangePasswordViewModel: fired!");

        if (supportRepository != null) {
            return;
        }

        supportRepository = SupportRepository.getSupportRepository(getApplication().getApplicationContext());
//        supportRepository.initSupportRepository();

        changePasswordResponseMutableLiveData = supportRepository.getChangePasswordResponseMutableLiveData();
    }

    public MutableLiveData<GeneralResponse> getChangePasswordResponseMutableLiveData() {
        return changePasswordResponseMutableLiveData;
    }

    public boolean updatePassword(PasswordModel passwordModel) {
        Log.i(TAG, "updatePassword: fired!");

        return supportRepository.updatePassword(passwordModel);
    }
}
