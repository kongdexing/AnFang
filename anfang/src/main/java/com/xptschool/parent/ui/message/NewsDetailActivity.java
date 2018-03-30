package com.xptschool.parent.ui.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanShop;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.shop.ShopDetailActivity;

import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    String newsId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newsId = (String) bundle.get(ExtraKey.DETAIL_ID);
            if (newsId != null) {
                getDetail();
            }
        }

    }

    private void getDetail() {
        VolleyHttpService.getInstance().sendGetRequest(HttpAction.GET_NEWS_DETAIL + "?m_id=" + newsId, new MyVolleyRequestListener() {
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
                            JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                            String desc = object.getString("content");
                            webView.loadData(desc, "text/html; charset=UTF-8", null);
                        } catch (Exception ex) {
                            Log.i(TAG, "onResponse: " + ex.getMessage());
                            Toast.makeText(NewsDetailActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(NewsDetailActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
    }


}
