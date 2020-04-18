package com.friend.swagger.activity;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
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
    // 昵称输入框
    EditText nameEditor;
    // 性别选择
    RadioButton male;
    RadioButton female;
    // 昵称
    String name;
    // 性别
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        nearbyApi = RetrofitService.createService(NearbyApi.class);
        intent = getIntent();
        seekBar = findViewById(R.id.seekbar);
        seekbarValueText = findViewById(R.id.seekbar_value);
        nameEditor = findViewById(R.id.name_editor);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        sex = "男";
        name = "";
        seekBar.getThumb().setColorFilter(Color.parseColor("#2c3e50"), PorterDuff.Mode.SRC_ATOP);
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#2c3e50"), PorterDuff.Mode.SRC_ATOP);
        initToolbar();
        initRecyclerView();
        setListener();
    }

    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValueText.setText(Integer.toString(progress));
                limit = new Double(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                requestNearbyUsers(lon, lat, id, name, sex);
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "男";
                requestNearbyUsers(lon, lat, id, name, sex);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "女";
                requestNearbyUsers(lon, lat, id, name, sex);
            }
        });
        nameEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
                requestNearbyUsers(lon, lat, id, name, sex);
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
                NearbyUser nearbyUser = nearbyList.get(position);
                Intent intent = new Intent(NearbyActivity.this, ChatUserDeatailActivity.class);
                intent.putExtra(ChatUserDeatailActivity.EXTRA_ID, Integer.parseInt(nearbyUser.getNearbyId()));
                startActivity(intent);
            }
        });
        requestNearbyUsers(lon, lat, id, name, sex);
    }

    private void requestNearbyUsers(Double lon, Double lat, String id, String uname, String usex) {
        nearbyApi.getNearbyUsers(lon, lat, limit, uname, usex).enqueue(new Callback<List<NearbyUser>>() {
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
                if (index != 0)
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
