package com.xptschool.parent.ui.contact;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.xptschool.parent.R;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.ContactSchool;
import com.xptschool.parent.model.ContactStudent;
import com.xptschool.parent.model.ContactTeacher;
import com.xptschool.parent.ui.main.BaseActivity;

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
