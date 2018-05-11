package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.OnClick;

public class BindWatch1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_watch1);
        setTitle("绑定设备xxx");
    }

    @OnClick({R.id.llBind2})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.llBind2:
                Intent intent = new Intent(this, BindWatchInputActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                //绑定成功
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
