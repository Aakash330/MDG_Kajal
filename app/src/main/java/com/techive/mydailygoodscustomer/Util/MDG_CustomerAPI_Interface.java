package com.techive.mydailygoodscustomer.Util;

import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.LoginResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OTPResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.SplitOrderResponse;
import com.techive.mydailygoodscustomer.Models.BuyerAddAddressModel;
import com.techive.mydailygoodscustomer.Models.BuyerAllDeliveryAddress;
import com.techive.mydailygoodscustomer.Models.Cart;
import com.techive.mydailygoodscustomer.Models.CashFreeOrder;
import com.techive.mydailygoodscustomer.Models.CityList;
import com.techive.mydailygoodscustomer.Models.HomeCart;
import com.techive.mydailygoodscustomer.Models.HomeModel;
import com.techive.mydailygoodscustomer.Models.LoginModel;
import com.techive.mydailygoodscustomer.Models.LogoutModel;
import com.techive.mydailygoodscustomer.Models.NumberValidate;
import com.techive.mydailygoodscustomer.Models.OrderHistory;
import com.techive.mydailygoodscustomer.Models.PasswordModel;
import com.techive.mydailygoodscustomer.Models.PlaceOrderModel;
import com.techive.mydailygoodscustomer.Models.ProductOwnRating;
import com.techive.mydailygoodscustomer.Models.ProductReviewModel;
import com.techive.mydailygoodscustomer.Models.ProductsModel;
import com.techive.mydailygoodscustomer.Models.RateStoreModel;
import com.techive.mydailygoodscustomer.Models.RegisterModel;
import com.techive.mydailygoodscustomer.Models.ResetPasswordModel;
import com.techive.mydailygoodscustomer.Models.SearchProducts;
import com.techive.mydailygoodscustomer.Models.SingleProduct;
import com.techive.mydailygoodscustomer.Models.StateList;
import com.techive.mydailygoodscustomer.Models.StoreListByCityName;
import com.techive.mydailygoodscustomer.Models.StoreListByName;
import com.techive.mydailygoodscustomer.Models.ViewShopRating;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MDG_CustomerAPI_Interface {

    @POST("buyer-store-list")
    Call<StoreListByName> getStoreListByName(@Query("name") String storeName);

    @POST("buyer-state-list")
    Call<StateList> getStateListByStateNameSegment(@Query("state") String stateName);

    @POST("buyer-city-list")
    Call<CityList> getCityListByCityNameSegment(@Query("city") String cityName,
                                                @Query("state_id") Integer stateId);

    @POST("buyer-login")
    Call<LoginResponse> login(@Body LoginModel loginModel);

    @POST("buyer-register")
    Call<LoginResponse> register(@Body RegisterModel registerModel);

    @POST("buyer-lat-long")
    Call<StoreListByCityName> getStoreListByLatLng(@Query("lat") String lat, @Query("lng") String lng);

    @POST("buyer-store-list-by-city")
    Call<StoreListByCityName> getStoreListByCityName(@Query("city_id") int cityId);

    @POST("buyer-fav-shop-list")
    Call<StoreListByCityName> getFavStoresList(@Query("byerId") int buyerId);

    @POST("buyer-make-fav-shop")
    Call<GeneralResponse> setFavStore(@Query("byerId") int buyerId,
                                      @Query("vender_id") int vendorId);

    @POST("buyer-banner-with-category")
    Call<HomeModel> getHomeBannerCategoryData(@Query("vendor_id") int vendorId);

    @POST("best-deal-products")
    Call<ProductsModel> getBestDealProducts(@Query("v_id") int vendorId,
                                            @Query("page") int page);

    @POST("recent-products")
    Call<ProductsModel> getRecentlyAddedProducts(@Query("v_id") int vendorId,
                                                 @Query("page") int page);

    @POST("buyer-regular-list")
    Call<ProductsModel> getBuyerRegularList(@Query("b_id") int buyerId,
                                            @Query("v_id") int vendorId,
                                            @Query("page") int page);

    @POST("buyer-add-to-cart")
    Call<GeneralResponse> addToCart(@Query("buy_id") int buyerId,
                                    @Query("v_id") int vendorId,
                                    @Query("prod_id") int productId,
                                    @Query("qty") int qty);

    @POST("buyer-cart-data")
    Call<Cart> getCartDetails(@Query("buy_id") int buyerId,
                              @Query("v_id") int vendorId);

    @POST("buyer-decrease-cart-item")
    Call<GeneralResponse> removeFromCart(@Query("buy_id") int buyerId,
                                         @Query("v_id") int vendorId,
                                         @Query("prod_id") int productId,
                                         @Query("qty") int qty);

    @POST("buyer-delete-cart-item")
    Call<GeneralResponse> completelyRemoveFromCart(@Query("buy_id") int buyerId,
                                                   @Query("v_id") int vendorId,
                                                   @Query("prod_id") int productId);

    @POST("buyer-add-address")
    Call<GeneralResponse> addBuyerDeliveryAddress(@Body BuyerAddAddressModel buyerAddAddressModel);

    @POST("buyer-address")
    Call<BuyerAllDeliveryAddress> getBuyerAllDeliveryAddress(@Query("buy_id") int buyerId);

    @POST("buyer-default-address")
    Call<GeneralResponse> setDefaultBuyerAddress(@Query("buy_id") int buyerId,
                                                 @Query("addressid") int addressId);

    @POST("buyer-update-address")
    Call<GeneralResponse> updateBuyerDeliveryAddress(@Body BuyerAddAddressModel buyerAddAddressModel);

    @POST("buyer-place-order")
    Call<GeneralResponse> placeOrder(@Body PlaceOrderModel placeOrderModel);

    @POST("buyer-create-order")
    Call<CashFreeOrder> createCashFreeOrder(@Query("buy_id") int buyerId,
                                            @Query("amount") float amount/*,
                                            @Query("orderId") String orderId*/);

    //USING THIS FUNCTION TO GET CART ON ALL PAGES OTHER THAN THE CART PAGE.
    @POST("buyer-all-cart")
    Call<HomeCart> getCart(@Query("buy_id") int buyerId,
                           @Query("v_id") int vendorId);

    @POST("buyer-order-history")
    Call<OrderHistory> getOrderHistory(@Query("buy_id") int buyerId,
                                       @Query("month") int month,
                                       @Query("year") int year);

    @POST("buyer-split-order")
    Call<SplitOrderResponse> postPaymentNotify(@Query("v_id") int vendorId,
                                               @Query("orderId") String orderId,
                                               @Query("buy_id") int buyerId);

    @POST("buyer-search-products")
    Call<SearchProducts> buyerSearchProducts(@Query("v_id") int vendorId,
                                             @Query("name") String searchQuery,
                                             @Query("page") int page);

    @POST("shop-rating-details")
    Call<ViewShopRating> viewAllRatingsOfShop(@Query("vender_id") int vendorId);

//    @POST("buyer-give-rating")
//    Call<GeneralResponse> rateStore(@Query("buy_id") int buyerId,
//                                    @Query("vId") int vendorId,
//                                    @Query("rate_value") float rating,
//                                    @Query("reviewsText") String reviewText);

    @POST("buyer-give-rating")
    Call<GeneralResponse> rateStore(@Body RateStoreModel rateStoreModel);

    @FormUrlEncoded
    @POST("https://api.textlocal.in/send/")
    Call<OTPResponse> sendOTP2(@Field("apiKey") String apiKey,
                               @Field("message") String message,
                               @Field("sender") String sender,
                               @Field("numbers") long recipient,
                               @Field("test") String test);

    @POST("user-check")
    Call<GeneralResponse> userValidityCheck(@Query("email") String email,
                                            @Query("number") String mobNumber);

    @POST("change-password")
    Call<GeneralResponse> changePassword(@Body PasswordModel passwordModel);

    @POST("forgot-password")
    Call<GeneralResponse> forgotPassword(@Body PasswordModel passwordModel);

    @POST("vendor-order-status-check")
    Call<OrderAcceptResponse> checkVendorOrderAcceptance(@Query("v_id") int vendorId,
                                                         @Query("buy_id") int buyerId);

    @GET("user-products-bycategory")
    Call<SearchProducts> getProductsByCategory(@Query("user") int vendorId,
                                               @Query("cat") int categoryId,
                                               @Query("page") int page);

    @POST("cancel-order")
    Call<OrderHistory> cancelOrder(@Query("user") int buyerId,
                                   @Query("order_id") String orderId,
                                   @Query("month") int month,
                                   @Query("year") int year);

    @POST("forgot-pass-no-validate")
    Call<NumberValidate> numberValidate(@Query("mobile") String mobileNumber);

    @POST("update-password-using-otp")
    Call<GeneralResponse> resetPassword(@Body ResetPasswordModel resetPasswordModel);

    @POST("single-product-details")
    Call<SingleProduct> getSingleProductDetail(@Query("v_id") int vendorId,
                                               @Query("prod_id") int productId,
                                               @Query("buy_id") Integer buyerId);

    @POST("product-rating-update")
    Call<GeneralResponse> rateProduct(@Body ProductReviewModel productReviewModel);

    @POST("product-allrating-view")
    Call<ViewShopRating> viewAllRatingsOfProduct(@Query("prod_id") int productId);

    @POST("product-rating-view")
    Call<ProductOwnRating> viewOwnRatingOfProduct(@Query("prod_id") int productId,
                                                  @Query("user_id") int buyerId);

    @POST("hot-selling-products")
    Call<ProductsModel> getHotSellingProducts(@Query("v_id") int vendorId,
                                              @Query("page") int page);

    @POST("logout-user")
    Call<GeneralResponse> logoutCustomer(@Body LogoutModel logoutModel);
}
