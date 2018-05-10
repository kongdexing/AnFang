package com.xptschool.parent.ui.contact;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.android.widget.view.KenBurnsView;
import com.xptschool.parent.R;
import com.xptschool.parent.model.ContactParent;
import com.xptschool.parent.model.ContactStudent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-12-14 0014.
 */

public class ContactStudentView extends ContactBaseView {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.llInfoBg)
    LinearLayout llInfoBg;
    @BindView(R.id.imgSex)
    ImageView imgSex;
    @BindView(R.id.txtAge)
    TextView txtAge;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtBirth_date)
    TextView txtBirth_date;
    @BindView(R.id.txtClassName)
    TextView txtClassName;

    @BindView(R.id.llParentContent)
    LinearLayout llParentContent;
    @BindView(R.id.txtParentCount)
    TextView txtParentCount;

    public ContactStudentView(Context context) {
        this(context, null);
    }

    public ContactStudentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_student, this, true);
        ButterKnife.bind(view);
    }

    public void setStudentInfo(ContactStudent student) {
        if (student == null) {
            return;
        }

        if (student.getSex().equals("1")) {
            imgHead.setImageResource(R.drawable.student_boy);
            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_boy);
            imgSex.setBackgroundResource(R.drawable.male_w);
        } else {
            imgHead.setImageResource(R.drawable.student_girl);
            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_girl);
            imgSex.setBackgroundResource(R.drawable.female_w);
        }

        txtAge.setText(student.getAge() + "岁");

        txtName.setText(student.getStu_name());
        txtBirth_date.setText(student.getBirth_date());
        txtClassName.setText(student.getG_name() + student.getC_name());
        List<ContactParent> parents = student.getParent();
        if (parents == null || parents.size() == 0) {
            txtParentCount.setText("无家长通讯信息");
            return;
        }

        for (int i = 0; i < parents.size(); i++) {
            final ContactParent parent = parents.get(i);
            ContactStuParentView parentView = new ContactStuParentView(mContext);
            parentView.setParentData(parent);
            llParentContent.addView(parentView);
        }
    }



}
