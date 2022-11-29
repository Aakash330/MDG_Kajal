package com.techive.mydailygoodscustomer.Util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techive.mydailygoodscustomer.UI.Fragments.DeliveryAddressDialogFragment;
import com.techive.mydailygoodscustomer.UI.Fragments.DeliveryAddressListDialogFragment;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationData {
    private static final String TAG = "ApplicationData";

    public static MDG_CustomerAPI_Interface mdg_customerAPI_interface, mdg_customerAPI_interfaceShort;

    private static int defaultStoreId, oldStoreIdHome, oldStoreIdBestDeals, oldStoreIdRecentlyAdded, oldStoreIdRegularList,
            oldStoreIdCart, oldStoreIdHotSelling;

    private static String defaultStoreName;

    private static int loggedInBuyerId;

    private static DeliveryAddressDialogFragment deliveryAddressDialogFragment;

    private static DeliveryAddressListDialogFragment deliveryAddressListDialogFragment;

    private static int cartTotalQty;

    private static HashMap<Integer, Integer> prodIdOrderQtyHashMap = new HashMap<>();

    public static void initializeRetrofit(Context applicationContext) {
        Log.i(TAG, "initializeRetrofit: fired!");

        if (mdg_customerAPI_interface == null) {
            Log.i(TAG, "initializeRetrofit: MDG Interface was null. Initializing now...");

            String BASE_URL = "https://www.mydailygoods.com/api/";

            CertificateClassOS6 certificateClassOS6 = new CertificateClassOS6();
            SSLSocketFactory sslSocketFactory = certificateClassOS6.getSslSocketFactory(applicationContext);
            Log.i(TAG, "initializeRetrofit: sslSocketFactory.toString(): " + sslSocketFactory.toString());

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Log.i(TAG, "initializeRetrofit: On OS <= M : " + Build.VERSION.SDK_INT);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(25, TimeUnit.SECONDS)
                        .writeTimeout(25, TimeUnit.SECONDS)
                        .sslSocketFactory(sslSocketFactory, certificateClassOS6.getTrustManager())
                        .addInterceptor(httpLoggingInterceptor).build();
            } else {
                Log.i(TAG, "initializeRetrofit: On OS > M : " + Build.VERSION.SDK_INT);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(25, TimeUnit.SECONDS)
                        .writeTimeout(25, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor).build();
            }
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            mdg_customerAPI_interface = retrofit.create(MDG_CustomerAPI_Interface.class);
        }
    }

    public static void initializeRetrofitShort(Context applicationContext) {
        Log.i(TAG, "initializeRetrofitShort: fired!");

        if (mdg_customerAPI_interfaceShort == null) {
            Log.i(TAG, "initializeRetrofitShort: MDG Interface was null. Initializing now...");

            String BASE_URL = "https://www.mydailygoods.com/api/";

            CertificateClassOS6 certificateClassOS6 = new CertificateClassOS6();
            SSLSocketFactory sslSocketFactory = certificateClassOS6.getSslSocketFactory(applicationContext);
            Log.i(TAG, "initializeRetrofitShort: sslSocketFactory.toString(): " + sslSocketFactory.toString());

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Log.i(TAG, "initializeRetrofitShort: On OS <= M : " + Build.VERSION.SDK_INT);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(3, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .sslSocketFactory(sslSocketFactory, certificateClassOS6.getTrustManager())
                        .addInterceptor(httpLoggingInterceptor).build();
            } else {
                Log.i(TAG, "initializeRetrofitShort: On OS > M : " + Build.VERSION.SDK_INT);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(3, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor).build();
            }
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            mdg_customerAPI_interfaceShort = retrofit.create(MDG_CustomerAPI_Interface.class);
        }
    }

    public static int getDefaultStoreId() {
        return defaultStoreId;
    }

    public static void setDefaultStoreId(int defaultStoreId) {
        Log.i(TAG, "setDefaultStoreId: New, to be set DefaultStoreId = " + defaultStoreId);
        ApplicationData.defaultStoreId = defaultStoreId;
    }

    public static String getDefaultStoreName() {
        return defaultStoreName;
    }

    public static void setDefaultStoreName(String defaultStoreName) {
        Log.i(TAG, "setDefaultStoreName: New, to be set DefaultStoreName = " + defaultStoreName);
        ApplicationData.defaultStoreName = defaultStoreName;
    }

    public static int getOldStoreIdHome() {
        return oldStoreIdHome;
    }

    public static void setOldStoreIdHome(int oldStoreIdHome) {
        Log.i(TAG, "setOldStoreIdHome: Setting " + oldStoreIdHome + " as the oldStoreIdHome.");
        ApplicationData.oldStoreIdHome = oldStoreIdHome;
    }

    public static int getOldStoreIdBestDeals() {
        return oldStoreIdBestDeals;
    }

    public static void setOldStoreIdBestDeals(int oldStoreIdBestDeals) {
        ApplicationData.oldStoreIdBestDeals = oldStoreIdBestDeals;
    }

    public static int getOldStoreIdRecentlyAdded() {
        return oldStoreIdRecentlyAdded;
    }

    public static void setOldStoreIdRecentlyAdded(int oldStoreIdRecentlyAdded) {
        ApplicationData.oldStoreIdRecentlyAdded = oldStoreIdRecentlyAdded;
    }

    public static int getOldStoreIdRegularList() {
        return oldStoreIdRegularList;
    }

    public static void setOldStoreIdRegularList(int oldStoreIdRegularList) {
        ApplicationData.oldStoreIdRegularList = oldStoreIdRegularList;
    }

    public static int getOldStoreIdCart() {
        return oldStoreIdCart;
    }

    public static void setOldStoreIdCart(int oldStoreIdCart) {
        ApplicationData.oldStoreIdCart = oldStoreIdCart;
    }

    public static int getOldStoreIdHotSelling() {
        return oldStoreIdHotSelling;
    }

    public static void setOldStoreIdHotSelling(int oldStoreIdHotSelling) {
        ApplicationData.oldStoreIdHotSelling = oldStoreIdHotSelling;
    }

    public static int getLoggedInBuyerId() {
        return loggedInBuyerId;
    }

    public static void setLoggedInBuyerId(int loggedInBuyerId) {
        Log.i(TAG, "setLoggedInBuyerId: New, to be set LoggedInBuyerId = " + loggedInBuyerId);
        ApplicationData.loggedInBuyerId = loggedInBuyerId;
    }

    public static DeliveryAddressDialogFragment getDeliveryAddressDialogFragment() {
        return deliveryAddressDialogFragment;
    }

    public static void setDeliveryAddressDialogFragment(DeliveryAddressDialogFragment deliveryAddressDialogFragment) {
        ApplicationData.deliveryAddressDialogFragment = deliveryAddressDialogFragment;
    }

    public static DeliveryAddressListDialogFragment getDeliveryAddressListDialogFragment() {
        return deliveryAddressListDialogFragment;
    }

    public static void setDeliveryAddressListDialogFragment(DeliveryAddressListDialogFragment deliveryAddressListDialogFragment) {
        ApplicationData.deliveryAddressListDialogFragment = deliveryAddressListDialogFragment;
    }

    public static int getCartTotalQty() {
        return cartTotalQty;
    }

    public static void setCartTotalQty(int cartTotalQty) {
        ApplicationData.cartTotalQty = cartTotalQty;
    }

    public static HashMap<Integer, Integer> getProdIdOrderQtyHashMap() {
        return prodIdOrderQtyHashMap;
    }

    public static void setProdIdOrderQtyHashMap(HashMap<Integer, Integer> prodIdOrderQtyHashMap) {
        ApplicationData.prodIdOrderQtyHashMap = prodIdOrderQtyHashMap;
    }
}
