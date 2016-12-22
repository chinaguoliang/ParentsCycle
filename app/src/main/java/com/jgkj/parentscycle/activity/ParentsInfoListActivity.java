

package com.jgkj.parentscycle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.adapter.MakeClassAddPersonAdapter;
import com.jgkj.parentscycle.adapter.ParentsInfoExpanLvAdapter;
import com.jgkj.parentscycle.bean.AuditParentsInfo;
import com.jgkj.parentscycle.bean.ClassesAndTeachersListItemInfo;
import com.jgkj.parentscycle.bean.ParentsListInfo;
import com.jgkj.parentscycle.bean.TeachersListInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.AuditParentsInfoPaser;
import com.jgkj.parentscycle.json.ParentsListInfoPaser;
import com.jgkj.parentscycle.json.TeacherListPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.utils.LogUtil;
import com.jgkj.parentscycle.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/8/3.
 */
public class ParentsInfoListActivity extends BaseActivity implements View.OnClickListener,NetListener {
    private static final String TAG = "ParentsInfoListActivity";
    @Bind(R.id.title_bar_layout_rel)
    View titleBg;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTv;

    @Bind(R.id.make_class_add_person_activity_content_lv)
    ExpandableListView mContentLv;

    @Bind(R.id.make_class_add_person_activity_submit_btn)
    Button confirmBtn;

    private HashMap<String,String> selectIdsData = new HashMap<String,String>();
    final HashMap<Integer, List<ClassesAndTeachersListItemInfo>> childDataString = new  HashMap<Integer, List<ClassesAndTeachersListItemInfo>>();
    ParentsInfoExpanLvAdapter mParentsInfoExpanLvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_class_add_person_activity);
        ButterKnife.bind(this);
        initViews();
        requestParentsInfoList();
    }

    private void requestParentsInfoList() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        ParentsListInfoPaser lp = new ParentsListInfoPaser();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);
        requestData.put("iseffectives","1");
        NetRequest.getInstance().request(mQueue, this, BgGlobal.PARENTS_LIST_INFO, requestData, lp);
    }

    private void initViews() {
        rightTv.setVisibility(View.GONE);
        titleTv.setText("新加入家长");
//        titleTv.setTextColor(Color.BLACK);
        //titleBg.setBackgroundColor(Color.WHITE);
        mContentLv.setGroupIndicator(null);
        mContentLv.setChildDivider(new BitmapDrawable());
        mContentLv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int id = (int)(Math.random() * 10000);
                ClassesAndTeachersListItemInfo calii = new ClassesAndTeachersListItemInfo();
                calii.setTmpinfoid(id + "");
                calii.setClassname(id + "");
                if (childDataString.get(groupPosition) == null) {
                    ArrayList <ClassesAndTeachersListItemInfo> caliiList = new ArrayList <ClassesAndTeachersListItemInfo>();
                    caliiList.add(calii);
                    childDataString.put(groupPosition,caliiList);
                } else {
                    childDataString.get(groupPosition).add(calii);
                }

                mParentsInfoExpanLvAdapter.notifyDataSetChanged();
            }
        });

        mContentLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (childDataString.get(groupPosition).get(childPosition).isSelected()) {
                    childDataString.get(groupPosition).get(childPosition).setSelected(false);
                } else {
                    childDataString.get(groupPosition).get(childPosition).setSelected(true);
                }

                mParentsInfoExpanLvAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @OnClick({R.id.baby_document_activity_back_iv,R.id.make_class_add_person_activity_submit_btn})

    @Override
    public void onClick(View v) {
        if (v == backIv) {
            //setIdsData();
            finish();
        } else if (v == confirmBtn) {
            setIdsData();
        }
    }


    private void setIdsData(){



        Intent intent = new Intent();
        String idsData = getSelectedIds();
        intent.putExtra("teacher_ids_data",idsData);
        setResult(1, intent);
        requestAuditParentsInfo(idsData);
    }

    private String getSelectedIds() {
        String result = "";
        Map map = new HashMap();
        Iterator iter = childDataString.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object val = entry.getValue();
            if (val != null) {
                List<ClassesAndTeachersListItemInfo> catliList = (List<ClassesAndTeachersListItemInfo>)val;
                int count = catliList.size();
                for (int i = 0 ; i < count ; i++) {
                    ClassesAndTeachersListItemInfo catLii = catliList.get(i);
                    if (catLii.isSelected()) {
                        result = result + catLii.getTmpinfoid();
                    }

                    result = result + ",";
                }
            }
        }

        if (result.contains(",")) {
            result = result.substring(0,result.length()-1);
        }
        LogUtil.d(TAG,"the selected ids:" + result);
        return result;
    }

    private void requestAuditParentsInfo(String id) {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        AuditParentsInfoPaser lp = new AuditParentsInfoPaser();
        requestData.put("iseffectives","1");
        requestData.put("tmpinfoid",id);
        NetRequest.getInstance().request(mQueue, this, BgGlobal.PARENTS_INFO_AUDIT, requestData, lp);

    }

    @Override
    public void uploadImgFinished(Bitmap bitmap, String uploadedKey) {

    }

    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof ParentsListInfo) {
            if (nbs.isSuccess()) {
                ParentsListInfo tii = (ParentsListInfo)nbs.obj;
                mParentsInfoExpanLvAdapter = new ParentsInfoExpanLvAdapter(this,tii.getObj(),childDataString);
                mContentLv.setAdapter(mParentsInfoExpanLvAdapter);
//                mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        makeClassAddPersonAdapter.setSelectPosition(position);
//                    }
//                });
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        } else if (nbs.obj instanceof AuditParentsInfo) {
            if (nbs.isSuccess()) {
                finish();
            }
            ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
        }
    }
}

