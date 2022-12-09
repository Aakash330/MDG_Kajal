package com.techive.mydailygoodscustomer.cashfreeupdatestesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.exception.CFInvalidArgumentException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.techive.mydailygoodscustomer.Models.CashFreeOrder;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashFreeInitializationTestingActivity extends AppCompatActivity implements CFCheckoutResponseCallback {


    private static final String TAG ="CashFreeInitializationTestingActivity" ;
   String orderAmount="1.0";
   String buyerid="89";
    String Order_token,Order_id;
    public static MutableLiveData<String> paymentStatusMutableLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_cash_free_initialization_testing);
           Button button=findViewById(R.id.button2);
        try {
            // If you are using a fragment then you need to add this line inside onCreate() of your Fragment
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            e.printStackTrace();
        }
        if (paymentStatusMutableLiveData == null) {
            Log.i(TAG, "onCreate: paymentStatusMutableLiveData initializing now!");
            paymentStatusMutableLiveData = new MutableLiveData<>();
        } else {
            Log.i(TAG, "onCreate: paymentStatusMutableLiveData already initialized!");
        }
        //-----------------------------------------------------TestingApi----------------------------------------------------------------
          /*  Call<DataResponse> cashFreeOrderCall = ServiceTs.getService().createCashFreeOrder(
                   buyerid , orderAmount*//*, uniqueOrderId*//*);*/
           /* cashFreeOrderCall.enqueue(new Callback<DataResponse>() {
                @Override
                public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                    Log.i(TAG, "onResponse: CREATE CASH FREE ORDER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                       // CashFreeOrder cashFreeOrderUnsuccessful = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", "Somehow Server didn't respond! " + response.code());
                        Toast.makeText(CashFreeInitializationTestingActivity.this, "unsuccessful...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.i(TAG, "onResponse: cashFreeOrder: " + response);
                    Log.i(TAG, "onResponse: cashFreeOrder: " + response.body().getOrderId());
                    Log.i(TAG, "onResponse: cashFreeOrder: " + response.body().getOrderToken());
                    Order_id  = response.body().getOrderId();
                    Order_token =response.body().getOrderToken();

                }

                @Override
                public void onFailure(Call<DataResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: CREATE CASH FREE ORDER Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(CashFreeInitializationTestingActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });*/
        //-----------------------------------------------------------LiveApi--------------------------------------------------
   /*     Call<CashFreeOrder> cashFreeOrderCall = ServiceTs.getService().createCashFreeOrder(
                buyerid , orderAmount*//*, uniqueOrderId*//*);
        cashFreeOrderCall.enqueue(new Callback<CashFreeOrder>() {
                @Override
                public void onResponse(Call<CashFreeOrder> call, Response<CashFreeOrder> response) {
                    Log.i(TAG, "onResponse: CREATE CASH FREE ORDER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                       // CashFreeOrder cashFreeOrderUnsuccessful = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", "Somehow Server didn't respond! " + response.code());
                        Toast.makeText(CashFreeInitializationTestingActivity.this, "unsuccessful...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    CashFreeOrder cashFreeOrder = response.body();
                    Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder);
                    Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder.getOrder_id());
                    Log.i(TAG, "onResponse: cashFreeOrder: " +cashFreeOrder.getOrder_token());
                    Order_id  = cashFreeOrder.getOrder_id();
                    Order_token =cashFreeOrder.getOrder_token();

                }

                @Override
                public void onFailure(Call<CashFreeOrder> call, Throwable t) {
                    Log.i(TAG, "onFailure: CREATE CASH FREE ORDER Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(CashFreeInitializationTestingActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });*/
//---------------------------------------------------button on click----------------------------------------------------
        button.setOnClickListener(v -> {
            //initiateOnlinePayment(Order_token,Order_id);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView,new CartFragmentTesting()).commit();
        });
        }

   /* private void initiateOnlinePayment(String orderToken, String orderId) {
        Log.i(TAG, "initiateOnlinePayment: fired! orderToken: " + orderToken + "\torderId: " + orderId);
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
//                    .setEnvironment(CFSession.Environment.SANDBOX)
                    .setEnvironment(CFSession.Environment.PRODUCTION)
                    //.setPaymentSessionID(orderToken)
                    .setOrderToken(orderToken)
                    //.setOrderToken("VQwl3LAMjdKcnFvgmYX9")
                    .setOrderId(orderId)
//                    .setOrderId("123456")
                    .build();
            //success@upi

//            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
//                    .setNavigationBarBackgroundColor("#6A3FD3")
//                    .setNavigationBarTextColor("#FFFFFF")
//                    .setButtonBackgroundColor("#6A3FD3")
//                    .setButtonTextColor("#FFFFFF")
//                    .setPrimaryTextColor("#000000")
//                    .setSecondaryTextColor("#000000")
//                    .build();

            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#198E0A") *//*toolbar_dark_green*//*
                    .setNavigationBarTextColor("#FFFFFF") *//*white*//*
                    .setButtonBackgroundColor("#198E0A") *//*toolbar_dark_green*//*
                    .setButtonTextColor("#FFFFFF") *//*white*//*
                    .setPrimaryTextColor("#000000") *//*black*//*
                    .setSecondaryTextColor("#000000") *//*black*//*
                    .build();

            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                    .setSession(cfSession)
//                .setCFUIPaymentModes(cfPaymentComponent)
//                    .setCFUIPaymentModes(null)
                    .setCFNativeCheckoutUITheme(cfTheme)
                    .build();

            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(this, cfDropCheckoutPayment);

        } catch (CFInvalidArgumentException cfInvalidArgumentException) {
            Log.i(TAG, "initiateOnlinePayment: cfInvalidArgumentException.getMessage(): " + cfInvalidArgumentException.getMessage());
            cfInvalidArgumentException.printStackTrace();
        } catch (Exception exception) {
            Log.i(TAG, "initiateOnlinePayment: exception.getMessage(): " + exception.getMessage());
            exception.printStackTrace();
        }
    }*/

    @Override
    public void onPaymentVerify(String s) {

        Toast.makeText(this, "successful"+s, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "initiateOnlinePayment: cfInvalidArgumentException.getMessage(): " + s);
        paymentStatusMutableLiveData.postValue(s);

    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
        Log.i(TAG, "initiateOnlinePayment: cfInvalidArgumentException.getMessage(): " + cfErrorResponse.getMessage());
        Toast.makeText(this, "unsuccessful: "+cfErrorResponse.getMessage()+"code:"+cfErrorResponse.getCode(), Toast.LENGTH_SHORT).show();
        paymentStatusMutableLiveData.postValue("Failed");

    }
}