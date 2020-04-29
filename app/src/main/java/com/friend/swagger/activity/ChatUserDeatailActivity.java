package com.friend.swagger.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.rong.imkit.RongIM;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.api.FriendsApi;
import com.friend.swagger.api.RequestApi;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.FriendRequest;
import com.friend.swagger.entity.UserProfile;
import com.google.gson.internal.LinkedTreeMap;
import com.tamsiree.rxtool.view.RxToast;
import com.tamsiree.rxui.view.dialog.RxDialog;
import com.tamsiree.rxui.view.dialog.RxDialogEditSureCancel;

import java.io.InputStream;
import java.util.Map;

public class ChatUserDeatailActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "indi.friend.swagger.ChatUserDeatailActivity.EXTRA_ID";
    private int friendId;
    private UserProfile userProfile;
    private UserApi userApi;
    private RequestApi requestApi;
    private FriendsApi friendsApi;
    private ImageView portrait;
    private TextView userName;
    private TextView userBio;
    private Button requestBtn;
    private Button chatBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_deatail);
        setTitle("用户信息");
        userApi = RetrofitService.createService(UserApi.class);
        friendsApi = RetrofitService.createService(FriendsApi.class);
        requestApi = RetrofitService.createService(RequestApi.class);
        portrait = findViewById(R.id.user_portrait);
        userName = findViewById(R.id.user_name);
        userBio = findViewById(R.id.user_bio);
        requestBtn = findViewById(R.id.request_btn);
        chatBtn = findViewById(R.id.chat_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        friendId = getIntent().getIntExtra(EXTRA_ID, 0);
        friendRelationDeal();
        initToolbar();
        initUserProfile();
        btnListener();
    }

    private void btnListener() {
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(ChatUserDeatailActivity.this);
                rxDialogEditSureCancel.setTitle("请输入申请内容");
                rxDialogEditSureCancel.setSure("发送");
                rxDialogEditSureCancel.setCancel("取消");
                rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendRequest friendRequest = new FriendRequest();
                        friendRequest.setReqSubId(Constant.USER_ID);
                        friendRequest.setReqMainId(friendId);
                        friendRequest.setReqMsg(rxDialogEditSureCancel.getEditText().getText().toString());
                        friendRequest.setReqCode(0);
                        requestApi.postFriendRequest(friendRequest).enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.body() == null)
                                    RxToast.error("请求失败");
                                if (response.body().get("code").equals("200"))
                                    RxToast.success("申请成功");
                                else
                                    RxToast.warning("耐心等待对方的选择");
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                RxToast.error("请求失败");
                            }
                        });
                        rxDialogEditSureCancel.dismiss();
                    }
                });
                rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditSureCancel.dismiss();
                    }
                });
                rxDialogEditSureCancel.show();
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatBtn.getText().equals("开始聊天"))
                    RongIM.getInstance().startPrivateChat(ChatUserDeatailActivity.this, "swaggertestid" + friendId, userProfile.getUserName());
                else
                    RongIM.getInstance().startPrivateChat(ChatUserDeatailActivity.this, "swaggertestid" + friendId, "临时聊天");
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void friendRelationDeal() {
        if (Constant.USER_ID == friendId) {
            requestBtn.setVisibility(View.GONE);
            chatBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            return;
        }
        friendsApi.friendFilter(Constant.USER_ID, friendId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() == null) {
                    RxToast.error("请求失败");
                    return;
                }
                LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("friend");
                if (map == null)
                    deleteBtn.setVisibility(View.GONE);
                else {
                    requestBtn.setVisibility(View.GONE);
                    chatBtn.setText("开始聊天");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                RxToast.error("请求失败");
            }
        });
    }

    private void initUserProfile() {
        userProfile = new UserProfile();
        userApi.getUserById(friendId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() == null) {
                    RxToast.error("请求失败");
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
                            RxToast.error("请求失败");
                        }
                    });
                    userName.setText(userProfile.getUserName());
                    userBio.setText(userProfile.getUserBio());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                RxToast.error("请求失败");
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