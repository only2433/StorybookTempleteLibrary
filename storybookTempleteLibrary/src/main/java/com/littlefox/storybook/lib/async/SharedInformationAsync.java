package com.littlefox.storybook.lib.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.NetworkUtil;
import com.littlefox.storybook.lib.object.SharedInformationResult;

import java.util.Locale;

public class SharedInformationAsync extends AsyncTask<Void, Integer, SharedInformationResult>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private AsyncListener mAsyncListener;
	private int mMovieIndex = 0;
	private String mAppName = "";
	
	public SharedInformationAsync(Context context, int index, String appName, AsyncListener asyncListener)
	{
		mContext 		= context;
		mMovieIndex 	= index;
		mAppName 		= appName;
		mAsyncListener 	= asyncListener;
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
	protected SharedInformationResult doInBackground(Void... params)
	{
		
		if(isRunning == true)
		{
			return null;
		}
		
		SharedInformationResult result;
		
		synchronized (mSync)
		{
			isRunning = true;
			
			try
			{
				/*JsonObject jsonRoot = new JsonObject();
				jsonRoot.addProperty("index", mMovieIndex);
				jsonRoot.addProperty("snsGubun", mAppName);
				jsonRoot.addProperty("locale", Locale.getDefault().toString());*/

				ContentValues list = new ContentValues();
				list.put("index", mMovieIndex);
				list.put("snsGubun", mAppName);
				list.put("locale", Locale.getDefault().toString());


				String response = NetworkUtil.requestServerPair(mContext, Common.BASE_URI+Common.URL_SNS_STORY_DESC, list, NetworkUtil.POST_METHOD);

				result = new Gson().fromJson(response, SharedInformationResult.class);
				
				if(result.code.equals("200") == false)
				{
					Log.f("SNS 정보 가져오기 성공");
					mAsyncListener.onErrorListener(result.code, result.message);
					return null;
				}
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
			
		}
		return result;
	}

	@Override
	protected void onPostExecute(SharedInformationResult result)
	{
		super.onPostExecute(result);
		
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningEnd(result);
		}
		isRunning = false;
	}

	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningCanceled();
		}
		
		isRunning = false;
	}
	
	

}
