package com.jgkj.parentscycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.parentscycle.R;
import com.jgkj.parentscycle.bean.ClassesAndTeachersListItemInfo;
import com.jgkj.parentscycle.bean.SearchSchoolItemInfo;
import com.jgkj.parentscycle.utils.UtilTools;

import java.util.List;
import java.util.Map;

/**
 * Created by chen on 16/8/4.
 */
public class AddSchoolExpanLvAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<SearchSchoolItemInfo> groupTitle;
    //子项是一个map，key是group的id，每一个group对应一个ChildItem的list
    private Map<Integer, List<ClassesAndTeachersListItemInfo>> childMap;
    private Button groupButton;//group上的按钮

    public AddSchoolExpanLvAdapter(Context context, List<SearchSchoolItemInfo> groupTitle, Map<Integer, List<ClassesAndTeachersListItemInfo>> childMap) {
        this.mContext = context;
        this.groupTitle = groupTitle;
        this.childMap = childMap;
    }
    /*
     *  Gets the data associated with the given child within the given group
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //我们这里返回一下每个item的名称，以便单击item时显示
        return childMap.get(groupPosition).get(childPosition).getClassadviser();
    }
    /*
     * 取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    /*
     *  Gets a View that displays the data for the given child within the given group
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_school_activity_elv_child_item, null);
            childHolder = new ChildHolder();
            childHolder.childImg = (ImageView) convertView.findViewById(R.id.mangement_class_activity_elv_child_item_chat_iv);
            childHolder.childText = (TextView) convertView.findViewById(R.id.mangement_class_activity_elv_child_item_baby_name_tv);
            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        childHolder.childText.setText(childMap.get(groupPosition).get(childPosition).getClassname());
        return convertView;
    }

    /*
     * 取得指定分组的子元素数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        if (childMap.get(groupPosition) == null) {
            return 0;
        }

        return childMap.get(groupPosition).size();
    }

    /**
     * 取得与给定分组关联的数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return groupTitle.get(groupPosition);
    }

    /**
     * 取得分组数
     */
    @Override
    public int getGroupCount() {
        return groupTitle.size();
    }

    /**
     * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    /*
     *Gets a View that displays the given group
     *return: the View corresponding to the group at the specified position
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_school_activity_elv_group_item, null);
            groupHolder = new GroupHolder();
            groupHolder.groupImg = (ImageView) convertView.findViewById(R.id.mangement_class_activity_elv_group_item_class_icon_iv);
            groupHolder.groupText = (TextView) convertView.findViewById(R.id.mangement_class_activity_elv_group_item_class_name_tv);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            groupHolder.groupImg.setImageResource(R.mipmap.down_arrow);
        }else {
            groupHolder.groupImg.setImageResource(R.mipmap.right_arrow);
        }

        groupHolder.groupText.setText(groupTitle.get(groupPosition).getSchoolname());

//        groupButton = (Button) convertView.findViewById(R.id.btn_group_function);
//        groupButton.setOnClickListener(this);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // Indicates whether the child and group IDs are stable across changes to the underlying data
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // Whether the child at the specified position is selectable
        return true;
    }
    /**
     * show the text on the child and group main_viewpager_item_layout
     */
    private class GroupHolder
    {
        ImageView groupImg;
        TextView groupText;
    }
    private class ChildHolder
    {
        ImageView childImg;
        TextView childText;
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_group_function:
//                Log.d("MyBaseExpandableListAdapter", "你点击了group button");
//            default:
//                break;
//        }
//
//    }
}