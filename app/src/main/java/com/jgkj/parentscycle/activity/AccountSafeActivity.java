package com.jgkj.parentscycle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.parentscycle.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/8/2.
 */
public class AccountSafeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "AccountSafeActivity";
    @Bind(R.id.title_bar_layout_rel)
    View titleBg;

    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTv;

    @Bind(R.id.account_safe_activity_modify_pass_rel)
    View modifyPass;

    @Bind(R.id.account_safe_activity_phone_rel)
    View modifyPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_safe_activity);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        rightTv.setVisibility(View.GONE);
        titleTv.setText("帐号安全");
        //titleTv.setTextColor(Color.BLACK);
        //titleBg.setBackgroundColor(Color.WHITE);
    }

    @OnClick({R.id.baby_document_activity_back_iv,R.id.account_safe_activity_modify_pass_rel,R.id.account_safe_activity_phone_rel})
    @Override
    public void onClick(View v) {
        if (v == modifyPass) {
            startActivity(new Intent(AccountSafeActivity.this,AccountSafeResetPassActivity.class));
        } else if (v == backIv) {
            finish();
        } else if (v == modifyPhone) {
            startActivity(new Intent(AccountSafeActivity.this,AccountSafeResetPhoneActivity.class));
        }
    }

    @Override
    public void uploadImgFinished(Bitmap bitmap,String uploadedKey) {

    }
}
