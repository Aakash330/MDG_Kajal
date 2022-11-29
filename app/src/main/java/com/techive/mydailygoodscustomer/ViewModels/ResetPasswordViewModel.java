package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.NumberValidate;
import com.techive.mydailygoodscustomer.Repositories.SupportRepository;

import org.jetbrains.annotations.NotNull;

public class ResetPasswordViewModel extends AndroidViewModel {
    private static final String TAG = "ResetPasswordViewModel";

    private SupportRepository supportRepository;

    private MutableLiveData<NumberValidate> numberValidateMutableLiveData;
    private MutableLiveData<GeneralResponse> resetPasswordMutableLiveData;
    private MutableLiveData<OTPResponse> otpResponseMutableLiveData;

    public String mobileNumber;
    public int OTPSent;

    public ResetPasswordViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "ResetPasswordViewModel: fired!");

        initResetPasswordViewModel();
    }

    private void initResetPasswordViewModel() {
        Log.i(TAG, "initResetPasswordViewModel: fired!");

        if (supportRepository != null) {
            Log.i(TAG, "initResetPasswordViewModel: Support Repository already initialized in ResetPasswordViewModel.");
            return;
        }

        supportRepository = SupportRepository.getSupportRepository(getApplication().getApplicationContext());

        numberValidateMutableLiveData = supportRepository.getNumberValidateMutableLiveData();
        resetPasswordMutableLiveData = supportRepository.getResetPasswordMutableLiveData();
        otpResponseMutableLiveData = supportRepository.getOtpResponseMutableLiveData();
    }

    /* GETTERS */
    public MutableLiveData<NumberValidate> getNumberValidateMutableLiveData() {
        return numberValidateMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getResetPasswordMutableLiveData() {
        return resetPasswordMutableLiveData;
    }

    public MutableLiveData<OTPResponse> getOtpResponseMutableLiveData() {
        return otpResponseMutableLiveData;
    }
    /* GETTERS - END */

    public boolean numberValidate(String mobileNumber) {
        Log.i(TAG, "numberValidate: fired!");

        this.mobileNumber = mobileNumber;

        return supportRepository.numberValidate(mobileNumber);
    }

    public boolean resetPassword(String newPassword, int type, int userId) {
        Log.i(TAG, "resetPassword: fired!");

        return supportRepository.resetPassword(newPassword, type, userId);
    }

    public boolean sendOTP(int random4DigitNo, long numbers) {
        Log.i(TAG, "sendOTP: fired!");

        return supportRepository.sendOTP(random4DigitNo, numbers);
    }
}
