package com.videogo.devicemgt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.videogo.RootActivity;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.CameraManager;
import com.videogo.constant.IntentConsts;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.PeripheralInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.ui.util.ActivityUtils;
import com.videogo.util.ConnectionDetector;
import com.videogo.widget.TitleBar;
import com.videogo.widget.WaitDialog;

//import com.videogo.restful.exception.VideoGoNetSDKException;
//import com.videogo.util.ActivityUtils;
//import com.videogo.widget.inputfilter.BytesLengthFilter;
//import com.videogo.widget.inputfilter.IllegalWordFilter;

public class ModifyDeviceNameActivity extends RootActivity implements OnClickListener {

    protected static final int MSG_UPDATA_DEVICE_NAME_FAIL = 1001;

    protected static final int MSG_UPDATA_DEVICE_NAME_SUCCESS = 1002;

    private final static int TYPE_DEVICE = 0x01;
    private final static int TYPE_CAMERA = 0x02;
    private final static int TYPE_DETECTOR = 0x04;

    /** 标题栏 */
    private TitleBar mTitleBar;
    /** 输入框 */
    private EditText mNameText;
    /** 类型 */
    private TextView mDetectorTypeView;
    /** 清除按钮 */
    private ImageButton mNameDelButton;
    /** 输入提示 */
    private TextView mInputHintView;

    /** 常用名称Layout */
    private ViewGroup mCommonNameLayout;
    /** 常用名称GridView */
    private GridView mCommonNameGridView;

    private DeviceInfoEx mDevice;
    private CameraInfoEx mCamera;
    private PeripheralInfo mDetector;

    private WaitDialog mWaitDialog;
    
    private String mDeviceNameString;

    /** 消息对象 */
    private Handler mHandler;
    private int mType;
    private DeviceInfoCtrl mDeviceInfoCtrl;
	private Button mSaveButton = null;
    private EZOpenSDK mEZOpenSDK = null;
    private String mDeviceSerial = null;
    private EZCameraInfo mCameraInfo = null;
//    private CommonNameGridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.videogo.open.R.layout.modify_device_name_page);

        findViews();
        initData();
        initTitleBar();
        initViews();
    }

    /**
     * 控件关联
     */
    private void findViews() {
        mTitleBar = (TitleBar) findViewById(com.videogo.open.R.id.title_bar);
        mTitleBar.setBackButton(com.videogo.open.R.drawable.common_title_cancel_selector);
        mNameText = (EditText) findViewById(com.videogo.open.R.id.name_text);
        mNameDelButton = (ImageButton) findViewById(com.videogo.open.R.id.name_del);
        mDetectorTypeView = (TextView) findViewById(com.videogo.open.R.id.detector_type);
        mInputHintView = (TextView) findViewById(com.videogo.open.R.id.input_hint);
        mCommonNameLayout = (ViewGroup) findViewById(com.videogo.open.R.id.common_name_layout);
        mSaveButton = (Button) findViewById(com.videogo.open.R.id.btn_id_save_name);
    }

    private void initData() {
        mHandler = new MyHandler();
        mDeviceInfoCtrl = DeviceInfoCtrl.getInstance();
        mEZOpenSDK = EZOpenSDK.getInstance();

        if (getIntent().hasExtra(IntentConsts.EXTRA_CAMERA_ID)) {
            String cameraId = getIntent().getStringExtra(IntentConsts.EXTRA_CAMERA_ID);
            mCamera = CameraManager.getInstance().getAddedCameraById(cameraId);

            if (mCamera == null) {
                finish();
                return;
            }

            mType = TYPE_CAMERA;

            mInputHintView.setText(getString(com.videogo.open.R.string.detail_modify_device_name_limit_tip, 50));
            mNameText.setFilters(new InputFilter[] {
                new LengthFilter(50)/*,
                new IllegalWordFilter(this, "[\\\\/:\\*\\?\"<>\\|'% ]",
                        getText(R.string.camera_name_contain_illegel_word))*/});

        } else {
        	mDeviceSerial = getIntent().getStringExtra("DEVICE_SERIAL");
        	mCameraInfo = (EZCameraInfo) getIntent().getParcelableExtra("EZCAMERA_INFO");
            mDevice = null;// DeviceManager.getInstance().getDeviceInfoExById(deviceId);
//            if (mDevice == null) {
//                finish();
//                return;
//            }

            mType = TYPE_DEVICE;

            mInputHintView.setText(getString(com.videogo.open.R.string.detail_modify_device_name_limit_tip, 50));
            mNameText.setFilters(new InputFilter[] {
                new LengthFilter(50)/*,
                new IllegalWordFilter(this, "[\\\\/:\\*\\?\"<>\\|'% ]",
                        getText(R.string.camera_name_contain_illegel_word))*/});
        }

        mWaitDialog = new WaitDialog(ModifyDeviceNameActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        mWaitDialog.setCancelable(false);
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitleBar.setTitle(com.videogo.open.R.string.ez_modify_name);
        mTitleBar.addBackButton(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        mTitleBar.addRightButton(R.drawable.common_title_confirm_selector, new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                modifyDeviceName();
//            }
//        });
    }

    private void initViews() {
//        String deviceName = getOriginalName();
    	String deviceName = getOriginalName(); //mj, get device name, not get camera name

        mNameText.setText(deviceName);
        mNameText.setSelection(mNameText.getText().length());
        mNameText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                setSelectLabel(str);
            }
        });
        mNameText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    modifyDeviceName();
                    return true;
                }
                return false;
            }
        });

        mNameDelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNameText.setText(null);
            }
        });

        if (mType == TYPE_DETECTOR) {} else {
            mCommonNameLayout.setVisibility(View.GONE);
        }
        mSaveButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
        int i = view.getId();
        if (i == com.videogo.open.R.id.btn_id_save_name) {
            modifyDeviceName();

        }
	}

    private String getOriginalName() {
    	if(mCameraInfo != null) {
    		return mCameraInfo.getCameraName();
    	}
    	return null;
//        switch (mType) {
//            case TYPE_CAMERA:
//                return mCamera.getCameraName();
//            case TYPE_DETECTOR:
//                // if (mDetector.getLocation().endsWith(mDetector.getChannelTypeStr())) {
//                // int index = mDetector.getLocation().lastIndexOf(mDetector.getChannelTypeStr());
//                // return mDetector.getLocation().substring(0, index);
//                // }
//                return mDetector.getLocation();
//            case TYPE_DEVICE:
//                return mDevice.getDeviceName();
//            default:
//                return "";
//        }
    }

    private void setSelectLabel(String input) {}

    /**
     * 修改设备名称
     * 
     * @throws
     */
    private void modifyDeviceName() {
    	if(TextUtils.isEmpty(mDeviceSerial)) {
    		return;
    	}
        mDeviceNameString = mNameText.getText().toString().trim();

        if (TextUtils.isEmpty(mDeviceNameString)) {
            showToast(com.videogo.open.R.string.company_addr_is_empty);
            return;
        }

        // 本地网络检测
        if (!ConnectionDetector.isNetworkAvailable(ModifyDeviceNameActivity.this)) {
            showToast(com.videogo.open.R.string.offline_warn_text);
            return;
        }

        mWaitDialog.show();

        new Thread() {
            @Override
            public void run() {
                int errorCode = 0;

                try {
                	mEZOpenSDK.setDeviceName(mDeviceSerial, mDeviceNameString);
                } catch (BaseException e) {
                	e.printStackTrace();
                    errorCode = e.getErrorCode();
                }

                if (errorCode != 0) {
                    mHandler.obtainMessage(MSG_UPDATA_DEVICE_NAME_FAIL, errorCode, 0).sendToTarget();
                } else {
                    mHandler.obtainMessage(MSG_UPDATA_DEVICE_NAME_SUCCESS).sendToTarget();
                }
            }
        }.start();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATA_DEVICE_NAME_FAIL:
                    handleUpdateFail(msg.arg1);
                    break;
                case MSG_UPDATA_DEVICE_NAME_SUCCESS:
                    handleUpdateSuccess();
                    break;
                default:
                    break;
            }
        }
    }

    private void handleUpdateFail(int errorCode) {
        mWaitDialog.dismiss();
        switch (errorCode) {
            case ErrorCode.ERROR_WEB_DIVICE_NOT_ONLINE:
                showToast(com.videogo.open.R.string.camera_not_online);
                break;
            case ErrorCode.ERROR_WEB_SESSION_ERROR:
//                ActivityUtils.handleSessionException(ModifyDeviceNameActivity.this);
                break;
            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
                ActivityUtils.handleHardwareError(ModifyDeviceNameActivity.this, null);
                break;
            default:
                // 修改失败，提示失败的消息
                showToast(com.videogo.open.R.string.detail_modify_fail, errorCode);
                break;
        }
    }

    private void handleUpdateSuccess() {
        mWaitDialog.dismiss();
        showToast(com.videogo.open.R.string.detail_modify_success);
        Intent intent = new Intent();
        intent.putExtra(IntentConsts.EXTRA_NAME, mDeviceNameString);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void hideSoftInput() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, com.videogo.open.R.anim.fade_down);
    }
}