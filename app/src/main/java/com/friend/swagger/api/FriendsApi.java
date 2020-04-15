package com.friend.swagger.api;

import com.friend.swagger.entity.FriendsManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    /**
     * 建立好友关系
     *
     * @param friendsManager
     * @return
     */
    @POST("friend/making")
    Call<Map<String, String>> friendMaking(@Body FriendsManager friendsManager);

    /**
     * 修改好友关系
     *
     * @param friendsManager
     * @return
     */
    @PUT("friend/modification")
    Call<Map<String, String>> friendModification(@Body FriendsManager friendsManager);

    /**
     * 删除好友
     *
     * @param friendsManager
     * @return
     */
    @DELETE("friend/release")
    Call<Map<String, String>> friendRelease(@Body FriendsManager friendsManager);

}
