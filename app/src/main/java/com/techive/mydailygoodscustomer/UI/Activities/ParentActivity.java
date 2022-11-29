package com.techive.mydailygoodscustomer.UI.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Fragments.HomeFragment;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.CartInterface;

public class ParentActivity extends AppCompatActivity implements CartInterface, CFCheckoutResponseCallback {
    private static final String TAG = "ParentActivity";

    private BottomNavigationView bottomNavigationView;

    public NavController navController;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    public MutableLiveData<String> paymentStatusMutableLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        setTitle("Store");
        Log.i(TAG, "onCreate: fired!");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragmentContainerView);
        /*NavController */
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    /*    bottomNavigationView.setOnItemReselectedListener((BottomNavigationView.OnNavigationItemReselectedListener)
                item -> navController.popBackStack(item.getItemId(), false)
        );*/

        //Testing (28-9-22)
        if (paymentStatusMutableLiveData == null) {
            Log.i(TAG, "onCreate: paymentStatusMutableLiveData initializing now!");
            paymentStatusMutableLiveData = new MutableLiveData<>();
        } else {
            Log.i(TAG, "onCreate: paymentStatusMutableLiveData already initialized!");
        }

        //Testing (28-9-22)
        try {
            // If you are using a fragment then you need to add this line inside onCreate() of your Fragment
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fired!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired!");

        /*old store id		=0
        default store id	=0

        upon store selection	  old=0  default=121
        upon 2nd store selection  old=121  default=134

        parentActivity onResume*/

//        if (bottomNavigationView.getSelectedItemId() == bottomNavigationView.getMenu().getItem(0).getItemId()) {
//            Log.i(TAG, "onResume: Resuming back on Home Fragment.");
////            HomeFragment.getHomeFragment().getShopData();
//
//        }/* else if (bottomNavigationView.getSelectedItemId() == bottomNavigationView.getMenu().getItem(1).getItemId()) {
//            Log.i(TAG, "onResume: Resuming back on Home Fragment.");
//
//        }*/ else if (bottomNavigationView.getSelectedItemId() == bottomNavigationView.getMenu().getItem(2).getItemId()) {
//            Log.i(TAG, "onResume: Resuming back on Account Fragment.");
//
//        } else if (bottomNavigationView.getSelectedItemId() == bottomNavigationView.getMenu().getItem(3).getItemId()) {
//            Log.i(TAG, "onResume: Resuming back on Regular List Fragment.");
//
//        } else if (bottomNavigationView.getSelectedItemId() == bottomNavigationView.getMenu().getItem(4).getItemId()) {
//            Log.i(TAG, "onResume: Resuming back on Cart Fragment.");
//
//        }

//        Log.i(TAG, "onResume: bottomNavigationView.getSelectedItemId(): " + bottomNavigationView.getSelectedItemId());
//        Log.i(TAG, "onResume: bottomNavigationView.getMenu().getItem(0): " + bottomNavigationView.getMenu().getItem(0));
//        Log.i(TAG, "onResume: bottomNavigationView.getMenu().getItem(0).getItemId(): " + bottomNavigationView.getMenu().getItem(0).getItemId());
//        onResume: bottomNavigationView.getSelectedItemId(): 2131296578
//        I/ParentActivity: onResume: bottomNavigationView.getMenu().getItem(0): Home
//        onResume: bottomNavigationView.getMenu().getItem(0).getItemId(): 2131296578
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: fired!");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.i(TAG, "onBackPressed: fired!");

        if (ApplicationData.getDeliveryAddressDialogFragment() != null) {
            Log.i(TAG, "onBackPressed: ApplicationData.getDeliveryAddressDialogFragment() != null");
            getSupportFragmentManager().beginTransaction().remove(ApplicationData.getDeliveryAddressDialogFragment()).commit();
            ApplicationData.setDeliveryAddressDialogFragment(null);
        } else if (ApplicationData.getDeliveryAddressListDialogFragment() != null) {
            Log.i(TAG, "onBackPressed: ApplicationData.getDeliveryAddressListDialogFragment() != null");
            getSupportFragmentManager().beginTransaction().remove(ApplicationData.getDeliveryAddressListDialogFragment()).commit();
            ApplicationData.setDeliveryAddressListDialogFragment(null);
        } else {
            Log.i(TAG, "onBackPressed: No DialogFragments visible!");

            if (bottomNavigationView.getSelectedItemId() == R.id.home) {
                Log.i(TAG, "onBackPressed: returning from home.");

                /*IMPLEMENTING DOUBLE BACK PRESS. - SUCCESS*/
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
//                    super.onBackPressed();
                    System.exit(0);
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "Tap back button again in order to exit!", Toast.LENGTH_SHORT).show();
                }
                mBackPressed = System.currentTimeMillis();
            } else {
                Log.i(TAG, "onBackPressed: not returning from home.");
                super.onBackPressed();
            }
        }
    }

    public void openHomeFragment() {
        Log.i(TAG, "openHomeFragment: fired!");

        navController.navigate(R.id.home);
    }

    @Override
    public void showBadgeOnCart(int qty) {
        Log.i(TAG, "showBadgeOnCart: fired! qty: " + qty);

        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
        if (qty == 0) {
            badgeDrawable.setVisible(false);
            badgeDrawable.clearNumber();
        } else {
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(qty);
        }
    }


    @Override
    public void onPaymentVerify(String s) {
        try{
            Log.i(TAG, "onPaymentVerify: Inside Parent Activity! orderId" + s);
            paymentStatusMutableLiveData.postValue(s);
        }catch (Exception ee){}

    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {

        try{
            Log.i(TAG, "onPaymentFailure: Inside Parent Activity! fired! cfErrorResponse.toJSON().toString(): " + cfErrorResponse.toJSON().toString() + "\ts: " + s);

            paymentStatusMutableLiveData.postValue("Failed");
        }catch (Exception ee){


        }

    }
}