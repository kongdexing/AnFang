package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.login.LoginActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class FriendActivity extends BaseActivity {

    @BindView(R.id.rlFriend1)
    RelativeLayout rlFriend1;
    @BindView(R.id.txtNoFriend)
    TextView txtNoFriend;

    @BindView(R.id.txtIMEI)
    TextView txtIMEI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setTitle(R.string.home_friend);
        getFriendList();
    }

    @OnClick({R.id.btnDelete})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                //弹出登录对话框
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage("确定要删除该好友吗？");
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        rlFriend1.setVisibility(View.GONE);
                        ToastUtils.showToast(FriendActivity.this, "删除成功");
                    }
                });
                break;
        }
    }

    private void getFriendList() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_FRIENDLIST,
                new VolleyHttpParamsEntity().addParam("imei", XPTApplication.getInstance().getCurrentWatchIMEI()),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取好友列表");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    rlFriend1.setVisibility(View.VISIBLE);
                                    txtNoFriend.setVisibility(View.GONE);
                                    JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                                    txtIMEI.setText(jsonObject.getString("id"));
                                } catch (Exception ex) {
                                    ToastUtils.showToast(FriendActivity.this, "数据解析错误");
                                    rlFriend1.setVisibility(View.GONE);
                                    txtNoFriend.setVisibility(View.VISIBLE);
                                }
                                break;
                            case HttpAction.FAILED:
                                rlFriend1.setVisibility(View.GONE);
                                txtNoFriend.setVisibility(View.VISIBLE);
                                break;
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
