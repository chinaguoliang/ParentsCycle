package com.jgkj.parentscycle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.MakeClassAddPersonInfo;
import com.jgkj.parentscycle.bean.ParentsListItemInfo;
import com.jgkj.parentscycle.bean.TeachersListItemInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 16/8/3.
 */
public class NewJoinTeacherListAdapter extends BaseAdapter {
    private  List<TeachersListItemInfo>  contentData;
    private HashMap<Integer,Integer> selectedData = new HashMap<Integer,Integer>();
    private Context mContext;
    public NewJoinTeacherListAdapter(Context context,  List<TeachersListItemInfo>  data){
        contentData = data;
        mContext = context;
    }

    public String getIdsData() {
        String result = "";
        Iterator iter = selectedData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (!TextUtils.isEmpty(val.toString())) {
                if (Integer.parseInt(val.toString()) > 0) {
                    result = result + val.toString() + ",";
                }
            }
        }

        if (result.length() > 1) {
            return result.substring(0, result.length()-1);
        } else {
            return "";
        }
    }

    public void setSelectPosition(int position) {
        TeachersListItemInfo makeClassAddPersonInfo= contentData.get(position);
        selectedData.clear();
        if (selectedData.get(position) == null || selectedData.get(position) == 0) {
            selectedData.put(position,Integer.parseInt(makeClassAddPersonInfo.getTmpinfoid()));
        } else {
            selectedData.put(position,0);
        }
        this.notifyDataSetChanged();
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
            convertView = mInflater.inflate(R.layout.new_join_school_list_item, null);
            convertView.setTag(holder);
            holder.nameTv = (TextView)convertView.findViewById(R.id.new_join_school_activity_content_lv_item_user_name_tv);
            holder.rightSymbleIv = (ImageView)convertView.findViewById(R.id.new_join_school_activity_content_lv_item_right_symble_iv);
            holder.userIconIv = (ImageView)convertView.findViewById(R.id.new_join_school_activity_content_lv_item_user_icon_iv);
        } else {
            holder = (MineViewHolder) convertView.getTag();
        }

        TeachersListItemInfo makeClassAddPersonInfo = contentData.get(position);
        holder.nameTv.setText(makeClassAddPersonInfo.getTeachername());
        if (selectedData.get(position) != null) {
            if (selectedData.get(position) > 0) {
                holder.rightSymbleIv.setVisibility(View.VISIBLE);
            } else {
                holder.rightSymbleIv.setVisibility(View.GONE);
            }
        } else {
            holder.rightSymbleIv.setVisibility(View.GONE);
        }


        return convertView;
    }

    class MineViewHolder {
        TextView nameTv;
        ImageView userIconIv;
        ImageView rightSymbleIv;
    }
}
