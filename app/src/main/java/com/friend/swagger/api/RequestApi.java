package com.friend.swagger.api;

import com.friend.swagger.entity.FriendRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-28 21:59
 **/
public interface RequestApi {
    /**
     * 发布好友请求
     *
     * @param friendRequest
     * @return
     */
    @POST("request")
    Call<Map<String, Object>> postFriendRequest(@Body FriendRequest friendRequest);

    /**
     * 查询请求列表
     *
     * @param mainId
     * @return
     */
    @GET("request/list")
    Call<List<FriendRequest>> getRequestList(@Query("mainId") Integer mainId);

    /**
     * 更新好友请求信息
     *
     * @param friendRequest
     * @return
     */
    @PUT("request/operation")
    Call<Map<String, Object>> operateRequest(@Body FriendRequest friendRequest);
}
