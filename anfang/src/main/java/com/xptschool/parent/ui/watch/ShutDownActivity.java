package com.xptschool.parent.ui.watch;

import android.os.Bundle;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import butterknife.OnClick;

/**
 * 远程关机
 */
public class ShutDownActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shut_down);

        setTitle(R.string.home_shutdown);

    }

    @OnClick({R.id.btnShutdown})
    void viewOnclick(View view) {
        switch (view.getId()) {
            case R.id.btnShutdown:
                //弹出登录对话框
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle("远程关机");
                dialog.setMessage("确定要远程关闭设备吗？");
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        shutdownWatch();
                    }
                });
                break;
        }
    }

    private void shutdownWatch() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_SHUTDOWN,
                new VolleyHttpParamsEntity().addParam("imei", "867587027680824")
                        .addParam("state", "1"), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在关机");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                ToastUtils.showToast(ShutDownActivity.this, "发送成功");
                                break;
                            default:
                                ToastUtils.showToast(ShutDownActivity.this, "发送失败");
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                        ToastUtils.showToast(ShutDownActivity.this, "发送失败");
                    }
                });
    }

}
