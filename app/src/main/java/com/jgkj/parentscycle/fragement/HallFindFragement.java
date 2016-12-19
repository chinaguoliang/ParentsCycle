package com.jgkj.parentscycle.fragement;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.activity.GrowthRecordActivity;
import com.jgkj.parentscycle.activity.MakeClassAddPersonActivity;
import com.jgkj.parentscycle.activity.ParentsCycleActivity;
import com.jgkj.parentscycle.activity.ParentsInfoListActivity;
import com.jgkj.parentscycle.activity.SearchSchoolActivity;
import com.jgkj.parentscycle.activity.SelectClassActivity;
import com.jgkj.parentscycle.activity.VideoTImeControllActivity;
import com.jgkj.parentscycle.adapter.HallFindAdapter;
import com.jgkj.parentscycle.utils.UtilTools;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chen on 16/7/9.
 */
public class HallFindFragement extends Fragment {
    @Bind(R.id.baby_document_activity_back_iv)
    ImageView backIv;

    @Bind(R.id.baby_document_activity_title)
    TextView titleTv;

    @Bind(R.id.baby_document_right_title_tv)
    TextView rightTitleTv;

    @Bind(R.id.hall_find_fragment_layout_lv)
    ListView contentLv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hall_find_fragment_layout, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        backIv.setVisibility(View.GONE);
        titleTv.setText("发现");
        rightTitleTv.setVisibility(View.GONE);
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add("父母圈");
        dataList.add("成长纪录");
        dataList.add("新加入家长");
//        dataList.add("新加入老师");
        dataList.add("加入学校");
        dataList.add("咨询");
        //dataList.add("视频时段控制");

        HallFindAdapter hallFindAdapter = new HallFindAdapter(this.getContext(),dataList);
        contentLv.setAdapter(hallFindAdapter);
        contentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(view.getContext(),ParentsCycleActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(view.getContext(),GrowthRecordActivity.class));
                } else if (position == 2) {
                    startActivity(new Intent(view.getContext(),ParentsInfoListActivity.class));
                }
//                else if (position == 3) {
//                    startActivity(new Intent(view.getContext(),MakeClassAddPersonActivity.class));
//                }
                else if (position == 3) {
                    startActivity(new Intent(view.getContext(),SearchSchoolActivity.class));
                } else if (position == 4) {
                    UtilTools.toChatModule(getContext());
                } else if (position == 5) {
                    startActivity(new Intent(view.getContext(),SelectClassActivity.class));
                }

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
