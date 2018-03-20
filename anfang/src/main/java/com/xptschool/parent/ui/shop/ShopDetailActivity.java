package com.xptschool.parent.ui.shop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanShop;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;

import org.json.JSONObject;

import butterknife.BindView;

public class ShopDetailActivity extends BaseActivity {

    private String TAG = ShopDetailActivity.class.getSimpleName();

    @BindView(R.id.goods_img)
    ImageView goods_img;
    @BindView(R.id.txtDes)
    TextView txtDes;
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtAddress)
    TextView txtAddress;

    @BindView(R.id.webView)
    WebView webView;

    BeanShop currentGood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail_web);

        if (getSupportActionBar() != null)
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentGood = (BeanShop) bundle.get(ExtraKey.SHOP_GOODS);
            if (currentGood != null) {
                ImageLoader.getInstance().displayImage(currentGood.getImage(),
                        new ImageViewAware(goods_img), CommonUtil.getDefaultImageLoaderOption());

                txtDes.setText(currentGood.getDescribe());
                try {
                    double price = Double.parseDouble(currentGood.getPrice());
                    if (0 >= price) {
                        txtPrice.setVisibility(View.GONE);
                    } else {
                        txtPrice.setText(currentGood.getPrice());
                    }
                } catch (Exception ex) {
                    txtPrice.setVisibility(View.GONE);
                }

                txtAddress.setText(currentGood.getAddress());

                setTitle(currentGood.getDescribe());
                getDetail();
            }
        }


    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");

//            view.loadData();

        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","progress:"+newProgress);
        }
    };

    private void getDetail() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_GOODDETAIL,
                new MyVolleyHttpParamsEntity().addParam("id", currentGood.getId()), new MyVolleyRequestListener() {
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

                                    String desc = object.getString("desc");

                                    webView.loadData(desc, "text/html", null);

                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(ShopDetailActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(ShopDetailActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
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
