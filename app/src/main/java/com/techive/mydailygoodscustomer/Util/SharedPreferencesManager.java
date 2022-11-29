package com.techive.mydailygoodscustomer.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String TAG = "SharedPreferencesManage";

    private Context context;

    public static final String mobNo = "MOB_NO";
    public static final String password = "PASSWORD";
    public static final String userId = "USER_ID";
    public static final String buyerName = "BUYER_NAME";
    public static final String token = "TOKEN";
    public static final String defaultStoreId = "DEFAULT_STORE_ID";

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public SharedPreferences getBuyerSharedPreferences() {
        return context.getSharedPreferences("BUYER_PREF", Context.MODE_PRIVATE);
    }
}
