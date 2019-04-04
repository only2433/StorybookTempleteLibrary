package com.littlefox.storybook.lib.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;

import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.NetworkUtil;
import com.littlefox.storybook.lib.object.InitItemResult;

import java.util.Locale;

public class InitAppAsync extends AsyncTask<Void, Integer, InitItemResult>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private AsyncListener mAsyncListener;

	public InitAppAsync(Context context,  AsyncListener asyncListener)
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
	protected InitItemResult doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			Log.e("mbRunning : true");
			return null;
		}
		
		InitItemResult itemResult;
		synchronized (mSync) 
		{
			isRunning = true;

			ContentValues list = new ContentValues();
			list.put("uuid", Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID));
			list.put("device_name", android.os.Build.MODEL);
			list.put("device_type", CommonUtils.getInstance(mContext).isTablet() == false ? "Phone" : "Tablet");
			list.put("device_os", "Android");
			list.put("locale", Locale.getDefault().toString());
			list.put("push_addr", (String) CommonUtils.getInstance(mContext).getSharedPreference(Common.PARAMS_GCM_REGISTERATION_ID, Common.TYPE_PARAMS_STRING));
			
			String response = NetworkUtil.requestServerPair(mContext,Common.BASE_URI+ Common.URL_INIT_APP, list, NetworkUtil.POST_METHOD);
			
			itemResult = new Gson().fromJson(response, InitItemResult.class);
			
			CommonUtils.getInstance(mContext).setSharedPreference(Common.PARAMS_APP_USER_PK, itemResult.device_id);
			
			
			if(itemResult.code.equals("200") == false)
			{
				mAsyncListener.onErrorListener(itemResult.code, itemResult.message);
				return null;
			}
			
		}
		return itemResult;
	}

	@Override
	protected void onPostExecute(InitItemResult result)
	{
		super.onPostExecute(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningEnd(result);
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
	protected void onCancelled(InitItemResult result)
	{
		super.onCancelled(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningCanceled();
		}
		
		isRunning = false;
	}
}
