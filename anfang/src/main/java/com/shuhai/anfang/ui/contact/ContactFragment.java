package com.shuhai.anfang.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.groupexpandable.FloatingGroupExpandableListView;
import com.android.widget.groupexpandable.WrapperExpandableListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.model.BeanStudent;
import com.shuhai.anfang.model.ContactSchool;
import com.shuhai.anfang.model.ContactTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.fragment.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-11-23 0023.
 */

public class ContactFragment extends BaseFragment {

    @BindView(R.id.edtContent)
    EditText edtContent;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.expandableview)
    FloatingGroupExpandableListView expandableview;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;
    WrapperExpandableListAdapter wrapperAdapter;
    ContactsAdapter adapter;

    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        adapter = new ContactsAdapter(mContext);

        wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        expandableview.setAdapter(wrapperAdapter);
        swipe_refresh_widget.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        swipe_refresh_widget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContacts();
            }
        });

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "onTextChanged: " + charSequence.toString());
                adapter.reloadByName(charSequence.toString());
                expandContactList();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "afterTextChanged: ");
            }
        });

    }

    @Override
    protected void initData() {
        if (!XPTApplication.getInstance().isLoggedIn()) {
            return;
        }

        ArrayList<Object> listTeacher = (ArrayList) GreenDaoHelper.getInstance().getContactTeacher();
        ArrayList<Object> listSchool = (ArrayList) GreenDaoHelper.getInstance().getSchoolInfo();

        if (listTeacher.size() > 0 || listSchool.size() > 0) {
            setContact(listTeacher, listSchool);
        }
        getContacts();
    }

    private void getContacts() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.MyContacts_QUERY,
                new VolleyHttpParamsEntity()
                        .addParam("token", CommonUtil.encryptToken(HttpAction.MyContacts_QUERY)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (swipe_refresh_widget != null) {
                            swipe_refresh_widget.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (swipe_refresh_widget != null) {
                            swipe_refresh_widget.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                            Gson gson = new Gson();
                            ArrayList<Object> listTeacher = gson.fromJson(jsonObject.getJSONArray("teacher").toString(),
                                    new TypeToken<List<ContactTeacher>>() {
                                    }.getType());

                            ArrayList<Object> listSchool = gson.fromJson(jsonObject.getJSONArray("school").toString(),
                                    new TypeToken<List<ContactSchool>>() {
                                    }.getType());

                            Log.i(TAG, "onResponse: listTeacher size " + listTeacher.size());
                            Log.i(TAG, "onResponse: listSchool size " + listSchool.size());
                            GreenDaoHelper.getInstance().insertContactTeacher((List) listTeacher);
                            GreenDaoHelper.getInstance().insertSchoolInfo((List) listSchool);
                            setContact(listTeacher, listSchool);
                        } catch (Exception ex) {
                            Log.e(TAG, "onResponse: json " + ex.getMessage());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        if (swipe_refresh_widget != null) {
                            swipe_refresh_widget.setRefreshing(false);
                        }
                    }
                });
    }

    private void setContact(ArrayList<Object> listTeacher, ArrayList<Object> listSchool) {
        LinkedHashMap<String, ArrayList<Object>> listContacts = new LinkedHashMap<>();

        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        for (int j = 0; j < students.size(); j++) {
            ArrayList<Object> teachers = new ArrayList<>();
            BeanStudent student = students.get(j);

            for (int i = 0; i < listSchool.size(); i++) {
                try {
                    ContactSchool school = (ContactSchool) listSchool.get(i);
                    if (student.getS_id().equals(school.getS_id()) &&
                            student.getA_id().equals(school.getA_id())) {
                        teachers.add(school);
                    }
                } catch (Exception ex) {

                }
            }

            for (int i = 0; i < listTeacher.size(); i++) {
                try {
                    ContactTeacher teacher = (ContactTeacher) listTeacher.get(i);
                    if (teacher.getS_id().equals(student.getS_id()) &&
                            teacher.getC_id().equals(student.getC_id())) {
                        teachers.add(teacher);
                    }
                } catch (Exception ex) {

                }
            }
            String title = student.getName() + " (" + student.getG_name() + student.getC_name() + ")";
            listContacts.put(title, teachers);
        }

        adapter.loadContacts(listContacts);
        expandContactList();
    }

    private void expandContactList() {
        for (int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
            expandableview.expandGroup(i);
        }
    }


}
