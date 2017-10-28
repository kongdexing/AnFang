package com.shuhai.anfang.ui.fragment;

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

import com.android.widget.autoviewpager.AutoScrollViewPager;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.model.BeanBanner;
import com.shuhai.anfang.push.BannerHelper;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.rlTipAD)
    RelativeLayout rlTipAD;
    @BindView(R.id.viewPagerTop)
    AutoScrollViewPager viewPagerTop;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.tipTitle)
    TextView tipTitle;

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

//        viewPagerTop.setCycle(true);
//        topAdapter = new MyTopPagerAdapter(this.getContext());
//        viewPagerTop.setAdapter(topAdapter);
        indicator.setViewPager(viewPagerTop);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                if (topBanners.size() > position) {
                    BeanBanner banner = topBanners.get(position);
                    if (banner != null) {
                        tipTitle.setText(banner.getTitle());
                        BannerHelper.postShowBanner(banner, "1");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.i(TAG, "onPageScrollStateChanged: ");
            }
        });
        indicator.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPagerTop != null) {
            viewPagerTop.startAutoScroll();
        }
    }

    @OnClick({R.id.home_homework, R.id.home_alarm, R.id.home_checkin, R.id.home_score,
            R.id.home_leave, R.id.home_location, R.id.home_notice, R.id.home_question})
    void buttonOnClick(View view) {
        switch (view.getId()) {
//            case R.id.home_homework:
//                startActivity(new Intent(getContext(), HomeWorkActivity.class));
//                break;
//            case R.id.home_notice:
//                startActivity(new Intent(getContext(), NoticeActivity.class));
//                break;
//            case R.id.home_question:
//                startActivity(new Intent(getContext(), QuestionActivity.class));
//                break;
//            case R.id.home_location:
//                ((MainActivity) this.getActivity()).resetNavBar();
//                ((MainActivity) this.getActivity()).showMap();
//                break;
//            case R.id.home_score:
//                startActivity(new Intent(getContext(), ScoreActivity.class));
//                break;
//            case R.id.home_checkin:
//                startActivity(new Intent(getContext(), CheckinActivity.class));
//                break;
//            case R.id.home_leave:
//                startActivity(new Intent(getContext(), LeaveActivity.class));
//                break;
//            case R.id.home_alarm:
//                startActivity(new Intent(getContext(), AlarmActivity.class));
//                break;
        }
    }

    @Override
    protected void initData() {
        Log.i(TAG, "HomeFragment initData: ");
    }

    public void reloadTopFragment(List<BeanBanner> banners) {
        Log.i(TAG, "reloadTopFragment: " + banners.size());
//        if (topAdapter != null && banners.size() > 0) {
//            topBanners = banners;
//            BeanBanner banner = banners.get(0);
//            if (banner != null && tipTitle != null) {
//                tipTitle.setText(banner.getTitle());
//            }
//            topAdapter.reloadData(banners);
//        }
        if (viewPagerTop != null) {
            viewPagerTop.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPagerTop.stopAutoScroll();
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
