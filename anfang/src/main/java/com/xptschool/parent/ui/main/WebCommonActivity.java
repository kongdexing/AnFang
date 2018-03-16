package com.xptschool.parent.ui.main;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.xptschool.parent.R;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.ui.web.AgentWebFragment;
import com.xptschool.parent.ui.web.FragmentKeyDown;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebCommonActivity extends AppCompatActivity {

    @BindView(R.id.container_framelayout)
    FrameLayout mFrameLayout;

    private FragmentManager mFragmentManager;
    private AgentWebFragment mAgentWebFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_common);
        ButterKnife.bind(this);

        mFragmentManager = this.getSupportFragmentManager();

        String webUrl = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            webUrl = bundle.getString(ExtraKey.WEB_URL);

            String title = bundle.getString(ExtraKey.WEB_TITLE);
            if (title != null && !title.isEmpty()) {
                setTitle(title);
            }
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;
        ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//				mBundle.putString(AgentWebFragment.URL_KEY, "https://m.vip.com/?source=www&jump_https=1");
        mBundle.putString(AgentWebFragment.URL_KEY, webUrl);
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
