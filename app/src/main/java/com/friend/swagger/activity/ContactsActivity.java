package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.ExpandAdapter;
import com.friend.swagger.api.FriendsApi;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.FriendsManager;
import com.friend.swagger.entity.GroupFriends;
import com.friend.swagger.entity.UserProfile;
import com.google.gson.internal.LinkedTreeMap;
import com.tamsiree.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {
    // api
    private FriendsApi friendsApi;
    private UserApi userApi;
    private ExpandableListView expandableListView;
    private List<FriendsManager> friendList;
    private List<GroupFriends> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getFriendList();
        expandableListView = findViewById(R.id.expand_list);
    }


    private void getFriendList() {
        friendList = new ArrayList<>();
        friendsApi = RetrofitService.createService(FriendsApi.class);
        friendsApi.getFirendList(Constant.USER_ID).enqueue(new Callback<List<FriendsManager>>() {
            @Override
            public void onResponse(Call<List<FriendsManager>> call, Response<List<FriendsManager>> response) {
                if (response.body() == null) {
                    RxToast.error("请求失败");
                    return;
                }
                friendList.addAll(response.body());
                convertToGroup(friendList);
                expandableListView.setAdapter(new ExpandAdapter(R.layout.friend_group_item, R.layout.friend_child_item
                        , LayoutInflater.from(getBaseContext()), groupList));
            }

            @Override
            public void onFailure(Call<List<FriendsManager>> call, Throwable t) {
                RxToast.error("请求失败");
            }
        });
    }

    private void convertToGroup(List<FriendsManager> list) {
        groupList = new ArrayList<>();
        GroupFriends all = new GroupFriends();
        all.setGroupName("全部好友");
        groupList.add(all);
        for (FriendsManager friendsManager : list) {
            // 未被分组的好友
            if (friendsManager.getGroupName() == null || friendsManager.getGroupName().isEmpty())
                all.getFriends().add(getUserInfo(friendsManager.getFriendUserId()));
            else {
                all.getFriends().add(getUserInfo(friendsManager.getFriendUserId()));
                // 被分组过的好友
                String groupName = friendsManager.getGroupName();
                Boolean deal = true;
                GroupFriends curGf = new GroupFriends();
                for (GroupFriends gf : groupList) {
                    if (gf.getGroupName().equals(groupName)) {
                        deal = false;
                        curGf = gf;
                        break;
                    }
                }
                if (deal) {
                    curGf.setGroupName(groupName);
                    groupList.add(curGf);
                }
                curGf.getFriends().add(getUserInfo(friendsManager.getFriendUserId()));
            }
        }
    }

    private UserProfile getUserInfo(int userId) {
        UserProfile userProfile = new UserProfile();
        userApi = RetrofitService.createService(UserApi.class);
        userApi.getUserById(userId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() == null) {
                    RxToast.error("请求失败");
                    return;
                }
                if (response.body().get("code").equals("200")) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
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
                } else {
                    RxToast.error("请求失败");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                RxToast.error("请求失败");
            }
        });
        return userProfile;
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
