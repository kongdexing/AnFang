package com.xptschool.parent.ui.shop;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.bean.BeanShop;
import com.xptschool.parent.ui.main.BaseListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 商品列表
 */
public class ShopListActivity extends BaseListActivity {

    BeanHomeCfg currentCfg;

    @BindView(R.id.txt_des)
    TextView txt_des;
    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;
    ShopListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentCfg = (BeanHomeCfg) bundle.get(ExtraKey.CATEGORY_ID);

            if (currentCfg != null) {
                setTitle(currentCfg.getTitle());

                SpannableStringBuilder span = new SpannableStringBuilder("缩进" + currentCfg.getMark());
                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                txt_des.setText(span);
                getShopList(currentCfg.getId());
            }
        }
    }

    private void initView() {
        initRecyclerView(recyclerView, null);

        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ShopListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void getShopList(String id) {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_SHOPLIST,
                new MyVolleyHttpParamsEntity().addParam("cate_id", "g_" + id),
                new MyVolleyRequestListener() {

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
                                    List<BeanShop> shops = new ArrayList<>();
                                    Gson gson = new Gson();
                                    shops = gson.fromJson(volleyHttpResult.getData().toString(),
                                            new TypeToken<List<BeanShop>>() {
                                            }.getType());
                                    adapter.refreshData(shops);
                                    recyclerView.setAdapter(adapter);
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(ShopListActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(ShopListActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
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
