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
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomePayMentView extends BaseInfoView {

    @BindView(R.id.llHomePay)
    LinearLayout llHomePay;

    @BindView(R.id.llpay1)
    LinearLayout llpay1;
    @BindView(R.id.pay1_title)
    TextView pay1_title;
    @BindView(R.id.pay1_des)
    TextView pay1_des;
    @BindView(R.id.pay1_img)
    ImageView pay1_img;

    @BindView(R.id.llpay2)
    LinearLayout llpay2;
    @BindView(R.id.pay2_title)
    TextView pay2_title;
    @BindView(R.id.pay2_des)
    TextView pay2_des;
    @BindView(R.id.pay2_img)
    ImageView pay2_img;

    public HomePayMentView(Context context) {
        super(context);
    }

    public HomePayMentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_payment, this, true);
        ButterKnife.bind(view);
    }

    public void bindData(List<BeanHomeCfg> homeCfgs) {
        if (homeCfgs.size() > 0) {
            llHomePay.setVisibility(VISIBLE);
            try {
                final BeanHomeCfg homeCfg1 = homeCfgs.get(0);
                if (homeCfg1 != null) {
                    pay1_title.setText(homeCfg1.getTitle());
                    pay1_des.setText(homeCfg1.getMark());
                    ImageLoader.getInstance().displayImage(homeCfg1.getImage(),
                            new ImageViewAware(pay1_img), CommonUtil.getDefaultImageLoaderOption());
                    llpay1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra(ExtraKey.WEB_URL, homeCfg1.getUrl());
                            mContext.startActivity(intent);
                        }
                    });
                }

                final BeanHomeCfg homeCfg2 = homeCfgs.get(1);
                if (homeCfg2 != null) {
                    pay2_title.setText(homeCfg2.getTitle());
                    pay2_des.setText(homeCfg2.getMark());
                    ImageLoader.getInstance().displayImage(homeCfg2.getImage(),
                            new ImageViewAware(pay2_img), CommonUtil.getDefaultImageLoaderOption());
                    llpay2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra(ExtraKey.WEB_URL, homeCfg2.getUrl());
                            mContext.startActivity(intent);
                        }
                    });
                }

            } catch (Exception ex) {
                Log.i(TAG, "bindData: " + ex.getMessage());
            }
        } else {
            llHomePay.setVisibility(GONE);
        }
    }

}
