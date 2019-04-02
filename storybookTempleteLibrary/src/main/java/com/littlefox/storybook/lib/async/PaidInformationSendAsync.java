package com.littlefox.storybook.lib.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.common.Common;
import com.littlefox.storybook.lib.common.CommonUtils;
import com.littlefox.storybook.lib.common.NetworkUtil;
import com.littlefox.storybook.lib.object.ItemResult;

public class PaidInformationSendAsync extends AsyncTask<Void, Integer, Boolean>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private String mPaidItemCode = "";
	
	public PaidInformationSendAsync(Context context, String paidItemCode)
	{
		mContext			= context; 
		mPaidItemCode 	= paidItemCode;
	}
	
	@Override
	protected Boolean doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			return null;
		}
		
		synchronized (mSync)
		{
			isRunning = true;
			
			try
			{
				/*JsonObject jsonRoot = new JsonObject();
				jsonRoot.addProperty("device_id", (String) CommonUtils.getInstance(mContext).getSharedPreference(Common.PARAMS_APP_USER_PK, Common.TYPE_PARAMS_STRING));
				jsonRoot.addProperty("product_code", mPaidItemCode);*/

				ContentValues list = new ContentValues();
				list.put("device_id", (String) CommonUtils.getInstance(mContext).getSharedPreference(Common.PARAMS_APP_USER_PK, Common.TYPE_PARAMS_STRING));
				list.put("product_code", mPaidItemCode);

				String response = NetworkUtil.requestServerPair(mContext, Common.BASE_URI+Common.URL_STORE_USE, list, NetworkUtil.POST_METHOD);
				ItemResult result = new Gson().fromJson(response, ItemResult.class);
				
				if(result.code.equals("200"))
				{
					Log.f("결제 코드가 제대로 전달됨");
					return true;
				}
				else
				{
					return false;
				}
				
			}catch(Exception e)
			{
				return false;
			}
		}

	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		isRunning = false;
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		super.onPostExecute(result);
		isRunning = false;
	}

	@Override
	protected void onCancelled(Boolean result)
	{
		super.onCancelled(result);
		isRunning = false;
	}
	
	

}
