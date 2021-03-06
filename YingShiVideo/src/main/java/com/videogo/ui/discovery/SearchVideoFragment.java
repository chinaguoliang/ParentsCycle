package com.videogo.ui.discovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.videogo.openapi.bean.req.SearchSquareVideoInfo;

public class SearchVideoFragment extends Fragment {
	private EditText belongType, latitude, longitude;
	private EditText range, cameraName;
	private CheckBox viewSort, cameraNameSort, rangeSort;
	private Button submit;
	
	public interface IOnSearchClick {
		void onSearch(SearchSquareVideoInfo searchSquareVideoInfo);
	}
	
	private IOnSearchClick onSearchClick;
	
	public void setOnSearchClick(IOnSearchClick onSearchClick) {
		this.onSearchClick = onSearchClick;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(com.videogo.open.R.layout.fragment_search_video, null, false);
		belongType = (EditText) v.findViewById(com.videogo.open.R.id.belongType);
		latitude = (EditText) v.findViewById(com.videogo.open.R.id.latitude);
		longitude = (EditText) v.findViewById(com.videogo.open.R.id.longitude);
		range = (EditText) v.findViewById(com.videogo.open.R.id.range);
		cameraName = (EditText) v.findViewById(com.videogo.open.R.id.cameraName);
		viewSort = (CheckBox) v.findViewById(com.videogo.open.R.id.viewSort);
		cameraNameSort = (CheckBox) v.findViewById(com.videogo.open.R.id.cameraNameSort);
		rangeSort = (CheckBox) v.findViewById(com.videogo.open.R.id.rangeSort);
		submit = (Button) v.findViewById(com.videogo.open.R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onSearchClick != null) {
					SearchSquareVideoInfo ssvi = new SearchSquareVideoInfo();
					try {
					    ssvi.setBelongType(Integer.valueOf(belongType.getText().toString()));
					} catch (NumberFormatException e) {
					    e.printStackTrace();
					}
					ssvi.setLongitude(longitude.getText().toString());
					ssvi.setLatitude(latitude.getText().toString());
					ssvi.setRange(range.getText().toString());
					ssvi.setCameraName(cameraName.getText().toString());
					ssvi.setViewSort(viewSort.isChecked() ? 1 : 0);
					ssvi.setCameraNameSort(cameraNameSort.isChecked() ? 1 : 0);
					ssvi.setRangeSort(rangeSort.isChecked() ? 1 : 0);
					onSearchClick.onSearch(ssvi);
				}
			}
		});
		return v;
	}
	
	
}
