package com.shuhai.anfang.ui.contact;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shuhai.anfang.R;

/**
 * Created by dexing on 2017-12-14 0014.
 */

public class ContactSchoolView extends LinearLayout {

    public ContactSchoolView(Context context) {
        this(context, null);
    }

    public ContactSchoolView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_school, this, true);
    }
}
