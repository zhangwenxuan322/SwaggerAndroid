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
        optionList.add("安全与隐私");
        optionList.add("聊天背景");
        optionList.add("清除缓存");
        optionList.add("清理聊天记录");
        settingsRecyclerView = (RecyclerView) findViewById(R.id.settings_recycler);
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
                Toast.makeText(SettingsActivity.this, optionList.get(position), Toast.LENGTH_SHORT).show();
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
            case R.id.user_detail:
                Toast.makeText(this, "deatil", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
