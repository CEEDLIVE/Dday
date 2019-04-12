package com.example.ceedlive.dday.http;

import com.example.ceedlive.dday.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {

    private static Retrofit mRetrofit;
    private static HttpLoggingInterceptor mHttpLoggingInterceptor;
    private static OkHttpClient mOkHttpClient;
    private static Retrofit.Builder mRetrofitBuilder;

    public static Retrofit getInstance(String baseUrl) {
        if (mRetrofit == null) {
            mHttpLoggingInterceptor = new HttpLoggingInterceptor();
            mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(mHttpLoggingInterceptor)
                    .build();

            mRetrofitBuilder = new Retrofit.Builder()
                    .baseUrl(baseUrl);

            if (BuildConfig.DEBUG) {
                mRetrofitBuilder.client(mOkHttpClient);
            }

            mRetrofit = mRetrofitBuilder
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

}
