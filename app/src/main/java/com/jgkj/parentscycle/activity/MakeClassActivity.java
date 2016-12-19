package com.jgkj.parentscycle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.CreateClassInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.MakeClassPaser;
import com.jgkj.parentscycle.json.TeacherInfoLIstPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.utils.LogUtil;
import com.jgkj.parentscycle.utils.ToastUtil;
import com.jgkj.parentscycle.utils.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/8/2.
 */
public class MakeClassActivity extends BaseActivity implements NetListener,View.OnClickListener{
    private static final String TAG = "MakeClassActivity";
    public static final int ADD_TEACHER = 9;
    public static final int ADD_ADVISER = 10;

    @Bind(R.id.title_bar_layout_rel)
    View titleBg;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView submitTv;

    @Bind(R.id.make_class_activity_add_teacher_rel)
    RelativeLayout addTeacherRel;

    @Bind(R.id.make_class_activity_class_name_et)
    EditText classNameEt;

//    @Bind(R.id.make_class_activity_class_master_name_et)
//    EditText classMasterEt;

    @Bind(R.id.make_class_activity_add_adviser_rel)
    RelativeLayout addAdviserRel;


    private String teacherIdsDataStr = "";
    private String classNameStr = "";
    private String classAdviserStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_class_activity);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        submitTv.setText("提交");
        submitTv.setTextColor(this.getResources().getColor(R.color.text_gray));
        titleTv.setText("建立班级");
        //titleTv.setTextColor(Color.BLACK);
        //titleBg.setBackgroundColor(Color.WHITE);
    }

    @OnClick({R.id.baby_document_activity_back_iv,R.id.make_class_activity_add_teacher_rel,R.id.baby_document_right_title_tv,R.id.make_class_activity_add_adviser_rel})
    @Override
    public void onClick(View v) {
       if (v == backIv) {
            finish();
        } else if (v == addTeacherRel) {
           startActivityForResult(new Intent(this, MakeClassAddPersonActivity.class), ADD_TEACHER);
       } else if (v == addAdviserRel) {
           startActivityForResult(new Intent(this, MakeClassAddPersonActivity.class), ADD_ADVISER);
       } else if (v == submitTv) {
           classNameStr = classNameEt.getText().toString();
//           classMasterStr = classMasterEt.getText().toString();
           if (TextUtils.isEmpty(classNameStr)) {
               ToastUtil.showToast(this,"请输入班级名称",Toast.LENGTH_SHORT);
               return;
           }

//           if (TextUtils.isEmpty(classMasterStr)) {
//               ToastUtil.showToast(this,"请输入班主任名字",Toast.LENGTH_SHORT);
//               return;
//           }

           boolean hadShow = showProgressDialog();
           if (!hadShow) {
               return;
           }

           requestCreateClass();
       }
    }


    //建立班级
    private void requestCreateClass() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID); //暂时传1
        requestData.put("classname", classNameStr);
        requestData.put("classadviser", classAdviserStr);
        ArrayList<String> ids = new ArrayList<String>();
        requestData.put("teacherid", teacherIdsDataStr);
        MakeClassPaser lp = new MakeClassPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.CREATE_CLASS, requestData, lp);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {

            if (requestCode == ADD_TEACHER) {
                teacherIdsDataStr = data.getStringExtra("teacher_ids_data");
                if (TextUtils.isEmpty(teacherIdsDataStr)) {
                    teacherIdsDataStr = "";
                }
            } else if (requestCode == ADD_ADVISER) {

                classAdviserStr = data.getStringExtra("teacher_ids_data");
                if (TextUtils.isEmpty(classAdviserStr)) {
                    classAdviserStr = "";
                }
            }

            //LogUtil.d(TAG,teacherIdsDataStr);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof CreateClassInfo) {
            CreateClassInfo ccI = (CreateClassInfo)nbs.obj;
            if (nbs.isSuccess()) {
                finish();
            }
            ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap,String uploadedKey) {

    }
}
