package com.xptschool.parent.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.main.BaseActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by dexing on 2017-11-15 0015.
 */

public class QRCodeActivity extends BaseActivity implements PlatformActionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        setTitle(R.string.mine_qr_code);


        Platform weibo = ShareSDK.getPlatform(QQ.NAME);
        weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
        weibo.setPlatformActionListener(this); // 设置分享事件回调

        weibo.authorize();//单独授权
        weibo.showUser(null);//授权并获取用户信息


        setTxtRight("分享");
        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.i(TAG, "onComplete: ");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.i(TAG, "onError: "+throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.i(TAG, "onCancel: ");
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("数海安防校园");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("欢迎下载使用信平台APP");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://school.xinpingtai.com/app/index.html");
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }
}
