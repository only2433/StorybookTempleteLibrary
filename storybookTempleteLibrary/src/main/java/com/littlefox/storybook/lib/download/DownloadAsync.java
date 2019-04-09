package com.littlefox.storybook.lib.download;

import android.content.Context;
import android.os.AsyncTask;

import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.NetworkUtil;

public class DownloadAsync extends AsyncTask<Void, Integer, Object>
{
	protected static boolean isRunning = false;
	protected static Object mSync = new Object();
	protected Context mContext;
	protected AsyncListener mAsyncListener;
	protected String mDownloadUrl ="";
	protected String mSaveFilePath = "";
	
	public DownloadAsync(Context context, String downloadUrl, String saveFilePath, AsyncListener asyncListener)
	{
		mContext 			= context;
		mDownloadUrl		= downloadUrl;
		mSaveFilePath		= saveFilePath;
		mAsyncListener 		= asyncListener;	 
		
		
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningStart();
		}
	}
	
	@Override
	protected Object doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			return false;
		}
		
		synchronized (mSync)
		{
			isRunning = true;

			boolean result = NetworkUtil.downloadFile(mDownloadUrl, mSaveFilePath, mAsyncListener);

			if (result == false)
			{
				return false;
			}

		}
		
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values)
	{
		super.onProgressUpdate(values);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningProgress(values[0]);
		}
	}

	@Override
	protected void onCancelled(Object result)
	{
		super.onCancelled(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningCanceled();
		}
		
		isRunning = false;
	}
	
	@Override
	protected void onPostExecute(Object result)
	{
		super.onPostExecute(result);
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningEnd(result);
		}
		isRunning = false;
	}

}
