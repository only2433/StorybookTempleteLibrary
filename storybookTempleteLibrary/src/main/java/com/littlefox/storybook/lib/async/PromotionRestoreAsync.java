package com.littlefox.storybook.lib.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.NetworkUtil;
import com.littlefox.storybook.lib.object.PromotionRestoreResult;

public class PromotionRestoreAsync extends AsyncTask<Void, Integer, PromotionRestoreResult>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private AsyncListener mAsyncListener;
	
	public PromotionRestoreAsync(Context context, AsyncListener asyncListener)
	{
		mContext 		= context;
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
	protected PromotionRestoreResult doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			return null;
		}
		PromotionRestoreResult result;
		
		synchronized (mSync)
		{
			isRunning = true;

			ContentValues list = new ContentValues();
			list.put("device_id", (String)CommonUtils.getInstance(mContext).getSharedPreference(Common.PARAMS_APP_USER_PK, Common.TYPE_PARAMS_STRING));

			try
			{
				String response = NetworkUtil.requestServerPair(mContext, Common.BASE_URI+Common.URL_RESTORE_LIST, list, NetworkUtil.POST_METHOD);
				result = new Gson().fromJson(response, PromotionRestoreResult.class);
			}catch(Exception e)
			{
				return null;
			}
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(PromotionRestoreResult result)
	{
		super.onPostExecute(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningEnd(result);
		}
		isRunning = false;
	}

	@Override
	protected void onCancelled(PromotionRestoreResult result)
	{
		super.onCancelled(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningCanceled();
		}
		isRunning = false;
	}
	
	

}
