package com.zdjf.qqh.net;


import android.support.annotation.NonNull;

import com.zdjf.qqh.data.commons.Urls;
import com.zdjf.qqh.utils.HttpsUtil;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.utils.Tools;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class MainRetrofit {
    public LoanService LoanService;

    public static final String BASE_URL = Urls.baseUrl;

    private static final int DEFAULT_TIMEOUT = 60;

    private Retrofit retrofit;

    MainRetrofit() {

        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                LogUtil.e(message);
            }
        });
        // Log信息拦截器
        // HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory(null, null, null);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        okhttp3.Response orginalResponse = chain.proceed(chain.request());
                        return orginalResponse.newBuilder()
                                .addHeader("platform", "ANDROID")
                                .addHeader("uniqueNo", Tools.getDeviceId())
                                .body(new ProgressResponseBody(orginalResponse.body(), new ProgressListener() {
                                    @Override
                                    public void onProgress(long progress, long total, boolean done) {
                                        LogUtil.d("onProgress: " + "total ---->" + total + "done ---->" + progress);
                                    }
                                }))
                                .build();
                    }
                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(loggingInterceptor)//设置 Debug Log 模式
                .build();
        /*OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);*/

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())  // 增加返回值为String的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        LoanService = retrofit.create(LoanService.class);
    }

    public LoanService getService() {
        return LoanService;
    }
}
