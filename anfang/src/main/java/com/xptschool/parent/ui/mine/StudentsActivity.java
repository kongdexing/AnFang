package com.xptschool.parent.ui.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.xptschool.parent.R;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StudentsActivity extends BaseActivity {

    @BindView(R.id.indicator)
    TitlePageIndicator indicator;

    @BindView(R.id.pager)
    ViewPager viewPager;

    private List<StudentFragment> listFrags = new ArrayList<>();
    private List<BeanClass> myClass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        setTitle(R.string.mine_student);
        initData();
    }

    private void initData() {
        String c_id = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            c_id = bundle.getString(ExtraKey.CLASS_ID);
        }
        Log.i(TAG, "initData: c_id " + c_id);
        if (c_id != null) {
            myClass.add(GreenDaoHelper.getInstance().getClassById(c_id));
        } else {
            myClass = GreenDaoHelper.getInstance().getAllClass();
        }
        Log.i(TAG, "initData: class size " + myClass.size());

        FragmentPagerAdapter adapter = new StudentFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(myClass.size());

        indicator.setViewPager(viewPager);
        indicator.setCurrentItem(0);
    }

    class StudentFragmentAdapter extends FragmentPagerAdapter {
        public StudentFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StudentFragment fragment = null;
            if (position >= listFrags.size()) {
                fragment = new StudentFragment();
                fragment.setClass(myClass.get(position));
                listFrags.add(position, fragment);
            } else {
                fragment = listFrags.get(position);
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return myClass.get(position).getName().toUpperCase();
        }

        @Override
        public int getCount() {
            return myClass.size();
        }
    }

}
