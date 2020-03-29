package com.friend.swagger.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.friend.swagger.R;

public class ModifyUserDetailActivity extends AppCompatActivity {
    public static final String EXTRA_SWAGGER_ID =
            "indi.friend.swagger.ModifyUserDetailActivity.EXTRA_SWAGGER_ID";
    public static final String EXTRA_SEX =
            "indi.friend.swagger.ModifyUserDetailActivity.EXTRA_SEX";
    public static final String EXTRA_BIO =
            "indi.friend.swagger.ModifyUserDetailActivity.EXTRA_BIO";
    private EditText editText;
    private RadioGroup sexGroup;
    private RadioButton male;
    private RadioButton female;
    private String title = "";
    private String textHint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeify_user_detail);
        sexGroup = findViewById(R.id.sex_group);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        initToolbar();
        initTitleAndHint();
    }

    private void initTitleAndHint() {
        editText = findViewById(R.id.edit_text);
        Intent intent = getIntent();
        if (intent.getStringExtra(EXTRA_SWAGGER_ID) != null) {
            title = "SwaggerId";
            textHint = "设置您的SwaggerId，此Id只可设置一次，由字母数字和字符组成，要求唯一";
            setTitle(title);
            editText.setHint(textHint);
            sexGroup.setVisibility(View.GONE);
        } else if (intent.getStringExtra(EXTRA_SEX) != null) {
            title = "性别";
            setTitle(title);
            editText.setVisibility(View.GONE);
            if (intent.getStringExtra(EXTRA_SEX).equals("男"))
                male.setChecked(true);
            else
                female.setChecked(true);
        } else if (intent.getStringExtra(EXTRA_BIO) != null) {
            title = "个性签名";
            if (intent.getStringExtra(EXTRA_BIO).equals("未设置")) {
                textHint = "记录一下心情吧！";
                editText.setHint(textHint);
            } else editText.setText(intent.getStringExtra(EXTRA_BIO));
            sexGroup.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_modify:
                Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_user_detail, menu);
        return true;
    }
}
