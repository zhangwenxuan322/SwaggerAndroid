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
import android.view.View;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.adapter.RequestAdapter;
import com.friend.swagger.api.RequestApi;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.FriendRequest;
import com.tamsiree.rxtool.view.RxToast;
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendRequestActivity extends AppCompatActivity {
    private List<FriendRequest> list;
    // recycler
    private RecyclerView reqRecyclerView;
    private RequestAdapter reqAdapter;
    private RecyclerView.LayoutManager reqLayoutManager;
    // api
    RequestApi requestApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        requestApi = RetrofitService.createService(RequestApi.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        initRecyclerView();

    }

    private void requestList() {
        requestApi.getRequestList(Constant.USER_ID).enqueue(new Callback<List<FriendRequest>>() {
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                if (response.body() == null) {
                    RxToast.error("请求失败");
                    return;
                }
                list.clear();
                list.addAll(response.body());
                reqAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                RxToast.error("请求失败");
            }
        });
    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        requestList();
        reqRecyclerView = findViewById(R.id.request_recycler);
        reqRecyclerView.setHasFixedSize(true);
        reqLayoutManager = new LinearLayoutManager(FriendRequestActivity.this);
        reqRecyclerView.setLayoutManager(reqLayoutManager);
        reqAdapter = new RequestAdapter(list);
        reqRecyclerView.setAdapter(reqAdapter);
        reqRecyclerView.addItemDecoration(new DividerItemDecoration(FriendRequestActivity.this,
                DividerItemDecoration.VERTICAL));
        reqAdapter.setOnRequestItemClickListener(new RequestAdapter.OnRequestItemClickListener() {
            @Override
            public void onRequestItemClick(int position) {
                RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(FriendRequestActivity.this);
                rxDialogSureCancel.setTitle("申请处理");
                rxDialogSureCancel.setSure("接受");
                rxDialogSureCancel.setCancel("拒绝");
                rxDialogSureCancel.setContent("您是否接受对方的好友请求？");
                rxDialogSureCancel.show();
                rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendRequest friendRequest = list.get(position);
                        friendRequest.setReqCode(1);
                        requestApi.operateRequest(friendRequest).enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.body() == null) {
                                    RxToast.error("请求失败");
                                    return;
                                }
                                RxToast.success("接受成功");
                                requestList();
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                RxToast.error("请求失败");
                            }
                        });
                        rxDialogSureCancel.dismiss();
                    }
                });
                rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendRequest friendRequest = list.get(position);
                        friendRequest.setReqCode(2);
                        requestApi.operateRequest(friendRequest).enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.body() == null) {
                                    RxToast.error("请求失败");
                                    return;
                                }
                                RxToast.success("拒绝成功");
                                requestList();
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                RxToast.error("请求失败");
                            }
                        });
                        rxDialogSureCancel.dismiss();
                    }
                });
            }
        });
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
