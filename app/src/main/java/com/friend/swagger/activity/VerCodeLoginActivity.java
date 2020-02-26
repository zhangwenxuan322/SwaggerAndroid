package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.friend.swagger.R;

public class VerCodeLoginActivity extends AppCompatActivity {
    // 手机号输入框
    EditText phoneText;
    // 验证码输入框
    EditText verCodeText;
    // 验证码
    TextView verCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_code_login);
        // 隐藏menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        phoneText = findViewById(R.id.login_phone);
        verCodeText = findViewById(R.id.login_ver_code);
        verCode = findViewById(R.id.ver_code);
        initButtonActions();
    }

    /**
     * 初始化按钮点击事件
     */
    private void initButtonActions() {
        Button getVerCodeBtn = findViewById(R.id.get_ver_code);
        Button passwordLoginBtn = findViewById(R.id.password_login);
        Button verCodeLoginBtn = findViewById(R.id.login_with_ver_code);
        Button goRegister = findViewById(R.id.go_register_at_ver_code);
        getVerCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        passwordLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(LoginActivity.class);
            }
        });
        verCodeLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(RegisterActivity.class);
            }
        });
    }

    /**
     * 跳转到指定页面
     * @param cls
     */
    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(VerCodeLoginActivity.this, cls);
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
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN ) {
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
