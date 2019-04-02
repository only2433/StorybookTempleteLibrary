package com.littlefox.storybook.lib.object;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.littlefox.library.system.common.FileUtils;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;

import android.content.Context;


public class SharedVideoInfo
{
	private ArrayList<VideoInformation> mVideoInformation = new ArrayList<VideoInformation>();
	
	/**
	 * 현재 다운로드 중인 아이템 의 Posiion
	 */
	private int mCurrentDownloadPosition = -1;
	
	/** 전체결제를 위한 IAP Code */
	private String mProductAlSku = "";
	
	/**
	 * 다운로드 해야하는 Queue에 넣어있는 리스트
	 */
	private ArrayList<Integer> mQueueDownloadList = new ArrayList<Integer>();
	
	/**
	 * 별점주기를 완료 했는 지의 여부
	 */
	private boolean isRateComplete = false;
	
	public SharedVideoInfo()
	{
		mVideoInformation = new ArrayList<VideoInformation>(); 
	}
	
	public void setVideoInfoList(ArrayList<VideoInformation> list)
	{
		mVideoInformation = list;
	}
	
	public ArrayList<VideoInformation> getVideoInfoList()
	{
		return mVideoInformation;
	}
	
	/**
	 * 해당 IAP코드에 맞는 Video 리스트의 포지션을 가져온다.
	 * @param sku 검색할 iap 코드
	 * @return 선택된 포지션
	 */
	public int getVideoInfoPosition(String sku)
	{
		for(int  i = 0; i < mVideoInformation.size() ; i++)
		{
			if(sku.equals(mVideoInformation.get(i).getPurchaseCode()) == true)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	
	public void addVideoInformation(VideoInformation videoInformation)
	{
		mVideoInformation.add(videoInformation);
	}
	
	/**
	 * 해당 다운로드 포지션이 다운로드 Queue에 있는 지 알려준다.
	 * @param index 해당 인덱스
	 * @return
	 */
	public boolean haveDownloadQueue(int index)
	{
		return mQueueDownloadList.contains(index) == true ? true : false;
	}
	
	/**
	 * 해당 index가 현재 다운로드 중인 아이템인지 확인
	 * @param index
	 * @return TRUE : 다운로드 중</p> FALSE : 다운로드 중이 아님
	 */
	public boolean isDownloadingItem(int index)
	{
		return mVideoInformation.get(index).getStatus() == VideoInformation.STATUS_DOWNLOAD_ING ? true : false;
	}
	
	/**
	 * 전체 결제에 대한 해당 SKU 를 저장
	 * @param productAllSku
	 */
	public void setProductAllSku(String productAllSku)
	{
		mProductAlSku = productAllSku;
	}
	
	public String getProductAllSku()
	{
		return mProductAlSku;
	}
	
	/**
	 * 큐리스트에 다운로드 해야하는 아이템을 추가한다.
	 * @param index 추가할 리스트의 인덱스
	 */
	public void addQueueItem(int index)
	{
		if(mCurrentDownloadPosition == -1)
		{
			mCurrentDownloadPosition = index;
			mVideoInformation.get(mCurrentDownloadPosition).setStatus(VideoInformation.STATUS_DOWNLOAD_ING);
		}
		else if(mCurrentDownloadPosition != index)
		{
			mVideoInformation.get(index).setStatus(VideoInformation.STATUS_DOWNLOAD_IDLE);
		}	
		mQueueDownloadList.add(index);
	}
	
	/**
	 * 결제 한 아이템을 다운로드 가능한 상태로 변경
	 * @param index 다운로드 가능할 상태로 만들 index
	 */
	public void setPublishItem(int index)
	{
		mVideoInformation.get(index).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
	}
	
	/**
	 * 결제 한 아이템인지 체크한다.
	 * @param index 해당 선택한 인덱스
	 * @return TRUE : 결제된 아이템</p>FALSE : 결제되지 않는 아이템
	 */
	public boolean isPurchaseItem(int index)
	{
		if(mVideoInformation.get(index).getStatus() == VideoInformation.STATUS_NOT_PURCHASED)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 큐에 다운로드 해야하는 아이템이 있는 지 체크 한다.<P>
	 * 있으면 다운로드 해야하는 아이템의 인텍스를 반환 <P>
	 * 아닐경우 -1 을 리턴
	 * @return
	 */
	public int pullDownloadItem()
	{
		if(mQueueDownloadList.size() > 0)
		{
			mCurrentDownloadPosition = mQueueDownloadList.get(0);
			mVideoInformation.get(mCurrentDownloadPosition).setStatus(VideoInformation.STATUS_DOWNLOAD_ING);
		}
		else
		{
			mCurrentDownloadPosition = -1;	
		}
		
		return mCurrentDownloadPosition;
	}
	
	/**
	 * 동영상 플레이가 끝난 아이템의 Status 를 변경
	 * @param index 리스트의 인덱스
	 */
	public void CompletePlayItem(int index)
	{
		mVideoInformation.get(index).setStatus(VideoInformation.STATUS_PLAY_COMPLETE);
	}
	
	/**
	 * 동영상 플레이를 한 아이템을 플레이했다고 세팅
	 * @param index 해당 인덱스
	 */
	public void setPlayedItem(int index)
	{
		mVideoInformation.get(index).setPlayed(true);
	}

	/**
	 * 다운로드가 완료되면 호출하여 큐를 삭제한다.
	 */
	public void deleteQueueDownloadItem()
	{
		if(mQueueDownloadList.size() > 0)
		{
			mVideoInformation.get(mQueueDownloadList.get(0)).setStatus(VideoInformation.STATUS_DOWNLOAD_COMPLETE);
			mQueueDownloadList.remove(0);
		}
	}
	
	
	
	/**
	 * 다운로드 대기중인 Queue 의 아이템을 취소한다.
	 * @param index
	 */
	public void cancelQueueDownloadIdleItem(int index)
	{
		for(int i = 0 ; i < mQueueDownloadList.size(); i++)
		{
			if(mQueueDownloadList.get(i) == index)
			{
				mQueueDownloadList.remove(i);
				mVideoInformation.get(index).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
				break;
			}
		}
	}
	
	/**
	 * 다운로드 대기중 상태나 다운로드 중인 아이템을 다운로드 가능 상태로 되돌린다
	 */
	public void initDownloadItem()
	{
		mQueueDownloadList = new ArrayList<Integer>();
		
		for(int i = 0 ; i < mVideoInformation.size(); i++)
		{
			if(mVideoInformation.get(i).getStatus() == VideoInformation.STATUS_DOWNLOAD_ING || mVideoInformation.get(i).getStatus() == VideoInformation.STATUS_DOWNLOAD_IDLE)
			{
				mVideoInformation.get(i).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
			}
		}
	}
	
	/**
	 * 플레이 가능한 상태인지 확인
	 * @param position
	 * @return
	 */
	public boolean isPlayAvailableItem(int position)
	{
		if(mVideoInformation.get(position).getStatus() == VideoInformation.STATUS_DOWNLOAD_COMPLETE || mVideoInformation.get(position).getStatus() == VideoInformation.STATUS_PLAY_COMPLETE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 서버 아이템이 변경 될 경우 다운로드 가능으로 상태로 바꾸고, 
	 * @param serverVideoListInfo
	 * @return
	 */
	public ArrayList<Integer> processChangeDateItem(SharedVideoInfo serverVideoListInfo)
	{
		ArrayList<Integer> changeItemIndexList = new ArrayList<Integer>();
		String deleteThumbnailPath = "";
		int deleteThumbnailIndex = 0;
		for(int i = 0; i < mVideoInformation.size(); i++)
		{
			if(isPlayAvailableItem(i))
			{
				if(mVideoInformation.get(i).getChangeDate().compareTo(serverVideoListInfo.getVideoInfoList().get(i).getChangeDate()) < 0)
				{
					deleteThumbnailIndex = i + 1;
					
					Log.f("Change Item Video Url : "+ serverVideoListInfo.getVideoInfoList().get(i).getDownloadVideoUrl());
					Log.f("Change Item Local Saved Time : "+ mVideoInformation.get(i).getChangeDate());
					Log.f("Change Item Server Saved Time : "+ serverVideoListInfo.getVideoInfoList().get(i).getChangeDate()+"\n");
					
					mVideoInformation.get(i).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
					FileUtils.deleteFile(StorybookTempleteAPI.PATH_MP4+ mVideoInformation.get(i).getVideoUrl());
					FileUtils.deleteFile(StorybookTempleteAPI.PATH_JSON+ mVideoInformation.get(i).getCaptionUrl());
					
					if(deleteThumbnailIndex < 10)
					{
						deleteThumbnailPath = StorybookTempleteAPI.PATH_THUMBNAIL + "thumbnail_0" + deleteThumbnailIndex + ".jpg";
					}
					else
					{
						deleteThumbnailPath = StorybookTempleteAPI.PATH_THUMBNAIL + "thumbnail_"+ deleteThumbnailIndex + ".jpg";
					}
					Log.f("Delete Item Thumbnail : "+ (StorybookTempleteAPI.PATH_THUMBNAIL + deleteThumbnailPath));
					FileUtils.deleteFile(StorybookTempleteAPI.PATH_THUMBNAIL + deleteThumbnailPath);
					changeItemIndexList.add(i);
					
					mVideoInformation.get(i).setChangeDate(serverVideoListInfo.getVideoInfoList().get(i).getChangeDate());
				}
			}
		}
		
		return changeItemIndexList;
	}
	
	/**
	 * 플레이가 끝나 현재 플레이되는 아이템 인덱스를 설정
	 */
	public void setPlayComplete(int index)
	{
		mVideoInformation.get(index).setStatus(VideoInformation.STATUS_PLAY_COMPLETE);
	}

	/**
	 * 다운로드 를 해야하는 리스트 사이즈를 구한다.
	 * @return
	 */
	public int getDownloadItemSize()
	{
		return mQueueDownloadList.size();
	}
	
	/**
	 * 현재 결제 되는 Sku를 리턴
	 * @param currentSku
	 */
	public void setPayComplete(String currentSku)
	{
		for(int i = 0 ; i < mVideoInformation.size(); i++)
		{
			if(mVideoInformation.get(i).getPurchaseCode().equals(currentSku))
			{
				mVideoInformation.get(i).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
			}
		}
	}
	
	/**
	 * Appraisal Popup이 떠야하는 지에 대한 여부 확인
	 * @return 
	 */
	public boolean isAppraisalTiming(Context context)
	{
		final int MAX_PERMIT_COUNT = (int) CommonUtils.getInstance(context).getSharedPreference(Common.PARAMS_FREE_ITEM_COUNT, Common.TYPE_PARAMS_INTEGER, 10);
		
		Log.i("MAX_PERMIT_COUNT : "+MAX_PERMIT_COUNT);
		int count = 0;
		
		for(int i = 0; i < mVideoInformation.size(); i++)
		{
			if(mVideoInformation.get(i).isPlayed() == true)
			{
				Log.i("played index : "+ i);
				count++;
			}
		}
		
		if(count >= MAX_PERMIT_COUNT)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Video정보중에 play 되었던 item을 play하지 않은 걸로 초기화
	 */
	public void setInitPlayed()
	{
		for(int i = 0; i < mVideoInformation.size(); i++)
		{
			mVideoInformation.get(i).setPlayed(false);
		}
	}
	
	/**
	 * 다운되어 있는 비디오를 삭제한다.
	 */
	public void deleteVideoAll()
	{
		mQueueDownloadList = new ArrayList<Integer>();
		mCurrentDownloadPosition = -1;
		
		for(int i = 0 ; i < mVideoInformation.size(); i++)
		{
			if(mVideoInformation.get(i).getStatus() == VideoInformation.STATUS_DOWNLOAD_COMPLETE)
			{
				Log.f("VideoInformation.STATUS_DOWNLOAD_AVAILABLE index : "+ i);
				mVideoInformation.get(i).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
				
				File file = new File(StorybookTempleteAPI.PATH_JSON+mVideoInformation.get(i).getCaptionUrl());
				if(file.exists() == true)
				{
					file.delete();
				}
				
				file = new File(StorybookTempleteAPI.PATH_MP4+mVideoInformation.get(i).getVideoUrl());
				if(file.exists() == true)
				{
					file.delete();
				}
			}
		}
	}
	
	/**
	 * 결제 안된 모든 아이템을 결제 된것으로 변경, 프로모션 아이템이면 프로모션 아이템임을 명시해준다.
	 * @param type ITEM_TYPE_PAID : 결제한 아이템, ITEM_TYPE_PROMOTION : 프로모션 아이템
	 */
	public void payAllItem(int type)
	{
		for(int i = 0 ; i < mVideoInformation.size(); i++)
		{
			if(mVideoInformation.get(i).getStatus() == VideoInformation.STATUS_NOT_PURCHASED)
			{
				if(type == Common.ITEM_TYPE_PROMOTION)
				{
					mVideoInformation.get(i).setPromotionUsed(true);
				}
				mVideoInformation.get(i).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
			}
		}
	}
	
	/**
	 * 결제 해야하는 아이템을 결제된것으로  변경 
	 * @param skuList 결제 리스트
	 * 
	 */
	public void payPurchaseItem(List<String> skuList,int type)
	{
		if(skuList == null)
		{
			return;
		}
		
		if(skuList.contains(StorybookTempleteAPI.ALL_SKU_ITEM_NAME))
		{
			for(int i = 0 ; i < skuList.size(); i++)
			{
				for(int j = 0 ; j < mVideoInformation.size(); j++)
				{
					if(mVideoInformation.get(j).getStatus() == VideoInformation.STATUS_NOT_PURCHASED)
					{
						if(type == Common.ITEM_TYPE_PROMOTION)
						{
							Log.f("프로모션 아이템 복구 : "+ mVideoInformation.get(j).getPurchaseCode());
							mVideoInformation.get(j).setPromotionUsed(true);
						}
						else
						{
							Log.f("지불한 아이템 복구 : "+ mVideoInformation.get(j).getPurchaseCode());
						}
						mVideoInformation.get(j).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
					}
				}
			}
		}
		else
		{
			for(int i = 0 ; i < skuList.size(); i++)
			{
				for(int j = 0 ; j < mVideoInformation.size(); j++)
				{
					if(mVideoInformation.get(j).getStatus() == VideoInformation.STATUS_NOT_PURCHASED)
					{
						if(mVideoInformation.get(j).getPurchaseCode().equals(skuList.get(i)))
						{
							if(type == Common.ITEM_TYPE_PROMOTION)
							{
								Log.f("프로모션 아이템 복구 : "+ mVideoInformation.get(j).getPurchaseCode());
								mVideoInformation.get(j).setPromotionUsed(true);
							}
							else
							{
								Log.f("지불한 아이템 복구 : "+ mVideoInformation.get(j).getPurchaseCode());
							}
							mVideoInformation.get(j).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
							break;
						}
					}
				}
			}
		}
		
		
	}
	
	
	/**
	 * 결제 해야하는 아이템을 결제된것으로  변경 
	 * @param skuList 결제 리스트
	 */
	public void payPurchaseItem(String sku)
	{
		if (sku == null)
		{
			return;
		}

		for (int i = 0; i < mVideoInformation.size(); i++)
		{
			if (mVideoInformation.get(i).getStatus() == VideoInformation.STATUS_NOT_PURCHASED)
			{
				if (mVideoInformation.get(i).getPurchaseCode().equals(sku))
				{
					mVideoInformation.get(i).setStatus(VideoInformation.STATUS_DOWNLOAD_AVAILABLE);
					break;
				}
			}
		}

	}

	/**
	 * 결제된 아이템을 취소했을 시 복구하기 위한 메소드
	 * @param skuList 결제 리스트
	 */
	public void restoreCancelingPurchasedItem(List<String> skuList)
	{
		if(skuList == null)
		{
			return;
		}
		
		if(skuList.contains(StorybookTempleteAPI.ALL_SKU_ITEM_NAME))
		{
			return;
		}
		
		boolean isCurrentPurchasedItem = false;
		for(int i = 0 ; i < mVideoInformation.size(); i++)
		{
			
			if(mVideoInformation.get(i).getStatus() != VideoInformation.STATUS_NOT_PURCHASED && mVideoInformation.get(i).isFreeItem() == false && mVideoInformation.get(i).isPromotionUsed() == false)
			{
				isCurrentPurchasedItem = false;
				for(String sku : skuList)
				{
					if(mVideoInformation.get(i).getPurchaseCode().equals(sku))
					{
						isCurrentPurchasedItem = true;
					}
				}
				
				if(isCurrentPurchasedItem == false)
				{
					cancelQueueDownloadIdleItem(i);
					mVideoInformation.get(i).setStatus(VideoInformation.STATUS_NOT_PURCHASED);
					File file = new File(StorybookTempleteAPI.PATH_JSON+mVideoInformation.get(i).getCaptionUrl());
					if(file.exists() == true)
					{
						file.delete();
					}
					
					file = new File(StorybookTempleteAPI.PATH_MP4+mVideoInformation.get(i).getVideoUrl());
					if(file.exists() == true)
					{
						file.delete();
					}
				}
			}
		}
	}

	public boolean isRateComplete()
	{
		return isRateComplete;
	}

	public void setRateComplete(boolean isRateComplete)
	{
		this.isRateComplete = isRateComplete;
	}
	
	
	
}
