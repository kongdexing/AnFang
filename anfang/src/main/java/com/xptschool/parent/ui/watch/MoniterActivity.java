package com.xptschool.parent.ui.watch;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.uitl.TFileUtils;
import com.xptschool.parent.R;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.AlbumSourceView;
import com.xptschool.parent.view.MoniterView;

import java.io.File;

import butterknife.OnClick;

/**
 * 语音监听
 */
public class MoniterActivity extends BaseActivity {

    private PopupWindow picPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moniter);
        setTitle(R.string.home_moniter);

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
            view1.setPhone("17600200500");

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

    public void setMonitorPhone(String phone) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_Monitor,
                new VolleyHttpParamsEntity().addParam("imei", "867587027683984")
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
                            if (picPopup != null) {
                                picPopup.dismiss();
                            }
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
