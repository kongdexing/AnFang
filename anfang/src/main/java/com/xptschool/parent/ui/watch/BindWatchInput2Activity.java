package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手动录入 IMEI
 */
public class BindWatchInput2Activity extends BaseActivity {

    @BindView(R.id.edtImei)
    EditText edtImei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_watch_input2);
        setTitle("输入绑定码");
    }

    @OnClick({R.id.ok})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                String result = edtImei.getText().toString().trim();
                if (result.isEmpty()) {
                    ToastUtils.showToast(this, R.string.msg_imei_error);
                    return;
                }
                Intent intent = new Intent(BindWatchInput2Activity.this, BindWatchInputActivity.class);
                intent.putExtra("mScan", result);
                startActivityForResult(intent, 3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 4) {
            Intent intent = new Intent(BindWatchInput2Activity.this,
                    ScanActivity.class);
            setResult(5, intent);
            finish();
        }
    }
}
