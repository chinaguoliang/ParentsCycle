package com.jgkj.parentscycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.AnnouncementListItem;
import com.jgkj.parentscycle.bean.HallDynamicInfo;
import com.jgkj.parentscycle.bean.HallMainChannelLvInfo;

import java.util.List;

/**
 * Created by chen on 16/7/23.
 */
public class HallDynamicAdapter extends BaseAdapter{

    public interface SetGoodsListener {
        public void hadSetGoodsListener(AnnouncementListItem hallDynamicInfo);
    }
    private SetGoodsListener mSetGoodsListener;
    public void setGoodListener(SetGoodsListener listener) {
        mSetGoodsListener = listener;
    }

    private List<AnnouncementListItem> contentData;
    private Context mContext;
    public HallDynamicAdapter(Context context, List<AnnouncementListItem> data){
        contentData = data;
        mContext = context;
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
        final HallMainChannelViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new HallMainChannelViewHolder();
            convertView = mInflater.inflate(R.layout.hall_dynamic_fragement_list_item, null);
            convertView.setTag(holder);
            holder.nameTv = (TextView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_user_name_tv);
            holder.timeTv = (TextView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_user_time_tv);
            holder.iconIv = (ImageView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_user_icon_iv);
            holder.bigIconIv = (ImageView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_bg_icon_iv);
            holder.contentTv = (TextView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_content_tv);
            holder.titleTv = (TextView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_title_tv);
            holder.setGoodTv = (TextView)convertView.findViewById(R.id.hall_dynamic_frgement_list_item_set_good_tv);
        } else {
            holder = (HallMainChannelViewHolder) convertView.getTag();
        }


        final AnnouncementListItem hallDynamicInfo = contentData.get(position);
        holder.nameTv.setText(hallDynamicInfo.getOspersion());
        holder.timeTv.setText(hallDynamicInfo.getCreatetime());
        holder.contentTv.setText(hallDynamicInfo.getAnnouncement());
        holder.titleTv.setText(hallDynamicInfo.getTitle());
        holder.setGoodTv.setText(hallDynamicInfo.getDznum());
        holder.setGoodTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetGoodsListener.hadSetGoodsListener(hallDynamicInfo);
            }
        });
        return convertView;
    }

    class HallMainChannelViewHolder {
        TextView nameTv;
        TextView timeTv;
        TextView contentTv;
        ImageView iconIv;
        ImageView bigIconIv;
        TextView titleTv;
        TextView setGoodTv;
    }
}
