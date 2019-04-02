package com.littlefox.storybook.lib.object;

import com.littlefox.logmonitor.Log;

public class VideoInformation
{
	/** 구매 전의 비디오 아이템 STATUS */
	public static final int STATUS_NOT_PURCHASED 		= 0;
	
	/** 다운로드 가능 상태의  비디오 아이템 STATUS */
	public static final int STATUS_DOWNLOAD_AVAILABLE	= 1;
	
	/** 다운로드 중인 비디오 아이템 STATUS */
	public static final int STATUS_DOWNLOAD_ING			= 2;
	
	/** 다운로드 대기 상태인 비디오 아이템 STATUS */
	public static final int STATUS_DOWNLOAD_IDLE		= 3;
	
	/** 다운로드 완료 됬을 때 비디오 아이템 STATUS */
	public static final int STATUS_DOWNLOAD_COMPLETE 	= 4;
	
	/** 플레이가 완료된 비디오 아이템 STATUS */
	public static final int STATUS_PLAY_COMPLETE		= 5;
	
	/**
	 * 상품의  FC ID
	 */
	private String mFcId = "";
	
	/**
	 * 상품 코드 명
	 */
	private String mPurchaseCode = "";
	
	/**
	 * 비디오  URL
	 */
	private String mVideoUrl = "";
	
	/**
	 * 썸네일 URL
	 */
	private String mThumbnailUrl = "";
	
	/**
	 * 자막 URL
	 */
	private String mCaptionUrl = "";
	
	/**
	 * 정보를 받는 URI Base Directory
	 */
	private String mBaseUri = "";
	
	/**
	 * 해당 비디오의 타이틀 
	 */
	private String mTitle = "";
	
	private int mCurrentStatus = STATUS_NOT_PURCHASED;
	
	/**
	 * 프리 아이템인지 아닌지
	 */
	private boolean isFreeItem = false;
	
	/**
	 * 프로모션아이템으로 사용했지는의 여부
	 */
	private boolean isPromotionUsed = false;
	
	
	/**
	 * 별점주기 관련하여 사용. 다음에 하기 클릭시 초기화 
	 */
	private boolean isPlayed = false;
	
	/**
	 * 파일이 변경되었는지의 시간 : 서버값과 비교하여 변경이 있을시에 유저에게 파일을 새로 받게 해야한다.
	 */
	private String mChangeDate = "";
	
	private int mDownloadProgress = 0;
	
	public VideoInformation(String fcId, String purchaseCode, String baseUri, String title, String changeDate)
	{
		mFcId			= fcId;
		mPurchaseCode 	= purchaseCode;
		mBaseUri 		= baseUri;
		mVideoUrl		= mBaseUri.substring(mBaseUri.lastIndexOf("/")+1, mBaseUri.length()) + ".mp4";
		mThumbnailUrl	= mBaseUri.substring(mBaseUri.lastIndexOf("/")+1, mBaseUri.length()) + ".png";
		mCaptionUrl		= mBaseUri.substring(mBaseUri.lastIndexOf("/")+1, mBaseUri.length()) + ".json";
		mBaseUri		= mBaseUri.substring(0, mBaseUri.lastIndexOf("/")+1);
		mTitle			= title;
		mChangeDate		= changeDate;
	}

	public void setStatus(int currentStatus)
	{
		mCurrentStatus = currentStatus;
		
		if(mCurrentStatus == STATUS_DOWNLOAD_AVAILABLE || mCurrentStatus == STATUS_DOWNLOAD_IDLE || mCurrentStatus == STATUS_DOWNLOAD_COMPLETE)
		{
			mDownloadProgress = 0;
		}
	}
	
	public void setFreeItem(boolean isFree)
	{
		isFreeItem = isFree;
	}
	
	public boolean isFreeItem()
	{
		return isFreeItem;
	}

	public int getStatus()
	{
		return mCurrentStatus;
	}
	
	public String getPurchaseCode()
	{
		return mPurchaseCode;
	}

	public void setPurchaseCode(String mPurchaseCode)
	{
		this.mPurchaseCode = mPurchaseCode;
	}

	public String getVideoUrl()
	{
		return mVideoUrl;
	}
	
	public String getDownloadVideoUrl()
	{
		return mBaseUri + mVideoUrl;
	}

	public void setVideoUrl(String mVideoUrl)
	{
		this.mVideoUrl = mVideoUrl;
	}

	public String getThumbnailUrl()
	{
		return mThumbnailUrl;
	}
	
	public String getDownloadThumbnailUrl()
	{
		return mBaseUri + mThumbnailUrl;
	}

	public void setThumbnailUrl(String mThumbnailUrl)
	{
		this.mThumbnailUrl = mThumbnailUrl;
	}

	public String getCaptionUrl()
	{
		return mCaptionUrl;
	}
	
	public String getDownloadCaptionUrl()
	{
		return mBaseUri + mCaptionUrl;
	}

	public void setCaptionUrl(String mCaptionUrl)
	{
		this.mCaptionUrl = mCaptionUrl;
	}

	public String getTitle()
	{
		return mTitle;
	}
	
	/**
	 * 타이틀을 리턴한다. 가끔씩 인덱스를 못찾는다. 재현이 안되서 try catch 
	 * @return
	 */
	public String getEpisodeTitle()
	{
		try
		{
			Log.f("mTitle : "+ mTitle);
			int startItemIndex = mTitle.indexOf(":");
			return mTitle.substring(startItemIndex+1, mTitle.length());
		}catch(Exception e)
		{
			return mTitle;
		}
	}


	public void setTitle(String mTitle)
	{
		this.mTitle = mTitle;
	}

	public String getFcId()
	{
		return mFcId;
	}

	public void setFcId(String mFcId)
	{
		this.mFcId = mFcId;
	}

	public boolean isPromotionUsed()
	{
		return isPromotionUsed;
	}

	public void setPromotionUsed(boolean isPromotionUsed)
	{
		this.isPromotionUsed = isPromotionUsed;
	}

	public boolean isPlayed()
	{
		return isPlayed;
	}

	public void setPlayed(boolean isPlayed)
	{
		this.isPlayed = isPlayed;
	}
	
	public void setChangeDate(String date)
	{
		this.mChangeDate = date;
	}
	
	public String getChangeDate()
	{
		return mChangeDate;
	}

	public int getDownloadProgress()
	{
		return mDownloadProgress;
	}

	public void setDownloadProgress(int mDownloadProgress)
	{
		this.mDownloadProgress = mDownloadProgress;
	}
	
	

}
