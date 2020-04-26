package com.friend.swagger.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.common.PhoneUtil;
import com.friend.swagger.common.SystemUtil;
import com.friend.swagger.entity.UserProfile;
import com.friend.swagger.viewmodel.CacheUserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStream;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_ACCOUNT =
            "indi.friend.swagger.ChatActivity.EXTRA_ACCOUNT";
    public static final String EXTRA_TOKEN =
            "indi.friend.swagger.ChatActivity.EXTRA_TOKEN";
    public static final String EXTRA_LON =
            "indi.friend.swagger.ChatActivity.EXTRA_LON";
    public static final String EXTRA_LAT =
            "indi.friend.swagger.ChatActivity.EXTRA_LAT";
    private FloatingActionButton startChatBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Intent intent;
    // api
    private UserApi userApi;
    // 用户信息
    private UserProfile userProfile;
    // 聊天用户信息
    private UserProfile chatUserProfile;
    // 侧边栏头部
    private View headView;
    private ImageView headImage;
    private TextView headText;
    // ViewModel
    private CacheUserViewModel cacheUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        startChatBtn = findViewById(R.id.startChat);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        userApi = RetrofitService.createService(UserApi.class);
        chatUserProfile = new UserProfile();
        intent = getIntent();
        initHeaderView();
        initView();
        RongIM.getInstance().setConversationClickListener(new MyConversationClickListener());
        dataInsert();
        cacheUserViewModel = ViewModelProviders.of(this).get(CacheUserViewModel.class);
        startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(ChatActivity.this, AllAddActivity.class);
                startActivity(addIntent);
            }
        });
    }

    /**
     * 数据注入
     */
    private void dataInsert() {
        String account = intent.getStringExtra(EXTRA_ACCOUNT);
        if (PhoneUtil.isMobileNO(account)) {
            // 根据手机号查询
            userApi.getUserByPhone(account).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    getUserInfo(response);
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, "用户信息请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 根据swaggerid查询
            userApi.getUserBySwaggerId(account).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    getUserInfo(response);
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, "用户信息请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 用户信息赋值
     *
     * @param response
     */
    private void getUserInfo(Response<Map<String, Object>> response) {
        if (response.body() == null) {
            Toast.makeText(ChatActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (response.body().get("code").equals("200")) {
            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
            userProfile = new UserProfile();
            userProfile.setUserId(new Double(map.get("userId").toString()).intValue());
            Constant.USER_ID = userProfile.getUserId();
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
            headText.setText(userProfile.getUserName());
            setUserPortrait(userProfile.getUserPortrait());
        } else {
            Toast.makeText(ChatActivity.this, "用户信息请求失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置用户头像
     */
    private void setUserPortrait(String fileName) {
        userApi.downloadPortrait(fileName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    headImage.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 初始化侧边栏头部视图并监听
     */
    private void initHeaderView() {
        headView = navigationView.inflateHeaderView(R.layout.header);
        headImage = headView.findViewById(R.id.user_portrait);
        headText = headView.findViewById(R.id.user_name);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetailListener();
            }
        });
        headText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetailListener();
            }
        });
    }

    /**
     * 头部视图点击事件
     */
    private void userDetailListener() {
        Intent userDetailIntent = new Intent(ChatActivity.this, UserDeatailActivity.class);
        userDetailIntent.putExtra(UserDeatailActivity.EXTRA_ID, userProfile.getUserId());
        startActivityForResult(userDetailIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 2:
                dataInsert();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化主视图
     */
    private void initView() {
        String userToken = intent.getStringExtra(EXTRA_TOKEN);
        // 连接 IM
        RongIM.connect(userToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            }

            @Override
            public void onSuccess(String s) {
//                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                    @Override
//                    public UserInfo getUserInfo(String userId) {
//                        int id = SystemUtil.getNum(userId);
//                        userApi.getUserById(id).enqueue(new Callback<Map<String, Object>>() {
//                            @Override
//                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
//                                if (response.body() != null) {
//                                    if (response.body().get("code").equals("200")) {
//                                        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
//                                        chatUserProfile.setUserName(String.valueOf(map.get("userName")));
//                                        chatUserProfile.setUserPortrait(String.valueOf(map.get("userPortrait")));
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
//
//                            }
//                        });
//                        UserInfo userInfo = new UserInfo(userId, chatUserProfile.getUserName(),
//                                Uri.parse(Constant.remoteUrl +
//                                        "user/portrait/" +
//                                        chatUserProfile.getUserPortrait()));
//                        RongIM.getInstance().refreshUserInfoCache(userInfo);
//                        return userInfo;
//                    }
//                }, true);
                // 清除缓存
//                RongUserInfoManager.getInstance().uninit();
                initFragment();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 融云fragment
     */
    private void initFragment() {
        // 集成 ConversationListFragment
//        ConversationListFragment conversationListFragment = new ConversationListFragment();
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationListFragment conversationListFragment = (ConversationListFragment) fragmentManage.findFragmentById(R.id.conversationlist);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();
        conversationListFragment.setUri(uri);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.main_layout, conversationListFragment);
//        transaction.commit();
    }

    /**
     * 监听侧边栏点击
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nearby:
                Intent nearbyIntent = new Intent(ChatActivity.this, NearbyActivity.class);
                nearbyIntent.putExtra(NearbyActivity.EXTRA_LON, intent.getDoubleExtra(EXTRA_LON, 0.0));
                nearbyIntent.putExtra(NearbyActivity.EXTRA_LAT, intent.getDoubleExtra(EXTRA_LAT, 0.0));
                nearbyIntent.putExtra(NearbyActivity.EXTRA_ID, userProfile.getUserId());
                startActivity(nearbyIntent);
                break;
            case R.id.group_chat:
                Intent createGroupIntent = new Intent(ChatActivity.this, CreateGroupActivity.class);
                startActivity(createGroupIntent);
                break;
            case R.id.contacts:
                Intent contactsIntent = new Intent(ChatActivity.this, ContactsActivity.class);
                startActivity(contactsIntent);
                break;
            case R.id.settings:
                Intent settingsIntent = new Intent(ChatActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.logout:
                userApi.logout(userProfile.getUserLoginInfoId()).enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.body() != null) {
                            RongIMClient.getInstance().disconnect();
                            Intent loginIntent = new Intent(ChatActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            Toast.makeText(ChatActivity.this, "登出成功", Toast.LENGTH_SHORT).show();
                            cacheUserViewModel.deleteAllCaches();
                            finish();
                        } else {
                            Toast.makeText(ChatActivity.this, "登出失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        return true;
    }
}
