package com.xptschool.parent.ui.watch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ClockActivity extends BaseActivity {

    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        setTitle(R.string.home_clock);
        initView();
    }

    private void initView() {

        container.addView(new WatchAlarmView(this));
        container.addView(new WatchAlarmView(this));
        container.addView(new WatchAlarmView(this));

    }



}
