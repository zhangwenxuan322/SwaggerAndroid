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
import com.friend.swagger.entity.NearbyUser;

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
 * @Date 2020-03-21 19:59
 **/
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {
    private List<NearbyUser> nearbyDataSet;
    private UserApi userApi;
    //声明自定义的监听接口
    private static OnNearbyItemClickListener onNearbyItemClickListener;

    public interface OnNearbyItemClickListener {
        // RecyclerView的点击事件，将信息回调给view
        void onNearbyItemClick(int position);
    }

    public void setNearbyItemClickListener(OnNearbyItemClickListener listener) {
        onNearbyItemClickListener = listener;
    }

    public static class NearbyViewHolder extends RecyclerView.ViewHolder {
        public ImageView nearbyPortrait;
        public TextView nearbyUserName;
        public TextView nearbyDistance;
        public TextView nearbyBio;

        public NearbyViewHolder(View v) {
            super(v);
            nearbyPortrait = v.findViewById(R.id.nearby_portrait);
            nearbyUserName = v.findViewById(R.id.nearby_user_name);
            nearbyDistance = v.findViewById(R.id.nearby_distance);
            nearbyBio = v.findViewById(R.id.nearby_bio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNearbyItemClickListener != null) {
                        onNearbyItemClickListener.onNearbyItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public NearbyAdapter(List<NearbyUser> mDataset) {
        this.nearbyDataSet = mDataset;
        userApi = RetrofitService.createService(UserApi.class);
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_item, parent, false);
        return new NearbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        userApi.downloadPortrait(nearbyDataSet.get(position).getNearbyPortrait()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    holder.nearbyPortrait.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        holder.nearbyUserName.setText(nearbyDataSet.get(position).getNearbyUserName());
        holder.nearbyDistance.setText(nearbyDataSet.get(position).getNearbyDistance());
        holder.nearbyBio.setText(nearbyDataSet.get(position).getNearbyUserBio());
    }

    @Override
    public int getItemCount() {
        return nearbyDataSet.size();
    }

}
