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
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.common.SystemUtil;
import com.tamsiree.rxtool.RxPermissionsTool;
import com.tamsiree.rxtool.RxPhotoTool;
import com.tamsiree.rxui.view.dialog.RxDialogChooseImage;

import java.io.File;
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
    private EditText userPhone;
    // 密码
    private EditText userPwd;
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
        userPhone = findViewById(R.id.register_phone);
        userPwd = findViewById(R.id.register_password);
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
//        if (male.isChecked()) {
//            Toast.makeText(RegisterActivity.this, "男", Toast.LENGTH_SHORT).show();
//        } else if (female.isChecked()) {
//            Toast.makeText(RegisterActivity.this, "女", Toast.LENGTH_SHORT).show();
//        }
//        Drawable.ConstantState drawableCs = RegisterActivity.this.getResources().getDrawable(R.drawable.circle_elves_ball).getConstantState();
//        if (portrait.getDrawable().getConstantState().equals(drawableCs)) {
//            Toast.makeText(RegisterActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
//            return;
//        }
        String fileNameByTimeStamp = "androidtest1.png";
//        File file = new File(RxPhotoTool.getRealFilePath(RegisterActivity.this,
//                RxPhotoTool.cropImageUri));
        File file = SystemUtil.drawableToFile(RegisterActivity.this, R.drawable.swagger_logo, "swaggerlogo");
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileNameByTimeStamp, requestFile);
        userApi.uploadPortrait(body, RequestBody.create(null, "androidtest1.png")).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
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
