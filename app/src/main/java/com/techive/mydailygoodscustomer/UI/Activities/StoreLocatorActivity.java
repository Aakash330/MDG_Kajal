package com.techive.mydailygoodscustomer.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Adapters.StoresListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.RateStoreModel;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName_Data;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName_Data_ReviewData;
import com.techive.mydailygoodscustomer.Models.StoreListByName;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.GPSUtil;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;
import com.techive.mydailygoodscustomer.ViewModels.StoreLocatorViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class StoreLocatorActivity extends AppCompatActivity implements StoresListRecyclerViewAdapter.OnStoresListRecyclerViewAdapterListener {
    private static final String TAG = "StoreLocatorActivity";

    private static final int REQUEST_CHECK_SETTINGS = 1, PERMISSIONS_REQUEST_CODE = 2, LOCATION_PERMISSION_REQUEST_CODE = 3,
            CALL_PERMISSION = 4;

    private SwipeRefreshLayout storeLocatorSwipeRefreshLayout;

    private MaterialButton openLoginMaterialButton, useCurrentLocationMaterialButton, ownGroceryStoreMaterialButton;

    private TextInputLayout searchByStoreNameTextInputLayout, searchByCityNameTextInputLayout;

    private MaterialAutoCompleteTextView searchByStoreNameMaterialAutoCompleteTextView, searchByCityNameMaterialAutoCompleteTextView;

    private RecyclerView visitedStoresRecyclerView;

    private MaterialTextView visitedStoresTitleMaterialTextView;

    private StoreLocatorViewModel storeLocatorViewModel;

    private GPSUtil gpsUtil;

    private SharedPreferencesManager sharedPreferencesManager;
    private SharedPreferences sharedPreferences;

    private String phoneNo;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        setTitle("Store Locator");
        Log.i(TAG, "onCreate: fired!");

        initComponentViews();

        storeLocatorViewModel = new ViewModelProvider(this).get(StoreLocatorViewModel.class);

        gpsUtil = new GPSUtil();

        initAdapters();

        initObservers();

        initListeners();

        requestRuntimePermissions();

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();

        Log.i(TAG, "onCreate: sharedPreferences.getAll().toString(): " + sharedPreferences.getAll().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fired!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired!");

        storeLocatorViewModel.getStoreListByCityNameMutableLiveData().observe(this, favStoresListObserver);

        //IF LOGGED IN, BUYER ID AVAILABLE, HIT THE FAV STORE LIST API,
        //OTHERWISE FAV STORE PART WILL BE GONE.
        if (sharedPreferences.getInt(SharedPreferencesManager.userId, 0) != 0) {
            Log.i(TAG, "onResume: Buyer already logged in! Retrieving Fav Stores List for buyer: "
                    + sharedPreferences.getInt(SharedPreferencesManager.userId, 0));

            /*CHANGE LOGIN BUTTON TO LOGOUT BUTTON.*/
            openLoginMaterialButton.setText("Logout");
            openLoginMaterialButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);

            ApplicationData.setLoggedInBuyerId(sharedPreferences.getInt(SharedPreferencesManager.userId, 0));

            if (!storeLocatorViewModel.getFavStoresList(sharedPreferences.getInt(SharedPreferencesManager.userId, 0))) {
                DialogUtil.showNoInternetToast(this);
            } else {
                DialogUtil.showLoadingDialog(this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");

        storeLocatorViewModel.getStoreListByCityNameMutableLiveData().removeObserver(favStoresListObserver);

        storeLocatorViewModel.setStoreListByCityNameMutableLiveDataValue(null);
        storeLocatorViewModel.setOrderAcceptResponseMutableLiveDataValue(null);
        if (storeLocatorViewModel.getCityListMutableLiveData().getValue() != null) {
            storeLocatorViewModel.setCityListMutableLiveDataValue(null);
        }
        if (storeLocatorViewModel.getStoreListByNameMutableLiveData().getValue() != null) {
            storeLocatorViewModel.setStoreListByNameMutableLiveDataValue(null);
        }
        if (storeLocatorViewModel.getSetFavStoreMutableLiveData().getValue() != null) {
            storeLocatorViewModel.setSetFavStoreMutableLiveDataValue(null);
        }
        if (storeLocatorViewModel.getStoreRatingResponseMutableLiveData().getValue() != null) {
            storeLocatorViewModel.setStoreRatingResponseMutableLiveDataValue(null);
        }

        if (storeLocatorViewModel.getLogoutResponseMutableLiveData().getValue() != null) {
            storeLocatorViewModel.setLogoutResponseMutableLiveData(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: fired!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Location Permission not granted upon request!");
                Toast.makeText(this, "Location Permission required for fetching nearby stores.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //PROCEED TO OPEN GPS
                Log.i(TAG, "onRequestPermissionsResult: Location Permission granted upon request. Proceeding to check for GPS...");
                gpsUtil.checkGPS(StoreLocatorActivity.this);
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Location permission denied upon request!");
                Toast.makeText(this, "Location Permission is required for fetching nearby stores!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CALL_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Call Permission granted upon request.");
                placeCall(phoneNo);
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Call Permission Denied Upon Request!");
                Toast.makeText(this, "Please Grant Call Permission to proceed with Calls.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: fired! requestCode: " + requestCode + "\tresultCode: " + resultCode);

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        // ...
                        Log.i(TAG, "onActivityResult: Activity.RESULT_OK: All changes were successfully made.");

                        //HIT FETCH LOCATION
                        if (!storeLocatorViewModel.fetchLocation()) {
                            Toast.makeText(StoreLocatorActivity.this, "Can't proceed without location permission." +
                                    "\nPlease provide location permission & try again!", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(StoreLocatorActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        } else {
                            DialogUtil.showFetchingLocationDialog(StoreLocatorActivity.this);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // ...
                        Log.i(TAG, "onActivityResult: Activity.RESULT_CANCELED: The user was asked to change settings, but chose not to.");
                        Toast.makeText(this, "Can't proceed without GPS." +
                                "\nPlease enable GPS to proceed or Manually follow the below options!", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void initComponentViews() {
        Log.i(TAG, "initComponentViews: fired!");

        storeLocatorSwipeRefreshLayout = findViewById(R.id.storeLocatorSwipeRefreshLayout);

        openLoginMaterialButton = findViewById(R.id.openLoginMaterialButton);
        useCurrentLocationMaterialButton = findViewById(R.id.useCurrentLocationMaterialButton);
        ownGroceryStoreMaterialButton = findViewById(R.id.ownGroceryStoreMaterialButton);

        searchByStoreNameTextInputLayout = findViewById(R.id.searchByStoreNameTextInputLayout);
        searchByCityNameTextInputLayout = findViewById(R.id.searchByCityNameTextInputLayout);

        searchByStoreNameMaterialAutoCompleteTextView = findViewById(R.id.searchByStoreNameMaterialAutoCompleteTextView);
        searchByCityNameMaterialAutoCompleteTextView = findViewById(R.id.searchByCityNameMaterialAutoCompleteTextView);

        visitedStoresRecyclerView = findViewById(R.id.visitedStoresRecyclerView);

        visitedStoresTitleMaterialTextView = findViewById(R.id.visitedStoresTitleMaterialTextView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        searchByStoreNameMaterialAutoCompleteTextView.setAdapter(storeLocatorViewModel.storeNamesArrayAdapter);
        searchByCityNameMaterialAutoCompleteTextView.setAdapter(storeLocatorViewModel.cityArrayAdapter);

        visitedStoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        visitedStoresRecyclerView.setAdapter(storeLocatorViewModel.storesListRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        gpsUtil.gpsMutableLiveData.observe(this, apiExceptionObserver);
        storeLocatorViewModel.getLocationResultMutableLiveData().observe(this, locationResultObserver);

        storeLocatorViewModel.getCityListMutableLiveData().observe(this, cityListObserver);
        storeLocatorViewModel.getStoreListByNameMutableLiveData().observe(this, storeListByNameObserver);
        storeLocatorViewModel.getStoreListByCityNameMutableLiveData().observe(this, favStoresListObserver);
        storeLocatorViewModel.getSetFavStoreMutableLiveData().observe(this, setFavStoreResponseObserver);
//        storeLocatorViewModel.getViewShopRatingMutableLiveData().observe(this, viewShopRatingObserver);
        storeLocatorViewModel.getStoreRatingResponseMutableLiveData().observe(this, storeRatingResponseObserver);
        storeLocatorViewModel.getOrderAcceptResponseMutableLiveData().observe(this, orderAcceptResponseObserver);

        storeLocatorViewModel.getLogoutResponseMutableLiveData().observe(this, logoutResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        storeLocatorSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        openLoginMaterialButton.setOnClickListener(this::proceedToLogin);
        useCurrentLocationMaterialButton.setOnClickListener(useCurrentLocationOnClickListener);
        ownGroceryStoreMaterialButton.setOnClickListener(this::redirectToOwnGroceryStore);

        searchByStoreNameMaterialAutoCompleteTextView.addTextChangedListener(storeTextWatcher);
        searchByStoreNameMaterialAutoCompleteTextView.setOnItemClickListener(storeNameOnItemClickListener);

        searchByCityNameMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);
        searchByCityNameMaterialAutoCompleteTextView.setOnItemClickListener(cityNameOnItemClickListener);

        storeLocatorViewModel.storesListRecyclerViewAdapter.setOnStoresListRecyclerViewAdapterListener(this);
    }

    private void requestRuntimePermissions() {
        Log.i(TAG, "requestRuntimePermissions: fired!");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        }
    }

    private void proceedToLogin(View view) {
        Log.i(TAG, "proceedToLogin: clicked!");

        if (openLoginMaterialButton.getText().toString().matches("Logout")) {

            if (!storeLocatorViewModel.logoutCustomer()) {
                DialogUtil.showNoInternetToast(this);
            } else {
                DialogUtil.showProcessingInfoDialog(this);
            }
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private final Observer<ApiException> apiExceptionObserver = new Observer<ApiException>() {
        @Override
        public void onChanged(ApiException apiException) {
            Log.i(TAG, "onChanged: StoreLocatorActivity, GPS Observer fired!");

            if (apiException == null) {     //GPS ALREADY ON
                //HIT FETCH LOCATION
                if (!storeLocatorViewModel.fetchLocation()) {
                    Toast.makeText(StoreLocatorActivity.this, "Can't proceed without location permission." +
                            "\nPlease provide location permission & try again!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(StoreLocatorActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    DialogUtil.showFetchingLocationDialog(StoreLocatorActivity.this);
                }
            } else {
                switch (apiException.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: {       //GPS CAN BE TURNED ON
                        gpsUtil.showGpsOnDialog(apiException);
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {       //LOCATION SETTINGS UNAVAILABLE
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        // ...
                        Log.i(TAG, "onComplete: LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: Not going to do anything.");
                        break;
                    }
                }
            }
        }
    };

    private final Observer<LocationResult> locationResultObserver = new Observer<LocationResult>() {
        @Override
        public void onChanged(LocationResult locationResult) {
            Log.i(TAG, "onChanged: LOCATION RESULT Observer fired!\nlocationResult.getLastLocation().toString(): " + locationResult.getLastLocation().toString());

            DialogUtil.dismissFetchingLocationDialog();

            //SEND LOCATION TO THE NEXT STORE LIST PAGE
            Intent storeListIntent = new Intent(StoreLocatorActivity.this, StoreListActivity.class);
            storeListIntent.putExtra("LATITUDE", locationResult.getLastLocation().getLatitude());
            storeListIntent.putExtra("LONGITUDE", locationResult.getLastLocation().getLongitude());
            startActivity(storeListIntent);
        }
    };

    private final Observer<CityList> cityListObserver = new Observer<CityList>() {
        @Override
        public void onChanged(CityList cityList) {
            Log.i(TAG, "onChanged: CITY LIST Observer fired!\ncityList: " + cityList);

            if (cityList != null) {
                if (cityList.getError() == 200) {
                    storeLocatorViewModel.initCityDataInMaterialAutoCompleteTextView();

                    storeLocatorViewModel.cityArrayAdapter.getFilter().filter("", null);

                    if (storeLocatorViewModel.cityHashMap.size() == 0) {
                        Log.i(TAG, "onChanged: No Matching Cities found!");
                        Toast.makeText(StoreLocatorActivity.this, "No Matching Cities found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i(TAG, "onChanged: Some Problem occurred while fetching City List data!");
                    Toast.makeText(StoreLocatorActivity.this, cityList.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer firing!");
            }
        }
    };

    private final Observer<StoreListByName> storeListByNameObserver = new Observer<StoreListByName>() {
        @Override
        public void onChanged(StoreListByName storeListByName) {
            Log.i(TAG, "onChanged: STORE LIST BY NAME Observer fired!\nstoreListByName: " + storeListByName);

            if (storeListByName != null) {
                if (storeListByName.getError() == 200) {
                    storeLocatorViewModel.initStoreNameDataInMaterialAutoCompleteTextView();

                    storeLocatorViewModel.storeNamesArrayAdapter.getFilter().filter("", null);

                    if (storeLocatorViewModel.storesHashMap.size() == 0) {
                        Log.i(TAG, "onChanged: No Matching stores found!");
                        Toast.makeText(StoreLocatorActivity.this, "No Matching stores found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i(TAG, "onChanged: Some problem occurred while fetching stores list data!");
                    Toast.makeText(StoreLocatorActivity.this, storeListByName.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<StoreListByCityName> favStoresListObserver = new Observer<StoreListByCityName>() {
        @Override
        public void onChanged(StoreListByCityName storeListByCityName) {
            Log.i(TAG, "onChanged: FAV STORES LIST Observer fired!\nstoreListByName: " + storeListByCityName);

            if (storeListByCityName != null) {

                DialogUtil.dismissLoadingDialog();

                if (storeListByCityName.getError() == 200) {
                    Log.i(TAG, "onChanged: Fav Stores Found!");
                    if (storeListByCityName.getData() != null && storeListByCityName.getData().size() > 0) {
                        visitedStoresTitleMaterialTextView.setVisibility(View.VISIBLE);
                        visitedStoresRecyclerView.setVisibility(View.VISIBLE);
                        storeLocatorViewModel.storesListRecyclerViewAdapter.setStoreListByCityName_dataList(storeListByCityName.getData());
                        Log.i(TAG, "onChanged: After setting Fav Stores Data in RecyclerView!");
                    }
                } else {
                    Log.i(TAG, "onChanged: Some problem occurred while fetching fav stores list data!");
                    Toast.makeText(StoreLocatorActivity.this, storeListByCityName.getMsg(), Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.i(TAG, "onChanged: Not doing anything since this is the observer firing 1st time.");
            }
        }
    };

    private final Observer<GeneralResponse> setFavStoreResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: SET FAV STORE Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (generalResponse.getError() == 200) {
                    Toast.makeText(StoreLocatorActivity.this, "Favourite Store Updated Successfully!", Toast.LENGTH_SHORT).show();

                    //TEMPORARILY COMMENTED OUT TO DISPLAY BANNER.
//                storeLocatorViewModel.storesListRecyclerViewAdapter.notifyItemChanged(storeLocatorViewModel.toBeDefaultStorePosition);
                    storeLocatorViewModel.storesListRecyclerViewAdapter.notifyFavStoreUpdate(
                            storeLocatorViewModel.toBeDefaultStorePosition, storeLocatorViewModel.lastCheckedPosition);
                } else {
                    Log.i(TAG, "onChanged: Something went wrong during set fav store response!");
                    Toast.makeText(StoreLocatorActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    storeLocatorViewModel.storesListRecyclerViewAdapter.notifyItemChanged(storeLocatorViewModel.lastCheckedPosition);
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

//    private final Observer<ViewShopRating> viewShopRatingObserver = new Observer<ViewShopRating>() {
//        @Override
//        public void onChanged(ViewShopRating viewShopRating) {
//            Log.i(TAG, "onChanged: VIEW ALL RATINGS OF SHOP fired!\nviewShopRating: " + viewShopRating);
//
//            DialogUtil.dismissLoadingDialog();
//
//            if (viewShopRating.getError() == 200) {
//                showAllRatingsBottomSheetDialog(viewShopRating.getData());
//            } else {
//                Log.i(TAG, "onChanged: Something went wrong while fetching All Ratings of a shop.");
//                Toast.makeText(StoreLocatorActivity.this, viewShopRating.getMsg(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

    private final Observer<GeneralResponse> storeRatingResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: STORE RATING Response Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (generalResponse.getError() == 200) {
                    bottomSheetDialog.dismiss();

                    DialogUtil.showCustomSnackbar(StoreLocatorActivity.this, storeLocatorSwipeRefreshLayout,
                            "You have Successfully Rated that store!", -1);

                    /*WILL NEED TO NOTIFY THE ADAPTER FOR REVIEW DATA CHANGES.*/
                    storeLocatorViewModel.storesListRecyclerViewAdapter.notifyStoreReviewUpdate(storeLocatorViewModel.toBeNotifiedStoreReviewPosition,
                            new StoreListByCityName_Data_ReviewData(storeLocatorViewModel.rateStoreModel.getRate_value(),
                                    storeLocatorViewModel.rateStoreModel.getReviewsText()));
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while rating the store!");
                    Toast.makeText(StoreLocatorActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st Time Observer fired!");
            }
        }
    };

    private final Observer<OrderAcceptResponse> orderAcceptResponseObserver = new Observer<OrderAcceptResponse>() {
        @Override
        public void onChanged(OrderAcceptResponse orderAcceptResponse) {
            Log.i(TAG, "onChanged: ORDER ACCEPTANCE Observer fired!\norderAcceptResponse: " + orderAcceptResponse);

            if (orderAcceptResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (orderAcceptResponse.getError() == 200) {
                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getStatus() == 0) {
                        Log.i(TAG, "onChanged: Vendor Accepting Orders, Proceeding to open Store.");

                        //OPEN SHOP PAGE ON SELECTING THE STORE.
                        ApplicationData.setDefaultStoreId(storeLocatorViewModel.toBeOpenedStoreId);
                        ApplicationData.setDefaultStoreName(storeLocatorViewModel.toBeOpenedStoreName);
                        Intent storeIntent = new Intent(StoreLocatorActivity.this, ParentActivity.class);
                        startActivity(storeIntent);
                        finish();

                    } else {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't Open Store!");
                        DialogUtil.showCustomSnackbar(StoreLocatorActivity.this, storeLocatorSwipeRefreshLayout,
                                "Sorry, Can't Open Store.\nVendor is not accepting any Orders at the moment!\nPlease try again later!", -1);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Order Acceptance!");
                    Toast.makeText(StoreLocatorActivity.this, orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: NOt doing anything since this is the observer firing 1st time.");
            }
        }
    };

    private final Observer<GeneralResponse> logoutResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: LOGOUT Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (generalResponse.getError() == 200 || generalResponse.getError() == 400) {
                    //PROCEED TO LOGOUT FROM DEVICE
                    logout();
                } else {
                    Log.i(TAG, "onChanged: Something went wrong during logout vendor.");
                    Toast.makeText(StoreLocatorActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: Null Observer fired!");
            }
        }
    };


    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            //HIT THE FAV STORE LIST API.
            if (sharedPreferences.getInt(SharedPreferencesManager.userId, 0) != 0) {
                Log.i(TAG, "onCreate: Buyer already logged in! Retrieving Fav Stores List.");

                if (!storeLocatorViewModel.getFavStoresList(sharedPreferences.getInt(SharedPreferencesManager.userId, 0))) {
                    DialogUtil.showNoInternetToast(StoreLocatorActivity.this);
                } else {
                    DialogUtil.showLoadingDialog(StoreLocatorActivity.this);
                }
            } else {
                Toast.makeText(StoreLocatorActivity.this, "Please Login to view your Favourite Stores!", Toast.LENGTH_SHORT).show();
            }

            storeLocatorSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private final AdapterView.OnItemClickListener storeNameOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemSelected: adapterView.getItemAtPosition(i=" + i + "): " + adapterView.getItemAtPosition(i));

            //OPEN SHOP PAGE ON SELECTING THE STORE.
            Log.i(TAG, "onItemClick: BEFORE, storeLocatorViewModel.toBeOpenedStoreId: " + storeLocatorViewModel.toBeOpenedStoreId);
            storeLocatorViewModel.toBeOpenedStoreId = storeLocatorViewModel.storesHashMap.get(adapterView.getItemAtPosition(i).toString());
            storeLocatorViewModel.toBeOpenedStoreName = adapterView.getItemAtPosition(i).toString();
            Log.i(TAG, "onItemClick: AFTER, storeLocatorViewModel.toBeOpenedStoreId: " + storeLocatorViewModel.toBeOpenedStoreId);

            /* NEW FLOW AS OF 22-6-22 */
//            if (!storeLocatorViewModel.checkVendorOrderAcceptance()) {
//                DialogUtil.showNoInternetToast(StoreLocatorActivity.this);
//            } else {
//                DialogUtil.showProcessingInfoDialog(StoreLocatorActivity.this);
//            }

            //MIGHT NEED NEW API - Logic changed on 23-8-22
            ApplicationData.setDefaultStoreId(storeLocatorViewModel.toBeOpenedStoreId);
            ApplicationData.setDefaultStoreName(storeLocatorViewModel.toBeOpenedStoreName);
            Intent storeIntent = new Intent(StoreLocatorActivity.this, ParentActivity.class);
            startActivity(storeIntent);
            finish();
        }
    };

    private final TextWatcher storeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: fired! charSequence: " + charSequence);

            if (charSequence.length() >= 3) {
                if (charSequence.length() <= 9) {
//                    if (!charSequence.toString().matches(storeLocatorViewModel.selectedStoreName)) {
                    if (!storeLocatorViewModel.storesHashMap.containsKey(charSequence.toString())) {
                        if (!storeLocatorViewModel.getStoreListByStoreNameSegment(charSequence.toString())) {
                            DialogUtil.showNoInternetToast(StoreLocatorActivity.this);
                        }
                    }
                }
                searchByStoreNameTextInputLayout.setError(null);
            } else {
                searchByStoreNameTextInputLayout.setError("Enter at least 3 characters to begin search.");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final AdapterView.OnItemClickListener cityNameOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemSelected: adapterView.getItemAtPosition(i=" + i + "): " + adapterView.getItemAtPosition(i));

//            storeLocatorViewModel.selectedCityName = adapterView.getItemAtPosition(i).toString();

            //OPEN STORE LIST PAGE ON SELECTING THE STORE.
            Intent storeListIntent = new Intent(StoreLocatorActivity.this, StoreListActivity.class);
            storeListIntent.putExtra("CITY_NAME", adapterView.getItemAtPosition(i).toString());
            storeListIntent.putExtra("CITY_ID", storeLocatorViewModel.cityHashMap.get(adapterView.getItemAtPosition(i).toString()));
            startActivity(storeListIntent);
        }
    };

    private final TextWatcher cityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: fired! charSequence: " + charSequence);

            if (charSequence.length() >= 3) {
                if (charSequence.length() <= 8) {
//                    if (!charSequence.toString().matches(storeLocatorViewModel.selectedCityName)) {
                    if (!storeLocatorViewModel.cityHashMap.containsKey(charSequence.toString())) {
                        if (!storeLocatorViewModel.getCityListByCityNameSegment(charSequence.toString())) {
                            DialogUtil.showNoInternetToast(StoreLocatorActivity.this);
                        }
                    }
                }
                searchByCityNameTextInputLayout.setError(null);
            } else {
                searchByCityNameTextInputLayout.setError("Enter at least 3 characters to begin search.");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final View.OnClickListener useCurrentLocationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: Use Current Location Button fired!");

            if (ActivityCompat.checkSelfPermission(StoreLocatorActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onClick: Location Permission not granted, requesting now...");
                ActivityCompat.requestPermissions(StoreLocatorActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                //PROCEED TO OPEN GPS
                Log.i(TAG, "onClick: Location permission already granted! Checking for GPS...");
                gpsUtil.checkGPS(StoreLocatorActivity.this);
            }
        }
    };

    private void redirectToOwnGroceryStore(View view) {
        Log.i(TAG, "redirectToOwnGroceryStore: fired!");

//        Intent productDetailsIntent = new Intent(this, ProductDetailsActivity.class);
//        productDetailsIntent.putExtra("PRODUCT_ID", 39644);
//        productDetailsIntent.putExtra("PRODUCT_ID", 37388);
//        startActivity(productDetailsIntent);
//        Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();

        Intent downloadSuggestIntent = new Intent();
        downloadSuggestIntent.setAction(Intent.ACTION_VIEW);
//        downloadSuggestIntent.setData(Uri.parse("http://play.google.com/store/apps"));
        downloadSuggestIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.techive.mydailygoods"));
        startActivity(downloadSuggestIntent);
    }

    private void logout() {
        Log.i(TAG, "logout: fired!");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(TAG, "logout: BEFORE, sharedPreferences.getAll().toString(): " + sharedPreferences.getAll().toString());

        editor.remove(SharedPreferencesManager.userId);
        editor.remove(SharedPreferencesManager.password);
        editor.remove(SharedPreferencesManager.mobNo);
        editor.remove(SharedPreferencesManager.buyerName);

//            editor.clear();
        editor.commit();
//        editor.apply();
        Log.i(TAG, "logout: AFTER PARTIAL CLEAR, sharedPreferences.getAll().toString(): " + sharedPreferences.getAll().toString());

        ApplicationData.setLoggedInBuyerId(0);

        Log.i(TAG, "logout: You Have Logged Out Successfully!");
        Toast.makeText(this, "You have Logged Out Successfully!", Toast.LENGTH_SHORT).show();

        visitedStoresTitleMaterialTextView.setVisibility(View.GONE);
        visitedStoresRecyclerView.setVisibility(View.GONE);
        storeLocatorViewModel.storesListRecyclerViewAdapter.setStoreListByCityName_dataList(null);

        openLoginMaterialButton.setText(getResources().getString(R.string.to_login));
        openLoginMaterialButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.cf_card_person, null), null, null, null);

        //RESET LAST CHECKED POSITION IN FAV STORES RECYCLER VIEW ADAPTER TO -1.
        storeLocatorViewModel.storesListRecyclerViewAdapter.resetLastCheckedPosition();
    }

    @Override
    public void onStoreCall(String[] mobNoArray) {
        Log.i(TAG, "onStoreCall: fired!");

        callOrWhatsappNumber(mobNoArray, true);
    }

    @Override
    public void onStoreWhatsapp(String[] whatsappArray) {
        Log.i(TAG, "onStoreWhatsapp: fired!");

        callOrWhatsappNumber(whatsappArray, false);
    }

    @Override
    public void onStoreWebsite(String website) {
        Log.i(TAG, "onStoreWebsite: fired!");

        if (!website.startsWith("https://") && !website.startsWith("http://")) {
            website = "https://" + website;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        startActivity(browserIntent);
    }

    @Override
    public void onStoreTiming(int storeId) {
        Log.i(TAG, "onStoreTiming: fired! storeId: " + storeId);

        String storeName = "";

        StringBuilder stringBuilder = new StringBuilder();
        for (StoreListByCityName_Data storeListByCityName_data : storeLocatorViewModel.getStoreListByCityNameMutableLiveData().getValue().getData()) {
            if (storeListByCityName_data.getStore_id() == storeId) {
                Log.i(TAG, "onStoreTiming: Store Id matched!");

                storeName = storeListByCityName_data.getStore();

                if (storeListByCityName_data.getMon_o().matches("0") || storeListByCityName_data.getMon_o().matches("Closed!")) {
                    stringBuilder.append("Mon: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getMon_o().matches("24 hours")) {
                    stringBuilder.append("Mon: ").append(storeListByCityName_data.getMon_o()).append("\n");
                } else {
                    stringBuilder.append("Mon: ").append(storeListByCityName_data.getMon_o()).append(" - ")
                            .append(storeListByCityName_data.getMon_c()).append("\n");
                }

                if (storeListByCityName_data.getTue_o().matches("0") || storeListByCityName_data.getTue_o().matches("Closed!")) {
                    stringBuilder.append("Tue: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getTue_o().matches("24 hours")) {
                    stringBuilder.append("Tue: ").append(storeListByCityName_data.getTue_o()).append("\n");
                } else {
                    stringBuilder.append("Tue: ").append(storeListByCityName_data.getTue_o()).append(" - ")
                            .append(storeListByCityName_data.getTue_c()).append("\n");
                }

                if (storeListByCityName_data.getWed_o().matches("0") || storeListByCityName_data.getWed_o().matches("Closed!")) {
                    stringBuilder.append("Wed: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getWed_o().matches("24 hours")) {
                    stringBuilder.append("Wed: ").append(storeListByCityName_data.getWed_o()).append("\n");
                } else {
                    stringBuilder.append("Wed: ").append(storeListByCityName_data.getWed_o()).append(" - ")
                            .append(storeListByCityName_data.getWed_c()).append("\n");
                }

                if (storeListByCityName_data.getThu_o().matches("0") || storeListByCityName_data.getThu_o().matches("Closed!")) {
                    stringBuilder.append("Thu: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getThu_o().matches("24 hours")) {
                    stringBuilder.append("Thu: ").append(storeListByCityName_data.getThu_o()).append("\n");
                } else {
                    stringBuilder.append("Thu: ").append(storeListByCityName_data.getThu_o()).append(" - ")
                            .append(storeListByCityName_data.getThu_c()).append("\n");
                }

                if (storeListByCityName_data.getFri_o().matches("0") || storeListByCityName_data.getFri_o().matches("Closed!")) {
                    stringBuilder.append("Fri: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getFri_o().matches("24 hours")) {
                    stringBuilder.append("Fri: ").append(storeListByCityName_data.getFri_o()).append("\n");
                } else {
                    stringBuilder.append("Fri: ").append(storeListByCityName_data.getFri_o()).append(" - ")
                            .append(storeListByCityName_data.getFri_c()).append("\n");
                }

                if (storeListByCityName_data.getSat_o().matches("0") || storeListByCityName_data.getSat_o().matches("Closed!")) {
                    stringBuilder.append("Sat: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getSat_o().matches("24 hours")) {
                    stringBuilder.append("Sat: ").append(storeListByCityName_data.getSat_o()).append("\n");
                } else {
                    stringBuilder.append("Sat: ").append(storeListByCityName_data.getSat_o()).append(" - ")
                            .append(storeListByCityName_data.getSat_c()).append("\n");
                }

                if (storeListByCityName_data.getSun_o().matches("0") || storeListByCityName_data.getSun_o().matches("Closed!")) {
                    stringBuilder.append("Sun: ").append("Closed!").append("\n");
                } else if (storeListByCityName_data.getSun_o().matches("24 hours")) {
                    stringBuilder.append("Sun: ").append(storeListByCityName_data.getSun_o()).append("\n");
                } else {
                    stringBuilder.append("Sun: ").append(storeListByCityName_data.getSun_o()).append(" - ")
                            .append(storeListByCityName_data.getSun_c()).append("\n");
                }
                break;
            }
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
//        materialAlertDialogBuilder.setView(R.layout.dialog_loading);
        materialAlertDialogBuilder.setCancelable(true);
        materialAlertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_access_time_24, null));
        materialAlertDialogBuilder.setTitle(storeName + " - STORE TIMINGS");
        materialAlertDialogBuilder.setMessage(stringBuilder.toString());
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        };
        materialAlertDialogBuilder.setNegativeButton("Cancel", onClickListener);
        AlertDialog timingsAlertDialog = materialAlertDialogBuilder.create();
        timingsAlertDialog.show();
    }

    @Override
    public void onStoreSetFav(int storeId, int toBeDefaultStorePosition, int lastCheckedPosition) {
        Log.i(TAG, "onStoreSetFav: fired! storeId: " + storeId + "\ttoBeDefaultStorePosition: "
                + toBeDefaultStorePosition + "\tlastCheckedPosition: " + lastCheckedPosition);

        storeLocatorViewModel.toBeDefaultStorePosition = toBeDefaultStorePosition;
        storeLocatorViewModel.lastCheckedPosition = lastCheckedPosition;

        if (!storeLocatorViewModel.setFavStore(sharedPreferences.getInt(SharedPreferencesManager.userId, 0), storeId)) {
            DialogUtil.showNoInternetToast(this);
        } else {
            DialogUtil.showProcessingInfoDialog(this);
        }
    }

    @Override
    public void onStoreClick(int storeId, String storeName) {
        Log.i(TAG, "onStoreClick: storeId: " + storeId + "\tstoreName: " + storeName);

//        ApplicationData.setDefaultStoreId(storeId);
//
//        Intent parentIntent = new Intent(this, ParentActivity.class);
//        startActivity(parentIntent);
//        finish();

        storeLocatorViewModel.toBeOpenedStoreId = storeId;
        storeLocatorViewModel.toBeOpenedStoreName = storeName;

        //ALSO SAVING TO SHARED PREFERENCES
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedPreferencesManager.defaultStoreId, storeLocatorViewModel.toBeOpenedStoreId);
        editor.apply();
        Log.i(TAG, "onStoreClick: sharedPreferences.getAll().toString(): " + sharedPreferences.getAll().toString());

        //MIGHT NEED NEW API - Logic changed on 23-8-22
        ApplicationData.setDefaultStoreId(storeLocatorViewModel.toBeOpenedStoreId);
        ApplicationData.setDefaultStoreName(storeLocatorViewModel.toBeOpenedStoreName);
        Intent storeIntent = new Intent(StoreLocatorActivity.this, ParentActivity.class);
        startActivity(storeIntent);
        finish();

//        if (!storeLocatorViewModel.checkVendorOrderAcceptance()) {
//            DialogUtil.showNoInternetToast(StoreLocatorActivity.this);
//        } else {
//            DialogUtil.showProcessingInfoDialog(StoreLocatorActivity.this);
//        }
    }

    @Override
    public void onViewAllRatings(int storeId) {     //NOT VIEWING ALL THE RATINGS FROM FAV STORES PAGE.
        Log.i(TAG, "onViewAllRatings: fired! storeId: " + storeId);

        /*WILL HIT THE API FOR GETTING ALL THE RATINGS. WHEN RETRIEVED, */
        /*WILL SHOW A BOTTOM SHEET DIALOG FOR ALL THE RATINGS.*/
//        if (!storeLocatorViewModel.viewAllRatingsOfShop(storeId)) {
//            DialogUtil.showNoInternetToast(this);
//        } else {
//            DialogUtil.showLoadingDialog(this);
//        }
    }

    @Override
    public void rateStore(int storeId, String storeName, StoreListByCityName_Data_ReviewData storeListByCityName_data_reviewData,
                          int toBeNotifiedPosition) {      //WILL ACTUALLY RATE THE STORE FROM HERE.
        Log.i(TAG, "rateStore: fired! storeId: " + storeId + "\tstoreName: " + storeName
                + "\tstoreListByCityName_data_reviewData: " + storeListByCityName_data_reviewData + "\ttoBeNotifiedPosition" + toBeNotifiedPosition);

        storeLocatorViewModel.toBeNotifiedStoreReviewPosition = toBeNotifiedPosition;
        /*WILL DISPLAY A DIALOG PROMPTING THE USER TO PROCEED WITH RATING & REVIEW.*/
        showRatingBottomSheetDialog(storeId, storeName, storeListByCityName_data_reviewData);
    }

    /*SHOW RATE US BOTTOM SHEET DIALOG.*/
    private void showRatingBottomSheetDialog(int storeId, String storeName, StoreListByCityName_Data_ReviewData storeListByCityName_data_reviewData) {
        Log.i(TAG, "showRatingBottomSheetDialog: fired!");

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_rate_store);

        MaterialTextView rateStoreNameMaterialTextView = bottomSheetDialog.findViewById(R.id.rateStoreNameMaterialTextView);
        AppCompatRatingBar rateStoreAppCompatRatingBar = bottomSheetDialog.findViewById(R.id.rateStoreAppCompatRatingBar);
        TextInputEditText reviewStoreTextInputEditText = bottomSheetDialog.findViewById(R.id.reviewStoreTextInputEditText);
        MaterialButton rateMaterialButton = bottomSheetDialog.findViewById(R.id.rateMaterialButton);

        //DISPLAY ANY EXISTING REVIEW DATA
        if (storeListByCityName_data_reviewData != null) {
            rateStoreAppCompatRatingBar.setRating(storeListByCityName_data_reviewData.getStar());
            reviewStoreTextInputEditText.setText(storeListByCityName_data_reviewData.getReviewMsg());
        }

        rateStoreNameMaterialTextView.setText(storeName);

        rateMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Rate material Button clicked!");
                float rating = rateStoreAppCompatRatingBar.getRating();
                String review = reviewStoreTextInputEditText.getText().toString();

                if (rating > 0.0 && !review.matches("")) {
                    Log.i(TAG, "onClick: Proceeding to Rate the store!");

                    /*HIT RATING API*/
                    storeLocatorViewModel.rateStoreModel = new RateStoreModel(ApplicationData.getLoggedInBuyerId(), storeId, rating, review);

                    if (!storeLocatorViewModel.rateStore(storeLocatorViewModel.rateStoreModel)) {
                        DialogUtil.showNoInternetToast(StoreLocatorActivity.this);
                    } else {
                        DialogUtil.showProcessingInfoDialog(StoreLocatorActivity.this);
                    }
                } else {
                    Log.i(TAG, "onClick: Rating & Review Required!");
                    Toast.makeText(StoreLocatorActivity.this, "Both Rating & Review is Mandatory!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    /*SHOW ALL RATINGS BOTTOM SHEET DIALOG*/
//    private void showAllRatingsBottomSheetDialog(List<ViewShopRating_Data> viewShopRating_dataList) {
//        Log.i(TAG, "showAllRatingsBottomSheetDialog: fired!");
//
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_all_ratings);
//
//        AllRatingsRecyclerViewAdapter allRatingsRecyclerViewAdapter = new AllRatingsRecyclerViewAdapter();
//
//        RecyclerView allRatingsRecyclerView = bottomSheetDialog.findViewById(R.id.allRatingsRecyclerView);
//
//        allRatingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        allRatingsRecyclerView.setAdapter(allRatingsRecyclerViewAdapter);
//
//        allRatingsRecyclerViewAdapter.setViewShopRating_dataList(viewShopRating_dataList);
//
//        bottomSheetDialog.show();
//    }

    private void callOrWhatsappNumber(String[] mobNoArray, boolean placeCall) {
        Log.i(TAG, "callOrWhatsappNumber: fired! placeCall: " + placeCall);

        DialogInterface.OnClickListener dialogInterfaceOnClickListener = null;

        ArrayList<String> numbersArrayList = new ArrayList<>();
        //FOR LOOP TO AVOID SHOWING NULL OR EMPTY VALUES AS MULTIPLE OPTIONS.
        for (String multiplePhoneNumber : mobNoArray) {
            Log.i(TAG, "callOrWhatsappNumber: (" + multiplePhoneNumber + ")");
            if (multiplePhoneNumber != null && !multiplePhoneNumber.trim().matches("")) {
                numbersArrayList.add(multiplePhoneNumber);
            }
        }
        Log.i(TAG, "callOrWhatsappNumber: numbersArrayList.size(): " + numbersArrayList.size());

        String[] finalMultiplePhoneNumbers = numbersArrayList.toArray(new String[]{});
        Log.i(TAG, "callOrWhatsappNumber: finalMultiplePhoneNumbers: " + Arrays.toString(finalMultiplePhoneNumbers));
        dialogInterfaceOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "onClick: position: " + i);

                if (placeCall) {
                    placeCall(finalMultiplePhoneNumbers[i]);
                } else {
                    whatsappChatCode(finalMultiplePhoneNumbers[i]);
                }
            }
        };

        new MaterialAlertDialogBuilder(this)
                .setTitle(placeCall ? "Select Store Mobile Number" : "Select Store Whatsapp Number")
                .setItems(finalMultiplePhoneNumbers, dialogInterfaceOnClickListener)
                .setIcon(placeCall ? R.drawable.ic_call_24 : R.drawable.whatsapp_icon)
                .show();
    }

    //CALLING CODE
    private void placeCall(String phoneNo) {
        Log.i(TAG, "placeCall: fired!");

        if (phoneNo.matches(""))
            Toast.makeText(getApplicationContext(), "No Phone Number to call!", Toast.LENGTH_SHORT).show();
        else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNo));
            //callIntent.setData(Uri.parse("tel:" + "+919264233010"));

            this.phoneNo = phoneNo;

            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION", "Permission not granted, Requesting permission...");
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
            } else {
                /*PackageManager - Class for retrieving various kinds of information related to the application packages that are currently installed on the device.
                 You can find this class through Context#getPackageManager.*/
                /*resolveActivity() - Returns the Activity component that should be used to handle this intent. The appropriate component is determined based on the
                 information in the intent.*/

                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    /*This condition check verifies if the device has an app to handle the intent activity. It'll only work after permission has been provided.
                    It probably does so by checking all the apps' manifest file for intent-filter.
                    * For ex: If the device doesn't have any app to handle phone calls, then we'll get null (possible in tablets).*/
                    Log.i("CALL_INTENT_RESOLVE_ACT", String.valueOf(callIntent.resolveActivity(getPackageManager())));
                    //CALL_INTENT_RESOLVE_ACT: ComponentInfo{com.android.server.telecom/com.android.server.telecom.components.UserCallActivity}

                    Log.i("PERMISSION", "Permission ALREADY granted! Placing call...");
                    /*NEEDS PERMISSION: <uses-permission android:name="android.permission.CALL_PHONE"/>*/
                    Toast.makeText(this, "Calling " + phoneNo, Toast.LENGTH_LONG).show();
                    startActivity(callIntent);
                } else {
                    Toast.makeText(this, "Can't find application to place call!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //WHATSAPP CODE
    private void whatsappChatCode(String phoneNo) {
        Log.i(TAG, "whatsappChatCode: fired! phoneNo: " + phoneNo);

        Intent whatsappIntent = new Intent();
        whatsappIntent.setAction(Intent.ACTION_VIEW);
        whatsappIntent.setPackage("com.whatsapp");

        if (phoneNo.length() == 10) {
            Log.i(TAG, "whatsappChatCode: phoneNo length is 10.");
            phoneNo = "+91" + phoneNo;
        }
        try {
//            whatsappIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone="
//                    + phoneNo + "&text=" + URLEncoder.encode("Testing through Uri Parse.", "UTF-8")));
            whatsappIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNo));
            startActivity(whatsappIntent);
        } catch (Exception e) {
            Log.i(TAG, "whatsappChatCode: e.getMessage(): " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Whatsapp Not Installed!\nPlease Install Whatsapp!", Toast.LENGTH_SHORT).show();
            //Taking the user to playStore.
            Intent downloadSuggestIntent = new Intent();
            downloadSuggestIntent.setAction(Intent.ACTION_VIEW);
            downloadSuggestIntent.setData(Uri.parse("http://play.google.com/store/apps"));
            startActivity(downloadSuggestIntent);
        }
    }
}