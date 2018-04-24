package com.xptschool.parent.ui.watch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.xptschool.parent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchAlarmView extends LinearLayout {

    @BindView(R.id.txtAPM)
    TextView txtAPM;
    @BindView(R.id.txtTime)
    TextView txtTime;
    @BindView(R.id.txtMode)
    TextView txtMode;
    @BindView(R.id.switch1)
    Switch switch1;

    //闹铃格式:时间-开关-频率（1：一次；2：每天；3：自定义）

    private String defTime = "07:00";
    private String defMode = "1";
    private String defWeek = "0000000";

    public WatchAlarmView(Context context) {
        this(context, null);
    }

    public WatchAlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_watch_clock, this, true);
        ButterKnife.bind(view);

    }

    public void bindData(String str){


    }

    @OnClick({R.id.rlItem1,R.id.switch1})
    void viewClick(View view){
        switch (view.getId()){
            case R.id.rlItem1:

                break;
            case R.id.switch1:

                break;
        }
    }


}
