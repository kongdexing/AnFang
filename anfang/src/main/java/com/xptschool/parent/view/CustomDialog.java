package com.xptschool.parent.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;

/**
 * Created by Administrator on 2016/10/28.
 */

public class CustomDialog implements View.OnClickListener {

    private Context mContext;
    private AlertDialog alertDialog;
    private TextView txtTitle;
    private TextView txtMessage;
    private TextView btnConfirm;
    private ImageView img_dialog_teacher;
    private ImageButton btnCancel;
    private DialogClickListener clickListener;

    public CustomDialog(Context context) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(XPTApplication.getInstance().getWindowWidth() * 5 / 6,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.popup_dialog);
        img_dialog_teacher = (ImageView) window.findViewById(R.id.img_dialog_teacher);
        txtTitle = (TextView) window.findViewById(R.id.title);
        txtMessage = (TextView) window.findViewById(R.id.content);
        btnConfirm = (TextView) window.findViewById(R.id.ok);
        btnCancel = (ImageButton) window.findViewById(R.id.cancel);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtTitle.setVisibility(View.GONE);
    }

    public void setTitle(int strId) {
        setTitle(mContext.getString(strId));
    }

    public void setVisibilityCancel(boolean visibility){
        btnCancel.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    public void setTitle(String title) {
        if (txtTitle != null) {
            if (title != null && !title.isEmpty()) {
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText(title);
            } else {
                txtTitle.setVisibility(View.GONE);
            }
        }
    }

    public void setMessage(int strId) {
        txtMessage.setText(strId);
    }

    public void setMessage(String message) {
        txtMessage.setText(message);
    }

    public void setAlertDialogClickListener(DialogClickListener listener) {
        clickListener = listener;
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
                if (clickListener != null) {
                    clickListener.onPositiveClick();
                }
                break;
            case R.id.cancel:
                break;
        }
        dismiss();
    }

    public interface DialogClickListener {
        void onPositiveClick();
    }

}
