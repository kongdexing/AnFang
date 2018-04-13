package com.xptschool.parent.ui.register;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.spinner.SpinnerModel;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanCounty;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.GetJsonDataUtil;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//选择学校|申请学校
public class SelSchoolActivity extends BaseActivity {

    @BindView(R.id.txtcounty)
    TextView txtcounty;
    @BindView(R.id.txtNoSchool)
    TextView txtNoSchool;

    @BindView(R.id.spnSchool)
    MaterialSpinner spnSchool;
    @BindView(R.id.spnCampus)
    MaterialSpinner spnCampus;

    @BindView(R.id.llExistSch)
    LinearLayout llExistSch;
    @BindView(R.id.llApply)
    LinearLayout llApply;
    @BindView(R.id.rlBnding)
    RelativeLayout rlBnding;
    @BindView(R.id.llBtnApply)
    LinearLayout llBtnApply;

    @BindView(R.id.edtSchName)
    EditText edtSchName;
    @BindView(R.id.edtCampus)
    EditText edtCampus;

    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    List<BeanCounty> counties = new ArrayList<>();
    List<BeanCounty> counties1 = new ArrayList<>();
    OptionsPickerView pvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_school);
        setTitle(R.string.title_select_school);
        //判断是否存在省市区数据
        counties1 = GreenDaoHelper.getInstance().getCountiesByParentId("0");
        if (counties1.size() == 0) {
            mHandler.sendEmptyMessage(MSG_LOAD_DATA);
            txtcounty.setClickable(false);
        }
    }

    private void loadCountiesData() {
        txtcounty.setClickable(true);
        counties1 = GreenDaoHelper.getInstance().getCountiesByParentId("0");
        for (int i = 0; i < counties1.size(); i++) {
            options1Items.add(counties1.get(i).getRegion_name());
            List<BeanCounty> counties2 = GreenDaoHelper.getInstance().getCountiesByParentId(counties1.get(i).getRegion_id());
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int j = 0; j < counties2.size(); j++) {
                CityList.add(counties2.get(j).getRegion_name());

                List<BeanCounty> counties3 = GreenDaoHelper.getInstance().getCountiesByParentId(counties2.get(j).getRegion_id());

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                for (int k = 0; k < counties3.size(); k++) {
                    City_AreaList.add(counties3.get(k).getRegion_name());
                }
                Province_AreaList.add(City_AreaList);
            }

            //添加城市数据
            options2Items.add(CityList);
            //添加地区数据
            options3Items.add(Province_AreaList);
        }
    }

    @OnClick({R.id.txtcounty, R.id.txtNoSchool, R.id.btnCancel, R.id.btnBinding, R.id.btnApply})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.txtcounty:
                ShowPickerView();
                break;
            case R.id.txtNoSchool:
                txtNoSchool.setVisibility(View.GONE);

                llExistSch.setVisibility(View.GONE);
                llApply.setVisibility(View.VISIBLE);

                rlBnding.setVisibility(View.GONE);
                llBtnApply.setVisibility(View.VISIBLE);
                break;
            case R.id.btnCancel:
                txtNoSchool.setVisibility(View.VISIBLE);

                llExistSch.setVisibility(View.VISIBLE);
                llApply.setVisibility(View.GONE);

                rlBnding.setVisibility(View.VISIBLE);
                llBtnApply.setVisibility(View.GONE);
                break;
            case R.id.btnBinding:

                break;
            case R.id.btnApply:

                break;
        }
    }

    private void ShowPickerView() {// 弹出选择器
        if (options1Items.size() == 0) {
            loadCountiesData();
        }
        if (pvOptions == null) {
            pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = options1Items.get(options1) + " " +
                            options2Items.get(options1).get(options2) + " " +
                            options3Items.get(options1).get(options2).get(options3);
                    txtcounty.setText(tx);

                    String toast = "";
                    //从数据库查询县 id
                    //1.获取省
                    BeanCounty county1 = counties1.get(options1);
                    List<BeanCounty> counties2 = GreenDaoHelper.getInstance().getCountiesByParentId(county1.getRegion_id());
                    if (counties2.size() > options2) {
                        //2.获取市
                        BeanCounty county2 = counties2.get(options2);
                        List<BeanCounty> counties3 = GreenDaoHelper.getInstance().getCountiesByParentId(county2.getRegion_id());
                        if (counties3.size() > options3) {
                            //3.获取县
                            BeanCounty county3 = counties3.get(options3);
                            toast = county1.getRegion_name() + county2.getRegion_name() + county3.getRegion_name() + "--" + county3.getRegion_code();
                            ToastUtils.showToast(SelSchoolActivity.this, toast);
                            getSchoolListByCountyCode(county3.getRegion_code());
                        }
                    }
                }
            }).setTitleText("城市选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .build();
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        }
        pvOptions.show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        Toast.makeText(SelSchoolActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    Toast.makeText(SelSchoolActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    loadCountiesData();
                    break;
                case MSG_LOAD_FAILED:
                    Toast.makeText(SelSchoolActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initJsonData() {//解析数据
        String JsonData = new GetJsonDataUtil().getJson(this, "sch_area_region.json");//获取assets目录下的json文件数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            Gson gson = new Gson();
            counties = gson.fromJson(jsonObject.getJSONArray("RECORDS").toString(), new TypeToken<List<BeanCounty>>() {
            }.getType());
            //将省市区存入数据库
            GreenDaoHelper.getInstance().insertCounties(counties);
        } catch (Exception ex) {
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
            return;
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    private void getSchoolListByCountyCode(String countyCode) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_SCHOOL,
                new MyVolleyHttpParamsEntity()
                        .addParam("region_id", countyCode), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取学校信息...");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    List<SchoolInfo> schoolInfos = new ArrayList<>();
                                    Gson gson = new Gson();
                                    schoolInfos = gson.fromJson(volleyHttpResult.getData().toString(), new TypeToken<List<SchoolInfo>>() {
                                    }.getType());
                                    spnSchool.setItems(schoolInfos);
                                    spnSchool.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<SchoolInfo>() {
                                        @Override
                                        public void onItemSelected(MaterialSpinner view, int position, long id, SchoolInfo item) {
                                            if (item.getArea() != null) {
                                                spnCampus.setItems(item.getArea());
                                            } else {
                                                spnCampus.setItems("暂无校区");
                                            }
                                        }
                                    });
                                    if (schoolInfos.size() > 0) {
                                        spnCampus.setItems(schoolInfos.get(0).getArea());
                                        //显示申请学校
                                        txtNoSchool.setVisibility(View.VISIBLE);
                                        rlBnding.setVisibility(View.VISIBLE);
                                    } else {
                                        spnCampus.setItems("暂无校区");
                                    }
                                } catch (Exception ex) {
                                    ToastUtils.showToast(SelSchoolActivity.this, ex.getMessage());
                                    spnSchool.setItems("");
                                    spnCampus.setItems("");
                                }
                                break;
                            default:
                                spnSchool.setItems("");
                                spnCampus.setItems("");
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                        spnSchool.setItems("");
                        spnCampus.setItems("");
                    }
                });
    }

    class SchoolInfo extends SpinnerModel {
        private String s_id;
        private List<SchoolArea> area;

        public String getS_id() {
            return s_id;
        }

        public void setS_id(String s_id) {
            this.s_id = s_id;
        }

        public List<SchoolArea> getArea() {
            return area;
        }

        public void setArea(List<SchoolArea> area) {
            this.area = area;
        }
    }

    class SchoolArea extends SpinnerModel {
        private String a_id;

        public String getA_id() {
            return a_id;
        }

        public void setA_id(String a_id) {
            this.a_id = a_id;
        }

    }

}
