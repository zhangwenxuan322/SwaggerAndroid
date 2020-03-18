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
    private List<Integer> mDataset;

    public static class SettingsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public SettingsViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tv1);
        }
    }

    public SettingsAdapter(List<Integer> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public SettingsAdapter.SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SettingsViewHolder vh = new SettingsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(mDataset.get(position)));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
