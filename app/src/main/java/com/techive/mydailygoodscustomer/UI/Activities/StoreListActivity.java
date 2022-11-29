package com.techive.mydailygoodscustomer.UI.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Adapters.AllRatingsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.StoresListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName_Data;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName_Data_ReviewData;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;
import com.techive.mydailygoodscustomer.Models.ViewShopRating_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.ViewModels.StoreListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoreListActivity extends AppCompatActivity implements StoresListRecyclerViewAdapter.OnStoresListRecyclerViewAdapterListener {
    private static final String TAG = "StoreListActivity";

    private SwipeRefreshLayout storeListSwipeRefreshLayout;
    private RecyclerView storeListRecyclerView;

    private StoreListViewModel storeListViewModel;

    private static final int CALL_PERMISSION = 1;

    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        Log.i(TAG, "onCreate: fired!");
        setTitle("Stores List");

        initComponentViews();

        storeListViewModel = new ViewModelProvider(this).get(StoreListViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        //GET CITY NAME THROUGH INTENT.
        if (getIntent().hasExtra("CITY_NAME")) {
            String receivedCityName = getIntent().getStringExtra("CITY_NAME");
            int receivedCityId = getIntent().getIntExtra("CITY_ID", 0);

            if (!storeListViewModel.getStoreListByCityName(receivedCityName, receivedCityId)) {
                DialogUtil.showNoInternetToast(this);
            } else {
                DialogUtil.showLoadingDialog(this);
            }
        }

        //GET LAT LNG THROUGH INTENT.
        if (getIntent().hasExtra("LATITUDE")) {
            double latitude = getIntent().getDoubleExtra("LATITUDE", 0);
            double longitude = getIntent().getDoubleExtra("LONGITUDE", 0);

            if (!storeListViewModel.getStoreListByLatLng(String.valueOf(latitude), String.valueOf(longitude))) {
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

        storeListViewModel.setStoreListByCityNameMutableLiveData(null);
        storeListViewModel.setOrderAcceptResponseMutableLiveData(null);
        if (storeListViewModel.getViewShopRatingMutableLiveData().getValue() != null) {
            storeListViewModel.setViewShopRatingMutableLiveData(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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

    private void initComponentViews() {
        Log.i(TAG, "initComponentViews: fired!");

        storeListSwipeRefreshLayout = findViewById(R.id.storeListSwipeRefreshLayout);
        storeListRecyclerView = findViewById(R.id.storeListRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        storeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeListRecyclerView.setAdapter(storeListViewModel.storesListRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        storeListViewModel.getStoreListByCityNameMutableLiveData().observe(this, storeListByCityNameObserver);
        storeListViewModel.getViewShopRatingMutableLiveData().observe(this, viewShopRatingObserver);
        storeListViewModel.getOrderAcceptResponseMutableLiveData().observe(this, orderAcceptResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        storeListSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        storeListViewModel.storesListRecyclerViewAdapter.setOnStoresListRecyclerViewAdapterListener(this);
    }

    //USING THE SAME OBSERVER FOR BOTH STORE NAME BY CITY & BY LAT-LNG.
    private final Observer<StoreListByCityName> storeListByCityNameObserver = new Observer<StoreListByCityName>() {
        @Override
        public void onChanged(StoreListByCityName storeListByCityName) {
            Log.i(TAG, "onChanged: STORE LIST BY CITY NAME/LAT LNG Observer fired!\nstoreListByCityName: " + storeListByCityName);

            if (storeListByCityName != null) {
                DialogUtil.dismissLoadingDialog();

                if (storeListByCityName.getError() == 200) {
                    storeListViewModel.storesListRecyclerViewAdapter.setStoreListByCityName_dataList(storeListByCityName.getData());
                    if (storeListByCityName.getData().size() == 0) {
                        DialogUtil.showCustomSnackbar(StoreListActivity.this, storeListSwipeRefreshLayout,
                                "No Stores Found! Please try a different city/location!", -2);
                    } else {
                        DialogUtil.dismissCustomSnackBar();
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while getting store list data.");
                    Toast.makeText(StoreListActivity.this, storeListByCityName.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
            }
        }
    };

    private final Observer<ViewShopRating> viewShopRatingObserver = new Observer<ViewShopRating>() {
        @Override
        public void onChanged(ViewShopRating viewShopRating) {
            Log.i(TAG, "onChanged: VIEW ALL RATINGS OF SHOP fired!\nviewShopRating: " + viewShopRating);

            if (viewShopRating != null) {
                DialogUtil.dismissLoadingDialog();

                if (viewShopRating.getError() == 200) {
                    showAllRatingsBottomSheetDialog(viewShopRating.getData(), viewShopRating.getAverage());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while fetching All Ratings of a shop.");
                    Toast.makeText(StoreListActivity.this, viewShopRating.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
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
                        ApplicationData.setDefaultStoreId(storeListViewModel.toBeOpenedStoreId);
                        ApplicationData.setDefaultStoreName(storeListViewModel.toBeOpenedStoreName);
                        Intent storeIntent = new Intent(StoreListActivity.this, ParentActivity.class);
                        startActivity(storeIntent);
                        finish();

                    } else {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't Open Store!");
                        DialogUtil.showCustomSnackbar(StoreListActivity.this, storeListSwipeRefreshLayout,
                                "Sorry, Can't Open Store.\nVendor is not accepting any Orders at the moment!\nPlease try again later!", -1);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Order Acceptance!");
                    Toast.makeText(StoreListActivity.this, orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            if (getIntent().hasExtra("CITY_NAME")) {
                Log.i(TAG, "onRefresh: Refreshing to get city based stores data!");
                if (!storeListViewModel.getStoreListByCityName(getIntent().getStringExtra("CITY_NAME"), getIntent().getIntExtra("CITY_ID", 0))) {
                    DialogUtil.showNoInternetToast(StoreListActivity.this);
                } else {
                    DialogUtil.showLoadingDialog(StoreListActivity.this);
                }
            }

            if (getIntent().hasExtra("LATITUDE")) {
                Log.i(TAG, "onRefresh: Refreshing to get lat-lng based stores data!");
//                if (!storeListViewModel.getStoreListByLatLng("25.6146715", "85.1962226")) {   //PATNA
                if (!storeListViewModel.getStoreListByLatLng(String.valueOf(getIntent().getDoubleExtra("LATITUDE", 0)),
                        String.valueOf(getIntent().getDoubleExtra("LONGITUDE", 0)))) {
                    DialogUtil.showNoInternetToast(StoreListActivity.this);
                } else {
                    DialogUtil.showLoadingDialog(StoreListActivity.this);
                }
            }

            storeListSwipeRefreshLayout.setRefreshing(false);
        }
    };

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
        for (StoreListByCityName_Data storeListByCityName_data : storeListViewModel.getStoreListByCityNameMutableLiveData().getValue().getData()) {
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
        Log.i(TAG, "onStoreSetFav: fired!");
    }

    @Override
    public void onStoreClick(int storeId, String storeName) {
        Log.i(TAG, "onStoreClick: storeId: " + storeId + "\tstoreName: " + storeName);

//        ApplicationData.setDefaultStoreId(storeId);
//
//        Intent parentIntent = new Intent(this, ParentActivity.class);
//        startActivity(parentIntent);

        storeListViewModel.toBeOpenedStoreId = storeId;
        storeListViewModel.toBeOpenedStoreName = storeName;

        //MIGHT NEED NEW API - Logic changed on 23-8-22
        ApplicationData.setDefaultStoreId(storeListViewModel.toBeOpenedStoreId);
        ApplicationData.setDefaultStoreName(storeListViewModel.toBeOpenedStoreName);
        Intent storeIntent = new Intent(StoreListActivity.this, ParentActivity.class);
        startActivity(storeIntent);
        finish();

//        if (!storeListViewModel.checkVendorOrderAcceptance()) {
//            DialogUtil.showNoInternetToast(StoreListActivity.this);
//        } else {
//            DialogUtil.showProcessingInfoDialog(StoreListActivity.this);
//        }
    }

    @Override
    public void onViewAllRatings(int storeId) {
        Log.i(TAG, "onViewAllRatings: fired! storeId: " + storeId);

        if (!storeListViewModel.viewAllRatingsOfShop(storeId)) {
            DialogUtil.showNoInternetToast(this);
        } else {
            DialogUtil.showLoadingDialog(this);
        }
    }

    @Override
    public void rateStore(int storeId, String storeName, StoreListByCityName_Data_ReviewData storeListByCityName_data_reviewData,
                          int toBeNotifiedPosition) {      //MERELY WRITTEN. WON'T ACTUALLY DO ANYTHING.
        Log.i(TAG, "rateStore: fired! storeId: " + storeId);
    }

    private void showAllRatingsBottomSheetDialog(List<ViewShopRating_Data> viewShopRating_dataList, float average) {
        Log.i(TAG, "showAllRatingsBottomSheetDialog: fired!");

//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_all_ratings);

        AllRatingsRecyclerViewAdapter allRatingsRecyclerViewAdapter = new AllRatingsRecyclerViewAdapter();

        MaterialTextView storeRatingAverageMaterialTextView = bottomSheetDialog.findViewById(R.id.storeRatingAverageMaterialTextView);
        RecyclerView allRatingsRecyclerView = bottomSheetDialog.findViewById(R.id.allRatingsRecyclerView);

        allRatingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allRatingsRecyclerView.setAdapter(allRatingsRecyclerViewAdapter);

        allRatingsRecyclerViewAdapter.setViewShopRating_dataList(viewShopRating_dataList);

        storeRatingAverageMaterialTextView.setText(getString(R.string.average) + " " + average);

        bottomSheetDialog.show();
    }

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