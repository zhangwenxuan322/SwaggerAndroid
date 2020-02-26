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

public class ForgetPasswordActivity extends AppCompatActivity {
    // 手机号输入框
    EditText phoneText;
    // 验证码输入框
    EditText verCodeText;
    // 新的密码输入框
    EditText resetPwdText;
    // 确认密码输入框
    EditText confirmPwdText;
    // 验证码
    TextView verCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        // 隐藏menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        phoneText = findViewById(R.id.forget_phone);
        verCodeText = findViewById(R.id.forget_ver_code);
        resetPwdText = findViewById(R.id.reset_password);
        confirmPwdText = findViewById(R.id.confirm_password);
        verCode = findViewById(R.id.ver_code);
        initButtonActions();
    }

    /**
     * 初始化按钮点击事件
     */
    private void initButtonActions() {
        Button getVerCodeBtn = findViewById(R.id.get_ver_code);
        Button confirmBtn = findViewById(R.id.confirm);
        Button goLoginBtn = findViewById(R.id.go_login);
        getVerCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        goLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(LoginActivity.class);
            }
        });
    }

    /**
     * 跳转到指定页面
     * @param cls
     */
    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(ForgetPasswordActivity.this, cls);
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
