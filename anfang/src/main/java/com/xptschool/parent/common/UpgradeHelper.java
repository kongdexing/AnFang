package com.xptschool.parent.common;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.volley.common.VolleyRequestListener;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;

public class UpgradeHelper {

    public static UpgradeHelper instance = null;

    public static synchronized UpgradeHelper getInstance() {
        if (instance == null) {
            synchronized (UpgradeHelper.class) {
                if (instance == null) {
                    instance = new UpgradeHelper();
                }
            }
        }
        return instance;
    }

    public void checkUpgrade(final UpgradeListener listener) {

        VolleyHttpService.getInstance().sendGetRequest(HttpAction.GET_UPGRADE_INFO, new VolleyRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                    if (listener != null) {
                        try {
                            listener.onUpgrade(volleyHttpResult.getData().toString());
                        } catch (Exception ex) {
                            listener.onUpgrade("");
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

    }


    public interface UpgradeListener {
        void onUpgrade(String data);
    }

}
