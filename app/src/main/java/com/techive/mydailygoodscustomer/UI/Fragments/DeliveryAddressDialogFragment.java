package com.techive.mydailygoodscustomer.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.BuyerAddAddressModel;
import com.techive.mydailygoodscustomer.Models.Cart_CartData_BuyerAddress;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DeliveryAddressInterface;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.ViewModels.DeliveryAddressViewModel;

import org.jetbrains.annotations.NotNull;

public class DeliveryAddressDialogFragment extends DialogFragment {
    private static final String TAG = "DeliveryAddressDialogFr";

    private NestedScrollView addDeliveryAddressNestedScrollView;

    private TextInputLayout deliveryStateTextInputLayout, deliveryCityTextInputLayout;

    private TextInputEditText deliveryFirstNameTextInputEditText, deliveryLastNameTextInputEditText, deliveryMobTextInputEditText,
            deliveryStreetAddressTextInputEditText, deliveryLandmarkTextInputEditText, deliveryZipcodeTextInputEditText;

    private MaterialAutoCompleteTextView deliveryStateMaterialAutoCompleteTextView, deliveryCityMaterialAutoCompleteTextView;

    private MaterialButton addDeliveryAddressMaterialButton;

    private MaterialTextView deliveryAddressTitleMaterialTextView;

    private DeliveryAddressViewModel deliveryAddressViewModel;

    private Cart_CartData_BuyerAddress toBeEditedDeliveryAddress;

    private DeliveryAddressInterface deliveryAddressInterface;

    public DeliveryAddressDialogFragment() {
        Log.i(TAG, "DeliveryAddressDialogFragment: Empty Constructor fired!");
    }

    public void setToBeEditDeliveryAddress(Cart_CartData_BuyerAddress toBeEditDeliveryAddress) {
        Log.i(TAG, "setToBeEditDeliveryAddress: fired!");

        toBeEditedDeliveryAddress = toBeEditDeliveryAddress;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: fired!");
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: fired!");

//        setStyle(STYLE_NORMAL, 0);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: fired");
        return inflater.inflate(R.layout.fragment_dialog_delivery_address, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: fired!");

        initComponentViews(view);

        deliveryAddressViewModel = new ViewModelProvider(this).get(DeliveryAddressViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        if (toBeEditedDeliveryAddress != null) {
            displayToBeEditedAddress();
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

        addDeliveryAddressNestedScrollView = view.findViewById(R.id.addDeliveryAddressNestedScrollView);

        deliveryStateTextInputLayout = view.findViewById(R.id.deliveryStateTextInputLayout);
        deliveryCityTextInputLayout = view.findViewById(R.id.deliveryCityTextInputLayout);

        deliveryFirstNameTextInputEditText = view.findViewById(R.id.deliveryFirstNameTextInputEditText);
        deliveryLastNameTextInputEditText = view.findViewById(R.id.deliveryLastNameTextInputEditText);
        deliveryMobTextInputEditText = view.findViewById(R.id.deliveryMobTextInputEditText);
        deliveryStreetAddressTextInputEditText = view.findViewById(R.id.deliveryStreetAddressTextInputEditText);
        deliveryLandmarkTextInputEditText = view.findViewById(R.id.deliveryLandmarkTextInputEditText);
        deliveryZipcodeTextInputEditText = view.findViewById(R.id.deliveryZipcodeTextInputEditText);

        deliveryStateMaterialAutoCompleteTextView = view.findViewById(R.id.deliveryStateMaterialAutoCompleteTextView);
        deliveryCityMaterialAutoCompleteTextView = view.findViewById(R.id.deliveryCityMaterialAutoCompleteTextView);

        addDeliveryAddressMaterialButton = view.findViewById(R.id.addDeliveryAddressMaterialButton);

        deliveryAddressTitleMaterialTextView = view.findViewById(R.id.deliveryAddressTitleMaterialTextView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        deliveryStateMaterialAutoCompleteTextView.setAdapter(deliveryAddressViewModel.statesArrayAdapter);
        deliveryCityMaterialAutoCompleteTextView.setAdapter(deliveryAddressViewModel.cityArrayAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        deliveryAddressViewModel.getAddDeliveryAddressMutableLiveData().observe(this, addDeliveryAddressObserver);
        deliveryAddressViewModel.getCityListMutableLiveData().observe(this, cityListObserver);
        deliveryAddressViewModel.getStateListMutableLiveData().observe(this, stateListObserver);
        deliveryAddressViewModel.getUpdateDeliveryAddressMutableLiveData().observe(this, updateDeliveryAddressObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        deliveryStateMaterialAutoCompleteTextView.addTextChangedListener(stateTextWatcher);
        deliveryCityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);

        addDeliveryAddressMaterialButton.setOnClickListener(addDeliveryAddress);

        deliveryStateMaterialAutoCompleteTextView.setOnItemClickListener(onItemClickListener);
    }

    private final Observer<GeneralResponse> addDeliveryAddressObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: AddDeliveryAddress Observer fired!\ngeneralResponse: " + generalResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if (generalResponse.getError() == 200) {
                DialogUtil.showCustomSnackbar(requireActivity(), addDeliveryAddressNestedScrollView,
                        "Delivery Address Added Successfully!", null);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: Inside run in AddDeliveryAddressObserver.");
                        getActivity().getSupportFragmentManager().beginTransaction().remove(
                                ApplicationData.getDeliveryAddressDialogFragment()).commit();
                        ApplicationData.setDeliveryAddressDialogFragment(null);
                    }
                }, 1300); /*1.3 secs*/
            } else {
                Log.i(TAG, "onChanged: Something went wrong while adding buyer address!");
                Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<CityList> cityListObserver = new Observer<CityList>() {
        @Override
        public void onChanged(CityList cityList) {
            Log.i(TAG, "onChanged: CityList Observer fired!\ncityList: " + cityList);

            if (cityList.getError() == 200) {
                deliveryAddressViewModel.initCityDataInMaterialAutoCompleteTextView();

                deliveryAddressViewModel.cityArrayAdapter.getFilter().filter("", null);
            } else {
                Log.i(TAG, "onChanged: Something went wrong while loading more City data.");
                Toast.makeText(requireActivity(), cityList.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<StateList> stateListObserver = new Observer<StateList>() {
        @Override
        public void onChanged(StateList stateList) {
            Log.i(TAG, "onChanged: StateList Observer fired!\nstateList: " + stateList);

            if (stateList.getError() == 200) {
                deliveryAddressViewModel.initStateDataInMaterialAutoCompleteTextView();

                deliveryAddressViewModel.statesArrayAdapter.getFilter().filter("", null);
            } else {
                Log.i(TAG, "onChanged: Something went wrong while loading more State data.");
                Toast.makeText(requireActivity(), stateList.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final Observer<GeneralResponse> updateDeliveryAddressObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: UpdateDeliveryAddress Observer fired!\ngeneralResponse: " + generalResponse);

            DialogUtil.dismissProcessingInfoDialog();

            if (generalResponse.getError() == 200) {
                DialogUtil.showCustomSnackbar(requireActivity(), addDeliveryAddressNestedScrollView,
                        "Delivery Address Updated Successfully!", null);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: Inside run in UpdateDeliveryAddressObserver.");

                        deliveryAddressInterface.displayEditedAddress(toBeEditedDeliveryAddress);

                        getActivity().getSupportFragmentManager().beginTransaction().remove(
                                ApplicationData.getDeliveryAddressDialogFragment()).commit();
                        ApplicationData.setDeliveryAddressDialogFragment(null);
                    }
                }, 1300); /*1.3 secs*/
            } else {
                Log.i(TAG, "onChanged: Something went wrong while updating buyer address!");
                Toast.makeText(requireActivity(), generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final TextWatcher stateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: STATE fired! charSequence: " + charSequence);

            if (charSequence.length() >= 3) {
                if (charSequence.length() <= 8) {
                    if (!deliveryAddressViewModel.stateHashMap.containsKey(charSequence.toString())) {
                        if (!deliveryAddressViewModel.getStateListByStateNameSegment(charSequence.toString())) {
                            DialogUtil.showNoInternetToast(requireActivity());
                        }
                    }
                }
                deliveryStateTextInputLayout.setError(null);
            } else {
                deliveryStateTextInputLayout.setError("Enter at least 3 characters to begin search.");
                deliveryAddressViewModel.selectedStateId = 0;
                deliveryCityMaterialAutoCompleteTextView.removeTextChangedListener(cityTextWatcher);
                deliveryCityMaterialAutoCompleteTextView.setText("");
                deliveryCityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);
                deliveryAddressViewModel.cityArrayAdapter.clear();
                deliveryAddressViewModel.cityArrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final TextWatcher cityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: CITY fired! charSequence: " + charSequence);

            if (deliveryAddressViewModel.selectedStateId != 0) {
                if (charSequence.length() >= 3) {
                    if (charSequence.length() <= 8) {
                        //WILL PREVENT FROM HITTING THE API AGAIN, WHEN THE DESIRED CITY IS SELECTED.
                        if (!deliveryAddressViewModel.cityHashMap.containsKey(charSequence.toString())) {
                            if (!deliveryAddressViewModel.getCityListByCityNameSegment(charSequence.toString(), deliveryAddressViewModel.selectedStateId)) {
                                DialogUtil.showNoInternetToast(requireActivity());
                            }
                        }
                    }
                    deliveryCityTextInputLayout.setError(null);
                } else {
                    deliveryCityTextInputLayout.setError("Enter at least 3 characters to begin search.");
                }
            } else {
                Log.i(TAG, "onTextChanged: Selected state id is 0.");
                Toast.makeText(requireActivity(), "Please first select any state!", Toast.LENGTH_SHORT).show();
                deliveryCityMaterialAutoCompleteTextView.removeTextChangedListener(cityTextWatcher);
                deliveryCityMaterialAutoCompleteTextView.setText("");
                deliveryCityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final View.OnClickListener addDeliveryAddress = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: fired!");

            String fName, lName, mob, streetAddress, landmark = "", zipcode;
            int stateId = -1, cityId = -1;

            //FIRST NAME
            if (!deliveryFirstNameTextInputEditText.getText().toString().matches("")) {
                fName = deliveryFirstNameTextInputEditText.getText().toString();
            } else {
                Toast.makeText(requireActivity(), "First Name is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            //LAST NAME
            if (!deliveryLastNameTextInputEditText.getText().toString().matches("")) {
                lName = deliveryLastNameTextInputEditText.getText().toString();
            } else {
                Toast.makeText(requireActivity(), "Last Name is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            //MOBILE
            if (!deliveryMobTextInputEditText.getText().toString().matches("")) {
                if (deliveryMobTextInputEditText.getText().toString().length() != 10) {
                    Toast.makeText(requireActivity(), "Mobile Number should be of 10 digits.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mob = deliveryMobTextInputEditText.getText().toString();
            } else {
                Toast.makeText(requireActivity(), "Mobile Number is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            //STREET ADDRESS
            if (!deliveryStreetAddressTextInputEditText.getText().toString().matches("")) {
                streetAddress = deliveryStreetAddressTextInputEditText.getText().toString();
            } else {
                Toast.makeText(requireActivity(), "Street Address is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            //LANDMARK
            if (!deliveryLandmarkTextInputEditText.getText().toString().matches("")) {
                landmark = deliveryLandmarkTextInputEditText.getText().toString();
            }

            //ZIPCODE
            if (!deliveryZipcodeTextInputEditText.getText().toString().matches("")) {
                zipcode = deliveryZipcodeTextInputEditText.getText().toString();
            } else {
                Toast.makeText(requireActivity(), "ZipCode is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            //STATE
            if (!deliveryStateMaterialAutoCompleteTextView.getText().toString().matches("")) {
                stateId = deliveryAddressViewModel.stateHashMap.get(deliveryStateMaterialAutoCompleteTextView.getText().toString());
            } else {
                Toast.makeText(requireActivity(), "State is required!", Toast.LENGTH_SHORT).show();
            }

            //CITY
            if (!deliveryCityMaterialAutoCompleteTextView.getText().toString().matches("")) {
                cityId = deliveryAddressViewModel.cityHashMap.get(deliveryCityMaterialAutoCompleteTextView.getText().toString());
            } else {
                Toast.makeText(requireActivity(), "City is required!", Toast.LENGTH_SHORT).show();
            }

            if (stateId == -1 || cityId == -1) {
                Toast.makeText(requireActivity(), "State & City should only be selected from the dropdown.", Toast.LENGTH_SHORT).show();
                return;
            }

            BuyerAddAddressModel buyerAddAddressModel = new BuyerAddAddressModel(ApplicationData.getLoggedInBuyerId(),
                    fName, lName, mob, streetAddress, landmark, stateId, cityId, zipcode);
            Log.i(TAG, "onClick: buyerAddAddressModel: " + buyerAddAddressModel);

            if (addDeliveryAddressMaterialButton.getText().toString().matches(getResources().getString(R.string.update))) {
                Log.i(TAG, "onClick: Proceeding to update!");

                //MODIFYING THE COPY OF EXISTING DELIVERY ADDRESS, TO BE PUSHED WHEN UPDATE IS SUCCESSFUL.
                toBeEditedDeliveryAddress.setName(fName);
                toBeEditedDeliveryAddress.setLname(lName);
                toBeEditedDeliveryAddress.setMobile(mob);
                toBeEditedDeliveryAddress.setAddress(streetAddress);
                toBeEditedDeliveryAddress.setLandmark(landmark);
                toBeEditedDeliveryAddress.setState(deliveryStateMaterialAutoCompleteTextView.getText().toString());
                toBeEditedDeliveryAddress.setCity(deliveryCityMaterialAutoCompleteTextView.getText().toString());
                toBeEditedDeliveryAddress.setPincode(zipcode);
                toBeEditedDeliveryAddress.setState_id(stateId);
                toBeEditedDeliveryAddress.setCity_id(cityId);

                buyerAddAddressModel.setAddrsId(toBeEditedDeliveryAddress.getId());
                if (!deliveryAddressViewModel.updateDeliveryAddress(buyerAddAddressModel)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showProcessingInfoDialog(requireActivity());
                }
            } else {
                Log.i(TAG, "onClick: Proceeding to add!");
                if (!deliveryAddressViewModel.addDeliveryAddress(buyerAddAddressModel)) {
                    DialogUtil.showNoInternetToast(requireActivity());
                } else {
                    DialogUtil.showProcessingInfoDialog(requireActivity());
                }
            }
        }
    };

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemClick: adapterView.getItemAtPosition(i): " + adapterView.getItemAtPosition(i));

            deliveryAddressViewModel.selectedStateId = deliveryAddressViewModel.stateHashMap.get(adapterView.getItemAtPosition(i));
            Log.i(TAG, "onItemSelected: deliveryAddressViewModel.selectedStateId: " + deliveryAddressViewModel.selectedStateId);
        }
    };

    private void displayToBeEditedAddress() {
        Log.i(TAG, "displayToBeEditedAddress: fired!");

        deliveryAddressTitleMaterialTextView.setText(getResources().getString(R.string.update_delivery_address));
        addDeliveryAddressMaterialButton.setText(getResources().getString(R.string.update));

        deliveryFirstNameTextInputEditText.setText(toBeEditedDeliveryAddress.getName());
        deliveryLastNameTextInputEditText.setText(toBeEditedDeliveryAddress.getLname());
        deliveryMobTextInputEditText.setText(toBeEditedDeliveryAddress.getMobile());
        deliveryStreetAddressTextInputEditText.setText(toBeEditedDeliveryAddress.getAddress());
        deliveryLandmarkTextInputEditText.setText(toBeEditedDeliveryAddress.getLandmark());
        deliveryZipcodeTextInputEditText.setText(toBeEditedDeliveryAddress.getPincode());
        deliveryStateMaterialAutoCompleteTextView.setText(toBeEditedDeliveryAddress.getState());
        deliveryCityMaterialAutoCompleteTextView.removeTextChangedListener(cityTextWatcher);
        deliveryCityMaterialAutoCompleteTextView.setText(toBeEditedDeliveryAddress.getCity());
        deliveryCityMaterialAutoCompleteTextView.addTextChangedListener(cityTextWatcher);
        Log.i(TAG, "displayToBeEditedAddress: toBeEditedDeliveryAddress.getCity_id(): " + toBeEditedDeliveryAddress.getCity_id());
        Log.i(TAG, "displayToBeEditedAddress: BEFORE, deliveryAddressViewModel.selectedStateId: " + deliveryAddressViewModel.selectedStateId);
        deliveryAddressViewModel.selectedStateId = toBeEditedDeliveryAddress.getState_id();
        Log.i(TAG, "displayToBeEditedAddress: AFTER, deliveryAddressViewModel.selectedStateId: " + deliveryAddressViewModel.selectedStateId);

        deliveryAddressViewModel.stateHashMap.put(toBeEditedDeliveryAddress.getState(), toBeEditedDeliveryAddress.getState_id());
        deliveryAddressViewModel.cityHashMap.put(toBeEditedDeliveryAddress.getCity(), toBeEditedDeliveryAddress.getCity_id());
    }

    public void setDeliveryAddressInterface(DeliveryAddressInterface deliveryAddressInterface) {
        this.deliveryAddressInterface = deliveryAddressInterface;
    }
}
