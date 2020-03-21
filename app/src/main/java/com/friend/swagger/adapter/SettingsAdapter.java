package com.friend.swagger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.friend.swagger.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author ZhangWenXuan
 * @Date 2020-03-18 10:02
 **/
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {
    private List<String> mDataset;
    //声明自定义的监听接口
    private static OnSettingsItemClickListener onSettingsItemClickListener;

    public interface OnSettingsItemClickListener {
        // RecyclerView的点击事件，将信息回调给view
        void onSettingsItemClick(int position);
    }

    public void setSettingsItemClickListener(OnSettingsItemClickListener listener) {
        onSettingsItemClickListener = listener;
    }

    public static class SettingsViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public SettingsViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.settings_option);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSettingsItemClickListener != null) {
                        onSettingsItemClickListener.onSettingsItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public SettingsAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public SettingsAdapter.SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_item, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
