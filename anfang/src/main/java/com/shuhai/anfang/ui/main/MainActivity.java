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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.CookieUtil;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.BroadcastAction;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.fragment.BaseFragment;
import com.shuhai.anfang.ui.fragment.HomeFragment;
import com.shuhai.anfang.ui.fragment.MapFragment;
import com.shuhai.anfang.ui.fragment.MessageFragment;
import com.shuhai.anfang.ui.fragment.MineFragment;
import com.shuhai.anfang.ui.login.LoginActivity;
import com.shuhai.anfang.util.ToastUtils;

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
public class MainActivity extends BaseMainActivity implements BDLocationListener {

    private List<BaseFragment> fragmentList;
    private BaseFragment mCurrentFgt, homeFragment, mapFragment, messageFragment, mineFragment;
    private FrameLayout fl_Content;
    private ImageButton homeBtn, mapBtn, messageBtn, mineBtn;
    private FragmentManager mFgtManager;
    private FragmentTransaction mFgtTransaction;
    private long mExitTime;
    @BindView(R.id.txtUnReadNum)
    TextView txtUnReadNum;
    public LocationClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        initView();
        initData();

        MainActivityPermissionsDispatcher.onLocationAllowWithCheck(this);
    }

    private void initView() {
        fl_Content = (FrameLayout) findViewById(R.id.fl_Content);
        homeBtn = (ImageButton) findViewById(R.id.nav_home);
        mapBtn = (ImageButton) findViewById(R.id.nav_track);
        messageBtn = (ImageButton) findViewById(R.id.nav_message);
        mineBtn = (ImageButton) findViewById(R.id.nav_mine);

        IntentFilter filter = new IntentFilter(BroadcastAction.RELOAD_BANNER);
        this.registerReceiver(MyBannerReceiver, filter);

        // 定位初始化
        mLocClient = new LocationClient(this);
    }

    private void initData() {
        Log.i(TAG, "initData: ");
        fragmentList = new ArrayList<>();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        messageFragment = new MessageFragment();
        mineFragment = new MineFragment();

        fragmentList.add(0, homeFragment);
        fragmentList.add(1, mapFragment);
        fragmentList.add(2, messageFragment);
        fragmentList.add(3, mineFragment);

        mFgtManager = getSupportFragmentManager();
        setInitialState();

        //login
        String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
        String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");

        if (!userName.isEmpty() && !password.isEmpty()) {
            login(userName, password,
                    SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_TYPE, "").toString(),
                    new DefaultRetryPolicy(4 * 1000, 0, 1));
        }
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
            if (!userName.isEmpty() || !password.isEmpty()) {
                login(userName, password, SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_TYPE, "").toString());
            }
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
    void onLocationAllow() {
        Log.i(TAG, "onLocationAllow: ");
        try {
            mLocClient.registerLocationListener(this);
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            mLocClient.setLocOption(option);
            mLocClient.start();
        } catch (Exception ex) {
            Log.e(TAG, "onLocationAllow: " + ex.getMessage());
        }
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

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
//        ToastUtils.showToast(this, bdLocation.getCityCode() + bdLocation.getCity());
        Log.i(TAG, "onReceiveLocation: " + bdLocation.getCountryCode() + bdLocation.getProvince());
        Log.i(TAG, "onReceiveLocation: " + bdLocation.getCity());
        //保存本地城市
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_CITY, bdLocation.getCity());
        if (homeFragment != null) {
            ((HomeFragment) homeFragment).getBanners();
        }
        mLocClient.stop();
    }

    @OnLongClick({R.id.nav_home, R.id.nav_track, R.id.nav_mine})
    public boolean buttonOnLongClick(View view) {
        viewClick(view);
        return true;
    }

    @OnClick({R.id.nav_home, R.id.nav_track, R.id.nav_message, R.id.nav_mine})
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
                mapBtn.setSelected(true);
                addOrReplaceFgt(1);
                break;
            case R.id.nav_message:
                messageBtn.setSelected(true);
                addOrReplaceFgt(2);
                break;
            case R.id.nav_mine:
                mineBtn.setSelected(true);
                addOrReplaceFgt(3);
                break;
        }
    }

    public void resetNavBar() {
        homeBtn.setSelected(false);
        mapBtn.setSelected(false);
        messageBtn.setSelected(false);
        mineBtn.setSelected(false);
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

    private void login(final String account, final String password, final String type) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
                new VolleyHttpParamsEntity()
                        .addParam("username", account)
                        .addParam("password", password)
                        .addParam("type", type),
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
                                    CommonUtil.analyseLoginData(httpResult, type, account);

                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: error " + ex.getMessage());
                                    toLogin();
                                }
                                break;
                            default:
                                toLogin();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error);
                        toLogin();
                    }
                });
    }

    private void toLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(ExtraKey.LOGIN_ORIGIN, "0");
        startActivity(intent);
    }

    @Override
    protected void onStartLogin() {
        super.onStartLogin();
    }

    @Override
    protected void onLoginSuccess() {
        super.onLoginSuccess();
        //登录成功后，根据用户角色获取广告位
        if (homeFragment != null) {
            ((HomeFragment) homeFragment).reloadPageData();
        }

        String userId = "";
        try {
            if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                userId = GreenDaoHelper.getInstance().getCurrentParent().getU_id();
            } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                userId = GreenDaoHelper.getInstance().getCurrentTeacher().getU_id();
            }
        } catch (Exception ex) {

        }

        EMClient.getInstance().login(userId, CommonUtil.md5("SHUHAIXINXI" + userId), new EMCallBack() {

            @Override
            public void onSuccess() {
                EMLoginSuccess();
                Log.d(TAG, "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.i(TAG, "onProgress: " + progress + "  status:" + status);
            }

            @Override
            public void onError(final int code, final String error) {
                if (code == 200) {
                    //USER_ALREADY_LOGIN
                    EMLoginSuccess();
                    Log.d(TAG, "USER_ALREADY_LOGIN！");
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i(TAG, "EMUI onError: " + code + " error:" + error);
                            ToastUtils.showToast(getApplicationContext(), "环信 login failed");
                        }
                    });
                }
            }
        });
    }

    private void EMLoginSuccess() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    @Override
    protected void onLoginFailed(String msg) {
        super.onLoginFailed(msg);
        if (homeFragment != null) {
            ((HomeFragment) homeFragment).reloadPageData();
        }
    }

    BroadcastReceiver MyBannerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastAction.RELOAD_BANNER)) {
                //广告过期后，重新获取广告位
                if (homeFragment != null) {
                    ((HomeFragment) homeFragment).getBanners();
                }
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
            super.onBackPressed();
        }
    }
}