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
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeShopItemView extends BaseInfoView {

    @BindView(R.id.llShopItem)
    LinearLayout llShopItem;
    @BindView(R.id.goods_img)
    ImageView goods_img;
    @BindView(R.id.goods_title)
    TextView goods_title;
    @BindView(R.id.goods_address)
    TextView goods_address;
    @BindView(R.id.goods_price)
    TextView goods_price;

    public HomeShopItemView(Context context) {
        this(context, null);
    }

    public HomeShopItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_shop_item, this, true);
        ButterKnife.bind(view);
    }

    public void bindData(final BeanHomeCfg homeCfg) {
        if (homeCfg == null) {
            return;
        }


        int width = XPTApplication.getInstance().getWindowWidth() / 3;
        int height = width;

        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,height);
        goods_img.setLayoutParams(layoutParams);

        ImageLoader.getInstance().displayImage(homeCfg.getImage(),
                new ImageViewAware(goods_img), CommonUtil.getDefaultImageLoaderOption());
        goods_title.setText(homeCfg.getTitle());
        goods_address.setText(homeCfg.getMark());
        goods_price.setText("￥ " + homeCfg.getPrice());
        llShopItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(ExtraKey.WEB_URL, homeCfg.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

}
