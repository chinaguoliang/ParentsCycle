package com.jgkj.parentscycle.fragement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.activity.BabyDocumentActivity;
import com.jgkj.parentscycle.activity.BabyShowActivity;
import com.jgkj.parentscycle.activity.CheckAttendanceActivity;
import com.jgkj.parentscycle.activity.PublishCourseActivity;
import com.jgkj.parentscycle.utils.UtilTools;

/**
 * Created by chen on 16/7/9.
 */
public class HallPublishMenuFragment extends Fragment implements View.OnClickListener{
    RelativeLayout mCircleMenu;
    private  final String TAG = "HallPublishMenuFragment";
    private View babyDocument;
    private View babyShowView;
    private View courseView;
    private View questionAnswerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hall_publish_fragment_layout, container,
                false);
        mCircleMenu = (RelativeLayout)view.findViewById(R.id.hall_publish_fragment_circle_bg_rel);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        ViewGroup.LayoutParams params = mCircleMenu.getLayoutParams();
        params.height = width - UtilTools.dip2px(view.getContext(),20);
        params.width = params.height;
        mCircleMenu.setLayoutParams(params);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        babyDocument = view.findViewById(R.id.hall_publish_fragment_baby_document_ll);
        babyShowView = view.findViewById(R.id.hall_publish_fragment_layout_baby_show_rel);
        courseView = view.findViewById(R.id.hall_publish_fragment_course_ll);
        questionAnswerView = view.findViewById(R.id.hall_publish_fragment_layout_question_answer_ll);

        babyDocument.setOnClickListener(this);
        babyShowView.setOnClickListener(this);
        courseView.setOnClickListener(this);
        questionAnswerView.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onClick(View v) {
        if (v == babyDocument) {
            startActivity(new Intent(v.getContext(), BabyDocumentActivity.class));
        } else if (v == babyShowView) {
            startActivity(new Intent(v.getContext(), BabyShowActivity.class));
        } else if (v == courseView) {
            startActivity(new Intent(v.getContext(), PublishCourseActivity.class));
        } else if (v == questionAnswerView) {
            startActivity(new Intent(v.getContext(), CheckAttendanceActivity.class));
        }
    }
}
