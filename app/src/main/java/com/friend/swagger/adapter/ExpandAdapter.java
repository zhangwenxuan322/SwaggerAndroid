package com.friend.swagger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.friend.swagger.R;
import com.friend.swagger.entity.GroupFriends;

import java.util.List;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-29 16:19
 **/
public class ExpandAdapter extends BaseExpandableListAdapter {
    private int groupViewId;
    private int childViewId;
    private LayoutInflater layoutInflater;
    private List<GroupFriends> list;

    public ExpandAdapter(int groupViewId, int childViewId, LayoutInflater layoutInflater, List<GroupFriends> list) {
        this.groupViewId = groupViewId;
        this.childViewId = childViewId;
        this.layoutInflater = layoutInflater;
        this.list = list;
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
        View view = layoutInflater.inflate(childViewId, parent, false);
        TextView textView = view.findViewById(R.id.child_name);
        textView.setText(list.get(groupPosition).getFriends().get(childPosition).getUserName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
