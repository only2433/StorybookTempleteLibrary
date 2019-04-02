package com.littlefox.storybook.lib.download;

import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.CommonUtils;

import android.content.Context;

public class ReturnIndexDownloadAsync extends DownloadAsync
{
	private int mDownloadItemIndex = -1;
	public ReturnIndexDownloadAsync(Context context, String downloadUrl, String saveFilePath, int downloadItemIndex,  AsyncListener asyncListener)
	{
		super(context, downloadUrl, saveFilePath, asyncListener);
		mDownloadItemIndex = downloadItemIndex;
	}

	@Override
	protected Object doInBackground(Void... params)
	{
		synchronized (mSync)
		{
			boolean result = CommonUtils.getInstance(mContext).downloadFile(mDownloadUrl, mSaveFilePath);

			if (result == false)
			{
				mAsyncListener.onErrorListener("-1", "Network Error");
				return -1;
			}

		}
		
		return mDownloadItemIndex;
	}

	@Override
	protected void onPostExecute(Object result)
	{
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningEnd(result);
		}
	}
	
	
	
	

}
