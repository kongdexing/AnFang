package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.OnClick;

//聊天列表页面
public class ChatListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        setTitle(R.string.home_chat);

    }

    @OnClick({R.id.rlItem1})
    void onClick(View view){
        switch (view.getId()){
            case R.id.rlItem1:

                startActivity(new Intent(this,ChatDetailActivity.class));

                break;
        }
    }

}
