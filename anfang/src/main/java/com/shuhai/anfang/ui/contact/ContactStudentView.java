package com.shuhai.anfang.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.android.widget.view.KenBurnsView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.adapter.DividerItemDecoration;
import com.shuhai.anfang.adapter.ScrollLinearLayoutManager;
import com.shuhai.anfang.adapter.WrapContentLinearLayoutManager;
import com.shuhai.anfang.model.ContactParent;
import com.shuhai.anfang.model.ContactStudent;
import com.shuhai.anfang.ui.chat.ChatActivity;
import com.shuhai.anfang.util.ToastUtils;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-12-14 0014.
 */

public class ContactStudentView extends ContactBaseView {

    @BindView(R.id.top_picture)
    KenBurnsView top_picture;
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

        top_picture.setResourceIds(R.drawable.bg_student, R.drawable.bg_student);
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
