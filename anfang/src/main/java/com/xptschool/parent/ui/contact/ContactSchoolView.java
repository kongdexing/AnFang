package com.xptschool.parent.ui.contact;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.model.ContactSchool;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-12-14 0014.
 */

public class ContactSchoolView extends ContactBaseView {

    @BindView(R.id.txtSchoolName)
    TextView txtSchoolName;

    @BindView(R.id.txtAreaName)
    TextView txtAreaName;

    @BindView(R.id.txtAddress)
    TextView txtAddress;

    @BindView(R.id.txtTel)
    TextView txtTel;

    @BindView(R.id.txtMainName)
    TextView txtMainName;
    @BindView(R.id.txtMainPhone)
    TextView txtMainPhone;

    @BindView(R.id.txtSecondName)
    TextView txtSecondName;
    @BindView(R.id.txtSecondPhone)
    TextView txtSecondPhone;

    @BindView(R.id.RlSchoolTel)
    RelativeLayout RlSchoolTel;
    @BindView(R.id.RlMainLeader)
    RelativeLayout RlMainLeader;
    @BindView(R.id.RlViceLeader)
    RelativeLayout RlViceLeader;

    public ContactSchoolView(Context context) {
        this(context, null);
    }

    public ContactSchoolView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_school, this, true);
        ButterKnife.bind(view);
    }

    public void setSchoolInfo(final ContactSchool school) {
        if (school == null) {
            return;
        }
        txtAreaName.setText(school.getA_name());
        txtSchoolName.setText(school.getS_name());
        txtAddress.setText(school.getAddress());
        txtTel.setText(school.getTel());
        RlSchoolTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(school.getTel());
            }
        });

        txtMainName.setText(school.getMain_zrr());
        txtMainPhone.setText(school.getMain_phone());
        RlMainLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(school.getMain_phone());
            }
        });

        txtSecondName.setText(school.getSub_zzr());
        txtSecondPhone.setText(school.getSub_phone());
        RlViceLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(school.getSub_phone());
            }
        });
    }

}
