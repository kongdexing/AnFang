package com.xptschool.parent.ui.watch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;


/**
 * 计步
 */
public class WalkActivity extends BaseActivity {

    @BindView(R.id.txtDay)
    TextView txtDay;
    @BindView(R.id.txtNum)
    TextView txtNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        setTitle(R.string.home_walk);

        txtDay.setText(CommonUtil.getCurrentDate());

        getStepNum();
    }

    private void getStepNum() {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_STEP,
                new VolleyHttpParamsEntity().addParam("date", CommonUtil.getCurrentDate())
                        .addParam("imei", "867587027683984"),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            txtNum.setText(volleyHttpResult.getInfo());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

}
