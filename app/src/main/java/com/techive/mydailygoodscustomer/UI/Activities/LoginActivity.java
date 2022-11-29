package com.techive.mydailygoodscustomer.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.LoginResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.LoginModel;
import com.techive.mydailygoodscustomer.Models.RegisterModel;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;
import com.techive.mydailygoodscustomer.ViewModels.LoginViewModel;

import java.util.concurrent.ThreadLocalRandom;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private NestedScrollView loginNestedScrollView, registerNestedScrollView;

//    private LinearLayoutCompat otpLinearLayoutCompat;

    private TextInputLayout stateTextInputLayout, cityTextInputLayout;

    private TextInputEditText loginMobNoTextInputEditText, loginPasswordTextInputEditText, firstNameTextInputEditText,
            lastNameTextInputEditText, regMobNoTextInputEditText, emailTextInputEditText, streetAddressTextInputEditText,
            landmarkTextInputEditText, zipcodeTextInputEditText, regPasswordTextInputEditText, regConfirmPasswordTextInputEditText,
            otpTextInputEditText;

    private MaterialButton loginMaterialButton, registerMaterialButton, sendOtpMaterialButton;

    private MaterialAutoCompleteTextView stateMaterialAutoCompleteTextView, cityMaterialAutoCompleteTextView;

    private MaterialTextView existingMemberMaterialTextView, newMemberMaterialTextView, forgotPasswordMaterialTextView;

    private MaterialCheckBox termsMaterialCheckBox;

    private SharedPreferencesManager sharedPreferencesManager;
    private SharedPreferences sharedPreferences;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login/Register");
        Log.i(TAG, "onCreate: fired!");

        initComponentViews();

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initAdapters();

        initObservers();

        initListeners();
    }

    private void initComponentViews() {
        Log.i(TAG, "initComponentViews: fired!");

        loginNestedScrollView = findViewById(R.id.loginNestedScrollView);
        registerNestedScrollView = findViewById(R.id.registerNestedScrollView);

//        otpLinearLayoutCompat = findViewById(R.id.otpLinearLayoutCompat);

        stateTextInputLayout = findViewById(R.id.stateTextInputLayout);
        cityTextInputLayout = findViewById(R.id.cityTextInputLayout);

        loginMobNoTextInputEditText = findViewById(R.id.loginMobNoTextInputEditText);
        loginPasswordTextInputEditText = findViewById(R.id.loginPasswordTextInputEditText);
        firstNameTextInputEditText = findViewById(R.id.firstNameTextInputEditText);
        lastNameTextInputEditText = findViewById(R.id.lastNameTextInputEditText);
        regMobNoTextInputEditText = findViewById(R.id.regMobNoTextInputEditText);
        emailTextInputEditText = findViewById(R.id.emailTextInputEditText);
        streetAddressTextInputEditText = findViewById(R.id.streetAddressTextInputEditText);
        landmarkTextInputEditText = findViewById(R.id.landmarkTextInputEditText);
        zipcodeTextInputEditText = findViewById(R.id.zipcodeTextInputEditText);
        regPasswordTextInputEditText = findViewById(R.id.regPasswordTextInputEditText);
        regConfirmPasswordTextInputEditText = findViewById(R.id.regConfirmPasswordTextInputEditText);
        otpTextInputEditText = findViewById(R.id.otpTextInputEditText);

        stateMaterialAutoCompleteTextView = findViewById(R.id.stateMaterialAutoCompleteTextView);
        cityMaterialAutoCompleteTextView = findViewById(R.id.cityMaterialAutoCompleteTextView);

        newMemberMaterialTextView = findViewById(R.id.newMemberMaterialTextView);
        existingMemberMaterialTextView = findViewById(R.id.existingMemberMaterialTextView);
        forgotPasswordMaterialTextView = findViewById(R.id.forgotPasswordMaterialTextView);

        loginMaterialButton = findViewById(R.id.loginMaterialButton);
        registerMaterialButton = findViewById(R.id.registerMaterialButton);
        sendOtpMaterialButton = findViewById(R.id.sendOtpMaterialButton);

        termsMaterialCheckBox = findViewById(R.id.termsMaterialCheckBox);

        /* Need to do this, to make links wrapped in string resource in textView accessible. */
        TextView readTermsMaterialTextView = findViewById(R.id.readTermsMaterialTextView);
        readTermsMaterialTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        stateMaterialAutoCompleteTextView.setAdapter(loginViewModel.statesArrayAdapter);
        cityMaterialAutoCompleteTextView.setAdapter(loginViewModel.cityArrayAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        loginViewModel.getLoginResponseMutableLiveData().observe(this, loginResponseObserver);
        loginViewModel.getCityListMutableLiveData().observe(this, cityListObserver);
        loginViewModel.getStateListMutableLiveData().observe(this, stateListObserver);
        loginViewModel.getOtpResponseMutableLiveData().observe(this, otpResponseObserver);
        loginViewModel.getUserValidityResponseMutableLiveData().observe(this, userValidityResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        newMemberMaterialTextView.setOnClickListener(memberOnClickListener);
        existingMemberMaterialTextView.setOnClickListener(memberOnClickListener);
        forgotPasswordMaterialTextView.setOnClickListener(memberOnClickListener);

        loginMaterialButton.setOnClickListener(this::login);
        registerMaterialButton.setOnClickListener(this::register);
        sendOtpMaterialButton.setOnClickListener(this::checkBuyerCredentialsValidity);

        stateMaterialAutoCompleteTextView.addTextChangedListener(stateTextWatcher);
        cityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);

        regMobNoTextInputEditText.addTextChangedListener(registeredMobileTextWatcher);

        stateMaterialAutoCompleteTextView.setOnItemClickListener(stateOnItemClickListener);
//        cityMaterialAutoCompleteTextView.setOnItemClickListener(cityOnItemClickListener);

//        streetAddressTextInputEditText.addTextChangedListener(streetAddressTextWatcher);
//        zipcodeTextInputEditText.addTextChangedListener(zipCodeTextWatcher);
    }

    private final Observer<LoginResponse> loginResponseObserver = new Observer<LoginResponse>() {
        @Override
        public void onChanged(LoginResponse loginResponse) {
            Log.i(TAG, "onChanged: LOGIN Observer fired!\nloginResponse: " + loginResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if (loginResponse.getError() == 200) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (loginViewModel.loginInitiated) {    //SAVING LOGIN DETAILS
//                    Toast.makeText(LoginActivity.this, "You are now Logged In!", Toast.LENGTH_SHORT).show();

                    editor.putString(SharedPreferencesManager.mobNo, loginViewModel.loginModel.getMobile());
                    editor.putString(SharedPreferencesManager.password, loginViewModel.loginModel.getPassword());
                    editor.putInt(SharedPreferencesManager.userId, loginResponse.getUser_id()); //BUYER ID
                    editor.putString(SharedPreferencesManager.buyerName, loginResponse.getName());

                    ApplicationData.setLoggedInBuyerId(loginResponse.getUser_id());
//                    finish();
                } else {        //SAVING REGISTRATION DETAILS
                    Toast.makeText(LoginActivity.this, "You have Registered Successfully!", Toast.LENGTH_SHORT).show();

                    editor.putString(SharedPreferencesManager.mobNo, loginViewModel.registerModel.getMobile());
                    editor.putString(SharedPreferencesManager.password, loginViewModel.registerModel.getPassword());
                    editor.putInt(SharedPreferencesManager.userId, loginResponse.getUser_id()); //BUYER ID
                    String fullName = loginViewModel.registerModel.getFname() + " " + loginViewModel.registerModel.getLname();
                    editor.putString(SharedPreferencesManager.buyerName, fullName);

                    ApplicationData.setLoggedInBuyerId(loginResponse.getUser_id());
                }
                editor.apply();
                finish();
            } else {
                Log.i(TAG, "onChanged: Somehow login/register was not processed. loginResponse.getMsg(): " + loginResponse.getMsg());
                Toast.makeText(LoginActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<CityList> cityListObserver = new Observer<CityList>() {
        @Override
        public void onChanged(CityList cityList) {
            Log.i(TAG, "onChanged: CityList Observer fired!\ncityList: " + cityList);

            if (cityList.getError() == 200) {
                loginViewModel.initCityDataInMaterialAutoCompleteTextView();

                loginViewModel.cityArrayAdapter.getFilter().filter("", null);
            } else {
                Log.i(TAG, "onChanged: Something went wrong while loading more City data.");
                Toast.makeText(LoginActivity.this, cityList.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<StateList> stateListObserver = new Observer<StateList>() {
        @Override
        public void onChanged(StateList stateList) {
            Log.i(TAG, "onChanged: StateList Observer fired!\nstateList: " + stateList);

            if (stateList.getError() == 200) {
                loginViewModel.initStateDataInMaterialAutoCompleteTextView();

                loginViewModel.statesArrayAdapter.getFilter().filter("", null);
            } else {
                Log.i(TAG, "onChanged: Something went wrong while loading more State data.");
                Toast.makeText(LoginActivity.this, stateList.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<OTPResponse> otpResponseObserver = new Observer<OTPResponse>() {
        @Override
        public void onChanged(OTPResponse otpResponse) {
            Log.i(TAG, "onChanged: OTP Response Observer fired!\notpResponse: " + otpResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if ("success".equals(otpResponse.getStatus())) {
                Toast.makeText(LoginActivity.this, "Please enter the OTP you have received!", Toast.LENGTH_SHORT).show();

                //WILL 0 OUT THE OTP SENT SO AS TO STATE INVALID AFTER 15 MINS.
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: fired! Zeroing out the OTP after 15 mins.");

                        loginViewModel.OTPSent = 0;
                    }
                }, 900000);     //15 mins

                //MY CHECKS.
            } else if ("Unsuccessful".equals(otpResponse.getStatus()) || "Failed".equals(otpResponse.getStatus())) {
                Log.i(TAG, "onChanged: Sending OTP failed!\t" + otpResponse);
                Toast.makeText(LoginActivity.this, "Couldn't send OTP.\nPlease try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<GeneralResponse> userValidityResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: User Validity Response Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse.getError() == 200) {    //VENDOR CREDENTIALS AVAILABLE.
                //PROCEED TO SEND OTP.
                sendOtp();
            } else if (generalResponse.getError() == 400) {     //VENDOR CREDENTIALS NOT AVAILABLE.
                Log.i(TAG, "onChanged: Vendor Credentials already taken!" +
                        "\nCan't Send OTP, Please enter another number.");
                DialogUtil.dismissProcessingInfoDialog();
                Toast.makeText(LoginActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "onChanged: Some problem occurred during Mob No Validation!\t" + generalResponse.getMsg());
                DialogUtil.dismissProcessingInfoDialog();
                Toast.makeText(LoginActivity.this, "Some problem occurred during Mob No Validation!\n"
                        + generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final View.OnClickListener memberOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");

            if (view.getId() == newMemberMaterialTextView.getId()) {
                registerNestedScrollView.setVisibility(View.VISIBLE);
                loginNestedScrollView.setVisibility(View.GONE);
            } else if (view.getId() == existingMemberMaterialTextView.getId()) {
                registerNestedScrollView.setVisibility(View.GONE);
                loginNestedScrollView.setVisibility(View.VISIBLE);
            } else if (view.getId() == forgotPasswordMaterialTextView.getId()) {
//                Toast.makeText(LoginActivity.this, "Coming Soon!", Toast.LENGTH_SHORT).show();

                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
            }
        }
    };

    private void login(View view) {
        Log.i(TAG, "login: clicked!");

        if (loginMobNoTextInputEditText.getText().toString().matches("") || loginPasswordTextInputEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Mobile Number & password required for login!", Toast.LENGTH_SHORT).show();
        } else {
            if (loginMobNoTextInputEditText.getText().toString().length() == 10) {
                //PREPARE A LOGIN MODEL
//                String hardwareAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//                Log.i(TAG, "login: hardwareAndroidId: " + hardwareAndroidId);

                String token = sharedPreferences.getString(SharedPreferencesManager.token, "");
                Log.i(TAG, "login: token: " + token);

//                token = (String) sharedPreferences.getAll().get(SharedPreferencesManager.token);
//                Log.i(TAG, "login: token again: " + token);

                if (!loginViewModel.login(new LoginModel(loginMobNoTextInputEditText.getText().toString(),
                        loginPasswordTextInputEditText.getText().toString(), token))) {
                    DialogUtil.showNoInternetToast(this);
                } else {
                    DialogUtil.showProcessingInfoDialog(this);
                }
            } else {
                Toast.makeText(this, "Mobile Number should be of 10 digits.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void register(View view) {
        Log.i(TAG, "register: clicked");

        if (firstNameTextInputEditText.getText().toString().matches("")) {
            Toast.makeText(this, "First Name is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lastNameTextInputEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Last Name is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regMobNoTextInputEditText.getText().toString().matches("") || regMobNoTextInputEditText.getText().toString().length() != 10) {
            Toast.makeText(this, "Mobile Number of 10 digits is required!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!regMobNoTextInputEditText.getText().toString().matches(loginViewModel.OPTSentOnNumber)) {
            Toast.makeText(this, "Please enter the same number on which OTP was requested!", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!emailTextInputEditText.getText().toString().matches("")) {
            if (!emailTextInputEditText.getText().toString().matches(emailPattern)) {
                Toast.makeText(this, "Valid Email is required!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!otpTextInputEditText.getText().toString().matches("")
                && Integer.parseInt(otpTextInputEditText.getText().toString()) == loginViewModel.OTPSent) {
            loginViewModel.otpVerified = true;
        } else {
            Log.i(TAG, "register: Please first Verify your Mobile Number through OTP.");
            Toast.makeText(this, "Please first Verify your Mobile Number through OTP.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (streetAddressTextInputEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Street Address is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zipcodeTextInputEditText.getText().toString().matches("") || zipcodeTextInputEditText.getText().toString().length() != 6) {
            Toast.makeText(this, "ZipCode of 6 digits is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (stateMaterialAutoCompleteTextView.getText().toString().matches("")) {
            Toast.makeText(this, "State is required!", Toast.LENGTH_LONG).show();
            return;
        }
        if (cityMaterialAutoCompleteTextView.getText().toString().matches("")) {
            Toast.makeText(this, "City is required!", Toast.LENGTH_LONG).show();
            return;
        }
        if (regPasswordTextInputEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regConfirmPasswordTextInputEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Password Confirmation is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!termsMaterialCheckBox.isChecked()) {
            Toast.makeText(this, "Please read & agree to the Terms & Conditions!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (regPasswordTextInputEditText.getText().toString().matches(regConfirmPasswordTextInputEditText.getText().toString())) {
            Log.i(TAG, "register: All Registration params satisfied. Proceeding to register.");

            DialogUtil.showProcessingInfoDialog(this);

            StringBuilder addressStringBuilder = new StringBuilder();
            addressStringBuilder.append(streetAddressTextInputEditText.getText().toString()).append(", ");
            addressStringBuilder.append(cityMaterialAutoCompleteTextView.getText().toString()).append(", ");
            addressStringBuilder.append(stateMaterialAutoCompleteTextView.getText().toString());
            Log.i(TAG, "register: addressStringBuilder.toString(): " + addressStringBuilder.toString());

            loginViewModel.getAddressFromLocationName(addressStringBuilder.toString(), new ReverseGeoCoderHandler());

            //PREPARE A REGISTER MODEL - DOING THIS IN THE REVERSE GEOCODER HANDLER.
//                if (!loginViewModel.register(new RegisterModel(firstNameTextInputEditText.getText().toString(),
//                        lastNameTextInputEditText.getText().toString(), regMobNoTextInputEditText.getText().toString(),
//                        emailTextInputEditText.getText().toString(), streetAddressTextInputEditText.getText().toString(),
//                        landmarkTextInputEditText.getText().toString(), , , , , zipcodeTextInputEditText.getText().toString(),
//                        regPasswordTextInputEditText.getText().toString()))) {
//                    DialogUtil.showNoInternetToast(this);
//                } else {
//                    DialogUtil.showProcessingInfoDialog(this);
//                }
        } else {
            Log.i(TAG, "register: Passwords don't match.");
            Toast.makeText(this, "Passwords mismatch.\nPlease check!", Toast.LENGTH_LONG).show();
        }
    }

    private final TextWatcher cityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: CITY fired! charSequence: " + charSequence);

            if (loginViewModel.selectedStateId != 0) {
                if (charSequence.length() >= 3) {
                    if (charSequence.length() <= 8) {
                        //WILL PREVENT FROM HITTING THE API AGAIN, WHEN THE DESIRED CITY IS SELECTED.
                        if (!loginViewModel.cityHashMap.containsKey(charSequence.toString())) {
                            if (!loginViewModel.getCityListByCityNameSegment(charSequence.toString(), loginViewModel.selectedStateId)) {
                                DialogUtil.showNoInternetToast(LoginActivity.this);
                            }
                        }
                    }
                    cityTextInputLayout.setError(null);
                } else {
                    cityTextInputLayout.setError("Enter at least 3 characters to begin search.");
                }
            } else {
                Log.i(TAG, "onTextChanged: Selected state id is 0.");
                Toast.makeText(LoginActivity.this, "Please first select any state!", Toast.LENGTH_SHORT).show();
                cityMaterialAutoCompleteTextView.removeTextChangedListener(cityTextWatcher);
                cityMaterialAutoCompleteTextView.setText("");
                cityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final TextWatcher stateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: STATE fired! charSequence: " + charSequence);

            if (charSequence.length() >= 3) {
                if (charSequence.length() <= 8) {
                    if (!loginViewModel.stateHashMap.containsKey(charSequence.toString())) {
                        if (!loginViewModel.getStateListByStateNameSegment(charSequence.toString())) {
                            DialogUtil.showNoInternetToast(LoginActivity.this);
                        }
                    }
                }
                stateTextInputLayout.setError(null);
            } else {
                stateTextInputLayout.setError("Enter at least 3 characters to begin search.");
                loginViewModel.selectedStateId = 0;
                cityMaterialAutoCompleteTextView.removeTextChangedListener(cityTextWatcher);
                cityMaterialAutoCompleteTextView.setText("");
                cityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);
                loginViewModel.cityArrayAdapter.clear();
                loginViewModel.cityArrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final TextWatcher registeredMobileTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: charSequence: (" + charSequence + ")");

            loginViewModel.otpVerified = false;

//            if (charSequence.length() == 10) {
//                otpLinearLayoutCompat.setVisibility(View.VISIBLE);
//            } else {
//                otpLinearLayoutCompat.setVisibility(View.GONE);
//            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final AdapterView.OnItemClickListener stateOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemClick: adapterView.getItemAtPosition(i): " + adapterView.getItemAtPosition(i));

            loginViewModel.selectedStateId = loginViewModel.stateHashMap.get(adapterView.getItemAtPosition(i));
            Log.i(TAG, "onItemSelected: loginViewModel.selectedStateId: " + loginViewModel.selectedStateId);
        }
    };

    private void checkBuyerCredentialsValidity(View view) {
        Log.i(TAG, "checkBuyerCredentialsValidity: fired!");

        String email = "", mobNo = "";

        if (emailTextInputEditText.getText() != null) {
            email = emailTextInputEditText.getText().toString();
        }
        if (regMobNoTextInputEditText.getText() != null) {
            mobNo = regMobNoTextInputEditText.getText().toString();
        }

        if (!mobNo.matches("") && mobNo.length() == 10) {
            if (!loginViewModel.userValidityCheck(email, mobNo)) {
                DialogUtil.showNoInternetToast(this);
            } else {
                DialogUtil.showProcessingInfoDialog(this);
            }
        } else {
            Log.i(TAG, "checkBuyerCredentialsValidity: Mob No required!");
//            Toast.makeText(this, "At least Valid Mobile Number is required to validate credentials.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Please Enter a Valid Mobile Number!", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendOtp() {
        Log.i(TAG, "sendOtp: fired!");

        int min = 10000;
        int max = 99999;
        int random5DigitNo = ThreadLocalRandom.current().nextInt(min, max);
        Log.i(TAG, "sendOtp: random5DigitNo: " + random5DigitNo);
        loginViewModel.OTPSent = random5DigitNo;
        loginViewModel.OPTSentOnNumber = regMobNoTextInputEditText.getText().toString();

        long numbers = Long.parseLong("91" + regMobNoTextInputEditText.getText().toString());
        Log.i(TAG, "sendOtp: After adding 91: " + numbers);

        if (!loginViewModel.sendOTP(random5DigitNo, numbers)) {
            DialogUtil.dismissProcessingInfoDialog();
            DialogUtil.showNoInternetToast(LoginActivity.this);
        }
    }

    private class ReverseGeoCoderHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i(TAG, "handleMessage: Inside ReverseGeoCoderHandler.");

            switch (msg.what) {
                case 1: {
//                    DialogUtil.dismissProcessingInfoDialog();
                    Bundle bundle = msg.getData();
                    String errorString = bundle.getString("ERROR");
                    Log.i(TAG, "handleMessage: errorString: " + errorString);
                    // handleMessage: errorString: null
//                    DialogUtil.dismissFetchingLocationDialog();
                    if (errorString != null) {
                        Log.i(TAG, "handleMessage: ErrorString NOT NULL.");
                        DialogUtil.dismissProcessingInfoDialog();
                        Toast.makeText(LoginActivity.this, "Please enter a valid store address!" +
                                "\nPlease check your Internet Connection!", Toast.LENGTH_LONG).show();
                    } else {
                        String latFromLoc = bundle.getString("LAT_FROM_LOC");
                        String longFromLoc = bundle.getString("LONG_FROM_LOC");
                        String currentStoreAddress = bundle.getString("CURRENT_ADDRESS");
                        Log.i(TAG, "handleMessage: currentStoreAddress: " + currentStoreAddress
                                + "\nlatFromLoc: " + latFromLoc + "\nlongFromLoc: " + longFromLoc);

                        if (latFromLoc.length() > 10) {
                            Log.i(TAG, "handleMessage: lat has length greater than 10.");
                            latFromLoc = latFromLoc.substring(0, 10);
                            Log.i(TAG, "handleMessage: After 10 chars substring, latFromLoc: " + latFromLoc);
                        }
                        if (longFromLoc.length() > 10) {
                            Log.i(TAG, "handleMessage: long has length greater than 10.");
                            longFromLoc = longFromLoc.substring(0, 10);
                            Log.i(TAG, "handleMessage: After 10 chars substring, longFromLoc: " + longFromLoc);
                        }

                        try {       //NULL POINTER EXCEPTION - EXTREMELY RARE CASE.
                            int stateId = loginViewModel.stateHashMap.get(stateMaterialAutoCompleteTextView.getText().toString());
                            int cityId = loginViewModel.cityHashMap.get(cityMaterialAutoCompleteTextView.getText().toString());

                            Log.i(TAG, "handleMessage: STATE: " + stateId);
                            Log.i(TAG, "handleMessage: CITY: " + cityId);

                            String token = sharedPreferences.getString(SharedPreferencesManager.token, SharedPreferencesManager.token);
                            Log.i(TAG, "handleMessage: registration, firebase token: " + token);

//                            DialogUtil.dismissProcessingInfoDialog();

                            //PREPARE A REGISTER MODEL
                            if (!loginViewModel.register(new RegisterModel(firstNameTextInputEditText.getText().toString(),
                                    lastNameTextInputEditText.getText().toString(), regMobNoTextInputEditText.getText().toString(),
                                    emailTextInputEditText.getText().toString(), streetAddressTextInputEditText.getText().toString(),
                                    landmarkTextInputEditText.getText().toString(), latFromLoc, longFromLoc, stateId, cityId,
                                    zipcodeTextInputEditText.getText().toString(), regPasswordTextInputEditText.getText().toString(),
                                    token))) {
                                DialogUtil.dismissProcessingInfoDialog();
                                DialogUtil.showNoInternetToast(LoginActivity.this);
                            }
                        } catch (Exception exception) {
                            Log.i(TAG, "handleMessage: Exception caught!\texception.getMessage(): " + exception.getMessage());
                            exception.printStackTrace();
                            DialogUtil.dismissProcessingInfoDialog();
                            Toast.makeText(LoginActivity.this, "Unable To Register Now." +
                                    "\nPlease try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }
}