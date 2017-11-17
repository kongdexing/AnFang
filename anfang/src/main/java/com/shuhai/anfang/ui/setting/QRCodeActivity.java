package com.shuhai.anfang.ui.setting;

import android.os.Bundle;

import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.main.BaseActivity;

/**
 * Created by dexing on 2017-11-15 0015.
 */

public class QRCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        setTitle(R.string.mine_qr_code);

    }
}
