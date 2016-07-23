package com.jgkj.parentscycle.fragement;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.activity.DynamicContentActivity;
import com.jgkj.parentscycle.adapter.HallDynamicAdapter;
import com.jgkj.parentscycle.bean.HallDynamicInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 16/7/9.
 */
public class HallDynamicFragement extends Fragment implements View.OnClickListener{
    @Bind(R.id.hall_dynamic_frgement_listview)
    ListView mListView;

    @Bind(R.id.hall_dynamic_frgement_list_item_announcement_tv)
    TextView announcementTv;


    @Bind(R.id.hall_dynamic_frgement_list_item_baby_show_tv)
    TextView babyShowTv;

    @Bind(R.id.hall_dynamic_frgement_list_item_school_tv)
    TextView schoolTv;

    @Bind(R.id.hall_dynamic_frgement_list_item_class_tv)
    TextView classTv;

    @Bind(R.id.hall_dynamic_frgement_list_item_tip_tv)
    TextView tipTv;

    @Bind(R.id.hall_dynamic_frgement_list_item_announcement_tv_divider)
    View announcementDivider;

    @Bind(R.id.hall_dynamic_frgement_list_item_baby_show_divider)
    View babyShowDivider;

    @Bind(R.id.hall_main_channel_fragment_layout_menu_2_rel)
    View menuView2;

    private List<HallDynamicInfo> dataList;

    private int blueColor;
    private int gryaColor;
    private int whiteColor;
    private int titleGrayColor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hall_dynamic_fragement_layout, container,
                false);
        ButterKnife.bind(this,view);
        dataList = new ArrayList<HallDynamicInfo>();
        initTestData();
        HallDynamicAdapter hallDynamicAdapter = new HallDynamicAdapter(view.getContext(),dataList);
        mListView.setAdapter(hallDynamicAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(view.getContext(),DynamicContentActivity.class));
            }
        });
        blueColor = this.getResources().getColor(R.color.main_blue_color);
        gryaColor = this.getResources().getColor(R.color.text_gray);
        whiteColor = this.getResources().getColor(R.color.white);
        titleGrayColor = this.getResources().getColor(R.color.title_gray);
        return view;
    }

    private void initTestData() {
        for (int i = 0 ; i < 20 ; i++) {
            if (i % 2 == 0) {
                HallDynamicInfo hd = new HallDynamicInfo();
                hd.setContent("幼儿园不错");
                hd.setName("李方");
                hd.setTime("10分钟前");
                dataList.add(hd);
            } else {
                HallDynamicInfo hd = new HallDynamicInfo();
                hd.setContent("市内的幼儿园");
                hd.setName("赵段");
                hd.setTime("60分钟前");
                dataList.add(hd);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick({R.id.hall_dynamic_frgement_list_item_announcement_tv,R.id.hall_dynamic_frgement_list_item_baby_show_tv,R.id.hall_dynamic_frgement_list_item_school_tv,
            R.id.hall_dynamic_frgement_list_item_class_tv,R.id.hall_dynamic_frgement_list_item_tip_tv})

    @Override
    public void onClick(View v) {
        if (v == schoolTv) {
            resetColor();
            schoolTv.setTextColor(blueColor);
        } else if (v == classTv) {
            resetColor();
            classTv.setTextColor(blueColor);
        } else if (v == tipTv) {
            resetColor();
            tipTv.setTextColor(blueColor);
        } else if (v == announcementTv) {
            announcementDivider.setVisibility(View.VISIBLE);
            babyShowDivider.setVisibility(View.GONE);
            announcementTv.setTextColor(whiteColor);
            babyShowTv.setTextColor(titleGrayColor);
            menuView2.setVisibility(View.VISIBLE);
        } else if (v == babyShowTv) {
            announcementDivider.setVisibility(View.GONE);
            babyShowDivider.setVisibility(View.VISIBLE);
            babyShowTv.setTextColor(whiteColor);
            announcementTv.setTextColor(titleGrayColor);
            menuView2.setVisibility(View.GONE);
        }
    }

    private void resetColor() {
        schoolTv.setTextColor(gryaColor);
        classTv.setTextColor(gryaColor);
        tipTv.setTextColor(gryaColor);
    }
}
