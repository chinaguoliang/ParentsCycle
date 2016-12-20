package com.jgkj.parentscycle.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.adapter.AccountInfoAdapter;
import com.jgkj.parentscycle.adapter.MangementClassExpanLvAdapter;
import com.jgkj.parentscycle.adapter.ModifyClassDialogLvAdapter;
import com.jgkj.parentscycle.bean.ClassedAndTeachersListInfo;
import com.jgkj.parentscycle.bean.ClassesAndTeachersListItemInfo;
import com.jgkj.parentscycle.bean.MakeClassAddPersonInfo;
import com.jgkj.parentscycle.bean.PerfectInfoInfo;
import com.jgkj.parentscycle.bean.TeacherInfoListInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.ClassedAndTeachersPaser;
import com.jgkj.parentscycle.json.PerfectInfoPaser;
import com.jgkj.parentscycle.json.TeacherInfoLIstPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.user.UserInfo;
import com.jgkj.parentscycle.utils.AsyncImageUtil;
import com.jgkj.parentscycle.utils.CircularImage;
import com.jgkj.parentscycle.utils.LogUtil;
import com.jgkj.parentscycle.utils.ToastUtil;
import com.jgkj.parentscycle.utils.UtilTools;
import com.jgkj.parentscycle.widget.ListViewForScrollView;
import com.jgkj.parentscycle.widget.SexSelectDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/7/18.
 */
public class AccountInfoActivity extends BaseActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,NetListener ,SexSelectDialog.SexSlectDialogFinish {
    private static final String TAG = "AccountInfoActivity";
    @Bind(R.id.account_info_activity_lv)
    ListViewForScrollView mContentLv;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTitleTv;

    @Bind(R.id.title_bar_layout_rel)
    RelativeLayout mWrapTitleRel;

    @Bind(R.id.account_info_activity_save_btn)
    Button saveBtn;

    @Bind(R.id.account_info_activity_name_tv)
    TextView nameTv;

    @Bind(R.id.account_info_activity_phone_tv)
    TextView phoneTv;

    @Bind(R.id.hall_mine_fragment_lv_header_user_icon_iv)
    CircularImage mIconIv;

    AccountInfoAdapter mAccountInfoAdapter;
    Dialog mModifyClassDialog;
    String classesIds = ""; //classid 的组合
    String selBirthday = "";
    int selSex = -1;
    TeacherInfoListInfo mTeacherInfoListInfo;

    private String headUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info_activity_layout);
        ButterKnife.bind(this);
        titleTv.setText("帐号信息");
        rightTitleTv.setVisibility(View.GONE);
        mWrapTitleRel.setBackgroundColor(Color.parseColor("#00000000"));
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 5) {
                    showDateDialog();
                } else if (position == 7) {
                    //帐号信息
                    startActivity(new Intent(AccountInfoActivity.this,AccountSafeActivity.class));
                } else if (position == 3) {
                    SexSelectDialog.showSexSelectDialog(AccountInfoActivity.this,AccountInfoActivity.this);
                } else if (position == 9) {
                    //选择班级
                    requestClassListBySchoolId();
                }
            }
        });

        requestNet();
    }

    //按学校ID 查询班级列表 （发布选择班级展示）
    private void requestClassListBySchoolId() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);
        ClassedAndTeachersPaser lp = new ClassedAndTeachersPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.SEARCH_CLASS_LIST_BY_SCHOOL_ID, requestData, lp);
    }


    @OnClick({R.id.baby_document_activity_back_iv,R.id.account_info_activity_save_btn,R.id.hall_mine_fragment_lv_header_user_icon_iv})
    @Override
    public void onClick(View v) {
       if (v == backIv) {
           finish();
       } else if (v == saveBtn) {
//           if (TextUtils.isEmpty(classesIds)) {
//               ToastUtil.showToast(v.getContext(),"请选择班级",Toast.LENGTH_SHORT);
//               return;
//           }
           requestSave();
       } else if (v == mIconIv) {
           showChangePhotoDialog();
       }
    }

    private void showDateDialog() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        int year=d.get(Calendar.YEAR);
        int month=d.get(Calendar.MONTH);
        int day=d.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,this,year,month,day);
        dpd.getWindow().setBackgroundDrawable(new BitmapDrawable()); //设置为透明
        dpd.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        List<String> dataList = mAccountInfoAdapter.getList();
        String date = year + "-" + (monthOfYear + 1) + "-" +dayOfMonth;
//        dataList.remove(4);
        dataList.set(5,"出生日期_" + date);
        selBirthday = date;
        mAccountInfoAdapter.getData().put(5,date);
        mAccountInfoAdapter.notifyDataSetChanged();
    }

    private void requestNet() {
        if (UserInfo.isLogined) {
            showProgressDialog();
            HashMap<String, String> requestData = new HashMap<String, String>();
            requestData.put("tmpinfoid", UserInfo.loginInfo.getRole().getId());
            requestData.put("schoolid", ConfigPara.SCHOOL_ID);  //暂时传1
            TeacherInfoLIstPaser lp = new TeacherInfoLIstPaser();
            NetRequest.getInstance().request(mQueue, this, BgGlobal.TEACHER_INFO_LIST, requestData, lp);
        }
    }

    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof ClassedAndTeachersListInfo) {
            if (nbs.isSuccess()) {
                ClassedAndTeachersListInfo tii = (ClassedAndTeachersListInfo)nbs.obj;
                initListView(tii.getDataList());
            } else {
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        }else if (nbs.obj instanceof PerfectInfoInfo) {
            if (nbs.isSuccess()) {
                finish();
            }

            ToastUtil.showToast(this, nbs.getMsg(), Toast.LENGTH_SHORT);
        } else {
            if (nbs.isSuccess()) {
                TeacherInfoListInfo tii = (TeacherInfoListInfo)nbs.obj;
                initListView(tii);
                LogUtil.d(TAG,"success");
            } else {
                initListView(new TeacherInfoListInfo());
                ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        }
    }

    private void initListView(TeacherInfoListInfo tii) {
        mTeacherInfoListInfo = tii;
        classesIds = tii.getClassid();

        nameTv.setText(tii.getNickname());
        phoneTv.setText(tii.getPhone());
        AsyncImageUtil.asyncLoadImage(mIconIv,
                tii.getHeadportrait(),
                R.mipmap.user_default_icon, true, false);
        ArrayList<String> data = new ArrayList<String>();
        data.add("昵称_" + tii.getNickname());
        data.add("姓名_" + tii.getTeachername());
        data.add("地区_" + "");

        if (TextUtils.equals(tii.getTeachersex(),"0")) {
            data.add("性别_女");
        } else if (TextUtils.equals(tii.getTeachersex(),"1")) {
            data.add("性别_男");
        } else {
            data.add("性别_ ");
        }

        data.add("民族_" + tii.getNationality());
        data.add("出生日期_" + tii.getBirthdate());


        data.add("手机号_" + tii.getPhone());
        data.add("账户安全_ ");
//        data.add("捆绑微信_ ");
//        data.add("捆绑QQ_ ");
//        data.add("选择班级_");
        mAccountInfoAdapter = new AccountInfoAdapter(this, data);
        mContentLv.setAdapter(mAccountInfoAdapter);
    }

    private void initListView(final List<ClassesAndTeachersListItemInfo> dataList) {
        int count = dataList.size();
        HashMap <String,String> nameIntMap = new HashMap <String,String>();
        ArrayList< MakeClassAddPersonInfo > sourceData = new ArrayList<MakeClassAddPersonInfo>();
        for (int i = 0 ; i < count ; i++) {
            ClassesAndTeachersListItemInfo catli = dataList.get(i);
            String className = catli.getClassname();
            String classId = catli.getClassid();
            if (nameIntMap.get(classId) != null) {
                continue;
            }

            nameIntMap.put(classId,classId);
            MakeClassAddPersonInfo mcpi = new MakeClassAddPersonInfo();
            mcpi.setId(classId);
            mcpi.setName(className);
            sourceData.add(mcpi);
        }
        showModifyClassDialog(sourceData);
    }


    private void showModifyClassDialog(ArrayList< MakeClassAddPersonInfo > sourceData) {
        mModifyClassDialog = new Dialog(this, R.style.DialogTheme);
        mModifyClassDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        View contentView = LayoutInflater.from(this).inflate(R.layout.modify_class_dialog, null);
        Button confirm = (Button) contentView.findViewById(R.id.modify_class_dialog_confirm_btn);
        ListView classLv = (ListView) contentView.findViewById(R.id.modify_class_dialog_lv);
        confirm.setOnClickListener(changePhotoListener);

        final ModifyClassDialogLvAdapter mcda = new ModifyClassDialogLvAdapter(this,sourceData);
        classLv.setAdapter(mcda);
        String classIds = mTeacherInfoListInfo.getClassid();
        String idArray[] = classIds.split(",");
        for (int i = 0 ; i < idArray.length ; i++) {
            mcda.setClassidShowSelected(idArray[i]);
        }


        classLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mcda.setSelectPosition(position);

            }
        });

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                classesIds = mcda.getIdsData();
                mModifyClassDialog.dismiss();
            }
        });

        mModifyClassDialog.setContentView(contentView);
        mModifyClassDialog.setCanceledOnTouchOutside(true);
        mModifyClassDialog.show();

        WindowManager.LayoutParams params = mModifyClassDialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = UtilTools.SCREEN_WIDTH;
        mModifyClassDialog.getWindow().setAttributes(params);
    }


    public void requestSave() {
        HashMap<String, String> requestData = new HashMap<String, String>();
        HashMap<Integer,String> data = mAccountInfoAdapter.getData();
        requestData.put("analysis",mTeacherInfoListInfo.getAnalysis());


        if (TextUtils.isEmpty(selBirthday)) {
            requestData.put("birthdate",mTeacherInfoListInfo.getBirthdate());
        } else {
            requestData.put("birthdate",selBirthday);
        }

        if (TextUtils.isEmpty(classesIds)) {
            requestData.put("classid",mTeacherInfoListInfo.getClassid());
        } else {
            requestData.put("classid",classesIds);
        }

        if (TextUtils.isEmpty(headUrl)) {
            requestData.put("headportrait",mTeacherInfoListInfo.getHeadportrait());
        } else {
            requestData.put("headportrait",headUrl);
        }
        requestData.put("kbwx",mTeacherInfoListInfo.getKbwx()); //1: 是  0：否
        requestData.put("kbqq",mTeacherInfoListInfo.getKbqq());

        String nationality = data.get(2);
        if (TextUtils.isEmpty(nationality)) {
            requestData.put("nationality",mTeacherInfoListInfo.getNationality());
        } else {
            requestData.put("nationality",nationality);
        }

        if (TextUtils.isEmpty(data.get(0))) {
            requestData.put("nickname",mTeacherInfoListInfo.getNickname());
        } else {
            requestData.put("nickname",data.get(0));
        }

        requestData.put("onthejob",mTeacherInfoListInfo.getOnthejob()); // 1:在职  0： 离职
        requestData.put("permissions",mTeacherInfoListInfo.getPermissions());
        String phone = data.get(6);
        if (TextUtils.isEmpty(phone)) {
            requestData.put("phone",mTeacherInfoListInfo.getPhone());
        } else {
            requestData.put("phone",phone);
        }


        requestData.put("schoolname",mTeacherInfoListInfo.getSchoolname());
        requestData.put("teacherid",mTeacherInfoListInfo.getTeacherid());
        if (TextUtils.isEmpty(data.get(1))) {
            requestData.put("teachername",mTeacherInfoListInfo.getTeachername());
        } else {
            requestData.put("teachername",data.get(1));
        }

        if (selSex == -1) {
            requestData.put("teachersex",mTeacherInfoListInfo.getTeachersex());
        } else {
            requestData.put("teachersex",selSex + "");
        }


        requestData.put("tmpinfoid", UserInfo.loginInfo.getRole().getId());
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);  //暂时传1
        PerfectInfoPaser lp = new PerfectInfoPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.TEACHER_INFO_SAVE, requestData, lp);
    }


    @Override
    public void uploadImgFinished(Bitmap bitmap,String uploadedKey) {
        headUrl = BgGlobal.IMG_SERVER_PRE_URL + uploadedKey;
        AsyncImageUtil.asyncLoadImage(mIconIv,
                headUrl,
                R.mipmap.user_default_icon, true, false);
    }

    @Override
    public void finishSlecct(int index) {
        List<String> dataList = mAccountInfoAdapter.getList();
        if (index == 1) {
            dataList.set(3,"性别_男");
            selSex = 1;
        } else if (index == 0) {
            dataList.set(3,"性别_女");
            selSex = 0;
        }

        mAccountInfoAdapter.getData().put(3,index + "");
        mAccountInfoAdapter.notifyDataSetChanged();
    }
}
