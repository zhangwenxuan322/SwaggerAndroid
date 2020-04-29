package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.AddAdapter;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.PhoneUtil;
import com.friend.swagger.entity.UserProfile;
import com.google.gson.internal.LinkedTreeMap;
import com.tamsiree.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllAddActivity extends AppCompatActivity {
    private EditText editText;
    private Button searchButton;
    private UserApi userApi;
    private List<UserProfile> list;
    // recycler
    private RecyclerView addRecyclerView;
    private AddAdapter addAdapter;
    private RecyclerView.LayoutManager addLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_add);
        editText = findViewById(R.id.add_edit_text);
        searchButton = findViewById(R.id.search_btn);
        userApi = RetrofitService.createService(UserApi.class);
        initToolbar();
        initRecyclerView();
        onClicklistener();
    }

    private void onClicklistener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhoneUtil.isMobileNO(editText.getText().toString())) {
                    userApi.getUserByPhone(editText.getText().toString()).enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            if (response.body() == null) {
                                RxToast.error("请求异常");
                                return;
                            }
                            if (response.body().get("code").equals("200")) {
                                list.clear();
                                LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
                                UserProfile userProfile = new UserProfile();
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
                                list.add(userProfile);
                            }
                            addAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            RxToast.error("请求失败");
                        }
                    });
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        addRecyclerView = findViewById(R.id.add_recycler);
        addRecyclerView.setHasFixedSize(true);
        addLayoutManager = new LinearLayoutManager(AllAddActivity.this);
        addRecyclerView.setLayoutManager(addLayoutManager);
        addAdapter = new AddAdapter(list);
        addRecyclerView.setAdapter(addAdapter);
        addRecyclerView.addItemDecoration(new DividerItemDecoration(AllAddActivity.this,
                DividerItemDecoration.VERTICAL));
        addAdapter.setAddItemClickListener(new AddAdapter.OnAddItemClickListener() {
            @Override
            public void onAddItemClick(int position) {
                Intent intent = new Intent(AllAddActivity.this, ChatUserDeatailActivity.class);
                intent.putExtra(ChatUserDeatailActivity.EXTRA_ID, list.get(position).getUserId());
                startActivity(intent);
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

    /**
     * 点击非编辑区域收起键盘
     * 获取点击事件
     */
    @CallSuper
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定当前是否需要隐藏
     */
    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
            //return !(ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
