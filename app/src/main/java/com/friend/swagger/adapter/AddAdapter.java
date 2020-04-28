package com.friend.swagger.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.entity.UserProfile;

import java.io.InputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-11 11:27
 **/
public class AddAdapter extends RecyclerView.Adapter<AddAdapter.AddViewHolder>{
    private List<UserProfile> list;
    private UserApi userApi;
    // 声明自定义的监听接口
    private static OnAddItemClickListener onAddItemClickListener;

    public interface OnAddItemClickListener {
        // RecyclerView的点击事件，将信息回调给view
        void onAddItemClick(int position);
    }

    public void setAddItemClickListener(OnAddItemClickListener listener) {
        onAddItemClickListener = listener;
    }

    public static class AddViewHolder extends RecyclerView.ViewHolder {
        public ImageView portrait;
        public TextView userName;
        public TextView userBio;

        public AddViewHolder(View v) {
            super(v);
            portrait = v.findViewById(R.id.add_portrait);
            userName = v.findViewById(R.id.add_user_name);
            userBio = v.findViewById(R.id.add_bio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddItemClickListener != null) {
                        onAddItemClickListener.onAddItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public AddAdapter(List<UserProfile> list) {
        this.list = list;
        userApi = RetrofitService.createService(UserApi.class);
    }

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_item, parent, false);
        return new AddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddViewHolder holder, int position) {
        userApi.downloadPortrait(list.get(position).getUserPortrait()).enqueue(new Callback<ResponseBody>() {
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
        holder.userName.setText(list.get(position).getUserName());
        holder.userBio.setText(list.get(position).getUserBio());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
