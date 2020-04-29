package com.friend.swagger.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.common.LocationUtil;
import com.friend.swagger.common.SystemUtil;
import com.friend.swagger.entity.CacheUser;
import com.friend.swagger.viewmodel.CacheUserViewModel;
import com.tamsiree.rxtool.RxPermissionsTool;
import com.tamsiree.rxtool.view.RxToast;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    // 账号输入框
    private EditText accountText;
    // 密码输入框
    private EditText passwordText;
    // 登录按钮
    private Button loginBtn;
    // api
    private UserApi userApi;
    // 经纬度
    private Double lon;
    private Double lat;
    // ViewModel
    private CacheUserViewModel cacheUserViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 隐藏menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        accountText = findViewById(R.id.login_username);
        passwordText = findViewById(R.id.login_password);
        userApi = RetrofitService.createService(UserApi.class);
        // 按钮点击事件初始化
        initButtonAction();
        RxPermissionsTool.with(LoginActivity.this).addPermission("android.permission.ACCESS_FINE_LOCATION").initPermission();
        cacheUserViewModel = ViewModelProviders.of(this).get(CacheUserViewModel.class);
        cacheUserViewModel.getCacheUser().observe(this, new Observer<List<CacheUser>>() {
            @Override
            public void onChanged(List<CacheUser> cacheUsers) {
                if (cacheUsers.size() > 0) {
                    String account = cacheUsers.get(0).getAccount();
                    String password = cacheUsers.get(0).getPassword();
                    accountText.setText(account);
                    passwordText.setText(password);
                    loginAction(account, password);
                } else {
//                    Toast.makeText(LoginActivity.this, "没数据", Toast.LENGTH_SHORT).show();
                    // 没有用户缓存数据就正常登陆
                }
            }
        });
    }

    /**
     * 按钮点击事件初始化
     */
    private void initButtonAction() {
        loginBtn = findViewById(R.id.login);
        Button forgetPwdBtn = findViewById(R.id.forget_password);
        Button verCodeLoginBtn = findViewById(R.id.ver_code_login);
        Button goRegisterBtn = findViewById(R.id.go_register);
        // 登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountText.getText().toString();
                String password = passwordText.getText().toString();
                if (account.isEmpty() || password.isEmpty()) {
                    RxToast.warning("账号或密码不能为空");
                } else {
                    loginAction(account, password);
                    cacheUserViewModel.insert(new CacheUser(account, password));
                }
            }
        });
        // 忘记密码
        forgetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(ForgetPasswordActivity.class);
            }
        });
        // 手机验证码登录
        verCodeLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(VerCodeLoginActivity.class);
            }
        });
        // 前往注册
        goRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(RegisterActivity.class);
            }
        });
    }

    private void loginAction(String account, String password) {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 255);
        } else {
            LocationUtil.initLocation(LoginActivity.this);
            lon = LocationUtil.longitude;
            lat = LocationUtil.latitude;
        }
        String device = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        userApi.userLogin(account, password, SystemUtil.getIpAddressString(), lon, lat, device).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Map<String, Object> map = response.body();
                if (map == null) {
                    return;
                }
                if (map.get("code").toString().equals("200")) {
                    // 登陆成功
                    String token = map.get("token").toString();
                    RxToast.success("登录成功");
                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                    intent.putExtra(ChatActivity.EXTRA_ACCOUNT, account);
                    intent.putExtra(ChatActivity.EXTRA_TOKEN, token);
                    intent.putExtra(ChatActivity.EXTRA_LON, lon);
                    intent.putExtra(ChatActivity.EXTRA_LAT, lat);
                    startActivity(intent);
                    finish();
                } else if (map.get("message").toString().equals(Constant.WRONG_PASSWORD)) {
                    RxToast.error("密码错误");
                } else if (map.get("message").toString().equals(Constant.USER_NOT_EXIST)) {
                    RxToast.error("用户不存在");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                RxToast.error("请求失败");
            }
        });
    }

    /**
     * 跳转到指定页面
     *
     * @param cls
     */
    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(LoginActivity.this, cls);
        startActivity(intent);
        finish();
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
