package com.shuhai.anfang.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.widget.mygridview.MyGridView;
import com.android.widget.view.CircularImageView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.model.HomeGroupItem;

import java.util.ArrayList;
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

    public void bindData(){
        grd_home.setNumColumns(5);
        List<HomeGroupItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeGroupItem item = new HomeGroupItem();
            item.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511168689&di=ea2101517140d09205241b2f268c0b1c&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.csai.cn%2Fimg%2Fnews%2F201505%2F201505211403073341.png");
            item.setTitle("如意宝");
            item.setWebUrl("");
            items.add(item);
        }
        adapter.reloadData(items);
    }

}
