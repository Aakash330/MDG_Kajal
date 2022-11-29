package com.techive.mydailygoodscustomer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Adapters.ProductDetailsImagesSliderAdapter;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductOwnRating;
import com.techive.mydailygoodscustomer.Models.ProductReviewModel;
import com.techive.mydailygoodscustomer.Models.SingleProduct;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;
import com.techive.mydailygoodscustomer.Repositories.ProductDetailsRepository;
import com.techive.mydailygoodscustomer.Util.ApplicationData;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ProductDetailsViewModel extends AndroidViewModel {
    private static final String TAG = "ProductDetailsViewModel";

    private ProductDetailsRepository productDetailsRepository;

    private MutableLiveData<SingleProduct> singleProductMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> rateProductMutableLiveData;
    private MutableLiveData<ViewShopRating> viewAllProductRatingsMutableLiveData;
    private MutableLiveData<ProductOwnRating> viewOwnProductRatingMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;

    public ProductDetailsImagesSliderAdapter productDetailsImagesSliderAdapter;

    private int toBeAddedQty = 0;

    public ProductDetailsViewModel(@NonNull @NotNull Application application) {
        super(application);
        Log.i(TAG, "ProductDetailsViewModel: fired!");

        initProductDetailsViewModel();
    }

    private void initProductDetailsViewModel() {
        Log.i(TAG, "initProductDetailsViewModel: fired!");

        if (productDetailsRepository != null) {
            Log.i(TAG, "initProductDetailsViewModel: ProductDetailsRepository already initialized in ProductDetailsViewModel.");
            return;
        }

        productDetailsRepository = ProductDetailsRepository.getProductDetailsRepositoryInstance(getApplication().getApplicationContext());

        singleProductMutableLiveData = productDetailsRepository.getSingleProductMutableLiveData();
        addToCartMutableLiveData = productDetailsRepository.getAddToCartMutableLiveData();
        rateProductMutableLiveData = productDetailsRepository.getRateProductMutableLiveData();
        viewAllProductRatingsMutableLiveData = productDetailsRepository.getViewAllProductRatingsMutableLiveData();
        viewOwnProductRatingMutableLiveData = productDetailsRepository.getViewOwnProductRatingMutableLiveData();
        prodAddAcceptResponseMutableLiveData = productDetailsRepository.getProdAddAcceptResponseMutableLiveData();

        productDetailsImagesSliderAdapter = new ProductDetailsImagesSliderAdapter(getApplication().getApplicationContext());
    }

    /* GETTERS & SETTERS - START */
    public MutableLiveData<SingleProduct> getSingleProductMutableLiveData() {
        return singleProductMutableLiveData;
    }

    public void setSingleProductMutableLiveData(SingleProduct singleProduct) {
        productDetailsRepository.setSingleProductMutableLiveData(singleProduct);
    }

    public MutableLiveData<GeneralResponse> getAddToCartMutableLiveData() {
        return addToCartMutableLiveData;
    }

    public void setAddToCartMutableLiveData(GeneralResponse addToCart) {
        productDetailsRepository.setAddToCartMutableLiveData(addToCart);
    }

    public MutableLiveData<GeneralResponse> getRateProductMutableLiveData() {
        return rateProductMutableLiveData;
    }

    public void setRateProductMutableLiveData(GeneralResponse rateProduct) {
        productDetailsRepository.setRateProductMutableLiveData(rateProduct);
    }

    public MutableLiveData<ViewShopRating> getViewAllProductRatingsMutableLiveData() {
        return viewAllProductRatingsMutableLiveData;
    }

    public void setViewAllProductRatingsMutableLiveData(ViewShopRating viewAllProductRatings) {
        productDetailsRepository.setViewAllProductRatingsMutableLiveData(viewAllProductRatings);
    }

    public MutableLiveData<ProductOwnRating> getViewOwnProductRatingMutableLiveData() {
        return viewOwnProductRatingMutableLiveData;
    }

    public void setViewOwnProductRatingMutableLiveData(ProductOwnRating viewOwnProductRating) {
        productDetailsRepository.setViewOwnProductRatingMutableLiveData(viewOwnProductRating);
    }

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse prodAddAcceptResponse) {
        productDetailsRepository.setProdAddAcceptResponseMutableLiveData(prodAddAcceptResponse);
    }
    /* GETTERS & SETTERS - END */

    //TO UPDATE THE SELECTED PRODUCTS IN CART
    public void updateCartData(int prodId) {
        Log.i(TAG, "updateCartData: fired!");

        Log.i(TAG, "updateCartData: PRODUCTDETAILS, BEFORE, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
        HashMap<Integer, Integer> prodIdQtyHashMap = ApplicationData.getProdIdOrderQtyHashMap();
        if (prodIdQtyHashMap.containsKey(prodId)) {
            int existingQty = prodIdQtyHashMap.get(prodId);
            existingQty = existingQty + toBeAddedQty;
            prodIdQtyHashMap.put(prodId, existingQty);
        } else {
            prodIdQtyHashMap.put(prodId, toBeAddedQty);
        }
        ApplicationData.setProdIdOrderQtyHashMap(prodIdQtyHashMap);
        Log.i(TAG, "updateCartData: PRODUCTDETAILS, AFTER, ApplicationData.getProdIdOrderQtyHashMap().toString(): " + ApplicationData.getProdIdOrderQtyHashMap().toString());
    }

    //TO UPDATE THE CART COUNT
    public void updateCartCount() {
        Log.i(TAG, "updateCartCount: fired!");

        Log.i(TAG, "onChanged: BEFORE, ApplicationData.getCartTotalQty(): " + ApplicationData.getCartTotalQty());
        ApplicationData.setCartTotalQty(ApplicationData.getCartTotalQty() + toBeAddedQty);
        Log.i(TAG, "onChanged: AFTER, ApplicationData.getCartTotalQty(): " + ApplicationData.getCartTotalQty());
    }

    //UPDATE PRODUCT REVIEW IN THE LOCAL PRODUCT DATA
    public void updateProductReview() {
        Log.i(TAG, "updateProductReview: fired!");

//        singleProductMutableLiveData.getValue();
    }

    public boolean getSingleProductDetails(int productId) {
        Log.i(TAG, "getSingleProductDetails: fired!");

        return productDetailsRepository.getSingleProductDetails(productId);
    }

//    public boolean addProductsToCart(int productId, int qty) {
//        Log.i(TAG, "addProductsToCart: fired!");
//
//        Log.i(TAG, "addProductsToCart: BEFORE, toBeAddedQty: " + toBeAddedQty);
//        toBeAddedQty = toBeAddedQty + qty;
//        Log.i(TAG, "addProductsToCart: AFTER, toBeAddedQty: " + toBeAddedQty);
//        return productDetailsRepository.addToCart(productId, qty);
//    }

    public boolean rateProduct(ProductReviewModel productReviewModel) {
        Log.i(TAG, "rateProduct: fired!");

        return productDetailsRepository.rateProduct(productReviewModel);
    }

    public boolean viewAllProductRatings(int productId) {
        Log.i(TAG, "viewAllProductRatings: fired!");

        return productDetailsRepository.viewAllProductRatings(productId);
    }

    public boolean viewOwnProductRating(int productId) {
        Log.i(TAG, "viewOwnProductRating: fired!");

        return productDetailsRepository.viewOwnProductRating(productId);
    }

    public boolean checkVendorOrderAcceptance(int productId, int qty) {
        Log.i(TAG, "checkVendorOrderAcceptance: fired!");

        Log.i(TAG, "checkVendorOrderAcceptance: BEFORE, toBeAddedQty: " + toBeAddedQty);
        toBeAddedQty = toBeAddedQty + qty;
        Log.i(TAG, "checkVendorOrderAcceptance: AFTER, toBeAddedQty: " + toBeAddedQty);

        return productDetailsRepository.checkVendorOrderAcceptance(productId, qty);
    }
}
