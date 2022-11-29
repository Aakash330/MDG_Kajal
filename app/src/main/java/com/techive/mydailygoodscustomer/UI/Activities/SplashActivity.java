package com.techive.mydailygoodscustomer.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i(TAG, "onCreate: fired!");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired!");

//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(TAG, "run: fired!");
//
////                Intent storeListIntent = new Intent(SplashActivity.this, StoreListActivity.class);
////                startActivity(storeListIntent);
//
//                Intent storeLocatorIntent = new Intent(SplashActivity.this, StoreLocatorActivity.class);
//                startActivity(storeLocatorIntent);
//
////                Intent storeIntent = new Intent(SplashActivity.this, ParentActivity.class);
////                ApplicationData.setDefaultStoreId(121);
////                storeIntent.putExtra("STORE_ID", 121);
////                storeIntent.putExtra("STORE_NAME", "Aamir Megamart");
////                startActivity(storeIntent);
//                finish();
//            }
//        }, 1300);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.d("TEST", "onCreate: activityManager.isBackgroundRestricted() = " + activityManager.isBackgroundRestricted());
            //D/TEST: onCreate: activityManager.isBackgroundRestricted() = false [EMULATOR OS 11]
        }

        //////////////////////////////////////////////////////////////////////
        /*FIREBASE TOKEN RETRIEVAL UPON EVERY LAUNCH*/
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.i(TAG, "onComplete: Inside Fetching Token OnComplete!");

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            //HAPPENED ON OS 6
                            //W/SplashActivity: Fetching FCM registration token failed
                            //    java.io.IOException: java.util.concurrent.ExecutionException: java.io.IOException: SERVICE_NOT_AVAILABLE
                            Intent storeLocatorIntent = new Intent(SplashActivity.this, StoreLocatorActivity.class);
                            startActivity(storeLocatorIntent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.d(TAG, "FIREBASE TOKEN: " + token);
//                        D/SplashActivity: FIREBASE TOKEN: fz9OEMBPQvmd4ifs283i9l:APA91bHmHLRo4TjzHZ1FmDAnJqyXWIQ8jrlwXnvfVrLm5gwubFi9V36TVgoutcSlNeLmW2CqVGrqY8CFVbl9cjN6y8q-yeEP6HRMoOQa_XiDf4reS7UeqJwRVbXXwn9HVF1T_EkdxGMS

                        SharedPreferencesManager sharedPreferencesManager;
                        SharedPreferences sharedPreferences;

                        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
                        sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();

                        Log.i(TAG, "onComplete: BEFORE, sharedPreferences.getAll(): " + sharedPreferences.getAll());
//                        I/SplashActivity: onComplete: BEFORE, sharedPreferences.getAll(): {USER_ID=126, PASSWORD=mdgcustomer, BUYER_NAME=Aamir, MOB_NO=7894561230, TOKEN=eFbX8PCuRte7UhDh7P4MeW:APA91bEI5PRapE-sHHIJZhAav4vlUQMmf_azj56AUgYexcqkSBa5zOGZJvq8V2UU886w4u40_HzJH3KKQm4vySdN4Asi6HKWo1pSE0Qlf7tDCgJjchT1z4zpl3jpD3t07NrPmj-k6Hi5}

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(SharedPreferencesManager.token, token);

                        editor.apply();
                        Log.i(TAG, "onComplete: AFTER, sharedPreferences.getAll(): " + sharedPreferences.getAll());
//                        I/SplashActivity: onComplete: AFTER, sharedPreferences.getAll(): {USER_ID=126, PASSWORD=mdgcustomer, BUYER_NAME=Aamir, MOB_NO=7894561230, TOKEN=fz9OEMBPQvmd4ifs283i9l:APA91bHmHLRo4TjzHZ1FmDAnJqyXWIQ8jrlwXnvfVrLm5gwubFi9V36TVgoutcSlNeLmW2CqVGrqY8CFVbl9cjN6y8q-yeEP6HRMoOQa_XiDf4reS7UeqJwRVbXXwn9HVF1T_EkdxGMS}

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Log.i(TAG, "run: Starting StoreLocatorActivity after Successfully retrieving token.");
                            Intent storeLocatorIntent = new Intent(SplashActivity.this, StoreLocatorActivity.class);
                            startActivity(storeLocatorIntent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }, 1200);

//                        Intent storeLocatorIntent = new Intent(SplashActivity.this, StoreLocatorActivity.class);
//                        startActivity(storeLocatorIntent);
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        finish();
                    }
                });
        //////////////////////////////////////////////////////////////////////

//        TextView mdgCustomerTextView = findViewById(R.id.mdgCustomerTextView);

//        mdgCustomerTextView.setAlpha(0f);
//        mdgCustomerTextView.animate().setDuration(1400).alpha(1f).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(TAG, "run: Inside textView runnable!");
//
//                Intent storeLocatorIntent = new Intent(SplashActivity.this, StoreLocatorActivity.class);
//                startActivity(storeLocatorIntent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                finish();
//            }
//        });
    }

}