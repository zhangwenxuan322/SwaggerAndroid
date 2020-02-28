package com.friend.swagger.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    // 账号输入框
    EditText accountText;
    // 密码输入框
    EditText passwordText;
    // 登录按钮
    Button loginBtn;
    // api
    UserApi userApi;

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
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    userApi.userLogin(account, password).enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            Map<String, Object> map = response.body();
                            if (map == null) {
                                Toast.makeText(LoginActivity.this, "接收参数失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (map.get("code").toString().equals("200")) {
                                // 登陆成功
                                String token = map.get("token").toString();
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                                intent.putExtra(ChatActivity.EXTRA_ACCOUNT, account);
                                intent.putExtra(ChatActivity.EXTRA_TOKEN, token);
                                startActivity(intent);
                                finish();
                            } else if (map.get("message").toString().equals(Constant.WRONG_PASSWORD)){
                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            } else if (map.get("message").toString().equals(Constant.USER_NOT_EXIST)) {
                                Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
