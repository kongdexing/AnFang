package com.xptschool.parent.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.widget.view.SmoothCheckBox;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;

/**
 * Created by Administrator on 2017/03/26.
 * 性别更改
 */
public class CustomSexDialog implements View.OnClickListener {

    private Context mContext;

    private Button btnConfirm, btnCancel;
    private DialogClickListener clickListener;
    private AlertDialog alertDialog;

    RelativeLayout rlMale, rlFeMale;
    SmoothCheckBox cbx_male, cbx_female;

    public CustomSexDialog(Context context) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(new EditText(context));
        alertDialog.show();
        alertDialog.getWindow().setLayout(XPTApplication.getInstance().getWindowWidth() * 4 / 5,
                XPTApplication.getInstance().getWindowHeight() / 3);

        alertDialog.setCanceledOnTouchOutside(false);

        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.popup_sex_dialog);

        rlMale = (RelativeLayout) window.findViewById(R.id.rlMale);
        rlFeMale = (RelativeLayout) window.findViewById(R.id.rlFeMale);
        cbx_male = (SmoothCheckBox) window.findViewById(R.id.cbx_male);
        cbx_female = (SmoothCheckBox) window.findViewById(R.id.cbx_female);

//        edtMessage.requestFocus();
        btnConfirm = (Button) window.findViewById(R.id.ok);
        btnCancel = (Button) window.findViewById(R.id.cancel);

        rlMale.setOnClickListener(this);
        rlFeMale.setOnClickListener(this);
        cbx_male.setOnClickListener(this);
        cbx_female.setOnClickListener(this);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    public void setAlertDialogClickListener(DialogClickListener listener) {
        clickListener = listener;
    }

    public void setSexVal(String sex) {
        if ("1".equals(sex)) {
            cbx_male.setChecked(true);
            cbx_female.setChecked(false);
        } else {
            cbx_male.setChecked(false);
            cbx_female.setChecked(true);
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
            case R.id.cbx_male:
            case R.id.rlMale:
                setSexVal("1");
                break;
            case R.id.cbx_female:
            case R.id.rlFeMale:
                setSexVal("0");
                break;
            case R.id.ok:
                String val = "1";
                if (cbx_female.isChecked()) {
                    val = "0";
                }
                dismiss();
                if (clickListener != null) {
                    clickListener.onPositiveClick(val);
                }
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
