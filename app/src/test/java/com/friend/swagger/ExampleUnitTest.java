package com.friend.swagger;

import android.location.Location;

import com.friend.swagger.activity.LoginActivity;
import com.friend.swagger.api.FriendsApi;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.api.VerCodeApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.common.LocationUtil;
import com.friend.swagger.common.SystemUtil;
import com.friend.swagger.entity.FriendsManager;
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
import okhttp3.ResponseBody;
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
    private FriendsApi friendsApi;

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
        friendsApi = retrofit.create(FriendsApi.class);
    }

    @Test
    public void getUserByPhoneTest() throws IOException {
        Call<Map<String, Object>> call = userApi.getUserByPhone("15150576095");
        Response<Map<String, Object>> response = call.execute();
        if (response.body().get("code").equals("200")) {
            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>)response.body().get("userProfile");
            System.out.println(map.toString());
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

//    @Test
//    public void userLoginTest() throws IOException {
//        Response<Map<String, Object>> response = userApi.userLogin("15150576095", "888", "111.111.111.111", "上海").execute();
//        System.out.println(response.body());
//    }

    @Test
    public void ipTest() {
        System.out.println(SystemUtil.getIpAddressString());
    }

    @Test
    public void changePasswordTest() throws IOException {
        Response<Map<String, Object>> response = userApi.changePassword("13813968440", "233").execute();
        System.out.println(response.body());
    }

    @Test
    public void downloadPortraitTest() throws IOException{
        Response<ResponseBody> responseBodyResponse = userApi.downloadPortrait("androidtest1.png").execute();
        System.out.println(responseBodyResponse.body());
    }

    @Test
    public void logoutTest() throws IOException {
        Response<Map<String, Object>> mapResponse = userApi.logout(1).execute();
        System.out.println(mapResponse.body());
    }

    @Test
    public void getUserByIdTest() throws IOException {
        Response<Map<String, Object>> response = userApi.getUserById(1).execute();
        System.out.println(response.body());
    }

    @Test
    public void getNumTest() throws IOException {
        int a = SystemUtil.getNum("androidtest45");
        System.out.println(a);
    }

    @Test
    public void friendFilterTest() throws IOException {
        Response<Map<String, Object>> response = friendsApi.friendFilter(13, 14).execute();
        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("friend");
        System.out.println(map);
    }
}