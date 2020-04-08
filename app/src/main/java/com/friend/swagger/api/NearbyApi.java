package com.friend.swagger.api;

import com.friend.swagger.entity.NearbyUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Author ZhangWenXuan
 * @Date 2020-03-23 17:20
 **/
public interface NearbyApi {
    /**
     * 获取附近的人
     *
     * @return
     */
    @GET("nearby/list")
    Call<List<NearbyUser>> getNearbyUsers(@Query("lon") Double lon,
                                          @Query("lat") Double lat,
                                          @Query("limit") Double limit,
                                          @Query("name") String name,
                                          @Query("sex") String sex);
}
