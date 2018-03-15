package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.main.WebCommonActivity;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻资讯-item
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeNewsItemView extends BaseInfoView {

    @BindView(R.id.llNewsItem)
    LinearLayout llNewsItem;
    @BindView(R.id.news_img)
    ImageView news_img;
    @BindView(R.id.news_title)
    TextView news_title;
    @BindView(R.id.news_source)
    TextView news_source;

    public HomeNewsItemView(Context context) {
        this(context, null);
    }

    public HomeNewsItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_news_item, this, true);
        ButterKnife.bind(view);
    }

    public void bindData(final BeanHomeCfg homeCfg) {
        if (homeCfg == null) {
            return;
        }

        if (null == homeCfg.getImage() || homeCfg.getImage().isEmpty()) {
            news_img.setVisibility(GONE);
        } else {
            news_img.setVisibility(VISIBLE);
        }

        news_title.setText(homeCfg.getTitle());
        news_source.setText(homeCfg.getSource());

        ImageLoader.getInstance().displayImage(homeCfg.getImage(),
                new ImageViewAware(news_img), CommonUtil.getDefaultImageLoaderOption());
        llNewsItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebCommonActivity.class);
                intent.putExtra(ExtraKey.WEB_URL, homeCfg.getUrl());
                mContext.startActivity(intent);
            }
        });

    }

}
