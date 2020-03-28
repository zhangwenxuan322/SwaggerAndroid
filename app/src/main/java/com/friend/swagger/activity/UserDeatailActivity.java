package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.UserDetailAdapter;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDeatailActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "indi.friend.swagger.UserDeatailActivity.EXTRA_ID";
    private RecyclerView userDetailRecyclerView;
    private UserDetailAdapter userDetailAdapter;
    private RecyclerView.LayoutManager userDetailLayoutManager;
    private List<String> detailInfos;
    private UserApi userApi;
    private ImageView userPortrait;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deatail);
        userApi = RetrofitService.createService(UserApi.class);
        userPortrait = findViewById(R.id.user_portrait);
        userName = findViewById(R.id.user_name);
        initToolbar();
        initRecyclerView();
        getUserData();
    }

    private void getUserData() {
        userApi.getUserById(getIntent().getIntExtra(EXTRA_ID, 0)).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() != null) {
                    if (response.body().get("code").equals("200")) {
                        detailInfos.clear();
                        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
                        userName.setText(String.valueOf(map.get("userName")));
                        if (String.valueOf(map.get("userSwaggerId")).equals("null") || String.valueOf(map.get("userSwaggerId")).equals(""))
                            detailInfos.add("未设置");
                        else
                            detailInfos.add(String.valueOf(map.get("userSwaggerId")));
                        detailInfos.add(String.valueOf(map.get("userPhone")));
                        detailInfos.add(String.valueOf(map.get("userSex")));
                        if (String.valueOf(map.get("userBio")).equals("null") || String.valueOf(map.get("userBio")).equals(""))
                            detailInfos.add("未设置");
                        else
                            detailInfos.add(String.valueOf(map.get("userBio")));
                        userDetailAdapter.notifyDataSetChanged();
                        userApi.downloadPortrait(String.valueOf(map.get("userPortrait"))).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null) {
                                    InputStream inputStream = response.body().byteStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    userPortrait.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        detailInfos = new ArrayList<>();
        detailInfos.add("t1");
        detailInfos.add("t2");
        detailInfos.add("t3");
        detailInfos.add("t4");
        userDetailRecyclerView = findViewById(R.id.detail_recycler);
        userDetailRecyclerView.setHasFixedSize(true);
        userDetailLayoutManager = new LinearLayoutManager(this);
        userDetailRecyclerView.setLayoutManager(userDetailLayoutManager);
        userDetailAdapter = new UserDetailAdapter(detailInfos);
        userDetailRecyclerView.setAdapter(userDetailAdapter);
        userDetailRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        userDetailAdapter.setOnUserDetailClickListener(new UserDetailAdapter.OnUserDetailClickListener() {
            @Override
            public void onUserDetailItemClick(int position) {
                Toast.makeText(UserDeatailActivity.this, detailInfos.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.user_detail:
                Toast.makeText(this, "deatil", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
