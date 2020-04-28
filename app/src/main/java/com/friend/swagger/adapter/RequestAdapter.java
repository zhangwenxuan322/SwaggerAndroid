package com.friend.swagger.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.entity.FriendRequest;
import com.friend.swagger.entity.UserProfile;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-28 22:29
 **/
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<FriendRequest> list;
    private UserApi userApi;
    // 监听接口
    private static OnRequestItemClickListener onRequestItemClickListener;

    public interface OnRequestItemClickListener {
        void onRequestItemClick(int position);
    }

    public void setOnRequestItemClickListener(OnRequestItemClickListener listener) {
        onRequestItemClickListener = listener;
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        public ImageView portrait;
        public TextView userName;
        public TextView requestContent;

        public RequestViewHolder(View v) {
            super(v);
            portrait = v.findViewById(R.id.request_portrait);
            userName = v.findViewById(R.id.request_user_name);
            requestContent = v.findViewById(R.id.request_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRequestItemClickListener != null) {
                        onRequestItemClickListener.onRequestItemClick(getAdapterPosition());
                    }
                }
            });

        }
    }

    public RequestAdapter(List<FriendRequest> list) {
        this.list = list;
        userApi = RetrofitService.createService(UserApi.class);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        int userId = list.get(position).getReqSubId();
        userApi.getUserById(userId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.body() != null) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body().get("userProfile");
                    holder.userName.setText(String.valueOf(map.get("userName")));
                    userApi.downloadPortrait(String.valueOf(map.get("userPortrait"))).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.body() != null) {
                                InputStream inputStream = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                holder.portrait.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
        holder.requestContent.setText(list.get(position).getReqMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
