package com.xptschool.parent.ui.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.common.VolleyHttpResult;
import com.just.library.AgentWeb;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.wallet.alipay.PayResult;
import com.xptschool.parent.ui.wallet.pocket.RechargeActivity;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by cenxiaozhong on 2017/5/14.
 * source CODE  https://github.com/Justson/AgentWeb
 */

public class AndroidInterface {

    private String TAG = "Pay";
    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;
    private static final int SDK_PAY_FLAG = 1;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }

    @JavascriptInterface
    public void callAndroid(final String option1, final String option2) {

        if (XPTApplication.getInstance().getCurrent_user_type() == null) {
            ToastUtils.showToast(context, "请登录后进行操作");
            return;
        }

        Log.i(TAG, "callAndroid: " + option1 + "\n" + option2);

        final VolleyHttpResult volleyHttpResult = new VolleyHttpResult();
        try {
            JSONObject json = new JSONObject(option2);
            volleyHttpResult.setStatus(json.getInt("status"));
            volleyHttpResult.setUrl(json.getString("url"));
            try {
                volleyHttpResult.setData(json.get("data"));
                Log.i(TAG, "analyseResponse: data--" + volleyHttpResult.getData());
            } catch (Exception ex) {
                Log.e(TAG, "analyseResponse: data " + ex.getMessage());
            }

            try {
                volleyHttpResult.setInfo(json.getString("info"));
                Log.i(TAG, "analyseResponse: info--" + volleyHttpResult.getInfo());
            } catch (Exception ex) {
                Log.e(TAG, "analyseResponse: info " + ex.getMessage());
            }
        } catch (Exception ex) {
            Log.i(TAG, "callAndroid json error: " + ex.getMessage());
            return;
        }

        switch (volleyHttpResult.getStatus()) {
            case HttpAction.SUCCESS:
                if ("1".equals(option1)) {
                    //微信支付
                    try {
                        JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                        IWXAPI api = WXAPIFactory.createWXAPI(context, XPTApplication.getInstance().WXAPP_ID);
                        boolean register = api.registerApp(XPTApplication.getInstance().WXAPP_ID);
                        PayReq req = new PayReq();
                        req.appId = XPTApplication.getInstance().WXAPP_ID;
                        req.partnerId = jsonObject.getString("partnerid");
                        req.prepayId = jsonObject.getString("prepayid");
                        req.nonceStr = jsonObject.getString("noncestr");
                        req.timeStamp = jsonObject.getString("timestamp");
                        req.packageValue = jsonObject.getString("package");
                        req.sign = jsonObject.getString("sign");
//                req.extData = "app data"; // optional
//                Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        boolean rst = api.sendReq(req);
//                        Toast.makeText(context, "register:" + register + " sendReq result " + rst, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {

                    }
                } else if ("0".equals(option1)) {
                    //支付宝
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask((WebViewActivity) context);
                            Map<String, String> result = alipay.payV2(volleyHttpResult.getInfo().toString(), true);
                            Log.i(TAG, "payV2:" + result.toString());
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
                break;
            default:
                ToastUtils.showToast(context, volleyHttpResult.getInfo().toString());
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.i(TAG, "handleMessage: " + resultInfo + " status :" + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
//                        RechargeActivity.this.finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }

        ;
    };

}
