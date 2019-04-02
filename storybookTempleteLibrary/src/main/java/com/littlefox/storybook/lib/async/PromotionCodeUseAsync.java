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

public class PromotionCodeUseAsync extends AsyncTask<Void, Integer, Boolean>
{
	private static boolean isRunning = false;
	private static Object mSync = new Object();
	private Context mContext;
	private String mUseProductCode = "";
	private String mPromotionCode = "";
	public PromotionCodeUseAsync(Context context,String promotionCode)
	{
		mContext		= context;
		mPromotionCode 	= promotionCode;
	}
	
	public PromotionCodeUseAsync(Context context,String promotionCode,String productCode)
	{
		mContext		= context;
		mPromotionCode	= promotionCode;
		mUseProductCode = productCode;
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		isRunning = false;
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
				jsonRoot.addProperty("promo_code", mPromotionCode);
				jsonRoot.addProperty("device_id", (String) CommonUtils.getInstance(mContext).getSharedPreference(Common.PARAMS_APP_USER_PK, Common.TYPE_PARAMS_STRING));
				
				if(mUseProductCode.equals("") == false)
					jsonRoot.addProperty("product_code", mUseProductCode);*/
				ContentValues list = new ContentValues();
				list.put("promo_code", mPromotionCode);
				list.put("device_id", (String) CommonUtils.getInstance(mContext).getSharedPreference(Common.PARAMS_APP_USER_PK, Common.TYPE_PARAMS_STRING));

				if(mUseProductCode.equals("") == false)
				{
					list.put("product_code", mUseProductCode);
				}

				String response = NetworkUtil.requestServerPair(mContext,Common.BASE_URI+ Common.URL_PROMO_USE, list, NetworkUtil.POST_METHOD);
				ItemResult result = new Gson().fromJson(response, ItemResult.class);
				
				if(result.code.equals("200"))
				{
					Log.f("프로모션 코드가 적용되는걸 서버에서 확인");
					return true;
				}
				else
				{
					return false;
				}
					
				
			}catch(Exception e)
			{
				Log.i("Exception e : "+ e.getMessage());
				return false;
			}
		}
		
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
