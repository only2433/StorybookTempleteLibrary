package com.littlefox.storybook.lib.async;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.NetworkUtil;
import com.littlefox.storybook.lib.object.VideoBaseResult;

public class VideoInformationAsync extends AsyncTask<Void, Integer, Boolean>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private AsyncListener mAsyncListener;
	private VideoBaseResult mVideoBaseResult = null;
	
	public VideoInformationAsync(Context context , AsyncListener asyncListener)
	{
		mContext = context;
		mAsyncListener = asyncListener;
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();

		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningStart();
		}
		
		isRunning = false;
	}
	
	
	@Override
	protected Boolean doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			Log.e("mbRunning : true");
			return false;
		}
		
		synchronized (mSync)
		{
			
			isRunning = true;
			String response = NetworkUtil.requestServerPair(mContext,Common.BASE_URI+Common.URL_STORY_LIST, null, NetworkUtil.POST_METHOD);
			mVideoBaseResult = new Gson().fromJson(response, VideoBaseResult.class);
		}
		
		return true;
	}

	
	@Override
	protected void onPostExecute(Boolean result)
	{
		super.onPostExecute(result);
		
		if(mAsyncListener != null)
		{
			if(result == true)
			{
				mAsyncListener.onRunningEnd(mVideoBaseResult);
			}
			else
			{
				mAsyncListener.onRunningEnd(null);
			}
			
		}
		isRunning = false;
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
	protected void onCancelled(Boolean result)
	{
		super.onCancelled(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningCanceled();
		}
		
		isRunning = false;
	}
}
