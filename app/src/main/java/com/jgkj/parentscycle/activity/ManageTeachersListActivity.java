package com.jgkj.parentscycle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.adapter.SelectTeacherListAdapter;
import com.jgkj.parentscycle.bean.TeachersListInfo;
import com.jgkj.parentscycle.bean.TeachersListItemInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.TeacherListPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.utils.ActivityUtils;
import com.jgkj.parentscycle.utils.ToastUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/8/23.
 */
public class ManageTeachersListActivity extends BaseActivity implements View.OnClickListener,NetListener{
    @Bind(R.id.title_bar_layout_rel)
    View titleBg;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTv;

    @Bind(R.id.make_class_add_person_activity_content_lv)
    ListView mContentLv;


    SelectTeacherListAdapter selectTeacherListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_teacher_list_activity);
        ButterKnife.bind(this);
        initViews();
        requestTeachersList();
    }

    private void requestTeachersList() {
            showProgressDialog();
            HashMap<String, String> requestData = new HashMap<String, String>();
            requestData.put("schoolid", ConfigPara.SCHOOL_ID);
            TeacherListPaser lp = new TeacherListPaser();
            NetRequest.getInstance().request(mQueue, this, BgGlobal.TEACHERS_LIST, requestData, lp);
    }


    private void initViews() {
        rightTv.setVisibility(View.GONE);
        titleTv.setText("老师管理");
//        titleTv.setTextColor(Color.BLACK);
//        titleBg.setBackgroundColor(Color.WHITE);

    }

    @OnClick({R.id.baby_document_activity_back_iv})

    @Override
    public void onClick(View v) {
        if (v == backIv) {
            finish();
        }
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap, String uploadedKey) {

    }

    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof TeachersListInfo) {
            if (nbs.isSuccess()) {
                TeachersListInfo tii = (TeachersListInfo)nbs.obj;
                selectTeacherListAdapter = new SelectTeacherListAdapter(this,tii.getData());
                mContentLv.setAdapter(selectTeacherListAdapter);
                mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TeachersListItemInfo tii = (TeachersListItemInfo)selectTeacherListAdapter.getItem(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("teacher_id",tii.getTmpinfoid());
                        ActivityUtils.jump(ManageTeachersListActivity.this,TeacherInfoActivity.class,-1,bundle);
                    }
                });
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        }
    }
}
