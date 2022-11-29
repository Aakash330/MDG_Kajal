package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.ProductOwnRating;
import com.techive.mydailygoodscustomer.Models.ProductReviewModel;
import com.techive.mydailygoodscustomer.Models.SingleProduct;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsRepository {
    private static final String TAG = "ProductDetailsRepositor";

    private static ProductDetailsRepository productDetailsRepository;

    private static Context applicationContext;

    private MutableLiveData<SingleProduct> singleProductMutableLiveData;
    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<GeneralResponse> rateProductMutableLiveData;
    private MutableLiveData<ViewShopRating> viewAllProductRatingsMutableLiveData;
    private MutableLiveData<ProductOwnRating> viewOwnProductRatingMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;

    private int toBeAddedProductId, toBeAddedProductQty;

    public ProductDetailsRepository() {
        Log.i(TAG, "ProductDetailsRepository: Empty Constructor fired!");

        initProductDetailsRepository();
    }

    public static ProductDetailsRepository getProductDetailsRepositoryInstance(Context context) {
        Log.i(TAG, "getProductDetailsRepositoryInstance: fired!");

        if (productDetailsRepository == null) {
            applicationContext = context;
            productDetailsRepository = new ProductDetailsRepository();
            Log.i(TAG, "getProductDetailsRepositoryInstance: ProductDetailsRepository initialized now!");
        }
        return productDetailsRepository;
    }

    private void initProductDetailsRepository() {
        Log.i(TAG, "initProductDetailsRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        singleProductMutableLiveData = new MutableLiveData<>();
        addToCartMutableLiveData = new MutableLiveData<>();
        rateProductMutableLiveData = new MutableLiveData<>();
        viewAllProductRatingsMutableLiveData = new MutableLiveData<>();
        viewOwnProductRatingMutableLiveData = new MutableLiveData<>();
        prodAddAcceptResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SingleProduct> getSingleProductMutableLiveData() {
        return singleProductMutableLiveData;
    }

    public void setSingleProductMutableLiveData(SingleProduct singleProduct) {
        singleProductMutableLiveData.setValue(singleProduct);
    }

    public MutableLiveData<GeneralResponse> getAddToCartMutableLiveData() {
        return addToCartMutableLiveData;
    }

    public void setAddToCartMutableLiveData(GeneralResponse addToCart) {
        addToCartMutableLiveData.setValue(addToCart);
    }

    public MutableLiveData<GeneralResponse> getRateProductMutableLiveData() {
        return rateProductMutableLiveData;
    }

    public void setRateProductMutableLiveData(GeneralResponse rateProduct) {
        rateProductMutableLiveData.setValue(rateProduct);
    }

    public MutableLiveData<ViewShopRating> getViewAllProductRatingsMutableLiveData() {
        return viewAllProductRatingsMutableLiveData;
    }

    public void setViewAllProductRatingsMutableLiveData(ViewShopRating viewAllProductRatings) {
        viewAllProductRatingsMutableLiveData.setValue(viewAllProductRatings);
    }

    public MutableLiveData<ProductOwnRating> getViewOwnProductRatingMutableLiveData() {
        return viewOwnProductRatingMutableLiveData;
    }

    public void setViewOwnProductRatingMutableLiveData(ProductOwnRating viewOwnProductRating) {
        viewOwnProductRatingMutableLiveData.setValue(viewOwnProductRating);
    }

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse prodAddAcceptResponse) {
        prodAddAcceptResponseMutableLiveData.setValue(prodAddAcceptResponse);
    }

    /* GET SINGLE PRODUCT DETAILS */
    public boolean getSingleProductDetails(int productId) {
        Log.i(TAG, "getSingleProductDetails: fired! productId: " + productId);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getSingleProductDetails: Network Available!");

            Integer buyerId;
            if (ApplicationData.getLoggedInBuyerId() == 0) {
                buyerId = null;
            } else {
                buyerId = ApplicationData.getLoggedInBuyerId();
            }
            Log.i(TAG, "getSingleProductDetails: buyerId: " + buyerId);

            Call<SingleProduct> singleProductCall = ApplicationData.mdg_customerAPI_interface.getSingleProductDetail(
                    ApplicationData.getDefaultStoreId(), productId, buyerId);
//            Call<SingleProduct> singleProductCall = ApplicationData.mdg_customerAPI_interface.getSingleProductDetail(
//                    121, productId, buyerId);
            singleProductCall.enqueue(new Callback<SingleProduct>() {
                @Override
                public void onResponse(Call<SingleProduct> call, Response<SingleProduct> response) {
                    Log.i(TAG, "onResponse: SINGLE PRODUCT Response seems to be successful!");

                    if (!response.isSuccessful()) {
                        SingleProduct singleProductUnsuccessful = new SingleProduct(applicationContext.getString(R.string.server_didnt_respond) + "-" + response.code(), response.code(), null);
                        singleProductMutableLiveData.setValue(singleProductUnsuccessful);
                        return;
                    }
                    SingleProduct singleProduct = response.body();
                    Log.i(TAG, "onResponse: singleProduct: " + singleProduct);
                    singleProductMutableLiveData.setValue(singleProduct);
                }

                @Override
                public void onFailure(Call<SingleProduct> call, Throwable t) {
                    Log.i(TAG, "onFailure: SINGLE PRODUCT Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        SingleProduct singleProductFailedSocketTimeout = new SingleProduct(applicationContext.getString(R.string.weak_internet_connection), 1001, null);
                        singleProductMutableLiveData.setValue(singleProductFailedSocketTimeout);
                        return;
                    }
                    SingleProduct singleProductFailed = new SingleProduct("Somehow Product Details Request failed!", 1000, null);
                    singleProductMutableLiveData.setValue(singleProductFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* ADD PRODUCTS TO CART */
//    public boolean addToCart(int productId, int qty) {
//        Log.i(TAG, "addToCart: fired!");
//
//        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
//            Log.i(TAG, "addToCart: Network Available!");
//
//            Call<GeneralResponse> addToCartCall = ApplicationData.mdg_customerAPI_interface.addToCart(
//                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId(), productId, qty);
//            addToCartCall.enqueue(new Callback<GeneralResponse>() {
//                @Override
//                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                    Log.i(TAG, "onResponse: ADD TO CART Response seems to be a success!");
//
//                    if (!response.isSuccessful()) {
//                        String prodId = "";
//                        try {
//                            Log.i(TAG, "onResponse: call.request().toString(): " + call.request().toString() + "\tcall.request().toString().length(): " + call.request().toString().length());
//                            //    onResponse: call.request().toString(): Request{method=POST, url=https://www.mydailygoods.com/api/buyer-cart-data?buy_id=126&v_id=121, tags={class retrofit2.Invocation=com.techive.mydailygoodscustomer.Util.MDG_CustomerAPI_Interface.getCartDetails() [126, 121]}}
//
//                            prodId = call.request().toString().substring((call.request().toString().indexOf("prod_id") + 8),
//                                    (call.request().toString().indexOf("qty") - 1));
//                            Log.i(TAG, "onResponse: prodId: " + prodId);
//                        } catch (Exception e) {
//                            Log.i(TAG, "onResponse: Exception in call.request().toString()!");
//                            e.printStackTrace();
//                        }
//
//                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse(applicationContext.getString(R.string.server_didnt_respond) + " - " + response.code(), prodId, response.code());
//                        addToCartMutableLiveData.setValue(generalResponseUnsuccessful);
//                        return;
//                    }
//                    GeneralResponse generalResponse = response.body();
//                    Log.i(TAG, "onResponse: ADD TO CART, generalResponse: " + generalResponse);
//                    addToCartMutableLiveData.setValue(generalResponse);
//                }
//
//                @Override
//                public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                    Log.i(TAG, "onFailure: ADD TO CART Request seems to have failed!\tt.getMessage(): " + t.getMessage());
//                    t.printStackTrace();
//
//                    String prodId = "";
//                    try {
//                        Log.i(TAG, "onResponse: call.request().toString(): " + call.request().toString() + "\tcall.request().toString().length(): " + call.request().toString().length());
//                        //    onResponse: call.request().toString(): Request{method=POST, url=https://www.mydailygoods.com/api/buyer-cart-data?buy_id=126&v_id=121, tags={class retrofit2.Invocation=com.techive.mydailygoodscustomer.Util.MDG_CustomerAPI_Interface.getCartDetails() [126, 121]}}
//
////                        int prodIdIndex = call.request().toString().indexOf("prod_id");
////                        int qtyIndex = call.request().toString().indexOf("qty");
//                        prodId = call.request().toString().substring((call.request().toString().indexOf("prod_id") + 8),
//                                (call.request().toString().indexOf("qty") - 1));
////                        Log.i(TAG, "onResponse: prodId: " + prodId + "\tprodIdIndex: " + prodIdIndex + "\tqtyIndex: " + qtyIndex);
//                        Log.i(TAG, "onResponse: prodId: " + prodId);
//                    } catch (Exception e) {
//                        Log.i(TAG, "onResponse: Exception in call.request().toString()!");
//                        e.printStackTrace();
//                    }
//
//                    if (t instanceof SocketTimeoutException) {
//                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), prodId, 1001);
//                        addToCartMutableLiveData.setValue(generalResponseFailedSocketTimeout);
//                        return;
//                    }
//                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Add to Cart request failed!", prodId, 1000);
//                    addToCartMutableLiveData.setValue(generalResponseFailed);
//                }
//            });
//            return true;
//        }
//        return false;
//    }

    /* ADD PRODUCTS TO CART - MODIFIED */
    public void addToCart() {
        Log.i(TAG, "addToCart: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "addToCart: Network Available!");

            Call<GeneralResponse> addToCartCall = ApplicationData.mdg_customerAPI_interface.addToCart(
                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId(), toBeAddedProductId, toBeAddedProductQty);
            addToCartCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: ADD TO CART Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        String prodId = "";
                        try {
                            Log.i(TAG, "onResponse: call.request().toString(): " + call.request().toString() + "\tcall.request().toString().length(): " + call.request().toString().length());
                            //    onResponse: call.request().toString(): Request{method=POST, url=https://www.mydailygoods.com/api/buyer-cart-data?buy_id=126&v_id=121, tags={class retrofit2.Invocation=com.techive.mydailygoodscustomer.Util.MDG_CustomerAPI_Interface.getCartDetails() [126, 121]}}

                            prodId = call.request().toString().substring((call.request().toString().indexOf("prod_id") + 8),
                                    (call.request().toString().indexOf("qty") - 1));
                            Log.i(TAG, "onResponse: prodId: " + prodId);
                        } catch (Exception e) {
                            Log.i(TAG, "onResponse: Exception in call.request().toString()!");
                            e.printStackTrace();
                        }

                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse(applicationContext.getString(R.string.server_didnt_respond) + " - " + response.code(), prodId, response.code());
                        addToCartMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: ADD TO CART, generalResponse: " + generalResponse);
                    addToCartMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: ADD TO CART Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    String prodId = "";
                    try {
                        Log.i(TAG, "onResponse: call.request().toString(): " + call.request().toString() + "\tcall.request().toString().length(): " + call.request().toString().length());
                        //    onResponse: call.request().toString(): Request{method=POST, url=https://www.mydailygoods.com/api/buyer-cart-data?buy_id=126&v_id=121, tags={class retrofit2.Invocation=com.techive.mydailygoodscustomer.Util.MDG_CustomerAPI_Interface.getCartDetails() [126, 121]}}

//                        int prodIdIndex = call.request().toString().indexOf("prod_id");
//                        int qtyIndex = call.request().toString().indexOf("qty");
                        prodId = call.request().toString().substring((call.request().toString().indexOf("prod_id") + 8),
                                (call.request().toString().indexOf("qty") - 1));
//                        Log.i(TAG, "onResponse: prodId: " + prodId + "\tprodIdIndex: " + prodIdIndex + "\tqtyIndex: " + qtyIndex);
                        Log.i(TAG, "onResponse: prodId: " + prodId);
                    } catch (Exception e) {
                        Log.i(TAG, "onResponse: Exception in call.request().toString()!");
                        e.printStackTrace();
                    }

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), prodId, 1001);
                        addToCartMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Add to Cart request failed!", prodId, 1000);
                    addToCartMutableLiveData.setValue(generalResponseFailed);
                }
            });
        }
    }

    /* RATE PRODUCT */
    public boolean rateProduct(ProductReviewModel productReviewModel) {
        Log.i(TAG, "rateProduct: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "rateProduct: Network Available!");

            Call<GeneralResponse> rateProductCall = ApplicationData.mdg_customerAPI_interface.rateProduct(productReviewModel);
            rateProductCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: RATE PRODUCT Response seems to be successful!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse(applicationContext.getString(R.string.server_didnt_respond) + "-" + response.code(), null, response.code());
                        rateProductMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: generalResponse: " + generalResponse);
                    rateProductMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: RATE PRODUCT Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        rateProductMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Rate Product Request failed!", null, 1000);
                    rateProductMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* VIEW ALL PRODUCT RATINGS */
    public boolean viewAllProductRatings(int productId) {
        Log.i(TAG, "viewAllProductRatings: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "viewAllProductRatings: Network Available!");

            Call<ViewShopRating> viewAllProductRatingsCall = ApplicationData.mdg_customerAPI_interface.viewAllRatingsOfProduct(productId);
            viewAllProductRatingsCall.enqueue(new Callback<ViewShopRating>() {
                @Override
                public void onResponse(Call<ViewShopRating> call, Response<ViewShopRating> response) {
                    Log.i(TAG, "onResponse: VIEW ALL PRODUCT RATINGS Response seems to be successful!");

                    if (!response.isSuccessful()) {
                        ViewShopRating productAllRatingsUnsuccessful = new ViewShopRating(applicationContext.getString(R.string.server_didnt_respond) + "-" + response.code(), null, 0f, response.code());
                        viewAllProductRatingsMutableLiveData.setValue(productAllRatingsUnsuccessful);
                        return;
                    }
                    ViewShopRating productAllRatings = response.body();
                    Log.i(TAG, "onResponse: productAllRatings: " + productAllRatings);
                    viewAllProductRatingsMutableLiveData.setValue(productAllRatings);
                }

                @Override
                public void onFailure(Call<ViewShopRating> call, Throwable t) {
                    Log.i(TAG, "onFailure: VIEW ALL PRODUCT RATINGS Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ViewShopRating productAllRatingsFailedSocketTimeout = new ViewShopRating(applicationContext.getString(R.string.weak_internet_connection), null, 0f, 1001);
                        viewAllProductRatingsMutableLiveData.setValue(productAllRatingsFailedSocketTimeout);
                        return;
                    }
                    ViewShopRating productAllRatingsFailed = new ViewShopRating("Somehow View All Product Ratings Request failed!", null, 0f, 1000);
                    viewAllProductRatingsMutableLiveData.setValue(productAllRatingsFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* VIEW OWN PRODUCT RATING */
    public boolean viewOwnProductRating(int productId) {
        Log.i(TAG, "viewOwnProductRating: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "viewOwnProductRating: Network Available!");

            Call<ProductOwnRating> viewOwnProductRatingsCall = ApplicationData.mdg_customerAPI_interface.viewOwnRatingOfProduct(productId, ApplicationData.getLoggedInBuyerId());
            viewOwnProductRatingsCall.enqueue(new Callback<ProductOwnRating>() {
                @Override
                public void onResponse(Call<ProductOwnRating> call, Response<ProductOwnRating> response) {
                    Log.i(TAG, "onResponse: VIEW OWN PRODUCT RATING Response seems to be successful!");

                    if (!response.isSuccessful()) {
                        ProductOwnRating productOwnRatingsUnsuccessful = new ProductOwnRating(applicationContext.getString(R.string.server_didnt_respond) + "-" + response.code(), null, response.code());
                        viewOwnProductRatingMutableLiveData.setValue(productOwnRatingsUnsuccessful);
                        return;
                    }
                    ProductOwnRating productOwnRating = response.body();
                    Log.i(TAG, "onResponse: productOwnRating: " + productOwnRating);
                    viewOwnProductRatingMutableLiveData.setValue(productOwnRating);
                }

                @Override
                public void onFailure(Call<ProductOwnRating> call, Throwable t) {
                    Log.i(TAG, "onFailure: VIEW OWN PRODUCT RATING Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        ProductOwnRating productOwnRatingFailedSocketTimeout = new ProductOwnRating(applicationContext.getString(R.string.weak_internet_connection), null, 1001);
                        viewOwnProductRatingMutableLiveData.setValue(productOwnRatingFailedSocketTimeout);
                        return;
                    }
                    ProductOwnRating productOwnRatingFailed = new ProductOwnRating("Somehow View Own Product Ratings Request failed!", null, 1000);
                    viewOwnProductRatingMutableLiveData.setValue(productOwnRatingFailed);
                }
            });
            return true;
        }
        return false;
    }

    //FOR CHECKING VENDOR STATUS TO ADD PRODUCT
    public boolean checkVendorOrderAcceptance(int productId, int qty) {
        Log.i(TAG, "checkVendorOrderAcceptance2: fired! productId: " + productId + "\tqty: " + qty);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "checkVendorOrderAcceptance2: Network Available!");

            toBeAddedProductId = productId;
            toBeAddedProductQty = qty;

            Call<OrderAcceptResponse> orderAcceptResponseCall = ApplicationData.mdg_customerAPI_interface.
                    checkVendorOrderAcceptance(ApplicationData.getDefaultStoreId(), ApplicationData.getLoggedInBuyerId());
            orderAcceptResponseCall.enqueue(new Callback<OrderAcceptResponse>() {
                @Override
                public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                    Log.i(TAG, "onResponse: PROD ADD ACCEPT Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        OrderAcceptResponse orderAcceptResponseUnsuccessful = new OrderAcceptResponse(applicationContext.getString(R.string.server_didnt_respond), -1, response.code());
                        prodAddAcceptResponseMutableLiveData.setValue(orderAcceptResponseUnsuccessful);
                        return;
                    }
                    OrderAcceptResponse orderAcceptResponse = response.body();
                    Log.i(TAG, "onResponse: orderAcceptResponse: " + orderAcceptResponse);

                    /* 0 = Success (Can Place Order in Store);  1 = Store Inactive (Can't accept anymore Orders)*/
                    if (orderAcceptResponse.getError() == 200 && orderAcceptResponse.getStatus() == 0) {
                        Log.i(TAG, "onResponse: Proceeding to add product from cart repository.");
                        addToCart();
                    } else {
                        Log.i(TAG, "onResponse: Not adding product from cart repository.");
                    }

                    prodAddAcceptResponseMutableLiveData.setValue(orderAcceptResponse);
                }

                @Override
                public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: PROD ADD ACCEPT Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        OrderAcceptResponse orderAcceptResponseFailedSocketTimeout = new OrderAcceptResponse(applicationContext.getString(R.string.weak_internet_connection), -1, 1001);
                        prodAddAcceptResponseMutableLiveData.setValue(orderAcceptResponseFailedSocketTimeout);
                        return;
                    }
                    OrderAcceptResponse orderAcceptResponseFailed = new OrderAcceptResponse("Somehow Order Accept Response failed!\nPlease try again later!", -1, 1000);
                    prodAddAcceptResponseMutableLiveData.setValue(orderAcceptResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

}
