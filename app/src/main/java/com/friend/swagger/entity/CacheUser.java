package com.friend.swagger.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Author ZhangWenXuan
 * @Date 2020-04-26 09:44
 **/
@Entity(tableName = "cache_user_table")
public class CacheUser {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String account;
    private String password;

    public CacheUser() {
    }

    public CacheUser(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CacheUser{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
