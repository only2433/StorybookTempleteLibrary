package com.littlefox.storybook.lib.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.littlefox.storybook.lib.async.listener.AsyncListener;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.NetworkUtil;
import com.littlefox.storybook.lib.object.PromotionBaseResult;

public class PromotionInformationAsync extends AsyncTask<Void, Integer, PromotionBaseResult>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private AsyncListener mAsyncListener;
	private String mPromoCode;
	
	public PromotionInformationAsync(Context context , String promoCode, AsyncListener asyncListener)
	{
		mContext 		= context;
		mPromoCode		= promoCode;
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
	protected PromotionBaseResult doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			
			return null;
		}
		PromotionBaseResult result;
		synchronized (mSync)
		{
			isRunning = true;
			/*JsonObject jsonRoot = new JsonObject();
			jsonRoot.addProperty("promo_code", mPromoCode);*/

			ContentValues list = new ContentValues();
			list.put("promo_code", mPromoCode);

			try
			{
				String response = NetworkUtil.requestServerPair(mContext,Common.BASE_URI+Common.URL_PROMO_QUERY, list, NetworkUtil.POST_METHOD);
				result = new Gson().fromJson(response, PromotionBaseResult.class);
				
				
			}catch(Exception e)
			{
				return null;
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(PromotionBaseResult result)
	{
		super.onPostExecute(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningEnd(result);
		}
		isRunning = false;
	}

	@Override
	protected void onCancelled(PromotionBaseResult result)
	{
		super.onCancelled(result);
		
		if(mAsyncListener != null)
		{
			mAsyncListener.onRunningCanceled();
		}
		isRunning = false;
	}





}
