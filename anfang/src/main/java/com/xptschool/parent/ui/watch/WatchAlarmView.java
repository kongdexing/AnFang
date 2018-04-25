package com.xptschool.parent.ui.watch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.mine.BaseInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchAlarmView extends BaseInfoView {

    @BindView(R.id.txtAPM)
    TextView txtAPM;
    @BindView(R.id.txtTime)
    TextView txtTime;
    @BindView(R.id.txtMode)
    TextView txtMode;
    @BindView(R.id.switch1)
    Switch switch1;

    //闹铃格式:时间-开关-频率（1：一次；2：每天；3：自定义）

    public String defTime = "07:00-0-1";
    private String currentAlarm = "";
    private int currentAlarmIndex = 0;

    public WatchAlarmView(Context context) {
        this(context, null);
    }

    public WatchAlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_watch_clock, this, true);
        ButterKnife.bind(view);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    /**
     * 时间-开关-频率
     *
     * @param str 08:10-1-1,08:10-1-2,08:10-1-3-0111110（周一至周五打开）
     */
    public void bindData(String str, int index) {
        if (str == null || str.isEmpty()) {
            bindData(defTime, index);
            return;
        }
        try {
            currentAlarm = str;
            currentAlarmIndex = index;
            String[] clock = str.split("-");
            //时间
            String time = clock[0];
            txtTime.setText(time);

            String hour = time.split(":")[0].trim();
            int apm = Integer.parseInt(hour);
            if (apm > 12 && apm < 23) {
                txtAPM.setText("下午");
            } else {
                txtAPM.setText("上午");
            }

            //开关
            String open = clock[1];
            switch1.setChecked("1".equals(open) ? true : false);

            //频率
            String rate = clock[2];
            if ("1".equals(rate)) {
                txtMode.setText("一次");
            } else if ("2".equals(rate)) {
                txtMode.setText("每天");
            } else if ("3".equals(rate)) {
                char[] weeks = clock[3].toCharArray();
                txtMode.setText("每周");
                String[] weekDay = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
                StringBuffer sbStr = new StringBuffer();
                for (int i = 0; i < weeks.length; i++) {
                    if (weeks[i] == '1') {
                        sbStr.append(weekDay[i] + " ");
                    }
                }
                txtMode.setText(sbStr.toString());
            }
        } catch (Exception ex) {
            bindData(defTime, index);
        }
    }

    @OnClick({R.id.rlItem1, R.id.switch1})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlItem1:
                Intent intent = new Intent(this.getContext(), ClockDetailActivity.class);
                intent.putExtra("alarm", currentAlarm);
                intent.putExtra("allAlarm", ((ClockActivity) mContext).alarmArray);
                intent.putExtra("alarmIndex", currentAlarmIndex);
                this.getContext().startActivity(intent);
                break;
            case R.id.switch1:
                Log.i("AlarmView", "onCheckedChanged: " + switch1.isChecked());
                String open = switch1.isChecked() ? "1" : "0";
                try {
                    String[] alarms = currentAlarm.split("-");
                    alarms[1] = open;
                    String strAlarm = alarms[0] + "-" + alarms[1] + "-" + alarms[2];
                    if (alarms.length == 4) {
                        strAlarm += "-" + alarms[3];
                    }
                    updateAlarm(strAlarm, currentAlarmIndex);
                } catch (Exception ex) {

                }
                break;
        }
    }

    public void updateAlarm(String alarm, int index) {
        String[] alarmArray = ((ClockActivity) mContext).alarmArray;

        String allAlarm = "";

        try {
            if (alarm != null) {
                alarmArray[index] = alarm;
            }

            for (int i = 0; i < alarmArray.length; i++) {
                allAlarm += alarmArray[i] + ",";
            }
            allAlarm = allAlarm.substring(0, allAlarm.length() - 1);
        } catch (Exception ex) {
            allAlarm = "";
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_ALARM_EDIT,
                new VolleyHttpParamsEntity().addParam("imei", ((ClockActivity) mContext).imei)
                        .addParam("AlarmTime", allAlarm),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((ClockActivity) mContext).showProgress("正在设置闹钟");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        ((ClockActivity) mContext).hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:

                                break;
                            default:
                                switch1.setChecked(!switch1.isChecked());
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        ((ClockActivity) mContext).hideProgress();
                        switch1.setChecked(!switch1.isChecked());
                    }
                });
    }

}
