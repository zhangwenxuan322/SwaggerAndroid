package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.PhoneUtil;
import com.friend.swagger.common.SystemUtil;
import com.friend.swagger.entity.UserProfile;
import com.tamsiree.rxtool.RxPermissionsTool;
import com.tamsiree.rxtool.RxPhotoTool;
import com.tamsiree.rxui.view.dialog.RxDialogChooseImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    // 头像
    private ImageView portrait;
    // 注册按钮
    private Button registerBtn;
    // 昵称
    private EditText userNameText;
    // 性别
    private RadioButton male;
    private RadioButton female;
    // 手机号
    private EditText userPhoneText;
    // 密码
    private EditText userPwdText;
    // api
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 隐藏menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        userNameText = findViewById(R.id.register_username);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        userPhoneText = findViewById(R.id.register_phone);
        userPwdText = findViewById(R.id.register_password);
        userApi = RetrofitService.createService(UserApi.class);
        initProtraitSelector();
        initButtonActions();
    }

    /**
     * 初始化按钮点击事件
     */
    private void initButtonActions() {
        registerBtn = findViewById(R.id.register);
        Button goLoginBtn = findViewById(R.id.go_login);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validRegister();
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
     * 注册检验
     */
    private void validRegister() {
        // 空验证
        if (userNameText.getText().toString().isEmpty() || userPhoneText.getText().toString().isEmpty() || userPwdText.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "不能有空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!male.isChecked() && !female.isChecked()) {
            Toast.makeText(RegisterActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        }
        // 值获取
        String userName = userNameText.getText().toString();
        String userPhone = userPhoneText.getText().toString();
        String userPwd = userPwdText.getText().toString();
        String userSex = "";
        if (male.isChecked()) userSex = "男";
        else if (female.isChecked()) userSex = "女";
        String fileName = userPhone + String.valueOf(new Date().getTime()) + ".png";
        // 手机号验证
        if (!PhoneUtil.isMobileNO(userPhone)) {
            Toast.makeText(RegisterActivity.this, "手机号格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        // 用户注册
        UserProfile userProfile = new UserProfile(userName, userSex, userPwd, userPhone,
                null, fileName, null, null, null,
                0, null);
        userApi.userRegister(userProfile).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body().get("code").toString().equals("415")) {
                    Toast.makeText(RegisterActivity.this, "用户已存在！", Toast.LENGTH_SHORT).show();
                }
                if (response.body().get("code").toString().equals("200")) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    // 上传头像
                    uploadPortrait(fileName);
                    // 跳转至登录页
                    toActivity(LoginActivity.class);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 上传头像
     *
     * @param fileName
     */
    private void uploadPortrait(String fileName) {
        File file;
        Drawable.ConstantState drawableCs = RegisterActivity.this.getResources().getDrawable(R.drawable.swagger_logo).getConstantState();
        if (portrait.getDrawable().getConstantState().equals(drawableCs)) {
            // 未选择图片上传默认头像
            file = SystemUtil.drawableToFile(RegisterActivity.this, R.drawable.swagger_logo, "swaggerlogo");
        } else {
            // 选择图片后上传选中头像
            file = new File(RxPhotoTool.getRealFilePath(RegisterActivity.this,
                    RxPhotoTool.cropImageUri));
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);
        userApi.uploadPortrait(body, RequestBody.create(null, fileName))
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.body().get("code").toString().equals("404")) {
                            Toast.makeText(RegisterActivity.this, response.body().get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 跳转到指定页面
     *
     * @param cls
     */
    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(RegisterActivity.this, cls);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化头像选择器
     */
    private void initProtraitSelector() {
        portrait = findViewById(R.id.register_portrait);
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(RegisterActivity.this);
                dialogChooseImage.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    dynamicPermission();
                    RxPhotoTool.cropImage(RegisterActivity.this, data.getData());// 裁剪图片
                }
                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    dynamicPermission();
                    /* data.getExtras().get("data");*/
                    RxPhotoTool.cropImage(RegisterActivity.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                }
                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.swagger_logo)
                        //异常占位图(当加载异常的时候出现的图片)
                        .error(R.drawable.swagger_logo)
                        //禁止Glide硬盘缓存缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(RegisterActivity.this).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(portrait);
//                Toast.makeText(RegisterActivity.this, RxPhotoTool.getRealFilePath(RegisterActivity.this, RxPhotoTool.cropImageUri), Toast.LENGTH_SHORT).show();
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 动态权限获取
     */
    private void dynamicPermission() {
        RxPermissionsTool.with(RegisterActivity.this).addPermission("android.permission.CAMERA").initPermission();
        RxPermissionsTool.with(RegisterActivity.this).addPermission("android.permission.READ_EXTERNAL_STORAGE").initPermission();
        RxPermissionsTool.with(RegisterActivity.this).addPermission("android.permission.WRITE_EXTERNAL_STORAGE").initPermission();
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
