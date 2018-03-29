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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.NewsType;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.ui.chat.ConversationListFragment;
import com.xptschool.parent.ui.contact.ContactFragment;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.ui.message.Msg1NoticeFragment;
import com.xptschool.parent.ui.message.Msg2NotifyFragment;
import com.xptschool.parent.ui.message.Msg3NewsFragment;
import com.xptschool.parent.ui.message.Msg4NewsFragment;

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

    private Fragment[] fragments;
    private ConversationListFragment conversationListFragment;
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
        UserType type = XPTApplication.getInstance().getCurrent_user_type();
        if (UserType.PARENT.equals(type) || UserType.TEACHER.equals(type)) {
            //显示通讯录，通知
            //改变头部 view
            txt1.setText(R.string.msg_contacts);
            txt2.setText(R.string.msg_notify);
            conversationListFragment = new ConversationListFragment();
            ContactFragment contactListFragment = new ContactFragment();
            fragments = new Fragment[]{contactListFragment, conversationListFragment};

        } else if (UserType.COMPANY.equals(type) || UserType.PROXY.equals(type) ||
                UserType.CITYPROXY.equals(type)) {
            //公告，通知
            txt1.setText(R.string.msg_callboard);
            txt2.setText(R.string.msg_notify);

            Msg1NoticeFragment msg1 = new Msg1NoticeFragment();
            Msg2NotifyFragment msg2 = new Msg2NotifyFragment();

            fragments = new Fragment[]{msg1, msg2};
        } else {
            //普通会员，显示新品推荐，致富财经
            txt1.setText(R.string.msg_newproj);
            txt2.setText(R.string.msg_fortune);

            Msg3NewsFragment msg1 = new Msg3NewsFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putString("type", NewsType.RECOMMEND.toString());
            msg1.setArguments(bundle1);

            Msg3NewsFragment msg2 = new Msg3NewsFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putString("type", NewsType.RICH_NEWS.toString());
            msg2.setArguments(bundle2);

            fragments = new Fragment[]{msg1, msg2};
        }


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
            return fragments == null ? 0 : fragments.length;
        }
    }

}
