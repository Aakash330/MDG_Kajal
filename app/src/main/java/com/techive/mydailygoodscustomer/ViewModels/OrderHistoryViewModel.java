package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.OrderHistoryRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.OrderHistory;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Repositories.OrderHistoryRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class OrderHistoryViewModel extends AndroidViewModel {
    private static final String TAG = "OrderHistoryViewModel";

    private OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryRecyclerViewAdapter orderHistoryRecyclerViewAdapter;

    private MutableLiveData<OrderHistory> orderHistoryMutableLiveData;
    private MutableLiveData<OrderHistory> cancelOrderMutableLiveData;

    public ArrayAdapter<String> monthsArrayAdapter;

    public OrderHistoryViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "OrderHistoryViewModel: fired!");

        initOrderHistoryViewModel();
    }

    private void initOrderHistoryViewModel() {
        Log.i(TAG, "initOrderHistoryViewModel: fired!");

        if (orderHistoryRepository != null) {
            Log.i(TAG, "initOrderHistoryViewModel: OrderHistoryRepository already initialized in OrderHistoryViewModel!");
            return;
        }

        orderHistoryRepository = OrderHistoryRepository.getOrderHistoryRepositoryInstance(getApplication().getApplicationContext());
        orderHistoryRepository.initOrderHistoryRepository();

        orderHistoryRecyclerViewAdapter = new OrderHistoryRecyclerViewAdapter(getApplication().getApplicationContext());

        orderHistoryMutableLiveData = orderHistoryRepository.getOrderHistoryMutableLiveData();
        cancelOrderMutableLiveData = orderHistoryRepository.getCancelOrderMutableLiveData();

        ArrayList<String> monthsArrayList = new ArrayList<>(asList("01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11", "12"));
        monthsArrayAdapter = new ArrayAdapter<>(getApplication().getApplicationContext(), R.layout.layout_list_item, monthsArrayList);
    }

    public MutableLiveData<OrderHistory> getOrderHistoryMutableLiveData() {
        return orderHistoryMutableLiveData;
    }

    public MutableLiveData<OrderHistory> getCancelOrderMutableLiveData() {
        return cancelOrderMutableLiveData;
    }

    public boolean getOrderHistory(int month, int year) {
        Log.i(TAG, "getOrderHistory: fired!");

        return orderHistoryRepository.getOrderHistory(month, year);
    }

    public boolean cancelOrder(String orderId, int month, int year) {
        Log.i(TAG, "cancelOrder: fired!");

        return orderHistoryRepository.cancelOrder(orderId, month, year);
    }
}
