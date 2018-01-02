package com.shuhai.anfang.ui.mine;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserHelper;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.view.CustomDialog;

/**
 * Created by shuhaixinxi on 2017/12/20.
 */

public class BaseInfoView extends LinearLayout {

    private String TAG = "InfoView";
    Context mContext;

    public BaseInfoView(Context context) {
        this(context, null);
    }

    public BaseInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

}
