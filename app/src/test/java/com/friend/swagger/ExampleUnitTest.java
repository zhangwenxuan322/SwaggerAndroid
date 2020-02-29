package com.friend.swagger;

import com.friend.swagger.api.UserApi;
import com.friend.swagger.api.VerCodeApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.UserProfile;
import com.friend.swagger.entity.VerCode;
import com.google.gson.internal.LinkedTreeMap;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private UserApi userApi;
    private VerCodeApi verCodeApi;

    @Before
    public void retrofit_build() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(50000, TimeUnit.MICROSECONDS)
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.remoteUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        userApi = retrofit.create(UserApi.class);
        verCodeApi = retrofit.create(VerCodeApi.class);
    }

    @Test
    public void getUserByPhoneTest() throws IOException {
        Call<Map<String, Object>> call = userApi.getUserByPhone("15150576095");
        Response<Map<String, Object>> response = call.execute();
        if (response.body().get("code").equals("200")) {
            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>)response.body().get("userProfile");
            System.out.println(map.get("userCreateTime"));
        }

    }

    @Test
    public void getUserBySwaggerIdTest() throws IOException {
        Call<Map<String, Object>> call = userApi.getUserBySwaggerId("ckt1998");
        Response<Map<String, Object>> response = call.execute();
        System.out.println(response.body());
    }

    @Test
    public void registerUserTest() throws IOException {
        UserProfile userProfile = new UserProfile("陈匡婷", "女", "ckt", "", "ckt1998", "xxx.jpg", "happy", null, null, 0, null);
        Call<Map<String, Object>> call = userApi.userRegister(userProfile);
        Response<Map<String, Object>> response = call.execute();
        System.out.println(response.body());
    }

    @Test
    public void postCodeTest() throws IOException {
        Call<VerCode> call = verCodeApi.getCode("13813968440");
        Response<VerCode> response = call.execute();
        System.out.println(response.body());
    }

    @Test
    public void userLoginTest() throws IOException {
        Response<Map<String, Object>> response = userApi.userLogin("15150576095", "888").execute();
        System.out.println(response.body());
    }
}