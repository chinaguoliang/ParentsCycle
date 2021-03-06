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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 16/8/3.
 */
public class ModifyClassDialogLvAdapter extends BaseAdapter {
    private List<MakeClassAddPersonInfo> contentData;
    private HashMap<Integer,Integer> selectedData = new HashMap<Integer,Integer>();
    private Context mContext;
    public ModifyClassDialogLvAdapter(Context context, List<MakeClassAddPersonInfo> data){
        contentData = data;
        mContext = context;
    }

    public void setClassidShowSelected(String classId) {
        int count = contentData.size();
        int tempPosition = 0;
        for (int i = 0 ; i < count ; i++) {
            MakeClassAddPersonInfo mcpi = contentData.get(i);
            if (TextUtils.equals(mcpi.getId(),classId)) {
                tempPosition = i;
                break;
            }
        }
        setSelectPosition(tempPosition);
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
        MakeClassAddPersonInfo makeClassAddPersonInfo= contentData.get(position);

        if (selectedData.get(position) == null || selectedData.get(position) == 0) {
            selectedData.put(position,Integer.parseInt(makeClassAddPersonInfo.getId()));
        } else {
            selectedData.put(position,0);
        }
      this.notifyDataSetChanged();
    }

    public void setCurrentPosition(int position) {
        selectedData.clear();
        MakeClassAddPersonInfo makeClassAddPersonInfo= contentData.get(position);
        if (selectedData.get(position) == null || selectedData.get(position) == 0) {
            selectedData.put(position,Integer.parseInt(makeClassAddPersonInfo.getId()));
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
            convertView = mInflater.inflate(R.layout.make_class_add_person_avtivity_lv_item, null);
            convertView.setTag(holder);
            holder.nameTv = (TextView)convertView.findViewById(R.id.make_class_add_person_activity_content_lv_item_user_name_tv);
            holder.rightSymbleIv = (ImageView)convertView.findViewById(R.id.make_class_add_person_activity_content_lv_item_right_symble_iv);
            holder.userIconIv = (ImageView)convertView.findViewById(R.id.make_class_add_person_activity_content_lv_item_user_icon_iv);
        } else {
            holder = (MineViewHolder) convertView.getTag();
        }

        MakeClassAddPersonInfo makeClassAddPersonInfo = contentData.get(position);
        holder.nameTv.setText(makeClassAddPersonInfo.getName());
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
