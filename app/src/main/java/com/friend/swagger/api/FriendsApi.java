package com.friend.swagger.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-12 20:57
 **/
public interface FriendsApi {
    /**
     * 判断两人是否为好友
     *
     * @param mainUserId
     * @param friendUserId
     * @return
     */
    @GET("friend/filter")
    Call<Map<String, Object>> friendFilter(@Query("mainUserId") Integer mainUserId,
                                           @Query("friendUserId") Integer friendUserId);
}
