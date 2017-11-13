package com.shuhai.anfang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuhai.anfang.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dexing on 2017-11-13 0013.
 */

public class MessageFragment extends BaseFragment {

    private Unbinder unbinder;

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

    }
}
