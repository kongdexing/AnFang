package com.xptschool.parent.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xptschool.parent.R;

/**
 * Created by shuhaixinxi on 2017/12/17.
 */

public class ContactBaseView extends LinearLayout {

    public Context mContext;

    public ContactBaseView(Context context) {
        super(context,null);
    }

    public ContactBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void call(String phone) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(mContext, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
        }
    }

}
