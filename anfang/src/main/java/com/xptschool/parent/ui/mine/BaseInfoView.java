package com.xptschool.parent.ui.mine;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by shuhaixinxi on 2017/12/20.
 */

public class BaseInfoView extends LinearLayout {

    private String TAG = "InfoView";
    public Context mContext;

    public BaseInfoView(Context context) {
        this(context, null);
    }

    public BaseInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

}
