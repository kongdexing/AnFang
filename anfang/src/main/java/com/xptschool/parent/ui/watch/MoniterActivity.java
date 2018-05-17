package com.xptschool.parent.ui.watch;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.CircularImageView;
import com.jph.takephoto.uitl.TFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.MoniterView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 语音监听
 */
public class MoniterActivity extends BaseActivity {

    private PopupWindow picPopup;

    @BindView(R.id.imgHead)
    CircularImageView imgHead;
    @BindView(R.id.txtNickName)
    TextView txtNickName;
    @BindView(R.id.txtIMEI)
    TextView txtIMEI;
    @BindView(R.id.txtCardPhone)
    TextView txtCardPhone;
    BeanStudent currentStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moniter);
        setTitle(R.string.home_moniter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setMonitorData();
    }

    private void setMonitorData() {
        currentStudent = XPTApplication.getInstance().getCurrentWatchStu();

        if (currentStudent != null) {
            ImageLoader.getInstance().displayImage(currentStudent.getPhoto(),
                    new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());
            txtNickName.setText(currentStudent.getStu_name());
            txtCardPhone.setText(currentStudent.getCard_phone());
            txtIMEI.setText(currentStudent.getImei_id());
        }
    }

    @OnClick({R.id.rlItem1})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlItem1:
                choosePic(findViewById(R.id.rlItem1));
                break;
        }
    }

    //弹起头像选择器
    public void choosePic(View view) {
        //选择相片来源
        if (picPopup == null) {
            TFileUtils.setCacheFile(null);

            MoniterView view1 = new MoniterView(this);
            view1.setStudentData(currentStudent);

            picPopup = new PopupWindow(view1,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            picPopup.setTouchable(true);
            picPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            picPopup.setBackgroundDrawable(new ColorDrawable());
            picPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1.0f);
                }
            });
        }
        backgroundAlpha(0.5f);
        picPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void setMonitorPhone(final String phone) {
        if (picPopup != null) {
            picPopup.dismiss();
        }

        if (phone.isEmpty()) {
            ToastUtils.showToast(this, R.string.hint_phone);
            return;
        }
        if (!CommonUtil.isPhone(phone)){
            ToastUtils.showToast(this, R.string.input_error_phone);
            return;
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.SET_WATCH_Monitor,
                new VolleyHttpParamsEntity().addParam("imei", XPTApplication.getInstance().getCurrentWatchIMEI())
                        .addParam("phone", phone),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在设置监护号码");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        ToastUtils.showToast(MoniterActivity.this, volleyHttpResult.getInfo());
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            //修改 imei 设备对应的监听号码
                            BeanStudent student = GreenDaoHelper.getInstance().getStudentByIMEI(XPTApplication.getInstance().getCurrentWatchIMEI());
                            student.setMonitor(phone);
                            GreenDaoHelper.getInstance().updateStudent(student);
                            setMonitorData();
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
