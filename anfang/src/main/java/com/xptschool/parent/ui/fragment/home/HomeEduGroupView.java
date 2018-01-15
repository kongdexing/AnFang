package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.fragment.HomePagerAdapter;
import com.xptschool.parent.ui.mine.BaseInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuhaixinxi on 2018/1/15.
 */

public class HomeEduGroupView extends BaseInfoView {

    @BindView(R.id.llOnline)
    LinearLayout llOnline;
    @BindView(R.id.pager_online)
    ViewPager pager_online;

    public HomeEduGroupView(Context context) {
        this(context, null);
    }

    public HomeEduGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_edu_group, this, true);
        ButterKnife.bind(view);
        llOnline.setVisibility(View.GONE);
    }

    public void initEduOnLine(List<BeanHomeCfg> onlines) {
        if (onlines.size() > 0) {
            llOnline.setVisibility(View.VISIBLE);
            int width = XPTApplication.getInstance().getWindowWidth() / 3;
            int height = width * 3 / 4;

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pager_online.getLayoutParams();
            layoutParams.height = height;
            pager_online.setLayoutParams(layoutParams);

            pager_online.setOffscreenPageLimit(3);
            pager_online.setPageMargin(5);

            List<View> eduViews = new ArrayList<>();
            for (int i = 0; i < onlines.size(); i++) {
                HomeEduView eduView = new HomeEduView(mContext);
                eduView.bindingData(onlines.get(i));
                eduViews.add(eduView);
            }

            HomePagerAdapter adapter = new HomePagerAdapter(eduViews);
            pager_online.setAdapter(adapter);
        } else {
            llOnline.setVisibility(View.GONE);
        }
    }

}
