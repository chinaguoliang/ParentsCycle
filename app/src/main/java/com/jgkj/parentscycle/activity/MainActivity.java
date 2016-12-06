package com.jgkj.parentscycle.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.AnnouncementListInfo;
import com.jgkj.parentscycle.bean.AnnouncementListItem;
import com.jgkj.parentscycle.bean.ArticleTransferInfo;
import com.jgkj.parentscycle.bean.BabyDocumentListInfo;
import com.jgkj.parentscycle.bean.SetLikeInfo;
import com.jgkj.parentscycle.bean.VideoControlTImeItem;
import com.jgkj.parentscycle.bean.VideoControlTimeInfo;
import com.jgkj.parentscycle.fragement.HallPublishMenuFragment;
import com.jgkj.parentscycle.fragement.HallDynamicFragement;
import com.jgkj.parentscycle.fragement.HallFindFragement;
import com.jgkj.parentscycle.fragement.HallMainChannelFragement;
import com.jgkj.parentscycle.fragement.HallMeFragement;
import com.jgkj.parentscycle.global.ActivityResultCode;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.global.ConfigPara;
import com.jgkj.parentscycle.json.AnnouncementListPaser;
import com.jgkj.parentscycle.json.ArticleTransferInfoPaser;
import com.jgkj.parentscycle.json.GetVideoControlTimeBySchoolIdPaser;
import com.jgkj.parentscycle.json.ModifyPassByOldPassPaser;
import com.jgkj.parentscycle.json.ResetPasswordPaser;
import com.jgkj.parentscycle.json.SetLikeInfoPaser;
import com.jgkj.parentscycle.json.TeacherInfoLIstPaser;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.NetRequest;
import com.jgkj.parentscycle.user.UserInfo;
import com.jgkj.parentscycle.utils.ImageHandler;
import com.jgkj.parentscycle.utils.LogUtil;
import com.jgkj.parentscycle.utils.PreferenceUtil;
import com.jgkj.parentscycle.utils.ToastUtil;
import com.jgkj.parentscycle.utils.UtilTools;
import com.videogo.CustomVideoData;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener,NetListener,LocationListener{
    public static final String TAG = "MainActivity";

    @Bind(R.id.main_activity_bottom_bar_main_channel_tv)
    TextView mBtmMainChannelTv;

    @Bind(R.id.main_activity_bottom_bar_main_channel_iv)
    ImageView mBtmMainChannelIv;

    @Bind(R.id.main_activity_bottom_bar_main_channel_ll)
    LinearLayout mBtmMainChannelLl;

    @Bind(R.id.main_activity_bottom_bar_dynamic_tv)
    TextView mBtmDynamicTv;

    @Bind(R.id.main_activity_bottom_bar_dynamic_iv)
    ImageView mBtmDynamicIv;

    @Bind(R.id.main_activity_bottom_bar_dynamic_ll)
    LinearLayout mBtmDynamicLl;

    @Bind(R.id.main_activity_bottom_bar_find_tv)
    TextView mBtmFindTv;

    @Bind(R.id.main_activity_bottom_bar_find_iv)
    ImageView mBtmFindIv;

    @Bind(R.id.main_activity_bottom_bar_find_ll)
    LinearLayout mBtmFindLl;

    @Bind(R.id.main_activity_bottom_bar_mine_tv)
    TextView mBtmMineTv;

    @Bind(R.id.main_activity_bottom_bar_mine_iv)
    ImageView mBtmMineIv;

    @Bind(R.id.main_activity_bottom_bar_mine_ll)
    LinearLayout mBtmMineLl;


    @Bind(R.id.main_activity_bottom_bar_publish_iv)
    ImageView mBtmPublishIv;


    public ImageHandler handler = new ImageHandler(new WeakReference<MainActivity>(this));
    private  HallDynamicFragement mHallDynamicFragement;
    private  HallFindFragement mHallFindFragement;
    private  HallMainChannelFragement mHallMainChannelFragement;
    private  HallMeFragement mHallMeFragement;
    private  FragmentManager mFragmentManager;

    private static final int REQUEST_PERMISSION_LOCATION = 255; // int should be between 0 and 255
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        ButterKnife.bind(this);
        initFragment();
        requestLocation();
    }

    private void requestLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        }
    }

    public void setViewPagerCurrentItem(int position) {
        if (mHallMainChannelFragement != null) {
            mHallMainChannelFragement.setViewPagerCurrentItem(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHallMeFragement != null) {
            mHallMeFragement.refreshUI();
        }
    }

    private void initFragment() {
        mHallDynamicFragement = new HallDynamicFragement();
        mHallFindFragement = new HallFindFragement();
        mHallMainChannelFragement = new HallMainChannelFragement();
        mHallMeFragement = new HallMeFragement();

        mFragmentManager = this.getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_activity_content_fl,mHallMainChannelFragement).commit();
    }


    @OnClick({R.id.main_activity_bottom_bar_main_channel_ll,R.id.main_activity_bottom_bar_dynamic_ll,R.id.main_activity_bottom_bar_find_ll
            ,R.id.main_activity_bottom_bar_mine_ll,R.id.main_activity_bottom_bar_publish_iv})

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = mFragmentManager
                .beginTransaction();

        if (v == mBtmMainChannelLl) {
            resetViews(transaction);
            mBtmMainChannelIv.setImageResource(R.mipmap.main_activity_home_channel_no_selected);
            transaction.show(mHallMainChannelFragement).commitAllowingStateLoss();
            mBtmMainChannelTv.setTextColor(this.getResources().getColor(R.color.main_blue_color));
        } else if (v == mBtmDynamicLl) {
            resetViews(transaction);
            mBtmDynamicIv.setImageResource(R.mipmap.main_activity_btm_dynamic_selected);
            if (mHallDynamicFragement.isAdded()) {
                transaction.show(mHallDynamicFragement).commitAllowingStateLoss();
            } else {
                transaction.add(R.id.main_activity_content_fl, mHallDynamicFragement);
                transaction.show(mHallDynamicFragement).commitAllowingStateLoss();
            }
            mBtmDynamicTv.setTextColor(this.getResources().getColor(R.color.main_blue_color));
            requestAnnouncementList();
        }  else if (v == mBtmFindLl) {
            resetViews(transaction);
            mBtmFindIv.setImageResource(R.mipmap.main_activity_find_selected);
            if (mHallFindFragement.isAdded()) {
                transaction.show(mHallFindFragement).commitAllowingStateLoss();
            } else {
                transaction.add(R.id.main_activity_content_fl,mHallFindFragement);
                transaction.show(mHallFindFragement).commitAllowingStateLoss();
            }
            mBtmFindTv.setTextColor(this.getResources().getColor(R.color.main_blue_color));
        }  else if (v == mBtmMineLl) {
            resetViews(transaction);

            mBtmMineIv.setImageResource(R.mipmap.main_activity_mine_selected);
            if (mHallMeFragement.isAdded()) {
                transaction.show(mHallMeFragement).commitAllowingStateLoss();
            } else {
                transaction.add(R.id.main_activity_content_fl,mHallMeFragement);
                transaction.show(mHallMeFragement).commitAllowingStateLoss();
            }
            mBtmMineTv.setTextColor(this.getResources().getColor(R.color.main_blue_color));
        }  else if (v == mBtmPublishIv) {
           startActivityForResult(new Intent(this,CircleMenuActivity.class),10);
        }
    }

    private void resetViews(FragmentTransaction transaction) {


        if (mHallDynamicFragement.isAdded()) {
            transaction.hide(mHallDynamicFragement);
        }

        if (mHallFindFragement.isAdded()) {
            transaction.hide(mHallFindFragement);
        }

        if (mHallMainChannelFragement.isAdded()) {
            transaction.hide(mHallMainChannelFragement);
        }

        if (mHallMeFragement.isAdded()) {
            transaction.hide(mHallMeFragement);
        }

        mBtmDynamicIv.setImageResource(R.mipmap.main_activity_btm_dynamic_no_selected);
        mBtmMainChannelIv.setImageResource(R.mipmap.main_activity_home_channel_gray_selected);
        mBtmPublishIv.setImageResource(R.mipmap.main_activity_publish_no_selected);
        mBtmFindIv.setImageResource(R.mipmap.main_activity_find_no_selected);
        mBtmMineIv.setImageResource(R.mipmap.main_activity_mine_no_selected);

        mBtmMainChannelTv.setTextColor(this.getResources().getColor(R.color.text_gray_2));
        mBtmDynamicTv.setTextColor(this.getResources().getColor(R.color.text_gray_2));
        mBtmFindTv.setTextColor(this.getResources().getColor(R.color.text_gray_2));
        mBtmMineTv.setTextColor(this.getResources().getColor(R.color.text_gray_2));
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap,String uploadedKey) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ActivityResultCode.MY_PUBLISH_CODE) {
            mBtmFindLl.performClick();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void requestResponse(Object obj) {
        hideProgressDialog();
        NetBeanSuper nbs = (NetBeanSuper)obj;
        if (nbs.obj instanceof AnnouncementListInfo) {
            if (nbs.isSuccess()) {
                AnnouncementListInfo bdlii = (AnnouncementListInfo)nbs.obj;
                mHallDynamicFragement.setDataList(bdlii.getDataList());
            } else {

            }

            ToastUtil.showToast(this, nbs.getMsg(), Toast.LENGTH_SHORT);
        } else if (nbs.obj instanceof VideoControlTimeInfo) {
            if (nbs.isSuccess()) {
                VideoControlTimeInfo bdlii = (VideoControlTimeInfo)nbs.obj;
                List<VideoControlTImeItem> listData =  bdlii.getObj();
                int count = listData.size();
                HashMap<String,String> dataMap = new HashMap<String,String>();
                for (int i = 0 ; i < count ; i++) {
                    VideoControlTImeItem vcti = listData.get(i);
                    if (TextUtils.equals(vcti.getSerial_number(),"0")) {

                    } else {
                        dataMap.put(vcti.getSerial_number(),vcti.getStart_time() + "_" + vcti.getEnd_time() + "_" + vcti.getIs_allow_play());
                    }

                }
                CustomVideoData.videoData = dataMap;
                UtilTools.toVideoModule(this, Volley.newRequestQueue(this));
            } else {
                UtilTools.toVideoModule(this, Volley.newRequestQueue(this));
                ToastUtil.showToast(this, nbs.getMsg(), Toast.LENGTH_SHORT);
            }
        } else if (nbs.obj instanceof SetLikeInfo) {
            if (nbs.isSuccess()) {

            } else {

            }

            ToastUtil.showToast(this, nbs.getMsg(), Toast.LENGTH_SHORT);
        } else if (nbs.obj instanceof ArticleTransferInfo) {
            if (nbs.isSuccess()) {
            } else {
            }
            ToastUtil.showToast(this, nbs.getMsg(), Toast.LENGTH_SHORT);
        }

    }

    //公告列表
    private void requestAnnouncementList() {
        if (!UserInfo.isLogined) {
            ToastUtil.showToast(this,"未登录",Toast.LENGTH_SHORT);
            return;
        }

        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("id", UserInfo.loginInfo.getRole().getId());
        requestData.put("page", "1");
        requestData.put("rows", "30");
        AnnouncementListPaser lp = new AnnouncementListPaser();
        NetRequest.getInstance().request(mQueue, this, BgGlobal.ANNOUNCEMENT_LIST, requestData, lp);
    }

    @Override
    public void onLocationChanged(Location location) {

        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
        String time = System.currentTimeMillis() + "";

        try {
            JSONObject jobj = new JSONObject();
            jobj.put("latitude",latitude);
            jobj.put("longitude",longitude);
            jobj.put("time",time);
            PreferenceUtil.setStringKey(this,PreferenceUtil.LOCATION_INFO,jobj.toString());
            //LogUtil.d(TAG,"locationinfo:" + jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void requestVideoData() {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("schoolid", ConfigPara.SCHOOL_ID);  //登录时ID
        GetVideoControlTimeBySchoolIdPaser lp = new GetVideoControlTimeBySchoolIdPaser();
        NetRequest.getInstance().requestTest(mQueue, this, BgGlobal.GET_VIDEO_CONTROL_DATA_BY_SCHOOL_ID, requestData, lp);
    }

    public void requestSetGood(String id) {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("wbid", id);
        requestData.put("osid",UserInfo.loginInfo.getInfo().getTmpinfoid());
        requestData.put("role","1");
        requestData.put("attype","1"); //1：公告 2、宝宝秀 3：父母圈 4: 某个评论
        SetLikeInfoPaser lp = new SetLikeInfoPaser();
        NetRequest.getInstance().request(mQueue, this,
                BgGlobal.SET_GOOD, requestData, lp);
    }


    public void requestTransferArticle(String id) {
        showProgressDialog();
        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("wbid", id);
        requestData.put("osid",UserInfo.loginInfo.getInfo().getTmpinfoid());
        requestData.put("role",UserInfo.loginInfo.getRole().getRole());
        requestData.put("attype","1"); // 1：公告 2、宝宝秀 3：父母圈
        requestData.put("chennl","1"); // 所属类型 1：qq 2：微信 3：陌陌
        ArticleTransferInfoPaser lp = new ArticleTransferInfoPaser();
        NetRequest.getInstance().request(mQueue, this,
                BgGlobal.PUBLIC_ARTICLE_DISTRIBUCTION, requestData, lp);
    }
}
