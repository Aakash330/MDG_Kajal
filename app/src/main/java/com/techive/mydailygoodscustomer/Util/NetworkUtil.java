package com.techive.mydailygoodscustomer.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtil {

    public static String networkConnectivityStatus(Context context)
    {
        String status = null;
        ConnectivityManager objConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /*Context is used because SystemService is only available from AppCompatActivity class.
        Thus by using Context over here, we can use this class elsewhere where some other class has extended AppCompatActivity class.*/
        NetworkInfo objNetworkInfo = objConnectivityManager.getActiveNetworkInfo();
        Log.i("NETWORK_INFO", String.valueOf(objNetworkInfo));
        //Log.i("NETWORK_INFO", String.valueOf(objNetworkInfo.getType()) + "\t" + objNetworkInfo.getTypeName());

        if(objNetworkInfo == null)
        {
            status = "No Internet Available!";
            return status;
        }
        else
        {
            if (objNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                status = "Mobile Data Enabled";
                return status;
            }
            else if (objNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                status = "WIFI Enabled";
                return status;
            }
        }
        //status = "Internet Available!";
        return  status;
    }
}
