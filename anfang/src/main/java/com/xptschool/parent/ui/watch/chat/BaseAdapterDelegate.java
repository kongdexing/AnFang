package com.xptschool.parent.ui.watch.chat;

import android.content.Context;

/**
 * Created by dexing on 2017/5/16.
 * Modified by dexing on2018/4/27
 * No1
 */

public class BaseAdapterDelegate {

    public String TAG = BaseAdapterDelegate.class.getSimpleName();
    public Context mContext;

    public BaseAdapterDelegate(Context context) {
        this.mContext = context;
        TAG = mContext.getClass().getSimpleName();
    }


}
