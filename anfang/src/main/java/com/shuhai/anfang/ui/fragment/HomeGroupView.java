package com.shuhai.anfang.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.widget.mygridview.MyGridView;
import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.model.BeanHomeCfg;
import com.shuhai.anfang.model.BeanHomeCfgChild;
import com.shuhai.anfang.model.GreenDaoHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-11-13 0013.
 */

public class HomeGroupView extends LinearLayout {

    @BindView(R.id.img_group_logo)
    CircularImageView img_group_logo;

    @BindView(R.id.txt_group_title)
    TextView txt_group_title;

    @BindView(R.id.grd_home)
    MyGridView grd_home;
    HomeGroupItemGridAdapter adapter ;

    public HomeGroupView(Context context) {
        this(context,null);
    }

    public HomeGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_home_group, this, true);
        ButterKnife.bind(view);
        adapter = new HomeGroupItemGridAdapter(context);
        grd_home.setAdapter(adapter);
    }

    public void bindData(BeanHomeCfg homeCfg){
        grd_home.setNumColumns(Integer.parseInt(homeCfg.getCell()));
        ImageLoader.getInstance().displayImage(homeCfg.getImg(),
                new ImageViewAware(img_group_logo), CommonUtil.getDefaultImageLoaderOption());
        txt_group_title.setText(homeCfg.getTitle());
        List<BeanHomeCfgChild> children = GreenDaoHelper.getInstance().getHomeChildCfgById(homeCfg.getId());
        adapter.reloadData(children);
    }

}
