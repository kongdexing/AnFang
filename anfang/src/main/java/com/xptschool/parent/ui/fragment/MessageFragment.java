package com.xptschool.parent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.ui.chat.ConversationListFragment;
import com.xptschool.parent.ui.contact.ContactFragment;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dexing on 2017-11-13 0013.
 */

public class MessageFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.llNotify)
    LinearLayout llNotify;
    @BindView(R.id.llContact)
    LinearLayout llContact;
    @BindView(R.id.rlTip)
    RelativeLayout rlTip;

    private Fragment[] fragments;
    private ConversationListFragment conversationListFragment;
    private ContactFragment contactListFragment;
    private int currIndex = 0;
    private int indicatorWidth = 0;

    public MessageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void initData() {
        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactFragment();
        fragments = new Fragment[]{conversationListFragment, contactListFragment};

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rlTip.getLayoutParams();
        indicatorWidth = XPTApplication.getInstance().getWindowWidth() / 2;
        params.width = indicatorWidth;
        rlTip.setLayoutParams(params);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Animation animation = null;
                switch (position) {
                    case 0:
                        animation = new TranslateAnimation(indicatorWidth, 0, 0, 0);
                        break;
                    case 1:
                        animation = new TranslateAnimation(0, indicatorWidth, 0, 0);
                        break;
                }
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                rlTip.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FragmentPagerAdapter adapter = new MyPagerAdapter(((MainActivity) mContext).getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        if (!EMClient.getInstance().isLoggedInBefore()) {
            //login
            ToastUtils.showToast(mContext, " not isLoggedInBefore");
        } else {
            ToastUtils.showToast(mContext, "Ease isLoggedInBefore");
        }
    }

    @OnClick({R.id.llNotify, R.id.llContact})
    void tipViewClick(View view) {
        switch (view.getId()) {
            case R.id.llNotify:
                currIndex = 0;
                viewPager.setCurrentItem(0);
                break;
            case R.id.llContact:
                currIndex = 1;
                viewPager.setCurrentItem(1);
                break;

        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

}
