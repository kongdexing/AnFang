package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.main.WebCommonActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 教育培训
 * Created by shuhaixinxi on 2018/1/13.
 */
public class HomeEduView extends BaseInfoView {

    @BindView(R.id.rlHomeItem)
    RelativeLayout rlHomeItem;
    @BindView(R.id.optionImg)
    ImageView optionImg;
    @BindView(R.id.optionText)
    TextView optionText;

    public HomeEduView(Context context) {
        this(context, null);
    }

    public HomeEduView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_edu, this, true);
        ButterKnife.bind(view);
    }

    public void bindingData(final BeanHomeCfg homeCfg) {
        ImageLoader.getInstance().displayImage(homeCfg.getImage(),
                new ImageViewAware(optionImg), CommonUtil.getDefaultImageLoaderOption());
        optionText.setText(homeCfg.getTitle());
        rlHomeItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebCommonActivity.class);
                intent.putExtra(ExtraKey.WEB_URL, homeCfg.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

}
