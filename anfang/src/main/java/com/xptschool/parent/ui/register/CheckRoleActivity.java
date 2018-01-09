package com.xptschool.parent.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.OnClick;

public class CheckRoleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_role);
        setTitle(R.string.login_role);
        showImgBack(false);
        setTxtRight(R.string.title_skip);
        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.llTeacher, R.id.llParent})
    void onViewClick(View view) {

        startActivity(new Intent(this, SelSchoolActivity.class));

        switch (view.getId()) {
            case R.id.llTeacher:

                break;
            case R.id.llParent:


                break;
        }
    }

}
