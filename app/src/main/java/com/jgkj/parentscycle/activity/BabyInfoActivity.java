package com.jgkj.parentscycle.activity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.TeacherInfoListInfo;
import com.jgkj.parentscycle.user.UserInfo;
import com.jgkj.parentscycle.widget.ListViewForScrollView;
import com.jgkj.parentscycle.widget.SexSelectDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/8/30.
 */
public class BabyInfoActivity extends BaseActivity implements View.OnClickListener,SexSelectDialog.SexSlectDialogFinish,DatePickerDialog.OnDateSetListener{
    @Bind(R.id.title_bar_layout_rel)
    View titleBg;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTv;


    @Bind(R.id.baby_info_activity_name_rel)
    RelativeLayout nameRel;

    @Bind(R.id.baby_info_activity_name_et)
    EditText nameet;


    @Bind(R.id.baby_info_activity_sex_rel)
    RelativeLayout sexRel;

    @Bind(R.id.baby_info_activity_sex_tv)
    TextView sexTv;


    @Bind(R.id.baby_info_activity_birthday_rel)
    RelativeLayout birthdayRel;

    @Bind(R.id.baby_info_activity_birthday_tv)
    TextView birthdayTv;


    @Bind(R.id.baby_info_activity_age_rel)
    RelativeLayout ageRel;

    @Bind(R.id.baby_info_activity_age_et)
    EditText ageEt;


    @Bind(R.id.baby_info_activity_to_school_time_rel)
    RelativeLayout toSchoolTimeRel;

    @Bind(R.id.baby_info_activity_to_school_time_tv)
    TextView toSchoolTimeTv;


    @Bind(R.id.baby_info_activity_farther_rel)
    RelativeLayout fartherRel;

    @Bind(R.id.baby_info_activity_farther_et)
    EditText fartherEt;


    @Bind(R.id.baby_info_activity_farther_phone_rel)
    RelativeLayout fartherPhoneRel;

    @Bind(R.id.baby_info_activity_farther_phone_et)
    EditText fartherPhoneEt;


    @Bind(R.id.baby_info_activity_monther_rel)
    RelativeLayout montherRel;

    @Bind(R.id.baby_info_activity_monther_et)
    EditText montherEt;


    @Bind(R.id.baby_info_activity_monther_phone_rel)
    RelativeLayout montherPhoneRel;

    @Bind(R.id.baby_info_activity_monther_phone_et)
    EditText montherPhoneEt;

    @Bind(R.id.baby_info_activity_belong_class_rel)
    RelativeLayout belongClassRel;

    @Bind(R.id.baby_info_activity_belong_class_tv)
    TextView belongClassTv;


    @Bind(R.id.baby_info_activity_join_class_rel)
    RelativeLayout joinClassRel;

    @Bind(R.id.baby_info_activity_transfer_class_rel)
    RelativeLayout transferClassRel;

    @Bind(R.id.baby_info_activity_leave_school_rel)
    RelativeLayout leaveSchoolRel;

    int selSex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_info_activity);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        titleBg.setBackgroundColor(0x00000000);
        rightTv.setVisibility(View.GONE);
        titleTv.setText("宝宝详情");
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap, String uploadedKey) {

    }

    @OnClick({R.id.baby_document_activity_back_iv,
              R.id.baby_info_activity_join_class_rel,
              R.id.baby_info_activity_sex_rel,
            R.id.baby_info_activity_birthday_rel,
            R.id.baby_info_activity_transfer_class_rel,
            R.id.baby_info_activity_leave_school_rel,
            R.id.baby_info_activity_to_school_time_rel


    })
    @Override
    public void onClick(View v) {
        if (v == backIv) {
            finish();
        } else if (v == joinClassRel) {

        } else if (v == sexRel) {
            SexSelectDialog.showSexSelectDialog(BabyInfoActivity.this,BabyInfoActivity.this);
        } else if (v == birthdayRel) {
            showDateDialog(1);
        } else if (v == transferClassRel) {

        } else if (v == leaveSchoolRel) {

        } else if (v == toSchoolTimeRel) {
            showDateDialog(2);
        }
    }


    private void showDateDialog(int flag) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        int year=d.get(Calendar.YEAR);
        int month=d.get(Calendar.MONTH);
        int day=d.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,this,year,month,day);
        dpd.getDatePicker().setTag(flag);
        dpd.getWindow().setBackgroundDrawable(new BitmapDrawable()); //设置为透明
        dpd.show();
    }

    @Override
    public void finishSlecct(int index) {
        if (index == 1) {
            sexTv.setText("男");
            selSex = 1;
        } else if (index == 0) {
            sexTv.setText("女");
            selSex = 0;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" +dayOfMonth;
        int flag = Integer.parseInt(view.getTag().toString());
        if (flag == 1) {
            birthdayTv.setText(date);
        } else if (flag == 2) {
            toSchoolTimeTv.setText(date);
        }
    }
}
