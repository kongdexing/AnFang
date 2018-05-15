package com.xptschool.parent.ui.watch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ClockActivity extends BaseActivity {

    @BindView(R.id.container)
    LinearLayout container;
    public String[] alarmArray = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        setTitle(R.string.home_clock);
    }

    private void initView() {
        container.removeAllViews();

        for (int i = 0; i < 3; i++) {
            WatchAlarmView watchAlarmView = new WatchAlarmView(ClockActivity.this);
            watchAlarmView.bindData(watchAlarmView.defTime, i);
            alarmArray[i] = watchAlarmView.defTime;
            container.addView(watchAlarmView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAlarmList();
    }

    private void getAlarmList() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_ALARM_LIST,
                new VolleyHttpParamsEntity().addParam("imei", XPTApplication.getInstance().getCurrentWatchIMEI()), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取闹钟信息");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    container.removeAllViews();
                                    //08:10-1-1,08:10-1-2,08:10-1-3-23111110
                                    String[] alarmItems = volleyHttpResult.getInfo().toString().split(",");

                                    for (int i = 0; i < 3; i++) {
                                        WatchAlarmView watchAlarmView = new WatchAlarmView(ClockActivity.this);
                                        try {
                                            alarmArray[i] = alarmItems[i].trim();
                                        } catch (Exception ex) {
                                            alarmArray[i] = watchAlarmView.defTime;
                                        }
                                        watchAlarmView.bindData(alarmArray[i], i);
                                        container.addView(watchAlarmView);
                                    }
                                } catch (Exception ex) {
                                    initView();
                                }
                                break;
                            default:
                                ToastUtils.showToast(ClockActivity.this, volleyHttpResult.getInfo());
                                initView();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                        initView();
                    }
                });

    }

}