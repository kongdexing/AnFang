package com.shuhai.anfang.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.meizu.cloud.pushsdk.PushManager;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.push.DeviceHelper;
import com.shuhai.anfang.push.MyPushIntentService;
import com.shuhai.anfang.push.UpushTokenHelper;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by dexing on 2017/6/5.
 * No1
 */

public class BaseMainActivity extends BaseActivity implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener {

    private HuaweiApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //依据手机类型，注册不同推送平台
        String model = android.os.Build.MODEL;
        String carrier = android.os.Build.MANUFACTURER;
        Log.i(TAG, "onCreate: " + model + "  " + carrier);

        if (carrier.toUpperCase().equals(DeviceHelper.M_XIAOMI)) {
            MiPushClient.registerPush(this, XPTApplication.APP_MIPUSH_ID, XPTApplication.APP_MIPUSH_KEY);
            LoggerInterface newLogger = new LoggerInterface() {

                @Override
                public void setTag(String tag) {
                    // ignore
                }

                @Override
                public void log(String content, Throwable t) {
                    Log.d(TAG, content, t);
                }

                @Override
                public void log(String content) {
                    Log.d(TAG, content);
                }
            };
            Logger.setLogger(this, newLogger);
            //推送可用
            MiPushClient.enablePush(this);
        } else if (carrier.toUpperCase().equals(DeviceHelper.M_HUAWEI)) {
            //创建华为移动服务client实例用以使用华为push服务
            //需要指定api为HuaweiId.PUSH_API
            //连接回调以及连接失败监听
            client = new HuaweiApiClient.Builder(this)
                    .addApi(HuaweiPush.PUSH_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //建议在oncreate的时候连接华为移动服务
            //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
            client.connect();
        } else if (carrier.toUpperCase().equals(DeviceHelper.M_MEIZU)) {
            PushManager.register(this, XPTApplication.MZ_APP_ID, XPTApplication.MZ_APP_KEY);
        } else {
            //友盟
            final PushAgent mPushAgent = PushAgent.getInstance(this);
            mPushAgent.setDebugMode(false);
            mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

            Log.i(TAG, "startServer: register ");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "startServer: register start");
                    //注册推送服务，每次调用register方法都会回调该接口
                    mPushAgent.register(new IUmengRegisterCallback() {

                        @Override
                        public void onSuccess(String deviceToken) {
                            //注册成功会返回device token
                            Log.i(TAG, "onSuccess: deviceToken " + deviceToken);
                            UpushTokenHelper.uploadDevicesToken(deviceToken, DeviceHelper.P_UMENG);
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                            Log.i(TAG, "onFailure: " + s + "---" + s1);
                        }
                    });
                }
            }).start();

            //接收通知
            mPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "PushAgent enable onSuccess: ");
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.i(TAG, "PushAgent enable onFailure: " + s + " s1 " + s1);
                }
            });
        }
    }

    private void getHWTokenAsyn() {
        if (!client.isConnected()) {
            Log.i(TAG, "获取token失败，原因：HuaweiApiClient未连接");
            client.connect();
            return;
        }

        Log.i(TAG, "异步接口获取push token");
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {

            @Override
            public void onResult(TokenResult result) {
                //这边的结果只表明接口调用成功，是否能收到响应结果只在广播中接收
                Log.i(TAG, "onResult code:" + result.getTokenRes().getRetCode() + "  token:" + result.getTokenRes().getToken());
            }
        });
    }

    @Override
    public void onConnected() {
        //华为移动服务client连接成功，在这边处理业务自己的事件
        Log.i(TAG, "HuaweiApiClient 连接成功");
        getHWTokenAsyn();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        //HuaweiApiClient断开连接的时候，业务可以处理自己的事件
        Log.i(TAG, "HuaweiApiClient 连接断开");
        //HuaweiApiClient异常断开连接, if 括号里的条件可以根据需要修改
        if (!this.isDestroyed() && !this.isFinishing()) {
            client.connect();
        }
    }

    //调用HuaweiApiAvailability.getInstance().resolveError传入的第三个参数
    //作用同startactivityforresult方法中的requestcode
    private static final int REQUEST_HMS_RESOLVE_ERROR = 1000;

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "HuaweiApiClient连接失败，错误码：" + result.getErrorCode());
        if (HuaweiApiAvailability.getInstance().isUserResolvableError(result.getErrorCode())) {
            final int errorCode = result.getErrorCode();
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 此方法必须在主线程调用
                    HuaweiApiAvailability.getInstance().resolveError(BaseMainActivity.this, errorCode, REQUEST_HMS_RESOLVE_ERROR);
                }
            });
        } else {
            //其他错误码请参见开发指南或者API文档
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean isLogin() {
        String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
        String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
        if (userName.isEmpty() || password.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //建议在onDestroy的时候停止连接华为移动服务
        //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
        if (client != null) {
            client.disconnect();
        }
    }

}
