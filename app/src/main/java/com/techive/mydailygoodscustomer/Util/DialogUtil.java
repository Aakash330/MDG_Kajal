package com.techive.mydailygoodscustomer.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.LoginActivity;

import java.util.HashMap;


public class DialogUtil {
    private static final String TAG = "DialogUtil";

    private static MaterialAlertDialogBuilder materialAlertDialogBuilder = null;

    private static AlertDialog loadingAlertDialog = null, fetchingAlertDialog = null, processingInfoAlertDialog = null;

    private static Snackbar snackbar;

    private static HashMap<String, AlertDialog> loadingStringAlertDialogHashMap = new HashMap<>();

    //LOADING - START
    public static void showLoadingDialog(Context context) {
        Log.i(TAG, "showLoadingDialog: fired!");

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setView(R.layout.dialog_loading);
        materialAlertDialogBuilder.setCancelable(false);
        loadingAlertDialog = materialAlertDialogBuilder.create();

        loadingAlertDialog.show();
    }

    public static void dismissLoadingDialog() {
        Log.i(TAG, "dismissLoadingDialog: fired!");

        if (loadingAlertDialog != null) {
            loadingAlertDialog.dismiss();
        }
    }

    public static void showLoadingDialog1(Context context, String tag) {
        Log.i(TAG, "showLoadingDialog1: fired! tag: " + tag);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setView(R.layout.dialog_loading);
        materialAlertDialogBuilder.setCancelable(false);
        AlertDialog loadingAlertDialog = materialAlertDialogBuilder.create();
        loadingStringAlertDialogHashMap.put(tag, loadingAlertDialog);
        loadingAlertDialog.show();
        Log.i(TAG, "showLoadingDialog1: loadingStringAlertDialogHashMap.toString(): " + loadingStringAlertDialogHashMap.toString());
    }

    public static void dismissLoadingDialog1(String tag) {
        Log.i(TAG, "dismissLoadingDialog1: fired! tag: " + tag);

        if (loadingStringAlertDialogHashMap.get(tag) != null) {
            loadingStringAlertDialogHashMap.get(tag).dismiss();
            loadingStringAlertDialogHashMap.remove(tag);
            Log.i(TAG, "dismissLoadingDialog1: loadingStringAlertDialogHashMap.toString(): " + loadingStringAlertDialogHashMap.toString());
        }
    }

    public static boolean isShowingLoadingDialog(String tag) {
        Log.i(TAG, "isShowingLoadingDialog: fired!\ttag: " + tag);

        return loadingStringAlertDialogHashMap.get(tag) != null;
    }
    //LOADING - END

    //FETCHING LOCATION - START
    public static void showFetchingLocationDialog(Context context) {
        Log.i(TAG, "showFetchingLocationDialog: fired!");

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setView(R.layout.dialog_fetching_location);
        materialAlertDialogBuilder.setCancelable(false);
        fetchingAlertDialog = materialAlertDialogBuilder.create();

        fetchingAlertDialog.show();
    }

    public static void dismissFetchingLocationDialog() {
        Log.i(TAG, "dismissFetchingLocationDialog: fired!");

        if (fetchingAlertDialog != null) {
            fetchingAlertDialog.dismiss();
        }
    }
    //FETCHING LOCATION - END

    //PROCESSING INFO
    public static void showProcessingInfoDialog(Context context) {
        Log.i(TAG, "showProcessingInfoDialog: fired!");

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setView(R.layout.dialog_processing_info);
        materialAlertDialogBuilder.setCancelable(false);
        processingInfoAlertDialog = materialAlertDialogBuilder.create();
        processingInfoAlertDialog.show();
    }

    public static void dismissProcessingInfoDialog() {
        Log.i(TAG, "dismissProcessingInfoDialog: fired!");

        if (processingInfoAlertDialog != null) {
            processingInfoAlertDialog.dismiss();
        }
    }
    //PROCESSING INFO - END

    public static void showNoInternetToast(Context context) {
        Log.i(TAG, "showNoInternetToast: fired!");

        Toast.makeText(context, context.getResources().getString(R.string.no_internet_msg_toast), Toast.LENGTH_SHORT).show();
    }

    public static void showCustomSnackbar(Context context, View anchorView, String msg, @Nullable Integer duration) {
        Log.i(TAG, "showCustomSnackbar: fired!");

        // LONG = 0, SHORT = -1, INDEFINITE = -2
//        Snackbar snackbar;
        if (duration == null) {
           // snackbar = Snackbar.make(anchorView, msg, Snackbar.LENGTH_LONG);
            snackbar = Snackbar.make(anchorView,msg, Snackbar.LENGTH_SHORT);;
        } else {
            //snackbar = Snackbar.make(anchorView, msg, duration);
            snackbar = Snackbar.make(anchorView,msg, Snackbar.LENGTH_SHORT);;
        }
        View snackBarView = snackbar.getView();
        TextView snackBarTextView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (snackBarTextView != null) {
            snackBarTextView.setMaxLines(4);
        }
        snackbar.setBackgroundTint(context.getResources().getColor(R.color.toolbar_dark_green, null))
                .setTextColor(context.getResources().getColor(R.color.white, null))
                .show();
    }

    public static void dismissCustomSnackBar() {
        Log.i(TAG, "dismissCustomSnackBar: fired!");

        if (snackbar != null) {
            snackbar.dismiss();
        }
    }
}
