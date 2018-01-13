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
 * 校园购
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeShopView extends BaseInfoView {

    @BindView(R.id.llHomeShop)
    LinearLayout llHomeShop;
    @BindView(R.id.pager_shop)
    ViewPager pager_shop;

    public HomeShopView(Context context) {
        super(context);
    }

    public HomeShopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_shop, this, true);
        ButterKnife.bind(view);
    }

    public void bindData(List<BeanHomeCfg> homeCfgs) {
        if (homeCfgs.size() > 0) {
            llHomeShop.setVisibility(VISIBLE);
            int width = XPTApplication.getInstance().getWindowWidth() / 3;
            int height = width * 3 / 4;

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pager_shop.getLayoutParams();
            layoutParams.height = height;
            pager_shop.setLayoutParams(layoutParams);

            pager_shop.setOffscreenPageLimit(3);
            pager_shop.setPageMargin(5);

            List<View> eduViews = new ArrayList<>();
            for (int i = 0; i < homeCfgs.size(); i++) {
                HomeShopItemView itemView = new HomeShopItemView(mContext);
                itemView.bindData(homeCfgs.get(i));
                eduViews.add(itemView);
            }

            HomePagerAdapter adapter = new HomePagerAdapter(eduViews);
            pager_shop.setAdapter(adapter);
        } else {
            llHomeShop.setVisibility(VISIBLE);
        }
    }

}
