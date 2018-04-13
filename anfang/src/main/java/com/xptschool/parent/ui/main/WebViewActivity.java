package com.xptschool.parent.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.just.agentweb.AgentWeb;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.ui.web.AndroidInterface;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {

    private String TAG = WebViewActivity.class.getSimpleName();
    @BindView(R.id.web_error)
    View web_error;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.web_content)
    WebView web_content;
    @BindView(R.id.btn_refresh)
    View btn_refresh;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            web_error.setVisibility(View.VISIBLE);
            return;
        }
        final String webUrl = bundle.getString(ExtraKey.WEB_URL);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(webUrl);
            }
        });
        loadUrl(webUrl);
    }

    private void loadUrl(String webUrl) {
        if (!webUrl.contains("http://") && !webUrl.contains("https://")) {
            webUrl = "http://" + webUrl;
        }
        web_error.setVisibility(View.GONE);
        web_content.clearCache(true);
        web_content.clearView();
        web_content.setBackgroundColor(Color.WHITE);
        WebSettings webSettings = web_content.getSettings();
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        web_content.requestFocus();

        web_content.setWebViewClient(new MyWebClient());

        MyWebChromeClient wn = new MyWebChromeClient();
        web_content.setWebChromeClient(wn);
        web_content.loadUrl(webUrl);
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar1 == null) {
                return;
            }
            progressBar1.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar1.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        if (web_content.canGoBack()) {
            web_content.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyWebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

}
