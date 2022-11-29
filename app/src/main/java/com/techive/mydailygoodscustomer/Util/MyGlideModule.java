package com.techive.mydailygoodscustomer.Util;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;

@GlideModule
public class MyGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull @NotNull Context context, @NonNull @NotNull Glide glide, @NonNull @NotNull Registry registry) {
        super.registerComponents(context, glide, registry);

        CertificateClassOS6 certificateClassOS6 = new CertificateClassOS6();
        SSLSocketFactory sslSocketFactory = certificateClassOS6.getSslSocketFactory(context);

        OkHttpClient okHttpClient;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, certificateClassOS6.getTrustManager())
                    /*.addInterceptor(httpLoggingInterceptor)*/.build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .build();
        }

        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
