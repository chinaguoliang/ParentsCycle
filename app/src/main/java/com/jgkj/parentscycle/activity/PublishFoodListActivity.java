package com.jgkj.parentscycle.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.adapter.ModifyClassDialogLvAdapter;
import com.jgkj.parentscycle.bean.ClassedAndTeachersListInfo;
import com.jgkj.parentscycle.bean.ClassesAndTeachersListItemInfo;
import com.jgkj.parentscycle.bean.MakeClassAddPersonInfo;
import com.jgkj.parentscycle.bean.PublishFoodLishInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.ClassedAndTeachersPaser;
import com.jgkj.parentscycle.json.PublishFoodListInfoPaser;
import com.jgkj.parentscycle.json.ResetPasswordPaser;
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
 * Created by chen on 16/8/20.
 */
public class PublishFoodListActivity extends BaseActivity implements View.OnClickListener,NetListener{
    @Bind(R.id.back_iv)
    ImageView backIv;

    @Bind(R.id.publish_food_list_activity_sv_ll)
    LinearLayout contentSvLl;

    @Bind(R.id.publish_food_list_activity_publish_btn)
    Button publishBtn;

    @Bind(R.id.publish_food_list_activity_add_food_btn)
    Button addFoodBtn;

    @Bind(R.id.publish_food_list_activity_week_1_tv)
    TextView mWeek1;

    @Bind(R.id.publish_food_list_activity_week_2_tv)
    TextView mWeek2;

    @Bind(R.id.publish_food_list_activity_week_3_tv)
    TextView mWeek3;

    @Bind(R.id.publish_food_list_activity_week_4_tv)
    TextView mWeek4;

    @Bind(R.id.publish_food_list_activity_week_5_tv)
    TextView mWeek5;

    @Bind(R.id.publish_food_list_activity_week_6_tv)
    TextView mWeek6;

    @Bind(R.id.publish_food_list_activity_week_7_tv)
    TextView mWeek7;

    @Bind(R.id.course_activity_filter_iv)
    ImageView selClassIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    private String weekNumStr = "";

    LayoutInflater mInflater;

    private LinearLayout currAddPicLl;

    private int blueColor;

    private String foodNumStr = "早餐";

    private String foodImgs = "";
    private String foodDesc = "";
    private MakeClassAddPersonInfo makeClassAddPersonInfo = new MakeClassAddPersonInfo();
    Dialog mModifyClassDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_food_list_activity);
        ButterKnife.bind(this);
        mInflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        blueColor = this.getResources().getColor(R.color.main_blue_color);

    }

    @OnClick({R.id.back_iv,R.id.publish_food_list_activity_publish_btn,
            R.id.publish_food_list_activity_week_1_tv,
            R.id.publish_food_list_activity_week_2_tv,
            R.id.publish_food_list_activity_week_3_tv,
            R.id.publish_food_list_activity_week_4_tv,
            R.id.publish_food_list_activity_week_5_tv,
            R.id.publish_food_list_activity_week_6_tv,
            R.id.publish_food_list_activity_week_7_tv,
            R.id.publish_food_list_activity_add_food_btn,
            R.id.course_activity_filter_iv
    })

    @Override
    public void onClick(View v) {
        if (v == backIv) {
            finish();
        } else if (v == addFoodBtn) {
            addFoodList(contentSvLl);
        } else if (v == mWeek1) {
            resetBg();
            mWeek1.setBackgroundColor(blueColor);
            weekNumStr = "1";
        } else if (v == mWeek2) {
            resetBg();
            mWeek2.setBackgroundColor(blueColor);
            weekNumStr = "2";
        } else if (v == mWeek3) {
            resetBg();
            mWeek3.setBackgroundColor(blueColor);
            weekNumStr = "3";
        } else if (v == mWeek4) {
            resetBg();
            mWeek4.setBackgroundColor(blueColor);
            weekNumStr = "4";
        } else if (v == mWeek5) {
            resetBg();
            mWeek5.setBackgroundColor(blueColor);
            weekNumStr = "5";
        } else if (v == mWeek6) {
            resetBg();
            mWeek6.setBackgroundColor(blueColor);
            weekNumStr = "6";
        } else if (v == mWeek7) {
            resetBg();
            mWeek7.setBackgroundColor(blueColor);
            weekNumStr = "7";
        } else if (v == publishBtn) {
            requestPublishFoodList();
        } else if (v == selClassIv) {
            requestClassListBySchoolId();
        }
    }

    private void resetBg() {
        mWeek1.setBackgroundColor(Color.WHITE);
        mWeek2.setBackgroundColor(Color.WHITE);
        mWeek3.setBackgroundColor(Color.WHITE);
        mWeek4.setBackgroundColor(Color.WHITE);
        mWeek5.setBackgroundColor(Color.WHITE);
        mWeek6.setBackgroundColor(Color.WHITE);
        mWeek7.setBackgroundColor(Color.WHITE);
    }

    private void addFoodList(final LinearLayout viewLl) {
        final LinearLayout courseItem = (LinearLayout)mInflater.inflate(R.layout.publish_food_list_item,null);
        courseItem.setTag("courseItem");
        int childCount = courseItem.getChildCount();
        viewLl.addView(courseItem,childCount - 1);
        TextView foodNum = (TextView)courseItem.findViewById(R.id.publish_food_list_item_food_period_tv);
        ImageView deleteIv = (ImageView) courseItem.findViewById(R.id.publish_food_list_item_del_iv);
        deleteIv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewLl.removeView(courseItem);
            }
        });

        final TextView publishPicTv = (TextView) courseItem.findViewById(R.id.publish_food_list_item_add_pic_tv);
        final LinearLayout addPicLl = (LinearLayout) courseItem.findViewById(R.id.publish_food_list_item_add_pic_ll);


        ViewTreeObserver companyTreeObserver = publishPicTv.getViewTreeObserver();
        companyTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                publishPicTv.getViewTreeObserver().removeOnPreDrawListener(this);
                addPicLl.setTag(publishPicTv.getWidth());
                return true;
            }
        });

        publishPicTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showChangePhotoDialog();
                currAddPicLl = addPicLl;
            }
        });

        foodNum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap,String uploadedKey) {
        ImageView iv = new ImageView(this);
        int width = Integer.parseInt(currAddPicLl.getTag().toString());
        iv.setImageBitmap(bitmap);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,width);
        iv.setPadding(10,10,10,10);
        String imgUrl = BgGlobal.IMG_SERVER_PRE_URL + uploadedKey;
        iv.setLayoutParams(params);
        iv.setTag(imgUrl);
        currAddPicLl.addView(iv,0);
    }


    //食谱发布
    public void requestPublishFoodList() {
        getImgsList();
        if (!checkInput()) {
            return;
        }

        showProgressDialog();

        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("weeknum", weekNumStr);
        requestData.put("meal",foodNumStr);
        requestData.put("foodimgs",foodImgs);
        requestData.put("fooddescription",foodDesc);
        requestData.put("classid", makeClassAddPersonInfo.getId());
        requestData.put("shoolid", ConfigPara.SCHOOL_ID);
        requestData.put("osperion",UserInfo.loginInfo.getInfo().getNickname());

        PublishFoodListInfoPaser lp = new PublishFoodListInfoPaser();
        NetRequest.getInstance().request(mQueue, this,
                BgGlobal.PUBLISH_FOOD_LIST, requestData, lp);
    }


    private boolean checkInput() {
        if (TextUtils.isEmpty(weekNumStr)) {
            ToastUtil.showToast(this,"请选择星期",Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(foodImgs)) {
            ToastUtil.showToast(this,"请添加照片",Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(foodDesc)) {
            ToastUtil.showToast(this,"请添加食谱描述",Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(makeClassAddPersonInfo.getId())) {
            ToastUtil.showToast(this,"请选择班级",Toast.LENGTH_SHORT);
            return false;
        }
        return true;
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
        } else if (nbs.obj instanceof PublishFoodLishInfo) {
            if (nbs.isSuccess()) {
                //finish();
            } else {
            }
            ToastUtil.showToast(this,nbs.getMsg(), Toast.LENGTH_SHORT);
        }
    }

    private void getImgsList() {
        foodDesc = "";
        foodImgs = "";
        foodNumStr = "";

        int count = contentSvLl.getChildCount();
        for (int i = 0 ; i < count ; i++) {
            View tempWrapView = contentSvLl.getChildAt(i);
            if (tempWrapView instanceof Button) {
                continue;
            }

            View foodPeriod = tempWrapView.findViewById(R.id.publish_food_list_item_food_period_tv);
            if (foodPeriod != null) {
                String foodPeriodStr = ((TextView)foodPeriod).getText().toString() + "@";
                foodNumStr = foodNumStr + foodPeriodStr;
            }


            EditText tempView = (EditText) tempWrapView.findViewById(R.id.publish_food_list_item_desc_et);
            foodDesc = foodDesc + tempView.getText().toString() + "@";
            LinearLayout addPicLl = (LinearLayout) tempWrapView.findViewById(R.id.publish_food_list_item_add_pic_ll);
            int childCount = addPicLl.getChildCount();
            String tempPics = "";
            for (int j = 0 ; j < childCount; j++) {
                Object obj = addPicLl.getChildAt(j);
                if (obj instanceof ImageView) {
                    ImageView tempIc = (ImageView) obj;
                    String url = "";
                    if (tempIc.getTag() != null) {
                        url = tempIc.getTag().toString() + ",";
                    }
                    tempPics = tempPics + url;
                }
            }

            if (tempPics.contains(",")) {
                tempPics = tempPics.substring(0,tempPics.length()-1);
            }

            foodImgs = foodImgs + tempPics + "@";
        }

        if (foodImgs.contains("@")) {
            foodImgs = foodImgs.substring(0,foodImgs.length()-1);
        }

        if (foodDesc.contains("@")) {
            foodDesc = foodDesc.substring(0,foodDesc.length()-1);
        }

        if (foodNumStr.contains("@")) {
            foodNumStr = foodNumStr.substring(0,foodNumStr.length()-1);
        }
    }



    //按学校ID 查询班级列表 （发布选择班级展示）
    private void requestClassListBySchoolId() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);
        ClassedAndTeachersPaser lp = new ClassedAndTeachersPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.SEARCH_CLASS_LIST_BY_SCHOOL_ID, requestData, lp);
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
        classLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeClassAddPersonInfo = (MakeClassAddPersonInfo)mcda.getItem(position);
                mcda.setCurrentPosition(position);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                titleTv.setText(makeClassAddPersonInfo.getName());
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


    private void showPopupWindow(View view) {
        final TextView foodNumTv = (TextView)view;

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(view.getContext()).inflate(
                R.layout.food_num_select_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 设置按钮的点击事件
        final TextView breakfastTv = (TextView)contentView.findViewById(R.id.food_num_select_pop_window_1);
        final TextView lunchTv = (TextView)contentView.findViewById(R.id.food_num_select_pop_window_2);
        final TextView dinnerTv = (TextView)contentView.findViewById(R.id.food_num_select_pop_window_3);

        breakfastTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = breakfastTv.getText().toString();
                foodNumTv.setText(str);
                popupWindow.dismiss();

            }
        });

        lunchTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String str = lunchTv.getText().toString();
                foodNumTv.setText(str);
                popupWindow.dismiss();

            }
        });

        dinnerTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String str = dinnerTv.getText().toString();
                foodNumTv.setText(str);
                popupWindow.dismiss();

            }
        });

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {



                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }

}
