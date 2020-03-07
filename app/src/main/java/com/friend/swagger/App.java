package com.friend.swagger;

import android.app.Application;

import com.billy.android.swipe.SmartSwipeBack;
import com.tamsiree.rxtool.RxTool;

import io.rong.imkit.RongIM;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-21 15:20
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        // 换作创建应用时得到的 App Key
        String appKey = "pwe86ga5p9vz6";
        // 调用 SDK 的初始化方法
        RongIM.init(this, appKey);
    }
}
