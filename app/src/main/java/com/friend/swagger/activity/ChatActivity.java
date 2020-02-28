package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import com.friend.swagger.R;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_ACCOUNT =
            "indi.friend.swagger.EXTRA_ACCOUNT";
    public static final String EXTRA_TOKEN =
            "indi.friend.swagger.EXTRA_TOKEN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String userToken = intent.getStringExtra(EXTRA_TOKEN);
        // 连接 IM
        RongIM.connect(userToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            }

            @Override
            public void onSuccess(String s) {
                initFragment();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    private void initFragment() {
        // 集成 ConversationListFragment
        ConversationListFragment conversationListFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();
        conversationListFragment.setUri(uri);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_layout, conversationListFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
