package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.DeliveryAddressListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.BuyerAllDeliveryAddress;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;
import com.techive.mydailygoodscustomer.Repositories.DeliveryAddressRepository;

import org.jetbrains.annotations.NotNull;

public class DeliveryAddressListViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveryAddressListView";

    private DeliveryAddressRepository deliveryAddressRepository;

    public DeliveryAddressListRecyclerViewAdapter deliveryAddressListRecyclerViewAdapter;

    private MutableLiveData<BuyerAllDeliveryAddress> buyerAllDeliveryAddressMutableLiveData;
    private MutableLiveData<GeneralResponse> setDefaultDeliveryAddressMutableLiveData;

    public int addressId;

    public Cart_CartData_BuyerAddress updatedCart_cartData_buyerAddress;

    public DeliveryAddressListViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "DeliveryAddressListViewModel: fired!");

        initDeliveryAddressListViewModel();
    }

    private void initDeliveryAddressListViewModel() {
        Log.i(TAG, "initDeliveryAddressListViewModel: fired!");

        if (deliveryAddressRepository != null) {
            Log.i(TAG, "initDeliveryAddressListViewModel: DeliveryAddressRepository already initialized in DeliveryAddressListVewModel.");
            return;
        }

        deliveryAddressRepository = DeliveryAddressRepository.getDeliveryAddressRepository(getApplication().getApplicationContext());
        deliveryAddressRepository.initDeliveryAddressRepository();

        deliveryAddressListRecyclerViewAdapter = new DeliveryAddressListRecyclerViewAdapter();

        buyerAllDeliveryAddressMutableLiveData = deliveryAddressRepository.getBuyerAllDeliveryAddressMutableLiveData();
        setDefaultDeliveryAddressMutableLiveData = deliveryAddressRepository.getSetDefaultDeliveryAddressMutableLiveData();
    }

    public MutableLiveData<BuyerAllDeliveryAddress> getBuyerAllDeliveryAddressMutableLiveData() {
        return buyerAllDeliveryAddressMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getSetDefaultDeliveryAddressMutableLiveData() {
        return setDefaultDeliveryAddressMutableLiveData;
    }

    public void updateDefaultDeliveryAddress() {
        Log.i(TAG, "updateDefaultDeliveryAddress: fired!");

        for (Cart_CartData_BuyerAddress cart_cartData_buyerAddress : buyerAllDeliveryAddressMutableLiveData.getValue().getData()) {
            if (cart_cartData_buyerAddress.getId() == addressId) {
                Log.i(TAG, "updateDefaultDeliveryAddress: BEFORE, cart_cartData_buyerAddress: " + cart_cartData_buyerAddress);
                cart_cartData_buyerAddress.setStatus(1);
                Log.i(TAG, "updateDefaultDeliveryAddress: AFTER, cart_cartData_buyerAddress: " + cart_cartData_buyerAddress);
                updatedCart_cartData_buyerAddress = cart_cartData_buyerAddress;
                return;
            }
        }
    }

    public boolean getBuyerAllDeliveryAddress() {
        Log.i(TAG, "getBuyerAllDeliveryAddress: fired!");

        return deliveryAddressRepository.getBuyerAllDeliveryAddress();
    }

    public boolean setDefaultDeliveryAddress(int addressId) {
        Log.i(TAG, "setDefaultDeliveryAddress: fired!");

        this.addressId = addressId;
        return deliveryAddressRepository.setDefaultDeliveryAddress(addressId);
    }
}
