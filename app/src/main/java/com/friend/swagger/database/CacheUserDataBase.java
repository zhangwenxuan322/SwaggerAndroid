package com.friend.swagger.database;

import android.content.Context;
import android.os.AsyncTask;

import com.friend.swagger.dao.CacheUserDao;
import com.friend.swagger.entity.CacheUser;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-26 09:43
 **/
@Database(entities = CacheUser.class, version = 1)
public abstract class CacheUserDataBase extends RoomDatabase {
    private static CacheUserDataBase instance;

    public abstract CacheUserDao cacheUserDao();

    public static synchronized CacheUserDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CacheUserDataBase.class, "cache_user_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(cacheUserCallBack)
                    .build();
        }
        return instance;
    }

    private static CacheUserDataBase.Callback cacheUserCallBack = new CacheUserDataBase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CacheUserDao cacheUserDao;

        private PopulateDbAsyncTask(CacheUserDataBase db) {
            cacheUserDao = db.cacheUserDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            cacheUserDao.insert(new CacheUser("test", "test"));
//            cacheUserDao.deleteAllCaches();
            return null;
        }
    }
}
