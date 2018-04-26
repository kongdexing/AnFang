package com.xptschool.parent.ui.watch;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.SmoothCheckBox;
import com.android.widget.wheelview.OnWheelChangedListener;
import com.android.widget.wheelview.WheelView;
import com.android.widget.wheelview.adapter.ArrayWheelAdapter;
import com.android.widget.wheelview.adapter.NumericWheelAdapter;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ClockDetailActivity extends BaseActivity {

    @BindView(R.id.APMView)
    WheelView APMWheelView;
    @BindView(R.id.hourWheelView)
    WheelView hourWheelView;
    @BindView(R.id.minuteWheelView)
    WheelView minuteWheelView;

    @BindView(R.id.ckbRepeat1)
    SmoothCheckBox ckbRepeat1;
    @BindView(R.id.ckbRepeat2)
    SmoothCheckBox ckbRepeat2;
    @BindView(R.id.ckbRepeat3)
    SmoothCheckBox ckbRepeat3;

    @BindView(R.id.week1)
    TextView week1;
    @BindView(R.id.week2)
    TextView week2;
    @BindView(R.id.week3)
    TextView week3;
    @BindView(R.id.week4)
    TextView week4;
    @BindView(R.id.week5)
    TextView week5;
    @BindView(R.id.week6)
    TextView week6;
    @BindView(R.id.week7)
    TextView week7;

    TextView[] weekViews = new TextView[7];
    private String currentAlarm = "";
    private String[] alarmArray = new String[3];
    private int alarmIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_detail);
        setTitle("编辑闹钟");
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentAlarm = bundle.getString("alarm");
            alarmArray = (String[]) bundle.get("allAlarm");
            alarmIndex = bundle.getInt("alarmIndex");
            bindData(currentAlarm);
        }

    }

    private void initView() {

        weekViews[0] = week7;
        weekViews[1] = week1;
        weekViews[2] = week2;
        weekViews[3] = week3;
        weekViews[4] = week4;
        weekViews[5] = week5;
        weekViews[6] = week6;

        for (int i = 0; i < weekViews.length; i++) {
            weekViews[i].setTag(false);
        }

        APMWheelView.setWheelBackground(R.color.bg_window);
        hourWheelView.setWheelBackground(R.color.bg_window);
        minuteWheelView.setWheelBackground(R.color.bg_window);

        APMWheelView.setWheelForeground(R.drawable.wheel_val);
        hourWheelView.setWheelForeground(R.drawable.wheel_val);
        minuteWheelView.setWheelForeground(R.drawable.wheel_val);

        String[] apmStr = {"上午", "下午"};
        APMWheelView.setViewAdapter(new APMAdapter(this, apmStr));
//        APMWheelView.setCyclic(true);
        APMWheelView.setEnabled(false);

        hourWheelView.setViewAdapter(new DateHourAdapter(this, 0, 23, 4));
        hourWheelView.setVisibleItems(5);
        hourWheelView.setCurrentItem(4);
        hourWheelView.setCyclic(true);
        hourWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                Log.i(TAG, "onChanged: " + oldValue + "  newVal " + newValue);
                if (newValue > 11 && newValue < 24) {
                    APMWheelView.setCurrentItem(1);
                } else {
                    APMWheelView.setCurrentItem(0);
                }
            }
        });

        minuteWheelView.setViewAdapter(new DateMinuteAdapter(this, 0, 59, 34));
        minuteWheelView.setCurrentItem(34);
        minuteWheelView.setVisibleItems(5);
        minuteWheelView.setCyclic(true);

        for (int i = 0; i < weekViews.length; i++) {
            weekViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ckbRepeat3.isChecked()) {
                        return;
                    }
                    boolean click = (Boolean) view.getTag();
                    view.setBackgroundColor(getResources().getColor(click ? R.color.item_clicked : R.color.text_black_2));
                    view.setTag(!click);
                }
            });
        }
    }

    private void bindData(String data) {
        try {
            String[] clock = data.split("-");
            //时间
            String time = clock[0];
            int hour = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);
            hourWheelView.setCurrentItem(hour);
            minuteWheelView.setCurrentItem(minute);

            int apm = hour;
            if (apm > 12 && apm < 23) {
                APMWheelView.setCurrentItem(1);
            } else {
                APMWheelView.setCurrentItem(0);
            }

            //频率
            String rate = clock[2];
            if ("1".equals(rate)) {
                ckbRepeat1.setChecked(true);
            } else if ("2".equals(rate)) {
                ckbRepeat2.setChecked(true);
            } else if ("3".equals(rate)) {
                ckbRepeat3.setChecked(true);

                char[] weeks = clock[3].toCharArray();

                for (int i = 0; i < weeks.length; i++) {
                    if (weeks[i] == '1') {
                        weekViews[i].setTag(true);
                        weekViews[i].setBackgroundColor(getResources().getColor(R.color.text_black_2));
                    } else {
                        weekViews[i].setTag(false);
                        weekViews[i].setBackgroundColor(getResources().getColor(R.color.item_clicked));
                    }
                }
            }
        } catch (Exception ex) {

        }

    }

    @OnClick({R.id.rlRepeat1, R.id.ckbRepeat1, R.id.rlRepeat2, R.id.ckbRepeat2, R.id.llRepeat3, R.id.ckbRepeat3,
            R.id.ok})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlRepeat1:
            case R.id.ckbRepeat1:
                resetCheckBox(ckbRepeat1);
                break;
            case R.id.rlRepeat2:
            case R.id.ckbRepeat2:
                resetCheckBox(ckbRepeat2);
                break;
            case R.id.llRepeat3:
            case R.id.ckbRepeat3:
                resetCheckBox(ckbRepeat3);
                for (int i = 0; i < weekViews.length; i++) {
                    boolean click = (Boolean) weekViews[i].getTag();
                    weekViews[i].setBackgroundColor(getResources().getColor(click ? R.color.text_black_2 : R.color.item_clicked));
                }
                break;
            case R.id.ok:
                int hour = hourWheelView.getCurrentItem();
                int minute = minuteWheelView.getCurrentItem();

                String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                String mode = "";
                if (ckbRepeat1.isChecked()) {
                    //响铃一次
                    mode = "-1-1";
                } else if (ckbRepeat2.isChecked()) {
                    //每天响铃
                    mode = "-1-2";
                } else if (ckbRepeat3.isChecked()) {
                    //自定义每周
                    String weeks = getCheckWeekStr();
                    if (weeks.equals("0000000")) {
                        ToastUtils.showToast(this, "请选择具体某一天");
                        return;
                    }
                    mode = "-1-3-" + weeks;
                }
                updateAlarm(time + mode, alarmIndex);
                break;
        }
    }

    private void resetCheckBox(SmoothCheckBox currentCkb) {
        ckbRepeat1.setChecked(false);
        ckbRepeat2.setChecked(false);
        ckbRepeat3.setChecked(false);
        currentCkb.setChecked(true);

        for (int i = 0; i < weekViews.length; i++) {
            weekViews[i].setBackgroundColor(getResources().getColor(R.color.item_clicked));
        }
    }

    private String getCheckWeekStr() {
        StringBuffer checkStatus = new StringBuffer();
        for (int i = 0; i < weekViews.length; i++) {
            boolean click = (Boolean) weekViews[i].getTag();
            if (click) {
                checkStatus.append("1");
            } else {
                checkStatus.append("0");
            }
        }
        Log.i(TAG, "checkWeek: " + checkStatus.toString());
        return checkStatus.toString();
    }

    public void updateAlarm(String alarm, int index) {

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
                new VolleyHttpParamsEntity().addParam("imei", XPTApplication.getInstance().getCurrentWatchIMEI())
                        .addParam("AlarmTime", allAlarm),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在设置闹钟");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        ToastUtils.showToast(ClockDetailActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

    private class APMAdapter extends ArrayWheelAdapter {
        public APMAdapter(Context context, Object[] items) {
            super(context, items);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) ClockDetailActivity.this.getResources().getDimensionPixelOffset(R.dimen.sp_22));
            setTextColor(ClockDetailActivity.this.getResources().getColor(R.color.white));
        }

        @Override
        public CharSequence getItemText(int index) {
            return super.getItemText(index);
        }

        @Override
        public int getItemsCount() {
            return super.getItemsCount();
        }
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateHourAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateHourAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue, "%02d 时");
            this.currentValue = current;
            setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) ClockDetailActivity.this.getResources().getDimensionPixelOffset(R.dimen.sp_24));
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(ClockDetailActivity.this.getResources().getColor(R.color.text_black));
            } else {
                view.setTextColor(ClockDetailActivity.this.getResources().getColor(R.color.white));
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateMinuteAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateMinuteAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue, "%02d 分");
            this.currentValue = current;
            setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) ClockDetailActivity.this.getResources().getDimensionPixelOffset(R.dimen.sp_24));
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(ClockDetailActivity.this.getResources().getColor(R.color.text_black));
            } else {
                view.setTextColor(ClockDetailActivity.this.getResources().getColor(R.color.white));
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

}
