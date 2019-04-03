package com.littlefox.storybook.lib.analytics;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.littlefox.logmonitor.Log;


/**
 * 구글 애널리틱스를 사용하는  Helper 클래스
 * @author 정재현
 *
 */
public class GoogleAnalyticsHelper
{

	public static GoogleAnalyticsHelper sGoogleAnalyticsHelper = null;
	private FirebaseAnalytics mFirebaseAnalytics;
	private Context mContext;
	
	public static GoogleAnalyticsHelper getInstance(Context context)
	{
		if(sGoogleAnalyticsHelper == null)
		{
			sGoogleAnalyticsHelper = new GoogleAnalyticsHelper();
			sGoogleAnalyticsHelper.init(context);
		}
		return sGoogleAnalyticsHelper;
	}
	
	/**
	 * 구글 애널리틱스를 초기화 시킨다.
	 * @param context
	 */
	public void init(Context context)
	{
		mContext = context;
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
	}


	public void sendCurrentAppView(String information)
	{
		Log.f("information : "+ information);
		mFirebaseAnalytics.setCurrentScreen((Activity)mContext, information, null);
	}

	/**
	 * 현재 사용자의 이벤트를 전달한다.
	 * @param category 액티비티
	 * @param action 특정 행동
	 */
	public void sendCurrentEvent(String category, String action)
	{
		Log.i("Category : "+ category + ", Action : "+action);
		Bundle bundle = new Bundle();
		bundle.putString("action", action);
		mFirebaseAnalytics.logEvent(category, bundle);
	}

	/**
	 * 현재 사용자의 이벤트를 전달한다.
	 * @param category 액티비티
	 * @param action 특정 행동
	 * @param label 특정 정보
	 */
	public void sendCurrentEvent(String category, String action, String label)
	{
		Log.i("Category : "+ category + ", Action : "+action+", Label : "+label);
		Bundle bundle = new Bundle();
		bundle.putString("action", action);
		bundle.putString("label", label);
		mFirebaseAnalytics.logEvent(category, bundle);
	}
}
