package com.xptschool.parent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.NewsType;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.ui.chat.ConversationListFragment;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.ui.message.Msg3NewsFragment;

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

    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;

    @BindView(R.id.txt1)
    TextView txt1;
    @BindView(R.id.txt2)
    TextView txt2;

    @BindView(R.id.rlTip)
    RelativeLayout rlTip;

    private ConversationListFragment conversationListFragment;
    private int currIndex = 0;
    private int indicatorWidth = 0;
    private int[][] tipImgs = new int[2][2];
    private MyPagerAdapter adapter;

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

                if (position == 0) {
                    img1.setBackgroundResource(tipImgs[0][1]);
                    img2.setBackgroundResource(tipImgs[1][0]);
                } else if (position == 1) {
                    img1.setBackgroundResource(tipImgs[0][0]);
                    img2.setBackgroundResource(tipImgs[1][1]);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initFragmentTip();

        UserHelper.getInstance().addUserChangeListener(new UserHelper.UserChangeListener() {
            @Override
            public void onUserLoginSuccess() {
                Log.i(TAG, "onUserLoginSuccess: ");
                initFragmentTip();
            }

            @Override
            public void onUserExit() {
                Log.i(TAG, "onUserExit: ");
                initFragmentTip();
            }
        });

    }

    private void initFragmentTip() {
        Fragment[] fragments = null;
        //普通会员，显示新品推荐，致富财经
        txt1.setText(R.string.msg_newproj);
        txt2.setText(R.string.msg_fortune);

        tipImgs[0][0] = R.drawable.icon_msg_xptj_def;
        tipImgs[0][1] = R.drawable.icon_msg_xptj_pre;
        tipImgs[1][0] = R.drawable.icon_msg_cf_def;
        tipImgs[1][1] = R.drawable.icon_msg_cf_pre;

        Msg3NewsFragment msg1 = new Msg3NewsFragment();
        msg1.setNewsType(NewsType.RECOMMEND.toString());

        Msg3NewsFragment msg2 = new Msg3NewsFragment();
        msg2.setNewsType(NewsType.RICH_NEWS.toString());

        fragments = new Fragment[]{msg1, msg2};
        if (adapter == null) {
            adapter = new MyPagerAdapter(((MainActivity) mContext).getSupportFragmentManager());
            viewPager.setAdapter(adapter);
        }
        adapter.setMyFragments(fragments);

//        viewPager.setCurrentItem(0);
        img1.setBackgroundResource(tipImgs[0][1]);
        img2.setBackgroundResource(tipImgs[1][0]);

        tipViewClick(((MainActivity) mContext).findViewById(R.id.llMessage1));

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public ConversationListFragment getConversationListFragment() {
        if (conversationListFragment == null) {
            conversationListFragment = new ConversationListFragment();
        }
        return conversationListFragment;
    }

    @OnClick({R.id.llMessage1, R.id.llMessage2})
    void tipViewClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.llMessage1:
                currIndex = 0;
                viewPager.setCurrentItem(0);
                break;
            case R.id.llMessage2:
                currIndex = 1;
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;
        private long time;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setMyFragments(Fragment[] fragments) {
            time = System.currentTimeMillis();
            this.fragments = fragments;
            try {
                notifyDataSetChanged();
            } catch (Exception ex) {
                Log.i(TAG, "setMyFragments: " + ex.getMessage());
            }
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: " + position);
            return fragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position) + time;
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.length;
        }
    }

}
