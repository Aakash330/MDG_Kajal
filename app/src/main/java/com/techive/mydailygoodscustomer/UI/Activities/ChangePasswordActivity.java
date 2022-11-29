package com.techive.mydailygoodscustomer.UI.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.PasswordModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;
import com.techive.mydailygoodscustomer.ViewModels.ChangePasswordViewModel;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";

    private MaterialTextView buyerNameMobMaterialTextView;

    private TextInputEditText oldPasswordTextInputEditText, newPasswordTextInputEditText, confirmPasswordTextInputEditText;

    private MaterialButton changePasswordMaterialButton;

    private ChangePasswordViewModel changePasswordViewModel;

    private SharedPreferencesManager sharedPreferencesManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");
        Log.i(TAG, "onCreate: fired!");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponentViews();

        changePasswordViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();

        String buyerName = sharedPreferences.getString(SharedPreferencesManager.buyerName, "");
        String buyerMobile = sharedPreferences.getString(SharedPreferencesManager.mobNo, "");
        Log.i(TAG, "onCreate: buyerName: " + buyerName + "\tbuyerMobile: " + buyerMobile);

        buyerNameMobMaterialTextView.setText("Hi, " + buyerName + "\n" + buyerMobile);
    }

    // this event will enable the back function to the button, upon press
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

        buyerNameMobMaterialTextView = findViewById(R.id.buyerNameMobMaterialTextView);

        oldPasswordTextInputEditText = findViewById(R.id.oldPasswordTextInputEditText);
        newPasswordTextInputEditText = findViewById(R.id.newPasswordTextInputEditText);
        confirmPasswordTextInputEditText = findViewById(R.id.confirmPasswordTextInputEditText);
        changePasswordMaterialButton = findViewById(R.id.changePasswordMaterialButton);
    }

    private void initAdapters() {
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        changePasswordViewModel.getChangePasswordResponseMutableLiveData().observe(this, changePasswordObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        changePasswordMaterialButton.setOnClickListener(onClickListener);
    }

    private final Observer<GeneralResponse> changePasswordObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: CHANGE PASSWORD Observer fired!\ngeneralResponse: " + generalResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if (generalResponse.getError() == 200) {
                DialogUtil.showCustomSnackbar(ChangePasswordActivity.this, changePasswordMaterialButton, "Password Changed Successfully!", -1);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Log.i(TAG, "run: Inside run of Change Password Observer.");
                    finish();
                }, 1700);
            } else {
                Log.i(TAG, "onChanged: Something went wrong while Changing password!");
                Toast.makeText(ChangePasswordActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: ChangePassword clicked!");

            String oldPass = oldPasswordTextInputEditText.getText().toString();
            String newPass = newPasswordTextInputEditText.getText().toString();
            String confirmPass = confirmPasswordTextInputEditText.getText().toString();

            if (!newPass.matches("") && !oldPass.matches("") && newPass.equals(confirmPass)) {
                Log.i(TAG, "changePassword: newPass = confirmPass: " + newPass + " = " + confirmPass);

                // HIT API TO RETRIEVE OLD PASSWORD
                // UPON MATCH WITH THE ENTERED OLD PASSWORD, UPDATE THE EXISTING PASSWORD.
                PasswordModel passwordModel = new PasswordModel("", ApplicationData.getLoggedInBuyerId(), oldPass, newPass, confirmPass);
                if (!changePasswordViewModel.updatePassword(passwordModel)) {
                    DialogUtil.showNoInternetToast(ChangePasswordActivity.this);
                } else {
                    DialogUtil.showProcessingInfoDialog(ChangePasswordActivity.this);
                }
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            }
        }
    };

}