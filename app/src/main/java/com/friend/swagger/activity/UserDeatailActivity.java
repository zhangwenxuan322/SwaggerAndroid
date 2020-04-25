package com.friend.swagger.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.friend.swagger.R;
import com.friend.swagger.adapter.UserDetailAdapter;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.google.gson.internal.LinkedTreeMap;
import com.tamsiree.rxtool.RxPermissionsTool;
import com.tamsiree.rxtool.RxPhotoTool;
import com.tamsiree.rxui.view.dialog.RxDialogChooseImage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDeatailActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "indi.friend.swagger.UserDeatailActivity.EXTRA_ID";
    private RecyclerView userDetailRecyclerView;
    private UserDetailAdapter userDetailAdapter;
    private RecyclerView.LayoutManager userDetailLayoutManager;
    private List<String> detailInfos;
    private UserApi userApi;
    private ImageView userPortrait;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deatail);
        userApi = RetrofitService.createService(UserApi.class);
        userPortrait = findViewById(R.id.user_portrait);
        userName = findViewById(R.id.user_name);
        initToolbar();
        initRecyclerView();
        getUserData();
        initProtraitSelector();
    }

    /**
     * 初始化头像选择器
     */
    private void initProtraitSelector() {
        userPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(UserDeatailActivity.this);
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
                    RxPhotoTool.cropImage(UserDeatailActivity.this, data.getData());// 裁剪图片
                }
                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    dynamicPermission();
                    /* data.getExtras().get("data");*/
                    RxPhotoTool.cropImage(UserDeatailActivity.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                }
                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.swagger_logo)
                        //异常占位图(当加载异常的时候出现的图片)
                        .error(R.drawable.swagger_logo)
                        //禁止Glide硬盘缓存缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(UserDeatailActivity.this).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(userPortrait);
                uploadPortrait();
                changePortrait();
//                Toast.makeText(RegisterActivity.this, RxPhotoTool.getRealFilePath(RegisterActivity.this, RxPhotoTool.cropImageUri), Toast.LENGTH_SHORT).show();
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;
            case 1:
                getUserData();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadPortrait() {

    }

    private void changePortrait() {

    }

    /**
     * 动态权限获取
     */
    private void dynamicPermission() {
        RxPermissionsTool.with(UserDeatailActivity.this).addPermission("android.permission.CAMERA").initPermission();
        RxPermissionsTool.with(UserDeatailActivity.this).addPermission("android.permission.READ_EXTERNAL_STORAGE").initPermission();
        RxPermissionsTool.with(UserDeatailActivity.this).addPermission("android.permission.WRITE_EXTERNAL_STORAGE").initPermission();
    }

    private void getUserData() {
        userApi.getUserById(getIntent().getIntExtra(EXTRA_ID, 0)).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() != null) {
                    if (response.body().get("code").equals("200")) {
                        detailInfos.clear();
                        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
                        userName.setText(String.valueOf(map.get("userName")));
                        if (String.valueOf(map.get("userSwaggerId")).equals("null") || String.valueOf(map.get("userSwaggerId")).equals(""))
                            detailInfos.add("未设置");
                        else
                            detailInfos.add(String.valueOf(map.get("userSwaggerId")));
                        detailInfos.add(String.valueOf(map.get("userPhone")));
                        detailInfos.add(String.valueOf(map.get("userSex")));
                        if (String.valueOf(map.get("userBio")).equals("null") || String.valueOf(map.get("userBio")).equals(""))
                            detailInfos.add("未设置");
                        else
                            detailInfos.add(String.valueOf(map.get("userBio")));
                        userDetailAdapter.notifyDataSetChanged();
                        userApi.downloadPortrait(String.valueOf(map.get("userPortrait"))).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null) {
                                    InputStream inputStream = response.body().byteStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    userPortrait.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        detailInfos = new ArrayList<>();
        detailInfos.add("t1");
        detailInfos.add("t2");
        detailInfos.add("t3");
        detailInfos.add("t4");
        userDetailRecyclerView = findViewById(R.id.detail_recycler);
        userDetailRecyclerView.setHasFixedSize(true);
        userDetailLayoutManager = new LinearLayoutManager(this);
        userDetailRecyclerView.setLayoutManager(userDetailLayoutManager);
        userDetailAdapter = new UserDetailAdapter(detailInfos);
        userDetailRecyclerView.setAdapter(userDetailAdapter);
        userDetailRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        userDetailAdapter.setOnUserDetailClickListener(new UserDetailAdapter.OnUserDetailClickListener() {
            @Override
            public void onUserDetailItemClick(int position) {
                if ((position == 0 && !detailInfos.get(position).equals("未设置")) || position == 1) {
                    // Do Nothing!
                } else {
                    Intent modifyIntent = new Intent(UserDeatailActivity.this, ModifyUserDetailActivity.class);
                    if (position == 0)
                        modifyIntent.putExtra(ModifyUserDetailActivity.EXTRA_SWAGGER_ID, detailInfos.get(position));
                    else if (position == 2)
                        modifyIntent.putExtra(ModifyUserDetailActivity.EXTRA_SEX, detailInfos.get(position));
                    else if (position == 3)
                        modifyIntent.putExtra(ModifyUserDetailActivity.EXTRA_BIO, detailInfos.get(position));
                    startActivityForResult(modifyIntent, 1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}