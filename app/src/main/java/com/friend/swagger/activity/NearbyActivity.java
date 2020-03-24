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
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.NearbyAdapter;
import com.friend.swagger.api.NearbyApi;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.entity.NearbyUser;

import java.util.List;

public class NearbyActivity extends AppCompatActivity {
    public static final String EXTRA_LON =
            "indi.friend.swagger.NearbyActivity.EXTRA_LON";
    public static final String EXTRA_LAT =
            "indi.friend.swagger.NearbyActivity.EXTRA_LAT";
    // api
    private NearbyApi nearbyApi;
    // 范围限制
    private Double limit;
    // intent
    private Intent intent;
    // recycler
    private RecyclerView nearbyRecyclerView;
    private NearbyAdapter nearbyAdapter;
    private RecyclerView.LayoutManager nearbyLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        nearbyApi = RetrofitService.createService(NearbyApi.class);
        intent = getIntent();
        limit = 10000.0;
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        Double lon = intent.getDoubleExtra(EXTRA_LON, 0.0);
        Double lat = intent.getDoubleExtra(EXTRA_LAT, 0.0);
        nearbyApi.getNearbyUsers(lon, lat, limit).enqueue(new Callback<List<NearbyUser>>() {
            @Override
            public void onResponse(Call<List<NearbyUser>> call, Response<List<NearbyUser>> response) {
                List<NearbyUser> nearbyList = response.body();
                if (nearbyList == null)
                    Toast.makeText(NearbyActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
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
            case R.id.user_detail:
                Toast.makeText(this, "deatil", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
