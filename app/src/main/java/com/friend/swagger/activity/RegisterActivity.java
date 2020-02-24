package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.friend.swagger.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 隐藏menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
