package com.hyphenate.easeui.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseMyTitleBar;

public abstract class EaseBaseFragment extends Fragment{

    public String TAG = EaseBaseFragment.class.getSimpleName();
    protected EaseMyTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        titleBar = (EaseMyTitleBar) getView().findViewById(R.id.title_bar);
        
        initView();
        setUpView();
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    protected abstract void initView();
    
    protected abstract void setUpView();


}
