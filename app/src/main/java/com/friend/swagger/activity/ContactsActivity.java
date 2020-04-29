package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.api.FriendsApi;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.FriendsManager;
import com.tamsiree.rxtool.view.RxToast;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    // api
    FriendsApi friendsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getFriendList();
    }

    private void getFriendList() {
        friendsApi = RetrofitService.createService(FriendsApi.class);
        friendsApi.getFirendList(Constant.USER_ID).enqueue(new Callback<List<FriendsManager>>() {
            @Override
            public void onResponse(Call<List<FriendsManager>> call, Response<List<FriendsManager>> response) {
                if (response.body() == null) {
                    RxToast.error("请求失败");
                    return;
                }
                RxToast.info(response.body().toString());
            }

            @Override
            public void onFailure(Call<List<FriendsManager>> call, Throwable t) {
                RxToast.error("请求失败");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
