package com.jgkj.parentscycle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.TeacherInfoListInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.TeacherInfoLIstPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.user.UserInfo;
import com.jgkj.parentscycle.utils.ToastUtil;
import com.jgkj.parentscycle.utils.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/7/18.
 */
public class SchoolInfoActivity extends BaseActivity implements NetListener,View.OnClickListener{
    @Bind(R.id.title_bar_layout_rel)
    View titleBg;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTv;

    @Bind(R.id.shool_info_activity_job_rel)
    RelativeLayout jobRel;

    @Bind(R.id.shool_info_activity_permission_rel)
    RelativeLayout permissionRel;

    @Bind(R.id.shool_info_activity_add_school_rel)
    RelativeLayout addSchoolRel;

    @Bind(R.id.shool_info_activity_make_class_rel)
    RelativeLayout makeClassRel;

    @Bind(R.id.shool_info_activity_teacher_mangement_rel)
    RelativeLayout teacherManagementRel;

    @Bind(R.id.shool_info_activity_class_mangement_rel)
    RelativeLayout classMangementRel;

    @Bind(R.id.shool_info_activity_leave_school_rel)
    RelativeLayout leaveSchoolRel;

    @Bind(R.id.school_info_activity_job_tv)
    TextView jobTv;

    @Bind(R.id.school_info_activity_permission_tv)
    TextView permissionTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info_activity);
        ButterKnife.bind(this);
        initViews();


        //requestCreateClass();
        //requestModifyTeacherPermission();
        //requestClassTeacherMangement();
        //requestAnnouncementPublish();
        //requestClassListBySchoolId();
        //requestAnnouncementList();
//        requestAnnouncementCommentSave();
        //requestTeacherInfo();
    }

    private void initViews() {
        titleTv.setText("学校信息");
        rightTv.setVisibility(View.GONE);
    }


    private void requestTeacherInfo() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("tmpinfoid", UserInfo.loginInfo.getRole().getId());
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);  //暂时传1
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.TEACHER_INFO_LIST, requestData, lp);
    }


    //修改教师权限
    private void requestModifyTeacherPermission() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("ostmpinfoid", UserInfo.loginInfo.getRole().getId());  //登录时ID
        requestData.put("permissions", UserInfo.LOOK_AFTER_TEACHER_PERMISSION);
        requestData.put("analysis", "laosh11i");
        requestData.put("teacherid", UserInfo.loginInfo.getRole().getId());
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.MODIFY_TEACHER_PERMISSION, requestData, lp);
    }

    //班级教师管理
    private void requestClassTeacherMangement() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);
        requestData.put("classid", "6");
        requestData.put("classname", "apple class");
        ArrayList <String> ids = new ArrayList<String>();
        ids.add("5");
        ids.add("9");
        ids.add("33");
        ids.add("90");
        requestData.put("teacherids", UtilTools.getRequestParams(ids));
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.CLASS_TEACHER_MANGEMENT, requestData, lp);
    }

    //公告发布
    private void requestAnnouncementPublish() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("title", "论三国");
        requestData.put("bannerimg", "6");
        requestData.put("announcement", "apple class");
        requestData.put("selectrange", "6");
        ArrayList<String> result = new ArrayList<String>();
        result.add("1.jpg");
        result.add("2.jpg");
        requestData.put("imags", UtilTools.getRequestParams(result));
        requestData.put("voidurls", "6");
        requestData.put("ospersion", "apple class");
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.ANNOUNCEMENT_PUBLISH, requestData, lp);
    }

    //按学校ID 查询班级列表 （发布选择班级展示）
    private void requestClassListBySchoolId() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.SEARCH_CLASS_LIST_BY_SCHOOL_ID, requestData, lp);
    }

    //公告列表
    private void requestAnnouncementList() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("id", UserInfo.loginInfo.getRole().getId());
        requestData.put("page", "1");
        requestData.put("rows", "10");
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.ANNOUNCEMENT_LIST, requestData, lp);
    }


    //公告评论保存
    private void requestAnnouncementCommentSave() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("id", UserInfo.loginInfo.getRole().getId());
        requestData.put("title", "奥巴马");
        requestData.put("bannerimg", "1.jpg");
        requestData.put("announcement", "bullshit");
        requestData.put("selectrange", "10");
        ArrayList<String> imgs = new ArrayList<String>();
        imgs.add("a.jpg");
        imgs.add("b.jpg");
        requestData.put("imags", UtilTools.getRequestParams(imgs));
        requestData.put("voidurls", "6.avi");
        requestData.put("ospersion", "10");
        requestData.put("criticsid","2");
        requestData.put("announid","2");
        requestData.put("isdispy","1");
        requestData.put("critics","奥巴马 bullshit");
        TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.ANNOUNCEMENT_COMMENT_SAVE, requestData, lp);
    }


    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof TeacherInfoListInfo) {
            if (nbs.isSuccess()) {
                TeacherInfoListInfo tii = (TeacherInfoListInfo)nbs.obj;
                jobTv.setText(tii.getAnalysis());
                permissionTv.setText(UserInfo.getTitleByPermission(tii.getPermissions()));
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        }

    }

    @OnClick({R.id.baby_document_activity_back_iv,R.id.shool_info_activity_make_class_rel,R.id.shool_info_activity_teacher_mangement_rel,R.id.shool_info_activity_class_mangement_rel})
    @Override
    public void onClick(View v) {
        if (v == backIv) {
            finish();
        } else if (v == makeClassRel) {
            startActivity(new Intent(SchoolInfoActivity.this,MakeClassActivity.class));
        } else if (v == teacherManagementRel) {
            startActivity(new Intent(this,ManageTeachersListActivity.class));
        } else if (v == classMangementRel) {
            startActivity(new Intent(this,MangementClassActivity.class));
        }
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap,String uploadedKey) {

    }
}
