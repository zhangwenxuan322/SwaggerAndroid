package com.friend.swagger.entity;

import java.util.List;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-29 16:09
 **/
public class GroupFriends {
    private List<UserProfile> friends;
    private String groupName;

    public GroupFriends() {
    }

    public GroupFriends(List<UserProfile> friends, String groupName) {
        this.friends = friends;
        this.groupName = groupName;
    }

    public List<UserProfile> getFriends() {
        return friends;
    }

    public void setFriends(List<UserProfile> friends) {
        this.friends = friends;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "GroupFriends{" +
                "friends=" + friends +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
