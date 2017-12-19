package com.shuhai.anfang.ui.wallet.card;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.ui.main.BaseActivity;
import com.shuhai.anfang.ui.wallet.pocket.BalanceUtil;
import com.shuhai.anfang.util.ToastUtils;
import com.shuhai.anfang.view.CustomEditDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 学生卡充值
 */
public class CardRechargeActivity extends BaseActivity {

    @BindView(R.id.txt_current_num)
    TextView txt_current_num;
    @BindView(R.id.edt_num)
    EditText edt_money;
    @BindView(R.id.txt_balance)
    TextView txt_balance;

    private String current_stuId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recharge);
        setTitle(R.string.label_card_recharge);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            current_stuId = bundle.getString("stu_id");
            txt_current_num.setText(bundle.getString("balance"));
        }
        getPocketBalance();

        edt_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String money = edt_money.getText().toString();
                if (money.contains(".")) {
                    int dotLength = money.length() - (money.indexOf(".") + 1);
                    if (dotLength > 2) {
                        String _money = money.substring(0, money.length() - 1);
                        edt_money.setText(_money);
                    }
                }

                try {
                    if (!money.isEmpty() && Double.parseDouble(money) > BalanceUtil.getParentBalance()) {
                        edt_money.setText(money.substring(0, money.length() - 1));
                    }
                } catch (Exception ex) {

                }
                edt_money.setSelection(edt_money.getText().toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.btn_recharge})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge:
                try {
                    float balance = Float.parseFloat(txt_balance.getText().toString());
                    final float recharge_sum = Float.parseFloat(edt_money.getText().toString());
                    if (recharge_sum > balance) {
                        Toast.makeText(this, R.string.msg_recharge_greater_than_balance, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (recharge_sum == 0) {
                        ToastUtils.showToast(this, "余额为0，无法充值");
                        return;
                    }

                    CustomEditDialog editDialog = new CustomEditDialog(this);
                    editDialog.setTitle(R.string.title_user_verify);
                    editDialog.setEdtInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editDialog.setHintEdit(R.string.msg_input_password);
                    editDialog.setAlertDialogClickListener(new CustomEditDialog.DialogClickListener() {
                        @Override
                        public void onPositiveClick(String value) {
                            String password = (String) SharedPreferencesUtil.getData(CardRechargeActivity.this, SharedPreferencesUtil.KEY_PWD, "");
                            if (value.equals(password)) {
                                getOrderId(recharge_sum + "");
//                                onStuCardRecharge(recharge_sum + "");
                            } else {
                                Toast.makeText(CardRechargeActivity.this, R.string.error_toast_password, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(this, "充值金额有误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getPocketBalance() {

        BalanceUtil.getBalance(new BalanceUtil.BalanceCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                if (txt_balance != null) {
                    txt_balance.setText(BalanceUtil.getParentBalance() + "");
                }
            }

            @Override
            public void onFailed(String error) {
                if (txt_balance != null) {
                    txt_balance.setText("获取失败");
                }
            }
        });
    }

    private void getOrderId(final String money) {

        VolleyHttpService.getInstance().sendGetRequest(HttpAction.STU_CARD_ORDERID, new MyVolleyRequestListener() {
            @Override
            public void onStart() {
                super.onStart();
                showProgress(R.string.progress_stu_card_recharge);
            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                super.onResponse(volleyHttpResult);
                if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                    try {
                        JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                        String orderId = object.getString("orderId");
                        String access_token = CommonUtil.md5(orderId + CommonUtil.CARD_KEY + current_stuId + money);
                        onStuCardRecharge(money, orderId, access_token);
                    } catch (Exception ex) {
                        hideProgress();
                        ToastUtils.showToast(CardRechargeActivity.this, R.string.msg_recharge_failed);
                    }
                } else {
                    hideProgress();
                    ToastUtils.showToast(CardRechargeActivity.this, R.string.msg_recharge_failed);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                hideProgress();
                ToastUtils.showToast(CardRechargeActivity.this, R.string.msg_recharge_failed);
            }
        });
    }

    //学生卡充值
    private void onStuCardRecharge(String money, String orderId, String accessToken) {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.STU_CARD_RECHARGE,
                new VolleyHttpParamsEntity()
                        .addParam("stu_id", current_stuId)
                        .addParam("account", money)
                        .addParam("order_id", orderId)
                        .addParam("access_token", accessToken)
                        .addParam("token", CommonUtil.encryptToken(HttpAction.STU_CARD_RECHARGE)), new MyVolleyRequestListener() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        Toast.makeText(CardRechargeActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            finish();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                        Toast.makeText(CardRechargeActivity.this, R.string.msg_recharge_failed, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
