package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.VerCodeApi;
import com.friend.swagger.common.PhoneUtil;
import com.friend.swagger.entity.VerCode;

public class VerCodeLoginActivity extends AppCompatActivity {
    // 手机号输入框
    private EditText phoneText;
    // 验证码输入框
    private EditText verCodeText;
    // 验证码
    private TextView verCodeDisplay;
    // api
    private VerCodeApi verCodeApi;
    // 获取验证码按钮
    private Button getVerCodeBtn;
    // 验证码值
    private VerCode verCodeValue;

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
        verCodeDisplay = findViewById(R.id.ver_code);
        verCodeApi = RetrofitService.createService(VerCodeApi.class);
        initButtonActions();
        initEditListeners();
    }

    /**
     * 监听输入框
     */
    private void initEditListeners() {
        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PhoneUtil.isMobileNO(phoneText.getText().toString()) && getVerCodeBtn.getText().equals("获取验证码")) {
                    getVerCodeBtn.setEnabled(true);
                    getVerCodeBtn.setTextColor(getColor(R.color.colorBlue));
                } else {
                    getVerCodeBtn.setEnabled(false);
                    getVerCodeBtn.setTextColor(getColor(R.color.gray));
                }
            }
        });
    }

    /**
     * 初始化按钮点击事件
     */
    private void initButtonActions() {
        getVerCodeBtn = findViewById(R.id.get_ver_code);
        Button passwordLoginBtn = findViewById(R.id.password_login);
        Button verCodeLoginBtn = findViewById(R.id.login_with_ver_code);
        Button goRegister = findViewById(R.id.go_register_at_ver_code);
        getVerCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountDownTimer timer = new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        getVerCodeBtn.setClickable(false);
                        getVerCodeBtn.setEnabled(false);
                        getVerCodeBtn.setTextColor(getColor(R.color.gray));
                        getVerCodeBtn.setText("已发送(" + millisUntilFinished / 1000 + ")");
                    }

                    @Override
                    public void onFinish() {
                        getVerCodeBtn.setEnabled(true);
                        getVerCodeBtn.setTextColor(getColor(R.color.colorBlue));
                        getVerCodeBtn.setText("获取验证码");
                    }
                }.start();
                String phone = phoneText.getText().toString();
                verCodeApi.getCode(phone).enqueue(new Callback<VerCode>() {
                    @Override
                    public void onResponse(Call<VerCode> call, Response<VerCode> response) {
                        verCodeDisplay.setText("验证码：" + response.body().getCodeValue());
                        verCodeValue = response.body();
                    }

                    @Override
                    public void onFailure(Call<VerCode> call, Throwable t) {
                        Toast.makeText(VerCodeLoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                String editCode = verCodeText.getText().toString();
                String editPhone = phoneText.getText().toString();
                if (editPhone.equals(verCodeValue.getCodePhone()) && editCode.equals(verCodeValue.getCodeValue())) {
                    Toast.makeText(VerCodeLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerCodeLoginActivity.this, "手机号或验证码有误", Toast.LENGTH_SHORT).show();
                }
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
     *
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
