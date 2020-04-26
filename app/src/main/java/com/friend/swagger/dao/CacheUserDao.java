package com.friend.swagger.dao;

import com.friend.swagger.entity.CacheUser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-26 09:46
 **/
@Dao
public interface CacheUserDao {
    @Insert
    void insert(CacheUser cacheUser);

    @Query("delete from cache_user_table")
    void deleteAllCaches();

    @Query("select * from cache_user_table order by id desc")
    LiveData<List<CacheUser>> getAllCaches();
}
