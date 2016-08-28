package com.videogo.ui.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.videogo.openapi.EZAlarmInfo;
import com.videogo.universalimageloader.core.DisplayImageOptions;
import com.videogo.universalimageloader.core.ImageLoader;
import com.videogo.universalimageloader.core.assist.FailReason;
import com.videogo.universalimageloader.core.download.DecryptFileInfo;
import com.videogo.universalimageloader.core.listener.ImageLoadingListener;
import com.videogo.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.videogo.widget.PinnedSectionListView.PinnedSectionListAdapter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import com.videogo.leavemessage.LeaveMessageItem;

public class EZMessageListAdapter2 extends BaseAdapter implements View.OnClickListener, OnCreateContextMenuListener,
        OnCheckedChangeListener, PinnedSectionListAdapter {

    /** 删除菜单 */
    public static final int MENU_DEL_ID = Menu.FIRST + 1;
    /** 更多菜单 */
    public static final int MENU_MORE_ID = Menu.FIRST + 2;

    private class ViewHolder {
        CheckBox check;
        TextView timeText;
        ImageView image;
        TextView fromTip;
        TextView from;
        TextView type;
        ViewGroup layout;
        ProgressBar imageProgress;
        ImageView unread;
    }

    private final DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final String[] mWeekdayNames = DateFormatSymbols.getInstance(Locale.getDefault()).getWeekdays();

    private Context mContext;
    private List<Object> mObjects;
    private Map<String, Boolean> mCheckStates = new HashMap<String, Boolean>();

    private Calendar mLastDate;
    private String mDeviceSerial;

    /** 监听对象 */
    private OnClickListener mOnClickListener;

    private ImageLoader mImageLoader;
    private boolean mCheckMode;
    private boolean mNoMenu;
    private boolean mDataSetChanged;

    private EZMessageListAdapter2(Context context, String deviceSerial) {
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mDeviceSerial = deviceSerial;
    }

    public EZMessageListAdapter2(Context context, List<? extends Object> list, String deviceSerial) {
        this(context, deviceSerial);
        mDeviceSerial = deviceSerial;
        setList(list);
    }

    public void setList(List<? extends Object> list) {
        if (list == null) {
            return;
        }
        List<Object> objects = new ArrayList<Object>();

        Map<String, Boolean> preCheckStates = mCheckStates;
        mCheckStates = new HashMap<String, Boolean>();

        mLastDate = null;
        Calendar tempDate = Calendar.getInstance();
        try {
//            if(list.size() == 1)//mj
//                return;
            for (Object item : list) {
//                String id = item instanceof LeaveMessageItem ? ((LeaveMessageItem) item).getMessageId()
//                        : ((EZAlarmInfo) item).getAlarmLogId();
//                String time = item instanceof LeaveMessageItem ? ((LeaveMessageItem) item).getCreateTime()
//                        : ((EZAlarmInfo) item).getAlarmStartTime();
                String id = ((EZAlarmInfo) item).getAlarmId();
                //mj String time = ((EZAlarmInfo) item).getAlarmStartTime();
                String time = ((EZAlarmInfo) item).getAlarmStartTime();

                try {
                tempDate.setTime(mDateFormat.parse(time));
                } catch (ParseException e) {
                    //tempDate.setTime(mDateFormat.parse(time));
                    e.printStackTrace();
                }
                if (mLastDate == null || !isSameDate(mLastDate, tempDate)) {
                    mLastDate = (Calendar) tempDate.clone();
                    objects.add(mLastDate);
                }
                objects.add(item);

                Boolean check = preCheckStates.get(id);
                if (check != null && check)
                    mCheckStates.put(id, true);
            }
        } catch (Exception e) {
        }

        mObjects = objects;
    }

    private boolean isSameDate(Calendar firstDate, Calendar secondDate) {
        return (firstDate.get(Calendar.DAY_OF_YEAR) == secondDate.get(Calendar.DAY_OF_YEAR) && firstDate
                .get(Calendar.YEAR) == secondDate.get(Calendar.YEAR));
    }

    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    public void setNoMenu(boolean noMenu) {
        mNoMenu = noMenu;
    }

    public void setCheckMode(boolean checkMode) {
        if (mCheckMode != checkMode) {
            mCheckMode = checkMode;
            if (!checkMode) {
                uncheckAll();
            }
        }
    }

    public boolean isCheckAll() {
        for (Object item : mObjects) {
            String id = null;
            if (item instanceof EZAlarmInfo)
                id = ((EZAlarmInfo) item).getAlarmId();
//            else if (item instanceof LeaveMessageItem)
//                id = ((LeaveMessageItem) item).getMessageId();

            if (id != null) {
                Boolean check = mCheckStates.get(id);
                if (check == null || !check)
                    return false;
            }
        }
        return true;
    }

    public void checkAll() {
        for (Object item : mObjects) {
            String id = null;
            if (item instanceof EZAlarmInfo)
                id = ((EZAlarmInfo) item).getAlarmId();
//            else if (item instanceof LeaveMessageItem)
//                id = ((LeaveMessageItem) item).getMessageId();

            if (id != null)
                mCheckStates.put(id, true);
        }
        notifyDataSetChanged();
    }

    public void uncheckAll() {
        mCheckStates.clear();
        notifyDataSetChanged();
    }

    public List<String> getCheckedIds() {
        List<String> ids = new ArrayList<String>();
        Set<Entry<String, Boolean>> entries = mCheckStates.entrySet();
        for (Entry<String, Boolean> entry : entries) {
            if (entry.getValue() != null && entry.getValue())
                ids.add(entry.getKey());
        }
        return ids;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (mObjects.get(position) instanceof Calendar) ? 0 : 1;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 0;
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            if (viewType == 0) {
                convertView = LayoutInflater.from(mContext).inflate(com.videogo.open.R.layout.ez_message_list_section, parent, false);

                // 获取控件对象
                viewHolder.timeText = (TextView) convertView.findViewById(com.videogo.open.R.id.message_time);

            } else {
                convertView = LayoutInflater.from(mContext).inflate(com.videogo.open.R.layout.ez_message_list_item, parent, false);

                // 获取控件对象
                viewHolder.check = (CheckBox) convertView.findViewById(com.videogo.open.R.id.message_check);
                viewHolder.timeText = (TextView) convertView.findViewById(com.videogo.open.R.id.message_time);
                viewHolder.layout = (ViewGroup) convertView.findViewById(com.videogo.open.R.id.message_layout);
                viewHolder.image = (ImageView) convertView.findViewById(com.videogo.open.R.id.message_image);
                viewHolder.fromTip = (TextView) convertView.findViewById(com.videogo.open.R.id.message_from_tip);
                viewHolder.from = (TextView) convertView.findViewById(com.videogo.open.R.id.message_from);
                viewHolder.type = (TextView) convertView.findViewById(com.videogo.open.R.id.message_type);
                viewHolder.imageProgress = (ProgressBar) convertView.findViewById(com.videogo.open.R.id.message_image_progress);
                viewHolder.unread = (ImageView) convertView.findViewById(com.videogo.open.R.id.message_unread);

                // 防止获得缓存
                viewHolder.image.setDrawingCacheEnabled(false);
                viewHolder.image.setWillNotCacheDrawing(true);

                // 点击弹出菜单的监听
                viewHolder.layout.setOnCreateContextMenuListener(this);
                // 内容区域的点击响应
                viewHolder.layout.setOnClickListener(this);
                viewHolder.check.setOnClickListener(this);
                viewHolder.check.setOnCheckedChangeListener(this);
            }

            // 设置控件集到convertView
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewType == 0) {
            // 获取数据
            Calendar date = (Calendar) getItem(position);
            String displayText;
            if (isSameDate(date, Calendar.getInstance())) {
                displayText = mContext.getString(com.videogo.open.R.string.today);
            } else {
                displayText = (date.get(Calendar.MONTH) + 1) + mContext.getString(com.videogo.open.R.string.month)
                        + date.get(Calendar.DAY_OF_MONTH) + mContext.getString(com.videogo.open.R.string.day) + ' '
                        + mWeekdayNames[date.get(Calendar.DAY_OF_WEEK)];
            }
            viewHolder.timeText.setText(displayText);

        } else {
            // 设置position
            viewHolder.layout.setTag(com.videogo.open.R.id.tag_key_position, position);
            viewHolder.check.setTag(com.videogo.open.R.id.tag_key_position, position);

            viewHolder.check.setVisibility(mCheckMode ? View.VISIBLE : View.GONE);

            Object item = getItem(position);

            if (item instanceof EZAlarmInfo) {
                EZAlarmInfo alarmLogInfo = (EZAlarmInfo) item;

                if (mCheckMode) {
                    Boolean checked = mCheckStates.get(alarmLogInfo.getAlarmId());
                    viewHolder.check.setChecked(checked == null ? false : checked);
                }

                // 消息类型
//                AlarmType alarmType = alarmLogInfo.getEnumAlarmType();
//
//                viewHolder.type.setText(alarmType == AlarmType.DOORBELL_ALARM ? alarmLogInfo.getSampleName() : mContext
//                        .getString(alarmType.getTextResId()));
                AlarmType alarmType = AlarmType.BODY_ALARM;

                viewHolder.type.setText(mContext.getString(alarmType.getTextResId()));

                // 消息来源
                viewHolder.from.setText(alarmLogInfo.getAlarmName());

                // 消息时间
                if (alarmLogInfo.getAlarmStartTime() != null)
                    viewHolder.timeText.setText(alarmLogInfo.getAlarmStartTime().split(" ")[1]);
                else
                    viewHolder.timeText.setText(null);

                // 消息查看状态
                viewHolder.unread.setVisibility(alarmLogInfo.getIsRead() == 0 ? View.VISIBLE : View.INVISIBLE);

                viewHolder.imageProgress.setProgress(0);
                viewHolder.imageProgress.setVisibility(View.VISIBLE);

                //mj AlarmLogInfo relAlarm = alarmLogInfo.getRelationAlarms();
                EZAlarmInfo relAlarm = null;
                // 消息图片
                //boolean detector_ipc_link = relAlarm.getEnumAlarmType() == AlarmType.DETECTOR_IPC_LINK;
                boolean detector_ipc_link = false;
                boolean alarm_has_camera = true;
                if (detector_ipc_link) {
                    if (!mDataSetChanged) {
                        loadImage(viewHolder, relAlarm);
                    }
                } else if (alarm_has_camera) {
                    if (!mDataSetChanged) {
                        loadImage(viewHolder, alarmLogInfo);
                    }
                } else {
                    mImageLoader.cancelDisplayTask(viewHolder.image);
                    viewHolder.image.setBackgroundResource(com.videogo.open.R.drawable.message_a1_bg);
                    viewHolder.image.setImageResource(alarmType.getDrawableResId());
                    viewHolder.imageProgress.setVisibility(View.GONE);
                }

            } /*else if (item instanceof LeaveMessageItem) {
                LeaveMessageItem leaveMessage = (LeaveMessageItem) item;

                if (mCheckMode) {
                    Boolean checked = mCheckStates.get(leaveMessage.getMessageId());
                    viewHolder.check.setChecked(checked == null ? false : checked);
                }

                // 消息类型
                viewHolder.type.setText(R.string.video_leave_message);

                // 消息来源
                viewHolder.from.setText(leaveMessage.getDeviceName());

                // 消息时间
                if (leaveMessage.getCreateTime() != null)
                    viewHolder.timeText.setText(leaveMessage.getCreateTime().split(" ")[1]);
                else
                    viewHolder.timeText.setText(null);

                // 消息查看状态
                viewHolder.unread.setVisibility(leaveMessage.getStatus() == 0 ? View.VISIBLE : View.INVISIBLE);

                // 消息图片
                mImageLoader.cancelDisplayTask(viewHolder.image);
                viewHolder.image.setImageResource(R.drawable.message_f1);
                viewHolder.imageProgress.setVisibility(View.GONE);
            }*/
        }

        return convertView;
    }

    private void loadImage(final ViewHolder viewHolder, EZAlarmInfo alarmLogInfo) {
        viewHolder.image.setBackgroundDrawable(null);
        viewHolder.image.setImageResource(com.videogo.open.R.drawable.notify_bg);
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .needDecrypt(alarmLogInfo.getAlarmEncryption()).considerExifParams(true)
                .showImageForEmptyUri(com.videogo.open.R.drawable.event_list_fail_pic).showImageOnFail(com.videogo.open.R.drawable.event_list_fail_pic)
                .showImageOnDecryptFail(com.videogo.open.R.drawable.alarm_encrypt_image_mid)
                .extraForDownloader(new DecryptFileInfo(mDeviceSerial, alarmLogInfo.getCheckSum()))
                .build();

        mImageLoader.displayImage(alarmLogInfo.getAlarmPicUrl(), viewHolder.image, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                viewHolder.imageProgress.setProgress(0);
                viewHolder.imageProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                viewHolder.imageProgress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.imageProgress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                viewHolder.imageProgress.setVisibility(View.GONE);
            }

        }, new ImageLoadingProgressListener() {

            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                viewHolder.imageProgress.setProgress(current * 100 / total);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position;
        int i = v.getId();
        if (i == com.videogo.open.R.id.message_layout) {
            position = (Integer) v.getTag(com.videogo.open.R.id.tag_key_position);

            if (mCheckMode) {
                CheckBox checkBox = (CheckBox) v.findViewById(com.videogo.open.R.id.message_check);
                checkBox.toggle();
                if (mOnClickListener != null)
                    mOnClickListener
                            .onCheckClick(EZMessageListAdapter2.this, checkBox, position, checkBox.isChecked());
            } else {
                if (mOnClickListener != null)
                    mOnClickListener.onItemClick(EZMessageListAdapter2.this, v, position);
            }

        } else if (i == com.videogo.open.R.id.message_check) {
            position = (Integer) v.getTag(com.videogo.open.R.id.tag_key_position);
            boolean check = ((CheckBox) v).isChecked();

            if (mOnClickListener != null)
                mOnClickListener.onCheckClick(EZMessageListAdapter2.this, v, position, check);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (!mCheckMode && !mNoMenu) {
            menu.add(Menu.NONE, MENU_DEL_ID, Menu.NONE, mContext.getString(com.videogo.open.R.string.delete));

            int position = (Integer) v.getTag(com.videogo.open.R.id.tag_key_position);
//            menu.add(Menu.NONE, MENU_MORE_ID, Menu.NONE, mContext.getString(R.string.tab_more));

            if (mOnClickListener != null)
                mOnClickListener.onItemLongClick(EZMessageListAdapter2.this, v, position);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (Integer) buttonView.getTag(com.videogo.open.R.id.tag_key_position);
        Object item = getItem(position);
        if (item instanceof EZAlarmInfo)
            mCheckStates.put(((EZAlarmInfo) item).getAlarmId(), isChecked);
//        else if (item instanceof LeaveMessageItem)
//            mCheckStates.put(((LeaveMessageItem) item).getMessageId(), isChecked);
    }

    public void clearImageCache() {
        mImageLoader.clearMemoryCache();
    }

    public interface OnClickListener {

        public void onCheckClick(BaseAdapter adapter, View view, int position, boolean checked);

        public void onItemLongClick(BaseAdapter adapter, View view, int position);

        public void onItemClick(BaseAdapter adapter, View view, int position);
    }
}