package com.techive.mydailygoodscustomer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.techive.mydailygoodscustomer.R;

public class PlaceOrderActivity extends AppCompatActivity {
    private static final String TAG = "PlaceOrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Log.i(TAG, "onCreate: fired!");

        initComponentViews();

        initAdapters();

        initObservers();

        initListeners();
    }

    private void initComponentViews() {
        Log.i(TAG, "initComponentViews: fired!");

    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");
    }


}