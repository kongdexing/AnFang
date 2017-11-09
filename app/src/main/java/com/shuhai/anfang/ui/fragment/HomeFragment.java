package com.shuhai.anfang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.widget.mygridview.MyGridView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.bean.HomeItem;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.model.BeanBanner;
import com.shuhai.anfang.push.BannerHelper;
import com.shuhai.anfang.ui.alarm.AlarmActivity;
import com.shuhai.anfang.ui.checkin.CheckinActivity;
import com.shuhai.anfang.ui.fence.FenceListActivity;
import com.shuhai.anfang.ui.homework.HomeWorkActivity;
import com.shuhai.anfang.ui.main.WebViewActivity;
import com.shuhai.anfang.ui.notice.NoticeActivity;
import com.shuhai.anfang.ui.score.ScoreActivity;
import com.shuhai.anfang.view.autoviewpager.GalleryTransformer;
import com.shuhai.anfang.view.autoviewpager.GlideImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.rlTipAD)
    RelativeLayout rlTipAD;
    @BindView(R.id.topBanner)
    Banner topBanner;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.tipTitle)
    TextView tipTitle;
    List<BeanBanner> advertList = new ArrayList<>();

    @BindView(R.id.grd_school)
    MyGridView grd_school;
    HomeItemGridAdapter itemAdapter;

    private Unbinder unbinder;
    //    private MyTopPagerAdapter topAdapter;
    private List<BeanBanner> topBanners = new ArrayList<>();

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "HomeFragment inflateView: ");
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        Log.i(TAG, "HomeFragment initView: ");
        int width = XPTApplication.getInstance().getWindowWidth();

        int height = width / 2;
        Log.i(TAG, "initView: " + width + "  " + height);

        try {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rlTipAD.getLayoutParams();
            lp.width = width;
            lp.height = height;
            rlTipAD.setLayoutParams(lp);
        } catch (Exception ex) {
            Log.i(TAG, "initView setLayoutParams error: " + ex.getMessage());
        }


        //设置图片加载器
        // 1.设置幕后item的缓存数目
        topBanner.setOffscreenPageLimit(1);
//        topBanner.setImages(listBannerImages);
        topBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        //设置banner动画效果
        topBanner.setPageTransformer(true, new GalleryTransformer(getActivity()));
        //设置自动轮播，默认为true
        topBanner.isAutoPlay(true);
        //设置轮播时间
        topBanner.setDelayTime(3000);
        topBanner.start();
        //banner设置方法全部调用完毕时最后调用
        topBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        topBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BeanBanner banner = advertList.get(position);

                if (banner.getTurn_type().equals("1")) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(ExtraKey.WEB_URL, banner.getUrl());
                    mContext.startActivity(intent);
                    BannerHelper.postShowBanner(banner, "2");
                }
            }
        });

        List<HomeItem> homeItems = new ArrayList<HomeItem>();
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_homework)
                .setTitle(getString(R.string.home_homework))
                .setIntent(new Intent(mContext, HomeWorkActivity.class)));

        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_notice)
                .setTitle(getString(R.string.home_notice))
                .setIntent(new Intent(mContext, NoticeActivity.class)));
        //成绩
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_classes)
                .setTitle(getString(R.string.home_score))
                .setIntent(new Intent(mContext, ScoreActivity.class)));
        //考勤
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_checkin)
                .setTitle(getString(R.string.home_checkin))
                .setIntent(new Intent(mContext, CheckinActivity.class)));

        //报警查询
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_alarm)
                .setTitle(getString(R.string.home_alarm))
                .setIntent(new Intent(mContext, AlarmActivity.class)));

        //电子围栏
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_homework)
                .setTitle(getString(R.string.home_fence))
                .setIntent(new Intent(mContext, FenceListActivity.class))
        );

        homeItems.add(new HomeItem().setIconId(R.drawable.home_notice)
                .setTitle(getString(R.string.home_notice)));
        homeItems.add(new HomeItem().setIconId(R.drawable.home_classes)
                .setTitle(getString(R.string.home_score)));

        itemAdapter = new HomeItemGridAdapter(mContext);

        grd_school.setAdapter(itemAdapter);
        itemAdapter.reloadData(homeItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: startAutoPlay");
        topBanner.startAutoPlay();
        //根据登录状态显示不同icon
        //当状态有更改时才变化Item

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: stopAutoPlay");
        topBanner.stopAutoPlay();
    }

    @Override
    protected void initData() {
        Log.i(TAG, "HomeFragment initData: ");
    }

    public void reloadTopFragment(List<BeanBanner> banners) {

        List<String> listBannerImages = new ArrayList<>();
        List<String> listTitles = new ArrayList<>();

        for (int i = 0; i < banners.size(); i++) {
            listBannerImages.add(banners.get(i).getImg());
            listTitles.add(banners.get(i).getTitle());
            Log.i(TAG, "reloadTopFragment: " + banners.get(i).getImg());
        }
        topBanner.update(listBannerImages, listTitles);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
