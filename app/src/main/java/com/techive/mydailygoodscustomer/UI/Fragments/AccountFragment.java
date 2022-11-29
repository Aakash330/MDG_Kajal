package com.techive.mydailygoodscustomer.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techive.mydailygoodscustomer.Adapters.AccountsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.UI.Activities.ChangePasswordActivity;
import com.techive.mydailygoodscustomer.UI.Activities.OrderHistoryActivity;
import com.techive.mydailygoodscustomer.UI.Activities.StoreLocatorActivity;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.Util.SharedPreferencesManager;
import com.techive.mydailygoodscustomer.ViewModels.AccountViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.util.Arrays.asList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements AccountsRecyclerViewAdapter.AccountOptionsListener {
    private static final String TAG = "AccountFragment";

    private RecyclerView accountRecyclerView;

    private AccountsRecyclerViewAdapter accountsRecyclerViewAdapter;

    private AccountViewModel accountViewModel;

    public AccountFragment() {
        // Required empty public constructor
        Log.i(TAG, "AccountFragment: Empty Constructor fired!");
    }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: fired!");

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired!");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

        initAdapters();

        initObservers();

        initListeners();
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

        if (accountViewModel.getLogoutResponseMutableLiveData().getValue() != null) {
            accountViewModel.setLogoutResponseMutableLiveData(null);
        }
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

        accountRecyclerView = view.findViewById(R.id.accountRecyclerView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        accountsRecyclerViewAdapter = new AccountsRecyclerViewAdapter(
                new ArrayList<>(asList("Order History", "Change Password", "LogOut")),
                new ArrayList<>(asList(getContext().getDrawable(R.drawable.orders_icon), getContext().getDrawable(R.drawable.change_password_icon),
                        getContext().getDrawable(R.drawable.logout_icon))));

        accountRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        accountRecyclerView.setAdapter(accountsRecyclerViewAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        accountViewModel.getLogoutResponseMutableLiveData().observe(this, logoutResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        accountsRecyclerViewAdapter.setAccountOptionsListener(this);
    }

    private final Observer<GeneralResponse> logoutResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: LOGOUT Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (generalResponse.getError() == 200 || generalResponse.getError() == 400) {
                    //PROCEED TO LOGOUT FROM DEVICE
                    logout();
                } else {
                    Log.i(TAG, "onChanged: Something went wrong during logout vendor.");
                    Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: Null Observer fired!");
            }
        }
    };


    private void logout() {
        Log.i(TAG, "logout: fired!");

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(requireActivity());
        SharedPreferences sharedPreferences = sharedPreferencesManager.getBuyerSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.i(TAG, "logout: BEFORE, sharedPreferences.getAll().toString(): " + sharedPreferences.getAll().toString());

        editor.remove(SharedPreferencesManager.userId);
        editor.remove(SharedPreferencesManager.password);
        editor.remove(SharedPreferencesManager.mobNo);
        editor.remove(SharedPreferencesManager.buyerName);

//        editor.clear();

//        editor.apply();
        editor.commit();

        Log.i(TAG, "logout: AFTER PARTIAL CLEAR, sharedPreferences.getAll().toString(): " + sharedPreferences.getAll().toString());

        ApplicationData.setLoggedInBuyerId(0);

        getActivity().finish();
        Toast.makeText(requireActivity(), "You have Logged Out Successfully!", Toast.LENGTH_SHORT).show();

        Intent storeLocatorIntent = new Intent(requireContext(), StoreLocatorActivity.class);
        startActivity(storeLocatorIntent);
    }

    @Override
    public void accountOptionsClick(String optionName) {
        Log.i(TAG, "accountOptionsClick: clicked! optionName: " + optionName);

        switch (optionName) {
            case "Order History": {
                Log.i(TAG, "accountOptionsClick: Order History Clicked!");

                if (ApplicationData.getLoggedInBuyerId() != 0) {
                    Intent orderHistoryIntent = new Intent(requireActivity(), OrderHistoryActivity.class);
                    startActivity(orderHistoryIntent);
                } else {
                    Log.i(TAG, "accountOptionsClick: Order History, Not yet Logged In!");
                    Toast.makeText(requireActivity(), "Please, First LogIn to proceed!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case "LogOut": {
                Log.i(TAG, "accountOptionsClick: LogOut Clicked!");

                if (ApplicationData.getLoggedInBuyerId() != 0) {
                    if (!accountViewModel.logoutCustomer()) {
                        DialogUtil.showNoInternetToast(requireActivity());
                    } else {
                        DialogUtil.showProcessingInfoDialog(requireActivity());
                    }

                    //MODIFIED LOGIC ON 26-8-22 (logout is now online so as to clear the buyer's token from online database)
//                    logout();
                } else {
                    Log.i(TAG, "accountOptionsClick: Logout, Not yet Logged In!");
                    Toast.makeText(requireActivity(), "Please, First LogIn to proceed!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case "Change Password": {
                Log.i(TAG, "accountOptionsClick: Change Password Clicked!");

                if (ApplicationData.getLoggedInBuyerId() != 0) {
                    Intent changePasswordIntent = new Intent(requireActivity(), ChangePasswordActivity.class);
                    startActivity(changePasswordIntent);
                } else {
                    Log.i(TAG, "accountOptionsClick: Change Password, Not yet Logged In!");
                    Toast.makeText(requireActivity(), "Please, First LogIn to proceed!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}