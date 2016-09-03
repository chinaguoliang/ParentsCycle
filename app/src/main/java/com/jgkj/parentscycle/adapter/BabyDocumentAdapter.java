package com.jgkj.parentscycle.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.activity.BabyInfoActivity;
import com.jgkj.parentscycle.activity.GrowthRecordActivity;
import com.jgkj.parentscycle.activity.ModifyAttendanceActivity;
import com.jgkj.parentscycle.utils.UtilTools;

import java.util.List;

/**
 * Created by chen on 16/7/18.
 */
public class BabyDocumentAdapter extends BaseAdapter {
    private List<String> contentData;
    private Context mContext;
    private int showSecondPosition = -1;
    public BabyDocumentAdapter(Context context, List<String> data){
        contentData = data;
        mContext = context;
    }

   public void setCurrSelPos(int position) {
       showSecondPosition = position;
   }

    @Override
    public int getCount() {
        return contentData.size();
    }

    @Override
    public Object getItem(int position) {
        return contentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MineViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new MineViewHolder();
            convertView = mInflater.inflate(R.layout.baby_document_lv_item, null);
            convertView.setTag(holder);
            holder.babyNameTv = (TextView)convertView.findViewById(R.id.baby_document_lv_item_baby_name_tv);
            holder.secondMenu = (LinearLayout)convertView.findViewById(R.id.baby_document_lv_item_second_menu_ll);
            holder.askForLeaveTv = (TextView)convertView.findViewById(R.id.baby_document_activity_ask_for_leave_tv);
            holder.chatTv = (TextView)convertView.findViewById(R.id.baby_document_activity_itme_chat_tv);
            holder.growthRecordTv = (TextView)convertView.findViewById(R.id.baby_document_activity_growth_record_tv);
            holder.babyDetailTv = (TextView)convertView.findViewById(R.id.baby_document_activity_baby_detail_tv);
        } else {
            holder = (MineViewHolder) convertView.getTag();
        }

        holder.babyNameTv.setText(contentData.get(position));
        if (showSecondPosition == position) {
            holder.secondMenu.setVisibility(View.VISIBLE);
        } else {
            holder.secondMenu.setVisibility(View.GONE);
        }

        holder.askForLeaveTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                context.startActivity(new Intent(context, ModifyAttendanceActivity.class));
            }
        });

        holder.chatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilTools.toChatModule(v.getContext());
            }
        });

        holder.growthRecordTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                context.startActivity(new Intent(context, GrowthRecordActivity.class));
            }
        });

        holder.babyDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                context.startActivity(new Intent(context,BabyInfoActivity.class));
            }
        });

        return convertView;
    }

    class MineViewHolder {
        TextView babyNameTv;	// 消息未读条数
        LinearLayout secondMenu;
        TextView askForLeaveTv;
        TextView chatTv;
        TextView growthRecordTv;
        TextView babyDetailTv;
    }
}
