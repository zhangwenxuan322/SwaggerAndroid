package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.UserProfile;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStream;
import java.util.Map;

public class ChatUserDeatailActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "indi.friend.swagger.ChatUserDeatailActivity.EXTRA_ID";
    private UserProfile userProfile;
    private UserApi userApi;
    private ImageView portrait;
    private TextView userName;
    private TextView userBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_deatail);
        setTitle("用户信息");
        userApi = RetrofitService.createService(UserApi.class);
        portrait = findViewById(R.id.user_portrait);
        userName = findViewById(R.id.user_name);
        userBio = findViewById(R.id.user_bio);
        initToolbar();
        initUserProfile();
    }

    private void initUserProfile() {
        userProfile = new UserProfile();
        int userId = getIntent().getIntExtra(EXTRA_ID, 0);
        userApi.getUserById(userId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() == null) {
                    Toast.makeText(ChatUserDeatailActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().get("code").equals("200")) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
                    userProfile.setUserId(new Double(map.get("userId").toString()).intValue());
                    userProfile.setUserName(String.valueOf(map.get("userName")));
                    userProfile.setUserSex(String.valueOf(map.get("userSex")));
                    userProfile.setUserPhone(String.valueOf(map.get("userPhone")));
                    if (map.get("userSwaggerId") == null)
                        userProfile.setUserSwaggerId("");
                    else
                        userProfile.setUserSwaggerId(String.valueOf(map.get("userSwaggerId")));
                    userProfile.setUserPortrait(String.valueOf(map.get("userPortrait")));
                    if (map.get("userBio") == null)
                        userProfile.setUserBio("");
                    else
                        userProfile.setUserBio(String.valueOf(map.get("userBio")));
                    userProfile.setUserLoginInfoId(new Double(String.valueOf(map.get("userLoginInfoId"))).intValue());
                    userApi.downloadPortrait(userProfile.getUserPortrait()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.body() != null) {
                                InputStream inputStream = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                portrait.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    userName.setText(userProfile.getUserName());
                    userBio.setText(userProfile.getUserBio());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

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
        }
        return super.onOptionsItemSelected(item);
    }
}