package com.friend.swagger.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.friend.swagger.dao.CacheUserDao;
import com.friend.swagger.database.CacheUserDataBase;
import com.friend.swagger.entity.CacheUser;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-26 10:07
 **/
public class CacheUserRepository {
    private CacheUserDao cacheUserDao;
    private LiveData<List<CacheUser>> allCaches;

    public CacheUserRepository(Application application) {
        CacheUserDataBase cacheUserDataBase = CacheUserDataBase.getInstance(application);
        cacheUserDao = cacheUserDataBase.cacheUserDao();
        allCaches = cacheUserDao.getAllCaches();
    }

    public void insert(CacheUser cacheUser) {
        new InsertUserAsyncTask(cacheUserDao).execute(cacheUser);
    }

    public void deleteAllCaches() {
        new DeleteAllUsersAsyncTask(cacheUserDao).execute();
    }

    public LiveData<List<CacheUser>> getAllCaches() {
        return allCaches;
    }

    private static class InsertUserAsyncTask extends AsyncTask<CacheUser, Void, Void> {
        private CacheUserDao cacheUserDao;

        private InsertUserAsyncTask(CacheUserDao cacheUserDao) {
            this.cacheUserDao = cacheUserDao;
        }

        @Override
        protected Void doInBackground(CacheUser... cacheUsers) {
            cacheUserDao.insert(cacheUsers[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private CacheUserDao cacheUserDao;

        private DeleteAllUsersAsyncTask(CacheUserDao cacheUserDao) {
            this.cacheUserDao = cacheUserDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cacheUserDao.deleteAllCaches();
            return null;
        }
    }
}
