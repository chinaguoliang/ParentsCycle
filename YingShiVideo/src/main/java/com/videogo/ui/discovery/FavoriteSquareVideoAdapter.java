package com.videogo.ui.discovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.RotateDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.videogo.asynctask.CollectTask;
import com.videogo.asynctask.CollectTask.OnDataNotifyListener;
import com.videogo.openapi.EZFavoriteSquareVideo;
import com.videogo.universalimageloader.core.DisplayImageOptions;
import com.videogo.universalimageloader.core.ImageLoader;
import com.videogo.universalimageloader.core.assist.FailReason;
import com.videogo.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.videogo.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FavoriteSquareVideoAdapter extends VideoGoBaseAdapter<EZFavoriteSquareVideo> {
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());
	private HashMap<Integer, Boolean> doingAction = new HashMap<Integer, Boolean>();

	public FavoriteSquareVideoAdapter(Context context) {
		super(context);
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.showImageOnLoading(com.videogo.open.R.drawable.my_cover)
		.showImageForEmptyUri(com.videogo.open.R.drawable.my_cover)
		.showImageOnFail(com.videogo.open.R.drawable.my_cover)
		.build();
	}
	
	public void clearImageCache() {
		mImageLoader.clearMemoryCache();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(com.videogo.open.R.layout.square_video_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.render(position);
		return convertView;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder {
		private ImageView cover;
		private TextView title, viewedCount, likedCount, commentCount;
		private TextView collectBtn, collectedTV;
		
		public ViewHolder(View view) {
			cover = (ImageView) view.findViewById(com.videogo.open.R.id.cover);
			cover.setDrawingCacheEnabled(false);
			cover.setWillNotCacheDrawing(true);
			title = (TextView) view.findViewById(com.videogo.open.R.id.videoTitle);
			viewedCount = (TextView) view.findViewById(com.videogo.open.R.id.viewedCount);
			likedCount = (TextView) view.findViewById(com.videogo.open.R.id.likedCount);
			collectBtn = (TextView) view.findViewById(com.videogo.open.R.id.collectBtn);
			collectedTV = (TextView) view.findViewById(com.videogo.open.R.id.collectedTV);
		}
		
		public void render(final int position) {
			title.setVisibility(View.INVISIBLE);
			viewedCount.setVisibility(View.INVISIBLE);
			likedCount.setVisibility(View.INVISIBLE);
			
			final EZFavoriteSquareVideo item = getItem(position);
			if (!TextUtils.isEmpty(item.getSquareCoverUrl())) {
				mImageLoader.displayImage(item.getSquareCoverUrl(), cover, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						if (loadedImage != null) {
							ImageView imageView = (ImageView) view;
							boolean firstDisplay = !displayImages.contains(imageUri);
							if (firstDisplay) {
								FadeInBitmapDisplayer.animate(imageView, 500);
								displayImages.add(imageUri);
							}
							title.setVisibility(View.VISIBLE);
							viewedCount.setVisibility(View.VISIBLE);
							likedCount.setVisibility(View.VISIBLE);
						}
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						super.onLoadingCancelled(imageUri, view);
					}
				});
			}
			title.setText(item.getSquareTitle());
			viewedCount.setText(String.valueOf(item.getSquareViewedCount()));
			likedCount.setText(String.valueOf(item.getSquareLikeCount()));
			
			Boolean isDoingAction = doingAction.get(position);
			if (isDoingAction == null) {
				isDoingAction = false;
				doingAction.put(position, isDoingAction);
			}

            final TextView visibleView;
            
            boolean stub =false;
			if (/*item.isCollected()*/stub) {
				collectBtn.setVisibility(View.GONE);
				collectedTV.setVisibility(View.VISIBLE);
                visibleView = collectedTV;
			} else {
				collectBtn.setVisibility(View.VISIBLE);
				collectedTV.setVisibility(View.GONE);
                visibleView = collectBtn;
			}
            visibleView.clearAnimation();
			if (isDoingAction) {
				RotateDrawable rd = (RotateDrawable) context.getResources().getDrawable(com.videogo.open.R.drawable.circle_roate_drawable);
				CollectRotateAnimation cra = new CollectRotateAnimation(rd, 500);
                visibleView.setOnClickListener(null);
                visibleView.setCompoundDrawablesWithIntrinsicBounds(rd, null, null, null);
                visibleView.startAnimation(cra);
			} else {
				visibleView.clearAnimation();
                if (collectBtn.getVisibility() == View.VISIBLE) {
                    visibleView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(com.videogo.open.R.drawable.icn_btn_bg_plus), null, null, null);
                } else {
                    visibleView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
				visibleView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						doingAction.put(position, true);
                        visibleView.setOnClickListener(null);
						RotateDrawable rd = (RotateDrawable) context.getResources().getDrawable(com.videogo.open.R.drawable.circle_roate_drawable);
						CollectRotateAnimation cra = new CollectRotateAnimation(rd, 500);
                        visibleView.setCompoundDrawablesWithIntrinsicBounds(rd, null, null, null);
                        visibleView.startAnimation(cra);
						
						new CollectTask(context,null, item, new OnDataNotifyListener() {
							@Override
							public void onDataNotify(boolean result) {/*mj
								if (result) {
                                    if (item.isCollected()) {
                                        Utils.showToast(context, R.string.cancel_collect_success);
                                    } else {
                                        Utils.showToast(context, R.string.collect_success);
                                    }
									item.setCollected(!item.isCollected());
								} else {
                                    if (item.isCollected()) {
                                        Utils.showToast(context, R.string.cancel_collect_fail);
                                    } else {
                                        Utils.showToast(context, R.string.collect_fail);
                                    }
                                }
                                visibleView.clearAnimation();
								doingAction.put(position, false);
								notifyDataSetChanged();
							*/}
						}).execute();
					}
				});
                if (visibleView == collectedTV && TextUtils.isEmpty(/*item.getFavoriteId()*/"")) {
                    visibleView.setOnClickListener(null);
                    visibleView.setBackgroundResource(0);
                }
			}
		}
	}
}
