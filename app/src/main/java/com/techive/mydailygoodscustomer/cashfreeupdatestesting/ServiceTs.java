package com.techive.mydailygoodscustomer.cashfreeupdatestesting;


import com.techive.mydailygoodscustomer.Models.CashFreeOrder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceTs {
    ServiceTs service = null;
    String AUTHORIZATION = "token";


    static ServiceTs getService() {
        if ( service== null) {
            String BASE_URL ="https://www.mydailygoods.com/api/";
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(60, TimeUnit.SECONDS);
            httpClient.readTimeout(60, TimeUnit.SECONDS);
            httpClient.writeTimeout(60, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);

            return new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
                    .create(ServiceTs.class);
        }
        return service;
    }


/*    @POST("buyer-create-order-test")
    Call<DataResponse> createCashFreeOrder(@Query("buy_id")String buyerId,
                                            @Query("amount") String amount*//*,
                                            @Query("orderId") String orderId*//*); */

    @POST("buyer-create-order")
    Call<CashFreeOrder> createCashFreeOrder(@Query("buy_id")String buyerId,
                                            @Query("amount") String amount/*,
                                            @Query("orderId") String orderId*/);



}
