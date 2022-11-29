package com.techive.mydailygoodscustomer.Util;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GPSUtil {
    private static final String TAG = "GPSUtil";
    private static final int REQUEST_CHECK_SETTINGS = 1;

    public GPSUtil() {
        Log.i(TAG, "GPSUtil: Empty Constructor fired!");
    }

    public /*static*/ MutableLiveData<ApiException> gpsMutableLiveData = new MutableLiveData<>();

    private /*static*/ Activity activity;

    public /*static*/ void checkGPS(Activity activity) {
        Log.i(TAG, "checkGPS: fired!");

//        GPSUtil.activity = activity;
        this.activity = activity;

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(LocationRequest.create());
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                Log.i(TAG, "onComplete: locationSettingsResponseTask.");

                try {
                    LocationSettingsResponse locationSettingsResponse = task.getResult(ApiException.class);
                    gpsMutableLiveData.setValue(null);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    // ...
                    Log.i(TAG, "onComplete: task.getResult(): \nALL SETTINGS ARE SATISFIED. INITIALIZE LOCATION REQUESTS.");

                } catch (ApiException exception) {
                    exception.printStackTrace();

                    gpsMutableLiveData.setValue(exception);
//                    switch (exception.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            Log.i(TAG, "onComplete: LocationSettingsStatusCodes.RESOLUTION_REQUIRED: ");
//                            gpsMutableLiveData.setValue(LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
//                            try {
//                                // Cast to a resolvable exception.
//                                ResolvableApiException resolvable = (ResolvableApiException) exception;
//                                // Show the dialog by calling startResolutionForResult(),
//                                // and check the result in onActivityResult().
////                                resolvable.startResolutionForResult(context, REQUEST_CHECK_SETTINGS);
//                            } catch (IntentSender.SendIntentException e) {
//                                // Ignore the error.
//                                Log.i(TAG, "onComplete: IntentSender.SendIntentException e.getMessage(): " + e.getMessage());
//                            } catch (ClassCastException e) {
//                                // Ignore, should be an impossible error.
//                                Log.i(TAG, "onComplete: ClassCastException e.getMessage(): " + e.getMessage());
//                            }
//                            break;
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            // Location settings are not satisfied. However, we have no way to fix the
//                            // settings so we won't show the dialog.
//                            // ...
//                            Log.i(TAG, "onComplete: LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: Not going to do anything.");
////                            gpsMutableLiveData.setValue(LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE);
//                            break;
//                    }
                }
            }
        });
        Log.i(TAG, "checkGPS: after onCompleteListener.");
    }

    public /*static*/ void showGpsOnDialog(ApiException apiException) {
        Log.i(TAG, "showGpsOnDialog: fired!");

        try {
            // Cast to a resolvable exception.
            ResolvableApiException resolvable = (ResolvableApiException) apiException;
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            // Ignore the error.
            Log.i(TAG, "showGpsOnDialog: IntentSender.SendIntentException e.getMessage(): " + e.getMessage());
        } catch (ClassCastException e) {
            // Ignore, should be an impossible error.
            Log.i(TAG, "showGpsOnDialog: ClassCastException e.getMessage(): " + e.getMessage());
        }
    }
}
