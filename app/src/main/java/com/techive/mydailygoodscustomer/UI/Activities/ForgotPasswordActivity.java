package com.techive.mydailygoodscustomer.UI.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.NumberValidate;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.ViewModels.ResetPasswordViewModel;

import java.util.concurrent.ThreadLocalRandom;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private TextInputEditText forgotMobileNoTextInputEditText, enterOTPTextInputEditText, newPasswordTextInputEditText;

    private MaterialButton forgot_sendOTPMaterialButton, resetPasswordMaterialButton;

    private LinearLayoutCompat forgotPasswordLinearLayoutCompat;

    private ResetPasswordViewModel resetPasswordViewModel;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Forgot Password");
        Log.i(TAG, "onCreate: fired!");

//        MaterialToolbar forgotPasswordMaterialToolbar = findViewById(R.id.forgotPasswordMaterialToolbar);
//        setSupportActionBar(forgotPasswordMaterialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponentViews();

        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);

//        initAdapters();

        initObservers();

        initListeners();
    }

    // FOR BACK BUTTON PRESS FROM THE TOOLBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponentViews() {
        Log.i(TAG, "initComponentViews: fired!");

        forgotPasswordLinearLayoutCompat = findViewById(R.id.forgotPasswordLinearLayoutCompat);

        forgotMobileNoTextInputEditText = findViewById(R.id.forgotMobileNoTextInputEditText);
        enterOTPTextInputEditText = findViewById(R.id.enterOTPTextInputEditText);
        newPasswordTextInputEditText = findViewById(R.id.newPasswordTextInputEditText);

        forgot_sendOTPMaterialButton = findViewById(R.id.forgot_sendOTPMaterialButton);
        resetPasswordMaterialButton = findViewById(R.id.resetPasswordMaterialButton);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        resetPasswordViewModel.getNumberValidateMutableLiveData().observe(this, numberValidateObserver);
        resetPasswordViewModel.getResetPasswordMutableLiveData().observe(this, resetPasswordObserver);
        resetPasswordViewModel.getOtpResponseMutableLiveData().observe(this, otpResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        forgot_sendOTPMaterialButton.setOnClickListener(onClickListener);
        resetPasswordMaterialButton.setOnClickListener(onClickListener);
    }

    private final Observer<NumberValidate> numberValidateObserver = new Observer<NumberValidate>() {
        @Override
        public void onChanged(NumberValidate numberValidate) {
            Log.i(TAG, "onChanged: NUMBER VALIDATE Observer fired!\nnumberValidate: " + numberValidate);

            DialogUtil.dismissProcessingInfoDialog();

            if (numberValidate.getError() == 200) {
                Log.i(TAG, "onChanged: Proceeding to send OTP!");

                sendOTP();
            } else {
                Log.i(TAG, "onChanged: Something went wrong during number validate!");
                Toast.makeText(ForgotPasswordActivity.this, numberValidate.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<GeneralResponse> resetPasswordObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: RESET PASSWORD Observer fired!\ngeneralResponse: " + generalResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if (generalResponse.getError() == 200) {
                DialogUtil.showCustomSnackbar(ForgotPasswordActivity.this, forgotPasswordLinearLayoutCompat, generalResponse.getMsg(), -1);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: Inside run method in Reset Password observer!");
                        finish();
                    }
                }, 1200);   /* 1.2 sec */
            } else {
                Log.i(TAG, "onChanged: Something went wrong during reset password!");
                Toast.makeText(ForgotPasswordActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<OTPResponse> otpResponseObserver = new Observer<OTPResponse>() {
        @Override
        public void onChanged(OTPResponse otpResponse) {
            Log.i(TAG, "onChanged: OtpResponseObserver fired!");

            DialogUtil.dismissProcessingInfoDialog();

            if ("success".equals(otpResponse.getStatus())) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter the OTP you have received!", Toast.LENGTH_SHORT).show();

                //WILL 0 OUT THE OTP SENT SO AS TO STATE INVALID AFTER 15 MINS.
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: fired! Zeroing out the OTP after 15 mins.");

                        resetPasswordViewModel.OTPSent = 0;
                    }
                }, 900000);     //15 mins

                //MY CHECKS.
            } else if ("Unsuccessful".equals(otpResponse.getStatus()) || "Failed".equals(otpResponse.getStatus())) {
                Log.i(TAG, "onChanged: Sending OTP failed!\t" + otpResponse);
                Toast.makeText(ForgotPasswordActivity.this, "Couldn't send OTP.\nPlease try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");

//            Toast.makeText(ForgotPasswordActivity.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

            if (view.getId() == forgot_sendOTPMaterialButton.getId()) {
                //SEND OTP
                if (!forgotMobileNoTextInputEditText.getText().toString().matches("")
                        && forgotMobileNoTextInputEditText.getText().toString().length() == 10) {
                    Log.i(TAG, "onClick: Proceeding to validate number.");

                    if (!resetPasswordViewModel.numberValidate(forgotMobileNoTextInputEditText.getText().toString())) {
                        DialogUtil.showNoInternetToast(ForgotPasswordActivity.this);
                    } else {
                        DialogUtil.showProcessingInfoDialog(ForgotPasswordActivity.this);
                    }
                } else {
                    Log.i(TAG, "onClick: Enter a proper number!");
                    Toast.makeText(ForgotPasswordActivity.this, "Mobile Number can't be empty & should be of 10 digits.", Toast.LENGTH_SHORT).show();
                }
            } else {
                //RESET PASSWORD

                if (!enterOTPTextInputEditText.getText().toString().matches("")
                        && enterOTPTextInputEditText.getText().toString().matches(String.valueOf(resetPasswordViewModel.OTPSent))) {
                    Log.i(TAG, "onClick: OTP Matched, proceeding to check password & reset.");
                    resetPassword();
                } else {
                    Log.i(TAG, "onClick: Entered OTP doesn't match!");
                    Toast.makeText(ForgotPasswordActivity.this, "Entered OTP doesn't match!\nPlease try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void sendOTP() {
        Log.i(TAG, "sendOTP: fired!");

        int min = 1000;
        int max = 9999;
        int random4DigitNo = ThreadLocalRandom.current().nextInt(min, max);
        Log.i(TAG, "sendOTP: random4DigitNo: " + random4DigitNo);
        resetPasswordViewModel.OTPSent = random4DigitNo;

        long numbers = Long.parseLong("91" + resetPasswordViewModel.mobileNumber);
        Log.i(TAG, "sendOTP: After adding 91: " + numbers);

        if (!resetPasswordViewModel.sendOTP(random4DigitNo, numbers)) {
            DialogUtil.showNoInternetToast(this);
        } else {
            DialogUtil.showProcessingInfoDialog(this);
        }
    }

    private void resetPassword() {
        Log.i(TAG, "resetPassword: fired!");

        if (!newPasswordTextInputEditText.getText().toString().matches("")
                && newPasswordTextInputEditText.getText().toString().length() >= 8) {
            if (!resetPasswordViewModel.resetPassword(newPasswordTextInputEditText.getText().toString(),
                    resetPasswordViewModel.getNumberValidateMutableLiveData().getValue().getType(),
                    resetPasswordViewModel.getNumberValidateMutableLiveData().getValue().getUserId())) {
                DialogUtil.showNoInternetToast(this);
            } else {
                DialogUtil.showProcessingInfoDialog(this);
            }
        } else {
            Log.i(TAG, "resetPassword: Password can't be empty & minimum length should be of 8 characters.");
            Toast.makeText(this, "Password can't be empty & minimum length should be of 8 characters.", Toast.LENGTH_SHORT).show();
        }
    }
}
