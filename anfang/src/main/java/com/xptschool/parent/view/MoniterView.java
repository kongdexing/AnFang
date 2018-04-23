package com.xptschool.parent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xptschool.parent.R;

public class MoniterView extends LinearLayout {

    private String TAG = MoniterView.class.getSimpleName();

    public MoniterView(Context context) {
        this(context,null);
    }

    public MoniterView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "HomeItemView: attrs");
        View view = LayoutInflater.from(context).inflate(R.layout.watch_pop_moniter, this, true);

    }
}
