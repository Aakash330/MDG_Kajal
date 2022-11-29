package com.techive.mydailygoodscustomer.UI.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.smarteist.autoimageslider.SliderView;
import com.techive.mydailygoodscustomer.Adapters.AllRatingsRecyclerViewAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductOwnRating;
import com.techive.mydailygoodscustomer.Models.ProductOwnRating_Data;
import com.techive.mydailygoodscustomer.Models.ProductReviewModel;
import com.techive.mydailygoodscustomer.Models.SingleProduct;
import com.techive.mydailygoodscustomer.Models.SingleProduct_Product;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;
import com.techive.mydailygoodscustomer.Models.ViewShopRating_Data;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.DialogUtil;
import com.techive.mydailygoodscustomer.ViewModels.ProductDetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailsActivity";

    private SwipeRefreshLayout productDetailsSwipeRefreshLayout;

    private SliderView productImagesSliderView;

    private MaterialButton decMaterialButton, incMaterialButton, addToCartMaterialButton, rateProductMaterialButton;

    private MaterialTextView productNameMaterialTextView, productSDMaterialTextView, productWeightMaterialTextView,
            productPriceMaterialTextView, productLDMaterialTextView, qtyMaterialTextView, viewAllProductRatingsMaterialTextView,
            productRatingAverageMaterialTextView;

    private ProductDetailsViewModel productDetailsViewModel;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTitle("Product Details");
        Log.i(TAG, "onCreate: fired!");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponentViews();

        productDetailsViewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);

        initAdapters();

        initObservers();

        initListeners();

        int productId = getIntent().getIntExtra("PRODUCT_ID", 0);
        Log.i(TAG, "onCreate: productId: " + productId);
        if (!productDetailsViewModel.getSingleProductDetails(productId)) {
            DialogUtil.showNoInternetToast(this);
        } else {
            DialogUtil.showLoadingDialog(this);
        }

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: fired!");

        if (productDetailsViewModel.getAddToCartMutableLiveData().getValue() != null) {
            productDetailsViewModel.setAddToCartMutableLiveData(null);
        }
        if (productDetailsViewModel.getRateProductMutableLiveData().getValue() != null) {
            productDetailsViewModel.setRateProductMutableLiveData(null);
        }
        if (productDetailsViewModel.getViewAllProductRatingsMutableLiveData().getValue() != null) {
            productDetailsViewModel.setViewAllProductRatingsMutableLiveData(null);
        }
        if (productDetailsViewModel.getViewOwnProductRatingMutableLiveData().getValue() != null) {
            productDetailsViewModel.setViewOwnProductRatingMutableLiveData(null);
        }

        if (productDetailsViewModel.getProdAddAcceptResponseMutableLiveData().getValue() != null) {
            productDetailsViewModel.setProdAddAcceptResponseMutableLiveData(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: fired!");

        productDetailsViewModel.setSingleProductMutableLiveData(null);
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

    private void initComponentViews() {
        Log.i(TAG, "initComponentViews: fired!");

        productDetailsSwipeRefreshLayout = findViewById(R.id.productDetailsSwipeRefreshLayout);

        productImagesSliderView = findViewById(R.id.productImagesSliderView);

        decMaterialButton = findViewById(R.id.decMaterialButton);
        incMaterialButton = findViewById(R.id.incMaterialButton);
        addToCartMaterialButton = findViewById(R.id.addToCartMaterialButton);
        rateProductMaterialButton = findViewById(R.id.rateProductMaterialButton);

        productNameMaterialTextView = findViewById(R.id.productNameMaterialTextView);
        productSDMaterialTextView = findViewById(R.id.productSDMaterialTextView);
        productWeightMaterialTextView = findViewById(R.id.productWeightMaterialTextView);
        productPriceMaterialTextView = findViewById(R.id.productPriceMaterialTextView);
        qtyMaterialTextView = findViewById(R.id.qtyMaterialTextView);
        productLDMaterialTextView = findViewById(R.id.productLDMaterialTextView);
        viewAllProductRatingsMaterialTextView = findViewById(R.id.viewAllProductRatingsMaterialTextView);
        productRatingAverageMaterialTextView = findViewById(R.id.productRatingAverageMaterialTextView);

        productImagesSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        productImagesSliderView.setScrollTimeInSec(3);
        productImagesSliderView.setAutoCycle(true);
        productImagesSliderView.startAutoCycle();
    }

    private void initAdapters() {
        Log.i(TAG, "initAdapters: fired!");

        productImagesSliderView.setSliderAdapter(productDetailsViewModel.productDetailsImagesSliderAdapter);
    }

    private void initObservers() {
        Log.i(TAG, "initObservers: fired!");

        productDetailsViewModel.getSingleProductMutableLiveData().observe(this, singleProductObserver);
        productDetailsViewModel.getAddToCartMutableLiveData().observe(this, addToCartResponseObserver);
        productDetailsViewModel.getRateProductMutableLiveData().observe(this, rateProductResponseObserver);
        productDetailsViewModel.getViewAllProductRatingsMutableLiveData().observe(this, productAllRatingsObserver);
        productDetailsViewModel.getViewOwnProductRatingMutableLiveData().observe(this, productOwnRatingObserver);
        productDetailsViewModel.getProdAddAcceptResponseMutableLiveData().observe(this, prodAddAcceptResponseObserver);
    }

    private void initListeners() {
        Log.i(TAG, "initListeners: fired!");

        productDetailsSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        decMaterialButton.setOnClickListener(onClickListener);
        incMaterialButton.setOnClickListener(onClickListener);
        addToCartMaterialButton.setOnClickListener(onClickListener);
        rateProductMaterialButton.setOnClickListener(onClickListener);
        viewAllProductRatingsMaterialTextView.setOnClickListener(onClickListener);
    }

    private final Observer<SingleProduct> singleProductObserver = new Observer<SingleProduct>() {
        @Override
        public void onChanged(SingleProduct singleProduct) {
            Log.i(TAG, "onChanged: SINGLE PRODUCT DETAILS Observer fired!\nsingleProduct: " + singleProduct);

            if (singleProduct != null) {
                DialogUtil.dismissLoadingDialog();

                if (singleProduct.getError() == 200) {
                    displayProductDetails(singleProduct.getProducts());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong during loading Product Details!");
                    Toast.makeText(ProductDetailsActivity.this, singleProduct.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> addToCartResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: ADD TO CART Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();

                if (generalResponse.getError() == 200) {
                    Toast.makeText(ProductDetailsActivity.this, "Your products were successfully added to your cart!", Toast.LENGTH_SHORT).show();
                    //UPDATE DATA TO REFLECT ON CART BADGE

                    productDetailsViewModel.updateCartData(getIntent().getIntExtra("PRODUCT_ID", 0));
                    productDetailsViewModel.updateCartCount();
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while adding products to cart!");
                    Toast.makeText(ProductDetailsActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<GeneralResponse> rateProductResponseObserver = new Observer<GeneralResponse>() {
        @Override
        public void onChanged(GeneralResponse generalResponse) {
            Log.i(TAG, "onChanged: RATE PRODUCT Observer fired!\ngeneralResponse: " + generalResponse);

            if (generalResponse != null) {
                DialogUtil.dismissProcessingInfoDialog();
                bottomSheetDialog.dismiss();

                if (generalResponse.getError() == 200) {
                    Toast.makeText(ProductDetailsActivity.this, "Your Product Review was Updated Successfully!", Toast.LENGTH_SHORT).show();

                    //UPDATE PRODUCT REVIEW TO REFLECT IN THE ALREADY LOADED PRODUCT DATA.
                    productDetailsViewModel.updateProductReview();
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while rating product!");
                    Toast.makeText(ProductDetailsActivity.this, generalResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st time observer fired!");
            }
        }
    };

    private final Observer<ViewShopRating> productAllRatingsObserver = new Observer<ViewShopRating>() {
        @Override
        public void onChanged(ViewShopRating productAllRatings) {
            Log.i(TAG, "onChanged: PRODUCT ALL RATINGS Observer fired!\nproductAllRatings: " + productAllRatings);

            if (productAllRatings != null) {
                DialogUtil.dismissLoadingDialog();

                if (productAllRatings.getError() == 200) {
                    displayAllProductRatingsBottomSheetDialog(productAllRatings.getData(), productAllRatings.getAverage());
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while loading all ratings of the product!");
                    Toast.makeText(ProductDetailsActivity.this, productAllRatings.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "onChanged: 1st Time Observer fired!");
            }
        }
    };

    private final Observer<ProductOwnRating> productOwnRatingObserver = new Observer<ProductOwnRating>() {
        @Override
        public void onChanged(ProductOwnRating productOwnRating) {
            Log.i(TAG, "onChanged: VIEW OWN RATING Observer fired!\nproductOwnRating: " + productOwnRating);

            if (productOwnRating != null) {
                DialogUtil.dismissLoadingDialog();

                if (productOwnRating.getError() == 200) {
                    Log.i(TAG, "onChanged: Product Rating Successfully found!");
                    //DISPLAYING RATING BOTTOM SHEET DIALOG WITH PREVIOUS RATING.
                } else {
                    Log.i(TAG, "onChanged: Something went wrong during getting own product rating!\nproductOwnRating.getMsg(): " + productOwnRating.getMsg());
                    if (productOwnRating.getError() != 400) {   //PROBLEM OCCURRED DURING PRODUCT OWN RATING
                        Toast.makeText(ProductDetailsActivity.this, productOwnRating.getMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                displayRatingBottomSheetDialog(productDetailsViewModel.getSingleProductMutableLiveData()
                        .getValue().getProducts(), productOwnRating.getData());
            } else {
                Log.i(TAG, "onChanged: 1st time Observer fired!");
            }
        }
    };

    private final Observer<OrderAcceptResponse> prodAddAcceptResponseObserver = new Observer<OrderAcceptResponse>() {
        @Override
        public void onChanged(OrderAcceptResponse orderAcceptResponse) {
            Log.i(TAG, "onChanged: PROD ADD ACCEPTANCE Observer fired!\norderAcceptResponse: " + orderAcceptResponse);

            if (orderAcceptResponse != null) {
//                DialogUtil.dismissProcessingInfoDialog();

                if (orderAcceptResponse.getError() == 200) {
                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getStatus() != 0) {
                        Log.i(TAG, "onChanged: Vendor Not Accepting Orders! Can't add to cart.");
                        DialogUtil.dismissProcessingInfoDialog();

                        DialogUtil.showCustomSnackbar(ProductDetailsActivity.this, productDetailsSwipeRefreshLayout, orderAcceptResponse.getMsg(), -1);

//                        productDetailsViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(hotSellingViewModel.toBeAddedProductId, hotSellingViewModel.toBeAddedProductQty, false);
                    }
                } else {
                    Log.i(TAG, "onChanged: Something went wrong while Checking for Vendor Prod Add Acceptance!");
                    DialogUtil.dismissProcessingInfoDialog();

                    Toast.makeText(ProductDetailsActivity.this, orderAcceptResponse.getMsg(), Toast.LENGTH_SHORT).show();

//                    productDetailsViewModel.hotSellingRecyclerViewAdapter.notifyProductAdded(hotSellingViewModel.toBeAddedProductId, hotSellingViewModel.toBeAddedProductQty, false);
                }
            } else {
                Log.i(TAG, "onChanged: Null observer fired!");
            }
        }
    };


    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "onRefresh: fired!");

            //REFRESH THE PRODUCT DETAILS
            int productId = getIntent().getIntExtra("PRODUCT_ID", 0);
            Log.i(TAG, "onRefresh: productId: " + productId);
            if (!productDetailsViewModel.getSingleProductDetails(productId)) {
                DialogUtil.showNoInternetToast(ProductDetailsActivity.this);
            } else {
                DialogUtil.showLoadingDialog(ProductDetailsActivity.this);
            }

            productDetailsSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: clicked!");

            if (view.getId() == decMaterialButton.getId()) {    //DECREMENT COUNT
                Log.i(TAG, "onClick: Decrement Clicked!");

                int qty = Integer.parseInt(qtyMaterialTextView.getText().toString());
                if (qty > 1) {
                    qtyMaterialTextView.setText(String.valueOf(qty - 1));
                }
            } else if (view.getId() == incMaterialButton.getId()) {     //INCREMENT COUNT
                Log.i(TAG, "onClick: Increment Clicked!");

                qtyMaterialTextView.setText(String.valueOf(Integer.parseInt(qtyMaterialTextView.getText().toString()) + 1));
            } else if (view.getId() == addToCartMaterialButton.getId()) {       //ADD TO CART
                Log.i(TAG, "onClick: Add To Cart Clicked!");

                if (productDetailsViewModel.getSingleProductMutableLiveData().getValue() != null
                        && productDetailsViewModel.getSingleProductMutableLiveData().getValue().getError() == 200) {
                    /*if (!productDetailsViewModel.addProductsToCart(getIntent().getIntExtra("PRODUCT_ID", 0),
                            Integer.parseInt(qtyMaterialTextView.getText().toString()))) {*/
                    if (!productDetailsViewModel.checkVendorOrderAcceptance(getIntent().getIntExtra("PRODUCT_ID", 0),
                            Integer.parseInt(qtyMaterialTextView.getText().toString()))) {
                        DialogUtil.showNoInternetToast(ProductDetailsActivity.this);
                    } else {
                        DialogUtil.showProcessingInfoDialog(ProductDetailsActivity.this);
                    }
                } else {
                    Log.i(TAG, "onClick: Product Details most probably not loaded yet!");
                    Toast.makeText(ProductDetailsActivity.this, "Please first load the Product Details!", Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == rateProductMaterialButton.getId()) {     //RATE PRODUCT
                Log.i(TAG, "onClick: Rate Product Clicked!");

                if (productDetailsViewModel.getSingleProductMutableLiveData().getValue() != null) {
                    if (productDetailsViewModel.getSingleProductMutableLiveData().getValue().getProducts() != null) {
                        if (productDetailsViewModel.getSingleProductMutableLiveData().getValue().getProducts().getOrdered() == 1) {
//                            displayRatingBottomSheetDialog(productDetailsViewModel.getSingleProductMutableLiveData().getValue().getProducts(), productOwnRating.getData());
                            if (!productDetailsViewModel.viewOwnProductRating(getIntent().getIntExtra("PRODUCT_ID", 0))) {
                                DialogUtil.showNoInternetToast(ProductDetailsActivity.this);
                            } else {
                                DialogUtil.showLoadingDialog(ProductDetailsActivity.this);
                            }
                        } else {
                            Log.i(TAG, "onClick: Product not yet purchased.");
                            Toast.makeText(ProductDetailsActivity.this, "Please first purchase the product to rate it!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i(TAG, "onClick: Product Details NULL!");
                        Toast.makeText(ProductDetailsActivity.this, "Please Load Product details first!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i(TAG, "onClick: Product Details MutableLiveData NULL!");
                    Toast.makeText(ProductDetailsActivity.this, "Please Load Product details first!", Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == viewAllProductRatingsMaterialTextView.getId()) {
                Log.i(TAG, "onClick: View All Ratings Clicked!");

                if (!productDetailsViewModel.viewAllProductRatings(getIntent().getIntExtra("PRODUCT_ID", 0))) {
                    DialogUtil.showNoInternetToast(ProductDetailsActivity.this);
                } else {
                    DialogUtil.showLoadingDialog(ProductDetailsActivity.this);
                }
            }
        }
    };

    private void displayProductDetails(SingleProduct_Product products) {
        Log.i(TAG, "displayProductDetails: fired!");

        //PRODUCT IMAGES
        ArrayList<String> productImagesArrayList = new ArrayList<>();
        if (products.getImage() != null && !products.getImage().matches("")) {
            productImagesArrayList.add(products.getImage());
        }
        if (products.getImage2() != null && !products.getImage2().matches("")) {
            productImagesArrayList.add(products.getImage2());
        }
        if (products.getImage3() != null && !products.getImage3().matches("")) {
            productImagesArrayList.add(products.getImage3());
        }
        if (products.getImage4() != null && !products.getImage4().matches("")) {
            productImagesArrayList.add(products.getImage4());
        }
        if (products.getImage5() != null && !products.getImage5().matches("")) {
            productImagesArrayList.add(products.getImage5());
        }

        productDetailsViewModel.productDetailsImagesSliderAdapter.setProductImagesList(productImagesArrayList);

        productNameMaterialTextView.setText(products.getName());
        if (products.getShort_desc() != null && !products.getShort_desc().matches("")) {
            productSDMaterialTextView.setVisibility(View.VISIBLE);
            productSDMaterialTextView.setText(products.getShort_desc());
        } else {
            productSDMaterialTextView.setVisibility(View.GONE);
        }
        if (products.getWeight() != null && !products.getWeight().matches("")) {
            productWeightMaterialTextView.setVisibility(View.VISIBLE);
            productWeightMaterialTextView.setText(products.getWeight());
        } else {
            productWeightMaterialTextView.setVisibility(View.GONE);
            productWeightMaterialTextView.setText("Weight - NA");
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(products.getSale_price())).append(" | ");
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, (spannableStringBuilder.toString().indexOf("|")), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        if ((products.getMrp_price() - products.getSale_price()) > 0) {
            //DISCOUNT AVAILABLE
            float disc = ((products.getMrp_price() - products.getSale_price()) * 100) / products.getMrp_price();
            Log.i(TAG, "displayProductDetails: disc: " + disc);

            String strDisc = String.format(Locale.getDefault(), "%.2f", disc);
            Log.i(TAG, "displayProductDetails: strDisc: " + strDisc);

            spannableStringBuilder.append(getString(R.string.rupee_symbol)).append(" ").append(String.valueOf(products.getMrp_price()));
            spannableStringBuilder.append("  (").append(strDisc).append("%)");
            spannableStringBuilder.setSpan(new StrikethroughSpan(), (spannableStringBuilder.toString().indexOf("|") + 2), (spannableStringBuilder.toString().indexOf("(") - 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), spannableStringBuilder.toString().indexOf("("), spannableStringBuilder.toString().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            spannableStringBuilder.delete(spannableStringBuilder.length() - 2, spannableStringBuilder.length());
        }

        productPriceMaterialTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        if (products.getLong_desc() != null && !products.getLong_desc().matches("")) {
            productLDMaterialTextView.setVisibility(View.VISIBLE);
            productLDMaterialTextView.setText(products.getLong_desc());
        } else {
            productLDMaterialTextView.setVisibility(View.GONE);
        }

        if (products.getRating_average() != null) {
            productRatingAverageMaterialTextView.setText(getString(R.string.average) + " " + products.getRating_average());
        } else {
            productRatingAverageMaterialTextView.setText(getString(R.string.average) + " " + 0);
        }
    }

    private void displayRatingBottomSheetDialog(SingleProduct_Product singleProduct_product, ProductOwnRating_Data data) {
        Log.i(TAG, "displayRatingBottomSheetDialog: fired! previousRating, data: " + data);

        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_rate_store);

        MaterialTextView rateStoreTitleMaterialTextView = bottomSheetDialog.findViewById(R.id.rateStoreTitleMaterialTextView);
        MaterialTextView rateStoreNameMaterialTextView = bottomSheetDialog.findViewById(R.id.rateStoreNameMaterialTextView);

        AppCompatRatingBar rateStoreAppCompatRatingBar = bottomSheetDialog.findViewById(R.id.rateStoreAppCompatRatingBar);

        TextInputEditText reviewStoreTextInputEditText = bottomSheetDialog.findViewById(R.id.reviewStoreTextInputEditText);

        MaterialButton rateMaterialButton = bottomSheetDialog.findViewById(R.id.rateMaterialButton);

        rateStoreTitleMaterialTextView.setText(getString(R.string.rate_product));
        rateStoreNameMaterialTextView.setText(singleProduct_product.getName());
        reviewStoreTextInputEditText.setHint(getString(R.string.product_review));

        if (data != null) {
            rateStoreAppCompatRatingBar.setRating(data.getStar());
            reviewStoreTextInputEditText.setText(data.getText());
        }

        rateMaterialButton.setOnClickListener(view -> {
            Log.i(TAG, "onClick: rate product clicked!");

            Log.i(TAG, "displayRatingBottomSheetDialog: rateStoreAppCompatRatingBar.getRating(): " + rateStoreAppCompatRatingBar.getRating());

            if (rateStoreAppCompatRatingBar.getRating() == 0 || reviewStoreTextInputEditText.getText().toString().matches("")) {
                Toast.makeText(this, "Both Rating & Review is required to publish!", Toast.LENGTH_SHORT).show();
            } else {
                ProductReviewModel productReviewModel = new ProductReviewModel(singleProduct_product.getId(),
                        ApplicationData.getLoggedInBuyerId(), rateStoreAppCompatRatingBar.getRating(),
                        reviewStoreTextInputEditText.getText().toString(), 1);
                Log.i(TAG, "displayRatingBottomSheetDialog: productReviewModel: " + productReviewModel);

                if (!productDetailsViewModel.rateProduct(productReviewModel)) {
                    DialogUtil.showNoInternetToast(ProductDetailsActivity.this);
                } else {
                    DialogUtil.showProcessingInfoDialog(ProductDetailsActivity.this);
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void displayAllProductRatingsBottomSheetDialog(List<ViewShopRating_Data> data, float average) {
        Log.i(TAG, "displayAllProductRatingsBottomSheetDialog: fired! data.size(): " + data.size() + "\taverage: " + average);

        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_all_ratings);

        MaterialTextView storeRatingsTitleMaterialTextView = bottomSheetDialog.findViewById(R.id.storeRatingsTitleMaterialTextView);
        MaterialTextView storeRatingAverageMaterialTextView = bottomSheetDialog.findViewById(R.id.storeRatingAverageMaterialTextView);
        RecyclerView allRatingsRecyclerView = bottomSheetDialog.findViewById(R.id.allRatingsRecyclerView);

        storeRatingsTitleMaterialTextView.setText(getString(R.string.product_ratings));
        storeRatingAverageMaterialTextView.setText(getString(R.string.average) + " " + average);

        AllRatingsRecyclerViewAdapter allRatingsRecyclerViewAdapter = new AllRatingsRecyclerViewAdapter();

        allRatingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allRatingsRecyclerView.setAdapter(allRatingsRecyclerViewAdapter);

        allRatingsRecyclerViewAdapter.setViewShopRating_dataList(data);

        bottomSheetDialog.show();
    }
}