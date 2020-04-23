package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.UserProfile;

import java.util.Map;

public class ModifyUserDetailActivity extends AppCompatActivity {
    public static final String EXTRA_SWAGGER_ID =
            "indi.friend.swagger.ModifyUserDetailActivity.EXTRA_SWAGGER_ID";
    public static final String EXTRA_SEX =
            "indi.friend.swagger.ModifyUserDetailActivity.EXTRA_SEX";
    public static final String EXTRA_BIO =
            "indi.friend.swagger.ModifyUserDetailActivity.EXTRA_BIO";
    private String swaggerIdTitle = "SwaggerId";
    private String sexTitle = "性别";
    private String bioTitle = "个性签名";
    private EditText editText;
    private RadioGroup sexGroup;
    private RadioButton male;
    private RadioButton female;
    private String title = "";
    private String textHint = "";
    private UserProfile userProfile;
    // api
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeify_user_detail);
        sexGroup = findViewById(R.id.sex_group);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        userProfile = new UserProfile();
        userProfile.setUserId(Constant.USER_ID);
        userApi = RetrofitService.createService(UserApi.class);
        initToolbar();
        initTitleAndHint();
    }

    private void initTitleAndHint() {
        editText = findViewById(R.id.edit_text);
        Intent intent = getIntent();
        if (intent.getStringExtra(EXTRA_SWAGGER_ID) != null) {
            title = swaggerIdTitle;
            textHint = "设置您的SwaggerId，此Id只可设置一次，由字母数字和字符组成，要求唯一";
            setTitle(title);
            editText.setHint(textHint);
            sexGroup.setVisibility(View.GONE);
        } else if (intent.getStringExtra(EXTRA_SEX) != null) {
            title = sexTitle;
            setTitle(title);
            editText.setVisibility(View.GONE);
            if (intent.getStringExtra(EXTRA_SEX).equals("男"))
                male.setChecked(true);
            else
                female.setChecked(true);
        } else if (intent.getStringExtra(EXTRA_BIO) != null) {
            title = bioTitle;
            if (intent.getStringExtra(EXTRA_BIO).equals("未设置")) {
                textHint = "记录一下心情吧！";
                editText.setHint(textHint);
            } else editText.setText(intent.getStringExtra(EXTRA_BIO));
            sexGroup.setVisibility(View.GONE);
        }
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
            case R.id.save_modify:
                saveAction();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAction() {
        if (title.equals(swaggerIdTitle) && editText.getText() != null && !"".equals(editText.getText().toString())) {
            // 设置SwaggerId
            String swaggerId = editText.getText().toString();
            userProfile.setUserSwaggerId(swaggerId);
            userApi.changeSwaggerId(userProfile).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.body() == null)
                        Toast.makeText(ModifyUserDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ModifyUserDetailActivity.this, "SwaggerId设置成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(ModifyUserDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (title.equals(sexTitle)) {
            // 修改性别
            String sex;
            if (male.isChecked()) sex = "男";
            else sex = "女";
            userProfile.setUserSex(sex);
            userApi.changeSex(userProfile).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.body() == null)
                        Toast.makeText(ModifyUserDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ModifyUserDetailActivity.this, "性别保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(ModifyUserDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (title.equals(bioTitle) && editText.getText() != null && !"".equals(editText.getText().toString())) {
            // 修改个性签名
            String bio = editText.getText().toString();
            userProfile.setUserBio(bio);
            userApi.changeBio(userProfile).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.body() == null)
                        Toast.makeText(ModifyUserDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ModifyUserDetailActivity.this, "个性签名保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(ModifyUserDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_user_detail, menu);
        return true;
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
