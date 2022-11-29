package com.techive.mydailygoodscustomer.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.techive.mydailygoodscustomer.UI.Activities.LoginActivity;

public class LoginUtil {
    private static final String TAG = "LoginUtil";

    public static void redirectToLogin(Context context, String message) {
        Log.i(TAG, "redirectToLogin: fired!");

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setTitle("Proceed to Login/Register!")
                .setMessage(message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "onClick: Canceled!");

                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "onClick: Yes!");

                        //PROCEED TO UPLOAD
                        dialogInterface.dismiss();

                        Intent loginIntent = new Intent(context, LoginActivity.class);
                        context.startActivity(loginIntent);
                    }
                })
                .create();
        alertDialog.show();
    }

}
