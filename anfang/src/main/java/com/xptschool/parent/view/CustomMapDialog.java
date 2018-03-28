package com.xptschool.parent.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/03/26.
 * 地图选择
 */
public class CustomMapDialog implements View.OnClickListener {

    private Context mContext;

    private AlertDialog alertDialog;
    private LinearLayout llMap;

    public CustomMapDialog(Context context) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(new EditText(context));
        alertDialog.show();
        alertDialog.getWindow().setLayout(XPTApplication.getInstance().getWindowWidth() * 4 / 5,
                XPTApplication.getInstance().getWindowHeight() / 3);

        alertDialog.setCanceledOnTouchOutside(true);

        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.popup_map_dialog);

        llMap = (LinearLayout) alertDialog.findViewById(R.id.llMap);
        llMap.removeAllViews();

        HashMap<String, String> mapStrs = CommonUtil.getMapPackNames();
        Set<Map.Entry<String, String>> sets = mapStrs.entrySet();
        for (Map.Entry<String, String> entry : sets) {
//            System.out.print(entry.getKey() + ", ");
//            System.out.println(entry.getValue());
            if (CommonUtil.isAppInstalled(entry.getKey())) {
                MapNameView mapNameView = new MapNameView(mContext);
                mapNameView.setMapVal(entry.getKey(), entry.getValue());
                llMap.addView(mapNameView);
            }
        }

    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                dismiss();
//                if (clickListener != null) {
//                    clickListener.onPositiveClick(val);
//                }
                break;
            case R.id.cancel:
                dismiss();
                break;
        }

    }

    public interface DialogClickListener {
        void onPositiveClick(String value);
    }

}
