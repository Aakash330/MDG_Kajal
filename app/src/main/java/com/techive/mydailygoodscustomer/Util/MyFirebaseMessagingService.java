package com.techive.mydailygoodscustomer.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.StoreLocatorActivity;

import org.jetbrains.annotations.NotNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.i(TAG, "onMessageReceived: fired!");
        Log.i(TAG, "onMessageReceived: message: " + message);
        Log.i(TAG, "onMessageReceived: message.getNotification(): " + message.getNotification());
//        I/MyFirebaseMessagingServ: onMessageReceived: message: com.google.firebase.messaging.RemoteMessage@b23ad9
//        I/MyFirebaseMessagingServ: onMessageReceived: message.getNotification(): com.google.firebase.messaging.RemoteMessage$Notification@4341f4c

        if (message.getNotification() != null) {
            Log.i(TAG, "onMessageReceived: message.getNotification().toString(): " + message.getNotification().toString());
            Log.i(TAG, "onMessageReceived: message.getNotification().getTitle(): " + message.getNotification().getTitle());
            Log.i(TAG, "onMessageReceived: message.getNotification().getBody(): " + message.getNotification().getBody());
//            I/MyFirebaseMessagingServ: onMessageReceived: message.getNotification().toString(): com.google.firebase.messaging.RemoteMessage$Notification@4341f4c
//            onMessageReceived: message.getNotification().getTitle(): Cust Noti Titile
//            onMessageReceived: message.getNotification().getBody(): Cust Noti Body

            showNotification(message.getNotification());
        } else {
            Log.i(TAG, "onMessageReceived: message.getNotification() is NULL.");
        }

        if (message.getData() != null) {
            Log.i(TAG, "onMessageReceived: message.getData().toString(): " + message.getData().toString());
//            I/MyFirebaseMessagingServ: onMessageReceived: message.getData().toString(): {}
        } else {
            Log.i(TAG, "onMessageReceived: message.getData() is NULL.");
        }
    }

    private void showNotification(RemoteMessage.Notification notification) {
        Log.i(TAG, "showNotification: fired! notification.getTitle(): " + notification.getTitle());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(), StoreLocatorActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent;
        //= PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_notifications_24)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(5678, builder.build());
    }

    @Override
    public void onNewToken(@NonNull @NotNull String token) {
        super.onNewToken(token);
        Log.i(TAG, "onNewToken: fired! token: " + token);
//        I/MyFirebaseMessagingServ: onNewToken: fired! token: fz9OEMBPQvmd4ifs283i9l:APA91bHmHLRo4TjzHZ1FmDAnJqyXWIQ8jrlwXnvfVrLm5gwubFi9V36TVgoutcSlNeLmW2CqVGrqY8CFVbl9cjN6y8q-yeEP6HRMoOQa_XiDf4reS7UeqJwRVbXXwn9HVF1T_EkdxGMS

//        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
//        SharedPreferences sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();
//
//        String storedToken = sharedPreferences.getString(SharedPreferencesManager.token, SharedPreferencesManager.token);
//        if (!storedToken.matches(token)) {
//            Log.i(TAG, "onNewToken: New Token doesn't match Stored Token, thus saving new token.");
//
//        } else {
//
//        }
    }

    //NOTES--
        /*A service that extends FirebaseMessagingService. This is required if you want to do any message handling beyond
     receiving notifications on apps in the background. To receive notifications in foregrounded apps, to receive
      data payload, to send upstream messages, and so on, you must extend this service.*/

//    public FirebaseMessagingReceiver() {
//        super();
//        Log.i(TAG, "FirebaseMessagingReceiver: Empty Constructor fired!");
//    }

    /*To receive messages, use a service that extends FirebaseMessagingService. Your service should override the
    onMessageReceived and onDeletedMessages callbacks. It should handle any message within 20 seconds of receipt
    (10 seconds on Android Marshmallow). The time window may be shorter depending on OS delays incurred ahead of
    calling onMessageReceived. After that time, various OS behaviors such as Android O's background execution
    limits may interfere with your ability to complete your work. For more information see our overview on message priority.

    onMessageReceived is provided for most message types, with the following exceptions:

    -Notification messages delivered when your app is in the background. In this case, the notification is delivered
    to the device’s system tray. A user tap on a notification opens the app launcher by default.

    -Messages with both notification and data payload, when received in the background. In this case, the notification is
     delivered to the device’s system tray, and the data payload is delivered in the extras of the intent of your launcher Activity.*/


    /*The registration token may change when:

    The app is restored on a new device
    The user uninstalls/reinstall the app
    The user clears app data.*/

    /*
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // FCM registration token to your app server.
    //sendRegistrationTokenToMDGServer();
}
