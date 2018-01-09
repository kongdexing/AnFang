package com.xptschool.parent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.xptschool.parent.R;

public abstract class BaseRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    public String TAG = "";
    private final TypedValue mTypedValue = new TypedValue();
    public int mBackground;

    public BaseRecycleAdapter(Context context) {
        mContext = context;
//        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
        TAG = getClass().getSimpleName();
//        Log.i(TAG, "BaseRecycleAdapter: ");
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mContext = context;
        mBackground = mTypedValue.resourceId;
    }

}
