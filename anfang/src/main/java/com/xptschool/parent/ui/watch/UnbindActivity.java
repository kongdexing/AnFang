package com.xptschool.parent.ui.watch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import com.xptschool.parent.ui.mine.MyChildActivity;
import com.xptschool.parent.ui.watch.chat.ServerManager;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 解绑设备
 */
public class UnbindActivity extends BaseActivity {

    @BindView(R.id.txtIMEI)
    TextView txtIMEI;
    BeanStudent currentStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind);
        setTitle(R.string.title_device_unbind);

        currentStudent = XPTApplication.getInstance().getCurrentWatchStu();
        txtIMEI.setText(currentStudent.getImei_id());
    }

    @OnClick({R.id.rlUnbind})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlUnbind:
                //解绑设备
                CustomDialog dialog = new CustomDialog(mContext);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage(mContext.getResources().getString(R.string.msg_unbind_watch, currentStudent.getImei_id()));
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        unBindDevice();
                    }
                });
                break;
        }
    }

    private void unBindDevice() {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.WATCH_UnBind,
                new VolleyHttpParamsEntity()
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在解除设备");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        ToastUtils.showToast(mContext, volleyHttpResult.getInfo());
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            //删除学生，重新刷新界面
                            GreenDaoHelper.getInstance().deleteStuById(currentStudent.getStu_id());
                            ServerManager.getInstance().stopService(mContext);
                            UnbindActivity.this.finish();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });

    }

}
