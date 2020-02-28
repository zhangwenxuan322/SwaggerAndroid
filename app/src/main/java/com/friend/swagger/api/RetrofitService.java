package com.friend.swagger.api;

import com.friend.swagger.common.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-27 14:31
 **/
public class RetrofitService {
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(50000, TimeUnit.MICROSECONDS)
            .readTimeout(50000, TimeUnit.MILLISECONDS)
            .build();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.remoteUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
