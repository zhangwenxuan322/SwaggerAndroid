package com.friend.swagger.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @Author ZhangWenXuan
 * @Date 2020-05-04 22:11
 **/
public interface UpdateApi {
    /**
     * 获取最新Apk信息
     * @return
     */
    @GET("apk/latest")
    Call<Map<String, Object>> getLatestApkInfo();
}
