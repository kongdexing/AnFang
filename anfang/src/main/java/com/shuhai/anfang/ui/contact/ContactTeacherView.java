package com.shuhai.anfang.ui.contact;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhai.anfang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-12-14 0014.
 */

public class ContactTeacherView extends LinearLayout{

    @BindView(R.id.txtDept)
    TextView txtDept;
    @BindView(R.id.txtCharge)
    TextView txtCharge;
    @BindView(R.id.txtEmail)
    TextView txtEmail;

    public ContactTeacherView(Context context) {
        this(context, null);
    }

    public ContactTeacherView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_teacher, this, true);
        ButterKnife.bind(view);
    }



}
