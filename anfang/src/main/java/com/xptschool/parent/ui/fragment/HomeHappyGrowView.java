package com.xptschool.parent.ui.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.mine.BaseInfoView;

import butterknife.ButterKnife;

/**
 * 快乐成长
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeHappyGrowView extends BaseInfoView {

    public HomeHappyGrowView(Context context) {
        super(context);
    }

    public HomeHappyGrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_edu, this, true);
        ButterKnife.bind(view);

    }



}
