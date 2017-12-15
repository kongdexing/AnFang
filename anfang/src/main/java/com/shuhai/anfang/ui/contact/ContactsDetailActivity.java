package com.shuhai.anfang.ui.contact;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.android.widget.view.KenBurnsView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.model.ContactParent;
import com.shuhai.anfang.ui.main.BaseActivity;

import butterknife.BindView;

public class ContactsDetailActivity extends BaseActivity {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.llContent)
    LinearLayout llContent;

    private PopupWindow picPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detial);

        String type = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(ExtraKey.CONTACT_TYPE);
            if (type.equals(ExtraKey.CONTACT_TEACHER)) {
                ContactTeacherView teacherView = new ContactTeacherView(this);
                llContent.addView(teacherView);
//                setTeacherInfo(bundle.get(ExtraKey.CONTACT));
            } else if (type.equals(ExtraKey.CONTACT_SCHOOL)) {
                ContactSchoolView schoolView = new ContactSchoolView(this);
                llContent.addView(schoolView);
//                setSchoolInfo((ContactSchool) bundle.get(ExtraKey.CONTACT));
            } else if (type.equals(ExtraKey.CONTACT_STUDENT)) {
                ContactStudentView studentView = new ContactStudentView(this);
                llContent.addView(studentView);
//                setStudentInfo((ContactStudent) bundle.get(ExtraKey.CONTACT));
            }
        }

        KenBurnsView mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
        mHeaderPicture.setResourceIds(R.drawable.bg_student, R.drawable.bg_student);

    }

//    private void setTeacherInfo(final Object teacher) {
//        if (teacher == null) {
//            return;
//        }
//        setTitle("老师");
//
//        ContactTeacher contactTeacher = (ContactTeacher) teacher;
//
//        if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
//            contactTeacher = (ContactTeacherForTeacher) teacher;
//        } else if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
//            contactTeacher = (ContactTeacher) teacher;
//        }
//
//
//        if (contactTeacher.getSex().equals("1")) {
//            imgHead.setImageResource(R.drawable.teacher_man);
//        } else {
//            imgHead.setImageResource(R.drawable.teacher_woman);
//        }
//
//        txtName.setText(contactTeacher.getName());
////        txtAreaName.setText(contactTeacher.getA_name());
////        txtSchoolName.setText(contactTeacher.getS_name());
////        txtDept.setText(contactTeacher.getD_name());
////        txtEmail.setText(contactTeacher.getEmail());
////        txtCharge.setText(contactTeacher.getCharge().equals("1") ? "是" : "否");
//        txtPhone.setText(contactTeacher.getPhone());
//        rlTeacherPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                call(contactTeacher.getPhone());
//            }
//        });
//
//        Button btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
//        btnSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(ContactsDetailActivity.this, ChatActivity.class);
////                intent.putExtra("userId", contactTeacher.getPhone());
////                startActivity(intent);
////                finish();
//            }
//        });
//    }
//
//    private void setSchoolInfo(final ContactSchool school) {
//        if (school == null) {
//            return;
//        }
//        setTitle("学校信息");
//        imgHead.setVisibility(View.GONE);
//        txtName.setVisibility(View.GONE);
//        txtAreaName.setText(school.getA_name());
//        txtSchoolName.setText(school.getS_name());
//        txtAddress.setText(school.getAddress());
//        txtTel.setText(school.getTel());
//        RlSchoolTel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                call(school.getTel());
//            }
//        });
//
//        txtMainName.setText(school.getMain_zrr());
//        txtMainPhone.setText(school.getMain_phone());
//        RlMainLeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                call(school.getMain_phone());
//            }
//        });
//
//        txtViceName.setText(school.getSub_zzr());
//        txtVicePhone.setText(school.getSub_phone());
//        RlViceLeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                call(school.getSub_phone());
//            }
//        });
//    }
//
//    private void setStudentInfo(ContactStudent student) {
//        if (student == null) {
//            return;
//        }
//        setTitle(student.getStu_name());
//
//        if (student.getSex().equals("1")) {
//            imgHead.setImageResource(R.drawable.student_boy);
//            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_boy);
//            imgSex.setBackgroundResource(R.drawable.male_w);
//        } else {
//            imgHead.setImageResource(R.drawable.student_girl);
//            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_girl);
//            imgSex.setBackgroundResource(R.drawable.female_w);
//        }
//
//        txtAge.setText(student.getAge() + "岁");
//
//        txtName.setText(student.getStu_name());
//        txtBirth_date.setText(student.getBirth_date());
//        txtClassName.setText(student.getG_name() + student.getC_name());
//        List<ContactParent> parents = student.getParent();
//        if (parents == null || parents.size() == 0) {
//            txtParentCount.setText("无家长通讯信息");
//            return;
//        }
//
//        ContactParentAdapter adapter = new ContactParentAdapter(this);
//        adapter.refreshDate(parents);
//        adapter.setPhoneClickListener(new ContactParentAdapter.OnParentPhoneClickListener() {
//            @Override
//            public void onPhoneClickListener(ContactParent parent) {
//                showBottomView(recycleView, parent);
//            }
//        });
//
//        recycleView.setHasFixedSize(true);
//        final WrapContentLinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(this);
//        recycleView.setLayoutManager(mLayoutManager);
//        recycleView.addItemDecoration(new DividerItemDecoration(this,
//                LinearLayoutManager.VERTICAL, R.drawable.line_dotted));
//        recycleView.setAdapter(adapter);
//    }

    public void showBottomView(View view, final ContactParent parent) {
        BottomChatView albumSourceView = new BottomChatView(ContactsDetailActivity.this);
        albumSourceView.setOnBottomChatClickListener(new BottomChatView.OnBottomChatClickListener() {
            @Override
            public void onCallClick() {
                call(parent.getPhone());
                picPopup.dismiss();
            }

            @Override
            public void onChatClick() {
//                Intent intent = new Intent(ContactsDetailActivity.this, ChatActivity.class);
//                intent.putExtra(ExtraKey.CHAT_PARENT, parent);
//                startActivity(intent);
                picPopup.dismiss();
            }

            @Override
            public void onBack() {
                picPopup.dismiss();
            }
        });
        picPopup = new PopupWindow(albumSourceView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        picPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        picPopup.setTouchable(true);
        picPopup.setBackgroundDrawable(new ColorDrawable());
        picPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        backgroundAlpha(0.5f);
        picPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void call(String phone) {
        Log.i(TAG, "call: ");
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
        }
    }

}
