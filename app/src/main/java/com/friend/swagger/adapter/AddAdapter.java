package com.friend.swagger.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.friend.swagger.R;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.entity.UserProfile;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-11 11:27
 **/
public class AddAdapter {
    private List<UserProfile> list;
    private UserApi userApi;
    //声明自定义的监听接口
    private static OnAddItemClickListener onAddItemClickListener;

    public interface OnAddItemClickListener {
        // RecyclerView的点击事件，将信息回调给view
        void onAddItemClick(int position);
    }

    public void setNearbyItemClickListener(OnAddItemClickListener listener) {
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
}
