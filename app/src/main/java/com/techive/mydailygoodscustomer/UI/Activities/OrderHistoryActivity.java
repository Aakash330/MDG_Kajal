package com.techive.mydailygoodscustomer.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;
import com.techive.mydailygoodscustomer.Adapters.OrderHistoryRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Adapters.OrderProductsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.OrderHistory;
import com.techive.mydailygoodscustomer.Models.OrderHistory_Data;
import com.techive.mydailygoodscustomer.Models.OrderHistory_Data_BuyerAddress;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.ViewModels.OrderHistoryViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryRecyclerViewAdapter.OrderHistoryClickListener {
    private static final String TAG = "OrderHistoryActivity";
    private static final int READ_WRITE_REQUEST_CODE = 1;

    private RecyclerView orderHistoryRecyclerView;

    private MaterialAutoCompleteTextView calendarMonthMaterialAutoCompleteTextView;

    private AppCompatImageButton minusYearAppCompatImageButton, plusYearAppCompatImageButton;

    private MaterialTextView calendarYearMaterialTextView;

    private OrderHistoryViewModel orderHistoryViewModel;

    private OrderHistory_Data orderHistory_data;

    private SimpleDateFormat simpleDateFormat;

    private String strMonth = "", strYear = "";

    private int month, year;

    private ActivityResultLauncher<Intent> receiptActivityResultLauncher;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        setTitle(getString(R.string.order_history));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();

        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        //WILL BY DEFAULT SHOW THE CALENDAR FOR THE CURRENT MONTH.
        Calendar calendar1 = Calendar.getInstance();
        String strCurrentDate = simpleDateFormat.format(calendar1.getTime());
        Log.i(TAG, "onCreate: strCurrentDate: " + strCurrentDate);

        try {
            /*yyyy-MM-dd*/
            strMonth = strCurrentDate.substring(5, 7);
            strYear = strCurrentDate.substring(0, 4);
            Log.i(TAG, "onCreate: strMonth: " + strMonth + "\tstrYear: " + strYear);

            month = Integer.parseInt(strMonth);
            year = Integer.parseInt(strYear);
            calendarMonthMaterialAutoCompleteTextView.setText(strMonth, false);
            calendarYearMaterialTextView.setText(strYear);
        } catch (Exception exception) {
            Log.i(TAG, "onCreate: Exception caught while trying to get month & year from currentDate" +
                    "\nexception.getMessage(): " + exception.getMessage());
            exception.printStackTrace();
            calendarYearMaterialTextView.setText(strYear);
        }

        if (!orderHistoryViewModel.getOrderHistory(month, year)) {
            DialogUtil.showNoInternetToast(this);
        } else {
            DialogUtil.showLoadingDialog(this);
        }

        receiptActivityResultLauncher = registerReceiptActivityResultLauncher();
    }

    // FOR BACK BUTTON PRESS FROM THE TOOLBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (requestCode == READ_WRITE_REQUEST_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: Storage Permission granted upon request.");
//                    saveReceiptToFile(orderHistory_data);
                    saveReceiptToFile2();
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: Storage Permission denied upon request.");
                    Toast.makeText(this, "External Storage permission required to save the Receipt file.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initComponents() {
        Log.i(TAG, "initComponents: fired!");

        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);

        calendarMonthMaterialAutoCompleteTextView = findViewById(R.id.calendarMonthMaterialAutoCompleteTextView);

        minusYearAppCompatImageButton = findViewById(R.id.minusYearAppCompatImageButton);
        plusYearAppCompatImageButton = findViewById(R.id.plusYearAppCompatImageButton);

        calendarYearMaterialTextView = findViewById(R.id.calendarYearMaterialTextView);
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryRecyclerView.setAdapter(orderHistoryViewModel.orderHistoryRecyclerViewAdapter);

        calendarMonthMaterialAutoCompleteTextView.setAdapter(orderHistoryViewModel.monthsArrayAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        orderHistoryViewModel.getOrderHistoryMutableLiveData().observe(this, orderHistoryObserver);
        orderHistoryViewModel.getCancelOrderMutableLiveData().observe(this, cancelOrderObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        orderHistoryViewModel.orderHistoryRecyclerViewAdapter.setOrderClickListener(this);

        minusYearAppCompatImageButton.setOnClickListener(minusPlusYearOnClickListener);
        plusYearAppCompatImageButton.setOnClickListener(minusPlusYearOnClickListener);

        calendarMonthMaterialAutoCompleteTextView.setOnItemClickListener(monthOnItemClickListener);
    }

    private final Observer<OrderHistory> orderHistoryObserver = new Observer<OrderHistory>() {
        @Override
        public void onChanged(OrderHistory orderHistory) {
            Log.i(TAG, "onChanged: ORDER HISTORY Observer fired!\norderHistory: " + orderHistory);

            DialogUtil.dismissLoadingDialog();

            if (orderHistory.getError() == 200) {
                DialogUtil.dismissCustomSnackBar();
                orderHistoryViewModel.orderHistoryRecyclerViewAdapter.setOrderHistory_dataList(orderHistory.getData());
            } else {
                Log.i(TAG, "onChanged: Something went wrong while loading OrderHistory!");
                orderHistoryViewModel.orderHistoryRecyclerViewAdapter.setOrderHistory_dataList(orderHistory.getData());

                if (orderHistory.getData() != null && orderHistory.getData().size() == 0) {
                    DialogUtil.showCustomSnackbar(OrderHistoryActivity.this, orderHistoryRecyclerView, orderHistory.getMsg(), -2);
                } else {
                    DialogUtil.dismissCustomSnackBar();
                    Toast.makeText(OrderHistoryActivity.this, orderHistory.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private final Observer<OrderHistory> cancelOrderObserver = new Observer<OrderHistory>() {
        @Override
        public void onChanged(OrderHistory orderHistory) {
            Log.i(TAG, "onChanged: CANCEL ORDER Observer fired!\norderHistory: " + orderHistory);

            DialogUtil.dismissProcessingInfoDialog();

            bottomSheetDialog.dismiss();

            if (orderHistory.getError() == 200) {
                DialogUtil.dismissCustomSnackBar();
                orderHistoryViewModel.orderHistoryRecyclerViewAdapter.setOrderHistory_dataList(orderHistory.getData());
            } else {
                Log.i(TAG, "onChanged: Something went wrong while canceling order!");
                Toast.makeText(OrderHistoryActivity.this, orderHistory.getMsg(), Toast.LENGTH_SHORT).show();

                //                orderHistoryViewModel.orderHistoryRecyclerViewAdapter.setOrderHistory_dataList(orderHistory.getData());

//                if (orderHistory.getData() != null && orderHistory.getData().size() == 0) {
//                    DialogUtil.showCustomSnackbar(OrderHistoryActivity.this, orderHistoryRecyclerView, orderHistory.getMsg(), -2);
//                } else {
//                    DialogUtil.dismissCustomSnackBar();
//                    Toast.makeText(OrderHistoryActivity.this, orderHistory.getMsg(), Toast.LENGTH_SHORT).show();
//                }
            }
        }
    };

    private final View.OnClickListener minusPlusYearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: One of minus/plus appCompatImageButton clicked!");

            int year = Integer.parseInt(calendarYearMaterialTextView.getText().toString());

            if (view.getId() == plusYearAppCompatImageButton.getId()) {
                //INCREMENT YEAR BY 1
                Log.i(TAG, "onClick: Incrementing year from " + year);
                year = year + 1;
            } else if (view.getId() == minusYearAppCompatImageButton.getId()) {
                //DECREMENT YEAR BY 1
                Log.i(TAG, "onClick: Decrementing year from " + year);
                year = year - 1;
            }

            //NOW WILL HIT THE CALENDAR API FROM HERE.
            if (calendarMonthMaterialAutoCompleteTextView.getText() != null) {
                String strMonth = calendarMonthMaterialAutoCompleteTextView.getText().toString();
                try {
                    if (!orderHistoryViewModel.getOrderHistory(Integer.parseInt(strMonth), year)) {
                        Toast.makeText(OrderHistoryActivity.this, "Please provide Internet Access & try again!", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtil.showLoadingDialog(OrderHistoryActivity.this);
                        calendarYearMaterialTextView.setText(String.valueOf(year));
                        month = Integer.parseInt(strMonth);
                        OrderHistoryActivity.this.year = year;
                    }
                } catch (Exception exception) {
                    Log.i(TAG, "onClick: exception.getMessage(): " + exception.getMessage());
                    exception.printStackTrace();
                    Toast.makeText(OrderHistoryActivity.this, "Exception Occurred while getting Order History." +
                            "\nPlease try again later!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onClick: Unable to get Calendar month. Contact Admin!");
            }
        }
    };

    private final AdapterView.OnItemClickListener monthOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemClick: adapterView.getItemAtPosition(i): " + adapterView.getItemAtPosition(i));

            month = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            year = Integer.parseInt(calendarYearMaterialTextView.getText().toString());

            //WILL HIT THE API FROM HERE TO FETCH THE CALENDAR
            if (!orderHistoryViewModel.getOrderHistory(month, year)) {
                Toast.makeText(OrderHistoryActivity.this, "Please provide Internet Access & try again!", Toast.LENGTH_SHORT).show();
            } else {
                DialogUtil.showLoadingDialog(OrderHistoryActivity.this);
            }
        }
    };

    /*SHOW BOTTOM SHEET DIALOG.*/
    private void showOrderProductsBottomSheetDialog(OrderHistory_Data orderHistory_data) {
        Log.i(TAG, "showOrderProductsBottomSheetDialog: fired!\norderHistory_data: " + orderHistory_data);

        this.orderHistory_data = orderHistory_data;

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_order_products);

        OrderProductsRecyclerViewAdapter orderProductsRecyclerViewAdapter = new OrderProductsRecyclerViewAdapter(this);

        RecyclerView orderProductsRecyclerView = bottomSheetDialog.findViewById(R.id.orderProductsRecyclerView);

        MaterialTextView orderedFromStoreMaterialTextView = bottomSheetDialog.findViewById(R.id.orderedFromStoreMaterialTextView);
        MaterialTextView itemsTotalPriceMaterialTextView = bottomSheetDialog.findViewById(R.id.itemsTotalPriceMaterialTextView);
        MaterialTextView itemsTotalSavingsMaterialTextView = bottomSheetDialog.findViewById(R.id.itemsTotalSavingsMaterialTextView);
        MaterialTextView itemsDeliveryChargeMaterialTextView = bottomSheetDialog.findViewById(R.id.itemsDeliveryChargeMaterialTextView);
        MaterialTextView itemsGrossTotalPriceMaterialTextView = bottomSheetDialog.findViewById(R.id.itemsGrossTotalPriceMaterialTextView);

        MaterialButton cancelOrderMaterialButton = bottomSheetDialog.findViewById(R.id.cancelOrderMaterialButton);
        MaterialButton printReceiptMaterialButton = bottomSheetDialog.findViewById(R.id.printReceiptMaterialButton);

        orderProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderProductsRecyclerView.setAdapter(orderProductsRecyclerViewAdapter);

        orderProductsRecyclerViewAdapter.setOrderHistory_data_productsList(orderHistory_data.getProducts(), orderHistory_data.getImagepath());

        orderedFromStoreMaterialTextView.setText("Ordered From: " + orderHistory_data.getStore_name());
        itemsTotalPriceMaterialTextView.setText("Items Total Price: " + getString(R.string.rupee_symbol) + " " +
                ((orderHistory_data.getTotal() + orderHistory_data.getTotal_saving() + orderHistory_data.getDiscount()) /*- orderHistory_data.getDelivery_change()*/));
        itemsTotalSavingsMaterialTextView.setText("Items Total Savings: " + getString(R.string.rupee_symbol) + " " + (orderHistory_data.getTotal_saving() + orderHistory_data.getDiscount()));
        itemsDeliveryChargeMaterialTextView.setText("Delivery Charges: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getDelivery_change());
        itemsGrossTotalPriceMaterialTextView.setText("Items Gross Total Price: " + getString(R.string.rupee_symbol) + " " + (orderHistory_data.getTotal() + orderHistory_data.getDelivery_change()));

        ///////////Checking for OrderStatus & Status for Cancel Availability.
        String strOrderStatus = orderHistory_data.getOrder_status();
        String strStatus = orderHistory_data.getStatus();
        boolean cancelAvailability = false;
        Log.i(TAG, "showOrderProductsBottomSheetDialog: strOrderStatus: " + strOrderStatus + "\tstrStatus: " + strStatus);
        if (strOrderStatus.matches("0") || strOrderStatus.matches("1")) {
            Log.i(TAG, "showOrderProductsBottomSheetDialog: Proceeding to check for Status...");
            if (strStatus == null) {
                cancelAvailability = true;
            } else {
                if (strStatus.matches("7")) {
                    cancelAvailability = true;
                } else {
                    Log.i(TAG, "showOrderProductsBottomSheetDialog: Not Allowing Cancel after Status check!");
                    cancelOrderMaterialButton.setVisibility(View.GONE);
                }
            }
        } else {
            Log.i(TAG, "showOrderProductsBottomSheetDialog: Not Allowing Cancel after OrderStatus check!");
            cancelOrderMaterialButton.setVisibility(View.GONE);
        }

        if (cancelAvailability) {
            DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.i(TAG, "onClick: Yes/No clicked! i: " + i);

                    if (i == -1) {  //YES
                        Log.i(TAG, "onClick: Initiate Cancel request!");

                        int month = Integer.parseInt(calendarMonthMaterialAutoCompleteTextView.getText().toString());
                        int year = Integer.parseInt(calendarYearMaterialTextView.getText().toString());

                        if (!orderHistoryViewModel.cancelOrder(orderHistory_data.getInvoiceId(), month, year)) {
                            DialogUtil.showNoInternetToast(OrderHistoryActivity.this);
                        } else {
                            DialogUtil.showProcessingInfoDialog(OrderHistoryActivity.this);
                        }
                    } else {        //NO
                        Log.i(TAG, "onClick: Not gonna cancel!");
                    }
                }
            };

            cancelOrderMaterialButton.setOnClickListener(view -> {
                Log.i(TAG, "onClick: Cancel Order clicked!");

                //SHOW A DIALOG ASKING FOR CANCEL CONFIRMATION.
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Cancel Order Confirmation?")
                        .setMessage("Are you sure you want to cancel your Order?")
                        .setIcon(R.drawable.ic_warning_24)
                        .setNegativeButton("No", dialogOnClickListener)
                        .setPositiveButton("Yes", dialogOnClickListener)
                        .show();
            });
        }

        printReceiptMaterialButton.setOnClickListener(view -> {
            Log.i(TAG, "onClick: Print Receipt clicked!");

            if (ActivityCompat.checkSelfPermission(OrderHistoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onClick: Requesting storage permission now!");
                //REQUEST PERMISSION
                ActivityCompat.requestPermissions(OrderHistoryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, READ_WRITE_REQUEST_CODE);
            } else {
                Log.i(TAG, "onClick: Storage permission previously granted. Proceeding to save contents.");
//                saveReceiptToFile(orderHistory_data);
                saveReceiptToFile2();
            }
        });

        bottomSheetDialog.show();
    }

    private void saveReceiptToFile(OrderHistory_Data orderHistory_data) {
        Log.i(TAG, "saveReceiptToFile: fired!");

        File storageDir;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "saveReceiptToFile: Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
            storageDir = new File(Environment.getExternalStorageDirectory() + "/MyDailyGoodsCustomer/Receipts");
        } else {
            Log.i(TAG, "saveReceiptToFile: Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
            storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/Receipts");
        }

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String orderId_OrderDate = "#" + orderHistory_data.getInvoiceId() + "_"
                + orderHistory_data.getCreated_at().substring(0, 11);

        String orderDetailsToBeSaved = "InvoiceId: #" + orderHistory_data.getInvoiceId()
                + "\nDate Ordered: " + orderHistory_data.getCreated_at();

        if (orderHistory_data.getBuyerAddress() != null) {
            orderDetailsToBeSaved = orderDetailsToBeSaved.concat("\nCustomer Address: " + orderHistory_data.getBuyerAddress());
        }

        orderDetailsToBeSaved = orderDetailsToBeSaved.concat("\n\nORDER DETAILS::").concat("\n Total Item Count: " + orderHistory_data.getQuantity() + "\n");

        StringBuilder order_productDetailsToBeSaved = new StringBuilder();
        for (int i = 0; i < orderHistory_data.getProducts().size(); i++) {
            order_productDetailsToBeSaved.append("\nProduct: ")
                    .append(orderHistory_data.getProducts().get(i).getName())
                    .append("\nQuantity: ").append(orderHistory_data.getProducts().get(i).getNew_qty())
                    .append(" x ").append(getString(R.string.rupee_symbol)).append(" ").append(orderHistory_data.getProducts().get(i).getSale_price())
                    .append("\t\t").append(getString(R.string.rupee_symbol)).append(" ").append(orderHistory_data.getProducts().get(i).getTotalPrice())
                    .append("\n--------------------");
        }

        String deliveryType = orderHistory_data.getSelf_pickup() == 1 ? "Self-Pickup" : "Home Delivery";

        String order_priceDetailsToBeSaved = "\n\nDelivery Type: " + deliveryType
                + "\nDiscount: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getDiscount()
                + "\nDelivery Charge: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getDelivery_change()
                + "\nGrand Total Price: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getTotal()
                + "\n\n==================== END ====================";

        Log.i(TAG, "saveReceiptToFile: orderDetailsToBeSaved + order_productDetailsToBeSaved + order_priceDetailsToBeSaved:\n"
                + orderDetailsToBeSaved + "\t\t" + order_productDetailsToBeSaved.toString() + "\t\t" + order_priceDetailsToBeSaved);

        File file = new File(storageDir + "/" + orderId_OrderDate + ".txt");
        try {
            boolean fileCreateStatus = file.createNewFile();
            Log.i(TAG, "saveReceiptToFile: fileCreateStatus: " + fileCreateStatus);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write((orderDetailsToBeSaved + order_productDetailsToBeSaved.toString() + order_priceDetailsToBeSaved).getBytes());
            fileOutputStream.close();
            Log.i(TAG, "saveReceiptToFile: Receipt successfully saved to file.");
            Toast.makeText(this, "Your receipt was successfully saved.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.i(TAG, "saveReceiptToFile: e.getMessage(): " + e.getMessage());
            Toast.makeText(this, "A problem occurred in saving your receipt.\nPlease try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /*2-7-22 - Success in writing through the OutputStream. Doing through this way because permanent storage of files is
    not possible in any directory other than the app specific directory.*/
    private void saveReceiptToFile2() {
        Log.i(TAG, "saveReceiptToFile2: fired!");

        String orderId_OrderDate = "#" + orderHistory_data.getInvoiceId() + "_"
                + orderHistory_data.getCreated_at().substring(0, 11) + System.currentTimeMillis();

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/txt");
        intent.putExtra(Intent.EXTRA_TITLE, orderId_OrderDate + ".txt");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        receiptActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> registerReceiptActivityResultLauncher() {
        Log.i(TAG, "registerReceiptActivityResultLauncher: fired!");

        ActivityResultContracts.StartActivityForResult startActivityForResult = new ActivityResultContracts.StartActivityForResult();
        ActivityResultCallback<ActivityResult> receiptActivityResultCallback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.i(TAG, "onActivityResult: fired! Inside Receipt Activity Launcher!");

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.i(TAG, "onActivityResult: Receipt Result was ok!");

                    if (result.getData() != null) {
                        String orderDetailsToBeSaved = "InvoiceId: #" + orderHistory_data.getInvoiceId()
                                + "\nDate Ordered: " + orderHistory_data.getCreated_at();

                        if (orderHistory_data.getBuyerAddress() != null) {
                            String buyerAddress;

                            OrderHistory_Data_BuyerAddress orderHistory_data_buyerAddress = orderHistory_data.getBuyerAddress();

                            buyerAddress = orderHistory_data_buyerAddress.getFirstname() + " " + orderHistory_data_buyerAddress.getLastname()
                            + "\nMob No. " + orderHistory_data_buyerAddress.getMobile() + "\n" + orderHistory_data_buyerAddress.getAddress();

                            if (orderHistory_data_buyerAddress.getLandmark() != null && !orderHistory_data_buyerAddress.getLandmark().matches("")) {
                                buyerAddress = buyerAddress.concat("\n" + orderHistory_data_buyerAddress.getLandmark());
                            }

                            buyerAddress = buyerAddress.concat("\n" + orderHistory_data_buyerAddress.getCity() + ", "
                                    + orderHistory_data_buyerAddress.getState() + ", " + orderHistory_data_buyerAddress.getPincode());

                            orderDetailsToBeSaved = orderDetailsToBeSaved.concat("\n\nCustomer Address:\n" + buyerAddress);
                        }

                        String paymentMode = orderHistory_data.getOnline_payment().matches("0") ? "COD" : "Online Payment";

                        orderDetailsToBeSaved = orderDetailsToBeSaved.concat("\n\nORDER DETAILS::")
                                .concat("\n Ordered From: " + orderHistory_data.getStore_name() + "\n")
                                .concat("\n Payment Mode: " + paymentMode + "\n")
                                .concat("\n Total Item Count: " + orderHistory_data.getQuantity() + "\n");

                        StringBuilder order_productDetailsToBeSaved = new StringBuilder();
                        for (int i = 0; i < orderHistory_data.getProducts().size(); i++) {
                            order_productDetailsToBeSaved.append("\nProduct: ")
                                    .append(orderHistory_data.getProducts().get(i).getName())
                                    .append("\nQuantity: ").append(orderHistory_data.getProducts().get(i).getNew_qty())
                                    .append(" x ").append(getString(R.string.rupee_symbol)).append(" ").append(orderHistory_data.getProducts().get(i).getSale_price())
                                    .append("\t\t").append(getString(R.string.rupee_symbol)).append(" ").append(orderHistory_data.getProducts().get(i).getTotalPrice())
                                    .append("\n--------------------");
                        }

                        String deliveryType = orderHistory_data.getSelf_pickup() == 1 ? "Self-Pickup" : "Home Delivery";

                        String order_priceDetailsToBeSaved = "\n\nDelivery Type: " + deliveryType
                                + "\nDiscount: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getDiscount()
                                + "\nDelivery Charge: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getDelivery_change()
                                + "\nGrand Total Price: " + getString(R.string.rupee_symbol) + " " + orderHistory_data.getTotal()
                                + "\n\n==================== END ====================";

                        Log.i(TAG, "onActivityResult: orderDetailsToBeSaved + order_productDetailsToBeSaved + order_priceDetailsToBeSaved:\n"
                                + orderDetailsToBeSaved + "\t\t" + order_productDetailsToBeSaved.toString() + "\t\t" + order_priceDetailsToBeSaved);

                        try {
                            OutputStream outputStream = getContentResolver().openOutputStream(result.getData().getData());
                            outputStream.write((orderDetailsToBeSaved + order_productDetailsToBeSaved + order_priceDetailsToBeSaved).getBytes());
                            outputStream.flush();
                            outputStream.close();
                            Log.i(TAG, "onActivityResult: After using output stream to write to a file.");
                            Toast.makeText(OrderHistoryActivity.this, "Your Receipt was Successfully Saved to the location you chose!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.i(TAG, "onActivityResult: Exception caught while writing to file!");
                            e.printStackTrace();
                            Toast.makeText(OrderHistoryActivity.this, "Sorry, Your Receipt couldn't be saved!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i(TAG, "onActivityResult: result.getData(): null");
                        Toast.makeText(OrderHistoryActivity.this, "Sorry, A problem occurred while saving your Receipt!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i(TAG, "onActivityResult: Receipt result was not ok!");
                    Toast.makeText(OrderHistoryActivity.this, "You didn't save your Receipt!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        receiptActivityResultLauncher = registerForActivityResult(startActivityForResult, receiptActivityResultCallback);
        return receiptActivityResultLauncher;
    }

    @Override
    public void orderClick(OrderHistory_Data orderHistory_data) {
        Log.i(TAG, "orderClick: fired!");

        //WILL OPEN A BOTTOM SHEET DIALOG FROM HERE
        showOrderProductsBottomSheetDialog(orderHistory_data);
    }

}