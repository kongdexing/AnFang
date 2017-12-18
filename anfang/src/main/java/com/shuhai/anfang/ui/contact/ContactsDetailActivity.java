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
import com.shuhai.anfang.model.ContactSchool;
import com.shuhai.anfang.model.ContactStudent;
import com.shuhai.anfang.model.ContactTeacher;
import com.shuhai.anfang.ui.main.BaseActivity;

import butterknife.BindView;

public class ContactsDetailActivity extends BaseActivity {

    @BindView(R.id.llContent)
    LinearLayout llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detial);
        String type = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(ExtraKey.CONTACT_TYPE);
            if (type.equals(ExtraKey.CONTACT_TEACHER)) {
                setTitle("个人资料");
                ContactTeacherView teacherView = new ContactTeacherView(this);
                teacherView.setTeacherInfo((ContactTeacher) bundle.getSerializable(ExtraKey.CONTACT));
                llContent.addView(teacherView);
            } else if (type.equals(ExtraKey.CONTACT_SCHOOL)) {
                setTitle("学校信息");
                ContactSchoolView schoolView = new ContactSchoolView(this);
                schoolView.setSchoolInfo((ContactSchool) bundle.getSerializable(ExtraKey.CONTACT));
                llContent.addView(schoolView);
            } else if (type.equals(ExtraKey.CONTACT_STUDENT)) {
                setTitle("个人资料");
                ContactStudentView studentView = new ContactStudentView(this);
                studentView.setStudentInfo((ContactStudent)bundle.getSerializable(ExtraKey.CONTACT));
                llContent.addView(studentView);
            }
        }

    }

}
