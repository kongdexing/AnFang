package com.shuhai.anfang.ui.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.main.BaseActivity;

//选择学校|申请学校
public class SelSchoolActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_school);
        setTitle(R.string.title_select_school);
    }


}
