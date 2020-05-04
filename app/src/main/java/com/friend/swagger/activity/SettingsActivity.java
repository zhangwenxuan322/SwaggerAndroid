package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.SettingsAdapter;
import com.open.hule.library.entity.AppUpdate;
import com.open.hule.library.utils.UpdateManager;
import com.tamsiree.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private RecyclerView settingsRecyclerView;
    private SettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
                if (optionList.get(position).equals("检查更新")) {
                    AppUpdate appUpdate = new AppUpdate.Builder()
                            //更新地址（必须）
                            .newVersionUrl("https://imtt.dd.qq.com/16891/8EC4E86B648D57FDF114AF5D3002C09B.apk")
                            // 版本号（非必须）
                            .newVersionCode("v1.7")
                            // 文件大小（非必须）
                            .fileSize("5.8M")
                            // 更新内容（非必填，默认“1.用户体验优化\n2.部分问题修复”）
                            .updateInfo("1.用户体验优化\n2.部分问题修复")
                            .build();
                    new UpdateManager().startUpdate(SettingsActivity.this, appUpdate);
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
