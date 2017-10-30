package com.shuhai.anfang.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.CookieUtil;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.BroadcastAction;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.imsdroid.Engine;
import com.shuhai.anfang.model.BeanBanner;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.fragment.BaseFragment;
import com.shuhai.anfang.ui.fragment.HomeFragment;
import com.shuhai.anfang.ui.fragment.MapFragment;
import com.shuhai.anfang.ui.fragment.MineFragment;
import com.shuhai.anfang.ui.login.LoginActivity;
import com.shuhai.anfang.util.ParentUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseMainActivity {

    private List<BaseFragment> fragmentList;
    private BaseFragment mCurrentFgt, homeFragment, mapFragment, mineFragment;
    private FrameLayout fl_Content;
    private ImageButton homeBtn, mapBtn, mineBtn;
    private FragmentManager mFgtManager;
    private FragmentTransaction mFgtTransaction;
    private long mExitTime;
    @BindView(R.id.txtUnReadNum)
    TextView txtUnReadNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        showImgBack(false);

        initView();
        initData();

    }

    private void initView() {
        fl_Content = (FrameLayout) findViewById(R.id.fl_Content);
        homeBtn = (ImageButton) findViewById(R.id.nav_home);
        mapBtn = (ImageButton) findViewById(R.id.nav_track);
        mineBtn = (ImageButton) findViewById(R.id.nav_mine);

        IntentFilter filter = new IntentFilter(BroadcastAction.RELOAD_BANNER);
        this.registerReceiver(MyBannerReceiver, filter);
    }

    private void initData() {
        Log.i(TAG, "initData: ");
        fragmentList = new ArrayList<>();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        mineFragment = new MineFragment();

        fragmentList.add(0, homeFragment);
        fragmentList.add(1, mapFragment);
        fragmentList.add(2, mineFragment);

        mFgtManager = getSupportFragmentManager();
        setInitialState();

//        getBanners();
    }

    private void setInitialState() {
        homeBtn.setSelected(true);

        mFgtTransaction = mFgtManager.beginTransaction();
        mCurrentFgt = fragmentList.get(0);
        mFgtTransaction.add(R.id.fl_Content, mCurrentFgt).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String cookie = CookieUtil.getCookie();
        if (cookie == null || cookie.isEmpty()) {
            Log.i(TAG, "onResume: cookie is null");
            String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
            String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
            login(userName, password);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && permissions.length > 0) {
            Log.i(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void showTrackFragment() {
        Log.i(TAG, "showTrackFragment: ");
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationDenied() {
        Log.i(TAG, "onLocationDenied: ");
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, R.string.permission_location_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        Log.i(TAG, "showRationaleForLocation: ");
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationNeverAskAgain() {
        Log.i(TAG, "onLocationNeverAskAgain: ");
        Toast.makeText(this, R.string.permission_location_never_askagain, Toast.LENGTH_SHORT).show();
    }

    @OnLongClick({R.id.nav_home, R.id.nav_track, R.id.nav_mine})
    public boolean buttonOnLongClick(View view) {
        viewClick(view);
        return true;
    }

    @OnClick({R.id.nav_home, R.id.nav_track, R.id.nav_mine})
    public void viewOnclick(View view) {
        viewClick(view);
    }

    public void viewClick(View view) {
        resetNavBar();
        switch (view.getId()) {
            case R.id.nav_home:
                homeBtn.setSelected(true);
                addOrReplaceFgt(0);
                break;
            case R.id.nav_track:
                showMap();
                break;
            case R.id.nav_mine:
                mineBtn.setSelected(true);
                addOrReplaceFgt(2);
                break;
        }
    }

    public void resetNavBar() {
        homeBtn.setSelected(false);
        mapBtn.setSelected(false);
        mineBtn.setSelected(false);
    }

    public void showMap() {
        MainActivityPermissionsDispatcher.showTrackFragmentWithCheck(this);
        mapBtn.setSelected(true);
        addOrReplaceFgt(1);
    }

    private void addOrReplaceFgt(int position) {
        if (mCurrentFgt == fragmentList.get(position)) {
            Log.e(TAG, "当前Fragment相同，不需要切换");
            return;
        }

        mFgtTransaction = mFgtManager.beginTransaction();
        if (fragmentList.get(position).isAdded()) {
            mFgtTransaction.hide(mCurrentFgt).show(fragmentList.get(position)).commit();
        } else {
            mFgtTransaction.hide(mCurrentFgt).add(R.id.fl_Content, fragmentList.get(position)).show(fragmentList.get(position)).commit();
        }
        Log.e(TAG, "Replace");
        mCurrentFgt.onPause();
        mCurrentFgt = fragmentList.get(position);
        mCurrentFgt.onResume();
    }

    private void login(final String account, final String password) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
                new VolleyHttpParamsEntity()
                        .addParam("username", account)
                        .addParam("password", password)
                        .addParam("type", "4"),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        switch (httpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject jsonData = new JSONObject(httpResult.getData().toString());
                                    CommonUtil.initBeanStudentByHttpResult(jsonData.getJSONArray("stuData").toString());
                                    CommonUtil.initParentInfoByHttpResult(jsonData.getJSONObject("login").toString(), account);
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: error " + ex.getMessage());
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra(ExtraKey.LOGIN_ORIGIN, "0");
                                    startActivity(intent);
                                }
                                break;
                            default:
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra(ExtraKey.LOGIN_ORIGIN, "0");
                                startActivity(intent);
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(ExtraKey.LOGIN_ORIGIN, "0");
                        startActivity(intent);
                    }
                });
    }

    private void getBanners() {
        Log.i(TAG, "getBanners: ");
        String url = HttpAction.HOME_Banner;
        VolleyHttpService.getInstance().sendPostRequest(url, new VolleyHttpParamsEntity()
                .addParam("s_id", ParentUtil.getStuSid()), new MyVolleyRequestListener() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                super.onResponse(volleyHttpResult);
                switch (volleyHttpResult.getStatus()) {
                    case HttpAction.SUCCESS:
                        try {
                            String info = volleyHttpResult.getData().toString();
                            Log.i(TAG, "onResponse: data " + info);
                            Gson gson = new Gson();
                            List<BeanBanner> banners = gson.fromJson(info, new TypeToken<List<BeanBanner>>() {
                            }.getType());
                            if (banners.size() > 0) {
                                GreenDaoHelper.getInstance().insertBanner(banners);
                            }
                            Log.i(TAG, "onResponse: size " + banners.size());
                            if (homeFragment != null) {
                                ((HomeFragment) homeFragment).reloadTopFragment(banners);
                            }
                        } catch (Exception ex) {
                            Log.i(TAG, "onResponse: error " + ex.getMessage());
                            //错误
                            if (homeFragment != null) {
                                ((HomeFragment) homeFragment).reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
                            }
                        }
                        break;
                }

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (homeFragment != null) {
                    ((HomeFragment) homeFragment).reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
                }
            }
        });
    }

    BroadcastReceiver MyBannerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastAction.RELOAD_BANNER)) {
                getBanners();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            this.unregisterReceiver(MyBannerReceiver);
        } catch (Exception ex) {

        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, R.string.toast_exit, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            if (!Engine.getInstance().stop()) {
                Log.e(TAG, "Failed to stop engine");
            }
            super.onBackPressed();
        }
    }
}