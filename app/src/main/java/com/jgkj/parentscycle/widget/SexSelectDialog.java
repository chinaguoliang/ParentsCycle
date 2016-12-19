package com.jgkj.parentscycle.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.utils.UtilTools;

/**
 * Created by chen on 16/8/22.
 */
public class SexSelectDialog {
    public interface SexSlectDialogFinish {
        public void finishSlecct(int index);
    }

    public static  void showSexSelectDialog(final Context context,final  SexSlectDialogFinish ssdf) {
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.sex_select_dialog,null);
        dialog.setContentView(contentView);
        TextView manTv = (TextView) contentView.findViewById(R.id.sex_select_dialog_man_tv);
        TextView womanTv = (TextView) contentView.findViewById(R.id.sex_select_dialog_woman_tv);
        manTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssdf.finishSlecct(1);
                dialog.dismiss();
            }
        });

        womanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssdf.finishSlecct(0);
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = UtilTools.SCREEN_WIDTH - 60;
        dialog.getWindow().setAttributes(params);
        dialog.show();

    }
}
