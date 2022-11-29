package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.OrderHistory;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryRepository {
    private static final String TAG = "OrderHistoryRepository";

    private static OrderHistoryRepository orderHistoryRepositoryInstance;

    private static Context applicationContext;

    private MutableLiveData<OrderHistory> orderHistoryMutableLiveData;
    private MutableLiveData<OrderHistory> cancelOrderMutableLiveData;

    public static OrderHistoryRepository getOrderHistoryRepositoryInstance(Context context) {
        Log.i(TAG, "getOrderHistoryRepositoryInstance: fired!");

        if (orderHistoryRepositoryInstance == null) {
            Log.i(TAG, "getOrderHistoryRepositoryInstance: OrderHistoryRepository Initialized now!");
            orderHistoryRepositoryInstance = new OrderHistoryRepository();
            applicationContext = context;
        }
        return orderHistoryRepositoryInstance;
    }

    public void initOrderHistoryRepository() {
        Log.i(TAG, "initOrderHistoryRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        orderHistoryMutableLiveData = new MutableLiveData<>();
        cancelOrderMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<OrderHistory> getOrderHistoryMutableLiveData() {
        return orderHistoryMutableLiveData;
    }

    public MutableLiveData<OrderHistory> getCancelOrderMutableLiveData() {
        return cancelOrderMutableLiveData;
    }

    public boolean getOrderHistory(int month, int year) {
        Log.i(TAG, "getOrderHistory: fired! month: " + month + "\tyear: " + year);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getOrderHistory: Network Available!");

            Call<OrderHistory> orderHistoryCall = ApplicationData.mdg_customerAPI_interface.getOrderHistory(
                    ApplicationData.getLoggedInBuyerId(), month, year);
            orderHistoryCall.enqueue(new Callback<OrderHistory>() {
                @Override
                public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                    Log.i(TAG, "onResponse: ORDER HISTORY Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        OrderHistory orderHistoryUnsuccessful = new OrderHistory("Somehow server didn't respond!", null, response.code());
                        orderHistoryMutableLiveData.setValue(orderHistoryUnsuccessful);
                        return;
                    }
                    OrderHistory orderHistory = response.body();
                    Log.i(TAG, "onResponse: orderHistory: " + orderHistory);
                    orderHistoryMutableLiveData.setValue(orderHistory);
                }

                @Override
                public void onFailure(Call<OrderHistory> call, Throwable t) {
                    Log.i(TAG, "onFailure: ORDER HISTORY Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        OrderHistory orderHistoryFailedSocketTimeout = new OrderHistory(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        orderHistoryMutableLiveData.setValue(orderHistoryFailedSocketTimeout);
                        return;
                    }
                    OrderHistory orderHistoryFailed = new OrderHistory("Order History Request seems to have failed!", null, 1000);
                    orderHistoryMutableLiveData.setValue(orderHistoryFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean cancelOrder(String orderId, int month, int year) {
        Log.i(TAG, "cancelOrder: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "cancelOrder: Network Available!");

            Call<OrderHistory> cancelOrderCall = ApplicationData.mdg_customerAPI_interface.cancelOrder(
                    ApplicationData.getLoggedInBuyerId(), orderId, month, year);
            cancelOrderCall.enqueue(new Callback<OrderHistory>() {
                @Override
                public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                    Log.i(TAG, "onResponse: CANCEL ORDER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        OrderHistory orderHistory = new OrderHistory(applicationContext.getString(R.string.server_didnt_respond), null, response.code());
                        cancelOrderMutableLiveData.setValue(orderHistory);
                        return;
                    }
                    OrderHistory orderHistory = response.body();
                    Log.i(TAG, "onResponse: CANCEL ORDER: " + orderHistory);
                    cancelOrderMutableLiveData.setValue(orderHistory);
                }

                @Override
                public void onFailure(Call<OrderHistory> call, Throwable t) {
                    Log.i(TAG, "onFailure: CANCEL ORDER Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        OrderHistory orderHistory = new OrderHistory(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        cancelOrderMutableLiveData.setValue(orderHistory);
                        return;
                    }
                    OrderHistory orderHistory = new OrderHistory("Somehow Cancel Order request has failed!\nPlease try again later!", null, 1000);
                    cancelOrderMutableLiveData.setValue(orderHistory);
                }
            });
            return true;
        }
        return false;
    }
}
