package com.techive.mydailygoodscustomer.Util;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.techive.mydailygoodscustomer.R;

public class MDGCustomerApp extends Application {
    private static final String TAG = "MDGCustomerApp";

//    protected static final String channelId = "mdgCustomerFirebaseNotification";

    private SharedPreferencesManager sharedPreferencesManager;
    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: fired!");

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();
        Log.i(TAG, "onCreate: Before, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
        ApplicationData.setDefaultStoreId(sharedPreferences.getInt(SharedPreferencesManager.defaultStoreId, 0));
        Log.i(TAG, "onCreate: After, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());

        setUpActivityListener();

        createMyNotificationChannels();
    }

    private void createMyNotificationChannels() {
        Log.i(TAG, "createMyNotificationChannels: fired!");

//        String channelId = getString(R.string.firebase_notification_channel_id);

        /*Create a notification channel if OS is Oreo or above it. */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    channelId,
//                    "MDG Customer Notification",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
////            NotificationChannel notificationChannel2 = new NotificationChannel(
////                    channelId2,
////                    "Upload Selfie Channel",
////                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            /*Can use either of the below techniques.*/
//            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//            notificationManager.createNotificationChannel(notificationChannel);
////            notificationManager.createNotificationChannel(notificationChannel2);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "createNotificationChannel: Creating Notification channel on OS >= Oreo. " + Build.VERSION.SDK_INT);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelId = getString(R.string.default_notification_channel_id);
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setUpActivityListener()
    {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Log.w("state","onAcvityCreate");
              //  activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                Log.i(TAG, "onActivityCreated: fired!");
                Log.i(TAG, "onActivityCreated: activity.toString(): " + activity.toString());

                Log.i(TAG, "onActivityCreated: Before, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
                if (ApplicationData.getDefaultStoreId() == 0) {
                    Log.i(TAG, "onActivityCreated: ApplicationData.getDefaultStoreId() was 0.");
                    ApplicationData.setDefaultStoreId(sharedPreferences.getInt(SharedPreferencesManager.defaultStoreId, 0));
                }
                Log.i(TAG, "onActivityCreated: After, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.w("state","onActivityStarted");
                Log.i(TAG, "onActivityStarted: fired!");
                Log.i(TAG, "onActivityStarted: activity.toString(): " + activity.toString());

                Log.i(TAG, "onActivityStarted: Before, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
                if (ApplicationData.getDefaultStoreId() == 0) {
                    Log.i(TAG, "onActivityStarted: ApplicationData.getDefaultStoreId() was 0.");
                    ApplicationData.setDefaultStoreId(sharedPreferences.getInt(SharedPreferencesManager.defaultStoreId, 0));
                }
                Log.i(TAG, "onActivityStarted: After, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.w("state","onActivityResumed");

                Log.i(TAG, "onActivityResumed: onActivityResumed fired!");
                Log.i(TAG, "onActivityResumed: activity.toString(): " + activity.toString());

                Log.i(TAG, "onActivityResumed: Before, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
                if (ApplicationData.getDefaultStoreId() == 0) {
                    Log.i(TAG, "onActivityResumed: ApplicationData.getDefaultStoreId() was 0.");
                    ApplicationData.setDefaultStoreId(sharedPreferences.getInt(SharedPreferencesManager.defaultStoreId, 0));//@kajal
                    Toast.makeText(getApplicationContext(), "Id " + sharedPreferences.getInt(SharedPreferencesManager.defaultStoreId, 0), Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "onActivityResumed: After, ApplicationData.getDefaultStoreId(): " + ApplicationData.getDefaultStoreId());
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.w("state","onActivityPaused");

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.w("state","onActivityStopped");

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                Log.w("state","onActivitySaveInstanceState");

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.w("state","onActivityDestroyed");

            }
        });
    }
}
