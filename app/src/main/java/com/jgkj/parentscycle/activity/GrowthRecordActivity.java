package com.jgkj.parentscycle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.adapter.GrowthRecordActivityLvAdapter;
import com.jgkj.parentscycle.bean.ClassedAndTeachersListInfo;
import com.jgkj.parentscycle.bean.GrowthRecordInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.json.GrowthRecordInfoPaser;
import com.jgkj.parentscycle.json.TeacherInfoLIstPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/8/21.
 */
public class GrowthRecordActivity extends BaseActivity implements View.OnClickListener,NetListener{

    @Bind(R.id.growth_record_activity_lv)
    ListView mContentLv;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTitleTv;

    @Bind(R.id.growth_record_activity_send_record_iv)
    ImageView sendRecordIv;

    @Bind(R.id.title_bar_layout_rel)
    View titleBg;
    @Override
    public void uploadImgFinished(Bitmap bitmap, String uploadedKey) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.growth_record_activity);
        ButterKnife.bind(this);
        initView();
        requestBabyGrowthList();
    }

    private void initView() {
        rightTitleTv.setVisibility(View.GONE);
        titleTv.setText("成长记录");
        titleBg.setBackgroundColor(Color.TRANSPARENT);
    }


    @OnClick({R.id.baby_document_activity_back_iv,R.id.growth_record_activity_send_record_iv})
    @Override
    public void onClick(View v) {
        if (v == backIv) {
            finish();
        } else if (v == sendRecordIv) {
            startActivity(new Intent(GrowthRecordActivity.this,PublishGrowthRecordActivity.class));
        }
    }


    //宝宝成长记录查询列表
    private void requestBabyGrowthList() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("rows","1");
        requestData.put("page","30");  //这里是服务器错误所以先这样调用了
        GrowthRecordInfoPaser lp = new GrowthRecordInfoPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.GET_BABY_GROWTH_RECORD_LIST, requestData, lp);
    }

    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;

        if (nbs.obj instanceof GrowthRecordInfo) {
            if (nbs.isSuccess()) {
                GrowthRecordInfo tii = (GrowthRecordInfo)nbs.obj;
                mContentLv.setAdapter(new GrowthRecordActivityLvAdapter(this,tii.getObj()));
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        }
    }


}
