package com.shuhai.anfang.ui.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.fragment.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-11-23 0023.
 */

public class ContactFragment extends BaseFragment {


    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void initData() {

    }
}
