package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 快乐成长
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeHappyGrowView extends BaseInfoView {

    @BindView(R.id.llHappy)
    LinearLayout llHappy;

    @BindView(R.id.ll_goods_1)
    LinearLayout ll_goods_1;
    @BindView(R.id.goods1_title)
    TextView goods1_title;
    @BindView(R.id.goods1_des)
    TextView goods1_des;
    @BindView(R.id.goods1_img)
    ImageView goods1_img;

    @BindView(R.id.ll_goods_2)
    LinearLayout ll_goods_2;
    @BindView(R.id.goods2_title)
    TextView goods2_title;
    @BindView(R.id.goods2_des)
    TextView goods2_des;
    @BindView(R.id.goods2_img)
    ImageView goods2_img;

    @BindView(R.id.ll_goods_3)
    LinearLayout ll_goods_3;
    @BindView(R.id.goods3_title)
    TextView goods3_title;
    @BindView(R.id.goods3_des)
    TextView goods3_des;
    @BindView(R.id.goods3_img)
    ImageView goods3_img;

    public HomeHappyGrowView(Context context) {
        super(context);
    }

    public HomeHappyGrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_happy, this, true);
        ButterKnife.bind(view);
    }

    public void bindData(List<BeanHomeCfg> children_goods) {
        if (children_goods.size() > 0) {
            llHappy.setVisibility(VISIBLE);
            try {
                final BeanHomeCfg homeCfg1 = children_goods.get(0);
                if (homeCfg1 != null) {
                    goods1_title.setText(homeCfg1.getTitle());
                    goods1_des.setText(homeCfg1.getMark());
                    ImageLoader.getInstance().displayImage(homeCfg1.getImage(),
                            new ImageViewAware(goods1_img), CommonUtil.getDefaultImageLoaderOption());
                }
                ll_goods_1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, homeCfg1.getUrl());
                        mContext.startActivity(intent);
                    }
                });

                final BeanHomeCfg homeCfg2 = children_goods.get(1);
                if (homeCfg2 != null) {
                    goods2_title.setText(homeCfg2.getTitle());
                    goods2_des.setText(homeCfg2.getMark());
                    ImageLoader.getInstance().displayImage(homeCfg2.getImage(),
                            new ImageViewAware(goods2_img), CommonUtil.getDefaultImageLoaderOption());
                }
                ll_goods_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, homeCfg2.getUrl());
                        mContext.startActivity(intent);
                    }
                });

                final BeanHomeCfg homeCfg3 = children_goods.get(2);
                if (homeCfg3 != null) {
                    goods3_title.setText(homeCfg3.getTitle());
                    goods3_des.setText(homeCfg3.getMark());
                    ImageLoader.getInstance().displayImage(homeCfg3.getImage(),
                            new ImageViewAware(goods3_img), CommonUtil.getDefaultImageLoaderOption());
                }
                ll_goods_3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, homeCfg3.getUrl());
                        mContext.startActivity(intent);
                    }
                });
            } catch (Exception ex) {
                Log.i(TAG, "bindData: ");
            }
        } else {
            llHappy.setVisibility(GONE);
        }

    }


}
