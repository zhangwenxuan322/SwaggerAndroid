package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.SettingsAdapter;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UpdateApi;
import com.friend.swagger.common.Constant;
import com.open.hule.library.entity.AppUpdate;
import com.open.hule.library.utils.UpdateManager;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private RecyclerView settingsRecyclerView;
    private SettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingsLayoutManager;
    private UpdateApi updateApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        updateApi = RetrofitService.createService(UpdateApi.class);
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<String> optionList = new ArrayList<>();
        optionList.add("检查更新");
        settingsRecyclerView = findViewById(R.id.settings_recycler);
        settingsRecyclerView.setHasFixedSize(true);
        settingsLayoutManager = new LinearLayoutManager(this);
        settingsRecyclerView.setLayoutManager(settingsLayoutManager);
        settingsAdapter = new SettingsAdapter(optionList);
        settingsRecyclerView.setAdapter(settingsAdapter);
        settingsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        settingsAdapter.setSettingsItemClickListener(new SettingsAdapter.OnSettingsItemClickListener() {
            @Override
            public void onSettingsItemClick(int position) {
                int curVersion = RxAppTool.getAppVersionCode(SettingsActivity.this);
                if (optionList.get(position).equals("检查更新")) {
                    updateApi.getLatestApkInfo().enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            Map<String, Object> map = response.body();
                            if (map == null) {
                                RxToast.error("请求错误");
                                return;
                            }
                            int apkVersion = Integer.valueOf(map.get("apkCode").toString());
                            String apkFileName = map.get("apkName").toString();
                            String apkDesc = map.get("apkDesc").toString();
                            if (apkVersion > curVersion) {
                                AppUpdate appUpdate = new AppUpdate.Builder()
                                        //更新地址（必须）
                                        .newVersionUrl(Constant.remoteUrl + "apk/" + apkFileName)
                                        // 版本号（非必须）
                                        .newVersionCode("v" + apkVersion)
                                        // 更新内容（非必填，默认“1.用户体验优化\n2.部分问题修复”）
                                        .updateInfo(apkDesc)
                                        .build();
                                new UpdateManager().startUpdate(SettingsActivity.this, appUpdate);
                            } else {
                                RxToast.info("已是最新版");
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            RxToast.error("请求错误");
                        }
                    });
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
