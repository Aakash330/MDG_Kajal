package com.techive.mydailygoodscustomer.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.techive.mydailygoodscustomer.Adapters.DeliveryAddressListRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.BuyerAllDeliveryAddress;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DeliveryAddressInterface;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.ViewModels.DeliveryAddressListViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveryAddressListDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryAddressListDialogFragment extends Fragment implements DeliveryAddressListRecyclerViewAdapter.OnDeliveryAddressListener {
    private static final String TAG = "DeliveryAddressListDial";

    private SwipeRefreshLayout deliveryAddressListSwipeRefreshLayout;

    private LinearLayoutCompat deliveryAddressListLinearLayoutCompat;

    private RecyclerView deliveryAddressListRecyclerView;

    private DeliveryAddressListViewModel deliveryAddressListViewModel;

    private DeliveryAddressInterface deliveryAddressInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeliveryAddressListDialogFragment() {
        // Required empty public constructor
        Log.i(TAG, "DeliveryAddressListDialogFragment: Empty Constructor fired!");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryAddressListDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryAddressListDialogFragment newInstance(String param1, String param2) {
        DeliveryAddressListDialogFragment fragment = new DeliveryAddressListDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: fired!");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: fired!");
        return inflater.inflate(R.layout.fragment_delivery_address_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

//        deliveryAddressListViewModel = new ViewModelProvider(this).get(DeliveryAddressListViewModel.class);
//        deliveryAddressListViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(DeliveryAddressListViewModel.class);
        deliveryAddressListViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(DeliveryAddressListViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        if (!deliveryAddressListViewModel.getBuyerAllDeliveryAddress()) {
            DialogUtil.showNoInternetToast(requireActivity());
        } else {
            DialogUtil.showLoadingDialog(requireActivity());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fired!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: fired!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: fired!");

        if (deliveryAddressListViewModel.addressId != 0) {
            deliveryAddressInterface.updateDefaultDeliveryAddress(deliveryAddressListViewModel.updatedCart_cartData_buyerAddress);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: fired!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: fired!");
    }

    private void initComponentViews(View view) {
        Log.i(TAG, "initComponentViews: fired!");

        deliveryAddressListSwipeRefreshLayout = view.findViewById(R.id.deliveryAddressListSwipeRefreshLayout);
        deliveryAddressListLinearLayoutCompat = view.findViewById(R.id.deliveryAddressListLinearLayoutCompat);
        deliveryAddressListRecyclerView = view.findViewById(R.id.deliveryAddressListRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        deliveryAddressListRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        deliveryAddressListRecyclerView.setAdapter(deliveryAddressListViewModel.deliveryAddressListRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

//        deliveryAddressListViewModel.getBuyerAllDeliveryAddressMutableLiveData().observe(this, buyerAllDeliveryAddressObserver);
        deliveryAddressListViewModel.getBuyerAllDeliveryAddressMutableLiveData().observe(getViewLifecycleOwner(), buyerAllDeliveryAddressObserver);
//        deliveryAddressListViewModel.getSetDefaultDeliveryAddressMutableLiveData().observe(this, setDefaultDeliveryAddressObserver);
        deliveryAddressListViewModel.getSetDefaultDeliveryAddressMutableLiveData().observe(getViewLifecycleOwner(), setDefaultDeliveryAddressObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        deliveryAddressListViewModel.deliveryAddressListRecyclerViewAdapter.setOnDeliveryAddressListener(this);
        deliveryAddressListSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private final Observer<BuyerAllDeliveryAddress> buyerAllDeliveryAddressObserver = new Observer<BuyerAllDeliveryAddress>() {
        @Override
        public void onChanged(BuyerAllDeliveryAddress buyerAllDeliveryAddress) {
            Log.i(TAG, "onChanged: DELIVERY ADDRESS LIST Observer fired!\nbuyerAllDeliveryAddress: " + buyerAllDeliveryAddress);

            DialogUtil.dismissLoadingDialog();

            if (buyerAllDeliveryAddress.getError() == 200) {
                deliveryAddressListViewModel.deliveryAddressListRecyclerViewAdapter.setCart_cartData_buyerAddressList(buyerAllDeliveryAddress.getData());
            } else {
                Log.i(TAG, "onChanged: Something went wrong while loading all delivery address list.");
                Toast.makeText(requireActivity(), buyerAllDeliveryAddress.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<GeneralResponse> setDefaultDeliveryAddressObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: Set Default Delivery Address Observer fired!\ngeneralResponse: " + generalResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if (generalResponse.getError() == 200) {
                DialogUtil.showCustomSnackbar(requireActivity(), deliveryAddressListLinearLayoutCompat, "Default Address Updated Successfully!", null);

                deliveryAddressListViewModel.updateDefaultDeliveryAddress();
            } else {
                Log.i(TAG, "onChanged: Something went wrong while setting default delivery address!");
                Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();

                deliveryAddressListViewModel.addressId = 0;
            }
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            if (!deliveryAddressListViewModel.getBuyerAllDeliveryAddress()) {
                DialogUtil.showNoInternetToast(requireActivity());
            } else {
                DialogUtil.showLoadingDialog(requireActivity());
            }

            deliveryAddressListSwipeRefreshLayout.setRefreshing(false);
        }
    };

    public void setDeliveryAddressInterface(DeliveryAddressInterface deliveryAddressInterface) {
        this.deliveryAddressInterface = deliveryAddressInterface;
    }

    @Override
    public void setDefaultDeliveryAddress(int addressId) {
        Log.i(TAG, "setDefaultDeliveryAddress: fired!\taddressId: " + addressId);

        if (!deliveryAddressListViewModel.setDefaultDeliveryAddress(addressId)) {
            DialogUtil.showNoInternetToast(requireActivity());
            deliveryAddressListViewModel.deliveryAddressListRecyclerViewAdapter.notifyDeliveryAddressDefault(addressId, false);
            deliveryAddressListViewModel.addressId = 0;
        } else {
            DialogUtil.showProcessingInfoDialog(requireActivity());
        }
    }

    @Override
    public void editDeliveryAddress(Cart_CartData_BuyerAddress cart_cartData_buyerAddress) {
        Log.i(TAG, "editDeliveryAddress: fired!\ncart_cartData_buyerAddress: " + cart_cartData_buyerAddress);

        FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
        fragmentManager1.beginTransaction().remove(ApplicationData.getDeliveryAddressListDialogFragment()).commit();

        ApplicationData.setDeliveryAddressListDialogFragment(null);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DeliveryAddressDialogFragment deliveryAddressDialogFragment = new DeliveryAddressDialogFragment();

        deliveryAddressDialogFragment.setToBeEditDeliveryAddress(cart_cartData_buyerAddress);

        deliveryAddressDialogFragment.setDeliveryAddressInterface(deliveryAddressInterface);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(android.R.id.content, deliveryAddressDialogFragment)
                .addToBackStack(null)
                .commit();

        ApplicationData.setDeliveryAddressDialogFragment(deliveryAddressDialogFragment);
    }
}