package com.shuhai.anfang.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.android.widget.view.KenBurnsView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.model.ContactSchool;
import com.shuhai.anfang.model.ContactTeacher;
import com.shuhai.anfang.ui.chat.ChatActivity;
import com.shuhai.anfang.ui.main.BaseActivity;

import butterknife.BindView;

public class ContactsDetailActivity extends BaseActivity {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.txtAreaName)
    TextView txtAreaName;

    @BindView(R.id.txtSchoolName)
    TextView txtSchoolName;

    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.rlTeacherPhone)
    RelativeLayout rlTeacherPhone;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtDept)
    TextView txtDept;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.txtCharge)
    TextView txtCharge;

    @BindView(R.id.llTeacher)
    LinearLayout llTeacher;

    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtTel)
    TextView txtTel;
    @BindView(R.id.llSchool)
    LinearLayout llSchool;
    @BindView(R.id.txtMainName)
    TextView txtMainName;
    @BindView(R.id.txtMainPhone)
    TextView txtMainPhone;

    @BindView(R.id.txtViceName)
    TextView txtViceName;
    @BindView(R.id.txtVicePhone)
    TextView txtVicePhone;

    @BindView(R.id.RlSchoolTel)
    RelativeLayout RlSchoolTel;
    @BindView(R.id.RlMainLeader)
    RelativeLayout RlMainLeader;
    @BindView(R.id.RlViceLeader)
    RelativeLayout RlViceLeader;

    String currentPhone;

    private ContactTeacher contactTeacher;
    private ContactSchool contactSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detial);

        String type = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(ExtraKey.CONTACT_TYPE);
            if (type.equals(ExtraKey.CONTACT_TEACHER)) {
                llTeacher.setVisibility(View.VISIBLE);
                llSchool.setVisibility(View.GONE);
                setTeacherInfo((ContactTeacher) bundle.get(ExtraKey.CONTACT));
            } else {
                llTeacher.setVisibility(View.GONE);
                llSchool.setVisibility(View.VISIBLE);
                rlTeacherPhone.setVisibility(View.GONE);
                setSchoolInfo((ContactSchool) bundle.get(ExtraKey.CONTACT));
            }
        }

        KenBurnsView mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
        mHeaderPicture.setResourceIds(R.drawable.bg_student, R.drawable.bg_student);

    }

    private void setTeacherInfo(final ContactTeacher teacher) {
        if (teacher == null) {
            return;
        }
        contactTeacher = teacher;

        setTitle("老师");
        if (teacher.getSex().equals("1")) {
            imgHead.setImageResource(R.drawable.teacher_man);
        } else {
            imgHead.setImageResource(R.drawable.teacher_woman);
        }

        txtName.setText(teacher.getName());
        txtAreaName.setText(teacher.getA_name());
        txtSchoolName.setText(teacher.getS_name());
        txtDept.setText(teacher.getD_name());
        txtEmail.setText(teacher.getEmail());
        txtCharge.setText(teacher.getCharge().equals("1") ? "是" : "否");
        txtPhone.setText(teacher.getPhone());
        rlTeacherPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPhone = teacher.getPhone();
                call();
            }
        });

        Button btnSendMsg = (Button)findViewById(R.id.btnSendMsg);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsDetailActivity.this, ChatActivity.class);
                intent.putExtra("userId", contactTeacher.getPhone());
                startActivity(intent);
                finish();
            }
        });
    }

    private void setSchoolInfo(final ContactSchool school) {
        if (school == null) {
            return;
        }
        setTitle("学校信息");
        imgHead.setVisibility(View.GONE);
        txtName.setVisibility(View.GONE);
        txtAreaName.setText(school.getA_name());
        txtSchoolName.setText(school.getS_name());
        txtAddress.setText(school.getAddress());
        txtTel.setText(school.getTel());
        RlSchoolTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPhone = school.getTel();
                call();
            }
        });

        txtMainName.setText(school.getMain_zrr());
        txtMainPhone.setText(school.getMain_phone());
        RlMainLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPhone = school.getMain_phone();
                call();
            }
        });

        txtViceName.setText(school.getSub_zzr());
        txtVicePhone.setText(school.getSub_phone());
        RlViceLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPhone = school.getSub_phone();
                call();
            }
        });
    }

    private void call() {
        Log.i(TAG, "call: ");
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + currentPhone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
        }
    }

}
