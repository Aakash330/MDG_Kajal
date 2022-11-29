package com.techive.mydailygoodscustomer.Repositories;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techive.mydailygoodscustomer.Models.APIResponse.GeneralResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptCartFragmentResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.OrderAcceptResponse;
import com.techive.mydailygoodscustomer.Models.APIResponse.SplitOrderResponse;
import com.techive.mydailygoodscustomer.Models.Cart;
import com.techive.mydailygoodscustomer.Models.CashFreeOrder;
import com.techive.mydailygoodscustomer.Models.PlaceOrderModel;
import com.techive.mydailygoodscustomer.R;
import com.techive.mydailygoodscustomer.Util.ApplicationData;
import com.techive.mydailygoodscustomer.Util.CertificateClassOS6;
import com.techive.mydailygoodscustomer.Util.MDG_CustomerAPI_Interface;
import com.techive.mydailygoodscustomer.Util.NetworkUtil;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartRepository {
    private static final String TAG = "CartRepository";

    private static CartRepository cartRepositoryInstance;

    private static Context applicationContext;

    /* Using this particular interface instance for Place Order. */
    public /*static*/ MDG_CustomerAPI_Interface mdg_customerAPI_interface;

//    private MDGCustomerDatabase mdgCustomerDatabase;
//    private CashFreeOrderDao cashFreeOrderDao;

    private MutableLiveData<GeneralResponse> addToCartMutableLiveData;
    private MutableLiveData<Cart> cartMutableLiveData;
    private MutableLiveData<GeneralResponse> removeFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> completelyRemoveFromCartMutableLiveData;
    private MutableLiveData<GeneralResponse> placeOrderMutableLiveData;
    private MutableLiveData<CashFreeOrder> cashFreeOrderMutableLiveData;
    private MutableLiveData<SplitOrderResponse> splitOrderResponseMutableLiveData;
    private MutableLiveData<OrderAcceptResponse> orderAcceptResponseMutableLiveData;
    private MutableLiveData<OrderAcceptCartFragmentResponse> orderAcceptCartFragmentResponseMutableLiveData;//change response
    // OrderAcceptResponse to OrderAcceptResponseCartFragment for Cart  @kajal 11_16_22
    private MutableLiveData<OrderAcceptResponse> prodAddAcceptResponseMutableLiveData;

//    private Handler handler;

//    private Executor executor;

    private int toBeAddedProductId;

    public CartRepository() {
        Log.i(TAG, "CartRepository: Empty Constructor fired!");

        initCartRepository();
    }

    public static CartRepository getCartRepositoryInstance(Context context) {
        Log.i(TAG, "getCartRepositoryInstance: fired!");

        if (cartRepositoryInstance == null) {
            applicationContext = context;
            cartRepositoryInstance = new CartRepository();
            Log.i(TAG, "getCartRepositoryInstance: Cart Repository initialized now!");
        }
        Log.i(TAG, "getCartRepositoryInstance: cartRepositoryInstance: " + cartRepositoryInstance);
        return cartRepositoryInstance;
    }

    /*public*/
    private void initCartRepository() {
        Log.i(TAG, "initCartRepository: fired!");

        ApplicationData.initializeRetrofit(applicationContext);

        initializeRetrofit(applicationContext);
//        mdgCustomerDatabase = MDGCustomerDatabase.getInstance(applicationContext);
//        cashFreeOrderDao = mdgCustomerDatabase.cashFreeOrderDao();

        addToCartMutableLiveData = new MutableLiveData<>();
        Log.i(TAG, "initCartRepository: BEFORE, cartMutableLiveData: " + cartMutableLiveData);
        cartMutableLiveData = new MutableLiveData<>();
        Log.i(TAG, "initCartRepository: AFTER, cartMutableLiveData: " + cartMutableLiveData);
        removeFromCartMutableLiveData = new MutableLiveData<>();
        completelyRemoveFromCartMutableLiveData = new MutableLiveData<>();
        placeOrderMutableLiveData = new MutableLiveData<>();
        cashFreeOrderMutableLiveData = new MutableLiveData<>();
        splitOrderResponseMutableLiveData = new MutableLiveData<>();
        orderAcceptResponseMutableLiveData = new MutableLiveData<>();
        orderAcceptCartFragmentResponseMutableLiveData = new MutableLiveData<>();// @kajal 11_16_22
        prodAddAcceptResponseMutableLiveData = new MutableLiveData<>();

//        executor = Executors.newSingleThreadExecutor();
    }

    /*public*/
    private /*static*/ void initializeRetrofit(Context applicationContext) {
        Log.i(TAG, "initializeRetrofit: fired!");

        if (mdg_customerAPI_interface == null) {
            Log.i(TAG, "initializeRetrofit: MDG Interface was null. Initializing now...");

            String BASE_URL = "https://www.mydailygoods.com/api/";

            CertificateClassOS6 certificateClassOS6 = new CertificateClassOS6();
            SSLSocketFactory sslSocketFactory = certificateClassOS6.getSslSocketFactory(applicationContext);
            Log.i(TAG, "initializeRetrofit: sslSocketFactory.toString(): " + sslSocketFactory.toString());

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Log.i(TAG, "initializeRetrofit: On OS <= M : " + Build.VERSION.SDK_INT);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(25, TimeUnit.SECONDS)
                        .writeTimeout(25, TimeUnit.SECONDS)
                        .sslSocketFactory(sslSocketFactory, certificateClassOS6.getTrustManager())
                        .addInterceptor(httpLoggingInterceptor).build();
            } else {
                Log.i(TAG, "initializeRetrofit: On OS > M : " + Build.VERSION.SDK_INT);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(25, TimeUnit.SECONDS)
                        .writeTimeout(25, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor).build();
            }
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            mdg_customerAPI_interface = retrofit.create(MDG_CustomerAPI_Interface.class);
        }
    }

    /* GETTERS - START */
    public MutableLiveData<GeneralResponse> getAddToCartMutableLiveData() {
        return addToCartMutableLiveData;
    }

    public MutableLiveData<Cart> getCartMutableLiveData() {
        return cartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getRemoveFromCartMutableLiveData() {
        return removeFromCartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getCompletelyRemoveFromCartMutableLiveData() {
        return completelyRemoveFromCartMutableLiveData;
    }

    public MutableLiveData<GeneralResponse> getPlaceOrderMutableLiveData() {
        return placeOrderMutableLiveData;
    }

    public MutableLiveData<CashFreeOrder> getCashFreeOrderMutableLiveData() {
        return cashFreeOrderMutableLiveData;
    }

    public MutableLiveData<SplitOrderResponse> getSplitOrderResponseMutableLiveData() {
        return splitOrderResponseMutableLiveData;
    }

    public MutableLiveData<OrderAcceptResponse> getOrderAcceptResponseMutableLiveData() {
        return orderAcceptResponseMutableLiveData;
    }
    public MutableLiveData<OrderAcceptCartFragmentResponse> getOrderAcceptCartFragmentResponseMutableLiveData() {
        return orderAcceptCartFragmentResponseMutableLiveData;
    }

    public MutableLiveData<OrderAcceptResponse> getProdAddAcceptResponseMutableLiveData() {
        return prodAddAcceptResponseMutableLiveData;
    }
    /* GETTERS - END */

    /* SETTERS - START */
    public void setAddToCartMutableLiveData(GeneralResponse addToCart) {
        addToCartMutableLiveData.setValue(addToCart);
    }

    public void setCartMutableLiveData(Cart cart) {
        cartMutableLiveData.setValue(cart);
    }

    public void setRemoveFromCartMutableLiveData(GeneralResponse removeFromCart) {
        removeFromCartMutableLiveData.setValue(removeFromCart);
    }

    public void setCompletelyRemoveFromCartMutableLiveData(GeneralResponse completelyRemoveFromCart) {
        completelyRemoveFromCartMutableLiveData.setValue(completelyRemoveFromCart);
    }

    public void setPlaceOrderMutableLiveData(GeneralResponse placeOrder) {
        placeOrderMutableLiveData.setValue(placeOrder);
    }

    public void setCashFreeOrderMutableLiveData(CashFreeOrder cashFreeOrder) {
        cashFreeOrderMutableLiveData.setValue(cashFreeOrder);
    }

    public void setSplitOrderResponseMutableLiveData(SplitOrderResponse splitOrderResponse) {
        splitOrderResponseMutableLiveData.setValue(splitOrderResponse);
    }

    public void setOrderAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        orderAcceptResponseMutableLiveData.setValue(orderAcceptResponse);
    }

    public void setProdAddAcceptResponseMutableLiveData(OrderAcceptResponse orderAcceptResponse) {
        prodAddAcceptResponseMutableLiveData.setValue(orderAcceptResponse);
    }
    /* SETTERS - END */

    /* NO DATA FOUND
    {
    "msg": "No data found!",
    "error": 400,
    "products": {
        "current_page": "",
        "data": [],
        "last_page": "",
        "total": ""
    }
}*/

    /* VIEW CART DETAILS */
    public boolean getCartDetails() {
        Log.i(TAG, "getCartDetails: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "getCartDetails: Network Available!");

            Call<Cart> cartCall = ApplicationData.mdg_customerAPI_interface.getCartDetails(
                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId());
            cartCall.enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    Log.i(TAG, "onResponse: GET CART DETAILS Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        Cart cartUnsuccessful = new Cart("Somehow server didn't respond!", response.code(), null);
                        cartMutableLiveData.setValue(cartUnsuccessful);
                        return;
                    }
                    Cart cart = response.body();
                    Log.i(TAG, "onResponse: CART DETAILS, cart: " + cart);
//                    cart.getCart_data().getFreebies().get(1).setMin_cart_value(6000);
//                    cart.getCart_data().getCoupons().get(1).setMin_spend(6000);
                    cartMutableLiveData.setValue(cart);
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Log.i(TAG, "onFailure: GET CART DETAILS Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        Cart cartFailedSocketTimeout = new Cart(applicationContext.getString(R.string.weak_internet_connection), 1001, null);
                        cartMutableLiveData.setValue(cartFailedSocketTimeout);
                        return;
                    }

                    Cart cartFailed = new Cart("Somehow Cart request failed. Please try again later!", 1000, null);
                    cartMutableLiveData.setValue(cartFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* WILL SOON REMOVE ALL USES OF THIS. */
    /* ADD/INCREMENT PRODUCT TO CART */
    public boolean addToCart(int productId) {
        Log.i(TAG, "addToCart: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "addToCart: Network Available!");

            Call<GeneralResponse> addToCartCall = ApplicationData.mdg_customerAPI_interface.addToCart(
                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId(), productId, 1);
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

                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!\nPlease try again later!", prodId, response.code());
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
            return true;
        }
        return false;
    }

    //CALLING ADDTOCART INTERNALLY FROM THE CART REPOSITORY.
    public void addToCart2() {
        Log.i(TAG, "addToCart2: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "addToCart2: Network Available!");

            Call<GeneralResponse> addToCartCall = ApplicationData.mdg_customerAPI_interface.addToCart(
                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId(), toBeAddedProductId, 1);
            addToCartCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: ADD TO CART 2 Response seems to be a success!");

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

                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!\nPlease try again later!", prodId, response.code());
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

    /* DECREMENT PRODUCT FROM CART */
    public boolean removeFromCart(int productId) {
        Log.i(TAG, "removeFromCart: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "removeFromCart: Network Available!");

            Call<GeneralResponse> removeFromCartCall = ApplicationData.mdg_customerAPI_interface.removeFromCart(
                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId(), productId, 1);
            removeFromCartCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: REMOVE FROM CART Response seems to be a success!");

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

                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!\nPlease try again later!", prodId, response.code());
                        removeFromCartMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: REMOVE FROM CART, generalResponse: " + generalResponse);
                    removeFromCartMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: REMOVE FROM CART Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

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

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), prodId, 1001);
                        removeFromCartMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Remove from Cart request failed!", prodId, 1000);
                    removeFromCartMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* DELETE PRODUCT FROM CART */
    public boolean completelyRemoveFromCart(int productId) {
        Log.i(TAG, "completelyRemoveFromCart: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "completelyRemoveFromCart: Network Available!");

            Call<GeneralResponse> completelyRemoveFromCartCall = ApplicationData.mdg_customerAPI_interface.completelyRemoveFromCart(
                    ApplicationData.getLoggedInBuyerId(), ApplicationData.getDefaultStoreId(), productId);
            completelyRemoveFromCartCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: COMPLETELY REMOVE FROM CART Response seems to be a success!");

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

                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow server didn't respond!\nPlease try again later!", prodId, response.code());
                        completelyRemoveFromCartMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: COMPLETELY REMOVE FROM CART, generalResponse: " + generalResponse);
                    completelyRemoveFromCartMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: COMPLETELY REMOVE FROM CART Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    String prodId = "";
                    try {
                        Log.i(TAG, "onResponse: call.request().toString(): " + call.request().toString() + "\tcall.request().toString().length(): " + call.request().toString().length());
                        //I/CartRepository: onResponse: call.request().toString(): Request{method=POST, url=https://www.mydailygoods.com/api/buyer-delete-cart-item?buy_id=126&v_id=121&prod_id=37385, tags={class retrofit2.Invocation=com.techive.mydailygoodscustomer.Util.MDG_CustomerAPI_Interface.completelyRemoveFromCart() [126, 121, 37385]}}	call.request().toString().length(): 259

                        prodId = call.request().toString().substring((call.request().toString().indexOf("prod_id") + 8),
                                (call.request().toString().indexOf("tags") - 2));
                        Log.i(TAG, "onResponse: prodId: " + prodId);
                    } catch (Exception e) {
                        Log.i(TAG, "onResponse: Exception in call.request().toString()!");
                        e.printStackTrace();
                    }

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeout = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), prodId, 1001);
                        completelyRemoveFromCartMutableLiveData.setValue(generalResponseFailedSocketTimeout);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Completely Remove from Cart request failed!", prodId, 1000);
                    completelyRemoveFromCartMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* MDG Server PLACE ORDER */
    public boolean placeOrder(PlaceOrderModel placeOrderModel) {
        Log.i(TAG, "placeOrder: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "placeOrder: Network Available!");

//            Call<GeneralResponse> placeOrderCall = ApplicationData.mdg_customerAPI_interface.placeOrder(placeOrderModel);
            Call<GeneralResponse> placeOrderCall = mdg_customerAPI_interface.placeOrder(placeOrderModel);
            placeOrderCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    Log.i(TAG, "onResponse: PLACE ORDER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        GeneralResponse generalResponseUnsuccessful = new GeneralResponse("Somehow Server didn't respond!\nPlease try again later!", "", response.code());
                        placeOrderMutableLiveData.setValue(generalResponseUnsuccessful);
                        return;
                    }
                    GeneralResponse generalResponse = response.body();
                    Log.i(TAG, "onResponse: PLACE ORDER, generalResponse: " + generalResponse);
                    placeOrderMutableLiveData.setValue(generalResponse);
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: PLACE ORDER Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        GeneralResponse generalResponseFailedSocketTimeOut = new GeneralResponse(applicationContext.getString(R.string.weak_internet_connection), "", 1001);
                        placeOrderMutableLiveData.setValue(generalResponseFailedSocketTimeOut);
                        return;
                    }
                    GeneralResponse generalResponseFailed = new GeneralResponse("Somehow Place Order Request failed!\nPlease try again later!", "", 1000);
                    placeOrderMutableLiveData.setValue(generalResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* CREATE ORDER ON CASHFREE SERVER */
    public boolean createCashFreeOrder(float orderAmount/*, String uniqueOrderId*/) {
        Log.i(TAG, "createCashFreeOrder: fired! orderAmount: " + orderAmount /*+ "\tuniqueOrderId: " + uniqueOrderId*/);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "createCashFreeOrder: Network Available!");

            Call<CashFreeOrder> cashFreeOrderCall = ApplicationData.mdg_customerAPI_interface.createCashFreeOrder(
                    ApplicationData.getLoggedInBuyerId(), orderAmount/*, uniqueOrderId*/);
            cashFreeOrderCall.enqueue(new Callback<CashFreeOrder>() {
                @Override
                public void onResponse(Call<CashFreeOrder> call, Response<CashFreeOrder> response) {
                    Log.i(TAG, "onResponse: CREATE CASH FREE ORDER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        CashFreeOrder cashFreeOrderUnsuccessful = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", "Somehow Server didn't respond! " + response.code());
                        cashFreeOrderMutableLiveData.setValue(cashFreeOrderUnsuccessful);
                        return;
                    }
                    CashFreeOrder cashFreeOrder = response.body();
                    Log.i(TAG, "onResponse: cashFreeOrder: " + cashFreeOrder);
                    cashFreeOrderMutableLiveData.setValue(cashFreeOrder);
                }

                @Override
                public void onFailure(Call<CashFreeOrder> call, Throwable t) {
                    Log.i(TAG, "onFailure: CREATE CASH FREE ORDER Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        CashFreeOrder cashFreeOrderFailedSocketTimeOut = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", applicationContext.getString(R.string.weak_internet_connection));
                        cashFreeOrderMutableLiveData.setValue(cashFreeOrderFailedSocketTimeOut);
                        return;
                    }
                    CashFreeOrder cashFreeOrderFailed = new CashFreeOrder("", "", "", "", 0f, "", "ERROR", "", "Somehow Create Order Request Failed!");
                    cashFreeOrderMutableLiveData.setValue(cashFreeOrderFailed);
                }
            });
            return true;
        }
        return false;
    }

    public boolean postPaymentNotifyMDGServer(String orderId) {
        Log.i(TAG, "postPaymentNotifyMDGServer: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "postPaymentNotifyMDGServer: Network Available!");

            Call<SplitOrderResponse> postPaymentNotifyCall = ApplicationData.mdg_customerAPI_interface.postPaymentNotify(
                    ApplicationData.getDefaultStoreId(), orderId, ApplicationData.getLoggedInBuyerId());
            postPaymentNotifyCall.enqueue(new Callback<SplitOrderResponse>() {
                @Override
                public void onResponse(Call<SplitOrderResponse> call, Response<SplitOrderResponse> response) {
                    Log.i(TAG, "onResponse: SPLIT ORDER Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        SplitOrderResponse splitOrderResponseUnsuccessful = new SplitOrderResponse("Somehow server didn't respond!", "ERROR");
                        splitOrderResponseMutableLiveData.setValue(splitOrderResponseUnsuccessful);
                        return;
                    }
                    SplitOrderResponse splitOrderResponse = response.body();
                    Log.i(TAG, "onResponse: splitOrderResponse: " + splitOrderResponse);
                    splitOrderResponseMutableLiveData.setValue(splitOrderResponse);
                }

                @Override
                public void onFailure(Call<SplitOrderResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: SPLIT ORDER Response seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        SplitOrderResponse splitOrderResponseFailedSocketTimeout = new SplitOrderResponse(applicationContext.getString(R.string.weak_internet_connection), "ERROR");
                        splitOrderResponseMutableLiveData.setValue(splitOrderResponseFailedSocketTimeout);
                        return;
                    }
                    SplitOrderResponse splitOrderResponseFailed = new SplitOrderResponse("Somehow split order Request failed!", "ERROR");
                    splitOrderResponseMutableLiveData.setValue(splitOrderResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    /* CHECK IF STORE CAN ACCEPT ORDERS TO PLACE ORDER. (Exception for BestDeals & RecentlyAdded) */
    public boolean checkVendorOrderAcceptance() {
        Log.i(TAG, "checkVendorOrderAcceptance: fired!");

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "checkVendorOrderAcceptance: Network Available!");

            Call<OrderAcceptResponse> orderAcceptResponseCall = ApplicationData.mdg_customerAPI_interface.
                    checkVendorOrderAcceptance(ApplicationData.getDefaultStoreId(), ApplicationData.getLoggedInBuyerId());
            orderAcceptResponseCall.enqueue(new Callback<OrderAcceptResponse>() {
                @Override
                public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                    Log.i(TAG, "onResponse: ORDER ACCEPT Response seems to be a success!");

                    if (!response.isSuccessful()) {
                        OrderAcceptResponse orderAcceptResponseUnsuccessful = new OrderAcceptResponse(applicationContext.getString(R.string.server_didnt_respond), -1, response.code());
                        orderAcceptResponseMutableLiveData.setValue(orderAcceptResponseUnsuccessful);
                        return;
                    }
                    OrderAcceptResponse orderAcceptResponse = response.body();
                    Log.i(TAG, "onResponse: orderAcceptResponse: " + orderAcceptResponse);
                    orderAcceptResponseMutableLiveData.setValue(orderAcceptResponse);
                }

                @Override
                public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
                    Log.i(TAG, "onFailure: ORDER ACCEPT Request seems to have failed!\tt.getMessage(): " + t.getMessage());
                    t.printStackTrace();

                    if (t instanceof SocketTimeoutException) {
                        OrderAcceptResponse orderAcceptResponseFailedSocketTimeout = new OrderAcceptResponse(applicationContext.getString(R.string.weak_internet_connection), -1, 1001);
                        orderAcceptResponseMutableLiveData.setValue(orderAcceptResponseFailedSocketTimeout);
                        return;
                    }
                    OrderAcceptResponse orderAcceptResponseFailed = new OrderAcceptResponse("Somehow Order Accept Response failed!\nPlease try again later!", -1, 1000);
                    orderAcceptResponseMutableLiveData.setValue(orderAcceptResponseFailed);
                }
            });
            return true;
        }
        return false;
    }

    //FOR CHECKING VENDOR STATUS TO ADD PRODUCT
    public boolean checkVendorOrderAcceptance2(int productId) {
        Log.i(TAG, "checkVendorOrderAcceptance2: fired! productId: " + productId);

        if (!NetworkUtil.networkConnectivityStatus(applicationContext).equals("No Internet Available!")) {
            Log.i(TAG, "checkVendorOrderAcceptance2: Network Available!");

            toBeAddedProductId = productId;

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
                        addToCart2();
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


    /////////////////////////////////DATABASE FUNCTIONS & RUNNABLES/////////////////////////////////

//    public void storeCashFreeOrderInLocalDB(Handler handler) {
//        Log.i(TAG, "storeCashFreeOrderInLocalDB: fired!");
//
//        this.handler = handler;
//
//        executor.execute(storeCashFreeOrderRunnable);
//    }

//    Runnable storeCashFreeOrderRunnable = new Runnable() {
//        @Override
//        public void run() {
//            Log.i(TAG, "run: Inside Store CashFreeOrder runnable!");
//
//            CashFreeOrderEntity cashFreeOrderEntity = new CashFreeOrderEntity(ApplicationData.getLoggedInBuyerId(),
//                    ApplicationData.getDefaultStoreId(), cashFreeOrderMutableLiveData.getValue().getCf_order_id(),
//                    cashFreeOrderMutableLiveData.getValue().getOrder_id(), String.valueOf(cashFreeOrderMutableLiveData
//                    .getValue().getOrder_amount()), cashFreeOrderMutableLiveData.getValue().getOrder_expiry_time(),
//                    cashFreeOrderMutableLiveData.getValue().getOrder_status(), cashFreeOrderMutableLiveData.getValue().getOrder_token(),
//                    cashFreeOrderMutableLiveData.getValue().getOrder_note());
//
//            cashFreeOrderDao.insertCashFreeOrder(cashFreeOrderEntity);
//
//            /*WILL PROBABLY NEED TO SEND BACK A RESPONSE SO THAT PLACE ORDER BUTTON BECOMES ENABLED.
//            * WILL HAVE TO TAKE CARE OF INSERTING THE SAME ORDER INTO DB ONLY ONCE, SO AS TO AVOID HAVING
//            * DUPLICATE ENTRIES, UPON MULTIPLE FAILED PAYMENT ATTEMPTS.*/
//        }
//    };
}
