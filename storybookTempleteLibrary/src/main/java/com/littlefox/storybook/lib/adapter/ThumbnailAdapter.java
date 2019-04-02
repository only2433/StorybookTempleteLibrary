package com.littlefox.storybook.lib.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.library.view.progress.CircleProgressView;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.object.SharedVideoInfo;
import com.littlefox.storybook.lib.object.VideoInformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ThumbnailAdapter extends BaseAdapter
{
	private SharedVideoInfo mVideoBase;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private RequestOptions mRequestOptions;
	private Drawable mDefaultThumbnailImage;

	
	public ThumbnailAdapter(Context context, SharedVideoInfo videoBase, Drawable defaultThumbnail)
	{
		mContext = context;
		mVideoBase = videoBase;
		mDefaultThumbnailImage = defaultThumbnail;
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount()
	{
		return mVideoBase.getVideoInfoList().size();
	}

	@Override
	public Object getItem(int position)
	{
		return mVideoBase.getVideoInfoList().get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		ViewHolder holder;
		if(convertView == null)
		{
			if(CommonUtils.getInstance(mContext).isTablet())
			{
				view = mLayoutInflater.inflate(R.layout.thumbnail_item_tablet, null);
			}
			else
			{
				view = mLayoutInflater.inflate(R.layout.thumbnail_item, null);
			}
			
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}
		
		try
		{
			ImageView _ThumbnailImage = (ImageView)view.findViewById(R.id.thumbnail_image);

			mRequestOptions = new RequestOptions();
			mRequestOptions.placeholder(mDefaultThumbnailImage);
			
			if (CommonUtils.getInstance(mContext).getPixel(Common.TARGET_DISPLAY_WIDTH) >= Common.TARGET_DISPLAY_WIDTH)
			{
				Glide.with(mContext)
						.setDefaultRequestOptions(mRequestOptions)
						.load(mVideoBase.getVideoInfoList().get(position).getDownloadThumbnailUrl())
						.transition(withCrossFade())
						.into(_ThumbnailImage);
 
			}
			else
			{
				Glide.with(mContext)
						.setDefaultRequestOptions(mRequestOptions)
						.load(mVideoBase.getVideoInfoList().get(position).getDownloadThumbnailUrl())
						.transition(withCrossFade())
						.into(_ThumbnailImage);
			}

		}catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		
		
		switch(mVideoBase.getVideoInfoList().get(position).getStatus())
		{
			
		case VideoInformation.STATUS_DOWNLOAD_AVAILABLE:
			holder.getThumbnailProgress().setVisibility(View.VISIBLE);
			holder.getPurchaseItemImageView().setVisibility(View.GONE);
			holder.getDownloadingCoverImageView().setVisibility(View.GONE);
			holder.getDownloadIdleCoverImageView().setVisibility(View.GONE);
			holder.getDownloadCompleteCheckBorderImageView().setVisibility(View.GONE);
			holder.getPlayCompleteCheckImageView().setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_DOWNLOAD_ING:
			holder.getThumbnailProgress().setVisibility(View.VISIBLE);
			holder.getPurchaseItemImageView().setVisibility(View.GONE);
			holder.getDownloadingCoverImageView().setVisibility(View.VISIBLE);
			holder.getDownloadIdleCoverImageView().setVisibility(View.GONE);
			holder.getDownloadCompleteCheckBorderImageView().setVisibility(View.GONE);
			holder.getPlayCompleteCheckImageView().setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_DOWNLOAD_COMPLETE:
			holder.getThumbnailProgress().setVisibility(View.GONE);
			holder.getPurchaseItemImageView().setVisibility(View.GONE);
			holder.getDownloadingCoverImageView().setVisibility(View.GONE);
			holder.getDownloadIdleCoverImageView().setVisibility(View.GONE);
			holder.getDownloadCompleteCheckBorderImageView().setVisibility(View.GONE);
			holder.getPlayCompleteCheckImageView().setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_DOWNLOAD_IDLE:
			holder.getThumbnailProgress().setVisibility(View.VISIBLE);
			holder.getPurchaseItemImageView().setVisibility(View.GONE);
			holder.getDownloadingCoverImageView().setVisibility(View.GONE);
			holder.getDownloadIdleCoverImageView().setVisibility(View.VISIBLE);
			holder.getDownloadCompleteCheckBorderImageView().setVisibility(View.GONE);
			holder.getPlayCompleteCheckImageView().setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_NOT_PURCHASED:
			holder.getThumbnailProgress().setVisibility(View.GONE);
			holder.getPurchaseItemImageView().setVisibility(View.VISIBLE);
			holder.getDownloadingCoverImageView().setVisibility(View.GONE);
			holder.getDownloadIdleCoverImageView().setVisibility(View.GONE);
			holder.getDownloadCompleteCheckBorderImageView().setVisibility(View.GONE);
			holder.getPlayCompleteCheckImageView().setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_PLAY_COMPLETE:
			holder.getThumbnailProgress().setVisibility(View.GONE);
			holder.getPurchaseItemImageView().setVisibility(View.GONE);
			holder.getDownloadingCoverImageView().setVisibility(View.GONE);
			holder.getDownloadIdleCoverImageView().setVisibility(View.GONE);
			holder.getDownloadCompleteCheckBorderImageView().setVisibility(View.VISIBLE);
			holder.getPlayCompleteCheckImageView().setVisibility(View.VISIBLE);
			break;
		}
		return view;
	}
	
	class ViewHolder
	{
		private View base								= null;
		private ImageView _ThumbnailImage 				= null;
		private ImageView _DownloadingCover 			= null;
		private ImageView _DownloadCompleteCheckBorder  = null;
		private ImageView _PlayCompleteCheck			= null;
		private ImageView _DownloadIdleCover			= null;
		private ImageView _PurchaseImageView			= null;
		private CircleProgressView _ThumbnailProgress 	= null;
		
		public ViewHolder(View base)
		{
			this.base = base;
		}
		
		
		public ImageView getDownloadCompleteCheckBorderImageView()
		{
			if(_DownloadCompleteCheckBorder == null)
			{
				_DownloadCompleteCheckBorder = (ImageView)base.findViewById(R.id.thumbnail_download_complete_check_border);
			}
			return _DownloadCompleteCheckBorder;
		}
		
		public ImageView getDownloadingCoverImageView()
		{
			if(_DownloadingCover == null)
			{
				_DownloadingCover = (ImageView)base.findViewById(R.id.thumbnail_downloading_cover);
			}
			return _DownloadingCover;
		}
		
		public ImageView getPlayCompleteCheckImageView()
		{
			if(_PlayCompleteCheck == null)
			{
				_PlayCompleteCheck = (ImageView)base.findViewById(R.id.thumbnail_play_complete_image);
			}
			return _PlayCompleteCheck;
		}
		
		public ImageView getDownloadIdleCoverImageView()
		{
			if(_DownloadIdleCover == null)
			{
				_DownloadIdleCover = (ImageView)base.findViewById(R.id.thumbnail_idle_cover);
			}
			return _DownloadIdleCover;
		}
		
		public ImageView getPurchaseItemImageView()
		{
			if(_PurchaseImageView == null)
			{
				_PurchaseImageView = (ImageView)base.findViewById(R.id.thumbnail_purchase_image);
			}
			return _PurchaseImageView;
		}
		
		public CircleProgressView getThumbnailProgress()
		{
			if(_ThumbnailProgress == null)
			{
				_ThumbnailProgress = (CircleProgressView)base.findViewById(R.id.thumbnail_progress_image);
			}
			return _ThumbnailProgress;
		}
	}
	
}
