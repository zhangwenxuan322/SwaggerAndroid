package com.friend.swagger.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.friend.swagger.R;
import com.friend.swagger.api.RetrofitService;
import com.friend.swagger.api.UserApi;
import com.friend.swagger.entity.GroupFriends;
import com.friend.swagger.entity.UserProfile;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-29 16:19
 **/
public class ExpandAdapter extends BaseExpandableListAdapter {
    private int groupViewId;
    private int childViewId;
    private LayoutInflater layoutInflater;
    private List<GroupFriends> list;
    private UserApi userApi;

    public ExpandAdapter(int groupViewId, int childViewId, LayoutInflater layoutInflater, List<GroupFriends> list) {
        this.groupViewId = groupViewId;
        this.childViewId = childViewId;
        this.layoutInflater = layoutInflater;
        this.list = list;
        userApi = RetrofitService.createService(UserApi.class);
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getFriends().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getFriends().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView != null)
            return convertView;
        View view = layoutInflater.inflate(groupViewId, parent, false);
        TextView textView = view.findViewById(R.id.group_name);
        textView.setText(list.get(groupPosition).getGroupName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView != null)
            return convertView;
        UserProfile userProfile = list.get(groupPosition).getFriends().get(childPosition);
        View view = layoutInflater.inflate(childViewId, parent, false);
        ImageView portrait = view.findViewById(R.id.portrait);
        TextView userName = view.findViewById(R.id.user_name);
        TextView bio = view.findViewById(R.id.bio);
        userName.setText(userProfile.getUserName());
        bio.setText(userProfile.getUserBio());
        userApi.downloadPortrait(userProfile.getUserPortrait()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    portrait.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
