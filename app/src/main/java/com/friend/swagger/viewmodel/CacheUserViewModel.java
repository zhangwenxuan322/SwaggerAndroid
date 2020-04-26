package com.friend.swagger.viewmodel;

import android.app.Application;

import com.friend.swagger.entity.CacheUser;
import com.friend.swagger.repository.CacheUserRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-26 10:20
 **/
public class CacheUserViewModel extends AndroidViewModel {
    private CacheUserRepository cacheUserRepository;
    private LiveData<List<CacheUser>> cacheUsers;

    public CacheUserViewModel(@NonNull Application application) {
        super(application);
        cacheUserRepository = new CacheUserRepository(application);
        cacheUsers = cacheUserRepository.getAllCaches();
    }

    public void insert(CacheUser cacheUser) {
        cacheUserRepository.insert(cacheUser);
    }

    public void deleteAllCaches() {
        cacheUserRepository.deleteAllCaches();
    }

    public LiveData<List<CacheUser>> getCacheUser() {
        return cacheUsers;
    }
}
