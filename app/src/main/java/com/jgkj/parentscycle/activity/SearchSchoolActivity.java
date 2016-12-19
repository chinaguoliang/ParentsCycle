package com.jgkj.parentscycle.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.adapter.AddSchoolExpanLvAdapter;
import com.jgkj.parentscycle.bean.ClassedAndTeachersListInfo;
import com.jgkj.parentscycle.bean.ClassesAndTeachersListItemInfo;
import com.jgkj.parentscycle.bean.SearchSchoolInfo;
import com.jgkj.parentscycle.bean.SearchSchoolItemInfo;
import com.jgkj.parentscycle.bean.TeacherJoinSchoolInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.ClassedAndTeachersPaser;
import com.jgkj.parentscycle.json.SearchSchoolInfoPaser;
import com.jgkj.parentscycle.json.TeacherInfoLIstPaser;
import com.jgkj.parentscycle.json.TeacherJoinSchoolInfoPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.user.UserInfo;
import com.jgkj.parentscycle.utils.ToastUtil;
import com.jgkj.parentscycle.utils.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/10/10.
 */
public class SearchSchoolActivity extends BaseActivity implements View.OnClickListener,NetListener{
    @Bind(R.id.search_school_activity_search_btn)
    Button searchBtn;

    @Bind(R.id.search_school_activity_ep_lv)
    ExpandableListView expandLv;

    @Bind(R.id.search_school_activity_search_content_et)
    EditText  searchContentEt;

    String searchContentStr;

    Dialog addSchoolDialog;
    AddSchoolExpanLvAdapter mAddSchoolExpanLvAdapter;

    private int currentGroupId;
    final HashMap<Integer, List<ClassesAndTeachersListItemInfo>> childDataString = new  HashMap<Integer, List<ClassesAndTeachersListItemInfo>>();

    @Override
    public void uploadImgFinished(Bitmap bitmap, String uploadedKey) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_school_activity);
        ButterKnife.bind(this);
        expandLv.setGroupIndicator(null);
        expandLv.setChildDivider(new BitmapDrawable());
    }

    //学校查询列表
    private void requestSearchSchoolList() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("rows","1000");
        requestData.put("page","1");
        requestData.put("schoolname",searchContentStr);
        SearchSchoolInfoPaser lp = new SearchSchoolInfoPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.QUERY_SCHOOL_LIST_BY_SCHOOL_NAME, requestData, lp);
    }

    //教师加入学校
    private void requestTeacherJoinSchool(String schoolId,String classId) {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid",schoolId);
        requestData.put("classid",classId);
        requestData.put("teacherids", UserInfo.loginInfo.getInfo().getTmpinfoid());
        TeacherJoinSchoolInfoPaser lp = new TeacherJoinSchoolInfoPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.CLASS_TEACHER_MANGEMENT, requestData, lp);
    }



    private void initGroupItem(List<SearchSchoolItemInfo> groupTitleList) {

        mAddSchoolExpanLvAdapter = new AddSchoolExpanLvAdapter(this,groupTitleList,childDataString);
        expandLv.setAdapter(mAddSchoolExpanLvAdapter);

        expandLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ClassesAndTeachersListItemInfo cali = childDataString.get(groupPosition).get(childPosition);
                showWhetherAddSchoolDialog(cali);
                return false;
            }
        });


        expandLv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                currentGroupId = groupPosition;
                SearchSchoolItemInfo searchSchoolItemInfo = (SearchSchoolItemInfo)mAddSchoolExpanLvAdapter.getGroup(groupPosition);
                requestClassListBySchoolId(searchSchoolItemInfo.getSchoolid());
            }
        });
    }


    @OnClick({R.id.search_school_activity_search_btn})
    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            searchContentStr = searchContentEt.getText().toString();
            if (TextUtils.isEmpty(searchContentStr)) {
                ToastUtil.showToast(this,"请输入搜索内容", Toast.LENGTH_SHORT);
                return;
            }

            requestSearchSchoolList();
        }
    }


    //按学校ID 查询班级列表 （发布选择班级展示）
    private void requestClassListBySchoolId(String schoolId) {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", schoolId);
        ClassedAndTeachersPaser lp = new ClassedAndTeachersPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.SEARCH_CLASS_LIST_BY_SCHOOL_ID, requestData, lp);
    }

    private void showWhetherAddSchoolDialog(final ClassesAndTeachersListItemInfo cali) {
        addSchoolDialog = new Dialog(this, R.style.DialogTheme);
        addSchoolDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        View contentView = LayoutInflater.from(this).inflate(R.layout.add_school_dialog, null);
        Button cancel = (Button) contentView.findViewById(R.id.add_school_dialog_no_btn);
        Button yes = (Button) contentView.findViewById(R.id.add_school_dialog_yes_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSchoolDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSchoolDialog.dismiss();

                requestTeacherJoinSchool(cali.getSchoolid(),cali.getClassid());
            }
        });


        addSchoolDialog.setContentView(contentView);
        addSchoolDialog.setCanceledOnTouchOutside(true);
        addSchoolDialog.show();

        WindowManager.LayoutParams params = addSchoolDialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = UtilTools.SCREEN_WIDTH - UtilTools.SCREEN_WIDTH / 5;
        addSchoolDialog.getWindow().setAttributes(params);
    }


    private void showTipDialog() {
        addSchoolDialog = new Dialog(this, R.style.DialogTheme);
        addSchoolDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        View contentView = LayoutInflater.from(this).inflate(R.layout.add_school_verify_dialog, null);
        Button yes = (Button) contentView.findViewById(R.id.add_school_verify_dialog_yes_btn);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSchoolDialog.dismiss();
                finish();
            }
        });


        addSchoolDialog.setContentView(contentView);
        addSchoolDialog.setCanceledOnTouchOutside(true);
        addSchoolDialog.show();

        WindowManager.LayoutParams params = addSchoolDialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = UtilTools.SCREEN_WIDTH - UtilTools.SCREEN_WIDTH / 5;
        addSchoolDialog.getWindow().setAttributes(params);
    }


    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof SearchSchoolInfo) {
            if (nbs.isSuccess()) {
                SearchSchoolInfo tii = (SearchSchoolInfo)nbs.obj;
                initGroupItem(tii.getObj());
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        } else if (nbs.obj instanceof ClassedAndTeachersListInfo) {
            if (nbs.isSuccess()) {
                ClassedAndTeachersListInfo tii = (ClassedAndTeachersListInfo)nbs.obj;
//                initListView(tii.getDataList());

                childDataString.put(currentGroupId,tii.getDataList());

//                if (!expandLv.isGroupExpanded(currentGroupId)) {
//                    expandLv.performItemClick(expandLv.getAdapter().getView(currentGroupId,null,null),currentGroupId,expandLv.getAdapter().getItemId(currentGroupId));
//                }
                mAddSchoolExpanLvAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        } else if (nbs.obj instanceof TeacherJoinSchoolInfo) {
            if (nbs.isSuccess()) {
                showTipDialog();
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        }
    }
}
