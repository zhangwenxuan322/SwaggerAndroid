package com.friend.swagger.api;

import com.friend.swagger.entity.UserProfile;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-23 11:21
 **/
public interface UserApi {
    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    @GET("user/phone/{phone}")
    Call<UserProfile> getUserByPhone(@Path("phone") String phone);

    /**
     * 根据swaggerid查询用户信息
     * @param swaggerId
     * @return
     */
    @GET("user/swagger_id/{swaggerId}")
    Call<UserProfile> getUserBySwaggerId(@Path("swaggerId") String swaggerId);

    /**
     * 用户注册
     * @param userProfile
     * @return
     */
    @POST("user")
    Call<Map<String, Object>> registerUser(@Body UserProfile userProfile);
}
