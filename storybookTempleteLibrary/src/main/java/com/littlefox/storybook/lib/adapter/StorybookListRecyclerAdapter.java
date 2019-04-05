package com.littlefox.storybook.lib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.adapter.listener.StorybookListItemListener;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.Font;
import com.littlefox.storybook.lib.object.SharedVideoInfo;
import com.littlefox.storybook.lib.object.VideoInformation;
import com.littlefox.storybook.lib.view.CircleProgressView;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.io.File;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class StorybookListRecyclerAdapter extends RecyclerView.Adapter<StorybookListRecyclerAdapter.ViewHolder>
{
	private static final int HEADER_FOOTER_ITEM_COUNT = 2;
	
	private static final int TYPE_HEADER 	= 0;
	private static final int TYPE_THUMBNAIL = 1;
	private static final int TYPE_FOOTER	= 2;
	
	private Context mContext;

	private String mVersionTitle;
	private String mVersionColor;
	private SharedVideoInfo mVideoBase;
	private StorybookListItemListener mStorybookListItemListener;
	

	private File mThumbnailFile = null;
	private int mDefaultThumbnailID = 0;
	private int mTopbarBackgroundImageId = 0;
	private int mMenuImageId = 0;
	private int mDownloadPercent = 0;
	private RequestOptions requestOptions;
	
	public StorybookListRecyclerAdapter(Context context, String versionTitle, String versionColor, SharedVideoInfo videoBase)
	{
		mContext 		= context;
		mVersionTitle 	= versionTitle;
		mVersionColor	= versionColor;
		mVideoBase 		= videoBase;
		
		mDefaultThumbnailID = mContext.getResources().getIdentifier("default_thumbnail", "drawable", mContext.getPackageName());
		mTopbarBackgroundImageId = mContext.getResources().getIdentifier("gnb_top_bg", "drawable", mContext.getPackageName());
		mMenuImageId = mContext.getResources().getIdentifier("gnb_menu_btn", "drawable", mContext.getPackageName()); 
	}
	
	public void setStorybookListItemListener(StorybookListItemListener listener)
	{
		mStorybookListItemListener = listener;
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		ScalableLayout _TopBarLayout;
		ImageView _TopBarBackgroundImage;
		ImageView _TopBarMenuImage;
		
		ScalableLayout 		_ThumbnailLayout;
		ImageView 			_ThumbnailImage;
		ImageView 			_ThumbnailDownloadCompleteCheckBorderImage;
		ImageView 			_ThumbnailDownloadingCoverImage;
		ImageView 			_ThumbnailPlayCompleteImage;
		ImageView 			_ThumbnailLockImage;
		CircleProgressView 	_ThumbnailProgressImage;
		ImageView 			_ThumbnailIdleCoverImage;
		
		ScalableLayout 	_FooterLayout;
		ImageView		_FooterImage;
		TextView		_VersionText;
		
		
		public ViewHolder(View view)
		{
			super(view);
			_TopBarLayout 			= (ScalableLayout)view.findViewById(R.id.top_bar_layout);
			_TopBarBackgroundImage 	= (ImageView)view.findViewById(R.id.top_bar_background_image);
			_TopBarMenuImage 		= (ImageView)view.findViewById(R.id.button_menu);
			
			_ThumbnailLayout 				= (ScalableLayout)view.findViewById(R.id.thumbnail_layout);
			_ThumbnailImage 				= (ImageView)view.findViewById(R.id.thumbnail_image);
			_ThumbnailDownloadCompleteCheckBorderImage = (ImageView)view.findViewById(R.id.thumbnail_download_complete_check_border);
			_ThumbnailDownloadingCoverImage = (ImageView)view.findViewById(R.id.thumbnail_downloading_cover);
			_ThumbnailPlayCompleteImage		= (ImageView)view.findViewById(R.id.thumbnail_play_complete_image);
			_ThumbnailLockImage				= (ImageView)view.findViewById(R.id.thumbnail_purchase_image);
			_ThumbnailProgressImage			= (CircleProgressView)view.findViewById(R.id.thumbnail_progress_image);
			_ThumbnailIdleCoverImage		= (ImageView)view.findViewById(R.id.thumbnail_idle_cover);
			
			_FooterLayout	= (ScalableLayout)view.findViewById(R.id.footer_layout);
			_FooterImage	= (ImageView)view.findViewById(R.id.footer_logo);
			_VersionText	= (TextView)view.findViewById(R.id.version_text);
		}
		
		public CircleProgressView getCircleProgressView()
		{
			return _ThumbnailProgressImage;
		}
		
	}
	
	

	@Override
	public long getItemId(int position)
	{
		if(position == 0)
		{
			return "Top Bar Ground".hashCode();
		}
		else if(position > 0 && position <= mVideoBase.getVideoInfoList().size())
		{
			int thumbnailIndex = position - 1;
			
			Log.i("position : "+ position +", hashCode : "+mVideoBase.getVideoInfoList().get(thumbnailIndex).getDownloadThumbnailUrl().hashCode());
			return mVideoBase.getVideoInfoList().get(thumbnailIndex).getDownloadThumbnailUrl().hashCode();
		}
		else
		{
			return "Bottom Bar Ground".hashCode();
		}
	}

	@Override
	public int getItemCount()
	{
		return mVideoBase.getVideoInfoList().size() + HEADER_FOOTER_ITEM_COUNT;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		
		if(position == 0)
		{
			setViewType(holder,TYPE_HEADER);
			
			holder._TopBarBackgroundImage.setImageResource(mTopbarBackgroundImageId);
			holder._TopBarMenuImage.setImageResource(mMenuImageId);
			
			holder._TopBarMenuImage.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mStorybookListItemListener.onMenuButtonClicked();
				}
			});
		}
		else if(position > 0 && position <= mVideoBase.getVideoInfoList().size())
		{
			setViewType(holder,TYPE_THUMBNAIL);
			setStatusThumbnailItem(holder, position - 1);
			if(mVideoBase.getVideoInfoList().get(position - 1).getStatus() == VideoInformation.STATUS_DOWNLOAD_ING
					|| mVideoBase.getVideoInfoList().get(position - 1).getStatus() == VideoInformation.STATUS_DOWNLOAD_IDLE
					|| mVideoBase.getVideoInfoList().get(position - 1).getStatus() == VideoInformation.STATUS_DOWNLOAD_AVAILABLE)
			{
				holder._ThumbnailProgressImage.setPercent(mVideoBase.getVideoInfoList().get(position - 1).getDownloadProgress());
			}
			
			
			mThumbnailFile = new File(CommonUtils.getInstance(mContext).getThumbnailPath(position));

			requestOptions = new RequestOptions();
			requestOptions.placeholder(mDefaultThumbnailID);

			Glide.with(mContext)
					.setDefaultRequestOptions(requestOptions)
					.load(mThumbnailFile)
					.transition(withCrossFade())
					.into(holder._ThumbnailImage);
			
			holder._ThumbnailImage.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					
					mStorybookListItemListener.onThumbnailItemSelected(position-1);
				}
			});
		}
		else
		{
			setViewType(holder,TYPE_FOOTER);
			
			holder._VersionText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
			holder._FooterImage.setImageResource(getFooterLogo(holder));
			holder._VersionText.setText(mVersionTitle);
			holder._VersionText.setTextColor(Color.parseColor(mVersionColor));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup container, int viewType)
	{
		View view;
		
		if(StorybookTempleteAPI.IS_TABLET)
			view = LayoutInflater.from(container.getContext()).inflate(R.layout.thumbnaill_recycler_view_tablet_item, container, false);
		else
			view = LayoutInflater.from(container.getContext()).inflate(R.layout.thumbnaill_recycler_view_item, container, false);
		
		return new ViewHolder(view);
	}
	
	public void downloadingProgress(int position)
	{
		notifyItemChanged(position+1);
	}
	
	private void setViewType(ViewHolder holder, int type)
	{
		switch(type)
		{
		case TYPE_HEADER:
			holder._TopBarLayout.setVisibility(View.VISIBLE);
			holder._ThumbnailLayout.setVisibility(View.GONE);
			holder._FooterLayout.setVisibility(View.GONE);
			break;
		case TYPE_THUMBNAIL:
			holder._TopBarLayout.setVisibility(View.GONE);
			holder._ThumbnailLayout.setVisibility(View.VISIBLE);
			holder._FooterLayout.setVisibility(View.GONE);
			break;
		case TYPE_FOOTER:
			holder._TopBarLayout.setVisibility(View.GONE);
			holder._ThumbnailLayout.setVisibility(View.GONE);
			holder._FooterLayout.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private void setStatusThumbnailItem(ViewHolder holder, int position)
	{
		switch(mVideoBase.getVideoInfoList().get(position).getStatus())
		{
			
		case VideoInformation.STATUS_DOWNLOAD_AVAILABLE:
			holder._ThumbnailProgressImage.setVisibility(View.VISIBLE);
			holder._ThumbnailLockImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadingCoverImage.setVisibility(View.GONE);
			holder._ThumbnailIdleCoverImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadCompleteCheckBorderImage.setVisibility(View.GONE);
			holder._ThumbnailPlayCompleteImage.setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_DOWNLOAD_ING:
			holder._ThumbnailProgressImage.setVisibility(View.VISIBLE);
			holder._ThumbnailLockImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadingCoverImage.setVisibility(View.VISIBLE);
			holder._ThumbnailIdleCoverImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadCompleteCheckBorderImage.setVisibility(View.GONE);
			holder._ThumbnailPlayCompleteImage.setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_DOWNLOAD_COMPLETE:
			holder._ThumbnailProgressImage.setVisibility(View.GONE);
			holder._ThumbnailLockImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadingCoverImage.setVisibility(View.GONE);
			holder._ThumbnailIdleCoverImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadCompleteCheckBorderImage.setVisibility(View.GONE);
			holder._ThumbnailPlayCompleteImage.setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_DOWNLOAD_IDLE:
			holder._ThumbnailProgressImage.setVisibility(View.VISIBLE);
			holder._ThumbnailLockImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadingCoverImage.setVisibility(View.GONE);
			holder._ThumbnailIdleCoverImage.setVisibility(View.VISIBLE);
			holder._ThumbnailDownloadCompleteCheckBorderImage.setVisibility(View.GONE);
			holder._ThumbnailPlayCompleteImage.setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_NOT_PURCHASED:
			holder._ThumbnailProgressImage.setVisibility(View.GONE);
			holder._ThumbnailLockImage.setVisibility(View.VISIBLE);
			holder._ThumbnailDownloadingCoverImage.setVisibility(View.GONE);
			holder._ThumbnailIdleCoverImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadCompleteCheckBorderImage.setVisibility(View.GONE);
			holder._ThumbnailPlayCompleteImage.setVisibility(View.GONE);
			break;
		case VideoInformation.STATUS_PLAY_COMPLETE:
			holder._ThumbnailProgressImage.setVisibility(View.GONE);
			holder._ThumbnailLockImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadingCoverImage.setVisibility(View.GONE);
			holder._ThumbnailIdleCoverImage.setVisibility(View.GONE);
			holder._ThumbnailDownloadCompleteCheckBorderImage.setVisibility(View.VISIBLE);
			holder._ThumbnailPlayCompleteImage.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private int getFooterLogo(ViewHolder holder)
	{
		int id = 0;
		if(Locale.getDefault().toString().contains(Locale.SIMPLIFIED_CHINESE.toString()) || Locale.getDefault().toString().contains(Locale.TRADITIONAL_CHINESE.toString()))
		{
			id = mContext.getResources().getIdentifier("footer_logo_cn", "drawable", mContext.getPackageName());
		}
		else if(Locale.getDefault().toString().equals(Locale.KOREA.toString()))
		{
			id = mContext.getResources().getIdentifier("footer_logo", "drawable", mContext.getPackageName());
		}
		else
		{
			id = mContext.getResources().getIdentifier("footer_logo2", "drawable", mContext.getPackageName());
		}
		
		return id;
	}
}
