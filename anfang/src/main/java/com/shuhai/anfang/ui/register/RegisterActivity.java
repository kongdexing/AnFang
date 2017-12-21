package com.shuhai.anfang.ui.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.main.BaseActivity;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle(R.string.title_register);


    }


}
