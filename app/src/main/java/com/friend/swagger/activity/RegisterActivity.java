package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.friend.swagger.R;
import com.tamsiree.rxtool.RxPermissionsTool;
import com.tamsiree.rxtool.RxPhotoTool;
import com.tamsiree.rxui.view.dialog.RxDialogChooseImage;

public class RegisterActivity extends AppCompatActivity {
    ImageView portrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 隐藏menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initProtraitSelector();
        initButtonActions();
    }

    /**
     * 初始化按钮点击事件
     */
    private void initButtonActions() {
        Button registerBtn = findViewById(R.id.register);
        Button goLoginBtn = findViewById(R.id.go_login);
        registerBtn.setOnClickListener(new View.OnClickListener() {
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
                        .placeholder(R.drawable.circle_elves_ball)
                        //异常占位图(当加载异常的时候出现的图片)
                        .error(R.drawable.circle_elves_ball)
                        //禁止Glide硬盘缓存缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(RegisterActivity.this).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(portrait);
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
