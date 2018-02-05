package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.fragment.HomePagerAdapter;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻资讯
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeNewsView extends BaseInfoView {

    @BindView(R.id.llHomeNews)
    LinearLayout llHomeNews;

    @BindView(R.id.txtGroupName)
    TextView txtGroupName;
    @BindView(R.id.txtMore)
    TextView txtMore;

    @BindView(R.id.pager_news)
    ViewPager pager_news;

    public HomeNewsView(Context context) {
        super(context);
    }

    public HomeNewsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_news, this, true);
        ButterKnife.bind(view);

        txtMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newsIntent = new Intent(mContext, WebViewActivity.class);
                newsIntent.putExtra(ExtraKey.WEB_URL, "http://school.xinpingtai.com/edunews/");
                mContext.startActivity(newsIntent);
            }
        });
    }

    public void bindData(List<BeanHomeCfg> homeCfgs) {
        if (homeCfgs.size() > 0) {
            llHomeNews.setVisibility(VISIBLE);

            txtGroupName.setText(homeCfgs.get(0).getProduct_name());

            int width = XPTApplication.getInstance().getWindowWidth() / 3;
            int height = width * 4 / 3;

//            LayoutParams layoutParams = (LayoutParams) pager_news.getLayoutParams();
//            layoutParams.height = height;
//            pager_news.setLayoutParams(layoutParams);
//
//            pager_news.setOffscreenPageLimit(3);
//            pager_news.setPageMargin(5);
//
//            List<View> eduViews = new ArrayList<>();
//            for (int i = 0; i < homeCfgs.size(); i++) {
//                HomeShopItemView itemView = new HomeShopItemView(mContext);
//                itemView.bindData(homeCfgs.get(i));
//                eduViews.add(itemView);
//            }
//
//            HomePagerAdapter adapter = new HomePagerAdapter(eduViews);
//            pager_news.setAdapter(adapter);
        } else {
            llHomeNews.setVisibility(GONE);
        }
    }

}
