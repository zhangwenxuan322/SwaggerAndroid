package com.friend.swagger.api;

import com.friend.swagger.entity.VerCode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-26 14:37
 **/
public interface VerCodeApi {
    /**
     * 获取验证码
     * @param phone
     * @return
     */
    @GET("code/{phone}")
    Call<VerCode> getCode(@Path("phone") String phone);
}
