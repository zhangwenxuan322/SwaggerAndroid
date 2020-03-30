package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.NearbyAdapter;
import com.friend.swagger.api.NearbyApi;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.entity.NearbyUser;

import java.util.ArrayList;
import java.util.List;

public class NearbyActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "indi.friend.swagger.NearbyActivity.EXTRA_ID";
    public static final String EXTRA_LON =
            "indi.friend.swagger.NearbyActivity.EXTRA_LON";
    public static final String EXTRA_LAT =
            "indi.friend.swagger.NearbyActivity.EXTRA_LAT";
    // api
    private NearbyApi nearbyApi;
    // 范围限制
    private Double limit = 10.0;
    // intent
    private Intent intent;
    // recycler
    private RecyclerView nearbyRecyclerView;
    private NearbyAdapter nearbyAdapter;
    private RecyclerView.LayoutManager nearbyLayoutManager;
    // Seekbar
    private SeekBar seekBar;
    private TextView seekbarValueText;
    // 经纬度
    Double lon;
    Double lat;
    // 用户id
    String id;
    // 附近的人列表
    List<NearbyUser> nearbyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        nearbyApi = RetrofitService.createService(NearbyApi.class);
        intent = getIntent();
        seekBar = findViewById(R.id.seekbar);
        seekbarValueText = findViewById(R.id.seekbar_value);
        seekBar.getThumb().setColorFilter(Color.parseColor("#2c3e50"), PorterDuff.Mode.SRC_ATOP);
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#2c3e50"), PorterDuff.Mode.SRC_ATOP);
        initToolbar();
        initRecyclerView();
        setSeekbarListener();
    }

    private void setSeekbarListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValueText.setText(Integer.toString(progress));
                limit = new Double(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                requestNearbyUsers(lon, lat, id);
            }
        });
    }

    private void initRecyclerView() {
        lon = intent.getDoubleExtra(EXTRA_LON, 0.0);
        lat = intent.getDoubleExtra(EXTRA_LAT, 0.0);
        id = String.valueOf(intent.getIntExtra(EXTRA_ID, 0));
        nearbyList = new ArrayList<>();
        nearbyRecyclerView = findViewById(R.id.nearby_recycler);
        nearbyRecyclerView.setHasFixedSize(true);
        nearbyLayoutManager = new LinearLayoutManager(NearbyActivity.this);
        nearbyRecyclerView.setLayoutManager(nearbyLayoutManager);
        nearbyAdapter = new NearbyAdapter(nearbyList);
        nearbyRecyclerView.setAdapter(nearbyAdapter);
        nearbyRecyclerView.addItemDecoration(new DividerItemDecoration(NearbyActivity.this,
                DividerItemDecoration.VERTICAL));
        nearbyAdapter.setNearbyItemClickListener(new NearbyAdapter.OnNearbyItemClickListener() {
            @Override
            public void onNearbyItemClick(int position) {
                Toast.makeText(NearbyActivity.this, nearbyList.get(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        requestNearbyUsers(lon, lat, id);
    }

    private void requestNearbyUsers(Double lon, Double lat, String id) {
        nearbyApi.getNearbyUsers(lon, lat, limit).enqueue(new Callback<List<NearbyUser>>() {
            @Override
            public void onResponse(Call<List<NearbyUser>> call, Response<List<NearbyUser>> response) {
                nearbyList.clear();
                List<NearbyUser> list = response.body();
                if (list == null) {
                    Toast.makeText(NearbyActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 从列表中删除正在使用的用户
                int index = 0;
                for (NearbyUser nearbyUser : list) {
                    if (nearbyUser.getNearbyId().equals(id)) {
                        index = list.indexOf(nearbyUser);
                        break;
                    }
                }
                list.remove(list.get(index));
                nearbyList.addAll(list);
                nearbyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<NearbyUser>> call, Throwable t) {
                Toast.makeText(NearbyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
