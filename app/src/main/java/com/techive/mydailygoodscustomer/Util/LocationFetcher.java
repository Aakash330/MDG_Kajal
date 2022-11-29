package com.techive.mydailygoodscustomer.Util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocationFetcher {

    private static final String TAG = "LocationFetcher";
    private static final int REQUEST_CHECK_SETTINGS = 1;

    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private MutableLiveData<LocationResult> locationResultMutableLiveData;
    private MutableLiveData<Location> locationMutableLiveData;

    private Geocoder geocoder;

//    private SharedPreferences sharedPreferences;

//    private PragatiDatabase pragatiDatabase;
//    private LocationDao locationDao;

//    private Executor executor;

//    private LocationEntity locationEntity;

    public LocationFetcher(Context context) {
        Log.i(TAG, "LocationFetcher: Constructor fired!");

        this.context = context;

        geocoder = new Geocoder(context, Locale.getDefault());


//        pragatiDatabase = PragatiDatabase.getInstance(context);
//        locationDao = pragatiDatabase.locationDao();

//        executor = Executors.newSingleThreadExecutor();

        locationResultMutableLiveData = new MutableLiveData<>();
        locationMutableLiveData = new MutableLiveData<>();

        initLocationSettings();

        /*pragatiDatabase = PragatiDatabase.getInstance(applicationContext);
        locationDao = pragatiDatabase.locationDao();

        executor = Executors.newSingleThreadExecutor();*/
    }

    private void initLocationSettings() {
        Log.i(TAG, "initLocationSettings: fired!");

//        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
//        sharedPreferences = sharedPreferencesManager.getEmployeeSharedPreferences();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * 10);
        locationRequest.setFastestInterval(1000 * 5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i(TAG, "onLocationResult: Location retrieved! Publishing result to LiveData.");

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                locationResultMutableLiveData.postValue(locationResult);
            }
        };
    }

    // Call from outside to get location.
    public boolean fetchLocation() {
        Log.i(TAG, "fetchLocation: fired!");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        return true;
    }

    // Call from outside to get last known location.
    public boolean fetchLastKnownLocation() {
        Log.i(TAG, "fetchLastKnownLocation: fired!");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                Log.i(TAG, "onComplete: fired! Last Known Location might have been retrieved!");
                if (task.getResult() != null) {
                    Log.i(TAG, "onComplete: fired! task.getResult().toString(): " + task.getResult().toString());
                }

//                if (task.getResult() == null) {
//                    //RETRIEVE LOCATION FROM SHARED PREFERENCES.
//                    if (!sharedPreferences.getString(SharedPreferencesManager.latitude, "").matches("")) {
//                        String strLatitude = sharedPreferences.getString(SharedPreferencesManager.latitude, "");
//                        double doubleLatitude = Double.parseDouble(strLatitude);
//                        location.setLatitude(doubleLatitude);
//
//                        String strLongitude = sharedPreferences.getString(SharedPreferencesManager.longitude, "");
//                        double doubleLongitude = Double.parseDouble(strLongitude);
//                        location.setLongitude(doubleLongitude);
//                        Log.i(TAG, "onSuccess: after getting from sharedPreferences, location.toString(): " + location.toString());
//                    }
//                }
            }
        });

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i(TAG, "onSuccess: fired! location.toString(): " + location.toString());

                //IN SOME RARE SITUATIONS THIS CAN BE NULL.
                if (location == null) {
                    //RETRIEVE LOCATION FROM SHARED PREFERENCES.
//                    if (!sharedPreferences.getString(SharedPreferencesManager.latitude, "").matches("")) {
//                        String strLatitude = sharedPreferences.getString(SharedPreferencesManager.latitude, "");
//                        double doubleLatitude = Double.parseDouble(strLatitude);
//                        location.setLatitude(doubleLatitude);
//
//                        String strLongitude = sharedPreferences.getString(SharedPreferencesManager.longitude, "");
//                        double doubleLongitude = Double.parseDouble(strLongitude);
//                        location.setLongitude(doubleLongitude);
//                        Log.i(TAG, "onSuccess: after getting from sharedPreferences, location.toString(): " + location.toString());
//                    }
                }
                Log.i(TAG, "onSuccess: location.toString(): " + location.toString());
                locationMutableLiveData.postValue(location);
            }
        });
        return true;
    }

    public ArrayList<String> getLocationInfo(LocationResult locationResult) {
        Log.i(TAG, "getLocationInfo: fired!");

        ArrayList<String> countStateCityNamesArrayList = new ArrayList<>();

        /*I/PunchInViewModel: getLocationInfo: fired!
W/System.err: java.io.IOException: grpc failed
W/System.err:     at android.location.Geocoder.getFromLocation(Geocoder.java:136)
        at com.techive.pragatidailyreporting.ViewModels.PunchInViewModel.getLocationInfo(PunchInViewModel.java:87)
        --exception at addressList = geocoder.getFromLocation(,,); line*/

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(),
                    locationResult.getLastLocation().getLongitude(), 3);
//            double dummyLat = 28.988440, dummyLong = 77.726220;
//            addressList = geocoder.getFromLocation(dummyLat, dummyLong, 1);
            if (addressList != null) {
                Log.i(TAG, "getLocationInfo: addressList.size(): " + addressList.size());
                Log.i(TAG, "getLocationInfo: addressList.toString(): " + addressList.toString());
                //I/LocationFetcher: getLocationInfo: addressList.size(): 1     < WHEN MAX RESULTS WAS 1 >
                //I/LocationFetcher: getLocationInfo: addressList.toString(): [Address[addressLines=[0:"J57V+CQF, Alamganj, Patna, Bihar 800007, India"],feature=J57V+CQF,admin=Bihar,sub-admin=Patna,locality=Patna,thoroughfare=null,postalCode=800007,countryCode=IN,countryName=India,hasLatitude=true,latitude=25.613582299999997,hasLongitude=true,longitude=85.19445429999999,phone=null,url=null,extras=null]]
                //I/LocationFetcher: getLocationInfo: addressList.size(): 3     < WHEN MAX RESULTS WAS 3 >
                //I/LocationFetcher: getLocationInfo: addressList.toString(): [Address[addressLines=[0:"J57V+CQF, Alamganj, Patna, Bihar 800007, India"],feature=J57V+CQF,admin=Bihar,sub-admin=Patna,locality=Patna,thoroughfare=null,postalCode=800007,countryCode=IN,countryName=India,hasLatitude=true,latitude=25.613582299999997,hasLongitude=true,longitude=85.19445429999999,phone=null,url=null,extras=null], Address[addressLines=[0:"Alamganj Main Rd, Muhammadpur, Patna, Bihar 800007, India"],feature=Alamganj Main Road,admin=Bihar,sub-admin=Patna,locality=Patna,thoroughfare=Alamganj Main Road,postalCode=800007,countryCode=IN,countryName=India,hasLatitude=true,latitude=25.6133329,hasLongitude=true,longitude=85.194718,phone=null,url=null,extras=null], Address[addressLines=[0:"Alamganj, Patna, Bihar 800007, India"],feature=Alamganj,admin=Bihar,sub-admin=Patna,locality=Patna,thoroughfare=null,postalCode=800007,countryCode=IN,countryName=India,hasLatitude=true,latitude=25.6144923,hasLongitude=true,longitude=85.1965287,phone=null,url=null,extras=null]]
            } else {
                Log.i(TAG, "getLocationInfo: addressList is null! No address retrieved from latitude & longitude.");
            }
        } catch (IOException e) {
            Log.i(TAG, "getLocationInfo: Exception caught during retrieving location name.");
            e.printStackTrace();
        }

        if (addressList != null && addressList.size() > 0) {
            Log.i(TAG, "getLocationInfo: addressList.get(0).getCountryName(): " + addressList.get(0).getCountryName());
            Log.i(TAG, "getLocationInfo: addressList.get(0).getAdminArea(): " + addressList.get(0).getAdminArea());
            Log.i(TAG, "getLocationInfo: addressList.get(0).getLocality(): " + addressList.get(0).getLocality());
            for (int i = 0; i < addressList.size(); i++) {
                Log.i(TAG, "getLocationInfo: addressList.get(i).getAddressLine(0): " + addressList.get(i).getAddressLine(0));
            }
            //I/LocationFetcher: getLocationInfo: addressList.get(0).getCountryName(): India
            //    getLocationInfo: addressList.get(0).getAdminArea(): Bihar
            //    getLocationInfo: addressList.get(0).getLocality(): Patna
            countStateCityNamesArrayList.add(addressList.get(0).getCountryName());
            countStateCityNamesArrayList.add(addressList.get(0).getAdminArea());
            countStateCityNamesArrayList.add(addressList.get(0).getLocality());
            for (int i = 0; i < addressList.size(); i++) {
                Log.i(TAG, "getLocationInfo: addressList.get(i).getAddressLine(0): " + addressList.get(i).getAddressLine(0));
                if (!addressList.get(i).getAddressLine(0).contains("+")) {
                    countStateCityNamesArrayList.add(addressList.get(i).getAddressLine(0));
                    break;
                }
            }
        }
        return countStateCityNamesArrayList;
        // If exception occurs during retrieving location info, then an empty "countStateCityNamesArrayList" will be returned.
    }

    public void getAddressFromLocationName(String locationName, final Handler handler) {
        Log.i(TAG, "getAddressFromLocation: fired!");

        final String[] latitudeFromLocationName = { null };
        final String[] longitudeFromLocationName = { null };
        final String[] stateFromLocationName = { null };
        final String[] cityFromLocationName = { null };

        Thread thread = new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "run: Inside run of thread in getAddressFromLocationName!");

                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationName, 5);
                    if (addressList != null && addressList.size() > 0) {
                        Log.i(TAG, "run: addressList.toString(): " + addressList.toString());
                        latitudeFromLocationName[0] = String.valueOf(addressList.get(0).getLatitude());
                        longitudeFromLocationName[0] = String.valueOf(addressList.get(0).getLongitude());
                        stateFromLocationName[0] = addressList.get(0).getAdminArea();
                        cityFromLocationName[0] = addressList.get(0).getSubAdminArea();
                        Log.i(TAG, "run: latitude: " + latitudeFromLocationName[0]
                                + " longitude: " + longitudeFromLocationName[0]);
                        /*I/DisplayProfileViewModel: run: Inside run of thread in getAddressFromLocationName!
                        I/DisplayProfileViewModel: run: addressList.toString(): [Address[addressLines=[0:"Sector 12, Faridabad, Haryana, India"],feature=Sector 12,admin=Haryana,sub-admin=Faridabad,locality=Faridabad,thoroughfare=null,postalCode=null,countryCode=IN,countryName=India,hasLatitude=true,latitude=28.3842547,hasLongitude=true,longitude=77.32535349999999,phone=null,url=null,extras=null]]
                        I/DisplayProfileViewModel: run: latitude: 28.3842547 longitude: 77.32535349999999*/

                    }
                } catch (IOException e) {
                    Log.i(TAG, "run: Unable to connect to Geocoder!");
                    e.printStackTrace();
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (latitudeFromLocationName[0] != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("LAT_FROM_LOC", latitudeFromLocationName[0]);
                        bundle.putString("LONG_FROM_LOC", longitudeFromLocationName[0]);
                        bundle.putString("STATE_FROM_LOC", stateFromLocationName[0]);
                        bundle.putString("CITY_FROM_LOC", cityFromLocationName[0]);
                        bundle.putString("CURRENT_ADDRESS", locationName);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("ERROR", "Unable to get lat-long from this address.");
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        Log.i(TAG, "getAddressFromLocationName: before thread start!");
        thread.start();
        Log.i(TAG, "getAddressFromLocationName: after thread start!");
    }

//    public void insertLocationEntryInLocalDB(LocationEntity locationEntity) {
//        Log.i(TAG, "insertLocationEntryInLocalDB: fired!");
//        this.locationEntity = locationEntity;
//        executor.execute(insertLocationEntryInLocalDB);
//    }

    public MutableLiveData<LocationResult> getLocationResultMutableLiveData() {
        return locationResultMutableLiveData;
    }

    public MutableLiveData<Location> getLocationMutableLiveData() {
        return locationMutableLiveData;
    }

    //FOR SAVING DATA IN LOCAL DB
//    Runnable insertLocationEntryInLocalDB = new Runnable() {
//        @Override
//        public void run() {
//            Log.i(TAG, "run: Inside INSERT LOCATION ENTRY IN LOCAL DB Runnable!");
//
//            locationDao.insertLocationData(locationEntity);
//            Log.i(TAG, "run: After inserting location data in local DB.");
//        }
//    };
}
