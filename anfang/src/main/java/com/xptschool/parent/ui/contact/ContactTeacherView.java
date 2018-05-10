package com.xptschool.parent.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.android.widget.view.KenBurnsView;
import com.hyphenate.easeui.EaseConstant;
import com.xptschool.parent.R;
import com.xptschool.parent.model.ContactTeacher;
import com.xptschool.parent.ui.chat.ChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-12-14 0014.
 */

public class ContactTeacherView extends ContactBaseView{

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtAreaName)
    TextView txtAreaName;
    @BindView(R.id.txtSchoolName)
    TextView txtSchoolName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.rlTeacherPhone)
    RelativeLayout rlTeacherPhone;

    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.btnSendMsg)
    Button btnSendMsg;

    public ContactTeacherView(Context context) {
        this(context, null);
    }

    public ContactTeacherView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_teacher, this, true);
        ButterKnife.bind(view);
    }

    public void setTeacherInfo(final ContactTeacher contactTeacher) {
        if (contactTeacher == null) {
            return;
        }

        if (contactTeacher.getSex().equals("1")) {
            imgHead.setImageResource(R.drawable.teacher_man);
        } else {
            imgHead.setImageResource(R.drawable.teacher_woman);
        }

        txtName.setText(contactTeacher.getName());
        txtAreaName.setText(contactTeacher.getA_name());
        txtSchoolName.setText(contactTeacher.getS_name());
        txtEmail.setText(contactTeacher.getEmail());
        txtPhone.setText(contactTeacher.getPhone());
        rlTeacherPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(contactTeacher.getPhone());
            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, contactTeacher.getU_id());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
    }


}
