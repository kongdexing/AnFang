package com.shuhai.anfang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.chat.ContactFragment;
import com.shuhai.anfang.ui.chat.ConversationListFragment;
import com.shuhai.anfang.ui.main.MainActivity;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dexing on 2017-11-13 0013.
 */

public class MessageFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    private Fragment[] fragments;
    private ConversationListFragment conversationListFragment;
    private ContactFragment contactListFragment;
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
        fragments = new Fragment[] { conversationListFragment, contactListFragment};

        FragmentPagerAdapter adapter = new MessageTabAdapter(((MainActivity)mContext).getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        indicator.setViewPager(viewPager);

    }

    class MessageTabAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public MessageTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.icon_msg_notice;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
