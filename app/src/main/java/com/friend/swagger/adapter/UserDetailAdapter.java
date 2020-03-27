package com.friend.swagger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.friend.swagger.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author ZhangWenXuan
 * @Date 2020-03-27 20:12
 **/
public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.UserDetailViewHolder> {
    private List<String> detailTitles;
    private List<String> detailInfos;
    private static OnUserDetailClickListener onUserDetailClickListener;

    public interface OnUserDetailClickListener {
        void onUserDetailItemClick(int position);
    }

    public void setOnUserDetailClickListener(OnUserDetailClickListener listener) {
        onUserDetailClickListener = listener;
    }

    public static class UserDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView detailTitle;
        public TextView detailInfo;

        public UserDetailViewHolder(View v) {
            super(v);
            detailTitle = v.findViewById(R.id.detail_title);
            detailInfo = v.findViewById(R.id.detail_info);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onUserDetailClickListener != null) {
                        onUserDetailClickListener.onUserDetailItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public UserDetailAdapter(List<String> detailInfos) {
        this.detailInfos = detailInfos;
        detailTitles = new ArrayList<>();
        detailTitles.add("SwaggerId");
        detailTitles.add("手机号");
        detailTitles.add("性别");
        detailTitles.add("签名");
    }

    @NonNull
    @Override
    public UserDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_detail_item, parent, false);
        return new UserDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailViewHolder holder, int position) {
        holder.detailTitle.setText(detailTitles.get(position));
        holder.detailInfo.setText(detailInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return detailTitles.size();
    }

}
