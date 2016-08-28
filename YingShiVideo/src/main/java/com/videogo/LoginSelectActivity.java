/* 
 * @ProjectName ezviz-openapi-android-demo
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName LoginSelectActivity.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-12-6
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.videogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.videogo.androidpn.AndroidpnUtils;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.ui.cameralist.EZCameraListActivity;
import com.videogo.ui.util.OpenYSService;


/**
 * 登录选择演示
 * @author xiaxingsuo
 * @data 2015-11-6
 */
public class LoginSelectActivity extends Activity implements OnClickListener,
    OpenYSService.OpenYSServiceListener {
    private EZOpenSDK mEZOpenSDK = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.videogo.open.R.layout.login_page);
        
        initData();
        initView();      
    }
    
    private void initData() {
    	mEZOpenSDK = EZOpenSDK.getInstance();
    }
    
    private void initView() {
        
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        int i = v.getId();
        if (i == com.videogo.open.R.id.interface_call_btn) {
            intent = new Intent(LoginSelectActivity.this, InterfaceDemoActivity.class);
            startActivity(intent);

        } else if (i == com.videogo.open.R.id.web_login_btn) {
            mEZOpenSDK.openLoginPage();

        } else if (i == com.videogo.open.R.id.goto_cameralist_btn) {
            openPlatformLoginDialog();

        } else if (i == com.videogo.open.R.id.id_ll_join_qq_group) {
            String key = "p57CNgQ_uf2gZMY0eYTvgQ_S_ZDzZz44";
            joinQQGroup(key);

        } else {
        }
    }
    
    private void openPlatformLoginDialog() {
        final EditText editText = new EditText(this);
        new  AlertDialog.Builder(this)  
        .setTitle(com.videogo.open.R.string.please_input_platform_accesstoken_txt)
        .setIcon(android.R.drawable.ic_dialog_info)   
        .setView(editText)  
        .setPositiveButton(com.videogo.open.R.string.certain, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String getAccessTokenSign = SignUtil.getGetAccessTokenSign();
                
            	mEZOpenSDK.setAccessToken(editText.getText().toString());
                Intent toIntent = new Intent(LoginSelectActivity.this, EZCameraListActivity.class);
                toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginSelectActivity.this.startActivity(toIntent);
                AndroidpnUtils.startPushServer(LoginSelectActivity.this);
            }
            
        })   
        .setNegativeButton(com.videogo.open.R.string.cancel, null)
        .show();  
    }

    /* (non-Javadoc)
     * @see com.videogo.ui.util.OpenYSService.OpenYSServiceListener#onResult(int)
     */
    @Override
    public void onOpenYSService(int result) {
        // TODO Auto-generated method stub
        
    }

    private boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
