package com.littlefox.storybook.lib.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.littlefox.library.storybooktempletelibrary.R;
import com.littlefox.library.view.animator.ViewAnimator;
import com.littlefox.library.view.listener.OnClickIndexListener;
import com.littlefox.storybook.lib.analytics.GoogleAnalyticsHelper;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.Font;
import com.littlefox.storybook.lib.object.RecommandAppInformation;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class RecommandAppScrollDialog extends Dialog
{
	Handler mRefreshHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MESSAGE_REFRESH_SCROLLVIEW:
				_RecommandItemScrollView.scrollTo(0, 0);
				changeRecommandItemInformation(TYPE_POPULAR);
				break;
			}
		}

	};
	
	private static final int MESSAGE_REFRESH_SCROLLVIEW = 0;
	
	private static final int DURATION_REFRESH = 100;
	
	private static final int TYPE_POPULAR 	= 0;
	private static final int TYPE_NEWEST 	= 1;
	private static final int TYPE_NAME		= 2;
	
	private Context mContext;
	private ArrayList<RecommandAppInformation> mRecommandAppInformationList;
	
	private TextView _RecommandTitleText;
	private TextView _RecommandTabPopularTitleText;
	private TextView _RecommandTabNewestTitleText;
	private TextView _RecommandTabNameTitleText;
	
	private ImageView _RecommandCloseButton;
	
	private ScrollView _RecommandItemScrollView;
	private ScalableLayout _RecommandItemAddLayout;
	
	private static final int DIALOG_BASE_WIDTH  = 1743;
	private static final int DIALOG_BASE_HEIGHT = 1036;
	private static final int ROW_COUNT		= 5;
	
	private static final int DURATION_ANIMATION = 500;


	private boolean isBackCardVisible = false;
	
	private boolean isLocaleKorea = false;

	public RecommandAppScrollDialog(Context context , ArrayList<RecommandAppInformation> recommandAppInformationList)
	{
		super(context, R.style.BackgroundDialog_Big);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_recommand_app_scrollview_layout);
		
		init(context, recommandAppInformationList);
	}
	
	
	
	private void init(Context context , ArrayList<RecommandAppInformation> recommandAppInformationList)
	{
		mContext = context;
		mRecommandAppInformationList = recommandAppInformationList;
		
		removeCurrentPackageItem(mRecommandAppInformationList);
		checkStoriesAppInstalled(mRecommandAppInformationList);
		
		_RecommandTitleText 			= (TextView)findViewById(R.id.recommand_title);
		_RecommandTabPopularTitleText 	= (TextView)findViewById(R.id.recommand_popular_tab_title);
		_RecommandTabNewestTitleText 	= (TextView)findViewById(R.id.recommand_newest_tab_title);
		_RecommandTabNameTitleText		= (TextView)findViewById(R.id.recommand_name_tab_title);
		
		_RecommandItemScrollView		= (ScrollView)findViewById(R.id.recommand_scroll_view);
		_RecommandItemAddLayout			= (ScalableLayout)findViewById(R.id.add_recommand_list_layout);
		
		_RecommandCloseButton			= (ImageView)findViewById(R.id.recommand_app_close_btn);
		_RecommandCloseButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		
		_RecommandTabPopularTitleText.setOnClickListener(mRecommandTabListener);
		_RecommandTabNewestTitleText.setOnClickListener(mRecommandTabListener);
		_RecommandTabNameTitleText.setOnClickListener(mRecommandTabListener);
		
		initFont();
		checkLocaleKorea();
		settingRecommandTabSelect(TYPE_POPULAR);
		settingListSortByType(TYPE_POPULAR);
		settingRecommandItemLayout();
		
		mRefreshHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_SCROLLVIEW, DURATION_REFRESH);
		
	}
	
	private void initFont()
	{
		_RecommandTitleText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
		_RecommandTabPopularTitleText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
		_RecommandTabNewestTitleText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
		_RecommandTabNameTitleText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
	}
	
	private void checkLocaleKorea()
	{
		if(Locale.getDefault().getLanguage().toString().contains("ko"))
		{
			isLocaleKorea = true;
		}
		else
		{
			isLocaleKorea = false;
		}
	}
	
	private void settingRecommandItemLayout()
	{
		
		
		final int ROW_SPACING 		= 28;
		final int COLUMN_SPACING	= 24;
		final int CARD_WIDTH		= 297;
		final int CARD_HEIGHT 		= 368;
		final int CARD_INIT_MARGIN_LEFT = 72;
		
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int positionTotalCount = mRecommandAppInformationList.size();
		int columnCount = mRecommandAppInformationList.size() / ROW_COUNT;
		columnCount = mRecommandAppInformationList.size() % ROW_COUNT != 0 ? columnCount + 1 : columnCount;
		int positionCount = 0;
		final int positionIndex = -1;
		_RecommandItemAddLayout.setScaleHeight((CARD_HEIGHT + COLUMN_SPACING) * columnCount); 
		ImageView appImage;
		TextView appTitle;
		for(int i = 0; i < columnCount ; i++)
		{
			for(int j = 0 ; j < ROW_COUNT; j++)
			{
				if(positionCount >= positionTotalCount)
				{
					break;
				}

				final FrameLayout base = (FrameLayout)inflater.inflate(R.layout.dialog_recommand_app_item, null);
				base.setOnClickListener(new OnClickIndexListener(positionCount)
				{

					@Override
					public void onClick(View v)
					{
						if(CommonUtils.getInstance(mContext).getPackageVersionName(mRecommandAppInformationList.get(index).getPackageName()).equals("") == false)
						{
							CommonUtils.getInstance(mContext).launchOtherApplication(mRecommandAppInformationList.get(index).getPackageName());
							GoogleAnalyticsHelper.getInstance(mContext).sendCurrentEvent(StorybookTempleteAPI.ANALYTICS_CATEGORY_RECOMMONAD_ICON, mRecommandAppInformationList.get(index).getAppName()+" "+StorybookTempleteAPI.ANALYTICS_ACTION_EXCUTE);

						}
						else
						{
							CommonUtils.getInstance(mContext).startLinkMove(mRecommandAppInformationList.get(index).getLinkUrl());
							GoogleAnalyticsHelper.getInstance(mContext).sendCurrentEvent(StorybookTempleteAPI.ANALYTICS_CATEGORY_RECOMMONAD_ICON, mRecommandAppInformationList.get(index).getAppName()+" "+StorybookTempleteAPI.ANALYTICS_ACTION_APP_STORE_MOVE);
						}
							
					}

				});
				
/*				appImage = (ImageView)base.findViewById(R.id.front_title_image);
				appTitle = (TextView)base.findViewById(R.id.front_title_text);
				appTitle.setTypeface(Font.getInstance(mContext).getRobotoMedium());
				
				Glide.with(mContext)
				.load(mRecommandAppInformationList.get(positionCount).getFileDownloadPath())
				.crossFade()
				.into(appImage);
				
				appTitle.setText(mRecommandAppInformationList.get(positionCount).getAppName());
				*/
				_RecommandItemAddLayout.addView(base, CARD_INIT_MARGIN_LEFT + (j * CARD_WIDTH) + (j * ROW_SPACING) , (CARD_HEIGHT * i) + (COLUMN_SPACING * i), CARD_WIDTH, CARD_HEIGHT);
				
				positionCount++;
				
			}

		}
		
		
	}
	
	private void changeRecommandItemInformation(int type)
	{
		final int COLUMN_ANIMATION_DELAY_DURATION = 100;
		final int SHOW_ITEM_VISIBLE_COUNT = 10;
		int positionTotalCount = mRecommandAppInformationList.size();
		int columnCount = mRecommandAppInformationList.size() / ROW_COUNT;
		columnCount = mRecommandAppInformationList.size() % ROW_COUNT != 0 ? columnCount +1 : columnCount;
		int positionCount = 0;
		isBackCardVisible = !isBackCardVisible;
		settingRecommandTabSelect(type);
		settingListSortByType(type);

		ImageView appFrontImage;
		ImageView appFrontAppInstallImage;
		TextView appFrontTitle;


		for(int i = 0; i < columnCount ; i++)
		{
			for(int j = 0 ; j < ROW_COUNT; j++)
			{
				if(positionCount >= positionTotalCount)
				{
					break;
				}

				FrameLayout base = (FrameLayout)_RecommandItemAddLayout.getChildAt(positionCount);
				

				appFrontImage = (ImageView)base.findViewById(R.id.front_title_image);
				appFrontAppInstallImage = (ImageView)base.findViewById(R.id.front_app_install_image);
				appFrontTitle = (TextView)base.findViewById(R.id.front_title_text);

				appFrontTitle.setTypeface(Font.getInstance(mContext).getRobotoMedium());
				
				

				Glide.with(mContext)
						.setDefaultRequestOptions(null)
				.load(mRecommandAppInformationList.get(positionCount).getFileDownloadPath())
				.transition(withCrossFade())
				.into(appFrontImage);
				
				appFrontTitle.setText(mRecommandAppInformationList.get(positionCount).getAppName());
				
				if(mRecommandAppInformationList.get(positionCount).isPackageInstalled())
				{
					appFrontAppInstallImage.setVisibility(View.GONE);
				}
				else
				{
					appFrontAppInstallImage.setVisibility(View.VISIBLE);
					if(isLocaleKorea)
					{
						appFrontAppInstallImage.setImageResource(R.drawable.icon_appinstall_kr);
					}
					else
					{
						appFrontAppInstallImage.setImageResource(R.drawable.icon_appinstall_en);
					}
				}
				
				
				
				if(Build.VERSION.SDK_INT >= Common.MALSHMALLOW)
				{
					if(positionCount < SHOW_ITEM_VISIBLE_COUNT)
					{
						ViewAnimator.animate(base)
						.wave()
						.startDelay((positionCount % ROW_COUNT) * COLUMN_ANIMATION_DELAY_DURATION)
						.duration(DURATION_ANIMATION)
						.start();
					}
				}
				
				positionCount++;
			}

		}	
	}
	
	private void checkStoriesAppInstalled(ArrayList<RecommandAppInformation> list)
	{
		Intent intent;
		for(int i = 0 ; i < list.size(); i++)
		{
			intent = mContext.getPackageManager().getLaunchIntentForPackage(list.get(i).getPackageName());

			if(intent != null)
			{
				list.get(i).setInstalled(true);
			}
			else
			{
				list.get(i).setInstalled(false);
			}
		}
	}
	
	private void removeCurrentPackageItem(ArrayList<RecommandAppInformation> list)
	{
		for(int i = 0 ; i < list.size(); i++)
		{
			if(list.get(i).getPackageName().equals(StorybookTempleteAPI.PACKAGE_NAME))
			{
				list.remove(i);
				break;
			}
		}
	}
	
	private View.OnClickListener mRecommandTabListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			int currentType = -1;
			int id = v.getId();
			if (id == R.id.recommand_popular_tab_title)
			{
				currentType = TYPE_POPULAR;
				GoogleAnalyticsHelper.getInstance(mContext).sendCurrentEvent(StorybookTempleteAPI.ANALYTICS_CATEGORY_RECOMMONAD_ICON, StorybookTempleteAPI.ANALYTICS_ACTION_POPULAR_TAB);
			} else if (id == R.id.recommand_newest_tab_title)
			{
				GoogleAnalyticsHelper.getInstance(mContext).sendCurrentEvent(StorybookTempleteAPI.ANALYTICS_CATEGORY_RECOMMONAD_ICON, StorybookTempleteAPI.ANALYTICS_ACTION_NEWEST_TAB);
				currentType = TYPE_NEWEST;
			} else if (id == R.id.recommand_name_tab_title)
			{
				GoogleAnalyticsHelper.getInstance(mContext).sendCurrentEvent(StorybookTempleteAPI.ANALYTICS_CATEGORY_RECOMMONAD_ICON, StorybookTempleteAPI.ANALYTICS_ACTION_NAME_TAB);
				currentType = TYPE_NAME;
			}
			_RecommandItemScrollView.scrollTo(0, 0);
			changeRecommandItemInformation(currentType);

		}
	};
	
	private void settingListSortByType(int type)
	{
		Comparator<RecommandAppInformation> comparator = null;
		switch(type)
		{
		case TYPE_POPULAR:
			comparator = new PopularAscCompare();
			break;
		case TYPE_NEWEST:
			comparator = new NewestAscCompare();
			break;
		case TYPE_NAME:
			comparator = new NameAscCompare();
			break;
		}
		
		Collections.sort(mRecommandAppInformationList , comparator);
		
	}
	
	private void settingRecommandTabSelect(int type)
	{
		switch(type)
		{
		case TYPE_POPULAR:
			_RecommandTabPopularTitleText.setTextColor(mContext.getResources().getColor(R.color.white));
			_RecommandTabNewestTitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_RecommandTabNameTitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_RecommandTabPopularTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab01_on, null));
			_RecommandTabNewestTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab02_off, null));
			_RecommandTabNameTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab03_off, null));
			break;
		case TYPE_NEWEST:
			_RecommandTabPopularTitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_RecommandTabNewestTitleText.setTextColor(mContext.getResources().getColor(R.color.white));
			_RecommandTabNameTitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_RecommandTabPopularTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab01_off, null));
			_RecommandTabNewestTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab02_on, null));
			_RecommandTabNameTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab03_off, null));
			break;
		case TYPE_NAME:
			_RecommandTabPopularTitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_RecommandTabNewestTitleText.setTextColor(mContext.getResources().getColor(R.color.black));
			_RecommandTabNameTitleText.setTextColor(mContext.getResources().getColor(R.color.white));
			_RecommandTabPopularTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab01_off, null));
			_RecommandTabNewestTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab02_off, null));
			_RecommandTabNameTitleText.setBackground(mContext.getResources().getDrawable(R.drawable.recommend_tab03_on, null));
			break;
		}
	}
	


	 /**
	  * 이름 오름차순
	  * @author falbb
	  *
	  */
	class NameAscCompare implements Comparator<RecommandAppInformation> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(RecommandAppInformation arg0, RecommandAppInformation arg1) {
			// TODO Auto-generated method stub
			return arg0.getAppName().compareTo(arg1.getAppName());
		}

	}
	
	class NewestAscCompare implements Comparator<RecommandAppInformation> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(RecommandAppInformation arg0, RecommandAppInformation arg1) {
			// TODO Auto-generated method stub
			return arg0.getNewestOrder() > arg1.getNewestOrder() ? 1 : -1;
		}

	}
	
	class PopularAscCompare implements Comparator<RecommandAppInformation> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(RecommandAppInformation arg0, RecommandAppInformation arg1) {
			// TODO Auto-generated method stub
			return arg0.getPopularOrder() > arg1.getPopularOrder() ? 1 : -1;
		}

	}
	
	

}
