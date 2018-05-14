package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.watch.chat.ServerManager;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class BindWatchInput2Activity extends BaseActivity {

    @BindView(R.id.edtImei)
    EditText edtImei;
//    @BindView(R.id.edtNickName)
//    EditText edtNickName;
//    @BindView(R.id.edtPhone)
//    EditText edtPhone;

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
                Intent intent = new Intent(BindWatchInput2Activity.this,BindWatchInputActivity.class);
                intent.putExtra("mScan", result);
                startActivityForResult(intent, 3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 4) {
            Intent intent = new Intent(BindWatchInput2Activity.this,
                    SecondActivity.class);
            setResult(5,intent);
            finish();
        }
    }
}
